package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import edu.kit.pse.gruppe1.goApp.server.database.management.GroupUserManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.RequestManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Request;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

public class RequestServletTest {

    private RequestServlet servlet;

    @Mock
    private RequestManagement mockReqMang;
    @Mock
    private GroupUserManagement mockGrUsrMang;
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

        servlet = new RequestServlet();

        // Setting MockObjects to private Management Classes in Servlet
        String name = "grUsrMang";
        Field field = servlet.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(servlet, mockGrUsrMang);

        name = "reqMang";
        field = servlet.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(servlet, mockReqMang);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreate() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>(5);
        List<User> users = new ArrayList<User>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);
        List<User> reqUsers = new ArrayList<User>(1);

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);
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
    public void testCreateUserIsMember() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>(5);
        List<User> users = new ArrayList<User>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);
        List<User> reqUsers = new ArrayList<User>(1);

        // add User to Group
        Group grp = new Group();
        grp.setGroupId(2);
        groups.add(grp);

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);
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
                    ErrorCodes.INTERACT_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testCreateUserHasRequest() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>(5);
        List<User> users = new ArrayList<User>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);
        List<User> reqUsers = new ArrayList<User>(1);

        // add User to Group
        Group grp = new Group();
        grp.setGroupId(2);
        reqGroups.add(grp);

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);
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
                    ErrorCodes.INTERACT_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testCreateUserLimitReached() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>();
        List<User> users = new ArrayList<User>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);
        List<User> reqUsers = new ArrayList<User>(1);

        // fill Groups
        Group grp = new Group();
        grp.setGroupId(3);
        for (int i = 0; i < ServletUtils.USERLIMIT; i++) {
            groups.add(grp);
        }

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);
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
                    ErrorCodes.USR_LIMIT.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCreateGroupLimitReached() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>();
        List<User> users = new ArrayList<User>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);
        List<User> reqUsers = new ArrayList<User>(1);

        // fill Groups
        User usr = new User();
        usr.setUserId(3);
        for (int i = 0; i < ServletUtils.GROUPLIMIT; i++) {
            users.add(usr);
        }

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);
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
                    ErrorCodes.GRP_LIMIT.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCreateDatabaseAddFalse() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>(5);
        List<User> users = new ArrayList<User>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);
        List<User> reqUsers = new ArrayList<User>(1);

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(false);
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
                    ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testCreateDatabaseReqByGroupNull() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>(5);
        List<User> users = new ArrayList<User>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(null);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);
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
    public void testCreateDatabaseReqByUserNull() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>(5);
        List<User> users = new ArrayList<User>(5);
        List<User> reqUsers = new ArrayList<User>(1);

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(null);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);
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
    public void testCreateDatabaseGetUserNull() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);
        List<User> reqUsers = new ArrayList<User>(1);

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(null);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);
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
                    ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testCreateDatabaseGetGroupsNull() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<User> users = new ArrayList<User>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);
        List<User> reqUsers = new ArrayList<User>(1);

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(null);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);
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
    public void testCreateWOutJSON() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>(5);
        List<User> users = new ArrayList<User>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);
        List<User> reqUsers = new ArrayList<User>(1);

        try {
            json.put(JSONParameter.METHOD.toString(), Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);
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
                    ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testAccept() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        Request req = new Request();

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.ACCEPT);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        when(mockReqMang.getRequest(groupID, userID)).thenReturn(req);
        when(mockReqMang.delete(groupID, userID)).thenReturn(true);
        when(mockGrUsrMang.add(groupID, userID)).thenReturn(true);

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
    public void testAcceptDatabaseAddFalse() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        Request req = new Request();

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.ACCEPT);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        when(mockReqMang.getRequest(groupID, userID)).thenReturn(req);
        when(mockReqMang.delete(groupID, userID)).thenReturn(true);
        when(mockGrUsrMang.add(groupID, userID)).thenReturn(false);

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
                    ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAcceptDatabaseDeleteFalse() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        Request req = new Request();

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.ACCEPT);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        when(mockReqMang.getRequest(groupID, userID)).thenReturn(req);
        when(mockReqMang.delete(groupID, userID)).thenReturn(false);
        when(mockGrUsrMang.add(groupID, userID)).thenReturn(true);

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
                    ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAcceptDatabaseGetRequestNull() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.ACCEPT);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        when(mockReqMang.getRequest(groupID, userID)).thenReturn(null);
        when(mockReqMang.delete(groupID, userID)).thenReturn(true);
        when(mockGrUsrMang.add(groupID, userID)).thenReturn(true);

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
                    ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAcceptWOutJSON() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        Request req = new Request();

        try {
            json.put(JSONParameter.METHOD.toString(), Methods.ACCEPT);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        when(mockReqMang.getRequest(groupID, userID)).thenReturn(req);
        when(mockReqMang.delete(groupID, userID)).thenReturn(true);
        when(mockGrUsrMang.add(groupID, userID)).thenReturn(true);

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
                    ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testReject() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        Request req = new Request();

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.REJECT);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockReqMang.getRequest(groupID, userID)).thenReturn(req);
        when(mockReqMang.delete(groupID, userID)).thenReturn(true);
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
    public void testRejectDatabaseDeleteFalse() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        Request req = new Request();

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.REJECT);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockReqMang.getRequest(groupID, userID)).thenReturn(req);
        when(mockReqMang.delete(groupID, userID)).thenReturn(false);
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
                    ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testRejectDatabaseRequestNull() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.REJECT);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockReqMang.getRequest(groupID, userID)).thenReturn(null);
        when(mockReqMang.delete(groupID, userID)).thenReturn(true);
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
                    ErrorCodes.DB_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testRejectWOutJSON() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        Request req = new Request();

        try {
            json.put(JSONParameter.METHOD.toString(), Methods.REJECT);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockReqMang.getRequest(groupID, userID)).thenReturn(req);
        when(mockReqMang.delete(groupID, userID)).thenReturn(true);
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
                    ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMethodNotExisting() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        Request req = new Request();

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.DEL_MEM);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        when(mockReqMang.getRequest(groupID, userID)).thenReturn(req);
        when(mockReqMang.delete(groupID, userID)).thenReturn(true);
        when(mockGrUsrMang.add(groupID, userID)).thenReturn(true);

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
                    ErrorCodes.METH_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testEmptyJSON() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        int userID = 1;
        int groupID = 2;
        Request req = new Request();

        when(mockReqMang.getRequest(groupID, userID)).thenReturn(req);
        when(mockReqMang.delete(groupID, userID)).thenReturn(true);
        when(mockGrUsrMang.add(groupID, userID)).thenReturn(true);

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
                    ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

}
