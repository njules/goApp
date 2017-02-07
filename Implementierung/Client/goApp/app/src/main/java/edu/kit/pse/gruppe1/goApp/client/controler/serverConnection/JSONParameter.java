package edu.kit.pse.gruppe1.goApp.client.controler.serverConnection;

import android.support.annotation.Nullable;
/**
 * Enumerations with all possible parameter-types for the JSON-strings.
 */
public enum JSONParameter {
    // TODO: Großbuchstaben
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

    UserID("UserID"),

    /**
     * 
     * ID of a group.
     * 
     */

    GroupID("GroupID"),

    /**
     * 
     * ID of an event.
     * 
     */

    EventID("EventID"),

    /**
     * 
     * Name of an user.
     * 
     */

    UserName("UserName"),

    /**
     * 
     * Name of a group.
     * 
     */

    GroupName("GroupName"),

    /**
     * 
     * Name of an event.
     * 
     */

    EventName("EventName"),

    /**
     * 
     * Time when the event starts
     * 
     */

    EventTime("EventTime"),

    /**
     * 
     * Longitude of an location
     * 
     */

    Longitude("Longitude"),

    /**
     * Latitude of an location
     * 
     */

    Latitude("Latitude"),

    /**
     * name of an location
     */

    LocationName("LocationName"),

    /**
     * method (from enum Methods)
     */

    Method("Method"),

    /**
     * ErrorCode (from enum ErrorCodes)
     */

    ErrorCode("ErrorCode");

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

        ALGO_Error(8, "Error while processing Clusering."),

        CONNECTION_FAILED(9, "Connection to server failed.");


        private final String fieldDescription;
        private final int errCode;

        private ErrorCodes(int code, String description) {
            fieldDescription = description;
            errCode = code;
        }

        //TODO: JavaDocs
        @Override
        public String toString() {
            return fieldDescription;
        }

        //TODO: JavaDocs
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
        REJECT;

        // TODO: JavaDocs
        public static Methods fromString(String s) {
            for (Methods meth : Methods.values()) {
                if (meth.toString().equals(s)) {
                    return meth;
                }
            }
            return null;
        }
    }

}