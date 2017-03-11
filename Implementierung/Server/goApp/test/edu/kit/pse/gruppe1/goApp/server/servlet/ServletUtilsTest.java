package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.Participant;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;

public class ServletUtilsTest {

    private static final double DELTA = 1e-15;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    // Do not test: testIsUserAlreadyRegistrated(), testGetGoogleIdByToken() and
    // testGetGoogleNameByToken()

    private User getUser() {
        User usr = new User();
        usr.setUserId(2);
        usr.setName("TestUser");
        usr.setGoogleId("GOOGLE");
        return usr;
    }

    private Group getGroup() {
        Group grp = new Group();
        grp.setFounder(getUser());
        grp.setGroupId(3);
        grp.setName("Test Group");
        return grp;
    }

    private Location getLocation() {
        Location loc = new Location();
        loc.setLatitude(34.12);
        loc.setLongitude(05.97);
        loc.setName("Test Location");
        loc.setLocationId(4);
        return loc;
    }

    private Event getEvent() {
        Event evt = new Event();
        evt.setName("New Event");
        evt.setLocation(getLocation());
        evt.setTimestamp(new Timestamp(1234));
        evt.setGroup(getGroup());
        evt.setCreator(getUser());
        evt.setEventId(5);
        return evt;
    }

    @Test
    public void testCreateJSONParticipate() {
        User usr = getUser();
        Event ev = getEvent();
        Participant part = new Participant(Status.STARTED.getValue(), ev, usr);

        JSONObject json = ServletUtils.createJSONParticipate(part);

        try {
            assertEquals(json.getInt(JSONParameter.USER_ID.toString()), usr.getUserId().intValue());
            assertEquals(json.getString(JSONParameter.USER_NAME.toString()), usr.getName());
            assertEquals(json.getInt(JSONParameter.STATUS.toString()), part.getStatus().intValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCreateJSONParticipateJSONError() {
        Participant part = new Participant();
        JSONObject json = ServletUtils.createJSONParticipate(part);

        assertEquals(json, null);

    }

    @Test
    public void testCreateJSONEventID() {
        Event evt = getEvent();

        JSONObject json = ServletUtils.createJSONEventID(evt);

        try {
            assertEquals(json.getInt(JSONParameter.EVENT_ID.toString()),
                    evt.getEventId().intValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testDLPartUsersStartedNull() {
        List<User> started = null;
        List<User> part = new ArrayList<User>(1);
        JSONObject json = ServletUtils.createJSONDoubleListPartUsers(started, part);
        try {
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.EMPTY_LIST.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDLPartUsersParticpNull() {
        List<User> started = new ArrayList<User>(1);
        List<User> part = null;
        JSONObject json = ServletUtils.createJSONDoubleListPartUsers(started, part);
        try {
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.EMPTY_LIST.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDLPartUsersParticpEmpty() {
        List<User> started = new ArrayList<User>(1);
        List<User> part = new ArrayList<User>();
        JSONObject json = ServletUtils.createJSONDoubleListPartUsers(started, part);
        try {
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.EMPTY_LIST.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDLPartUsersStartedEmpty() {
        List<User> started = new ArrayList<User>(1);
        List<User> part = new ArrayList<User>();
        JSONObject json = ServletUtils.createJSONDoubleListPartUsers(started, part);
        try {
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.EMPTY_LIST.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDLPartUsersPartFilled() {
        List<User> started = new ArrayList<User>(1);
        List<User> part = new ArrayList<User>();
        part.add(getUser());
        part.add(getUser());
        part.add(getUser());
        JSONObject json = ServletUtils.createJSONDoubleListPartUsers(started, part);
        try {
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCreateJSONEvent() {
        Event event = getEvent();

        JSONObject json = ServletUtils.createJSONEvent(event);

        try {
            assertEquals(json.getInt(JSONParameter.EVENT_ID.toString()),
                    event.getEventId().intValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
            assertEquals(json.getString(JSONParameter.EVENT_NAME.toString()), event.getName());
            assertEquals(json.getLong(JSONParameter.EVENT_TIME.toString()),
                    event.getTimestamp().getTime());

            assertEquals(json.getString(JSONParameter.LOC_NAME.toString()),
                    event.getLocation().getName());
            assertEquals(json.getDouble(JSONParameter.LONGITUDE.toString()),
                    event.getLocation().getLongitude().doubleValue(), DELTA);
            assertEquals(json.getDouble(JSONParameter.LATITUDE.toString()),
                    event.getLocation().getLatitude(), DELTA);

            assertEquals(json.getInt(JSONParameter.GROUP_ID.toString()),
                    event.getGroup().getGroupId().intValue());
            assertEquals(json.getInt(JSONParameter.USER_ID.toString()),
                    event.getCreator().getUserId().intValue());

        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testCreateJSONDoubleListEventListNull() {
        List<Event> accEvt = null;
        List<Event> newEvt = new ArrayList<Event>(1);
        JSONObject json = ServletUtils.createJSONDoubleListEvent(accEvt, newEvt);
        try {
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.EMPTY_LIST.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCreateJSONEventIDNull() {
        JSONObject json = ServletUtils.createJSONEventID(null);
        assertEquals(json, null);
    }

    @Test
    public void testCreateJSONEventNull() {
        JSONObject json = ServletUtils.createJSONEvent(null);
        assertEquals(json, null);
    }

    @Test
    public void testCreateJSONLocation() {
        Location loc = getLocation();

        JSONObject json = ServletUtils.createJSONLocation(loc);

        try {
            assertEquals(json.getString(JSONParameter.LOC_NAME.toString()), loc.getName());
            assertEquals(json.getDouble(JSONParameter.LONGITUDE.toString()),
                    loc.getLongitude().doubleValue(), DELTA);
            assertEquals(json.getDouble(JSONParameter.LATITUDE.toString()), loc.getLatitude(),
                    DELTA);
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCreateJSONGroup() {
        Group group = getGroup();
        JSONObject json = null;
        json = ServletUtils.createJSONGroup(group);
        try {
            assertEquals(json.getInt(JSONParameter.USER_ID.toString()),
                    group.getFounder().getUserId().intValue());
            assertEquals(json.getString(JSONParameter.USER_NAME.toString()),
                    group.getFounder().getName());
            assertEquals(json.getString(JSONParameter.GROUP_NAME.toString()), group.getName());
            assertEquals(json.getInt(JSONParameter.GROUP_ID.toString()),
                    group.getGroupId().intValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateJSONUser() {
        User user = getUser();
        JSONObject json = null;
        json = ServletUtils.createJSONUser(user);

        try {
            assertEquals(json.getInt(JSONParameter.USER_ID.toString()),
                    user.getUserId().intValue());
            assertEquals(json.getString(JSONParameter.USER_NAME.toString()), user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateJSONGroupID() {
        Group group = getGroup();
        JSONObject json = null;
        json = ServletUtils.createJSONGroupID(group);
        try {
            assertEquals(json.getInt(JSONParameter.GROUP_ID.toString()),
                    group.getGroupId().intValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateJSONError() {
        ErrorCodes error = ErrorCodes.GRP_LIMIT;
        JSONObject json = null;
        json = ServletUtils.createJSONError(error);
        try {
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), error.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
