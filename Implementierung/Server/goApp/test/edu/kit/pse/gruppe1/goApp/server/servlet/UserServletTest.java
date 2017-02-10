package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public class UserServletTest {
    private UserServlet servlet;
    
    @Mock
    private UserManagement mockUsrMang;
    
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
    
    private User createUser(){
        //TODO
        return null;
    }

    @Test
    public void testChangeName(){
        JSONObject json = new JSONObject();
        JSONObject newJson = null;
        User user = createUser();
        
        //when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
        //usrMang.getUser(userID);
        //!usrMang.update(user)
    }

    @Test
    public void testDoGetHttpServletRequestHttpServletResponse() {
        fail("Not yet implemented");
    }

    @Test
    public void testDoPostHttpServletRequestHttpServletResponse() {
        fail("Not yet implemented");
    }

}
