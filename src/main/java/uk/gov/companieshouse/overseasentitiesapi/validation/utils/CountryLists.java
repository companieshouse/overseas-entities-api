package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountryLists {

        private static final List<String> overseasCountries;
        private static final List<String> ukCountries;
        private static final List<String> allCountries;

        private CountryLists() {}

        static {
            List<String> modifiableListOfCountries = new ArrayList<>();
            modifiableListOfCountries.add("Afghanistan");
            modifiableListOfCountries.add("Aland Islands");
            modifiableListOfCountries.add("Albania");
            modifiableListOfCountries.add("Alderney");
            modifiableListOfCountries.add("Algeria");
            modifiableListOfCountries.add("American Samoa");
            modifiableListOfCountries.add("Andorra");
            modifiableListOfCountries.add("Angola");
            modifiableListOfCountries.add("Anguilla");
            modifiableListOfCountries.add("Antarctica");
            modifiableListOfCountries.add("Antigua and Barbuda");
            modifiableListOfCountries.add("Argentina");
            modifiableListOfCountries.add("Armenia");
            modifiableListOfCountries.add("Aruba");
            modifiableListOfCountries.add("Australia");
            modifiableListOfCountries.add("Austria");
            modifiableListOfCountries.add("Azerbaijan");
            modifiableListOfCountries.add("Bahamas");
            modifiableListOfCountries.add("Bahrain");
            modifiableListOfCountries.add("Bangladesh");
            modifiableListOfCountries.add("Barbados");
            modifiableListOfCountries.add("Belarus");
            modifiableListOfCountries.add("Belgium");
            modifiableListOfCountries.add("Belize");
            modifiableListOfCountries.add("Benin");
            modifiableListOfCountries.add("Bermuda");
            modifiableListOfCountries.add("Bhutan");
            modifiableListOfCountries.add("Bolivia");
            modifiableListOfCountries.add("Bonaire, Sint Eustatius and Saba");
            modifiableListOfCountries.add("Bosnia and Herzegovina");
            modifiableListOfCountries.add("Botswana");
            modifiableListOfCountries.add("Bouvet Island");
            modifiableListOfCountries.add("Brazil");
            modifiableListOfCountries.add("British Indian Ocean Territory");
            modifiableListOfCountries.add("Brunei Darussalam");
            modifiableListOfCountries.add("Bulgaria");
            modifiableListOfCountries.add("Burkina Faso");
            modifiableListOfCountries.add("Burundi");
            modifiableListOfCountries.add("Cambodia");
            modifiableListOfCountries.add("Cameroon");
            modifiableListOfCountries.add("Canada");
            modifiableListOfCountries.add("Cape Verde");
            modifiableListOfCountries.add("Cayman Islands");
            modifiableListOfCountries.add("Central African Republic");
            modifiableListOfCountries.add("Chad");
            modifiableListOfCountries.add("Chile");
            modifiableListOfCountries.add("China");
            modifiableListOfCountries.add("Christmas Island");
            modifiableListOfCountries.add("Cocos (Keeling) Islands");
            modifiableListOfCountries.add("Colombia");
            modifiableListOfCountries.add("Comoros");
            modifiableListOfCountries.add("Congo");
            modifiableListOfCountries.add("Congo, the Democratic Republic of the");
            modifiableListOfCountries.add("Cook Islands");
            modifiableListOfCountries.add("Costa Rica");
            modifiableListOfCountries.add("Croatia");
            modifiableListOfCountries.add("Cuba");
            modifiableListOfCountries.add("Curacao");
            modifiableListOfCountries.add("Cyprus");
            modifiableListOfCountries.add("Czech Republic");
            modifiableListOfCountries.add("Denmark");
            modifiableListOfCountries.add("Djibouti");
            modifiableListOfCountries.add("Dominica");
            modifiableListOfCountries.add("Dominican Republic");
            modifiableListOfCountries.add("East Timor");
            modifiableListOfCountries.add("Ecuador");
            modifiableListOfCountries.add("Egypt");
            modifiableListOfCountries.add("El Salvador");
            modifiableListOfCountries.add("Equatorial Guinea");
            modifiableListOfCountries.add("Eritrea");
            modifiableListOfCountries.add("Estonia");
            modifiableListOfCountries.add("Eswatini");
            modifiableListOfCountries.add("Ethiopia");
            modifiableListOfCountries.add("Falkland Islands");
            modifiableListOfCountries.add("Faroe Islands");
            modifiableListOfCountries.add("Fiji");
            modifiableListOfCountries.add("Finland");
            modifiableListOfCountries.add("France");
            modifiableListOfCountries.add("French Guiana");
            modifiableListOfCountries.add("French Polynesia");
            modifiableListOfCountries.add("French Southern Territories");
            modifiableListOfCountries.add("Gabon");
            modifiableListOfCountries.add("Gambia");
            modifiableListOfCountries.add("Georgia");
            modifiableListOfCountries.add("Germany");
            modifiableListOfCountries.add("Ghana");
            modifiableListOfCountries.add("Gibraltar");
            modifiableListOfCountries.add("Greece");
            modifiableListOfCountries.add("Greenland");
            modifiableListOfCountries.add("Grenada");
            modifiableListOfCountries.add("Guadeloupe");
            modifiableListOfCountries.add("Guam");
            modifiableListOfCountries.add("Guatemala");
            modifiableListOfCountries.add("Guernsey");
            modifiableListOfCountries.add("Guinea");
            modifiableListOfCountries.add("Guinea-Bissau");
            modifiableListOfCountries.add("Guyana");
            modifiableListOfCountries.add("Haiti");
            modifiableListOfCountries.add("Heard Island and McDonald Islands");
            modifiableListOfCountries.add("Herm");
            modifiableListOfCountries.add("Honduras");
            modifiableListOfCountries.add("Hong Kong");
            modifiableListOfCountries.add("Hungary");
            modifiableListOfCountries.add("Iceland");
            modifiableListOfCountries.add("India");
            modifiableListOfCountries.add("Indonesia");
            modifiableListOfCountries.add("Iran");
            modifiableListOfCountries.add("Iraq");
            modifiableListOfCountries.add("Ireland");
            modifiableListOfCountries.add("Isle of Man");
            modifiableListOfCountries.add("Israel");
            modifiableListOfCountries.add("Italy");
            modifiableListOfCountries.add("Ivory Coast");
            modifiableListOfCountries.add("Jamaica");
            modifiableListOfCountries.add("Japan");
            modifiableListOfCountries.add("Jersey");
            modifiableListOfCountries.add("Jordan");
            modifiableListOfCountries.add("Kazakhstan");
            modifiableListOfCountries.add("Kenya");
            modifiableListOfCountries.add("Kiribati");
            modifiableListOfCountries.add("Kosovo");
            modifiableListOfCountries.add("Kuwait");
            modifiableListOfCountries.add("Kyrgyzstan");
            modifiableListOfCountries.add("Laos");
            modifiableListOfCountries.add("Latvia");
            modifiableListOfCountries.add("Lebanon");
            modifiableListOfCountries.add("Lesotho");
            modifiableListOfCountries.add("Liberia");
            modifiableListOfCountries.add("Libya");
            modifiableListOfCountries.add("Liechtenstein");
            modifiableListOfCountries.add("Lithuania");
            modifiableListOfCountries.add("Luxembourg");
            modifiableListOfCountries.add("Macao");
            modifiableListOfCountries.add("Macedonia");
            modifiableListOfCountries.add("Madagascar");
            modifiableListOfCountries.add("Malawi");
            modifiableListOfCountries.add("Malaysia");
            modifiableListOfCountries.add("Maldives");
            modifiableListOfCountries.add("Mali");
            modifiableListOfCountries.add("Malta");
            modifiableListOfCountries.add("Marshall Islands");
            modifiableListOfCountries.add("Martinique");
            modifiableListOfCountries.add("Mauritania");
            modifiableListOfCountries.add("Mauritius");
            modifiableListOfCountries.add("Mayotte");
            modifiableListOfCountries.add("Mexico");
            modifiableListOfCountries.add("Micronesia");
            modifiableListOfCountries.add("Moldova");
            modifiableListOfCountries.add("Monaco");
            modifiableListOfCountries.add("Mongolia");
            modifiableListOfCountries.add("Montenegro");
            modifiableListOfCountries.add("Montserrat");
            modifiableListOfCountries.add("Morocco");
            modifiableListOfCountries.add("Mozambique");
            modifiableListOfCountries.add("Myanmar");
            modifiableListOfCountries.add("Namibia");
            modifiableListOfCountries.add("Nauru");
            modifiableListOfCountries.add("Nepal");
            modifiableListOfCountries.add("Netherlands");
            modifiableListOfCountries.add("New Caledonia");
            modifiableListOfCountries.add("New Zealand");
            modifiableListOfCountries.add("Nicaragua");
            modifiableListOfCountries.add("Niger");
            modifiableListOfCountries.add("Nigeria");
            modifiableListOfCountries.add("Niue");
            modifiableListOfCountries.add("Norfolk Island");
            modifiableListOfCountries.add("North Korea");
            modifiableListOfCountries.add("Northern Mariana Islands");
            modifiableListOfCountries.add("Norway");
            modifiableListOfCountries.add("Oman");
            modifiableListOfCountries.add("Pakistan");
            modifiableListOfCountries.add("Palau");
            modifiableListOfCountries.add("Palestine, State of");
            modifiableListOfCountries.add("Panama");
            modifiableListOfCountries.add("Papua New Guinea");
            modifiableListOfCountries.add("Paraguay");
            modifiableListOfCountries.add("Peru");
            modifiableListOfCountries.add("Philippines");
            modifiableListOfCountries.add("Pitcairn");
            modifiableListOfCountries.add("Poland");
            modifiableListOfCountries.add("Portugal");
            modifiableListOfCountries.add("Puerto Rico");
            modifiableListOfCountries.add("Qatar");
            modifiableListOfCountries.add("Reunion");
            modifiableListOfCountries.add("Romania");
            modifiableListOfCountries.add("Russia");
            modifiableListOfCountries.add("Rwanda");
            modifiableListOfCountries.add("Saint Barthelemy");
            modifiableListOfCountries.add("Saint Helena, Ascension and Tristan da Cunha");
            modifiableListOfCountries.add("Saint Kitts and Nevis");
            modifiableListOfCountries.add("Saint Lucia");
            modifiableListOfCountries.add("Saint Martin (French part)");
            modifiableListOfCountries.add("Saint Pierre and Miquelon");
            modifiableListOfCountries.add("Saint Vincent and the Grenadines");
            modifiableListOfCountries.add("Samoa");
            modifiableListOfCountries.add("San Marino");
            modifiableListOfCountries.add("Sao Tome and Principe");
            modifiableListOfCountries.add("Sark");
            modifiableListOfCountries.add("Saudi Arabia");
            modifiableListOfCountries.add("Senegal");
            modifiableListOfCountries.add("Serbia");
            modifiableListOfCountries.add("Seychelles");
            modifiableListOfCountries.add("Sierra Leone");
            modifiableListOfCountries.add("Singapore");
            modifiableListOfCountries.add("Sint Maarten (Dutch part)");
            modifiableListOfCountries.add("Slovakia");
            modifiableListOfCountries.add("Slovenia");
            modifiableListOfCountries.add("Solomon Islands");
            modifiableListOfCountries.add("Somalia");
            modifiableListOfCountries.add("South Africa");
            modifiableListOfCountries.add("South Georgia and the South Sandwich Islands");
            modifiableListOfCountries.add("South Korea");
            modifiableListOfCountries.add("South Sudan");
            modifiableListOfCountries.add("Spain");
            modifiableListOfCountries.add("Sri Lanka");
            modifiableListOfCountries.add("Sudan");
            modifiableListOfCountries.add("Suriname");
            modifiableListOfCountries.add("Svalbard and Jan Mayen");
            modifiableListOfCountries.add("Sweden");
            modifiableListOfCountries.add("Switzerland");
            modifiableListOfCountries.add("Syria");
            modifiableListOfCountries.add("Taiwan");
            modifiableListOfCountries.add("Tajikistan");
            modifiableListOfCountries.add("Tanzania");
            modifiableListOfCountries.add("Thailand");
            modifiableListOfCountries.add("Togo");
            modifiableListOfCountries.add("Tokelau");
            modifiableListOfCountries.add("Tonga");
            modifiableListOfCountries.add("Trinidad and Tobago");
            modifiableListOfCountries.add("Tunisia");
            modifiableListOfCountries.add("Turkey");
            modifiableListOfCountries.add("Turkmenistan");
            modifiableListOfCountries.add("Turks and Caicos Islands");
            modifiableListOfCountries.add("Tuvalu");
            modifiableListOfCountries.add("Uganda");
            modifiableListOfCountries.add("Ukraine");
            modifiableListOfCountries.add("United Arab Emirates");
            modifiableListOfCountries.add("United States");
            modifiableListOfCountries.add("United States Minor Outlying Islands");
            modifiableListOfCountries.add("Uruguay");
            modifiableListOfCountries.add("Uzbekistan");
            modifiableListOfCountries.add("Vanuatu");
            modifiableListOfCountries.add("Vatican City");
            modifiableListOfCountries.add("Venezuela");
            modifiableListOfCountries.add("Vietnam");
            modifiableListOfCountries.add("Virgin Islands, British");
            modifiableListOfCountries.add("Virgin Islands, U.S.");
            modifiableListOfCountries.add("Wallis and Futuna");
            modifiableListOfCountries.add("Western Sahara");
            modifiableListOfCountries.add("Yemen");
            modifiableListOfCountries.add("Zambia");
            modifiableListOfCountries.add("Zimbabwe");
            overseasCountries = Collections.unmodifiableList(modifiableListOfCountries);

            modifiableListOfCountries = new ArrayList<>();
            modifiableListOfCountries.add("England");
            modifiableListOfCountries.add("Northern Ireland");
            modifiableListOfCountries.add("Scotland");
            modifiableListOfCountries.add("Wales");
            ukCountries = Collections.unmodifiableList(modifiableListOfCountries);

            modifiableListOfCountries = new ArrayList<>();
            modifiableListOfCountries.addAll(overseasCountries);
            modifiableListOfCountries.addAll(ukCountries);
            modifiableListOfCountries.add("United Kingdom");
            allCountries = Collections.unmodifiableList(modifiableListOfCountries);
        }


        public static List<String> getOverseasCountries() {
           return overseasCountries;
        }

        public static List<String> getUkCountries() {
           return ukCountries;
        }

        public static List<String> getAllCountries() {
            return allCountries;
        }
}
