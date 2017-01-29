package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static JSONObject createJSONEvent(Event event) {
        JSONObject json = new JSONObject();
        try {
            json.append(JSONParameter.EventName.toString(), event.getName());
            json.append(JSONParameter.EventTime.toString(), event.getTime().getTime());

            // TODO: json.append(JSONParameter.LOCATION.toString(),
            // locationObject(event.getLocation()));
            json.append(JSONParameter.LocationName.toString(), event.getLocation().getName());
            json.append(JSONParameter.Longitude.toString(), event.getLocation().getLongitude());
            json.append(JSONParameter.Latitude.toString(), event.getLocation().getLatitude());

            // TODO: json.append(JSONParameter.GROUP.toString(), groupObject(event.getGroup()));
            json.append(JSONParameter.GroupID.toString(), event.getGroup().getGroupId());

            // TODO: json.append(JSONParameter.USER.toString(), userObject(event.getCreator()));
            json.append(JSONParameter.UserID.toString(), event.getCreator().getUserId());

            json.append(JSONParameter.EventID.toString(), event.getEventId());
            json.append(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    // private static JSONObject locationObject(Location location) throws JSONException {
    // JSONObject json = new JSONObject();
    // json.append(JSONParameter.LocationName.toString(), location.getName());
    // json.append(JSONParameter.Longitude.toString(), location.getLongitude());
    // json.append(JSONParameter.Latitude.toString(), location.getLatitude());
    // return json;
    // }

    public static JSONObject createJSONLocation(Location location) {
        JSONObject json = new JSONObject();

        try {
            json.append(JSONParameter.LocationName.toString(), location.getName());
            json.append(JSONParameter.Longitude.toString(), location.getLongitude());
            json.append(JSONParameter.Latitude.toString(), location.getLatitude());
            json.append(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONGroup(Group group) {
        JSONObject json = new JSONObject();

        try {
            json.append(JSONParameter.LocationName.toString(), group.getName());
            json.append(JSONParameter.Longitude.toString(), group.getGroupId());

            // TODO: json.append(JSONParameter.USER.toString(), userObject(group.getFounder()));
            json.append(JSONParameter.UserID.toString(), group.getFounder().getUserId());
            json.append(JSONParameter.UserName.toString(), group.getFounder().getName());

            json.append(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONUser(User user) {
        JSONObject json = new JSONObject();
        try {
            // TODO: User user object ??
            json.append(JSONParameter.UserID.toString(), user.getUserId());
            json.append(JSONParameter.UserName.toString(), user.getName());
            json.append(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    // private static JSONObject userObject(User user){
    // JSONObject json = new JSONObject();
    // try {
    // json.append(JSONParameter.UserID.toString(), user.getUserId());
    // json.append(JSONParameter.UserName.toString(), user.getName());
    // } catch (JSONException e) {
    // e.printStackTrace();
    // return null;
    // }
    // return json;
    // }

    public static JSONObject createJSONListUsr(List<User> user) {
        JSONObject json = new JSONObject();
        try {
            // TODO: Liste User
            json.append(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONListGrp(List<Group> group) {
        JSONObject json = new JSONObject();
        try {
            // TODO: Liste Gruppen

            json.append(JSONParameter.ErrorCode.toString(), ErrorCodes.OK.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject createJSONError(JSONParameter.ErrorCodes error) {
        JSONObject res = new JSONObject();
        try {
            res.append(JSONParameter.ErrorCode.toString(), error.getErrorCode());
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
