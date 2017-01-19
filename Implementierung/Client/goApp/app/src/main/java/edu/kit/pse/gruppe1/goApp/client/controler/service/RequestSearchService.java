package edu.kit.pse.gruppe1.goApp.client.controler.service;

import java.util.List;

import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * This Service is used to list Requests
 */
public class RequestSearchService {

	/**
	 * finds all Requests of a given user. . This is used to show all the requests to this user in the StartActivity
	 * @param user the user who started the requests
	 * @return all requests the user send or null
	 */
	private List<Group> getRequestsByUser(User user) {
		// TODO - implement RequestSearchService.getRequestsByUser
		throw new UnsupportedOperationException();
	}

	/**
	 * finds all access requests to a given group. This is used to present the requests to the founder of the group to let him decide about them in the GrouInfoActivity
	 * @param group the existing group which founder wants to access the requests
	 * @return all request which are currently in the group or null if non exist
	 */
	private List<User> getRequestsByGroup(Group group) {
		// TODO - implement RequestSearchService.getRequestsByGroup
		throw new UnsupportedOperationException();
	}

}