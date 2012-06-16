package apiclient;

/**
 * Created by IntelliJ IDEA.
 * User: Jaroslav Málek
 * Date: 13.3.12
 * Time: 10:33
 * <p/>
 * Obecne error codes:
 * 2 : Invalid service - This service does not exist
 * 3 : Invalid Method - No method with that name in this package
 * 4 : Authentication Failed - You do not have permissions to access the service
 * 5 : Invalid format - This service doesn't exist in that format
 * 6 : Invalid parameters - Your request is missing a required parameter
 * 7 : Invalid resource specified
 * 8 : Operation failed - Something else went wrong
 * 9 : Invalid session key - Please re-authenticate
 * 10 : Invalid API key - You must be granted a valid key by last.fm
 * 11 : Service Offline - This service is temporarily offline. Try again later.
 * 13 : Invalid method signature supplied
 * 16 : There was a temporary error processing your request. Please try again
 * 26 : Suspended API key - Access for your account has been suspended, please contact Last.fm
 * 29 : Rate limit exceeded - Your IP has made too many requests in a short period
 */
public class LastFmException extends Exception {

    Integer code = null;

    String uri = null;

    public LastFmException() {
    }

    public LastFmException(String message) {
        super(message);
    }

    public LastFmException(String message, int code, String uri) {
        super(message);
        this.code = code;
        this.uri = uri;
    }

    public LastFmException(String message, Throwable cause) {
        super(message, cause);
    }

    public LastFmException(Throwable cause) {
        super(cause);
    }

    public Integer getCode() {
        return code;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        String msg = super.toString();
        msg += ", Code: " + code + "\n" + uri + "\n";
        return msg;
    }
}
