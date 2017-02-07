package garmter.com.camtalk.network;

public class LoginException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LoginException(String msg) {
        super(msg);
    }
    public LoginException(String msg, Throwable t) {
        super(msg, t);
    }
}
