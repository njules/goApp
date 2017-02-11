package edu.kit.pse.gruppe1.goApp.server.servlet;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EventServletTest.class, GroupSearchServletTest.class, GroupServletTest.class,
        LocationServletTest.class, LoginServletTest.class, ParticipateServletTest.class,
        RequestServletTest.class, ServletUtilsTest.class, UserServletTest.class,
        RequestSearchServletTest.class })
public class AllServletTests {

}
