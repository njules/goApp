package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.User;

/**
 * Servlet implementation class LoginServlet
 * 
 * This servlet is used to register users that start the app for the first time and login returning
 * users.
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private UserManagement usrMang;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public LoginServlet() {
    super();
    usrMang = new UserManagement();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // TODO Auto-generated method stub
    response.getWriter().append("Served at: ").append(request.getContextPath());
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(request, response);
  }

  /**
   * Invoking this method creates a new user and registers his ID to the database. The new user may
   * then join groups. A user can register only once, if he returns to the app he must use the login
   * function.
   * 
   * @param json
   *          The JSON object contains a user ID to which the user shall be registered.
   * @return Returns a JSON string containing the user that just registered.
   */
  private String register(JSONObject json) {
      //TODO: wie sicherstellen, ob schon registriert, DB oder hier?
      int googleId = -1;
      String name = null;
      String error = null;
      User user = null;
      
      try {
        googleId = json.getInt(JSONParameter.ID.toString());
    } catch (JSONException e) {
        error = "GoogleID not found";
    }
      try {
        name = json.getString(JSONParameter.UserName.toString());
    } catch (JSONException e) {
        error = "UserName not found";
    }
      
    user = usrMang.add(name, googleId);
    return createJSONObject(user, error);
  }

  /**
   * If a user has already registered to the database but his client is currently not logged in, he
   * can call this method to regain access to the functions of this app. A user may only login if he
   * isn't already logged in and has registered himself previously at any point.
   * 
   * @param json
   *          This JSON object contains the user that wants to login.
   * @return Returns a JSON string containing the user that just logged in.
   */
  private String login(JSONObject json) {
    // TODO: Was passsiert hier?
    throw new UnsupportedOperationException();
  }
  
  // TODO: JavaDocs
  // TODO: wie ganze Klassen Serialisieren
  private String createJSONObject(User user, String error) {
    String result = null;
    JSONObject res = new JSONObject();
    try {
      if (error == null) {
        res.append(JSONParameter.User.toString(), user);
      } else {
        res.append(JSONParameter.ErrorCode.toString(), error);
      }
    } catch (JSONException e) {
      // TODO Was nur tun?
    }
    result = res.toString();
    return result;
  }

}
