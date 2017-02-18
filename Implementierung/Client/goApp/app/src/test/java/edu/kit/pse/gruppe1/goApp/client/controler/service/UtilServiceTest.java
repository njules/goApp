package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.util.Log;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

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
        users = new User[2];
        for (int i = 0; i < users.length; i++) {
            users[i] = new User(i, "user" + i);
            users[i].setStatus(Status.fromInteger(1));
        }
        groups = new Group[2];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = new Group(i, "group" + i, users[i]);
        }
        locations = new Location[2];
        for (int i = 0; i < locations.length; i++) {
            locations[i] = new Location(i, i, "location" + i);
        }
        events = new Event[2];
        for (int i = 0; i < events.length; i++) {
            events[i] = new Event(i, "event" + i, new Timestamp(i * 1000000000), locations[i], users[i]);
        }
    }


    @Test
    public void getGroups() throws Exception {

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
        json.put(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.OK.getErrorCode());

        assertEquals(UtilService.getGroups(json)[0].getId(), groups[0].getId());
        assertEquals(UtilService.getGroups(json)[0].getName(), groups[0].getName());
        assertEquals(UtilService.getGroups(json)[1].getId(), groups[1].getId());
        assertEquals(UtilService.getGroups(json)[1].getName(), groups[1].getName());


    }

    @Test
    public void getUsers() throws Exception {
        json = new JSONObject();
        for (int i = 0; i < users.length; i++) {
            JSONObject user = new JSONObject();
            user.put(JSONParameter.USER_ID.toString(), users[i].getId());
            user.put(JSONParameter.USER_NAME.toString(), users[i].getName());
            json.accumulate(JSONParameter.LIST_USER.toString(), user);
        }
        json.put(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.OK.getErrorCode());

        assertEquals(UtilService.getUsers(json)[0].getId(), users[0].getId());
        assertEquals(UtilService.getUsers(json)[0].getName(), users[0].getName());
        assertEquals(UtilService.getUsers(json)[1].getId(), users[1].getId());
        assertEquals(UtilService.getUsers(json)[1].getName(), users[1].getName());
    }

    @Test
    public void getParticipants() throws Exception {
        json = new JSONObject();
        for (int i = 0; i < users.length; i++) {
            JSONObject user = new JSONObject();
            user.put(JSONParameter.USER_ID.toString(), users[i].getId());
            user.put(JSONParameter.USER_NAME.toString(), users[i].getName());
            user.put(JSONParameter.STATUS.toString(), users[i].getStatus().toString());
            json.accumulate(JSONParameter.LIST_PART.toString(), user);
        }

        json.put(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.OK.getErrorCode());

        assertEquals(UtilService.getParticipants(json, JSONParameter.LIST_PART.toString())[1].getId(), users[1].getId());
        assertEquals(UtilService.getParticipants(json, JSONParameter.LIST_PART.toString())[1].getName(), users[1].getName());
        assertEquals(UtilService.getParticipants(json, JSONParameter.LIST_PART.toString())[0].getId(), users[0].getId());
        assertEquals(UtilService.getParticipants(json, JSONParameter.LIST_PART.toString())[0].getName(), users[0].getName());
    }

    @Test
    public void getLocations() throws Exception {
        json = new JSONObject();
        for (int i = 0; i < locations.length; i++) {
            JSONObject location = new JSONObject();
            location.put(JSONParameter.LONGITUDE.toString(), locations[i].getLongitude());
            location.put(JSONParameter.LATITUDE.toString(), locations[i].getLatitude());
            location.put(JSONParameter.LOC_NAME.toString(), locations[i].getName());
            json.accumulate(JSONParameter.LIST_LOC.toString(), location);
        }

        json.put(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.OK.getErrorCode());

        assertEquals(UtilService.getLocations(json)[0].getLongitude(), locations[0].getLongitude(), 0.001);
        assertEquals(UtilService.getLocations(json)[0].getLatitude(), locations[0].getLatitude(), 0.001);
        assertEquals(UtilService.getLocations(json)[0].getName(), locations[0].getName());
        assertEquals(UtilService.getLocations(json)[1].getLongitude(), locations[1].getLongitude(), 0.001);
        assertEquals(UtilService.getLocations(json)[1].getLatitude(), locations[1].getLatitude(), 0.001);
        assertEquals(UtilService.getLocations(json)[1].getName(), locations[1].getName());

    }

    @Test
    public void isErrorTrue() throws Exception {
        json = new JSONObject();
        json.put(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.ERROR_ON_SERVER.getErrorCode());
        assert (UtilService.isError(json));
    }

    @Test
    public void isErrorFalse() throws Exception {
        json = new JSONObject();
        json.put(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.OK.getErrorCode());
        assertFalse(UtilService.isError(json));
    }

    @Test
    public void getError() throws Exception {
        json = new JSONObject();
        json.put(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.USR_LIMIT.getErrorCode());

        assertEquals(UtilService.getError(json), "Group is full");
    }

    @Test
    public void getEvents() throws Exception {
        JSONArray jsons = new JSONArray();
        for (int i = 0; i < users.length; i++) {
            JSONObject event = new JSONObject();
            event.put(JSONParameter.EVENT_ID.toString(), events[i].getId());
            event.put(JSONParameter.EVENT_NAME.toString(), events[i].getName());
            event.put(JSONParameter.EVENT_TIME.toString(), events[i].getTime().getTime());
            event.put(JSONParameter.USER_ID.toString(), events[i].getCreator().getId());
            event.put(JSONParameter.USER_NAME.toString(), events[i].getCreator().getName());
            event.put(JSONParameter.LONGITUDE.toString(), events[i].getLocation().getLongitude());
            event.put(JSONParameter.LATITUDE.toString(), events[i].getLocation().getLatitude());
            event.put(JSONParameter.LOC_NAME.toString(), events[i].getLocation().getName());
            jsons.put(i, event);
        }

        assertEquals(UtilService.getEvents(jsons)[0].getId(), events[0].getId());
        assertEquals(UtilService.getEvents(jsons)[0].getName(), events[0].getName());
        assertEquals(UtilService.getEvents(jsons)[1].getId(), events[1].getId());
        assertEquals(UtilService.getEvents(jsons)[1].getName(), events[1].getName());
    }
}
