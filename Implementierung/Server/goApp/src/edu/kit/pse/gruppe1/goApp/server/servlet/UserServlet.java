package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class UserServlet
 * 
 * This servlet is used by users to access and manage information about them.
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
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
	 * This method allows a user to change his name to a string value of his choice. A user may only change his own name.
	 * @param json The JSON object contains the user that wants to change his name and the string with the name the user wants to change it to.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String changeName(JSONObject json) {
		// TODO - implement UserServlet.changeName
		throw new UnsupportedOperationException();
	}

	/**
	 * A user can invoke this to retrieve any information about a given user such as groups he is a member of and events he wants to participate or is invited to.
	 * @param json This JSON object contains the user about whom the information shall be released.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String getUser(JSONObject json) {
		// TODO - implement UserServlet.getUser
		throw new UnsupportedOperationException();
	}


}
