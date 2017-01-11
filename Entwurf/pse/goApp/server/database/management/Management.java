package pse.goApp.server.database.management;

/**
 * Interface to use from all DB Management Classes
 */
public interface Management {

	/**
	 * Delete an entry with ID in DB
	 * @param ID ID from Entry to delete
	 * @return true, if deletion was successfull, otherwise false
	 */
	boolean delete(int ID);

	/**
	 * Constructor which initializes a DB Session
	 * @param dbInitializer DataBaseInitializier Object - Created from Factory
	 */
	void Management(DatabaseInitializer dbInitializer);

}