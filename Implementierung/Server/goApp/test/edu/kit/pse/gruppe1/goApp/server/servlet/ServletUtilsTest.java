package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.Participant;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;

public class ServletUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    // Do not test: testIsUserAlreadyRegistrated(), testGetGoogleIdByToken() and
    // testGetGoogleNameByToken()

    private User getUser(){
        User usr = new User();
        usr.setUserId(2);
        usr.setName("TestUser");
        usr.setGoogleId("GOOGLE");
        return usr;
    }
    private Group getGroup(){
        Group grp = new Group();
        grp.setFounder(getUser());
        grp.setGroupId(3);
        grp.setName("Test Group");
        return grp;
    }
    
    private Location getLocation(){
        Location loc = new Location();
        loc.setLatitude(34.12);
        loc.setLongitude(05.97);
        loc.setName("Test Location");
        loc.setLocationId(4);
        return loc;
    }
    private Event getEvent(){
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
    public void testCreateJSONListPart() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONEventID() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONEvent() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONLocation() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONGroup() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONUser() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONListEvent() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONDoubleListEvent() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONListUsr() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONListGrp() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONGroupID() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONListLoc() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateJSONError() {
        fail("Not yet implemented");
    }

    @Test
    public void testExtractJSON() {
        fail("Not yet implemented");
    }

}
