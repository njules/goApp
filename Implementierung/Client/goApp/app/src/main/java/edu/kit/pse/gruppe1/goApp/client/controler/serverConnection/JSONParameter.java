package edu.kit.pse.gruppe1.goApp.client.controler.serverConnection;

import android.support.annotation.Nullable;

/**
 * Enumerations with all possible parameter-types for the JSON-strings.
 */
public enum JSONParameter {
    /**
     * ID of the request
     */
    ID("ID"),
    /**
     * Error code which is 0 if no error occurred.
     */
    ErrorCode("Error code"),
    /**
     * ID of an user.
     */
    UserID("User ID"),
    /**
     * ID of a group.
     */
    GroupID("Group ID"),
    /**
     * ID of an event.
     */
    EventID("Event ID"),
    /**
     * Name of an user.
     */
    UserName("User name"),
    /**
     * Name of a group.
     */
    GroupName("Group name"),
    /**
     * Name of an event.
     */
    EventName("Event name"),
    /**
     * The name of the method which should be executed on the server. For example the create method of the GroupServlet.
     */
    Method("Method");

    private final String fieldDescription;

    private JSONParameter(String description) {
        fieldDescription = description;
    }

    /**
     * Gives the corresponding name to an enum literal. Normally something like the enum literal name.
     */
    @Override
    public String toString() {
        return fieldDescription;
    }

    /**
     * Gives the corresponding enum literal to a string.
     *
     * @param s the string to which the returned JSONParameter should match
     * @return the JSONParameter to the string s
     */
    @Nullable
    public static JSONParameter fromString(String s) {
        for (JSONParameter json : JSONParameter.values()) {
            if (json.toString().equals(s)) {
                return json;
            }
        }
        return null;
    }

}