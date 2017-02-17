package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.util.Log;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Timestamp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by Katharina Riesterer on 17.02.2017.
 */


public class UtilServiceTest {

    private static Group[] groups;
    private static User[] users;
    private static Location[] locations;
    private static Event[] events;
    private JSONObject json;

    @BeforeClass
    public static void init() {
        users = new User[5];
        for (int i = 0; i < users.length; i++) {
            users[i] = new User(i, "user" + i);
        }
        groups = new Group[5];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = new Group(i, "group" + i, users[i]);
        }
        locations = new Location[5];
        for (int i = 0; i < locations.length; i++) {
            locations[i] = new Location(i, i, "location" + i);
        }
        events = new Event[5];
        for (int i = 0; i < events.length; i++) {
            events[i] = new Event(i, "event" + i, new Timestamp(i * 1000000000), locations[i], users[i]);
        }
    }

    @Test
    public void getGroupsTest() throws JSONException {
        json = new JSONObject();
        for (int i = 0; i < groups.length; i++) {
            JSONObject group = new JSONObject();
            group.put(JSONParameter.GROUP_ID.toString(), groups[i].getId());
            group.put(JSONParameter.GROUP_NAME.toString(), groups[i].getName());
            group.put(JSONParameter.USER_ID.toString(), groups[i].getFounder().getId());
            group.put(JSONParameter.USER_NAME.toString(), groups[i].getFounder().getName());
            group.put(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.OK.getErrorCode());
            json.accumulate(JSONParameter.LIST_GROUP.toString(), group);
        }
        System.out.println(json.toString());

        assertEquals(UtilService.getGroups(json),groups);


    }

    @Before
    public void setUp() throws Exception {


    }
}
