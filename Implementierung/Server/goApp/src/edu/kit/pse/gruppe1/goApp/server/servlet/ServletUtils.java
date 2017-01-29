package edu.kit.pse.gruppe1.goApp.server.servlet;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.User;

public final class ServletUtils {

    private ServletUtils() {
    }

    public static String createJSONGroup(Group group) {
        return null;
    }

    public static String createJSONUser(User user) {
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

}
