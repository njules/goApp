package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.kit.pse.gruppe1.goApp.server.database.management.RequestManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;

public class RequestSearchServletTest {
    private RequestSearchServlet servlet;

    @Mock
    private RequestManagement mockReqMang;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        servlet = new RequestSearchServlet();

        // Setting MockObjects to private Management Classes in Servlet
        String name = "reqMang";
        Field field = servlet.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(servlet, mockReqMang);
    }

    @Test
    public void testGetRequestsByUser() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        String name = "getRequestsByUser";
        int userID = 1;
        List<Group> groupList = createListGroup();

        when(mockReqMang.getRequestByUser(userID)).thenReturn(groupList);

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        newJson = ServletTestUtils.callMethod(servlet, json, name);

        try {
            assertEquals(newJson.getInt(JSONParameter.ERROR_CODE.toString()),
                    ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

    }

    private List<User> createListUser() {
        List<User> list = new ArrayList<User>();
        list.add(new User("123","Test User"));
        return list;
    }

    private List<Group> createListGroup() {
        List<Group> list = new ArrayList<Group>();
        list.add(new Group("Test Gruppe", new User("123","Test User")));
        return list;
    }

    @Test
    public void testGetRequestsByGroup() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        String name = "getRequestsByGroup";
        int groupID = 1;
        List<User> userList = createListUser();

        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(userList);

        try {
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

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
