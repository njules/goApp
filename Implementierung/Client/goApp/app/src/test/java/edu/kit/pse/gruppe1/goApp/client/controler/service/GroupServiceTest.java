package edu.kit.pse.gruppe1.goApp.client.controler.service;

import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;

import static org.mockito.Mockito.when;

/**
 * Created by Katharina Riesterer on 04.02.2017.
 */

public class GroupServiceTest {
    GroupService service;
    JSONObject requestJson;
    User user;
    Group group;


    @Mock
    HTTPConnection mockConnection;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        service = new GroupService();
        Field field = service.getClass().getField("connection");
        field.setAccessible(true);
        field.set(service, mockConnection);
        requestJson = new JSONObject();
        user = new User(1,"User");
        group = new Group(0,"Group",user);

    }


    @Test
    public void CreateTest() {
        try {
            requestJson.put(JSONParameter.GroupName.toString(), group.getName());
            requestJson.put(JSONParameter.UserID.toString(), user.getId());
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject response = new JSONObject();
        try {
            response.accumulate(JSONParameter.GroupID.toString(), group.getId());
            response.accumulate(JSONParameter.ErrorCode.toString(), JSONParameter.ErrorCodes.OK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        when(mockConnection.sendGetRequest(requestJson.toString())).thenReturn(response);
    }


}
