package edu.kit.pse.gruppe1.goApp.client.view;

/**
 * An Activity needs to implement this interface so that DialogFragments can pass information to it.
 */

public interface Communicator {
    public void respond(String response);
}
