package uk.gov.companieshouse.overseasentitiesapi.utils;

import static uk.gov.companieshouse.overseasentitiesapi.utils.FormerNameConcatenation.concatenateFormerNames;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import uk.gov.companieshouse.api.model.common.Address;
import uk.gov.companieshouse.api.model.officers.FormerNamesApi;
import uk.gov.companieshouse.api.model.officers.OfficerRoleApi;
import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
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

    public static boolean equals(AddressDto addressDto, Address address) {
        var nullValuesCheck = handleNulls(addressDto, address);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        return Objects.equals(addressDto.getPropertyNameNumber(), address.getPremises())
                && Objects.equals(addressDto.getLine1(), address.getAddressLine1())
                && Objects.equals(addressDto.getLine2(), address.getAddressLine2())
                && Objects.equals(addressDto.getTown(), address.getLocality())
                && Objects.equals(addressDto.getCounty(), address.getRegion())
                && Objects.equals(addressDto.getCountry(), address.getCountry())
                && Objects.equals(addressDto.getPoBox(), address.getPoBox())
                && Objects.equals(addressDto.getCareOf(), address.getCareOf())
                && Objects.equals(addressDto.getPostcode(), address.getPostalCode());
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

    public static boolean equals(List<String> list, String[] array) {
        var nullValuesCheck = handleNulls(list, array);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        var arrayFromList = list.toArray();
        Arrays.sort(arrayFromList);
        Arrays.sort(array);
        return Arrays.equals(arrayFromList, array);
    }

    public static boolean equals(PersonName personName, String string) {
        var nullValuesCheck = handleNulls(personName, string);
        if (nullValuesCheck != null) {
            return nullValuesCheck;
        }

        return personName.toString().equals(string);
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

        var concatenatedFormerNames = concatenateFormerNames(strings);

        return string.equals(concatenatedFormerNames);
    }

    private static Boolean handleNulls(Object a, Object b) {
        Boolean bool = null;
        if (a == null || b == null) {
            bool = a == null && b == null;
        }

        return bool;
    }
}
