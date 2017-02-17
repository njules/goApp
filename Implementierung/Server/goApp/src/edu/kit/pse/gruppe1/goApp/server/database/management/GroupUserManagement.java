package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.util.ArrayList;
import java.util.List;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.User;

/**
 * Manages GroupUser Table
 */
public class GroupUserManagement {

    /**
     * Constructor
     */
    public GroupUserManagement() {
        super();
    }

    /**
     * creates new entry
     * 
     * @param groupId
     *            groupID to combine with user
     * @param userId
     *            userID to combine with group
     * @return ID of new entry
     */
    public boolean add(int groupId, int userId) {
        User user = new UserManagement().getUser(userId);
        Group group = new GroupManagement().getGroup(groupId);
        if (user == null || group == null) {
            return false;
        }
        if (!group.addUser(user)) {
            return false;
        }
        return new GroupManagement().update(group);
    }

    /**
     * get all User to given Group
     * 
     * @param groupId
     *            ID of group
     * @return List of matching User
     */
    public List<User> getUsers(int groupId) {
        Group group = new GroupManagement().getGroup(groupId);
        if (group == null) {
            return null;
        }
        List<User> users = new ArrayList<>();
        users.addAll(group.getUsers());
        return users;
    }

    /**
     * get all groups to given User
     * 
     * @param userId
     *            ID of User
     * @return List of matching Groups
     */
    public List<Group> getGroups(int userId) {
        User user = new UserManagement().getUser(userId);
        if (user == null) {
            return null;
        }
        List<Group> groups = new ArrayList<>();
        groups.addAll(user.getGroups());
        return groups;
    }

    /**
     * deletes entry in table with given GroupID and userID
     * 
     * @param groupId
     *            groupID to delete (in combination)
     * @param userId
     *            userID to delete (in combination)
     * @return true, if successful, otherwise false
     */
    public boolean delete(int groupId, int userId) {
        Group group = new GroupManagement().getGroup(groupId);
        if (group == null) {
            return false;
        }
        if (!group.removeUser(userId)) {
            return false;
        }
        return new GroupManagement().update(group);
    }
}