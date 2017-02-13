package edu.kit.pse.gruppe1.goApp.client.model;

/**
 * Created by Tobias on 26.01.2017.
 */

public class Preferences {
    private static User user;
    private static Group group;
    private static String idToken;

    public static User getUser(){
        return user;
    }

    public static void setUser(User nUser){
        user = nUser;
    }

    public static Group getGroup() {
        return group;
    }

    public static void setGroup(Group group) {
        Preferences.group = group;
    }

    public static String getIdToken() {
        return idToken;
    }

    public static void setIdToken(String idToken) {
        Preferences.idToken = idToken;
    }
}
