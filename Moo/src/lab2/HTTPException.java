package lab2;

public class HTTPException extends Exception {
	private static final long serialVersionUID = 1L;
	private StatusCode code;
	
	// Constructor
	public HTTPException(StatusCode code) {
		this.code = code;
	}

	// Constructor which converts the error code to enumeration.
	public HTTPException(int in) {
		for (StatusCode status : StatusCode.values()) {
			if (status.getCode() == in) {
				this.code = status;
				break;
			}
		}
	}

	public StatusCode getStatusCode() {
		return code;
	}
}
