package edu.kit.pse.gruppe1.goApp.server.servlet;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.Participant;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;

/**
 * Utils for all Servlets
 *
 * contains methods for creating JSON Strings out of classes and to reduce redundant code
 */
public final class ServletUtils {

    protected static final int USERLIMIT = 20;
    protected static final int GROUPLIMIT = 50;

    /**
     * private constructor to make class static
     */
    private ServletUtils() {
    }

    /**
     * TODO: checks if GoogleTokenisValid atm always true, and parameters will change in the future
     * 
     * @return
     */
    
    protected static boolean isUserAlreadyRegistrated(String googleId) {
        
        UserManagement management = new UserManagement();
        
        User user = management.getUserByGoogleId(googleId);
        
        if(user == null) {
            return false;
        } else {
            return true;
        }
        
        
    }
    protected static String getGoogleIdByToken(String idTokenString) {

        String CLIENT_ID = "425489712686-6jq1g9fk1ttct9pgn8am0b2udfpht8u6.apps.googleusercontent.com";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new ApacheHttpTransport(), new JacksonFactory())
                        .setAudience(Collections.singletonList(CLIENT_ID)).build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }

        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String googleId = payload.getSubject();
            return googleId;

        } else {
            return null;
        }
    }

    protected static String getGoogleNameByToken(String idTokenString) {

        String CLIENT_ID = "425489712686-6jq1g9fk1ttct9pgn8am0b2udfpht8u6.apps.googleusercontent.com";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new ApacheHttpTransport(), new JacksonFactory())
                        .setAudience(Collections.singletonList(CLIENT_ID)).build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }

        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String Name = (String) payload.get("name");
            return Name;

        } else {
            return null;
        }
    }

    protected static JSONObject createJSONParticipate(Participant part) {
        JSONObject json = new JSONObject();
        try {
            json.put(JSONParameter.USER_ID.toString(), part.getUser());
            json.put(JSONParameter.STATUS.toString(), part.getStatus());
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONListPart(List<Participant> part) {
        JSONObject json = new JSONObject();
        try {
            for (Participant p : part) {
                json.append(JSONParameter.LIST_PART.toString(), createJSONParticipate(p));
            }
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;

    }

    protected static JSONObject createJSONEventID(Event event) {
        JSONObject json = new JSONObject();
        try {
            json.put(JSONParameter.EVENT_ID.toString(), event.getEventId());
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONEvent(Event event) {
        JSONObject json = new JSONObject();
        try {
            json.put(JSONParameter.EVENT_NAME.toString(), event.getName());
            json.put(JSONParameter.EVENT_TIME.toString(), event.getTimestamp().getTime());
            json.put(JSONParameter.EVENT_ID.toString(), event.getEventId());

            json.put(JSONParameter.LOC_NAME.toString(), event.getLocation().getName());
            json.put(JSONParameter.LONGITUDE.toString(), event.getLocation().getLongitude());
            json.put(JSONParameter.LATITUDE.toString(), event.getLocation().getLatitude());

            json.put(JSONParameter.GROUP_ID.toString(), event.getGroup().getGroupId());
            json.put(JSONParameter.USER_ID.toString(), event.getCreator().getUserId());
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONLocation(Location location) {
        JSONObject json = new JSONObject();

        try {
            json.put(JSONParameter.LOC_NAME.toString(), location.getName());
            json.put(JSONParameter.LONGITUDE.toString(), location.getLongitude());
            json.put(JSONParameter.LATITUDE.toString(), location.getLatitude());
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONGroup(Group group) {
        JSONObject json = new JSONObject();

        try {
            json.put(JSONParameter.USER_ID.toString(), group.getFounder().getUserId());
            json.put(JSONParameter.USER_NAME.toString(), group.getFounder().getName());
            json.put(JSONParameter.GROUP_NAME.toString(), group.getName());
            json.put(JSONParameter.GROUP_ID.toString(), group.getGroupId());

            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONUser(User user) {
        JSONObject json = new JSONObject();
        try {
            json.put(JSONParameter.USER_ID.toString(), user.getUserId());
            json.put(JSONParameter.USER_NAME.toString(), user.getName());
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONListEvent(List<Event> event) {
        JSONObject json = new JSONObject();
        try {
            for (Event evt : event) {
                json.append(JSONParameter.LIST_EVENT.toString(), createJSONEvent(evt));
            }
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONListUsr(List<User> user) {
        JSONObject json = new JSONObject();
        try {
            for (User usr : user) {
                json.append(JSONParameter.LIST_USER.toString(), createJSONUser(usr));
            }
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONListGrp(List<Group> group) {
        JSONObject json = new JSONObject();
        try {
            for (Group grp : group) {
                json.append(JSONParameter.LIST_GROUP.toString(), createJSONGroup(grp));
            }
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONGroupID(Group grp) {
        JSONObject json = new JSONObject();

        try {
            json.put(JSONParameter.GROUP_ID.toString(), grp.getGroupId());
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONListLoc(List<Location> locat) {
        JSONObject json = new JSONObject();
        try {
            for (Location loc : locat) {
                json.append(JSONParameter.LIST_LOC.toString(), createJSONLocation(loc));
            }
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    protected static JSONObject createJSONError(JSONParameter.ErrorCodes error) {
        JSONObject res = new JSONObject();
        try {
            res.put(JSONParameter.ERROR_CODE.toString(), error.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    // maybe not needed anymore
    // /**
    // * extract Method from request
    // *
    // * @param request
    // * Incoming Request
    // * @param jsonRequest
    // * jsonRequest (to set here)
    // * @return Method (Enum Value)
    // * @throws ServletException
    // * if Servlet Exception happens
    // * @throws JSONException
    // * if JSONException happens - either with Message if JSON was empty or without (Read
    // * Error)
    // * @throws IOException
    // * if problems with IO operation happened
    // */
    // public static Methods getMethod(HttpServletRequest request, JSONObject jsonRequest)
    // throws ServletException, JSONException, IOException {
    //
    //
    // String jsonString = null;
    // JSONParameter.Methods method = Methods.NONE;
    // jsonString = request.getReader().readLine();
    //
    // if (jsonString == null) {
    // throw new JSONException(ErrorCodes.EMPTY_JSON.toString());
    // }
    // jsonRequest = new JSONObject(jsonString);
    // method = JSONParameter.Methods
    // .fromString(jsonRequest.getString(JSONParameter.Method.toString()));
    //
    // // do this to prevent null-pointer exception in switch-case in every Servlet
    // if (method == null) {
    // method = Methods.NONE;
    // }
    // return method;
    // }

    /**
     * extracts JSONObject from HTTP Request
     * 
     * @param request
     *            Request to Servlet
     * @param response
     *            Response to sent
     * @return JSONObject if reading was successful, otherwise null and then sets also response
     */
    protected static JSONObject extractJSON(HttpServletRequest request,
            HttpServletResponse response) {
        String jsonString = null;
        ErrorCodes error = ErrorCodes.OK;
        try {
            jsonString = request.getReader().readLine();
        } catch (IOException e) {
            e.printStackTrace();
            error = JSONParameter.ErrorCodes.IO_ERROR;
        }

        if (jsonString != null) {
            try {
                return new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
                error = JSONParameter.ErrorCodes.READ_JSON;
            }
        } else if (error.equals(ErrorCodes.OK)) {
            error = JSONParameter.ErrorCodes.READ_JSON;
        }

        try {
            response.getWriter().println(createJSONError(error));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
