package com.parunev.docconnect.models.enums;

import lombok.Getter;
/**
 * The `Gender` enum defines the possible gender values that can be associated
 * with individuals in the application. Each gender value has a human-readable
 * name associated with it.
 * <p>
 * Enum Values:
 * - `MALE`: Represents the male gender.
 * - `FEMALE`: Represents the female gender.
 * - `OTHER`: Represents a gender option other than male or female.
 * <p>
 * Usage:
 * You can use the `Gender` enum to specify the gender of individuals within
 * your application. For example:
 * <p>
 * ```java
 * // Set the gender for a user profile
 * userProfile.setGender(Gender.FEMALE);
 * <p>
 * // Retrieve the gender of a user profile
 * Gender userGender = userProfile.getGender();
 * ```
 *
 * @see lombok.Getter
 */
@Getter
public enum Gender {

    /**
     * Represents the male gender.
     */
    MALE("Male"),

    /**
     * Represents the female gender.
     */
    FEMALE("Female"),

    /**
     * Represents a gender option other than male or female.
     */
    OTHER("Other");

    /**
     * The human-readable name associated with the gender.
     */
    private final String genderName;

    /**
     * Constructs a `Gender` enum with the specified gender name.
     *
     * @param genderName The human-readable name of the gender.
     */
    Gender(String genderName){
        this.genderName = genderName;
    }
}
