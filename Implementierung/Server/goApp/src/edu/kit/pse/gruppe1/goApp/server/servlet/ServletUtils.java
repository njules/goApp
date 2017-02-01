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

    public static boolean isValidGoogleToken() {
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

        return false;
    }

    public static JSONObject createJSONEvent(Event event) {
        JSONObject json = new JSONObject();
        try {
            json.accumulate(JSONParameter.EventName.toString(), event.getName());
            json.accumulate(JSONParameter.EventTime.toString(), event.getTimestamp().getTime());
            json.accumulate(JSONParameter.EventID.toString(), event.getEventId());

            json.accumulate(JSONParameter.LocationName.toString(), event.getLocation().getName());
            json.accumulate(JSONParameter.Longitude.toString(), event.getLocation().getLongitude());
            json.accumulate(JSONParameter.Latitude.toString(), event.getLocation().getLatitude());

            json.accumulate(JSONParameter.GroupID.toString(), event.getGroup().getGroupId());
            json.accumulate(JSONParameter.UserID.toString(), event.getCreator().getUserId());

            json.put(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONLocation(Location location) {
        JSONObject json = new JSONObject();

        try {
            json.accumulate(JSONParameter.LocationName.toString(), location.getName());
            json.accumulate(JSONParameter.Longitude.toString(), location.getLongitude());
            json.accumulate(JSONParameter.Latitude.toString(), location.getLatitude());
            json.put(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONGroup(Group group) {
        JSONObject json = new JSONObject();

        try {
            json.accumulate(JSONParameter.UserID.toString(), group.getFounder().getUserId());
            json.accumulate(JSONParameter.UserName.toString(), group.getFounder().getName());
            json.accumulate(JSONParameter.GroupName.toString(), group.getName());
            json.accumulate(JSONParameter.GroupID.toString(), group.getGroupId());

            json.put(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONUser(User user) {
        JSONObject json = new JSONObject();
        try {
            json.accumulate(JSONParameter.UserID.toString(), user.getUserId());
            json.accumulate(JSONParameter.UserName.toString(), user.getName());
            json.put(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONListEvent(List<Event> event) {
        JSONObject json = new JSONObject();
        try {
            for (Event evt : event) {
                json.append(JSONParameter.LIST_EVENT.toString(), createJSONEvent(evt));
            }
            json.put(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONListUsr(List<User> user) {
        JSONObject json = new JSONObject();
        try {
            for (User usr : user) {
                json.append(JSONParameter.LIST_USER.toString(), createJSONUser(usr));
            }
            json.put(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONListGrp(List<Group> group) {
        JSONObject json = new JSONObject();
        try {
            for (Group grp : group) {
                json.append(JSONParameter.LIST_GROUP.toString(), createJSONGroup(grp));
            }
            json.put(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONError(JSONParameter.ErrorCodes error) {
        JSONObject res = new JSONObject();
        try {
            res.put(JSONParameter.ErrorCode.toString(), error.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    /**
     * extract Method from request
     * 
     * @param request
     *            Incoming Request
     * @param jsonRequest
     *            jsonRequest (to set here)
     * @return Method (Enum Value)
     * @throws ServletException
     *             if Servlet Exception happens
     * @throws JSONException
     *             if JSONException happens - either with Message if JSON was empty or without (Read
     *             Error)
     * @throws IOException
     *             if problems with IO operation happened
     */
    public static Methods getMethod(HttpServletRequest request, JSONObject jsonRequest)
            throws ServletException, JSONException, IOException {
        String jsonString = null;
        JSONParameter.Methods method = null;
        jsonString = request.getReader().readLine();

        if (jsonString == null) {
            throw new JSONException(ErrorCodes.EMPTY_JSON.toString());
        }
        jsonRequest = new JSONObject(jsonString);
        method = JSONParameter.Methods
                .fromString(jsonRequest.getString(JSONParameter.Method.toString()));
        return method;
    }

    // TODO Julian: JavaDocs schreiben
    protected static JSONObject extractJSON(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            if (request.getReader().readLine() == null) {
            }
            return new JSONObject(request.getReader().readLine());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            try {
                response.getWriter().println(createJSONError(JSONParameter.ErrorCodes.IO_ERROR));
            } catch (IOException n) {
                n.printStackTrace();
            }
            return null;
        }
    }
}
