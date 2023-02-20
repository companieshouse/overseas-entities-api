package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.utils.DataSanitisation;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.CONCATENATED_STRING_FORMAT;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;

@Component
public class NationalityValidator {

    private static final List<String> nationalities;

    static {
        List<String> modifiableListOfNationalities = new ArrayList<>();
        modifiableListOfNationalities.add("Afghan");
        modifiableListOfNationalities.add("Albanian");
        modifiableListOfNationalities.add("Algerian");
        modifiableListOfNationalities.add("American");
        modifiableListOfNationalities.add("Andorran");
        modifiableListOfNationalities.add("Angolan");
        modifiableListOfNationalities.add("Anguillan");
        modifiableListOfNationalities.add("Argentine");
        modifiableListOfNationalities.add("Armenian");
        modifiableListOfNationalities.add("Australian");
        modifiableListOfNationalities.add("Austrian");
        modifiableListOfNationalities.add("Azerbaijani");
        modifiableListOfNationalities.add("Bahamian");
        modifiableListOfNationalities.add("Bahraini");
        modifiableListOfNationalities.add("Bangladeshi");
        modifiableListOfNationalities.add("Barbadian");
        modifiableListOfNationalities.add("Belarusian");
        modifiableListOfNationalities.add("Belgian");
        modifiableListOfNationalities.add("Belizean");
        modifiableListOfNationalities.add("Beninese");
        modifiableListOfNationalities.add("Bermudian");
        modifiableListOfNationalities.add("Bhutanese");
        modifiableListOfNationalities.add("Bolivian");
        modifiableListOfNationalities.add("Botswanan");
        modifiableListOfNationalities.add("Brazilian");
        modifiableListOfNationalities.add("British");
        modifiableListOfNationalities.add("British Virgin Islander");
        modifiableListOfNationalities.add("Bruneian");
        modifiableListOfNationalities.add("Bulgarian");
        modifiableListOfNationalities.add("Burkinan");
        modifiableListOfNationalities.add("Burmese");
        modifiableListOfNationalities.add("Burundian");
        modifiableListOfNationalities.add("Cambodian");
        modifiableListOfNationalities.add("Cameroonian");
        modifiableListOfNationalities.add("Canadian");
        modifiableListOfNationalities.add("Cape Verdean");
        modifiableListOfNationalities.add("Cayman Islander");
        modifiableListOfNationalities.add("Central African");
        modifiableListOfNationalities.add("Chadian");
        modifiableListOfNationalities.add("Chilean");
        modifiableListOfNationalities.add("Chinese");
        modifiableListOfNationalities.add("Citizen of Antigua and Barbuda");
        modifiableListOfNationalities.add("Citizen of Bosnia and Herzegovina");
        modifiableListOfNationalities.add("Citizen of Guinea-Bissau");
        modifiableListOfNationalities.add("Citizen of Kiribati");
        modifiableListOfNationalities.add("Citizen of Seychelles");
        modifiableListOfNationalities.add("Citizen of the Dominican Republic");
        modifiableListOfNationalities.add("Citizen of Vanuatu");
        modifiableListOfNationalities.add("Colombian");
        modifiableListOfNationalities.add("Comoran");
        modifiableListOfNationalities.add("Congolese (Congo)");
        modifiableListOfNationalities.add("Congolese (DRC)");
        modifiableListOfNationalities.add("Cook Islander");
        modifiableListOfNationalities.add("Costa Rican");
        modifiableListOfNationalities.add("Croatian");
        modifiableListOfNationalities.add("Cuban");
        modifiableListOfNationalities.add("Cymraes");
        modifiableListOfNationalities.add("Cymro");
        modifiableListOfNationalities.add("Cypriot");
        modifiableListOfNationalities.add("Czech");
        modifiableListOfNationalities.add("Danish");
        modifiableListOfNationalities.add("Djiboutian");
        modifiableListOfNationalities.add("Dominican");
        modifiableListOfNationalities.add("Dutch");
        modifiableListOfNationalities.add("East Timorese");
        modifiableListOfNationalities.add("Ecuadorean");
        modifiableListOfNationalities.add("Egyptian");
        modifiableListOfNationalities.add("Emirati");
        modifiableListOfNationalities.add("English");
        modifiableListOfNationalities.add("Equatorial Guinean");
        modifiableListOfNationalities.add("Eritrean");
        modifiableListOfNationalities.add("Estonian");
        modifiableListOfNationalities.add("Ethiopian");
        modifiableListOfNationalities.add("Faroese");
        modifiableListOfNationalities.add("Fijian");
        modifiableListOfNationalities.add("Filipino");
        modifiableListOfNationalities.add("Finnish");
        modifiableListOfNationalities.add("French");
        modifiableListOfNationalities.add("Gabonese");
        modifiableListOfNationalities.add("Gambian");
        modifiableListOfNationalities.add("Georgian");
        modifiableListOfNationalities.add("German");
        modifiableListOfNationalities.add("Ghanaian");
        modifiableListOfNationalities.add("Gibraltarian");
        modifiableListOfNationalities.add("Greek");
        modifiableListOfNationalities.add("Greenlandic");
        modifiableListOfNationalities.add("Grenadian");
        modifiableListOfNationalities.add("Guamanian");
        modifiableListOfNationalities.add("Guatemalan");
        modifiableListOfNationalities.add("Guinean");
        modifiableListOfNationalities.add("Guyanese");
        modifiableListOfNationalities.add("Haitian");
        modifiableListOfNationalities.add("Honduran");
        modifiableListOfNationalities.add("Hong Konger");
        modifiableListOfNationalities.add("Hungarian");
        modifiableListOfNationalities.add("Icelandic");
        modifiableListOfNationalities.add("Indian");
        modifiableListOfNationalities.add("Indonesian");
        modifiableListOfNationalities.add("Iranian");
        modifiableListOfNationalities.add("Iraqi");
        modifiableListOfNationalities.add("Irish");
        modifiableListOfNationalities.add("Israeli");
        modifiableListOfNationalities.add("Italian");
        modifiableListOfNationalities.add("Ivorian");
        modifiableListOfNationalities.add("Jamaican");
        modifiableListOfNationalities.add("Japanese");
        modifiableListOfNationalities.add("Jordanian");
        modifiableListOfNationalities.add("Kazakh");
        modifiableListOfNationalities.add("Kenyan");
        modifiableListOfNationalities.add("Kittitian");
        modifiableListOfNationalities.add("Kosovan");
        modifiableListOfNationalities.add("Kuwaiti");
        modifiableListOfNationalities.add("Kyrgyz");
        modifiableListOfNationalities.add("Lao");
        modifiableListOfNationalities.add("Latvian");
        modifiableListOfNationalities.add("Lebanese");
        modifiableListOfNationalities.add("Liberian");
        modifiableListOfNationalities.add("Libyan");
        modifiableListOfNationalities.add("Liechtenstein citizen");
        modifiableListOfNationalities.add("Lithuanian");
        modifiableListOfNationalities.add("Luxembourger");
        modifiableListOfNationalities.add("Macanese");
        modifiableListOfNationalities.add("Macedonian");
        modifiableListOfNationalities.add("Malagasy");
        modifiableListOfNationalities.add("Malawian");
        modifiableListOfNationalities.add("Malaysian");
        modifiableListOfNationalities.add("Maldivian");
        modifiableListOfNationalities.add("Malian");
        modifiableListOfNationalities.add("Maltese");
        modifiableListOfNationalities.add("Marshallese");
        modifiableListOfNationalities.add("Martiniquais");
        modifiableListOfNationalities.add("Mauritanian");
        modifiableListOfNationalities.add("Mauritian");
        modifiableListOfNationalities.add("Mexican");
        modifiableListOfNationalities.add("Micronesian");
        modifiableListOfNationalities.add("Moldovan");
        modifiableListOfNationalities.add("Monegasque");
        modifiableListOfNationalities.add("Mongolian");
        modifiableListOfNationalities.add("Montenegrin");
        modifiableListOfNationalities.add("Montserratian");
        modifiableListOfNationalities.add("Moroccan");
        modifiableListOfNationalities.add("Mosotho");
        modifiableListOfNationalities.add("Mozambican");
        modifiableListOfNationalities.add("Namibian");
        modifiableListOfNationalities.add("Nauruan");
        modifiableListOfNationalities.add("Nepalese");
        modifiableListOfNationalities.add("New Zealander");
        modifiableListOfNationalities.add("Nicaraguan");
        modifiableListOfNationalities.add("Nigerian");
        modifiableListOfNationalities.add("Nigerien");
        modifiableListOfNationalities.add("Niuean");
        modifiableListOfNationalities.add("North Korean");
        modifiableListOfNationalities.add("Northern Irish");
        modifiableListOfNationalities.add("Norwegian");
        modifiableListOfNationalities.add("Omani");
        modifiableListOfNationalities.add("Pakistani");
        modifiableListOfNationalities.add("Palauan");
        modifiableListOfNationalities.add("Palestinian");
        modifiableListOfNationalities.add("Panamanian");
        modifiableListOfNationalities.add("Papua New Guinean");
        modifiableListOfNationalities.add("Paraguayan");
        modifiableListOfNationalities.add("Peruvian");
        modifiableListOfNationalities.add("Pitcairn Islander");
        modifiableListOfNationalities.add("Polish");
        modifiableListOfNationalities.add("Portuguese");
        modifiableListOfNationalities.add("Prydeinig");
        modifiableListOfNationalities.add("Puerto Rican");
        modifiableListOfNationalities.add("Qatari");
        modifiableListOfNationalities.add("Romanian");
        modifiableListOfNationalities.add("Russian");
        modifiableListOfNationalities.add("Rwandan");
        modifiableListOfNationalities.add("Salvadorean");
        modifiableListOfNationalities.add("Sammarinese");
        modifiableListOfNationalities.add("Samoan");
        modifiableListOfNationalities.add("Sao Tomean");
        modifiableListOfNationalities.add("Saudi Arabian");
        modifiableListOfNationalities.add("Scottish");
        modifiableListOfNationalities.add("Senegalese");
        modifiableListOfNationalities.add("Serbian");
        modifiableListOfNationalities.add("Sierra Leonean");
        modifiableListOfNationalities.add("Singaporean");
        modifiableListOfNationalities.add("Slovak");
        modifiableListOfNationalities.add("Slovenian");
        modifiableListOfNationalities.add("Solomon Islander");
        modifiableListOfNationalities.add("Somali");
        modifiableListOfNationalities.add("South African");
        modifiableListOfNationalities.add("South Korean");
        modifiableListOfNationalities.add("South Sudanese");
        modifiableListOfNationalities.add("Spanish");
        modifiableListOfNationalities.add("Sri Lankan");
        modifiableListOfNationalities.add("St Helenian");
        modifiableListOfNationalities.add("St Lucian");
        modifiableListOfNationalities.add("Stateless");
        modifiableListOfNationalities.add("Sudanese");
        modifiableListOfNationalities.add("Surinamese");
        modifiableListOfNationalities.add("Swazi");
        modifiableListOfNationalities.add("Swedish");
        modifiableListOfNationalities.add("Swiss");
        modifiableListOfNationalities.add("Syrian");
        modifiableListOfNationalities.add("Taiwanese");
        modifiableListOfNationalities.add("Tajik");
        modifiableListOfNationalities.add("Tanzanian");
        modifiableListOfNationalities.add("Thai");
        modifiableListOfNationalities.add("Togolese");
        modifiableListOfNationalities.add("Tongan");
        modifiableListOfNationalities.add("Trinidadian");
        modifiableListOfNationalities.add("Tristanian");
        modifiableListOfNationalities.add("Tunisian");
        modifiableListOfNationalities.add("Turkish");
        modifiableListOfNationalities.add("Turkmen");
        modifiableListOfNationalities.add("Turks and Caicos Islander");
        modifiableListOfNationalities.add("Tuvaluan");
        modifiableListOfNationalities.add("Ugandan");
        modifiableListOfNationalities.add("Ukrainian");
        modifiableListOfNationalities.add("Uruguayan");
        modifiableListOfNationalities.add("Uzbek");
        modifiableListOfNationalities.add("Vatican citizen");
        modifiableListOfNationalities.add("Venezuelan");
        modifiableListOfNationalities.add("Vietnamese");
        modifiableListOfNationalities.add("Vincentian");
        modifiableListOfNationalities.add("Wallisian");
        modifiableListOfNationalities.add("Welsh");
        modifiableListOfNationalities.add("Yemeni");
        modifiableListOfNationalities.add("Zambian");
        modifiableListOfNationalities.add("Zimbabwean");
        nationalities = Collections.unmodifiableList(modifiableListOfNationalities);
    }

