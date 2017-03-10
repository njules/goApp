package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

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

import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

public class UserServletTest {
    private UserServlet servlet;

    @Mock
    private UserManagement mockUsrMang;

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

        servlet = new UserServlet();

        // Setting MockObjects to private Management Classes in Servlet
        String name = "usrMang";
        Field field = servlet.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(servlet, mockUsrMang);

    }

    @After
    public void tearDown() throws Exception {
        servlet = null;
    }

    private User createUser() {
        User user = new User();
        user.setUserId(123);
        user.setName("Test User");
        return user;
    }

    @Test
    public void testChangeName() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        User user = createUser();

        try {
            json.put(JSONParameter.USER_ID.toString(), user.getUserId());
            json.put(JSONParameter.USER_NAME.toString(), user.getName());
            json.put(JSONParameter.METHOD.toString(), Methods.CHANGE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
        when(mockUsrMang.update(user)).thenReturn(true);
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
    public void testMethodNotExisting() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        User user = createUser();

        try {
            // there is no method accept in UserServlet
            json.put(JSONParameter.METHOD.toString(), Methods.ACCEPT);
            json.put(JSONParameter.USER_ID.toString(), user.getUserId());
            json.put(JSONParameter.USER_NAME.toString(), user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        
        when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
        when(mockUsrMang.update(user)).thenReturn(true);
        
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
            User user = createUser();
            
            when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
            when(mockUsrMang.update(user)).thenReturn(true);
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
    public void testChangeNameDatabseNullGetUser() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        User user = createUser();

        try {
            json.put(JSONParameter.USER_ID.toString(), user.getUserId());
            json.put(JSONParameter.USER_NAME.toString(), user.getName());
            json.put(JSONParameter.METHOD.toString(), Methods.CHANGE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockUsrMang.getUser(user.getUserId())).thenReturn(null);
        when(mockUsrMang.update(user)).thenReturn(true);
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
    public void testChangeNameDatabseFalseUpdate() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        User user = createUser();

        try {
            json.put(JSONParameter.USER_ID.toString(), user.getUserId());
            json.put(JSONParameter.USER_NAME.toString(), user.getName());
            json.put(JSONParameter.METHOD.toString(), Methods.CHANGE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
        when(mockUsrMang.update(user)).thenReturn(false);
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
    public void testChangeNameWOutJSON() {
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        User user = createUser();

        try {
            json.put(JSONParameter.METHOD.toString(), Methods.CHANGE);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
        when(mockUsrMang.update(user)).thenReturn(true);
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
