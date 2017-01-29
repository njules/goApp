package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

public final class ServletUtils {

    private ServletUtils() {
    }

    public static String createJSONEvent(Event event) {
        return null;
    }

    public static String createJSONLocation(Location location) {
        return null;
    }

    public static String createJSONGroup(Group group) {
        return null;
    }

    public static String createJSONUser(User user) {
        return null;
    }

    public static String createJSONListUsr(List<User> user) {
        return null;
    }

    public static String createJSONListGrp(List<Group> group) {
        return null;
    }

    public static String createJSONError(JSONParameter.ErrorCodes error) {
        JSONObject res = new JSONObject();
        try {
            res.append(JSONParameter.ErrorCode.toString(), error.getErrorCode());
        } catch (JSONException e) {
            // TODO Keine Ahnung
        }
        return res.toString();
    }

    // TODO: testweise, noch nicht gut
    /**
     * DO NOT USE YET
     * 
     * @param request
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static Methods getMethod(HttpServletRequest request) throws JSONException, IOException {
        String jsonString = null;
        JSONParameter.Methods method = null;
        jsonString = request.getReader().readLine();

        if (jsonString == null) {
            throw new JSONException(ErrorCodes.EMPTY_JSON.toString());
        }
        JSONObject jsonRequest = new JSONObject(jsonString);
        method = JSONParameter.Methods
                .fromString(jsonRequest.getString(JSONParameter.Method.toString()));
        return method;
    }
}
