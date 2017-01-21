package edu.kit.pse.gruppe1.goApp.client.edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;


import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.controler.service.GroupSearchService;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.User;

import static org.mockito.Mockito.when;

/**
 * Created by Katharina Riesterer on 19.01.2017.
 */

public class GroupSearchServiceTest {

    @Mock
    Context mockContext;
    @Mock
    BroadcastReceiver resultReceiver;


    GroupSearchService service;
    User user = new User(1245, "Max");
    @Before
    public void Init(){
        service = new GroupSearchService();
        service.getGroupsByMember(mockContext, user);
    }

    @Test
    public void Start() throws Exception {
        assertThat(service.isRunning(), is(true));
    }

    @Test
    public void Receive(){
        ResultReceiver receiver = new ResultReceiver();
        assertThat(receiver.isReceive(),is(true));
    }

    private class ResultReceiver extends BroadcastReceiver{

        private Group[] groups;
        private boolean receive =false;
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.equals(null)){

            } else{
                receive = true;
            }


        }

        public boolean isReceive() {
            return receive;
        }
    }
}

