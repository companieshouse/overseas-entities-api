package uk.gov.companieshouse.overseasentitiesapi.mocks;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.api.model.company.foreigncompany.ForeignCompanyDetailsApi;
import uk.gov.companieshouse.api.model.company.foreigncompany.OriginatingRegistryApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;

public class CollatedOverseasEntityDataMock {
    private static final String EXISTING_COMPANY_NAME = "Existing name";
    private static final String EXISTING_PRINCIPAL_ADDRESS_COUNTRY = "Existing Country (Principal)";
    private static final String EXISTING_SERVICE_ADDRESS_COUNTRY = "Existing Country (Service)";
    private static final String EXISTING_LEGAL_FORM = "Existing legal form";
    private static final String EXISTING_GOVERNED_BY = "Existing governed by";
    private static final String EXISTING_REGISTRY_NAME = "Existing registry name,Existing Jurisdiction";
    private static final String EXISTING_SUBMISSION_REGISTRY_NAME = "Existing registry name";
    private static final String EXISTING_SUBMISSION_JURISDICTION = "Existing Jurisdiction";
    private static final String EXISTING_REGISTRATION_NUMBER = "Existing registration number";
    private static final String EXISTING_INCORPORATION_COUNTRY = "Incorporation country";
    private static final String EXISTING_EMAIL = "Existing email";
    private static final String UPDATED_COMPANY_NAME = "Updated name";
    private static final String UPDATED_ADDRESS_COUNTRY = "Updated Country";
    private static final String UPDATED_LEGAL_FORM = "Updated legal form";
    private static final String UPDATED_GOVERNED_BY = "Updated governed by";
    private static final String UPDATED_REGISTRY_NAME = "Updated registry name";
    private static final String UPDATED_REGISTRATION_NUMBER = "Updated registration number";
    private static final String UPDATED_EMAIL = "Updated email";

    public static Pair<CompanyProfileApi, OverseasEntityDataApi> getExistingRegistrationAllData() {
        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = new ImmutablePair<>(
                new CompanyProfileApi() {{
                    setCompanyName(EXISTING_COMPANY_NAME);
                    setRegisteredOfficeAddress(
                            new RegisteredOfficeAddressApi() {{
                                setCountry(EXISTING_PRINCIPAL_ADDRESS_COUNTRY);
                            }}
                    );
                    setServiceAddress(
                            new RegisteredOfficeAddressApi() {{
                                setCountry(EXISTING_SERVICE_ADDRESS_COUNTRY);
                            }}
                    );
                    setForeignCompanyDetails(
                            new ForeignCompanyDetailsApi() {{
                                setLegalForm(EXISTING_LEGAL_FORM);
                                setGovernedBy(EXISTING_GOVERNED_BY);
                                setOriginatingRegistry(
                                        new OriginatingRegistryApi() {{
                                            setCountry(EXISTING_INCORPORATION_COUNTRY);
                                            setName(EXISTING_REGISTRY_NAME);
                                            setRegistrationNumber(EXISTING_REGISTRATION_NUMBER);
                                        }}
                                );
                            }}
                    );
                }},
                new OverseasEntityDataApi() {{
                    setEmail(EXISTING_EMAIL);
                }});

        return existingRegistration;
    }

    public static OverseasEntitySubmissionDto getNoChangeUpdateSubmission() {
        OverseasEntitySubmissionDto updateSubmission = new OverseasEntitySubmissionDto() {{
            setEntityName(new EntityNameDto() {{
                setName(EXISTING_COMPANY_NAME);
            }});
            setEntity(new EntityDto() {{
                setPrincipalAddress(new AddressDto() {{
                    setCountry(EXISTING_PRINCIPAL_ADDRESS_COUNTRY);
                }});
                setServiceAddress(new AddressDto() {{
                    setCountry(EXISTING_SERVICE_ADDRESS_COUNTRY);
                }});
                setLegalForm(EXISTING_LEGAL_FORM);
                setLawGoverned(EXISTING_GOVERNED_BY);
                setIncorporationCountry(EXISTING_INCORPORATION_COUNTRY);
                setPublicRegisterName(EXISTING_SUBMISSION_REGISTRY_NAME);
                setPublicRegisterJurisdiction(EXISTING_SUBMISSION_JURISDICTION);
                setRegistrationNumber(EXISTING_REGISTRATION_NUMBER);
                setEmail(EXISTING_EMAIL);
            }});
        }};

        return updateSubmission;
    }

    public static OverseasEntitySubmissionDto getUpdateSubmissionAllDataDifferent() {
        OverseasEntitySubmissionDto updateSubmission = new OverseasEntitySubmissionDto() {{
            setEntityName(new EntityNameDto() {{
                setName(UPDATED_COMPANY_NAME);
            }});
            setEntity(new EntityDto() {{
                setPrincipalAddress(new AddressDto() {{
                    setCountry(UPDATED_ADDRESS_COUNTRY);
                }});
                setServiceAddress(new AddressDto() {{
                    setCountry(UPDATED_ADDRESS_COUNTRY);
                }});
                setLegalForm(UPDATED_LEGAL_FORM);
                setLawGoverned(UPDATED_GOVERNED_BY);
                setIncorporationCountry(UPDATED_ADDRESS_COUNTRY);
                setPublicRegisterName(UPDATED_REGISTRY_NAME);
                setRegistrationNumber(UPDATED_REGISTRATION_NUMBER);
                setEmail(UPDATED_EMAIL);
            }});
        }};

        return updateSubmission;
    }

    public static OverseasEntitySubmissionDto getUpdateSubmissionEntityNameDifferent() {
        OverseasEntitySubmissionDto updateSubmission = getNoChangeUpdateSubmission();
        updateSubmission.getEntityName().setName(UPDATED_COMPANY_NAME);

        return updateSubmission;
    }

    public static OverseasEntitySubmissionDto getUpdateSubmissionCorrespondenceAddressDifferent() {
        OverseasEntitySubmissionDto updateSubmission = getNoChangeUpdateSubmission();

        updateSubmission.getEntity().setServiceAddress(
                new AddressDto() {{
                    setCountry(UPDATED_ADDRESS_COUNTRY);
                }});

        return updateSubmission;
    }

    public static OverseasEntitySubmissionDto getUpdateSubmissionCompanyIdentificationDifferent() {
        OverseasEntitySubmissionDto updateSubmission = getNoChangeUpdateSubmission();
        updateSubmission.getEntity().setLegalForm(UPDATED_LEGAL_FORM);
        updateSubmission.getEntity().setLawGoverned(UPDATED_GOVERNED_BY);
        updateSubmission.getEntity().setIncorporationCountry(UPDATED_ADDRESS_COUNTRY);
        updateSubmission.getEntity().setPublicRegisterName(UPDATED_REGISTRY_NAME);
        updateSubmission.getEntity().setRegistrationNumber(UPDATED_REGISTRATION_NUMBER);

        return updateSubmission;
    }

    public static OverseasEntitySubmissionDto getUpdateSubmissionEntityEmailDifferent() {
        OverseasEntitySubmissionDto updateSubmission = getNoChangeUpdateSubmission();
        updateSubmission.getEntity().setEmail(UPDATED_EMAIL);

        return updateSubmission;
    }
}
