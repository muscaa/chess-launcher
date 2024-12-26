package muscaa.chess.launcher.version;

public class VersionException extends Exception {
	
	private static final long serialVersionUID = 5283216401298727198L;
	
	public VersionException() {
        super();
    }
	
    public VersionException(String message) {
        super(message);
    }
    
    public VersionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public VersionException(Throwable cause) {
        super(cause);
    }
}
