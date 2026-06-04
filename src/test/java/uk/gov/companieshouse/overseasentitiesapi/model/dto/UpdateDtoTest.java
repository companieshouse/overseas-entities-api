package uk.gov.companieshouse.overseasentitiesapi.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import uk.gov.companieshouse.overseasentitiesapi.model.RelevantStatementsType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataToReviewDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateDtoTest {

    private UpdateDto updateDtoUnderTest;

    @BeforeEach
    void setUp() {
        updateDtoUnderTest = new UpdateDto();
    }

    @Test
    void testChangeBORelevantPeriodGetterAndSetter() {
        final RelevantStatementsType changeBORelevantPeriod = RelevantStatementsType.CHANGE_BO_RELEVANT_PERIOD;
        updateDtoUnderTest.setChangeBORelevantPeriod(changeBORelevantPeriod);
        assertThat(updateDtoUnderTest.getChangeBORelevantPeriod()).isEqualTo(changeBORelevantPeriod);
    }

    @Test
    void testTrusteeInvolvedRelevantPeriodGetterAndSetter() {
        final RelevantStatementsType trusteeInvolvedRelevantPeriod = RelevantStatementsType.CHANGE_BO_RELEVANT_PERIOD;
        updateDtoUnderTest.setTrusteeInvolvedRelevantPeriod(trusteeInvolvedRelevantPeriod);
        assertThat(updateDtoUnderTest.getTrusteeInvolvedRelevantPeriod()).isEqualTo(trusteeInvolvedRelevantPeriod);
    }

    @Test
    void testChangeBeneficiaryRelevantPeriodGetterAndSetter() {
        final RelevantStatementsType changeBeneficiaryRelevantPeriod = RelevantStatementsType.CHANGE_BO_RELEVANT_PERIOD;
        updateDtoUnderTest.setChangeBeneficiaryRelevantPeriod(changeBeneficiaryRelevantPeriod);
        assertThat(updateDtoUnderTest.getChangeBeneficiaryRelevantPeriod()).isEqualTo(changeBeneficiaryRelevantPeriod);
    }

    @Test
    void testDateOfCreationGetterAndSetter() {
        final LocalDate dateOfCreation = LocalDate.of(2020, 1, 1);
        updateDtoUnderTest.setDateOfCreation(dateOfCreation);
        assertThat(updateDtoUnderTest.getDateOfCreation()).isEqualTo(dateOfCreation);
    }

    @Test
    void testFilingDateGetterAndSetter() {
        final LocalDate filingDate = LocalDate.of(2020, 1, 1);
        updateDtoUnderTest.setFilingDate(filingDate);
        assertThat(updateDtoUnderTest.getFilingDate()).isEqualTo(filingDate);
    }

    @Test
    void testBoMoDataFetchedGetterAndSetter() {
        final boolean boMoDataFetched = false;
        updateDtoUnderTest.setBoMoDataFetched(boMoDataFetched);
        assertThat(updateDtoUnderTest.isBoMoDataFetched()).isFalse();
    }

    @Test
    void testRegistrableBeneficialOwnerGetterAndSetter() {
        final Boolean registrableBeneficialOwner = false;
        updateDtoUnderTest.setRegistrableBeneficialOwner(registrableBeneficialOwner);
        assertThat(updateDtoUnderTest.getRegistrableBeneficialOwner()).isFalse();
    }

    @Test
    void testNoChangeGetterAndSetter() {
        final Boolean noChange = false;
        updateDtoUnderTest.setNoChange(noChange);
        assertThat(updateDtoUnderTest.getNoChange()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("noChangeRelevantPeriodCases")
    void testIsNoChangeRelevantPeriod(
            RelevantStatementsType bo,
            RelevantStatementsType beneficiary,
            RelevantStatementsType trustee,
            boolean expected
            ) {
        updateDtoUnderTest.setChangeBORelevantPeriod(bo);
        updateDtoUnderTest.setChangeBeneficiaryRelevantPeriod(beneficiary);
        updateDtoUnderTest.setTrusteeInvolvedRelevantPeriod(trustee);

        assertThat(updateDtoUnderTest.isNoChangeRelevantPeriod()).isEqualTo(expected);
    }

    private static Stream<Arguments> noChangeRelevantPeriodCases() {
        return Stream.of(
                // No matches → TRUE
                Arguments.of(null, null, null, true),
                Arguments.of(null, RelevantStatementsType.NO_CHANGE_BENEFICIARY_RELEVANT_PERIOD, null, true),

                // Any match → FALSE
                Arguments.of(RelevantStatementsType.CHANGE_BO_RELEVANT_PERIOD, null, null, false),
                Arguments.of(null, RelevantStatementsType.CHANGE_BENEFICIARY_RELEVANT_PERIOD, null, false),
                Arguments.of(null, null, RelevantStatementsType.TRUSTEE_INVOLVED_RELEVANT_PERIOD, false),

                // Multiple matches → FALSE
                Arguments.of(
                    RelevantStatementsType.CHANGE_BO_RELEVANT_PERIOD,
                    RelevantStatementsType.CHANGE_BENEFICIARY_RELEVANT_PERIOD,
                    null,
                    false
                    )
                );
    }

    @Test
    void testTrustDataFetchedGetterAndSetter() {
        final boolean trustDataFetched = false;
        updateDtoUnderTest.setTrustDataFetched(trustDataFetched);
        assertThat(updateDtoUnderTest.isTrustDataFetched()).isFalse();
    }

    @Test
    void testReviewBeneficialOwnersIndividualGetterAndSetter() {
        final List<BeneficialOwnerIndividualDto> reviewBeneficialOwnersIndividual = List.of(
                new BeneficialOwnerIndividualDto());
        updateDtoUnderTest.setReviewBeneficialOwnersIndividual(reviewBeneficialOwnersIndividual);
        assertThat(updateDtoUnderTest.getReviewBeneficialOwnersIndividual())
                .isEqualTo(reviewBeneficialOwnersIndividual);
    }

    @Test
    void testReviewBeneficialOwnersCorporateGetterAndSetter() {
        final List<BeneficialOwnerCorporateDto> reviewBeneficialOwnersCorporate = List.of(
                new BeneficialOwnerCorporateDto());
        updateDtoUnderTest.setReviewBeneficialOwnersCorporate(reviewBeneficialOwnersCorporate);
        assertThat(updateDtoUnderTest.getReviewBeneficialOwnersCorporate()).isEqualTo(reviewBeneficialOwnersCorporate);
    }

    @Test
    void testReviewBeneficialOwnersGovernmentOrPublicAuthorityGetterAndSetter() {
        final List<BeneficialOwnerGovernmentOrPublicAuthorityDto> reviewBeneficialOwnersGovernmentOrPublicAuthority = List.of(
                new BeneficialOwnerGovernmentOrPublicAuthorityDto());
        updateDtoUnderTest.setReviewBeneficialOwnersGovernmentOrPublicAuthority(
                reviewBeneficialOwnersGovernmentOrPublicAuthority);
        assertThat(updateDtoUnderTest.getReviewBeneficialOwnersGovernmentOrPublicAuthority())
                .isEqualTo(reviewBeneficialOwnersGovernmentOrPublicAuthority);
    }

    @Test
    void testReviewManagingOfficersIndividualGetterAndSetter() {
        final List<ManagingOfficerIndividualDto> reviewManagingOfficersIndividual = List.of(
                new ManagingOfficerIndividualDto());
        updateDtoUnderTest.setReviewManagingOfficersIndividual(reviewManagingOfficersIndividual);
        assertThat(updateDtoUnderTest.getReviewManagingOfficersIndividual())
                .isEqualTo(reviewManagingOfficersIndividual);
    }

    @Test
    void testReviewManagingOfficersCorporateGetterAndSetter() {
        final List<ManagingOfficerCorporateDto> reviewManagingOfficersCorporate = List.of(
                new ManagingOfficerCorporateDto());
        updateDtoUnderTest.setReviewManagingOfficersCorporate(reviewManagingOfficersCorporate);
        assertThat(updateDtoUnderTest.getReviewManagingOfficersCorporate()).isEqualTo(reviewManagingOfficersCorporate);
    }

    @Test
    void testReviewTrustsGetterAndSetter() {
        final List<TrustDataToReviewDto> reviewTrusts = List.of(new TrustDataToReviewDto());
        updateDtoUnderTest.setReviewTrusts(reviewTrusts);
        assertThat(updateDtoUnderTest.getReviewTrusts()).isEqualTo(reviewTrusts);
    }
}
