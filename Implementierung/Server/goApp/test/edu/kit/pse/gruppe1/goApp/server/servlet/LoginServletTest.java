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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.User;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Eva-Maria
 *
 */
public class LoginServletTest {
    private LoginServlet servlet;

    @Mock
    UserManagement mockUsrMang;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        servlet = new LoginServlet();
        // String name1 = "test";
        // Field field1 = servlet.getClass().getDeclaredField(name1);
        // field1.setAccessible(true);
        // System.out.println(field1.get(servlet));
        // field1.set(servlet, "Test");
        // System.out.println(field1.get(servlet));

        // UserManagement nUsrMang = new UserManagement();
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
    }

    @Test
    public void testLogin() {
        String name = "login";
        User realUsr = new User(1234, "Test User");
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        Method method;

        // prepare Test
        when(mockUsrMang.getUser(realUsr.getGoogleId())).thenReturn(realUsr);
        try {
            json.accumulate(JSONParameter.UserName.toString(), realUsr.getName());
            json.accumulate(JSONParameter.UserID.toString(), realUsr.getGoogleId());
        } catch (JSONException e1) {
            e1.printStackTrace();
            fail();
        }

        // Call Method
        try {
            method = servlet.getClass().getDeclaredMethod(name, JSONObject.class);
            method.setAccessible(true);
            Object returnValue = method.invoke(servlet, json);
            // assert Object returnValue ist JSONObject
            newJson = (JSONObject) returnValue;

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
        if (newJson != null) {
            try {
                assertEquals(realUsr.getName(),
                        newJson.getString(JSONParameter.UserName.toString()));

            } catch (JSONException e) {
                e.printStackTrace();
                fail();
            }
        } else {
            fail();
        }

    }

    @Ignore
    @Test
    public void testLoginWithNonUsr() {
        User nullUsr = new User(1, "Null User");
        when(mockUsrMang.getUser(nullUsr.getGoogleId())).thenReturn(null);
    }

    // @Test
    // public void test() {
    // String name = "getTest";
    // Method method = null;
    // JSONObject json = new JSONObject();
    //
    // try {
    // method = servlet.getClass().getDeclaredMethod(name);
    // method.setAccessible(true);
    // Object returnValue = method.invoke(servlet);
    // System.out.println(returnValue);
    // assertEquals(returnValue.toString(), "Test");
    // } catch (NoSuchMethodException | SecurityException | IllegalAccessException
    // | IllegalArgumentException | InvocationTargetException e) {
    // e.printStackTrace();
    // fail();
    // }
    // }

    /**
     * Test method for
     * {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Test
    @Ignore
    public void testDoGetHttpServletRequestHttpServletResponse() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Test
    @Ignore
    public void testDoPostHttpServletRequestHttpServletResponse() {
        fail("Not yet implemented");
    }

}
