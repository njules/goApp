package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;

public class GoServletTest {
    
    private JSONObject request;
    private EventUserManagement mockEvtUsrMng;

    @Before
    public void setUp() throws Exception {
        request = new JSONObject();
        GoServlet servlet = new GoServlet();
        Field eventUserManager = servlet.getClass().getDeclaredField("eventUser");
        eventUserManager.setAccessible(true);
        eventUserManager.set(servlet, mockEvtUsrMng);
    }

    @After
    public void tearDown() throws Exception {
        request = null;
    }

    @Test
    public void testStartedParticipants() {
        fail("Not yet implemented");
    }

    @Test
    public void testGo() {
        fail("Not yet implemented");
    }

}
