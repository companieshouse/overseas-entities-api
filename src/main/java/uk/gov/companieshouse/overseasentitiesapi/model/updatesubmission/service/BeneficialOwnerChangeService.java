package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner.BeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner.Psc;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner.ResidentialAddress;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner.ServiceAddress;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.converters.ConverterFactory;

@Service
public class BeneficialOwnerChangeService {

  private final Map<String, Pair<PscApi, PrivateBoDataApi>> publicPrivateBo;

  private final OverseasEntitySubmissionDto overseasEntitySubmissionDto;

  public BeneficialOwnerChangeService(OverseasEntitySubmissionDto overseasEntitySubmissionDto,
      Map<String, Pair<PscApi, PrivateBoDataApi>> publicPrivateBo) {
    this.overseasEntitySubmissionDto = overseasEntitySubmissionDto;
    this.publicPrivateBo = publicPrivateBo;
  }

  private Psc getChanges(BeneficialOwnerIndividualDto beneficialOwnerIndividualDto) {
    var submissionId = beneficialOwnerIndividualDto.getChipsReference();
    var publicPrivateBoPair = publicPrivateBo.get(submissionId);
    return null;
  }

  public List<Change> beneficialOwnerChanges() {
    List<Change> changes = new ArrayList<>();

    return changes;
  }

  private BeneficialOwnerChange covertBeneficialOwnerIndividualToChange(
      BeneficialOwnerIndividualDto beneficialOwnerIndividualDto) {
    var beneficialOwnerChange = new BeneficialOwnerChange();
    var psc = new Psc();
    var previousPsc = new Psc();

    Pair<PscApi, PrivateBoDataApi> publicPrivateBoPair = publicPrivateBo.get(
        beneficialOwnerIndividualDto.getChipsReference());

    List<ChangeHelper> changeHelpers = List.of(
        new ChangeHelper<>(beneficialOwnerIndividualDto.getNationality(),
            publicPrivateBoPair.getLeft().getNationality(), (a, b) -> a.setNationality(b),
            String.class),

        new ChangeHelper<>(beneficialOwnerIndividualDto.getDateOfBirth(),
            publicPrivateBoPair.getLeft().getDateOfBirth(), (a, b) -> a.setBirthDate(b),
            LocalDate.class),

        new ChangeHelper<>(beneficialOwnerIndividualDto.getUsualResidentialAddress(),
            publicPrivateBoPair.getRight().getUsualResidentialAddress(),
            (a, b) -> a.setResidentialAddress(b), ResidentialAddress.class),

        new ChangeHelper<>(beneficialOwnerIndividualDto.getFirstName() + " "
            + beneficialOwnerIndividualDto.getLastName(), publicPrivateBoPair.getLeft().getName(),
            (a, b) -> a.setPersonName(b), PersonName.class),

        new ChangeHelper<>(beneficialOwnerIndividualDto.getServiceAddress(),
            publicPrivateBoPair.getRight().getPrincipalAddress(), (a, b) -> a.setServiceAddress(b),
            ServiceAddress.class),

        new ChangeHelper<>(beneficialOwnerIndividualDto.getUsualResidentialAddress(),
            publicPrivateBoPair.getRight().getUsualResidentialAddress(),
            (a, b) -> a.setUsualResidence(b), String.class),

        new ChangeHelper<>(beneficialOwnerIndividualDto.getOnSanctionsList().toString(),
            publicPrivateBoPair.getLeft().isSanctioned(),
            (a, b) -> a.setSecureIndicator(b), String.class)
    );

    changeHelpers.forEach(changeHelper -> {
      if (changeHelper.proposedData != null && !changeHelper.objectsEqual.test(
          changeHelper.proposedData, changeHelper.currentData)) {

        if (changeHelper.proposedData.getClass() == changeHelper.setterParameterClass) {
          changeHelper.setter.accept(psc, changeHelper.proposedData);
        } else {
          changeHelper.setter.accept(psc,
              ConverterFactory.getConverter(changeHelper.proposedData.getClass(),
                      changeHelper.setterParameterClass)
                  .convert(changeHelper.proposedData));
        }
      }
      changeHelper.setter.accept(previousPsc, changeHelper.currentData);
    });

    beneficialOwnerChange.setPsc(psc);
    return beneficialOwnerChange;
  }

}
