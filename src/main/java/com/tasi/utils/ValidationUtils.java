
package com.tasi.utils;

public class ValidationUtils {

    private ValidationUtils() {
        // Utility classes should not have constructors
    }

    public static boolean isNotEmpty(String value) {
        return null != value && !value.trim().isEmpty();
    }
}
