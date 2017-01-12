package edu.kit.pse.gruppe1.goApp.client.controler.service;

import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * This Service is needed to list various Groups at once.
 */
public class GroupSearchService {

	/**
	 * finds all groups which the user is a member of. This is used to present the groups in the StartActivity
	 * @param user the user which groups are returned
	 * @return all groups the user is member of or null
	 */
	private List<edu.kit.sdqweb.pse.gruppe1.goApp.client.model.Group> getGroupsByMember(User user) {
		// TODO - implement GroupSearchService.getGroupsByMember
		throw new UnsupportedOperationException();
	}

	/**
	 * finds all groups which name include the given string to show the results of a search request by the user using the search function of the NewGroupActivity
	 * @param name the string which the user typed in the NewGroupActivity to find a new group he wants to be member of with that name
	 * @return all groups the name is included in the group name or null
	 */
	private List<edu.kit.sdqweb.pse.gruppe1.goApp.client.model.Group> getGroupsByName(String name) {
		// TODO - implement GroupSearchService.getGroupsByName
		throw new UnsupportedOperationException();
	}

}