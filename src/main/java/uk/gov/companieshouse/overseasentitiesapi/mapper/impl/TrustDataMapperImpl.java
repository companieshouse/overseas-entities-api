package uk.gov.companieshouse.overseasentitiesapi.mapper.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.mapper.TrustDataMapper;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.CorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.HistoricalBoDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.IndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TrustDataMapperImpl implements TrustDataMapper {

    private static final String INDIVIDUAL_SETTLERS = "Individual Settlers";
    private static final String INDIVIDUAL_GRANTORS = "Individual Grantors";
    private static final String INDIVIDUAL_BENEFICIARIES = "Individual Beneficiaries";
    private static final String INDIVIDUAL_INTERESTED_PERSONS = "Individual Interested Persons";

    private static final String CORPORATE_SETTLERS = "Corporate Settlers";
    private static final String CORPORATE_GRANTORS = "Corporate Grantors";
    private static final String CORPORATE_BENEFICIARIES = "Corporate Beneficiaries";
    private static final String CORPORATE_INTERESTED_PERSONS = "Corporate Interested Persons";

    public TrustDataDao dtoToDao(TrustDataDto trustDataDto) throws ServiceException {
        TrustDataDao trustDataDao = new TrustDataDao();
        trustDataDao.setName(trustDataDto.getTrustName());
        trustDataDao.setTrustCreationDate(
            LocalDate.of(
                Integer.parseInt(trustDataDto.getTrustCreationDateYear()),
                Integer.parseInt(trustDataDto.getTrustCreationDateMonth()),
                Integer.parseInt(trustDataDto.getTrustCreationDateDay())
            ));
        trustDataDao.setUnableToObtainAllTrustInfo(Boolean.parseBoolean(trustDataDto.getUnableToObtainAllTrustInfo()));
        setBeneficialOwners(trustDataDto, trustDataDao);

        return trustDataDao;
    }

    private void setBeneficialOwners(TrustDataDto trustDataDto, TrustDataDao trustDataDao) throws ServiceException {
        List<BeneficialOwnerDao> grantors = new ArrayList<>();
        List<BeneficialOwnerDao> settlers = new ArrayList<>();
        List<BeneficialOwnerDao> beneficiaries = new ArrayList<>();
        List<BeneficialOwnerDao> interestedPersons = new ArrayList<>();

        for (IndividualDto individualDto : trustDataDto.getIndividualDtos()) {
            switch (individualDto.getType()) {
                case INDIVIDUAL_GRANTORS:
                    grantors.add(individualDtoToBeneficialOwnerDao(individualDto));
                    break;
                case INDIVIDUAL_SETTLERS:
                    settlers.add(individualDtoToBeneficialOwnerDao(individualDto));
                    break;
                case INDIVIDUAL_BENEFICIARIES:
                    beneficiaries.add(individualDtoToBeneficialOwnerDao(individualDto));
                    break;
                case INDIVIDUAL_INTERESTED_PERSONS:
                    interestedPersons.add(individualDtoToBeneficialOwnerDao(individualDto));
                    break;
                default:
                    throw new ServiceException("Invalid beneficial owner type received: " + individualDto.getType());
            }
        }

        if (trustDataDto.getCorporateDtos() != null && !trustDataDto.getCorporateDtos().isEmpty()) {
            for (CorporateDto corporateDto : trustDataDto.getCorporateDtos()) {
                switch (corporateDto.getType()) {
                    case CORPORATE_GRANTORS:
                        grantors.add(corporateDtoToBeneficialOwner(corporateDto));
                        break;
                    case CORPORATE_SETTLERS:
                        settlers.add(corporateDtoToBeneficialOwner(corporateDto));
                        break;
                    case CORPORATE_BENEFICIARIES:
                        beneficiaries.add(corporateDtoToBeneficialOwner(corporateDto));
                        break;
                    case CORPORATE_INTERESTED_PERSONS:
                        interestedPersons.add(corporateDtoToBeneficialOwner(corporateDto));
                        break;
                    default:
                        throw new ServiceException("Invalid beneficial owner type received: " + corporateDto.getType());
                }
            }
        }

        List<BeneficialOwnerDao> historicalBoDtoList = new ArrayList<>();
        if (trustDataDto.getHistoricalBoDtos() != null && !trustDataDto.getHistoricalBoDtos().isEmpty()) {
            for (HistoricalBoDto historicalBoDto : trustDataDto.getHistoricalBoDtos()) {
                historicalBoDtoList.add(historicalBoDtoToBeneficialOwner(historicalBoDto));
            }
        }

        trustDataDao.setGrantors(grantors);
        trustDataDao.setSettlers(settlers);
        trustDataDao.setBeneficiaries(beneficiaries);
        trustDataDao.setInterestedPersons(interestedPersons);
        trustDataDao.setHistoricalBeneficialOwners(historicalBoDtoList);
    }

    private BeneficialOwnerDao individualDtoToBeneficialOwnerDao(IndividualDto individualDto) {
        BeneficialOwnerDao beneficialOwnerDao = new BeneficialOwnerDao();
        beneficialOwnerDao.setType(individualDto.getType());
        beneficialOwnerDao.setForename(individualDto.getForename());
        beneficialOwnerDao.setOtherForenames(individualDto.getOtherForenames());
        beneficialOwnerDao.setSurname(individualDto.getSurname());
        beneficialOwnerDao.setDateOfBirth(
            LocalDate.of(
                Integer.parseInt(individualDto.getDobYear()),
                Integer.parseInt(individualDto.getDobMonth()),
                Integer.parseInt(individualDto.getDobDay())
            ));
        beneficialOwnerDao.setNationality(individualDto.getNationality());
        AddressDao serviceAddress = createAddress(
                individualDto.getSaAddressPremises(),
                individualDto.getSaAddressLine1(),
                individualDto.getSaAddressLine2(),
                individualDto.getSaAddressRegion(),
                individualDto.getSaAddressLocality(),
                individualDto.getSaAddressCountry(),
                individualDto.getSaAddressPostalCode(),
                individualDto.getSaAddressCareOf(),
                individualDto.getSaAddressPoBox());
        beneficialOwnerDao.setServiceAddress(serviceAddress);
        AddressDao ura = createAddress(
                individualDto.getUraAddressPremises(),
                individualDto.getUraAddressLine1(),
                individualDto.getUraAddressLine2(),
                individualDto.getUraAddressRegion(),
                individualDto.getUraAddressLocality(),
                individualDto.getUraAddressCountry(),
                individualDto.getUraAddressPostalCode(),
                individualDto.getUraAddressCareOf(),
                individualDto.getUraAddressPoBox());
        beneficialOwnerDao.setUsualResidentialAddress(ura);
        beneficialOwnerDao.setDateBecameInterestedPerson(
            LocalDate.of(
                Integer.parseInt(individualDto.getDateBecameInterestedPersonYear()),
                Integer.parseInt(individualDto.getDateBecameInterestedPersonMonth()),
                Integer.parseInt(individualDto.getDateBecameInterestedPersonDay())
            ));

        return beneficialOwnerDao;
    }

    private BeneficialOwnerDao corporateDtoToBeneficialOwner(CorporateDto corporateDto) {
        BeneficialOwnerDao beneficialOwnerDao = new BeneficialOwnerDao();
        beneficialOwnerDao.setType(corporateDto.getType());
        beneficialOwnerDao.setName(corporateDto.getName());
        AddressDao serviceAddress = createAddress(
                corporateDto.getSaAddressPremises(),
                corporateDto.getSaAddressLine1(),
                corporateDto.getSaAddressLine2(),
                corporateDto.getSaAddressRegion(),
                corporateDto.getSaAddressLocality(),
                corporateDto.getSaAddressCountry(),
                corporateDto.getSaAddressPostalCode(),
                corporateDto.getSaAddressCareOf(),
                corporateDto.getSaAddressPoBox());
        beneficialOwnerDao.setServiceAddress(serviceAddress);
        AddressDao ro = createAddress(
                corporateDto.getRoAddressPremises(),
                corporateDto.getRoAddressLine1(),
                corporateDto.getRoAddressLine2(),
                corporateDto.getRoAddressRegion(),
                corporateDto.getRoAddressLocality(),
                corporateDto.getRoAddressCountry(),
                corporateDto.getRoAddressPostalCode(),
                corporateDto.getRoAddressCareOf(),
                corporateDto.getRoAddressPoBox());
        beneficialOwnerDao.setUsualResidentialAddress(ro);
        beneficialOwnerDao.setDateBecameInterestedPerson(
                LocalDate.of(
                        Integer.parseInt(corporateDto.getDateBecameInterestedPersonYear()),
                        Integer.parseInt(corporateDto.getDateBecameInterestedPersonMonth()),
                        Integer.parseInt(corporateDto.getDateBecameInterestedPersonDay())
                ));
        beneficialOwnerDao.setIdentificationCountryRegistration(corporateDto.getIdentificationCountryRegistration());
        beneficialOwnerDao.setIdentificationLegalRegistration(corporateDto.getIdentificationLegalRegistration());
        beneficialOwnerDao.setIdentificationLegalForm(corporateDto.getIdentificationLegalForm());
        beneficialOwnerDao.setIdentificationPlaceRegistered(corporateDto.getIdentificationPlaceRegistered());
        beneficialOwnerDao.setIdentificationRegistrationNumber(corporateDto.getIdentificationRegistrationNumber());

        return beneficialOwnerDao;
    }

    private BeneficialOwnerDao historicalBoDtoToBeneficialOwner(HistoricalBoDto historicalBoDto) {
        BeneficialOwnerDao beneficialOwnerDao = new BeneficialOwnerDao();
        beneficialOwnerDao.setForename(historicalBoDto.getForename());
        beneficialOwnerDao.setOtherForenames(historicalBoDto.getOtherForenames());
        beneficialOwnerDao.setSurname(historicalBoDto.getSurname());
        beneficialOwnerDao.setCeasedDate(
            LocalDate.of(
                Integer.parseInt(historicalBoDto.getCeasedDateYear()),
                Integer.parseInt(historicalBoDto.getCeasedDateMonth()),
                Integer.parseInt(historicalBoDto.getCeasedDateDay())
            )
        );

        return beneficialOwnerDao;
    }

    private AddressDao createAddress(String propertyNameNumber, String line1, String line2, String county, String locality, String country, String postcode, String careOf, String poBox) {
        AddressDao address = new AddressDao();
        address.setPropertyNameNumber(propertyNameNumber);
        address.setLine1(line1);
        address.setLine2(line2);
        address.setCounty(county);
        address.setLocality(locality);
        address.setCountry(country);
        address.setCareOf(careOf);
        address.setPoBox(poBox);
        address.setPostcode(postcode);

        return address;
    }
}
