package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.cessations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.Cessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.IndividualBeneficialOwnerCessation;

class BeneficialOwnerCessationServiceTest {

  @Mock private OverseasEntitySubmissionDto overseasEntitySubmissionDto;

  @Mock private Map<String, Pair<PscApi, PrivateBoDataApi>> combinedBoData;

  private BeneficialOwnerCessationService beneficialOwnerCessationService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    beneficialOwnerCessationService = new BeneficialOwnerCessationService();
  }

  @Test
  void testBeneficialOwnerCessations() {
    List<BeneficialOwnerIndividualDto> individualBeneficialOwners = new ArrayList<>();
    BeneficialOwnerIndividualDto individualBeneficialOwner = new BeneficialOwnerIndividualDto();
    individualBeneficialOwner.setFirstName("John");
    individualBeneficialOwner.setLastName("Doe");
    individualBeneficialOwner.setDateOfBirth(LocalDate.of(1990, 5, 15));
    individualBeneficialOwner.setCeasedDate(LocalDate.now());
    individualBeneficialOwner.setChipsReference("ABC123");
    individualBeneficialOwners.add(individualBeneficialOwner);

    List<BeneficialOwnerCorporateDto> corporateBeneficialOwners = new ArrayList<>();
    BeneficialOwnerCorporateDto corporateBeneficialOwner = new BeneficialOwnerCorporateDto();
    corporateBeneficialOwner.setName("ACME Corporation");
    corporateBeneficialOwner.setCeasedDate(LocalDate.now());
    corporateBeneficialOwner.setChipsReference("DEF456");
    corporateBeneficialOwners.add(corporateBeneficialOwner);

    List<BeneficialOwnerGovernmentOrPublicAuthorityDto> legalPersonBeneficialOwners =
        new ArrayList<>();
    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPersonBeneficialOwner =
        new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPersonBeneficialOwner.setName("Government Authority");
    legalPersonBeneficialOwner.setCeasedDate(LocalDate.now());
    legalPersonBeneficialOwner.setChipsReference("GHI789");
    legalPersonBeneficialOwners.add(legalPersonBeneficialOwner);

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
        .thenReturn(individualBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
        .thenReturn(corporateBeneficialOwners);
    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
        .thenReturn(legalPersonBeneficialOwners);

    String hashedId1 = individualBeneficialOwner.getChipsReference();
    String hashedId2 = corporateBeneficialOwner.getChipsReference();
    String hashedId3 = legalPersonBeneficialOwner.getChipsReference();

    PrivateBoDataApi privateBoData1 = new PrivateBoDataApi();
    privateBoData1.setPscId("123");
    PrivateBoDataApi privateBoData2 = new PrivateBoDataApi();
    privateBoData2.setPscId("456");
    PrivateBoDataApi privateBoData3 = new PrivateBoDataApi();
    privateBoData3.setPscId("789");

    when(combinedBoData.get(hashedId1)).thenReturn(new ImmutablePair<>(null, privateBoData1));
    when(combinedBoData.get(hashedId2)).thenReturn(new ImmutablePair<>(null, privateBoData2));
    when(combinedBoData.get(hashedId3)).thenReturn(new ImmutablePair<>(null, privateBoData3));

    List<Cessation> cessations =
        beneficialOwnerCessationService.beneficialOwnerCessations(overseasEntitySubmissionDto, combinedBoData);

    assertEquals(3, cessations.size()); // Assuming only one beneficial owner satisfies the filter criteria

    IndividualBeneficialOwnerCessation individualCessation = (IndividualBeneficialOwnerCessation) cessations.get(0);
    assertEquals("123", individualCessation.getAppointmentId());
    assertEquals(LocalDate.now(), individualCessation.getActionDate());
    assertEquals(LocalDate.of(1990, 5, 15), individualCessation.getBirthDate());
    assertEquals("John", individualCessation.getPersonName().getForename());
    assertEquals("Doe", individualCessation.getPersonName().getSurname());
  }

  @Test
  void testBeneficialOwnerCessations_IllegalArgumentException() {
    List<BeneficialOwnerIndividualDto> individualBeneficialOwners = new ArrayList<>();
    BeneficialOwnerIndividualDto individualBeneficialOwner = new BeneficialOwnerIndividualDto();
    individualBeneficialOwner.setFirstName("John");
    individualBeneficialOwner.setLastName("Doe");
    individualBeneficialOwner.setDateOfBirth(LocalDate.of(1990, 5, 15));
    individualBeneficialOwner.setCeasedDate(LocalDate.now());
    individualBeneficialOwner.setChipsReference("XYZ987");
    individualBeneficialOwners.add(individualBeneficialOwner);

    when(overseasEntitySubmissionDto.getBeneficialOwnersIndividual())
            .thenReturn(individualBeneficialOwners);

    List<BeneficialOwnerCorporateDto> corporateBeneficialOwners = new ArrayList<>();
    BeneficialOwnerCorporateDto corporateBeneficialOwner = new BeneficialOwnerCorporateDto();
    corporateBeneficialOwner.setName("ACME Corporation");
    corporateBeneficialOwner.setCeasedDate(LocalDate.now());
    corporateBeneficialOwner.setChipsReference("ZYX654");
    corporateBeneficialOwners.add(corporateBeneficialOwner);

    when(overseasEntitySubmissionDto.getBeneficialOwnersCorporate())
            .thenReturn(corporateBeneficialOwners);

    List<BeneficialOwnerGovernmentOrPublicAuthorityDto> legalPersonBeneficialOwners =
            new ArrayList<>();
    BeneficialOwnerGovernmentOrPublicAuthorityDto legalPersonBeneficialOwner =
            new BeneficialOwnerGovernmentOrPublicAuthorityDto();
    legalPersonBeneficialOwner.setName("Government Authority");
    legalPersonBeneficialOwner.setCeasedDate(LocalDate.now());
    legalPersonBeneficialOwner.setChipsReference("XYZ321");
    legalPersonBeneficialOwners.add(legalPersonBeneficialOwner);

    when(overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority())
            .thenReturn(legalPersonBeneficialOwners);

    assertThrows(
            IllegalArgumentException.class,
            () -> beneficialOwnerCessationService.beneficialOwnerCessations(
                    overseasEntitySubmissionDto, combinedBoData)
    );
  }

}
