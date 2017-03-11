package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
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

import edu.kit.pse.gruppe1.goApp.server.database.management.GroupManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class GroupSearchServletTest {
    private GroupSearchServlet servlet;
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

    @Captor
    private ArgumentCaptor<String> argCap;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        servlet = new GroupSearchServlet();
        Field field = servlet.getClass().getDeclaredField("groupM");
        field.setAccessible(true);
        field.set(servlet, groupManager);
        field = servlet.getClass().getDeclaredField("groupUM");
        field.setAccessible(true);
        field.set(servlet, groupUserManager);
    }

    @After
    public void tearDown() throws Exception {
        servlet = null;
        jsonRequest = null;
    }

    @Test
    public void testNameSearch() {
        // set up input
        final List<Group> fakeGroups = new ArrayList<Group>();
        fakeGroups.add(new Group("Party Ago", new User("1", null)));
        fakeGroups.add(new Group("Party Hello", new User("3", null)));
        String name = "Party ";
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_GRP_NAME);
            json.put(JSONParameter.GROUP_NAME.toString(), name);
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
            when(groupManager.getGroupsByName(name)).thenReturn(fakeGroups);
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
        // test for correct group list
        verify(response).println(argCap.capture());
        List<Group> returnedGroups = new ArrayList<Group>();
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    JSONParameter.ErrorCodes.OK.getErrorCode());
            JSONArray array = json.getJSONArray(JSONParameter.LIST_GROUP.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonGrp = array.getJSONObject(i);
                returnedGroups.add(
                        new Group(jsonGrp.getString(JSONParameter.GROUP_NAME.toString()), null));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
        for (Group group : returnedGroups) {
            assertTrue(fakeGroups.contains(group));
        }
        assertEquals(fakeGroups.size(), returnedGroups.size());
    }

    @Test
    public void testMemberSearch() {
        // set up input
        final List<Group> fakeGroups = new ArrayList<Group>();
        fakeGroups.add(new Group("Mensa", new User("3", null)));
        fakeGroups.add(new Group("Joggen", new User("3", null)));
        final int member = 3;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_GRP_MEM);
            json.put(JSONParameter.USER_ID.toString(), member);
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
            when(groupUserManager.getGroups(member)).thenReturn(fakeGroups);
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
        // test for correct group list
        verify(response).println(argCap.capture());
        List<Group> returnedGroups = new ArrayList<Group>();
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    JSONParameter.ErrorCodes.OK.getErrorCode());
            JSONArray array = json.getJSONArray(JSONParameter.LIST_GROUP.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonGrp = array.getJSONObject(i);
                returnedGroups.add(
                        new Group(jsonGrp.getString(JSONParameter.GROUP_NAME.toString()), null));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
        for (Group group : returnedGroups) {
            assertTrue(fakeGroups.contains(group));
        }
        assertEquals(fakeGroups.size(), returnedGroups.size());
    }
    
    @Test
    public void missingMemberSearch() {
        // set up input
        final List<Group> fakeGroups = new ArrayList<Group>();
        fakeGroups.add(new Group("Mensa", new User("3", null)));
        fakeGroups.add(new Group("Joggen", new User("3", null)));
        final int member = 3;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_GRP_MEM);
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
            when(groupUserManager.getGroups(member)).thenReturn(fakeGroups);
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
        // test for correct group list
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void groupNonExistentMemberSearch() {
        // set up input
        final List<Group> fakeGroups = new ArrayList<Group>();
        fakeGroups.add(new Group("Mensa", new User("3", null)));
        fakeGroups.add(new Group("Joggen", new User("3", null)));
        final int member = 3;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_GRP_MEM);
            json.put(JSONParameter.USER_ID.toString(), member);
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
            when(groupUserManager.getGroups(member)).thenReturn(null);
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
        // test for correct group list
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void missingNameSearch() {
        // set up input
        final List<Group> fakeGroups = new ArrayList<Group>();
        fakeGroups.add(new Group("Mensa", new User("3", null)));
        fakeGroups.add(new Group("Joggen", new User("3", null)));
        final String name = "party";
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_GRP_NAME);
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
            when(groupManager.getGroupsByName(name)).thenReturn(null);
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
        // test for correct group list
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void groupNonExistentNameSearch() {
        // set up input
        final List<Group> fakeGroups = new ArrayList<Group>();
        fakeGroups.add(new Group("Mensa", new User("3", null)));
        fakeGroups.add(new Group("Joggen", new User("3", null)));
        final String name = "party";
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_GRP_NAME);
            json.put(JSONParameter.GROUP_NAME.toString(), name);
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
            when(groupManager.getGroupsByName(name)).thenReturn(null);
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
        // test for correct group list
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    JSONParameter.ErrorCodes.DB_ERROR.getErrorCode());
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
        // test for correct group list
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()),
                    JSONParameter.ErrorCodes.METH_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
    
    @Test
    public void missingMethod() {
        // prepare input JSON parameter
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
        // test for correct group list
        verify(response).println(argCap.capture());
        try {
            JSONObject response = new JSONObject(argCap.getValue());
            assertEquals(response.getInt(JSONParameter.ERROR_CODE.toString()),
                    JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
}
