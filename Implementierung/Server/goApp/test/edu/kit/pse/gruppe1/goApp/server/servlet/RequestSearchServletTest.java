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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.kit.pse.gruppe1.goApp.server.database.management.RequestManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

public class RequestSearchServletTest {
    private RequestSearchServlet servlet;

    @Mock
    private RequestManagement mockReqMang;

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
        int userID = 1;
        List<Group> groupList = createListGroup();

        try {
            json.put(JSONParameter.USER_ID.toString(), userID);
            json.put(JSONParameter.METHOD.toString(), Methods.GET_REQ_USR);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        
        when(mockReqMang.getRequestByUser(userID)).thenReturn(groupList);
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
        int groupID = 1;
        List<User> userList = createListUser();

        try {
            json.put(JSONParameter.GROUP_ID.toString(), groupID);
            json.put(JSONParameter.METHOD.toString(), Methods.GET_REQ_GRP);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        
        when(mockReqMang.getRequestByGroup(groupID)).thenReturn(userList);
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

}
