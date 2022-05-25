package uk.gov.companieshouse.overseasentitiesapi.mapper.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.mapper.TrustDataMapper;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.IndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TrustDataMapperImpl implements TrustDataMapper {

    private static final String INDIVIDUAL_SETTLERS = "Individual Settlers";
    private static final String INDIVIDUAL_GRANTORS = "Individual Grantors";
    private static final String INDIVIDUAL_BENEFICIARIES = "Individual Beneficiaries";
    private static final String INDIVIDUAL_INTERESTED_PERSONS = "Individual Interested Persons";

    public TrustDataDao dtoToDao(TrustDataDto trustDataDto) {
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

    private void setBeneficialOwners(TrustDataDto trustDataDto, TrustDataDao trustDataDao) {
        List<BeneficialOwnerDao> grantors = new ArrayList<>();
        List<BeneficialOwnerDao> settlers = new ArrayList<>();
        List<BeneficialOwnerDao> beneficiaries = new ArrayList<>();
        List<BeneficialOwnerDao> interestedPersons = new ArrayList<>();
        for (IndividualDto individualDto : trustDataDto.getIndividualDtos()) {
            System.out.println("**************************");
            System.out.println(individualDto.getType());

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
                    // TODO error
                    System.out.println("ERROR"); // TODO
            }
        }

        trustDataDao.setGrantors(grantors);
        trustDataDao.setSettlers(settlers);
        trustDataDao.setBeneficiaries(beneficiaries);
        trustDataDao.setInterestedPersons(interestedPersons);
    }

    private BeneficialOwnerDao individualDtoToBeneficialOwnerDao(IndividualDto individualDto) {
        BeneficialOwnerDao beneficialOwnerDao = new BeneficialOwnerDao();
        beneficialOwnerDao.setType(individualDto.getType().toString());
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

    private AddressDao createAddress(String propertyNameNumber, String line1, String line2, String county, String locality, String postcode, String careOf, String poBox) {
        AddressDao address = new AddressDao();
        address.setPropertyNameNumber(propertyNameNumber);
        address.setLine1(line1);
        address.setLine2(line2);
        address.setCounty(county);
        address.setLocality(locality);
        address.setCareOf(careOf);
        address.setPoBox(poBox);
        address.setPostcode(postcode);

        return address;
    }
}
