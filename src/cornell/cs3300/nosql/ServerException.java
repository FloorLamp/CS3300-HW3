package cornell.cs3300.nosql;

/**
 * Exception thrown when an error has occured on the server
 */
public class ServerException extends Exception {
	private static final long serialVersionUID = 1L;

	public static enum ErrorType {
		/**
		 * TODO: put your error types here
		 */
	}

	private ErrorType error;

	public ServerException() {
	}

	public ServerException(ErrorType errorType, String message) {
		super(message);
		this.error = errorType;
	}

	public ServerException(ErrorType errorType, String message, Throwable cause) {
		super(message, cause);
		this.error = errorType;
	}

	public ErrorType getErrorType() {
		return error;
	}

	@Override
	public String toString() {
		return error + " - " + super.toString();
	}
}