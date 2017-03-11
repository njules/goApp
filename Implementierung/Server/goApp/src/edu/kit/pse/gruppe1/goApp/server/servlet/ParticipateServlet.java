package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Servlet implementation class ParticipateServlet
 * 
 * Users can access methods in this servlet to indicate whether they want to participate in an event
 * from the group.
 */
@WebServlet("/ParticipateServlet")
public class ParticipateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final EventUserManagement eventUser;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ParticipateServlet() {
        super();
        eventUser = new EventUserManagement();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String strResponse = null;
        JSONObject jsonRequest = null;
        JSONParameter.Methods method = null;
        PrintWriter out = null;
        ErrorCodes error = ErrorCodes.OK;

        out = response.getWriter();

        jsonRequest = ServletUtils.extractJSON(request, response);
        if (jsonRequest == null) {
            // response was set in extractJSON
            return;
        }

        try {
            method = JSONParameter.Methods
                    .fromString(jsonRequest.getString(JSONParameter.METHOD.toString()));
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
        }

        if (method == null || !error.equals(ErrorCodes.OK)) {
            method = Methods.NONE;
        }

        switch (method) {
        case SET_STATUS:
            strResponse = setStatus(jsonRequest).toString();
            break;
        default:
            if (error.equals(ErrorCodes.OK)) {
                error = ErrorCodes.METH_ERROR;
            }
            strResponse = ServletUtils.createJSONError(error).toString();
            break;
        }
        out.println(strResponse);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * A user may reject and accept an invitation to any event in a group he is a member of. He may
     * accept or reject any event only once and can not invoke any of these two methods again later
     * on for the same event. Has he accepted an event, he is participating in it. While
     * participating he can update his status to "go" in order to get access to the groups position
     * and get tracked himself.
     * 
     * @param json
     *            A JSON object that contains the user wanting to update his status, the status he
     *            wants it to be set to and the event for which this action shall take place.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private JSONObject setStatus(JSONObject json) {
        int event = -1;
        int user = -1;
        Status status = null;
        try {
            event = json.getInt(JSONParameter.EVENT_ID.toString());
            user = json.getInt(JSONParameter.USER_ID.toString());
            status = Status.fromInteger(json.getInt(JSONParameter.STATUS.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }
        if (status == null) {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
        }
        if (status.equals(Status.REJECTED)) {
            if (!eventUser.delete(event, user)) {
                return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
            }
        }
        if (!eventUser.updateStatus(event, user, status)) {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
        }
        return ServletUtils.createJSONError(JSONParameter.ErrorCodes.OK);

    }
}
