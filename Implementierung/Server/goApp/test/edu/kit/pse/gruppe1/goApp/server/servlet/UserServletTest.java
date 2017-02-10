package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.json.JSONException;
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
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;

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
        String name = "changeName";

        when(mockUsrMang.getUser(user.getUserId())).thenReturn(user);
        when(mockUsrMang.update(user)).thenReturn(true);

        try {
            json.put(JSONParameter.USER_ID.toString(), user.getUserId());
            json.put(JSONParameter.USER_NAME.toString(), user.getName());
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
