
package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupUserManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Servlet implementation class GroupServlet
 * 
 * This servlet contains all methods to create, manipulate and delete groups.
 */
@WebServlet("/GroupServlet")
public class GroupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final GroupManagement groupManager;
    private final GroupUserManagement groupUserManager;
    private final UserManagement userManager;
    private final EventUserManagement eventUserManager;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupServlet() {
        super();
        groupManager = new GroupManagement();
        groupUserManager = new GroupUserManagement();
        userManager = new UserManagement();
        eventUserManager = new EventUserManagement();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject jsonRequest = ServletUtils.extractJSON(request, response);
        if (jsonRequest == null) {
            return;
        }
        Methods method;
        try {
            method = JSONParameter.Methods
                    .fromString(jsonRequest.getString(JSONParameter.METHOD.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            response.getWriter()
                    .println(ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON));
            return;
        }

        if (method == null) {
            method = Methods.NONE;
        }

        switch (method) {
        case CREATE:
            response.getWriter().println(create(jsonRequest).toString());
            break;
        case DELETE:
            response.getWriter().println(delete(jsonRequest).toString());
            break;
        case SET_NAME:
            response.getWriter().println(setName(jsonRequest).toString());
            break;
        case DEL_MEM:
            response.getWriter().println(deleteMember(jsonRequest).toString());
            break;
        case GET_EVENT:
            response.getWriter().println(getEvents(jsonRequest).toString());
            break;
        case GET_MEMBERS:
            response.getWriter().println(getMembers(jsonRequest).toString());
            break;
        case SET_FOUNDER:
            response.getWriter().println(setFounder(jsonRequest).toString());
            break;
        default:
            response.getWriter()
                    .println(ServletUtils.createJSONError(JSONParameter.ErrorCodes.METH_ERROR));
        }
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
     * This method creates a new group. A user can create a new group if has not yet reached the
     * group limit. By creating a group his group count is incremented by one. He is also registered
     * as the groups founder.
     * 
     * @param json
     *            This JSON objects contains information about the group that is to be created such
     *            as the Name and the founding member.
     * @return Returns a JSON string containing the ID of the created group.
     */
    private JSONObject create(JSONObject json) {
        String name = null;
        int founder = -1;
        Group group = null;
        try {
            name = json.getString(JSONParameter.GROUP_NAME.toString());
            founder = json.getInt(JSONParameter.USER_ID.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }
        group = groupManager.add(name, founder);
        if (group == null) {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
        }
        return ServletUtils.createJSONGroupID(group);
    }

    /**
     * This method may be called by a founder to delete his group. The group will be removed from
     * the database as well as all events and the users are removed from this group. The group count
     * of all users in this group is reduced by one.
     * 
     * @param json
     *            JSON object containing the ID of the group that is to be deleted.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private JSONObject delete(JSONObject json) {
        try {
            int group = json.getInt(JSONParameter.GROUP_ID.toString());
            if (!groupManager.delete(group)) {
                return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
            }
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.OK);
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }
    }

    /**
     * This method may change the name of a given group. Only a groups founder may change its name.
     * 
     * @param json
     *            JSON object containing the ID of the group on which the name change shall be
     *            executed and the new name of the group in a string.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private JSONObject setName(JSONObject json) {
        int groupId = -1;
        String newName = null;
        try {
            groupId = json.getInt(JSONParameter.GROUP_ID.toString());
            newName = json.getString(JSONParameter.GROUP_NAME.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }

        if (!groupManager.updateName(groupId, newName)) {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.METH_ERROR);
        }
        return ServletUtils.createJSONError(JSONParameter.ErrorCodes.OK);
    }

    /**
     * A founder may call this method in order to remove members from his group. They will also be
     * removed from all events in this group and their group count is decremented by one.
     * 
     * @param json
     *            JSON object containing the ID of the group from which the member shall be removed
     *            and the user that should be removed.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private JSONObject deleteMember(JSONObject json) {
        int group = -1;
        int member = -1;
        try {
            group = json.getInt(JSONParameter.GROUP_ID.toString());
            member = json.getInt(JSONParameter.USER_ID.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }

        if (!groupUserManager.delete(group, member)) {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
        }
        return ServletUtils.createJSONError(JSONParameter.ErrorCodes.OK);
    }

    /**
     * Any user may request a list of events from any one group he is a member of.
     * 
     * @param json
     *            JSON object containing the ID of the group from which the events are requested.
     * @return Returns a JSON string containing a List with all Events within that group.
     */
    private JSONObject getEvents(JSONObject json) {
        int group = -1;
        int member = -1;
        List<Event> event = new ArrayList<Event>();
        try {
            group = json.getInt(JSONParameter.GROUP_ID.toString());
            member = json.getInt(JSONParameter.USER_ID.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }

        List<Event> eventsPart = eventUserManager.getEventsByStatus(Status.PARTICIPATE, group,
                member);
        List<Event> eventsStart = eventUserManager.getEventsByStatus(Status.STARTED, group, member);
        if (eventsPart != null) {
            event.addAll(eventsPart);
        } else {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
        }
        if (eventsStart != null) {
            event.addAll(eventsStart);
        } else {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
        }

        List<Event> events2 = eventUserManager.getEventsByStatus(Status.INVITED, group, member);
        if (events2 == null) {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
        }
        return ServletUtils.createJSONDoubleListEvent(event, events2);

    }

    /**
     * This method returns all members of a given group and may be invoked by any member of that
     * group.
     * 
     * @param json
     *            JSON object containing the ID of the group about which the members are requested.
     * @return Returns a JSON string containing the groups members.
     */
    private JSONObject getMembers(JSONObject json) {
        try {
            int groupID = json.getInt(JSONParameter.GROUP_ID.toString());
            List<User> users = groupUserManager.getUsers(groupID);
            return ServletUtils.createJSONListUsr(users);
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }
    }

    /**
     * If a founder wishes to resign, he can call this method to transfer his rights to another
     * user. Only a founder may invoke this method on his group. If a founder leaves his group this
     * method is called automatically. Only a user that is currently a member in this group may be
     * elected as the new groups founder.
     * 
     * @param json
     *            JSON object containing the ID of the group whose founder shall be changed and the
     *            member of the group that is to become the founder.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private JSONObject setFounder(JSONObject json) {
        User user = null;
        int group = -1;
        int newFounder = -1;
        try {
            group = json.getInt(JSONParameter.GROUP_ID.toString());
            newFounder = json.getInt(JSONParameter.USER_ID.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }
        user = userManager.getUser(newFounder);
        if (user == null) {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
        }
        if (!groupManager.updateFounder(group, user)) {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
        }
        return ServletUtils.createJSONError(JSONParameter.ErrorCodes.OK);
    }

}
