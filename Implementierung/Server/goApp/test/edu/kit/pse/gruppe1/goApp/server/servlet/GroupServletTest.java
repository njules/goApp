package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupUserManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class GroupServletTest {
    private GroupServlet servlet;
    private String jsonRequest;
    
    @Mock
    private HttpServletRequest httpRequest;
    @Mock
    private HttpServletResponse httpResponse;
    @Mock
    private BufferedReader request;
    @Mock
    private PrintWriter response;
    @Mock
    private GroupManagement groupManager;
    @Mock
    private GroupUserManagement groupUserManager;
    @Mock
    private UserManagement userManager;
    @Mock
    private EventUserManagement eventUserManager;
    
    @Captor
    private ArgumentCaptor<String> argCap;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        servlet = new GroupServlet();
        Field field = servlet.getClass().getDeclaredField("groupManager");
        field.setAccessible(true);
        field.set(servlet, groupManager);
        field = servlet.getClass().getDeclaredField("groupUserManager");
        field.setAccessible(true);
        field.set(servlet, groupUserManager);
        field = servlet.getClass().getDeclaredField("userManager");
        field.setAccessible(true);
        field.set(servlet, userManager);
        field = servlet.getClass().getDeclaredField("eventUserManager");
        field.setAccessible(true);
        field.set(servlet, eventUserManager);
    }

    @After
    public void tearDown() throws Exception {
        servlet = null;
        jsonRequest = null;
    }

    @Test
    public void testGroupCreating() {
        // set up input
        final String name = "Die Illuminati";
        final int founder = 666;
        final int groupID = 36;
        final Group group = new Group();
        group.setGroupId(groupID);
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.CREATE);
            json.put(JSONParameter.GROUP_NAME.toString(), name);
            json.put(JSONParameter.USER_ID.toString(), founder);
            jsonRequest = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to create JSON request!\n");
        }
        // initialize mocking
        try {
            when(httpRequest.getReader()).thenReturn(request);
            when(httpResponse.getWriter()).thenReturn(response);
            when(request.readLine()).thenReturn(jsonRequest);
            when(groupManager.add(name, founder)).thenReturn(group);
       } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            fail("Failed mocking!\n");
        }
        // call method
        try {
            servlet.doPost(httpRequest, httpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail("Failed to post HTTP request!\n");
        }
        // test for correct results
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.OK.getErrorCode());
            assertEquals(json.getInt(JSONParameter.GROUP_ID.toString()), group.getGroupId().intValue());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    @Test
    public void testGroupDeleting() {
        // set up input
        final int group = 2;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.DELETE);
            json.put(JSONParameter.GROUP_ID.toString(), group);
            jsonRequest = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to create JSON request!\n");
        }
        // initialize mocking
        try {
            when(httpRequest.getReader()).thenReturn(request);
            when(httpResponse.getWriter()).thenReturn(response);
            when(request.readLine()).thenReturn(jsonRequest);
            when(groupManager.delete(group)).thenReturn(true);
       } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            fail("Failed mocking!\n");
        }
        // call method
        try {
            servlet.doPost(httpRequest, httpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail("Failed to post HTTP request!\n");
        }
        // test for correct results
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    //TODO DB_ERROR is returned by servlet
    @Test
    public void testNameChanges() {
        // set up input
        final int group = 5;
        final String name = "take over Karlsruhe";
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.DELETE);
            json.put(JSONParameter.GROUP_NAME.toString(), name);
            json.put(JSONParameter.GROUP_ID.toString(), group);
            jsonRequest = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to create JSON request!\n");
        }
        // initialize mocking
        try {
            when(httpRequest.getReader()).thenReturn(request);
            when(httpResponse.getWriter()).thenReturn(response);
            when(request.readLine()).thenReturn(jsonRequest);
            when(groupManager.updateName(group, name)).thenReturn(true);
       } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            fail("Failed mocking!\n");
        }
        // call method
        try {
            servlet.doPost(httpRequest, httpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail("Failed to post HTTP request!\n");
        }
        // test for correct results
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void testMemberKicking() {
        // set up input
        final int group = 5;
        final int member = 5;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.DEL_MEM);
            json.put(JSONParameter.USER_ID.toString(), 5);
            json.put(JSONParameter.GROUP_ID.toString(), group);
            jsonRequest = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to create JSON request!\n");
        }
        // initialize mocking
        try {
            when(httpRequest.getReader()).thenReturn(request);
            when(httpResponse.getWriter()).thenReturn(response);
            when(request.readLine()).thenReturn(jsonRequest);
            when(groupUserManager.delete(group, member)).thenReturn(true);
       } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            fail("Failed mocking!\n");
        }
        // call method
        try {
            servlet.doPost(httpRequest, httpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail("Failed to post HTTP request!\n");
        }
        // test for correct results
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    //TODO fuck this test
    @Test
    public void testEventRequesting() {
        // set up input
        final int group = 80;
        final int user = 77;
        final Group randomGroup = new Group();
        final User randomFounder = new User();
        final Location randomPlace = new Location(0, 0, "null");
        final Timestamp randomTime = new Timestamp(0);
        final List<Event> fakePending = new ArrayList<Event>();
        fakePending.add(new Event("secret meeting", randomPlace, randomTime, randomGroup, randomFounder));
        final List<Event> fakeFriends = new ArrayList<Event>();
        fakeFriends.add(new Event("you're not invited, go away", randomPlace, randomTime, randomGroup, randomFounder));
        final List<Event> fakeMeeting = new ArrayList<Event>();
        fakeMeeting.add(new Event("why do you have to make everything worse", randomPlace, randomTime, randomGroup, randomFounder));
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_EVENT);
            json.put(JSONParameter.USER_ID.toString(), user);
            json.put(JSONParameter.GROUP_ID.toString(), group);
            jsonRequest = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to create JSON request!\n");
        }
        // initialize mocking
        try {
            when(httpRequest.getReader()).thenReturn(request);
            when(httpResponse.getWriter()).thenReturn(response);
            when(request.readLine()).thenReturn(jsonRequest);
            when(eventUserManager.getEventsByStatus(Status.INVITED, group , user)).thenReturn(fakePending);
            when(eventUserManager.getEventsByStatus(Status.PARTICIPATE, group, user)).thenReturn(fakeFriends);
            when(eventUserManager.getEventsByStatus(Status.STARTED, group, user)).thenReturn(fakeMeeting);
       } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            fail("Failed mocking!\n");
        }
        // call method
        try {
            servlet.doPost(httpRequest, httpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail("Failed to post HTTP request!\n");
        }
        // test for correct event list
        verify(response).println(argCap.capture());
        List<Event> pending = new ArrayList<Event>();
        List<Event> going = new ArrayList<Event>();
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.OK.getErrorCode());
            JSONArray array = json.getJSONArray(JSONParameter.NEW_EVENTS.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonEvt = array.getJSONObject(i);
                pending.add(new Event(jsonEvt.getString(JSONParameter.EVENT_NAME.toString()), null, null, null, null));
            }
            array = json.getJSONArray(JSONParameter.ACC_EVENTS.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonEvt = array.getJSONObject(i);
                going.add(new Event(jsonEvt.getString(JSONParameter.EVENT_NAME.toString()), null, null, null, null));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
        for (Event test : fakePending) {
            assertTrue(pending.contains(test));
            pending.remove(test);
        }
        assertTrue(pending.isEmpty());
        for (Event test : fakeFriends) {
            assertTrue(going.contains(test));
            going.remove(test);
        }
        for (Event test : fakeMeeting) {
            assertTrue(going.contains(test));
            going.remove(test);
        }
        assertTrue(going.isEmpty());
    }
    
    //TODO
    @Test
    public void testMemberRequesting() {
        // set up input
        final List<User> fakeUsers = new ArrayList<User>();
        fakeUsers.add(new User("0", "4ever"));
        fakeUsers.add(new User("1", "alone"));
        final int group = 65;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SYNC_LOC);
            json.put(JSONParameter.GROUP_ID.toString(), group);
            jsonRequest = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to create JSON request!\n");
        }
        // initialize mocking
        try {
            when(httpRequest.getReader()).thenReturn(request);
            when(httpResponse.getWriter()).thenReturn(response);
            when(request.readLine()).thenReturn(jsonRequest);
            when(groupUserManager.getUsers(group)).thenReturn(fakeUsers);
       } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            fail("Failed mocking!\n");
        }
        // call method
        try {
            servlet.doPost(httpRequest, httpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail("Failed to post HTTP request!\n");
        }
        // test for correct location list
        verify(response).println(argCap.capture());
        List<User> result = new ArrayList<User>();
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.OK.getErrorCode());
            JSONArray array = json.getJSONArray(JSONParameter.LIST_USER.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonUsr = array.getJSONObject(i);
                result.add(new User(jsonUsr.getString(JSONParameter.USER_ID.toString()), jsonUsr.getString(JSONParameter.USER_NAME.toString())));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
        for (User user : result) {
            assertTrue(fakeUsers.contains(user));
            fakeUsers.remove(user);
        }
        assertTrue(fakeUsers.isEmpty());
    }
    
    @Test
    public void testFounderChanging() {
        // set up input
        final User newFounder = new User("7", null);
        final int group = 6;
        final int newFounderID = 7;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SET_FOUNDER);
            json.put(JSONParameter.GROUP_ID.toString(), group);
            json.put(JSONParameter.USER_ID.toString(), newFounderID);
            jsonRequest = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to create JSON request!\n");
        }
        // initialize mocking
        try {
            when(httpRequest.getReader()).thenReturn(request);
            when(httpResponse.getWriter()).thenReturn(response);
            when(request.readLine()).thenReturn(jsonRequest);
            when(userManager.getUser(newFounderID)).thenReturn(newFounder);
            when(groupManager.updateFounder(group, newFounder)).thenReturn(true);
       } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            fail("Failed mocking!\n");
        }
        // call method
        try {
            servlet.doPost(httpRequest, httpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail("Failed to post HTTP request!\n");
        }
        // test for correct results
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
}
