package edu.kit.pse.gruppe1.goApp.server.database.management;

/**
 * Interface to use from all DB Management Classes
 */
public interface Management {

    /**
     * Delete an entry with ID in DB
     * 
     * @param iD
     *            ID from Entry to delete
     * @return true, if deletion was successfull, otherwise false
     */
    boolean delete(int iD);

}