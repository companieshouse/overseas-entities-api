package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.RelevantStatementsType;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataToReviewDao;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateDaoTest {

    private UpdateDao updateDaoUnderTest;

    @BeforeEach
    void setUp() {
        updateDaoUnderTest = new UpdateDao();
    }

    @Test
    void testChangeBORelevantPeriodGetterAndSetter() {
        final RelevantStatementsType changeBORelevantPeriod = RelevantStatementsType.CHANGE_BO_RELEVANT_PERIOD;
        updateDaoUnderTest.setChangeBORelevantPeriod(changeBORelevantPeriod);
        assertThat(updateDaoUnderTest.getChangeBORelevantPeriod()).isEqualTo(changeBORelevantPeriod);
    }

    @Test
    void testTrusteeInvolvedRelevantPeriodGetterAndSetter() {
        final RelevantStatementsType trusteeInvolvedRelevantPeriod = RelevantStatementsType.CHANGE_BO_RELEVANT_PERIOD;
        updateDaoUnderTest.setTrusteeInvolvedRelevantPeriod(trusteeInvolvedRelevantPeriod);
        assertThat(updateDaoUnderTest.getTrusteeInvolvedRelevantPeriod()).isEqualTo(trusteeInvolvedRelevantPeriod);
    }

    @Test
    void testChangeBeneficiaryRelevantPeriodGetterAndSetter() {
        final RelevantStatementsType changeBeneficiaryRelevantPeriod = RelevantStatementsType.CHANGE_BO_RELEVANT_PERIOD;
        updateDaoUnderTest.setChangeBeneficiaryRelevantPeriod(changeBeneficiaryRelevantPeriod);
        assertThat(updateDaoUnderTest.getChangeBeneficiaryRelevantPeriod()).isEqualTo(changeBeneficiaryRelevantPeriod);
    }

    @Test
    void testDateOfCreationGetterAndSetter() {
        final LocalDate dateOfCreation = LocalDate.of(2020, 1, 1);
        updateDaoUnderTest.setDateOfCreation(dateOfCreation);
        assertThat(updateDaoUnderTest.getDateOfCreation()).isEqualTo(dateOfCreation);
    }

    @Test
    void testFilingDateGetterAndSetter() {
        final LocalDate filingDate = LocalDate.of(2020, 1, 1);
        updateDaoUnderTest.setFilingDate(filingDate);
        assertThat(updateDaoUnderTest.getFilingDate()).isEqualTo(filingDate);
    }

    @Test
    void testBoMoDataFetchedGetterAndSetter() {
        final boolean boMoDataFetched = false;
        updateDaoUnderTest.setBoMoDataFetched(boMoDataFetched);
        assertThat(updateDaoUnderTest.isBoMoDataFetched()).isFalse();
    }

    @Test
    void testRegistrableBeneficialOwnerGetterAndSetter() {
        final Boolean registrableBeneficialOwner = false;
        updateDaoUnderTest.setRegistrableBeneficialOwner(registrableBeneficialOwner);
        assertThat(updateDaoUnderTest.getRegistrableBeneficialOwner()).isFalse();
    }

    @Test
    void testNoChangeGetterAndSetter() {
        final Boolean noChange = false;
        updateDaoUnderTest.setNoChange(noChange);
        assertThat(updateDaoUnderTest.getNoChange()).isFalse();
    }

    @Test
    void testTrustDataFetchedGetterAndSetter() {
        final boolean trustDataFetched = false;
        updateDaoUnderTest.setTrustDataFetched(trustDataFetched);
        assertThat(updateDaoUnderTest.isTrustDataFetched()).isFalse();
    }

    @Test
    void testReviewBeneficialOwnersIndividualGetterAndSetter() {
        final List<BeneficialOwnerIndividualDao> reviewBeneficialOwnersIndividual = List.of(
                new BeneficialOwnerIndividualDao());
        updateDaoUnderTest.setReviewBeneficialOwnersIndividual(reviewBeneficialOwnersIndividual);
        assertThat(updateDaoUnderTest.getReviewBeneficialOwnersIndividual())
                .isEqualTo(reviewBeneficialOwnersIndividual);
    }

    @Test
    void testReviewBeneficialOwnersCorporateGetterAndSetter() {
        final List<BeneficialOwnerCorporateDao> reviewBeneficialOwnersCorporate = List.of(
                new BeneficialOwnerCorporateDao());
        updateDaoUnderTest.setReviewBeneficialOwnersCorporate(reviewBeneficialOwnersCorporate);
        assertThat(updateDaoUnderTest.getReviewBeneficialOwnersCorporate()).isEqualTo(reviewBeneficialOwnersCorporate);
    }

    @Test
    void testReviewBeneficialOwnersGovernmentOrPublicAuthorityGetterAndSetter() {
        final List<BeneficialOwnerGovernmentOrPublicAuthorityDao> reviewBeneficialOwnersGovernmentOrPublicAuthority = List.of(
                new BeneficialOwnerGovernmentOrPublicAuthorityDao());
        updateDaoUnderTest.setReviewBeneficialOwnersGovernmentOrPublicAuthority(
                reviewBeneficialOwnersGovernmentOrPublicAuthority);
        assertThat(updateDaoUnderTest.getReviewBeneficialOwnersGovernmentOrPublicAuthority())
                .isEqualTo(reviewBeneficialOwnersGovernmentOrPublicAuthority);
    }

    @Test
    void testReviewManagingOfficersIndividualGetterAndSetter() {
        final List<ManagingOfficerIndividualDao> reviewManagingOfficersIndividual = List.of(
                new ManagingOfficerIndividualDao());
        updateDaoUnderTest.setReviewManagingOfficersIndividual(reviewManagingOfficersIndividual);
        assertThat(updateDaoUnderTest.getReviewManagingOfficersIndividual())
                .isEqualTo(reviewManagingOfficersIndividual);
    }

    @Test
    void testReviewManagingOfficersCorporateGetterAndSetter() {
        final List<ManagingOfficerCorporateDao> reviewManagingOfficersCorporate = List.of(
                new ManagingOfficerCorporateDao());
        updateDaoUnderTest.setReviewManagingOfficersCorporate(reviewManagingOfficersCorporate);
        assertThat(updateDaoUnderTest.getReviewManagingOfficersCorporate()).isEqualTo(reviewManagingOfficersCorporate);
    }

    @Test
    void testReviewTrustsGetterAndSetter() {
        final List<TrustDataToReviewDao> reviewTrusts = List.of(new TrustDataToReviewDao());
        updateDaoUnderTest.setReviewTrusts(reviewTrusts);
        assertThat(updateDaoUnderTest.getReviewTrusts()).isEqualTo(reviewTrusts);
    }
}
