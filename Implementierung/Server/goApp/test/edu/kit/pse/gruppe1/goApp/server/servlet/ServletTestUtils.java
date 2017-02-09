package edu.kit.pse.gruppe1.goApp.server.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

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

}
