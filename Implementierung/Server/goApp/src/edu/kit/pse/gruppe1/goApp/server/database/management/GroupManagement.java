package edu.kit.pse.gruppe1.goApp.server.database.management;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.User;

/**
 * Manages Group Table
 */
public class GroupManagement implements Management {

    /**
     * Constructor
     */
    public GroupManagement() {
        super();
    }

    /**
     * creates new Group and adds new entry to table
     * 
     * @param name
     *            Name of Group
     * @param founderId
     *            id of the founder (User) of Group
     * @return created Group
     */
    public Group add(String name, int founderId) {
        User founder = new UserManagement().getUser(founderId);
        if (founder == null) {
            return null;
        }
        Group group = new Group(name, founder);
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.save(group);
        session.getTransaction().commit();
        return group;
    }

    /**
     * updates entry in table
     * 
     * @param chGroup
     *            Group with changed attributes
     * @return true, if update was successfull, otherwise false
     */
    public boolean update(Group chGroup) {
        if (chGroup == null || chGroup.getGroupId() == null) {
            return false;
        }
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.update(chGroup);
        session.getTransaction().commit();
        return true;
    }

    /**
     * updates name of entry
     * 
     * @param groupId
     *            ID of entry to change
     * @param newName
     *            Name to set
     * @return true, if update was successfull, otherwise false
     */
    public boolean updateName(int groupId, String newName) {
        Group group = getGroup(groupId);
        if (group == null) {
            return false;
        }
        group.setName(newName);
        return update(group);
    }

    /**
     * updates founder of given entry
     * 
     * @param groupId
     *            ID of entry to change
     * @param newFounder
     *            Founder to set
     * @return true if success, otherwise false
     */
    public boolean updateFounder(int groupId, User newFounder) {
        Group group = getGroup(groupId);
        if (group == null || newFounder == null) {
            return false;
        }
        group.setFounder(newFounder);
        return update(group);
    }

    /**
     * returns Group with given GruopID
     * 
     * @param groupId
     *            ID of Group to get
     * @return a Group
     */
    public Group getGroup(int groupId) {
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        Group group = (Group) session.get(Group.class, groupId);
        session.getTransaction().commit();
        return group;
    }

    /**
     * get all Groups with given Name
     * 
     * @param searchName
     *            name to search for
     * @return List of matching Groups
     */
    public List<Group> getGroupsByName(String searchName) {
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Group> groups = session.createCriteria(Group.class)
                .add(Restrictions.ilike("name", searchName, MatchMode.ANYWHERE))
                .addOrder(Property.forName("name").asc()).setMaxResults(50).list();
        session.getTransaction().commit();
        return groups;
    }

    /**
     * return all Events which are connected with given Group
     * 
     * @param groupId
     *            GroupID to search Events for
     * @return List of Events
     */
    public List<Event> getEvents(int groupId) {
        Group group = getGroup(groupId);
        if (group == null) {
            return null;
        }
        List<Event> events = new ArrayList<>();
        events.addAll(group.getEvents());
        return events;
    }

    @Override
    public boolean delete(int groupId) {
        Group group = getGroup(groupId);
        if (group == null) {
            return false;
        }
        Session session = DatabaseInitializer.getFactory().getCurrentSession();
        session.beginTransaction();
        session.delete(group);
        session.getTransaction().commit();
        return true;
    }

}