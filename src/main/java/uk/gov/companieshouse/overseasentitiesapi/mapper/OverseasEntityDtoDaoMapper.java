package uk.gov.companieshouse.overseasentitiesapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;

import java.time.LocalDate;

@Component
@Mapper(componentModel = "spring")
public interface OverseasEntityDtoDaoMapper {

      @Mapping(source = "entity.publicRegisterEntityRegisteredOn", target = "entity.publicRegisterEntityRegisteredOn", qualifiedByName = "localDate")
      OverseasEntitySubmissionDto daoToDto(OverseasEntitySubmissionDao overseasEntitySubmission);

      @Mapping(source = "entity.publicRegisterEntityRegisteredOn", target = "entity.publicRegisterEntityRegisteredOn", qualifiedByName = "localDate")
      OverseasEntitySubmissionDao dtoToDao(OverseasEntitySubmissionDto overseasEntitySubmission);

      @Named("localDate")
      static LocalDate localDate(LocalDate date) {
            if (date == null) {
                  return null;
            }

            return LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
      }
}
