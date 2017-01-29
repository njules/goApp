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
        return null;
    }

    public static JSONObject createJSONLocation(Location location) {
        return null;
    }

    public static JSONObject createJSONGroup(Group group) {
        return null;
    }

    public static JSONObject createJSONUser(User user) {
        return null;
    }

    public static JSONObject createJSONListUsr(List<User> user) {
        return null;
    }

    public static JSONObject createJSONListGrp(List<Group> group) {
        return null;
    }

    public static JSONObject createJSONError(JSONParameter.ErrorCodes error) {
        JSONObject res = new JSONObject();
        try {
            res.append(JSONParameter.ErrorCode.toString(), error.getErrorCode());
        } catch (JSONException e) {
            e.printStackTrace();
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

    protected static JSONObject extractJSON(HttpServletRequest request, HttpServletResponse response) {
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
