package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;

public final class ServletTestUtils {
    
    public static HttpServletRequest simulateRequest(){
        //HttpServletRequest request = new HttpServletRequestWrapper(request);
        return null;
    }
    
    public static HttpServletResponse simulateResponse(){
        return null;
    }
    
    protected static JSONObject getJSONfromString(String str){
       return  null;
    }
    
    /**used from LoginServlet*/
    /**atm for testing what works shared*/
    protected static void checkUser(JSONObject newJson, User user) {
        if (newJson != null) {
            try {
                assertEquals(ErrorCodes.OK.getErrorCode(),
                        newJson.getInt(JSONParameter.ERROR_CODE.toString()));
                assertEquals(user.getName(), newJson.getString(JSONParameter.USER_NAME.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
                fail();
            }
        } else {
            fail();
        }
    }
    
    protected Event createEvent() {
        String name = "Test Event";
        Location loc = new Location(49.014352, 8.404579, "Test Location");
        // Location is somewhere in Karlsruhe
        Timestamp time = new Timestamp(1000);
        Group grp = new Group();
        grp.setGroupId(1);
        User usr = new User();
        usr.setUserId(2);
        Event event = new Event(name, loc, time, grp, usr);
        event.setEventId(3);
        return event;
    }
    
    /**
     * calls private method and returns JSOnObject (which is result)
     * 
     * @param servlet servlet to test
     * @param json json object to put into method
     * @param name mehtod name
     * @return result of method
     */
    protected static JSONObject callMethod(HttpServlet servlet, JSONObject json, String name) {
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

}
