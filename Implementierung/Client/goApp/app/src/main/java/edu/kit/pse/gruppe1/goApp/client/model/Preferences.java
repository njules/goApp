package edu.kit.pse.gruppe1.goApp.client.model;

/**
 * This class saves a small amount of data the App needs to access at various Activities.
 */

public class Preferences {
    private static User user;
    private static Group group;
    private static String idToken;

    /**
     * @return the User who is currently logged in.
     */
    public static User getUser() {
        return user;
    }

    /**
     * @param nUser the new User.
     */
    public static void setUser(User nUser) {
        user = nUser;
    }

    /**
     * @return the current Group.
     */
    public static Group getGroup() {
        return group;
    }

    /**
     * @param group the new Group.
     */
    public static void setGroup(Group group) {
        Preferences.group = group;
    }

    /**
     * @return the currently saved Google Token.
     */
    public static String getIdToken() {
        return idToken;
    }

    /**
     * @param idToken the new Google Token.
     */
    public static void setIdToken(String idToken) {
        Preferences.idToken = idToken;
    }
}
