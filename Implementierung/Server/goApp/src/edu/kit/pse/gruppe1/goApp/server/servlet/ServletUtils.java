package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Utils for all Servlets
 *
 * contains methods for creating JSON Strings out of classes and to reduce redundant code
 */
public final class ServletUtils {

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
    protected static boolean isValidGoogleToken() {
        // TODO: https://developers.google.com/identity/sign-in/web/backend-auth
        // verfify ID signed by Google
        // check if one User has this GoogleID == aud TODO: fehlt noch im kopierten Code
        // iss == accounts.google.com oder https://accounts.google.com
        // expired time has not passed exp

        // Copied from Google (URL siehe oben)
        JsonFactory jsonFactory = null;
        HttpTransport transport = null;
        String CLIENT_ID = null;
        String idTokenString = null;

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                // .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        // (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            // ...

        } else {
            System.out.println("Invalid ID token.");
        }

        // kopiert bis hier.

        // try:
        // https://developers.google.com/api-client-library/java/google-api-java-client/reference/1.20.0/com/google/api/client/googleapis/auth/oauth2/GoogleAuthorizationCodeFlow#newAuthorizationUrl()
        // com.google.api.client.http.HttpTransport transport = null;
        // com.google.api.client.json.JsonFactory jsonFactory = null;
        // String clientId = null;
        // String clientSecret = null;
        // Collection<String> scopes = null;
        // Credential token = null;
        //
        // GoogleAuthorizationCodeFlow google = new GoogleAuthorizationCodeFlow(transport,
        // jsonFactory,
        // clientId, clientSecret, scopes);
        // String userID = "123";
        // try {
        // token = google.loadCredential(userID);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // if (token == null) {
        // AuthorizationCodeRequestUrl url = google.newAuthorizationUrl();
        // }

        return true;
    }
    
    protected static JSONObject createJSONEventID(Event event){
        JSONObject json = new JSONObject();
        try {
            json.put(JSONParameter.EVENT_ID.toString(), event.getEventId());
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.toString());
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

            json.put(JSONParameter.GRUOP_ID.toString(), event.getGroup().getGroupId());
            json.put(JSONParameter.USER_ID.toString(), event.getCreator().getUserId());
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.toString());
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
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.toString());
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
            json.put(JSONParameter.GRUOP_ID.toString(), group.getGroupId());

            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.toString());
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
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.toString());
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
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.toString());
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
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.toString());
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
            json.put(JSONParameter.ERROR_CODE.toString(), ErrorCodes.OK.toString());
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
