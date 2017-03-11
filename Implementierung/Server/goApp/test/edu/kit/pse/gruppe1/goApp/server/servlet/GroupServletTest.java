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
import java.util.List;

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

    @Test
    public void testNameChanges() {
        // set up input
        final int group = 5;
        final String name = "take over Karlsruhe";
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SET_NAME);
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
            json.put(JSONParameter.USER_ID.toString(), member);
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
            when(eventUserManager.getEventsByStatus(Status.PARTICIPATE, group, user)).thenReturn(new ArrayList<Event>(fakeFriends));
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
        try {//System.out.println(fakePending);System.out.println(fakeFriends);System.out.println(fakeMeeting);
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
            boolean contains = false;
            for (Event result : pending) {
                if (result.getName().equals(test.getName())) {
                    contains = true;
                    break;
                }
            }
            assertTrue(contains);
        }
        assertEquals(pending.size(), fakePending.size());
        for (Event test : fakeFriends) {
            boolean contains = false;
            for (Event result : going) {
                if (result.getName().equals(test.getName())) {
                    contains = true;
                    break;
                }
            }
            assertTrue(contains);
        }
        for (Event test : fakeMeeting) {
            boolean contains = false;
            for (Event result : going) {
                if (result.getName().equals(test.getName())) {
                    contains = true;
                    break;
                }
            }
            assertTrue(contains);
        }
        assertEquals(going.size(), fakeFriends.size() + fakeMeeting.size());
    }
    
    @Test
    public void testMemberRequesting() {
        // set up input
        final List<User> fakeUsers = new ArrayList<User>();
        User user1 = new User(null, "4ever");
        user1.setUserId(1);
        fakeUsers.add(user1);
        User user2 = new User(null, "alone");
        user2.setUserId(3);
        fakeUsers.add(user2);
        final int group = 65;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_MEMBERS);
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
                User user = new User(null, jsonUsr.getString(JSONParameter.USER_NAME.toString()));
                user.setUserId(jsonUsr.getInt(JSONParameter.USER_ID.toString()));
                result.add(user);
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

    @Test
    public void missingMethod() {
        JSONObject json = new JSONObject();
        jsonRequest = json.toString();
        // initialize mocking
        try {
            when(httpRequest.getReader()).thenReturn(request);
            when(httpResponse.getWriter()).thenReturn(response);
            when(request.readLine()).thenReturn(jsonRequest);
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
            JSONObject response = new JSONObject(argCap.getValue());
            assertEquals(response.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    @Test
    public void invalidMethod() {
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SYNC_LOC);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.METH_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void createMissingParameter() {
        // set up input
        final int founder = 666;
        final int groupID = 36;
        final Group group = new Group();
        group.setGroupId(groupID);
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.CREATE);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void createDatabaseError() {
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
            when(groupManager.add(name, founder)).thenReturn(null);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    @Test
    public void deleteMissingParameter() {
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.DELETE);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    @Test
    public void deleteDatabaseError() {
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
            when(groupManager.delete(group)).thenReturn(false);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    @Test
    public void setNameMissingParameter() {
        // set up input
        final int group = 5;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SET_NAME);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    @Test
    public void setNameDatabaseError() {
        // set up input
        final int group = 5;
        final String name = "take over Karlsruhe";
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SET_NAME);
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
            when(groupManager.updateName(group, name)).thenReturn(false);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    @Test
    public void deleteMemberMissingParameter() {
        // set up input
        final int group = 5;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.DEL_MEM);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    @Test
    public void deleteMemberDatabaseError() {
        // set up input
        final int group = 5;
        final int member = 5;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.DEL_MEM);
            json.put(JSONParameter.USER_ID.toString(), member);
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
            when(groupManager.delete(group)).thenReturn(false);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void getEventsMissingParameter() {
        // set up input
        final int user = 77;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_EVENT);
            json.put(JSONParameter.USER_ID.toString(), user);
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
        try {//System.out.println(fakePending);System.out.println(fakeFriends);System.out.println(fakeMeeting);
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void getEventsDatabaseParticipantsError() {
        // set up input
        final int group = 80;
        final int user = 77;
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
            when(eventUserManager.getEventsByStatus(Status.PARTICIPATE, group, user)).thenReturn(null);
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
        try {//System.out.println(fakePending);System.out.println(fakeFriends);System.out.println(fakeMeeting);
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void getEventsDatabaseStartedError() {
        // set up input
        final int group = 80;
        final int user = 77;
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
            when(eventUserManager.getEventsByStatus(Status.PARTICIPATE, group, user)).thenReturn(new ArrayList<Event>());
            when(eventUserManager.getEventsByStatus(Status.STARTED, group, user)).thenReturn(null);
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
        try {//System.out.println(fakePending);System.out.println(fakeFriends);System.out.println(fakeMeeting);
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void getEventsDatabaseInvitedError() {
        // set up input
        final int group = 80;
        final int user = 77;
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
            when(eventUserManager.getEventsByStatus(Status.PARTICIPATE, group, user)).thenReturn(new ArrayList<Event>());
            when(eventUserManager.getEventsByStatus(Status.STARTED, group, user)).thenReturn(new ArrayList<Event>());
            when(eventUserManager.getEventsByStatus(Status.INVITED, group, user)).thenReturn(null);
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
        try {//System.out.println(fakePending);System.out.println(fakeFriends);System.out.println(fakeMeeting);
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void getMembersMissingParameter() {
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_MEMBERS);
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
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void getMembersDataseError() {
        // set up input
        final int group = 65;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_MEMBERS);
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
            when(groupUserManager.getUsers(group)).thenReturn(null);
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
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void setFounderMissingParameter() {
        // set up input
        final int newFounderID = 7;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SET_FOUNDER);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void setFounderGetUserDatabaseError() {
        // set up input
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
            when(userManager.getUser(newFounderID)).thenReturn(null);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void setFounderDatabaseError() {
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
            when(groupManager.updateFounder(group, newFounder)).thenReturn(false);
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
}
