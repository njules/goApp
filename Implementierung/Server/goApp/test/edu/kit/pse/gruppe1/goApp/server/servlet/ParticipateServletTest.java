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
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Status;

public class ParticipateServletTest {
    private ParticipateServlet servlet;
    private String jsonRequest;

    @Mock
    private HttpServletRequest httpRequest;
    @Mock
    private HttpServletResponse httpResponse;
    @Mock
    private BufferedReader request;
    @Mock
    private PrintWriter response;
    @Mock
    private EventUserManagement eventUserManager;
    
    @Captor
    private ArgumentCaptor<String> argCap;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        servlet = new ParticipateServlet();
        Field field = servlet.getClass().getDeclaredField("eventUser");
        field.setAccessible(true);
        field.set(servlet, eventUserManager);
    }

    @After
    public void tearDown() throws Exception {
        servlet = null;
    }

    @Test
    public void testSetStatus() {
        // set up input
        final int event = 5;
        final int user = 7;
        final Status status = Status.PARTICIPATE;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SET_STATUS);
            json.put(JSONParameter.USER_ID.toString(), user);
            json.put(JSONParameter.EVENT_ID.toString(), event);
            json.put(JSONParameter.STATUS.toString(), status.getValue());
            jsonRequest = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to create JSON request!\n");
        }
        // initialize mocking
        try {
            when(httpRequest.getReader()).thenReturn(request);
            when(httpResponse.getWriter()).thenReturn(response);
            when(request.readLine()).thenReturn(jsonRequest);
            when(eventUserManager.updateStatus(event, user, status)).thenReturn(true);
       } catch (IOException e) {
            e.printStackTrace();
            fail("Failed mocking!\n");
        }
        // call method
        try {
            servlet.doPost(httpRequest, httpResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            fail("Failed to post HTTP request!\n");
        }
        // test for correct location list
        verify(response).println(argCap.capture());
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            System.out.println(json.getInt(JSONParameter.ERROR_CODE.toString()));
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
}
