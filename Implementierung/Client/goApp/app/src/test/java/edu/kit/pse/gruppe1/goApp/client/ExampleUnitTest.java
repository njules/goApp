package edu.kit.pse.gruppe1.goApp.client;

import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        JSONObject j = new HTTPConnection("UserServlet").sendGetRequest("");
        assertThat(j.getString(JSONParameter.ERROR_CODE.toString()), is(""));

    }
}