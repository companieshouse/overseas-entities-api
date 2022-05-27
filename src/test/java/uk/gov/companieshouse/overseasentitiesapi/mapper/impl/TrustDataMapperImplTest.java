package uk.gov.companieshouse.overseasentitiesapi.mapper.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.CorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.HistoricalBoDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.IndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TrustDataMapperImplTest {
    private static final String TRUST_NAME = "trust_name";
    private static final String UNABLE_TO_OBTAIN_ALL_TRUST_INFO = "false";

    private static final String DAY = "01";
    private static final String MONTH = "01";
    private static final String YEAR = "2001";

    private static final String FORENAME = "forename";
    private static final String OTHER_FORENAMES = "otherForenames";
    private static final String SURNAME = "surname";

    private static final String INDIVIDUAL_TYPE = "Individual Grantors";

    private static final String NATIONALITY = "nationality";
    private static final String ADDRESS_LINE1 = "line1";
    private static final String ADDRESS_LINE2 = "line2";
    private static final String ADDRESS_CARE_OF = "careOf";
    private static final String ADDRESS_COUNTRY = "country";
    private static final String ADDRESS_PO_BOX = "pobox";
    private static final String ADDRESS_POST_CODE = "postcode";
    private static final String ADDRESS_PREMISES = "premises";
    private static final String ADDRESS_REGION = "region";
    private static final String ADDRESS_LOCALITY = "locality";

    private static final String CORPORATE_TYPE = "Corporate Settlers";
    private static final String IDENTIFICATION_COUNTRY_REGISTRATION = "country";
    private static final String IDENTIFICATION_LEGAL_REGISTRATION = "legal";
    private static final String IDENTIFICATION_LEGAL_FORM = "form";
    private static final String IDENTIFICATION_PLACE_REGISTERED = "place";
    private static final String IDENTIFICATION_REGISTRATION_NUMBER = "number";

    private TrustDataMapperImpl trustDataMapper = new TrustDataMapperImpl();

    @Test
    void dtoToDao_Successful() throws ServiceException {
        TrustDataDto trustDataDto = generateTrustDataDto();

        TrustDataDao trustDataDao = trustDataMapper.dtoToDao(trustDataDto);

        assertEquals(TRUST_NAME, trustDataDao.getName());

        assertEquals(Integer.parseInt(YEAR), trustDataDao.getTrustCreationDate().getYear());
        assertEquals(Integer.parseInt(MONTH), trustDataDao.getTrustCreationDate().getMonthValue());
        assertEquals(Integer.parseInt(DAY), trustDataDao.getTrustCreationDate().getDayOfMonth());

        assertFalse(trustDataDao.getUnableToObtainAllTrustInfo());

        assertEquals(1, trustDataDao.getHistoricalBeneficialOwners().size());
        assertEquals(FORENAME, trustDataDao.getHistoricalBeneficialOwners().get(0).getForename());
        assertEquals(OTHER_FORENAMES, trustDataDao.getHistoricalBeneficialOwners().get(0).getOtherForenames());
        assertEquals(SURNAME, trustDataDao.getHistoricalBeneficialOwners().get(0).getSurname());
        assertEquals(Integer.parseInt(YEAR), trustDataDao.getHistoricalBeneficialOwners().get(0).getCeasedDate().getYear());
        assertEquals(Integer.parseInt(MONTH), trustDataDao.getHistoricalBeneficialOwners().get(0).getCeasedDate().getMonthValue());
        assertEquals(Integer.parseInt(DAY), trustDataDao.getHistoricalBeneficialOwners().get(0).getCeasedDate().getDayOfMonth());

        assertEquals(1, trustDataDao.getGrantors().size());
        assertEquals(INDIVIDUAL_TYPE, trustDataDao.getGrantors().get(0).getType());
        assertEquals(FORENAME, trustDataDao.getGrantors().get(0).getForename());
        assertEquals(OTHER_FORENAMES, trustDataDao.getGrantors().get(0).getOtherForenames());
        assertEquals(SURNAME, trustDataDao.getGrantors().get(0).getSurname());
        assertEquals(Integer.parseInt(YEAR), trustDataDao.getGrantors().get(0).getDateOfBirth().getYear());
        assertEquals(Integer.parseInt(MONTH), trustDataDao.getGrantors().get(0).getDateOfBirth().getMonthValue());
        assertEquals(Integer.parseInt(DAY), trustDataDao.getGrantors().get(0).getDateOfBirth().getDayOfMonth());
        assertEquals(NATIONALITY, trustDataDao.getGrantors().get(0).getNationality());
        assertEquals(ADDRESS_LINE1, trustDataDao.getGrantors().get(0).getServiceAddress().getLine1());
        assertEquals(ADDRESS_LINE2, trustDataDao.getGrantors().get(0).getServiceAddress().getLine2());
        assertEquals(ADDRESS_CARE_OF, trustDataDao.getGrantors().get(0).getServiceAddress().getCareOf());
        assertEquals(ADDRESS_COUNTRY, trustDataDao.getGrantors().get(0).getServiceAddress().getCountry());
        assertEquals(ADDRESS_LOCALITY, trustDataDao.getGrantors().get(0).getServiceAddress().getLocality());
        assertEquals(ADDRESS_PO_BOX, trustDataDao.getGrantors().get(0).getServiceAddress().getPoBox());
        assertEquals(ADDRESS_POST_CODE, trustDataDao.getGrantors().get(0).getServiceAddress().getPostcode());
        assertEquals(ADDRESS_PREMISES, trustDataDao.getGrantors().get(0).getServiceAddress().getPropertyNameNumber());
        assertEquals(ADDRESS_REGION, trustDataDao.getGrantors().get(0).getServiceAddress().getCounty());
        assertEquals(ADDRESS_LINE1, trustDataDao.getGrantors().get(0).getUsualResidentialAddress().getLine1());
        assertEquals(ADDRESS_LINE2, trustDataDao.getGrantors().get(0).getUsualResidentialAddress().getLine2());
        assertEquals(ADDRESS_CARE_OF, trustDataDao.getGrantors().get(0).getUsualResidentialAddress().getCareOf());
        assertEquals(ADDRESS_COUNTRY, trustDataDao.getGrantors().get(0).getUsualResidentialAddress().getCountry());
        assertEquals(ADDRESS_LOCALITY, trustDataDao.getGrantors().get(0).getUsualResidentialAddress().getLocality());
        assertEquals(ADDRESS_PO_BOX, trustDataDao.getGrantors().get(0).getUsualResidentialAddress().getPoBox());
        assertEquals(ADDRESS_POST_CODE, trustDataDao.getGrantors().get(0).getUsualResidentialAddress().getPostcode());
        assertEquals(ADDRESS_PREMISES, trustDataDao.getGrantors().get(0).getUsualResidentialAddress().getPropertyNameNumber());
        assertEquals(ADDRESS_REGION, trustDataDao.getGrantors().get(0).getUsualResidentialAddress().getCounty());
        assertEquals(Integer.parseInt(YEAR), trustDataDao.getGrantors().get(0).getDateBecameInterestedPerson().getYear());
        assertEquals(Integer.parseInt(MONTH), trustDataDao.getGrantors().get(0).getDateBecameInterestedPerson().getMonthValue());
        assertEquals(Integer.parseInt(DAY), trustDataDao.getGrantors().get(0).getDateBecameInterestedPerson().getDayOfMonth());

        assertEquals(1, trustDataDao.getSettlers().size());
        assertEquals(CORPORATE_TYPE, trustDataDao.getSettlers().get(0).getType());
        assertEquals(FORENAME, trustDataDao.getSettlers().get(0).getName());
        assertEquals(Integer.parseInt(YEAR), trustDataDao.getSettlers().get(0).getDateBecameInterestedPerson().getYear());
        assertEquals(Integer.parseInt(MONTH), trustDataDao.getSettlers().get(0).getDateBecameInterestedPerson().getMonthValue());
        assertEquals(Integer.parseInt(DAY), trustDataDao.getSettlers().get(0).getDateBecameInterestedPerson().getDayOfMonth());
        assertEquals(ADDRESS_LINE1, trustDataDao.getSettlers().get(0).getRegisteredOfficeAddress().getLine1());
        assertEquals(ADDRESS_LINE2, trustDataDao.getSettlers().get(0).getRegisteredOfficeAddress().getLine2());
        assertEquals(ADDRESS_CARE_OF, trustDataDao.getSettlers().get(0).getRegisteredOfficeAddress().getCareOf());
        assertEquals(ADDRESS_COUNTRY, trustDataDao.getSettlers().get(0).getRegisteredOfficeAddress().getCountry());
        assertEquals(ADDRESS_LOCALITY, trustDataDao.getSettlers().get(0).getRegisteredOfficeAddress().getLocality());
        assertEquals(ADDRESS_PO_BOX, trustDataDao.getSettlers().get(0).getRegisteredOfficeAddress().getPoBox());
        assertEquals(ADDRESS_POST_CODE, trustDataDao.getSettlers().get(0).getRegisteredOfficeAddress().getPostcode());
        assertEquals(ADDRESS_PREMISES, trustDataDao.getSettlers().get(0).getRegisteredOfficeAddress().getPropertyNameNumber());
        assertEquals(ADDRESS_REGION, trustDataDao.getSettlers().get(0).getRegisteredOfficeAddress().getCounty());
        assertEquals(ADDRESS_LINE1, trustDataDao.getSettlers().get(0).getServiceAddress().getLine1());
        assertEquals(ADDRESS_LINE2, trustDataDao.getSettlers().get(0).getServiceAddress().getLine2());
        assertEquals(ADDRESS_CARE_OF, trustDataDao.getSettlers().get(0).getServiceAddress().getCareOf());
        assertEquals(ADDRESS_COUNTRY, trustDataDao.getSettlers().get(0).getServiceAddress().getCountry());
        assertEquals(ADDRESS_LOCALITY, trustDataDao.getSettlers().get(0).getServiceAddress().getLocality());
        assertEquals(ADDRESS_PO_BOX, trustDataDao.getSettlers().get(0).getServiceAddress().getPoBox());
        assertEquals(ADDRESS_POST_CODE, trustDataDao.getSettlers().get(0).getServiceAddress().getPostcode());
        assertEquals(ADDRESS_PREMISES, trustDataDao.getSettlers().get(0).getServiceAddress().getPropertyNameNumber());
        assertEquals(ADDRESS_REGION, trustDataDao.getSettlers().get(0).getServiceAddress().getCounty());
        assertEquals(IDENTIFICATION_COUNTRY_REGISTRATION, trustDataDao.getSettlers().get(0).getIdentificationCountryRegistration());
        assertEquals(IDENTIFICATION_LEGAL_REGISTRATION, trustDataDao.getSettlers().get(0).getIdentificationLegalRegistration());
        assertEquals(IDENTIFICATION_LEGAL_FORM, trustDataDao.getSettlers().get(0).getIdentificationLegalForm());
        assertEquals(IDENTIFICATION_PLACE_REGISTERED, trustDataDao.getSettlers().get(0).getIdentificationPlaceRegistered());
        assertEquals(IDENTIFICATION_REGISTRATION_NUMBER, trustDataDao.getSettlers().get(0).getIdentificationRegistrationNumber());
    }

    @Test
    void dtoToDao_ServiceException() throws ServiceException {
        TrustDataDto trustDataDto = generateTrustDataDto();
        trustDataDto.getIndividualDtos().get(0).setType("invalid");

        assertThrows(ServiceException.class, () -> trustDataMapper.dtoToDao(trustDataDto));
    }

    private TrustDataDto generateTrustDataDto() {
        TrustDataDto trustDataDto = new TrustDataDto();

        trustDataDto.setTrustName(TRUST_NAME);
        trustDataDto.setTrustCreationDateDay(DAY);
        trustDataDto.setTrustCreationDateMonth(MONTH);
        trustDataDto.setTrustCreationDateYear(YEAR);
        trustDataDto.setUnableToObtainAllTrustInfo(UNABLE_TO_OBTAIN_ALL_TRUST_INFO);

        List<IndividualDto> individualDtos = new ArrayList<>();
        individualDtos.add(generateIndividualDto());
        trustDataDto.setIndividualDtos(individualDtos);

        List<HistoricalBoDto> historicalBoDtos = new ArrayList<>();
        historicalBoDtos.add(generateHistoricalBoDto());
        trustDataDto.setHistoricalBoDtos(historicalBoDtos);

        List<CorporateDto> corporateDtos = new ArrayList<>();
        corporateDtos.add(generateCorporateDto());
        trustDataDto.setCorporateDtos(corporateDtos);

        return trustDataDto;
    }

    private IndividualDto generateIndividualDto() {
        IndividualDto individualDto = new IndividualDto();
        individualDto.setType(INDIVIDUAL_TYPE);

        individualDto.setForename(FORENAME);
        individualDto.setOtherForenames(OTHER_FORENAMES);
        individualDto.setSurname(SURNAME);

        individualDto.setDobDay(DAY);
        individualDto.setDobMonth(MONTH);
        individualDto.setDobYear(YEAR);

        individualDto.setNationality(NATIONALITY);

        individualDto.setSaAddressLine1(ADDRESS_LINE1);
        individualDto.setSaAddressLine2(ADDRESS_LINE2);
        individualDto.setSaAddressCareOf(ADDRESS_CARE_OF);
        individualDto.setSaAddressCountry(ADDRESS_COUNTRY);
        individualDto.setSaAddressLocality(ADDRESS_LOCALITY);
        individualDto.setSaAddressPoBox(ADDRESS_PO_BOX);
        individualDto.setSaAddressPostalCode(ADDRESS_POST_CODE);
        individualDto.setSaAddressPremises(ADDRESS_PREMISES);
        individualDto.setSaAddressRegion(ADDRESS_REGION);

        individualDto.setUraAddressLine1(ADDRESS_LINE1);
        individualDto.setUraAddressLine2(ADDRESS_LINE2);
        individualDto.setUraAddressCareOf(ADDRESS_CARE_OF);
        individualDto.setUraAddressCountry(ADDRESS_COUNTRY);
        individualDto.setUraAddressLocality(ADDRESS_LOCALITY);
        individualDto.setUraAddressPoBox(ADDRESS_PO_BOX);
        individualDto.setUraAddressPostalCode(ADDRESS_POST_CODE);
        individualDto.setUraAddressPremises(ADDRESS_PREMISES);
        individualDto.setUraAddressRegion(ADDRESS_REGION);

        individualDto.setDateBecameInterestedPersonDay(DAY);
        individualDto.setDateBecameInterestedPersonMonth(MONTH);
        individualDto.setDateBecameInterestedPersonYear(YEAR);

        return individualDto;
    }

    private CorporateDto generateCorporateDto() {
        CorporateDto corporateDto = new CorporateDto();
        corporateDto.setType(CORPORATE_TYPE);

        corporateDto.setName(FORENAME);
        corporateDto.setDateBecameInterestedPersonDay(DAY);
        corporateDto.setDateBecameInterestedPersonMonth(MONTH);
        corporateDto.setDateBecameInterestedPersonYear(YEAR);

        corporateDto.setRoAddressLine1(ADDRESS_LINE1);
        corporateDto.setRoAddressLine2(ADDRESS_LINE2);
        corporateDto.setRoAddressCareOf(ADDRESS_CARE_OF);
        corporateDto.setRoAddressCountry(ADDRESS_COUNTRY);
        corporateDto.setRoAddressLocality(ADDRESS_LOCALITY);
        corporateDto.setRoAddressPoBox(ADDRESS_PO_BOX);
        corporateDto.setRoAddressPostalCode(ADDRESS_POST_CODE);
        corporateDto.setRoAddressPremises(ADDRESS_PREMISES);
        corporateDto.setRoAddressRegion(ADDRESS_REGION);

        corporateDto.setSaAddressLine1(ADDRESS_LINE1);
        corporateDto.setSaAddressLine2(ADDRESS_LINE2);
        corporateDto.setSaAddressCareOf(ADDRESS_CARE_OF);
        corporateDto.setSaAddressCountry(ADDRESS_COUNTRY);
        corporateDto.setSaAddressLocality(ADDRESS_LOCALITY);
        corporateDto.setSaAddressPoBox(ADDRESS_PO_BOX);
        corporateDto.setSaAddressPostalCode(ADDRESS_POST_CODE);
        corporateDto.setSaAddressPremises(ADDRESS_PREMISES);
        corporateDto.setSaAddressRegion(ADDRESS_REGION);

        corporateDto.setIdentificationCountryRegistration(IDENTIFICATION_COUNTRY_REGISTRATION);
        corporateDto.setIdentificationLegalRegistration(IDENTIFICATION_LEGAL_REGISTRATION);
        corporateDto.setIdentificationLegalForm(IDENTIFICATION_LEGAL_FORM);
        corporateDto.setIdentificationPlaceRegistered(IDENTIFICATION_PLACE_REGISTERED);
        corporateDto.setIdentificationRegistrationNumber(IDENTIFICATION_REGISTRATION_NUMBER);

        return corporateDto;
    }

    private HistoricalBoDto generateHistoricalBoDto() {
        HistoricalBoDto historicalBoDto = new HistoricalBoDto();

        historicalBoDto.setForename(FORENAME);
        historicalBoDto.setOtherForenames(OTHER_FORENAMES);
        historicalBoDto.setSurname(SURNAME);

        historicalBoDto.setCeasedDateDay(DAY);
        historicalBoDto.setCeasedDateMonth(MONTH);
        historicalBoDto.setCeasedDateYear(YEAR);

        return historicalBoDto;
    }
}
