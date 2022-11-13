package io.tech1.framework.domain.utilities.http;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class HttpRequestFieldsUtility {

    private static final Pattern CAMEL_CASE_LETTERS_AND_NUMBERS_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");

    public static boolean containsCamelCaseLettersAndNumbersWithLength(String field, int length) {
        return CAMEL_CASE_LETTERS_AND_NUMBERS_PATTERN.matcher(field).matches() && field.length() >= length;
    }

    public static boolean isEmail(String field) {
        return EMAIL_PATTERN.matcher(field).matches();
    }
}
