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
import org.junit.Ignore;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eva-Maria
 *
 */

public class LoginServletTest {
    private LoginServlet servlet;
    private JSONObject sendJSONLogin;
    private JSONObject sendJSONRegister;

    @Mock
    private UserManagement mockUsrMang;
    private HttpServletRequest mockHttpRequest;
    private HttpServletResponse mockHttpResponse;
    private PrintWriter mockPrintWriter;
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

        setSendJSON();
    }

    private void setSendJSON() {
        try {
            sendJSONLogin = new JSONObject();
            sendJSONRegister = new JSONObject();
            sendJSONLogin.put(JSONParameter.METHOD.toString(),
                    JSONParameter.Methods.LOGIN.toString());
            sendJSONRegister.put(JSONParameter.METHOD.toString(),
                    JSONParameter.Methods.REGISTER.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        servlet = null;
        sendJSONLogin = null;
        sendJSONRegister = null;
    }

    @Test
    public void testLogin() {
        User realUsr = new User(1234, "Test User");
        realUsr.setUserId(3);
        JSONObject newJson = null;

        when(mockUsrMang.getUser(realUsr.getUserId())).thenReturn(realUsr);

        newJson = loginMethod(realUsr);
        checkUser(newJson, realUsr);
    }

    @Test
    public void testRegister() {
        JSONObject newJson = null;
        User user = newRegisterUser();

        when(mockUsrMang.add(user.getName(), user.getGoogleId())).thenReturn(user);

        newJson = registerMethod(user);
        checkUser(newJson, user);

    }

    @Test
    public void testLoginToRegister() {
        JSONObject newJson = null;
        User user = newLoginUser();

        when(mockUsrMang.getUser(user.getUserId())).thenReturn(null);
        when(mockUsrMang.add(user.getName(), user.getGoogleId())).thenReturn(user);

        newJson = loginMethod(user);
        checkUser(newJson, user);
    }

    private JSONObject loginMethod(User user) {
        JSONObject json = new JSONObject();
        try {
            json.accumulate(JSONParameter.USER_NAME.toString(), user.getName());
            json.accumulate(JSONParameter.USER_ID.toString(), user.getUserId());
            json.accumulate(JSONParameter.GOOGLE_ID.toString(), user.getGoogleId());
        } catch (JSONException e1) {
            e1.printStackTrace();
            fail();
        }
        return method(json, "login");
    }

    private JSONObject registerMethod(User user) {
        JSONObject json = new JSONObject();
        try {
            json.accumulate(JSONParameter.USER_NAME.toString(), user.getName());
            json.accumulate(JSONParameter.GOOGLE_ID.toString(), user.getGoogleId());
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

    private User newLoginUser() {
        User user = newRegisterUser();
        user.setUserId(5);
        return user;
    }

    private User newRegisterUser() {
        User user = new User(1234, "New User");
        return user;
    }

    private void checkUser(JSONObject newJson, User user) {
        if (newJson != null) {
            try {
                assertEquals(ErrorCodes.OK.toString(),
                        newJson.getString(JSONParameter.ERROR_CODE.toString()));
                assertEquals(user.getName(), newJson.getString(JSONParameter.USER_NAME.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
                fail();
            }
        } else {
            fail();
        }
    }

    /**
     * Test method for
     * {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Test
    public void testDoGetWithLogin() {
        JSONObject newJson;
        User user = newLoginUser();
        String jsonStr = "";
        
        when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
        try {
            when(mockHttpResponse.getWriter()).thenReturn(mockPrintWriter);
            when(mockBuffRead.readLine()).thenReturn(jsonStr);
            when(mockHttpRequest.getReader().readLine()).thenReturn(jsonStr);
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
        newJson = new JSONObject(captor);
        checkUser(newJson, user);
    }

    /**
     * Test method for
     * {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Test
    @Ignore
    public void testDoPostWithLogin() {
        fail("Not yet implemented");
    }

}
