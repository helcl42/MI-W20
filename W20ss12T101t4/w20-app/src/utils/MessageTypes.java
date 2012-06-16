package utils;

/**
 * Created by IntelliJ IDEA.
 * User: lubos
 * Date: 3/12/12
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public enum MessageTypes {
    SNAPSHOT_ERROR("Error"),
    SNAPSHOT_OK("Snapshot finished");

    private String message;

    private MessageTypes(String type) {
        this.message = type;
    }

    public String toString() {
        return message;
    }
}
