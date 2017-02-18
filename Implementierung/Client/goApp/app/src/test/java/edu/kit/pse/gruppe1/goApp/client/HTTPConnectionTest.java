package edu.kit.pse.gruppe1.goApp.client;

import android.util.Log;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Jonas on 17.02.2017.
 */

public class HTTPConnectionTest {
    @Test
    public void testSendGetRequest() throws Exception {
        JSONObject response = new JSONObject();
        JSONObject j = new HTTPConnection("GroupSearchServlet").sendGetRequest(response.toString());
        assertThat(j.getInt(JSONParameter.ERROR_CODE.toString()), is(JSONParameter.ErrorCodes.READ_JSON.getErrorCode()));
    }
    @Test
    public void testSendPostRequest() throws Exception {
        JSONObject response = new JSONObject();
        JSONObject j = new HTTPConnection("GroupSearchServlet").sendPostRequest(response.toString());
        assertThat(j.getInt(JSONParameter.ERROR_CODE.toString()), is(JSONParameter.ErrorCodes.READ_JSON.getErrorCode()));
    }
    @Test
    public void testConnectionFailed() throws Exception {
        JSONObject response = new JSONObject();
        JSONObject j = new HTTPConnection("NoValidServlet").sendPostRequest(response.toString());
        assertThat(j.getInt(JSONParameter.ERROR_CODE.toString()), is(JSONParameter.ErrorCodes.CONNECTION_FAILED.getErrorCode()));
    }

}
