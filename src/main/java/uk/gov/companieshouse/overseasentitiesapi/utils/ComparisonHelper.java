package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import uk.gov.companieshouse.api.model.officers.FormerNamesApi;
import uk.gov.companieshouse.api.model.officers.OfficerRoleApi;
import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

public class ComparisonHelper {

    private ComparisonHelper() {
    }

    private static String normalise(String value) {
        var text = StringUtils.normalizeSpace(value);
        if (text == null || text.isEmpty()) {
            return null;
        }
        return text;
    }

    public static boolean equals(AddressDto addressDto, AddressApi addressApi) {
        var nullValuesCheck = handleNulls(addressDto, addressApi);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        String addressDtoLine1 = combineAddressLines(addressDto.getPropertyNameNumber(),
                addressDto.getLine1());
        String addressApiLine1 = combineAddressLines(addressApi.getPremises(),
                addressApi.getAddressLine1());

        return fuzzyStringEqual(addressDto.getCareOf(), addressApi.getCareOf())
                && fuzzyStringEqual(addressDto.getPoBox(), addressApi.getPoBox())
                && fuzzyStringEqual(addressDtoLine1, addressApiLine1)
                && fuzzyStringEqual(addressDto.getLine2(), addressApi.getAddressLine2())
                && fuzzyStringEqual(addressDto.getTown(), addressApi.getLocality())
                && fuzzyStringEqual(addressDto.getCounty(), addressApi.getRegion())
                && fuzzyStringEqual(addressDto.getPostcode(), addressApi.getPostcode())
                && fuzzyStringEqual(addressDto.getCountry(), addressApi.getCountry());
    }

    public static boolean equals(AddressDto addressDto,
            uk.gov.companieshouse.api.model.managingofficerdata.AddressApi addressApi) {
        var nullValuesCheck = handleNulls(addressDto, addressApi);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        String addressDtoLine1 = combineAddressLines(addressDto.getPropertyNameNumber(),
                addressDto.getLine1());
        String addressLine1 = combineAddressLines(addressApi.getPremises(),
                addressApi.getAddressLine1());

        return fuzzyStringEqual(addressDto.getCareOf(), addressApi.getCareOf())
                && fuzzyStringEqual(addressDto.getPoBox(), addressApi.getPoBox())
                && fuzzyStringEqual(addressDtoLine1, addressLine1)
                && fuzzyStringEqual(addressDto.getLine2(), addressApi.getAddressLine2())
                && fuzzyStringEqual(addressDto.getTown(), addressApi.getLocality())
                && fuzzyStringEqual(addressDto.getCounty(), addressApi.getRegion())
                && fuzzyStringEqual(addressDto.getPostcode(), addressApi.getPostalCode())
                && fuzzyStringEqual(addressDto.getCountry(), addressApi.getCountry());
    }

    public static boolean equals(AddressDto addressDto,
            uk.gov.companieshouse.api.model.common.Address address) {
        var nullValuesCheck = handleNulls(addressDto, address);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        String addressDtoLine1 = combineAddressLines(addressDto.getPropertyNameNumber(),
                addressDto.getLine1());
        String addressLine1 = combineAddressLines(address.getPremises(), address.getAddressLine1());

        return fuzzyStringEqual(addressDto.getCareOf(), address.getCareOf())
                && fuzzyStringEqual(addressDto.getPoBox(), address.getPoBox())
                && fuzzyStringEqual(addressDtoLine1, addressLine1)
                && fuzzyStringEqual(addressDto.getLine2(), address.getAddressLine2())
                && fuzzyStringEqual(addressDto.getTown(), address.getLocality())
                && fuzzyStringEqual(addressDto.getCounty(), address.getRegion())
                && fuzzyStringEqual(addressDto.getPostcode(), address.getPostalCode())
                && fuzzyStringEqual(addressDto.getCountry(), address.getCountry());
    }

