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

@Deprecated
//TODO: delete bevor Abgabe
public class LoginServletTest {
    private LoginServlet servlet;

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

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        servlet = null;
    }

    @Ignore
    @Test
    public void testLogin() {
        String googleTk = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjExZmU5ZjY4ZDgyOGQ4NTMzNzI4OTg0NDEyYTAxMTZhODI4MTEwZjkifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJpYXQiOjE0ODY4MjY3ODEsImV4cCI6MTQ4NjgzMDM4MSwiYXVkIjoiNDI1NDg5NzEyNjg2LTZqcTFnOWZrMXR0Y3Q5cGduOGFtMGIydWRmcGh0OHU2LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTE3Mzk0Nzc1NDI5Mzc2MDM4NDYxIiwiYXpwIjoiNDI1NDg5NzEyNjg2LWphYm1mbnVkNTNvMzVhOTBpY3V0Zmk2ZzJxbW1pdGZ2LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwibmFtZSI6IkthdGhhcmluYSBSaWVzdGVyZXIiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDYuZ29vZ2xldXNlcmNvbnRlbnQuY29tLy13MUd1UEd3ODFhSS9BQUFBQUFBQUFBSS9BQUFBQUFBQUFBQS9BRFBsaGZLeS1QclpXWmZnTGNTLUhkc1VJMTV2SnRHUGl3L3M5Ni1jL3Bob3RvLmpwZyIsImdpdmVuX25hbWUiOiJLYXRoYXJpbmEiLCJmYW1pbHlfbmFtZSI6IlJpZXN0ZXJlciIsImxvY2FsZSI6ImRlIn0.SCjUI2oTLGFYp_S0Ozq6DwG6VynJ8QUlmrPIy4llrhV544JvFTuh_bB_zxXwRRJnJ4kywY0ES1hvdPqoFnGZrMaAj0e7VQXVTm1J6trAEOunhITB6zP4uKPFEBGLRYtz9b-erHvl7Mdaa7F3r9_aphdfScUx5ts0_jevjLqa_b3WMuMcn9tG1mEarDzh5Rb28OOZP5JPv-YY_uRmxe-l-x6zpzFjqtaw-DBXRexS9n1bfiY9x3HM_6vCE2q4fI6FY6fhWbDHu5yNL4FejS0NBdLRx8YHsThGH4PWxAydM5nWmP4nmS0J160Y_jSnD8kVUgm_r_9ngOOgLvwrPoR6GA";
        User realUsr = new User(googleTk, "Test User");
        realUsr.setUserId(3);
        JSONObject newJson = null;

        JSONObject json = new JSONObject();
        try {
            json.put(JSONParameter.GOOGLE_TOKEN.toString(), googleTk);
        } catch (JSONException e1) {
            e1.printStackTrace();
            fail();
        }
        newJson = method(json, "login");

        when(mockUsrMang.getUser(realUsr.getUserId())).thenReturn(realUsr);

        newJson = loginMethod(realUsr);
        //ServletTestUtils.checkUser(newJson, realUsr);
    }

    @Ignore
    @Test
    public void testRegister() {
        // TODO: an Änderungen anpassen
        JSONObject newJson = null;
        User user = null;// newRegisterUser();

        when(mockUsrMang.add(user.getName(), user.getGoogleId())).thenReturn(user);

        newJson = registerMethod(user);
        //ServletTestUtils.checkUser(newJson, user);

    }

    @Ignore
    @Test
    public void testLoginToRegister() {
        JSONObject newJson = new JSONObject();
        String googleToken = "TOKEN"; // no real token, but there are no tokens from google to test
        User user = new User("12334", "Test User");
        // PowerMockito.spy(ServletUtils.class);
        // ServletUtils spy = spy(ServletUtils.class);

        // TODO: sehen wie das mit static ist
        // Mockito.when(ServletUtils.isUserAlreadyRegistrated(user.getGoogleId())).thenReturn(false);
        // doReturn(false).when(spy).isUserAlreadyRegistrated(user.getGoogleId());
        // doReturn(user.getGoogleId()).when(spy).getGoogleIdByToken(googleToken);
        // doReturn(user.getName()).when(spy).getGoogleNameByToken(googleToken);

        when(mockUsrMang.add(user.getName(), user.getGoogleId())).thenReturn(user);

        try {
            newJson.put(JSONParameter.METHOD.toString(), Methods.LOGIN.toString());
            newJson.put(JSONParameter.GOOGLE_TOKEN.toString(), googleToken);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }

        newJson = loginMethod(user);
       // ServletTestUtils.checkUser(newJson, user);
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

    /**
     * Test method for
     * {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Test
    public void testDoGetWithLogin() {
        String googleTk = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjExZmU5ZjY4ZDgyOGQ4NTMzNzI4OTg0NDEyYTAxMTZhODI4MTEwZjkifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJpYXQiOjE0ODY4MjY3ODEsImV4cCI6MTQ4NjgzMDM4MSwiYXVkIjoiNDI1NDg5NzEyNjg2LTZqcTFnOWZrMXR0Y3Q5cGduOGFtMGIydWRmcGh0OHU2LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTE3Mzk0Nzc1NDI5Mzc2MDM4NDYxIiwiYXpwIjoiNDI1NDg5NzEyNjg2LWphYm1mbnVkNTNvMzVhOTBpY3V0Zmk2ZzJxbW1pdGZ2LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwibmFtZSI6IkthdGhhcmluYSBSaWVzdGVyZXIiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDYuZ29vZ2xldXNlcmNvbnRlbnQuY29tLy13MUd1UEd3ODFhSS9BQUFBQUFBQUFBSS9BQUFBQUFBQUFBQS9BRFBsaGZLeS1QclpXWmZnTGNTLUhkc1VJMTV2SnRHUGl3L3M5Ni1jL3Bob3RvLmpwZyIsImdpdmVuX25hbWUiOiJLYXRoYXJpbmEiLCJmYW1pbHlfbmFtZSI6IlJpZXN0ZXJlciIsImxvY2FsZSI6ImRlIn0.SCjUI2oTLGFYp_S0Ozq6DwG6VynJ8QUlmrPIy4llrhV544JvFTuh_bB_zxXwRRJnJ4kywY0ES1hvdPqoFnGZrMaAj0e7VQXVTm1J6trAEOunhITB6zP4uKPFEBGLRYtz9b-erHvl7Mdaa7F3r9_aphdfScUx5ts0_jevjLqa_b3WMuMcn9tG1mEarDzh5Rb28OOZP5JPv-YY_uRmxe-l-x6zpzFjqtaw-DBXRexS9n1bfiY9x3HM_6vCE2q4fI6FY6fhWbDHu5yNL4FejS0NBdLRx8YHsThGH4PWxAydM5nWmP4nmS0J160Y_jSnD8kVUgm_r_9ngOOgLvwrPoR6GA";
        User user = new User(googleTk, "Test User");
        user.setUserId(3);
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        String jsonStr = null;
        try {
            json.put(JSONParameter.GOOGLE_TOKEN.toString(), googleTk);
            json.put(JSONParameter.METHOD.toString(), Methods.LOGIN.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
            fail();
        }

        when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
        try {
            when(mockHttpResponse.getWriter()).thenReturn(mockPrintWriter);
            when(mockBuffRead.readLine()).thenReturn(json.toString());
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
        //ServletTestUtils.checkUser(newJson, user);
    }

    /**
     * Test method for
     * {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Ignore
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
       // ServletTestUtils.checkUser(newJson, user);
    }

    /**
     * Test method for
     * {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Ignore
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
        //ServletTestUtils.checkUser(newJson, user);
    }

}
