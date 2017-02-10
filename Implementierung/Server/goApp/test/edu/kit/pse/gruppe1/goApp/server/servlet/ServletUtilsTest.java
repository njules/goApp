package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.Participant;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
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

    @Test
    public void testCreateJSONParticipate() {
        fail("Not yet implemented");
        /* Participant part = new Participant(Status.STARTED);
        
        json.put(JSONParameter.USER_ID.toString(), part.getUser().getUserId());
        json.put(JSONParameter.USER_NAME.toString(), part.getUser().getName());
        json.put(JSONParameter.STATUS.toString(), part.getStatus());
        json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());*/
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
