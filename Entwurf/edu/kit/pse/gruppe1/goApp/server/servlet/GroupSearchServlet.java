package edu.kit.pse.gruppe1.goApp.server.servlet;

/**
 * This servlet offers methods to search for groups with a specific name or in which a given user is the member.
 */
public class GroupSearchServlet {

	/**
	 * Searches for all groups whose name begins with the given string.
	 * @param json The JSON object contains the string that is searched for in the groups names.
	 * @return Returns a JSON string containing a list of all the groups associated with this name.
	 */
	private String getGroupsByName(JSONObject json) {
		// TODO - implement GroupSearchServlet.getGroupsByName
		throw new UnsupportedOperationException();
	}

	/**
	 * Searches for all groups the given user is a member of.
	 * @param json The JSON object contains the user that is a member in the groups searched for.
	 * @return Returns a JSON string containing a list of all the groups in which the given user is a member.
	 */
	private String getGroupsByMember(JSONObject json) {
		// TODO - implement GroupSearchServlet.getGroupsByMember
		throw new UnsupportedOperationException();
	}

}