package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class EventServlet
 * 
 * This servlet is used to create, view and edit events within a given group.
 */
@WebServlet("/EventServlet")
public class EventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public EventServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * Any user, that is a member of a group may create an event within this group. The member that creates this event will be registered as the event admin. The admin has the right to change data about his event. Each member of a group may only be admin of one event within this group.
	 * @param json This JSON object contains all information about the new event such as event time, location, event name and the user creating this event.
	 * @return A JSON string containing the previously created event is returned.
	 */
	private String create(JSONObject json) {
		// TODO - implement EventServlet.create
		throw new UnsupportedOperationException();
	}

	/**
	 * A method used to access information about an event. Every user, that can see this event may request information about it. Users that are not a member of the group may not view the groups events. Accessible information includes name, location, admin and time.
	 * @param json A JSON object containing the event about which the information is requested.
	 * @return A JSON string containing all information about the given event is returned.
	 */
	private String getEvent(JSONObject json) {
		// TODO - implement EventServlet.getEvent
		throw new UnsupportedOperationException();
	}

	/**
	 * This method may only be invoked by an event admin and he may only change the event, he administrates. He may update all information such as name, location and date. He may also elect a new admin for this event or delete it.
	 * @param json The JSON object contains an event with the updated information.
	 * @return A JSON string containing the updated information about the event is returned.
	 */
	private String change(JSONObject json) {
		// TODO - implement EventServlet.change
		throw new UnsupportedOperationException();
	}

}