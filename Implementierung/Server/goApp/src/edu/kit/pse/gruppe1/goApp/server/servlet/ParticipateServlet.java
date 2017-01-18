package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class ParticipateServlet
 * 
 * Users can access methods in this servlet to indicate whether they want to participate in an event from the group.
 */
@WebServlet("/ParticipateServlet")
public class ParticipateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ParticipateServlet() {
        super();
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
	 * A user may accept an invitation to any event in a group he is a member of. He may accept or reject any event only once and can not invoke any of these two methods again later on for the same event.
	 * @param json A JSON object that contains the user wanting to accept the invite and the event he wants to participate in.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String accept(JSONObject json) {
		// TODO - implement ParticipateServlet.accept
		throw new UnsupportedOperationException();
	}

	/**
	 * A user may reject an invitation to any event in a group he is a member of. He may accept or reject any event only once and can not invoke any of these two methods again later on for the same event.
	 * @param json A JSON object that contains the user wanting to decline the invite and the event he wants to abstain from.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String reject(JSONObject json) {
		// TODO - implement ParticipateServlet.reject
		throw new UnsupportedOperationException();
	}

}