    private final DataSanitisation dataSanitisation;

    @Autowired
    public NationalityValidator(DataSanitisation dataSanitisation) {
        this.dataSanitisation = dataSanitisation;
    }

    public void validateAgainstNationalityList(String qualifiedFieldName, String nationality, Errors errors, String loggingContext) {

        if (!nationalities.contains(nationality)) {
            var validationMessage = String.format(ValidationMessages.NATIONALITY_NOT_ON_LIST_ERROR_MESSAGE, dataSanitisation.makeStringSafeForLogging(nationality));
            setErrorMsgToLocation(errors, qualifiedFieldName, validationMessage);
            ApiLogger.infoContext(loggingContext, validationMessage);
        }
    }

    public Errors validateSecondNationality(String fieldNameNationality, String fieldNameSecondNationality, String nationality, String secondNationality, Errors errors, String loggingContext) {
        var compoundQualifiedFieldName = String.format("%s and %s", fieldNameNationality, fieldNameSecondNationality);

        StringValidators.checkIsNotEqual(nationality, secondNationality, ValidationMessages.SECOND_NATIONALITY_SHOULD_BE_DIFFERENT, fieldNameSecondNationality, errors, loggingContext);
        StringValidators.isLessThanOrEqualToMaxLength(String.format(CONCATENATED_STRING_FORMAT, nationality, secondNationality), 50, compoundQualifiedFieldName, errors, loggingContext);
        validateAgainstNationalityList(fieldNameSecondNationality, secondNationality, errors, loggingContext);

        return errors;
    }
}

