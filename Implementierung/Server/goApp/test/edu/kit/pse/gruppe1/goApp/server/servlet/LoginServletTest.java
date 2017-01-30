/**
 * 
 */
package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.database.management.Management;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
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

    private class UserManagement implements Management {
        private User user;

        public UserManagement() {
            super();
        }

        public User add(String name, int googleId) {
            this.user = new User(googleId, name);
            return user;
        }

        public User getUser(int userId) {
            return this.user;
        }
        public boolean update(User chUser) {
            return false;
        }
        public boolean updateName(int userId, String newName) {
            return false;
        }
        
        public boolean updateLocation(int userId, Location newLocation) {
            return false;
        }
        
        public boolean delete(int userId) {
            return false;
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        servlet = new LoginServlet();
        String name = "usrMang";
        String name1 = "test";
        UserManagement nUsrMang = new UserManagement();

        Field field1 = servlet.getClass().getDeclaredField(name1);
        field1.setAccessible(true);
        field1.set(servlet, "Test");
        System.out.println(field1.get(servlet));
        System.out.println(field1.get(servlet));

//        Field field = servlet.getClass().getDeclaredField(name);
//        field.setAccessible(true);
//        field.set(servlet, nUsrMang);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link edu.kit.pse.gruppe1.goApp.server.servlet.LoginServlet#LoginServlet()}.
     */
    @Test
    @Ignore
    public void testLoginServlet() {
        fail("Not yet implemented");
    }

    @Test
    public void Test() {
        String name = "getTest";
        Method method = null;
        JSONObject json = new JSONObject();
        try {
            method = servlet.getClass().getDeclaredMethod(name);
            method.setAccessible(true);
            Object returnValue = method.invoke(servlet);
            System.out.println(returnValue);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

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
