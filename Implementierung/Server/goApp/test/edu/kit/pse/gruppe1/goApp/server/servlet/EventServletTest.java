package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.model.Participant;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;

public class EventServletTest {
    private EventServlet servlet;

    @Mock
    private EventManagement mockEventMang;
    @Mock
    private EventUserManagement mockEventUsrMang;
    @Mock
    private HttpServletRequest mockHttpRequest;
    @Mock
    private HttpServletResponse mockHttpResponse;
    @Mock
    private PrintWriter mockPrintWriter;
    @Mock
    private BufferedReader mockBuffRead;

    @Captor
    private ArgumentCaptor<String> captor;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        servlet = new EventServlet();

        // Setting MockObjects to private Management Classes in Servlet
        String name = "eventMang";
        Field field = servlet.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(servlet, mockEventMang);

        name = "eventUsrMang";
        field = servlet.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(servlet, mockEventUsrMang);

    }

    @After
    public void tearDown() throws Exception {
        servlet = null;
    }

    private JSONObject createJSONEvent(Event event) {
        JSONObject json = new JSONObject();

        try {
            json.put(JSONParameter.EVENT_NAME.toString(), event.getName());
            json.put(JSONParameter.LONGITUDE.toString(), event.getLocation().getLongitude());
            json.put(JSONParameter.LATITUDE.toString(), event.getLocation().getLatitude());
            json.put(JSONParameter.LOC_NAME.toString(), event.getLocation().getName());
            json.put(JSONParameter.EVENT_TIME.toString(), event.getTimestamp().getTime());
            json.put(JSONParameter.USER_ID.toString(), event.getCreator().getUserId());
            json.put(JSONParameter.GROUP_ID.toString(), event.getGroup().getGroupId());
            json.put(JSONParameter.EVENT_ID.toString(), event.getEventId());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        return json;

    }

    private Event createEvent() {
        String name = "Test Event";
        Location loc = new Location(49.014352, 8.404579, "Test Location");
        // Location is somewhere in Karlsruhe
        Timestamp time = new Timestamp(1000);
        Group grp = new Group();
        grp.setGroupId(1);
        User usr = new User();
        usr.setUserId(2);
        Event event = new Event(name, loc, time, grp, usr);
        event.setEventId(3);
        return event;
    }

    private JSONObject createJSONEventwID(Event event) {
        JSONObject json = createJSONEvent(event);
        try {
            json.put(JSONParameter.EVENT_ID.toString(), event.getEventId());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        return json;
    }

    @Test
    public void testCreate() {
        Event event = createEvent();
        JSONObject json = createJSONEvent(event);
        JSONObject newJson = null;

        // Define Mock behaviors
        when(mockEventMang.add(event.getName(), event.getLocation(), event.getTimestamp(),
                event.getCreator().getUserId(), event.getGroup().getGroupId())).thenReturn(event);

        newJson = ServletTestUtils.callMethod(servlet, json, "create");
        try {
            assertEquals(newJson.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());

            // needed to cast, because event.getEventId return Integer instead of int
            assertEquals(newJson.getInt(JSONParameter.EVENT_ID.toString()),
                    (int) event.getEventId());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    private List<Participant> createPartList(){
        List<Participant> part = new ArrayList<Participant>();
        Event e1 = createEvent();
        User u1 = new User();
                
        return part;
    }

    @Test
    public void testGetParticipates() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        List<Participant> part = createPartList();
        try {
            json.put(JSONParameter.EVENT_ID.toString(), 128);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        /*
         * this.status = status; this.event = event; this.user = user;
         */
        // part = this.eventUsrMang.getParticipants(eventID);
    }

    @Ignore
    @Test
    public void testChange() {
        fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testDoGet() {
        fail("Not yet implemented");
    }

    @Ignore
    @Test
    public void testDo() {
        fail("Not yet implemented");
    }

}
