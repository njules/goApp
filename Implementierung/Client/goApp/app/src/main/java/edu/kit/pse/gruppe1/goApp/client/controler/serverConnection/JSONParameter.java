package edu.kit.pse.gruppe1.goApp.client.controler.serverConnection;

/**
 * Enumerations with all possible parameter-types for the JSON-strings.
 */
public enum JSONParameter {
  
    // TODO: String weg machen ->�berfl�ssig?
    // TODO: Gro�buchstaben -> mit Katha vorher abkl�ren -> wird zu Fehlern bei ihr f�hren.
    /**
     * 
     * ID of the request
     * 
     */

    ID("ID"),

    /**
     * 
     * ID of an user.
     * 
     */

    USER_ID("UserID"),

    /**
     * 
     * ID of a group.
     * 
     */

    GRUOP_ID("GroupID"),

    /**
     * 
     * ID of an event.
     * 
     */

    EVENT_ID("EventID"),

    /**
     * 
     * Name of an user.
     * 
     */

    USER_NAME("UserName"),

    /**
     * 
     * Name of a group.
     * 
     */

    GROUP_NAME("GroupName"),

    /**
     * 
     * Name of an event.
     * 
     */

    EVENT_NAME("EventName"),

    /**
     * 
     * Time when the event starts
     * 
     */

    EVENT_TIME("EventTime"),

    /**
     * 
     * Longitude of an location
     * 
     */

    LONGITUDE("Longitude"),

    /**
     * Latitude of an location
     * 
     */

    LATITUDE("Latitude"),

    /**
     * name of an location
     */

    LOC_NAME("LocationName"),

    /**
     * method (from enum Methods)
     */

    METHOD("Method"),

    /**
     * ErrorCode (from enum ErrorCodes)
     */

    ERROR_CODE("ErrorCode"),

    /**
     * List of User
     */
    LIST_USER("ListUser"),

    /**
     * List of Group
     */

    LIST_GROUP("ListGroup"),

    /**
     * List of Group
     */
    LIST_EVENT("ListEvent"),

    /**
     * Google ID
     */
    GOOGLE_ID("GoogleId");

    private final String fieldDescription;

    private JSONParameter(String description) {
        fieldDescription = description;
    }

    /**
     * Gives the corresponding name to an enum literal. Normally something like the enum literal
     * name.
     */
    @Override
    public String toString() {
        return fieldDescription;
    }

    /**
     * Gives the corresponding enum literal to a string.
     *
     * @param s
     *            the string to which the returned JSONParameter should match
     * @return the JSONParameter to the string s
     */
    public static JSONParameter fromString(String s) {
        for (JSONParameter json : JSONParameter.values()) {
            if (json.toString().equals(s)) {
                return json;
            }
        }
        return null;
    }

    // TODO: JavaDocs
    public enum ErrorCodes {
        OK(0, "No error"),

        READ_JSON(1, "JSON could not be read."),

        WRITE_JSON(2, "JSON could not be wrote."),

        EMPTY_JSON(3, "Empty JSON String"),

        DB_ERROR(4, "Error in Database."),

        USR_LIMIT(5, "User limit was reached."),

        GRP_LIMIT(6, "Group limit was reached."),

        IO_ERROR(7, "Error with IO Methods."),

        ALGO_ERROR(8, "Error while processing Clusering."),


        METH_ERROR(9, "Method not found."),

        CONNECTION_FAILED(10, "Connection to server failed.");


        private final String fieldDescription;
        private final int errCode;

        private ErrorCodes(int code, String description) {
            fieldDescription = description;
            errCode = code;
        }

        // TODO: JavaDocs
        @Override
        public String toString() {
            return fieldDescription;
        }

        // TODO: JavaDocs
        public int getErrorCode() {
            return errCode;
        }

        // TODO: JavaDocs
        public static ErrorCodes fromString(String s) {
            for (ErrorCodes err : ErrorCodes.values()) {
                if (err.toString().equals(s)) {
                    return err;
                }
            }
            return null;
        }
        
        //TODO: JavDocs
        public static ErrorCodes fromErCode(int i){
            for (ErrorCodes err : ErrorCodes.values()) {
                if (err.getErrorCode() == i) {
                    return err;
                }
            }
            return null;
        }

    }

    // TODO: JavaDocs
    public enum Methods {
        /**
         * Event.create Group.create Request.create
         */
        CREATE,

        /**
         * Event.change User.changeName
         */
        CHANGE,

        /**
         * User.getUser
         */
        GET_USER,

        /**
         * Event.getEvent Group.getEvents
         */
        GET_EVENT,

        /**
         * Go.getStartedParticpants
         */
        GET_START,

        /**
         * Group.getGroup
         */
        GET_GROUP,

        /**
         * GroupSearch.getGroupsByName
         */
        GET_GRP_NAME,

        /**
         * GroupSearch.getGroupsByMember
         */
        GET_GRP_MEM,

        /**
         * Location.getCluster
         */
        GET_CLUSTER,

        /**
         * RequestSearchServlet.getRequestsByUser
         */
        GET_REQ_USR,

        /**
         * RequestSearchServlet.getRequestsByGroup
         */
        GET_REQ_GRP,

        /**
         * Group.delete
         */
        DELETE,

        /**
         * Group.deleteMember
         */
        DEL_MEM,

        /**
         * Group.setName
         */
        SET_NAME,

        /**
         * Go.setStarted
         */
        SET_START,

        /**
         * Group.setFounder
         */
        SET_FOUNDER,

        /**
         * Location.setGPS
         */
        SET_GPS,

        /**
         * Login.register
         */
        REGISTER,

        /**
         * Login.login
         */
        LOGIN,

        /**
         * Participate.accept Request.accept
         */
        ACCEPT,

        /**
         * Participate.reject Request.reject
         */
        REJECT,

        /**
         * No Method was found - equals null, but does not produce NullPointerException
         */
        NONE;

        // TODO: JavaDocs
        public static Methods fromString(String s) {
            for (Methods meth : Methods.values()) {
                if (meth.toString().equals(s)) {
                    return meth;
                }
            }
            return NONE;
        }
    }

}