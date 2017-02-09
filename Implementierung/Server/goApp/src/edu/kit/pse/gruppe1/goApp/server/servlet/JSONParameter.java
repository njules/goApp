package edu.kit.pse.gruppe1.goApp.server.servlet;

/**
 * Enumerations with all possible parameter-types for the JSON-strings.
 */
public enum JSONParameter {
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

    GROUP_ID("GroupID"),

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
     * List of Users
     */
    LIST_USER("ListUser"),

    /**
     * List of Groups
     */
    LIST_GROUP("ListGroup"),

    /**
     * List of Events
     */
    LIST_EVENT("ListEvent"),

    /**
     * List of Locations
     */
    LIST_LOC("ListLocation"),

    /**
     * List of Participates (UserID & Status)
     */
    LIST_PART("ListParticipate"),

    /**
     * Status of USer in Event
     */
    STATUS("Status"),

    /**
     * Accepted Events
     */
    ACC_Events("AcceptedEvents"),

    /**
     * New Events
     */
    NEW_EVENTS("NewEvents"),

    /**
     * Google Token
     */
    GOOGLE_TOKEN("GoogleToken");

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

    /**
     * enum with status codes
     *
     */
    public enum Status {
        REJECT(1), 
        
        ACCEPT(2), 
        
        GO(3);

        private final int status;

        private Status(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    /**
     * Enum of all possible ErrorCodes
     *
     */
    public enum ErrorCodes {
        /**
         * No error occured
         */
        OK(0, "No error"),

        /**
         * Error while reading JSONObject
         */
        READ_JSON(1, "JSON could not be read."),

        /**
         * Error while writing JSONObject
         */
        WRITE_JSON(2, "JSON could not be wrote."),

        /**
         * JSONObject was empty
         */
        EMPTY_JSON(3, "Empty JSON String"),

        /**
         * Error while database interaction or in database
         */
        DB_ERROR(4, "Error in Database."),

        /**
         * too many user in one group, user limit was reached
         */
        USR_LIMIT(5, "User limit was reached."),

        /**
         * too many groups for one user, group limit was reached
         */
        GRP_LIMIT(6, "Group limit was reached."),

        /**
         * Error while IO operations
         */
        IO_ERROR(7, "Error with IO Methods."),

        /**
         * Error while clustering locations
         */
        ALGO_ERROR(8, "Error while processing Clusering."),

        /**
         * Method is not valid
         */
        METH_ERROR(9, "Method not found.");

        private final String fieldDescription;
        private final int errCode;

        private ErrorCodes(int code, String description) {
            fieldDescription = description;
            errCode = code;
        }

        /**
         * return String of Enum
         */
        @Override
        public String toString() {
            return fieldDescription;
        }

        /**
         * gets Error Code to enum value
         * 
         * @return error code
         */
        public int getErrorCode() {
            return errCode;
        }

        /**
         * get enum value from String
         * 
         * @param s
         *            String from enum
         * @return enum value or null, if none exists with given String
         */
        public static ErrorCodes fromString(String s) {
            for (ErrorCodes err : ErrorCodes.values()) {
                if (err.toString().equals(s)) {
                    return err;
                }
            }
            return null;
        }

        /**
         * get enum value from error code
         * 
         * @param i
         *            error code
         * @return enum value or null, if none exists with given String
         */
        public static ErrorCodes fromErCode(int i) {
            for (ErrorCodes err : ErrorCodes.values()) {
                if (err.getErrorCode() == i) {
                    return err;
                }
            }
            return null;
        }

    }

    /**
     * Enum with all possible methods
     *
     */
    public enum Methods {
        /**
         * Event.create Group.create Request.create
         */
        CREATE,

        /**
         * Event.change User.changeName
         */
        CHANGE,

        // /**
        // * User.getUser
        // */
        // GET_USER,

        /**
         * Event.getEvent Group.getEvents
         * 
         */
        GET_EVENT,

        // /**
        // * Go.getStartedParticpants
        // */
        // GET_START,

        /**
         * Group.getGroup TODO: Methode umbenenen
         */
        GET_MEMBERS,

        /**
         * GroupSearch.getGroupsByName
         */
        GET_GRP_NAME,

        /**
         * GroupSearch.getGroupsByMember
         */
        GET_GRP_MEM,

        // /**
        // * Location.getCluster
        // */
        // GET_CLUSTER,

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
        @Deprecated
        SET_START,

        /**
         * Group.setFounder
         */
        SET_FOUNDER,

        /**
         * Location TODO: kombinierte Methode
         */
        SYNC_LOC,

        /**
         * Participate.setStatus
         * replaces accept, reject and set_start
         */
        SET_STATUS,

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
        @Deprecated
        ACCEPT,

        /**
         * Participate.reject Request.reject
         */
        @Deprecated
        REJECT,

        /**
         * No Method was found - equals null, but does not produce NullPointerException
         */
        NONE;

        /**
         * get enum value from given string
         * 
         * @param s
         *            string to search for
         * @return enum value or NONE, if no method existed with given string
         */
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