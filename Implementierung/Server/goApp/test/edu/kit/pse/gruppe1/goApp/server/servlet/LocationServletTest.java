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

import org.json.JSONArray;
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

import edu.kit.pse.gruppe1.goApp.server.algorithm.ClusterFacade;
import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Location;

public class LocationServletTest {
    private LocationServlet servlet;
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
    private UserManagement userManager;
    @Mock
    private EventManagement eventManager;
    @Mock
    private Event fakeEvent;
    @Mock
    private ClusterFacade clusterer;
    
    @Captor
    private ArgumentCaptor<String> argCap;
    
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        servlet = new LocationServlet();
        Field field = servlet.getClass().getDeclaredField("event");
        field.setAccessible(true);
        field.set(servlet, eventManager);
        field = servlet.getClass().getDeclaredField("eventUser");
        field.setAccessible(true);
        field.set(servlet, userManager);
        field = servlet.getClass().getDeclaredField("clusterer");
        field.setAccessible(true);
        field.set(servlet, clusterer);
    }

    @After
    public void tearDown() throws Exception {
        servlet = null;
        jsonRequest = null;
    }

    @Test
    public void testSyncPos() {
        // set up input
        fakeEvent = new Event();
        final List<Location> fakeLocations = new ArrayList<Location>();
        fakeLocations.add(new Location(5, 2, null));
        fakeLocations.add(new Location(5, 3, null));
        final int user = 5;
        final double lat = 3;
        final double lon = 3;
        final int evt = 2;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SYNC_LOC);
            json.put(JSONParameter.USER_ID.toString(), user);
            json.put(JSONParameter.LATITUDE.toString(), lat);
            json.put(JSONParameter.LONGITUDE.toString(), lon);
            json.put(JSONParameter.EVENT_ID.toString(), evt);
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
            when(userManager.updateLocation(user, new Location(lon, lat, null))).thenReturn(true);
            when(eventManager.getEvent(evt)).thenReturn(fakeEvent);
            when(clusterer.getLocationsByMultiDBSCAN(fakeEvent)).thenReturn(fakeLocations);
       } catch (IOException | NullPointerException e) {
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
        List<Location> cluster = new ArrayList<Location>();
        try {
            JSONObject json = new JSONObject(argCap.getValue());
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.OK.getErrorCode());
            JSONArray array = json.getJSONArray(JSONParameter.LIST_LOC.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonLoc = array.getJSONObject(i);
                cluster.add(new Location(jsonLoc.getDouble(JSONParameter.LONGITUDE.toString()),
                        jsonLoc.getDouble(JSONParameter.LATITUDE.toString()), null));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
        for (Location loc : cluster) {
            assertTrue(fakeLocations.contains(loc));
            fakeLocations.remove(loc);
        }
        assertTrue(fakeLocations.isEmpty());
    }

    @Test
    public void missingMethod() {
        // set up input
        final int user = 5;
        final double lat = 3;
        final double lon = 3;
        final int evt = 2;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.USER_ID.toString(), user);
            json.put(JSONParameter.LATITUDE.toString(), lat);
            json.put(JSONParameter.LONGITUDE.toString(), lon);
            json.put(JSONParameter.EVENT_ID.toString(), evt);
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
       } catch (IOException | NullPointerException e) {
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    @Test
    public void invalidMethod() {
        // set up input
        final int user = 5;
        final double lat = 3;
        final double lon = 3;
        final int evt = 2;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.DEL_MEM);
            json.put(JSONParameter.USER_ID.toString(), user);
            json.put(JSONParameter.LATITUDE.toString(), lat);
            json.put(JSONParameter.LONGITUDE.toString(), lon);
            json.put(JSONParameter.EVENT_ID.toString(), evt);
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
       } catch (IOException | NullPointerException e) {
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.METH_ERROR.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }

    @Test
    public void missingParameter() {
        // set up input
        final int user = 5;
        final int evt = 2;
        // prepare input JSON parameter
        try {
            JSONObject json = new JSONObject();
            json.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SYNC_LOC);
            json.put(JSONParameter.USER_ID.toString(), user);
            json.put(JSONParameter.EVENT_ID.toString(), evt);
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
       } catch (IOException | NullPointerException e) {
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
            assertEquals(json.getInt(JSONParameter.ERROR_CODE.toString()), JSONParameter.ErrorCodes.READ_JSON.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Failed to read JSON response!\n");
        }
    }
}
