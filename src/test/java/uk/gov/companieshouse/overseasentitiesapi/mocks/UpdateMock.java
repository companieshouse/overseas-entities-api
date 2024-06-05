package uk.gov.companieshouse.overseasentitiesapi.mocks;

import java.time.LocalDate;

import uk.gov.companieshouse.overseasentitiesapi.model.RelevantStatementsType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;

public class UpdateMock {
    public static UpdateDto getUpdateDto() {
        UpdateDto updateDto = new UpdateDto();
        updateDto.setFilingDate(LocalDate.of(2001, 2, 1));
        updateDto.setChangeBORelevantPeriod(RelevantStatementsType.CHANGE_BO_RELEVANT_PERIOD);
        updateDto.setTrusteeInvolvedRelevantPeriod(RelevantStatementsType.NO_TRUSTEE_INVOLVED_RELEVANT_PERIOD);
        updateDto.setChangeBeneficiaryRelevantPeriod(RelevantStatementsType.CHANGE_BENEFICIARY_RELEVANT_PERIOD);
        return updateDto;
    }
}
