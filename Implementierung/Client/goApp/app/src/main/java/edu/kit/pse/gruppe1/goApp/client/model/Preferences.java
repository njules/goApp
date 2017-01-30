package edu.kit.pse.gruppe1.goApp.client.model;

/**
 * Created by Tobias on 26.01.2017.
 */

public class Preferences {
    private static User user = new User(17, "Maxi");

    public static User getUser(){
        return user;
    }

    public static void setUser(User nUser){
        user = nUser;
    }
}