    public static boolean equals(CompanyIdentification existing, CompanyIdentification updated) {
        var nullValuesCheck = handleNulls(existing, updated);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        var format = new StringJoiner(",");
        var registerInformationFormat = format.add(updated.getPlaceRegistered())
                .add(updated.getPlaceRegisteredJurisdiction());

        return fuzzyStringEqual(existing.getLegalForm(), updated.getLegalForm())
                && fuzzyStringEqual(existing.getGoverningLaw(), updated.getGoverningLaw())
                && fuzzyStringEqual(existing.getRegisterLocation(), updated.getRegisterLocation())
                && fuzzyStringEqual(existing.getPlaceRegistered(), registerInformationFormat.toString())
                && fuzzyStringEqual(existing.getRegistrationNumber(), updated.getRegistrationNumber());
    }

    public static boolean equals(LocalDate a, String b) {
        var nullValuesCheck = handleNulls(a, b);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        if (b.contains(" ")) {
            b = b.substring(0, b.indexOf(" "));
        }

        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var localDate = LocalDate.parse(b, formatter);
        return a.equals(localDate);
    }

    public static boolean natureOfControlsEquals(List<String> chipsFormatList,
            String[] publicDataFormat) {
        var nullValuesCheck = handleNulls(chipsFormatList, publicDataFormat);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        var arrayFromList = chipsFormatList
                .stream()
                .map(NatureOfControlTypeMapping.CHIPS_FORMAT_TO_PUBLIC_FORMAT::get)
                .filter(Objects::nonNull)
                .toArray(String[]::new);

        Arrays.sort(arrayFromList);
        Arrays.sort(publicDataFormat);

        return Arrays.equals(arrayFromList, publicDataFormat);
    }

    public static boolean equals(PersonName personName, String string) {
        var nullValuesCheck = handleNulls(personName, string);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        return StringUtils.equalsIgnoreCase(normalise(personName.toString()), normalise(string));
    }

    public static boolean equalsIndividualMOName(PersonName personName, String string) {
        var nullValuesCheck = handleNulls(personName, string);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }
        String individualMoFormat = personName.getSurname().concat(", ")
                .concat(personName.getForename());
        return StringUtils.equalsIgnoreCase(normalise(individualMoFormat), normalise(string));
    }

    public static boolean equalsIgnoreCase(String string, String other) {
        var nullValuesCheck = handleNulls(string, other);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        return StringUtils.equalsIgnoreCase(normalise(string), normalise(other));
    }

    public static boolean equals(Boolean b, boolean b2) {
        var nullValuesCheck = handleNulls(b, b2);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        return b == b2;
    }

    public static boolean equals(String string, OfficerRoleApi officerRoleApi) {
        var nullValuesCheck = handleNulls(string, officerRoleApi);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        return string.equals(officerRoleApi.getOfficerRole());
    }

    public static boolean equals(String string, String[] strings) {
        var nullValuesCheck = handleNulls(string, strings);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        return string.equals(String.join(" ", strings));
    }

    public static boolean equalsFormerName(String string, List<FormerNamesApi> strings) {
        var nullValuesCheck = handleNulls(string, strings);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        var concatenatedFormerNames = FormerNameConcatenation.concatenateFormerNames(strings);

        return StringUtils.equalsIgnoreCase(normalise(string), normalise(concatenatedFormerNames));
    }

    private static Boolean handleNulls(Object a, Object b) {
        Boolean bool = null;
        if (a == null || b == null) {
            bool = a == null && b == null;
        }

        return bool;
    }

    private static String combineAddressLines(String... lines) {
        var joiner = new StringJoiner(" ");

        for (String line : lines) {
            if (line != null) {
                joiner.add(line);
            }
        }

        return joiner.toString();
    }

    public static boolean equalsIndividualNationality(String str, String other) {
        var nullValuesCheck = handleNulls(str, other);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        var normalisedStr = normaliseCommaSeparatedList(str);
        var normalisedOther = normaliseCommaSeparatedList(other);

        return StringUtils.equalsIgnoreCase(normalisedStr, normalisedOther);
    }

    private static String normaliseCommaSeparatedList(String list){
        if(list.contains(",")){
            return Arrays.stream(list.split(","))
                    .map(ComparisonHelper::normalise)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(","));
        } else {
            return normalise(list);
        }
    }

    private static boolean fuzzyStringEqual(String field1, String field2){
        return StringUtils.equalsIgnoreCase(normalise(field1), normalise(field2));
    }
}
