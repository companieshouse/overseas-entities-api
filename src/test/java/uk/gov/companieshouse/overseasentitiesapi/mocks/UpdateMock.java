package uk.gov.companieshouse.overseasentitiesapi.mocks;

import java.time.LocalDate;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;

public class UpdateMock {
    public static UpdateDto getUpdateDto() {
        UpdateDto updateDto = new UpdateDto();
        updateDto.setFilingDate(LocalDate.of(2001, 2, 1));
        return updateDto;
    }
}
