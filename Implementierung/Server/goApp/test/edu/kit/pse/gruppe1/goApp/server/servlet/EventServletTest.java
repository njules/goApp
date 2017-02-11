package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
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
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

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

    
    @Test
    public void testCreate() {
        Event event = createEvent();
        JSONObject json = createJSONEvent(event);
        JSONObject newJson = null;

        try {
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        // Define Mock behaviors
        when(mockEventMang.add(event.getName(), event.getLocation(), event.getTimestamp(),
                event.getCreator().getUserId(), event.getGroup().getGroupId())).thenReturn(event);
        try {
            when(mockHttpResponse.getWriter()).thenReturn(mockPrintWriter);
            when(mockBuffRead.readLine()).thenReturn(json.toString());
            when(mockHttpRequest.getReader()).thenReturn(mockBuffRead);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        try {
            servlet.doPost(mockHttpRequest, mockHttpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail();
        }

        verify(mockPrintWriter).println(captor.capture());
        
        try {
            newJson = new JSONObject(captor.getValue());
            assertEquals(newJson.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testGetParticipates() {
        JSONObject json = new JSONObject();
        JSONObject internJson = null;
        JSONArray arrJson = null;
        JSONObject newJson = null;
        List<Participant> part = createPartList();
        int eventID = createEvent().getEventId();
        int userID = -1;
        
        try {
            json.put(JSONParameter.METHOD.toString(), Methods.GET_EVENT);
            json.put(JSONParameter.EVENT_ID.toString(), eventID);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        when(mockEventUsrMang.getParticipants(eventID)).thenReturn(part);
        
        try {
            when(mockHttpResponse.getWriter()).thenReturn(mockPrintWriter);
            when(mockBuffRead.readLine()).thenReturn(json.toString());
            when(mockHttpRequest.getReader()).thenReturn(mockBuffRead);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        try {
            servlet.doPost(mockHttpRequest, mockHttpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail();
        }

        verify(mockPrintWriter).println(captor.capture());

        try {
            newJson = new JSONObject(captor.getValue());
            arrJson = newJson.getJSONArray(JSONParameter.LIST_PART.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        for (int i = 0; i < arrJson.length(); i++) {
            try {
                internJson = arrJson.getJSONObject(i);
                userID = internJson.getInt(JSONParameter.USER_ID.toString());

                for (Participant p : part) {
                    if (p.getUser().getUserId() == userID) {
                        assertEquals(p.getStatus().intValue(),
                                internJson.getInt(JSONParameter.STATUS.toString()));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    @Test
    public void testChange() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        Event event = createEvent();

        when(mockEventMang.getEvent(event.getEventId())).thenReturn(event);

        try {
            json.put(JSONParameter.EVENT_ID.toString(), event.getEventId());
            json.put(JSONParameter.METHOD.toString(), Methods.CHANGE.toString());
            json.put(JSONParameter.EVENT_NAME.toString(), "New Name");
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockEventMang.update(any(Event.class))).thenReturn(true);
        try {
            when(mockHttpResponse.getWriter()).thenReturn(mockPrintWriter);
            when(mockBuffRead.readLine()).thenReturn(json.toString());
            when(mockHttpRequest.getReader()).thenReturn(mockBuffRead);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        
        try {
            servlet.doPost(mockHttpRequest, mockHttpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail();
        }

        verify(mockPrintWriter).println(captor.capture());


        try {
            newJson = new JSONObject(captor.getValue());
            assertEquals(newJson.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
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
    
    private List<Participant> createPartList() {
        List<Participant> part = new ArrayList<Participant>();
        Event e1 = createEvent();
        User u1 = new User();
        User u2 = new User();
        u1.setUserId(1);
        u2.setUserId(2);
        part.add(new Participant(Status.INVITED.getValue(), e1, u1));
        part.add(new Participant(Status.PARTICIPATE.getValue(), e1, u2));
        return part;
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

   


}
