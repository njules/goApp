package edu.kit.pse.gruppe1.goApp.client.model;

/**
 * Created by Tobias on 26.01.2017.
 */

public class Preferences {
    private static User user = new User(1, "Maxi");
    private static Group group;

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
}
