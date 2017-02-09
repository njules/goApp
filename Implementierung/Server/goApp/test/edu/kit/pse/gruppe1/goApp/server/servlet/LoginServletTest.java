/**
 * 
 */
package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServletTest {
    private LoginServlet servlet;
    // private JSONObject sendJSONLogin;
    // private JSONObject sendJSONRegister;

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

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        servlet = new LoginServlet();

        String name = "usrMang";
        Field field = servlet.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(servlet, mockUsrMang);

        // setSendJSON();
    }

    // private void setSendJSON() {
    // try {
    // sendJSONLogin = new JSONObject();
    // sendJSONRegister = new JSONObject();
    // sendJSONLogin.put(JSONParameter.METHOD.toString(),
    // JSONParameter.Methods.LOGIN.toString());
    // sendJSONRegister.put(JSONParameter.METHOD.toString(),
    // JSONParameter.Methods.REGISTER.toString());
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        servlet = null;
    }

    @Test
    public void testLogin() {
        // TODO: an Änderungen anpassen
        User realUsr = new User("1234", "Test User");
        realUsr.setUserId(3);
        JSONObject newJson = null;

        when(mockUsrMang.getUser(realUsr.getUserId())).thenReturn(realUsr);

        newJson = loginMethod(realUsr);
        ServletTestUtils.checkUser(newJson, realUsr);
    }

    @Test
    public void testRegister() {
        // TODO: an Änderungen anpassen
        JSONObject newJson = null;
        User user = null;// newRegisterUser();

        when(mockUsrMang.add(user.getName(), user.getGoogleId())).thenReturn(user);

        newJson = registerMethod(user);
        ServletTestUtils.checkUser(newJson, user);

    }

    @Test
    public void testLoginToRegister() {
        JSONObject newJson = new JSONObject();
        String googleToken = "TOKEN"; // no real token, but there are no tokens from google to test
        User user = new User("12334", "Test User");
        ServletUtils servletUtils = spy(ServletUtils.class);

        // TODO: sehen wie das mit static ist
        doReturn(false).when(servletUtils).isUserAlreadyRegistrated(user.getGoogleId());
        doReturn(user.getGoogleId()).when(servletUtils).getGoogleIdByToken(googleToken);
        doReturn(user.getName()).when(servletUtils).getGoogleNameByToken(googleToken);

        when(mockUsrMang.add(user.getName(), user.getGoogleId())).thenReturn(user);

        try {
            newJson.put(JSONParameter.METHOD.toString(), Methods.LOGIN.toString());
            newJson.put(JSONParameter.GOOGLE_TOKEN.toString(), googleToken);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        newJson = loginMethod(user);
        ServletTestUtils.checkUser(newJson, user);
    }

    private JSONObject loginMethod(User user) {
        // TODO: an Änderungen anpassen
        JSONObject json = new JSONObject();
        try {
            json.accumulate(JSONParameter.USER_NAME.toString(), user.getName());
            json.accumulate(JSONParameter.USER_ID.toString(), user.getUserId());
            // json.accumulate(JSONParameter.GOOGLE_TOKEN.toString(), user.getGoogleId());
        } catch (JSONException e1) {
            e1.printStackTrace();
            fail();
        }
        return method(json, "login");
    }

    private JSONObject registerMethod(User user) {
        // TODO: an Änderungen anpassen
        JSONObject json = new JSONObject();
        try {
            json.accumulate(JSONParameter.USER_NAME.toString(), user.getName());
            // json.accumulate(JSONParameter.GOOGLE_ID.toString(), user.getGoogleId());
        } catch (JSONException e1) {
            e1.printStackTrace();
            fail();
        }
        return method(json, "register");
    }

    /**
     * calls loginMethod with Mocked User Object mock methods before use
     * 
     * @param realUsr
     *            User for JSON String
     * 
     * @param name
     *            could be register or login
     * @return
     */
    private JSONObject method(JSONObject json, String name) {
        Method method;
        JSONObject newJson = null;

        // Call Method
        try {
            method = servlet.getClass().getDeclaredMethod(name, JSONObject.class);
            method.setAccessible(true);
            Object returnValue = method.invoke(servlet, json);

            // assert Object returnValue is JSONObject
            newJson = (JSONObject) returnValue;

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
        return newJson;
    }

    // private JSONObject appendUserToJson(User user, JSONObject json) {
    // // TODO: an Änderungen anpassen
    // try {
    // json.accumulate(JSONParameter.USER_ID.toString(), user.getUserId());
    // json.accumulate(JSONParameter.USER_NAME.toString(), user.getName());
    // // json.accumulate(JSONParameter.GOOGLE_ID.toString(), user.getGoogleId());
    // json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.toString());
    // } catch (JSONException e) {
    // e.printStackTrace();
    // fail();
    // }
    //
    // return json;
    //
    // }

    /**
     * Test method for
     * {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Test
    public void testDoGetWithLogin() {
        // TODO: an Änderungen anpassen
        JSONObject newJson = null;
        User user = null; // newLoginUser();
        String jsonStr = null;

        when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
        try {
            when(mockHttpResponse.getWriter()).thenReturn(mockPrintWriter);
            when(mockBuffRead.readLine()).thenReturn(jsonStr);
            when(mockHttpRequest.getReader()).thenReturn(mockBuffRead);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        try {
            servlet.doGet(mockHttpRequest, mockHttpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail();
        }

        verify(mockPrintWriter).println(captor.capture());
        try {
            newJson = new JSONObject(captor.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        ServletTestUtils.checkUser(newJson, user);
    }

    /**
     * Test method for
     * {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Test
    public void testDoPostWithLogin() {
        // TODO: an Änderungen anpassen
        JSONObject newJson = null;
        User user = null; // newLoginUser();
        // TODO: String jsonStr = appendUserToJson(user, sendJSONLogin).toString();
        String jsonStr = null;

        when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
        try {
            when(mockHttpResponse.getWriter()).thenReturn(mockPrintWriter);
            when(mockBuffRead.readLine()).thenReturn(jsonStr);
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
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        ServletTestUtils.checkUser(newJson, user);
    }

    /**
     * Test method for
     * {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Test
    public void testDoGetWithRegister() {
        // TODO: an Änderungen anpassen
        JSONObject newJson = null;
        User user = null; // newRegisterUser();
        String jsonStr = null;// appendUserToJson(user, sendJSONRegister).toString();

        when(mockUsrMang.add(user.getName(), user.getGoogleId())).thenReturn(user);
        when(mockUsrMang.getUser(anyInt())).thenReturn(null);
        try {
            when(mockHttpResponse.getWriter()).thenReturn(mockPrintWriter);
            when(mockBuffRead.readLine()).thenReturn(jsonStr);
            when(mockHttpRequest.getReader()).thenReturn(mockBuffRead);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        try {
            servlet.doGet(mockHttpRequest, mockHttpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail();
        }

        verify(mockPrintWriter).println(captor.capture());
        try {
            newJson = new JSONObject(captor.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        ServletTestUtils.checkUser(newJson, user);
    }

}
