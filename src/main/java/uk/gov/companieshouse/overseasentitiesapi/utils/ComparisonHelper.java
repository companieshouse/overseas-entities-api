package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import java.util.Objects;
import java.util.StringJoiner;

import org.apache.commons.lang.StringUtils;
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

        return StringUtils.equalsIgnoreCase(normalise(addressDto.getPropertyNameNumber()),
                normalise(addressApi.getPremises()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getLine1()),
                normalise(addressApi.getAddressLine1()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getLine2()),
                normalise(addressApi.getAddressLine2()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getTown()),
                normalise(addressApi.getLocality()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getCounty()),
                normalise(addressApi.getRegion()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getCountry()),
                normalise(addressApi.getCountry()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getPoBox()),
                normalise(addressApi.getPoBox()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getCareOf()),
                normalise(addressApi.getCareOf()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getPostcode()),
                normalise(addressApi.getPostcode()));
    }

    public static boolean equals(AddressDto addressDto,
            uk.gov.companieshouse.api.model.managingofficerdata.AddressApi addressApi) {
        var nullValuesCheck = handleNulls(addressDto, addressApi);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        return StringUtils.equalsIgnoreCase(normalise(addressDto.getPropertyNameNumber()),
                normalise(addressApi.getPremises()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getLine1()),
                normalise(addressApi.getAddressLine1()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getLine2()),
                normalise(addressApi.getAddressLine2()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getTown()),
                normalise(addressApi.getLocality()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getCounty()),
                normalise(addressApi.getRegion()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getCountry()),
                normalise(addressApi.getCountry()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getPoBox()),
                normalise(addressApi.getPoBox()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getCareOf()),
                normalise(addressApi.getCareOf()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getPostcode()),
                normalise(addressApi.getPostalCode()));
    }

    public static boolean equals(AddressDto addressDto,
            uk.gov.companieshouse.api.model.common.Address address) {
        var nullValuesCheck = handleNulls(addressDto, address);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        return StringUtils.equalsIgnoreCase(normalise(addressDto.getPropertyNameNumber()), normalise(address.getPremises()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getLine1()), normalise(address.getAddressLine1()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getLine2()), normalise(address.getAddressLine2()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getTown()), normalise(address.getLocality()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getCounty()), normalise(address.getRegion()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getCountry()), normalise(address.getCountry()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getPoBox()), normalise(address.getPoBox()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getCareOf()), normalise(address.getCareOf()))
                && StringUtils.equalsIgnoreCase(normalise(addressDto.getPostcode()), normalise(address.getPostalCode()));
    }

    public static boolean equals(CompanyIdentification existing, CompanyIdentification updated) {
        var nullValuesCheck = handleNulls(existing, updated);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        var format = new StringJoiner(",");
        var registerInformationFormat = format.add(updated.getPlaceRegistered()).add(updated.getPlaceRegisteredJurisdiction());
        return StringUtils.equalsIgnoreCase(normalise(existing.getLegalForm()), normalise(updated.getLegalForm()))
                && StringUtils.equalsIgnoreCase(normalise(existing.getGoverningLaw()), normalise(updated.getGoverningLaw()))
                && StringUtils.equalsIgnoreCase(normalise(existing.getRegisterLocation()), normalise(updated.getRegisterLocation()))
                && StringUtils.equalsIgnoreCase(normalise(existing.getPlaceRegistered()), normalise(registerInformationFormat.toString()))
                && StringUtils.equalsIgnoreCase(normalise(existing.getRegistrationNumber()), normalise(updated.getRegistrationNumber()));
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

    public static boolean natureOfControlsEquals(List<String> chipsFormatList, String[] publicDataFormat) {
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

        return StringUtils.equalsIgnoreCase(normalise(personName.toString()),normalise(string));
    }

    public static boolean equalsIndividualMOName(PersonName personName, String string) {
        var nullValuesCheck = handleNulls(personName, string);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }
        String individualMoFormat = personName.getSurname().concat(", ").concat(personName.getForename());
        return StringUtils.equalsIgnoreCase(normalise(individualMoFormat),normalise(string));
    }

    public static boolean equalsIgnoreCase(String string, String other) {
        var nullValuesCheck = handleNulls(string, other);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        return StringUtils.equalsIgnoreCase(normalise(string),normalise(other));
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

        return StringUtils.equalsIgnoreCase(normalise(string),normalise(concatenatedFormerNames));
    }

    private static Boolean handleNulls(Object a, Object b) {
        Boolean bool = null;
        if (a == null || b == null) {
            bool = a == null && b == null;
        }

        return bool;
    }

    public static boolean equalsIndividualNationality(String string, String other) {
        var nullValuesCheck = handleNulls(string, other);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }
        String normalisedNationality = other.endsWith(",") ? other.substring(0, other.length()-1): other;
        return StringUtils.equalsIgnoreCase(normalise(string),normalise(normalisedNationality));
    }
}
