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

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/BeispielServlet")
public class BeispielServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   /**
    * @see HttpServlet#HttpServlet()
    */
   public BeispielServlet() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
    *      response)
    */
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/plain");
      PrintWriter out = response.getWriter();
      String jsonString = request.getReader().readLine();
      if (jsonString == null) {
         out.println("emtpy request");
         return;
      }
      try {
         JSONObject jsonRequest = new JSONObject(jsonString);

         String responseS = "";
         switch (jsonRequest.getString(JSONParameter.Method.toString())) {
         case "testMethode":
            responseS = testMethode(jsonRequest);
            break;
         }
         out.println(responseS);
      } catch (JSONException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   private String testMethode(JSONObject jsonRequest) {
      JSONObject jsonAnswer = new JSONObject();
      /*
      try {
         String name = new Database().getUserName(jsonRequest.getInt(JSONParameter.UserID.toString()));
         jsonAnswer.put(JSONParameter.Name.toString(), name);
      } catch (JSONException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }*/
      System.out.println(jsonAnswer.toString());
      return jsonAnswer.toString();// JSONObject.quote(jsonAnswer.toString());
   }

   /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
    *      response)
    */
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      doGet(request, response);
   }

}
