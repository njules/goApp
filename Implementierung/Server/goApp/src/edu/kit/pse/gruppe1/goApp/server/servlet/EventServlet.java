package edu.kit.pse.gruppe1.goApp.server.servlet;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EventServlet
 * 
 * This servlet is used to create, view and edit events within a given group.
 */
@WebServlet("/EventServlet")
public class EventServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private EventManagement eventMang;

  /**
   * Default constructor.
   */
  public EventServlet() {
    this.eventMang = new EventManagement();
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
   * Any user, that is a member of a group may create an event within this group. The member that
   * creates this event will be registered as the event admin. The admin has the right to change
   * data about his event. Each member of a group may only be admin of one event within this group.
   * 
   * @param json
   *          This JSON object contains all information about the new event such as event time,
   *          location, event name and the user creating this event.
   * @return A JSON string containing the previously created event is returned.
   */
  private String create(JSONObject json) {
    Event event = null;
    Time time = null;
    String name = null;
    Location location = null;
    int creatorID = -1;
    int groupID = -1;
    String error = null;

    try {
      name = json.getString(JSONParameter.EventName.toString());
    } catch (JSONException e) {
      error = "EventName not found";
    }
    try {
      double longitude = json.getDouble(JSONParameter.Longitude.toString());
      double latitude = json.getDouble(JSONParameter.Latitude.toString());
      String locName = json.getString(JSONParameter.LocationName.toString());

      location = new Location(longitude, latitude, locName);
    } catch (JSONException e) {
      error = "Location not found";
    }
    try {
      time = (Time) json.get(JSONParameter.EventTime.toString());
    } catch (JSONException e) {
      error = "EventTime not found";
    }
    try {
      creatorID = json.getInt(JSONParameter.UserID.toString());
    } catch (JSONException e) {
      error = "CreatorID not found";
    }
    try {
      groupID = json.getInt(JSONParameter.GroupID.toString());
    } catch (JSONException e) {
      error = "GroupID not found";
    }

    event = this.eventMang.add(name, location, time, creatorID, groupID);

    if (event == null) {
      error = "Event could not be created on database";
    }
    return createJSONObject(event, error);
  }

  // TODO: JavaDocs
  // TODO: wie ganze Klassen Serialisieren
  private String createJSONObject(Event event, String error) {
    String result = null;
    JSONObject res = new JSONObject();
    try {
      if (error == null) {
        res.append(JSONParameter.Event.toString(), event);
      } else {
        res.append(JSONParameter.ErrorCode.toString(), error);
      }
    } catch (JSONException e) {
      // TODO Was nur tun?
    }
    result = res.toString();
    return result;
  }

  /**
   * A method used to access information about an event. Every user, that can see this event may
   * request information about it. Users that are not a member of the group may not view the groups
   * events. Accessible information includes name, location, admin and time.
   * 
   * @param json
   *          A JSON object containing the event about which the information is requested.
   * @return A JSON string containing all information about the given event is returned.
   */
  private String getEvent(JSONObject json) {
    Event event = null;
    String error = null;

    try {
      int eventID = json.getInt(JSONParameter.EventID.toString());
      event = this.eventMang.getEvent(eventID);
    } catch (JSONException e) {
      error = "eventID not found";
    }
    return createJSONObject(event, error);

  }

  /**
   * This method may only be invoked by an event admin and he may only change the event, he
   * administrates. He may update all information such as name, location and date. He may also elect
   * a new admin for this event or delete it.
   * 
   * @param json
   *          The JSON object contains an event with the updated information.
   * @return A JSON string containing the updated information about the event is returned.
   */
  private String change(JSONObject json) {
    String error = null;
    Event event = null;
    boolean valuesChanged = false;

    try {
      int eventID = json.getInt(JSONParameter.EventID.toString());
      event = this.eventMang.getEvent(eventID);
    } catch (JSONException e) {
      error = "eventID not found";
      return createJSONObject(event, error);
    }

    // for the following part: if attribute is in json Object - change value in Event
    try {
      String name = json.getString(JSONParameter.EventName.toString());
      event.setName(name);
      valuesChanged = true;
    } catch (JSONException e) {
      // do nothing, because it can happen
    }
    try {
      double longitude = json.getDouble(JSONParameter.Longitude.toString());
      double latitude = json.getDouble(JSONParameter.Latitude.toString());
      Location loc = event.getLocation();
      loc.setLatitude(latitude);
      loc.setLongitude(longitude);
      event.setLocation(loc);
      valuesChanged = true;
    } catch (JSONException e) {
      // do nothing, because it can happen
    }

    try {
      String locName = json.getString(JSONParameter.LocationName.toString());
      Location loc = event.getLocation();
      loc.setName(locName);
      event.setLocation(loc);
      valuesChanged = true;
    } catch (JSONException e) {
      // do nothing, because it can happen
    }

    try {
      Time time = (Time) json.get(JSONParameter.EventTime.toString());
      event.setTime(time);
      valuesChanged = true;
    } catch (JSONException e) {
      // do nothing, because it can happen
    }

    try {
      int creatorID = json.getInt(JSONParameter.UserID.toString());
      UserManagement usMang = new UserManagement();
      User creator = usMang.getUser(creatorID);
      if (creator != null) {
        event.setCreator(creator);
        valuesChanged = true;
      } else {
        error = "user does not exist";
      }
    } catch (JSONException e) {
      // do nothing, because it can happen
    }
    try {
      int groupID = json.getInt(JSONParameter.GroupID.toString());
      GroupManagement grMang = new GroupManagement();
      Group group = grMang.getGroup(groupID);
      if (group != null) {
        event.setGroup(group);
        valuesChanged = true;
      } else {
        error = "group does not exist";
      }
    } catch (JSONException e) {
      // do nothing, because it can happen
    }

    if (!valuesChanged) {
      error = "no changed values";
    } else {
      eventMang.update(event);
    }
    return createJSONObject(event, error);
  }

}
