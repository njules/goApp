package edu.kit.pse.gruppe1.goApp.server.model;

/**
 * Enumeration with all possible values for the status of a participant.
 */
public enum Status {
    /**
     * If an event is created all users of the group are connected to the event and have the status
     * invited
     */
    INVITED(0),

    /**
     * If an user clicks on participate he get the status participate
     */
    PARTICIPATE(1),

    /**
     * If the user clicks on started he get the status started
     */
    STARTED(2),
    
    /**
     * If a user decides not to participate in an event
     */
    REJECTED(3);

    private final Integer value;

    private Status(Integer value) {
        this.value = value;
    }

    /**
     * returns the Integer value of the enum literal to save the value in the DB
     * 
     * @return the Integer value of the enum literal
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Gives the Status to the Integer value saved in the DB
     * 
     * @param i
     *            the Integer value saved in the DB
     * @return the Status which corresponds to the Integer value
     */
    public static Status fromInteger(Integer i) {
        for (Status status : Status.values()) {
            if (status.getValue().equals(i)) {
                return status;
            }
        }
        return null;
    }
}
