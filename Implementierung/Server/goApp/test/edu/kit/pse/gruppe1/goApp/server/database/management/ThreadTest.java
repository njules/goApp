package edu.kit.pse.gruppe1.goApp.server.database.management;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.gruppe1.goApp.server.model.User;

public class ThreadTest {
    private User createdUser;
    private String userName = "user name";
    private int googleId = 1234;

    @Before
    public void setUp() throws Exception {
        createdUser = new UserManagement().add(userName, googleId);
        assertThat(createdUser, is(notNullValue()));
    }

    @After
    public void tearDown() throws Exception {
        new UserManagement().delete(createdUser.getUserId());
    }

    @Test
    public void test() {
        int countThreads = 10;
        Thread[] threads = new Thread[countThreads];
        for (int i = 0; i < countThreads; i++) {
            threads[i] = new Thread(new QueryThread());
            threads[i].start();
        }

        for (int i = 0; i < countThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private class QueryThread implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                User user = new UserManagement().getUser(createdUser.getUserId());
                assertThat(user, is(notNullValue()));
                assertThat(user.getUserId(), is(createdUser.getUserId()));
            }
        }
    }

}
