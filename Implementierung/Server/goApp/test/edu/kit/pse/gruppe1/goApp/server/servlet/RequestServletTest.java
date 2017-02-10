package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.kit.pse.gruppe1.goApp.server.database.management.GroupUserManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.RequestManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Request;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;

public class RequestServletTest {

    private RequestServlet servlet;

    @Mock
    private RequestManagement mockReqMang;
    @Mock
    private GroupUserManagement mockGrUsrMang;

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
        String name = "create";
        int userID = 1;
        int groupID = 2;
        List<Group> groups = new ArrayList<Group>(5);
        List<User> users = new ArrayList<User>(5);
        List<Group> reqGroups = new ArrayList<Group>(1);
        List<User> reqUsers = new ArrayList<User>(1);

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockGrUsrMang.getGroups(userID)).thenReturn(groups);
        when(mockGrUsrMang.getUsers(groupID)).thenReturn(users);
        when(mockReqMang.getRequestByUser(userID)).thenReturn(reqGroups);
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(reqUsers);
        when(mockReqMang.add(groupID, userID)).thenReturn(true);

        newJson = ServletTestUtils.callMethod(servlet, json, name);

        try {
            assertEquals(newJson.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testAccept() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        String name = "accept";
        int userID = 1;
        int groupID = 2;
        Request req = new Request();
        
        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        when(mockReqMang.getRequest(groupID,userID)).thenReturn(req);
        when(mockReqMang.delete(groupID,userID)).thenReturn(true);
        when(mockGrUsrMang.add(groupID,userID)).thenReturn(true);

        newJson = ServletTestUtils.callMethod(servlet, json, name);
        try {
            assertEquals(newJson.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testReject() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        String name = "reject";
        int userID = 1;
        int groupID = 2;
        Request req = new Request();
        
        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        
        when(mockReqMang.getRequest(groupID,userID)).thenReturn(req);
        when(mockReqMang.delete(groupID,userID)).thenReturn(true);

        newJson = ServletTestUtils.callMethod(servlet, json, name);
        try {
            assertEquals(newJson.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }
}
