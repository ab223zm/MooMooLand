package lab2;

import java.io.File;
import java.net.URLConnection;
import java.util.Date;

public class HTTPResponse {

	public enum ResponseType {
		RESPOND, CONTENT_TYPE, CONTENT_LENGTH, SERVER, DATE;
	};

	private StatusCode code;
	private String contentType;
	private long contentLength;
	private static final String CRLF = "\r\n";
	
	public HTTPResponse() {}

	
	// Reads a file and sets the content type and length	
	public void setFile(String path) {
		File file = new File(path);
		this.contentType = getContentType(file.getName());
		this.contentLength = file.length();

	}

	
	// Creates a response in string format
	public String getResponse(ResponseType[] inType) {
		StringBuilder sb = new StringBuilder();
		for (ResponseType rt : inType) {
			if (rt == ResponseType.RESPOND) {
				sb.append(code.toString());
			}
			if (rt == ResponseType.DATE) {
				sb.append("Date: " + new Date().toString() + CRLF);
			}
			if (rt == ResponseType.SERVER) {

				sb.append("Server: " + "Simple Web Server" + CRLF);
			}
			if (rt == ResponseType.CONTENT_TYPE) {

			}
			if (!(contentType == null)) {
				sb.append("Content-Type: " + contentType + CRLF);
			}
			if (rt == ResponseType.CONTENT_LENGTH) {

				sb.append("Content-Length: " + contentLength + CRLF);
			}

		}
		sb.append(CRLF);

		return sb.toString();
	}
	
	private String getContentType(String fileExtension) {
		return URLConnection.guessContentTypeFromName(fileExtension);
	}
	public void setResponse(StatusCode status) {
		this.code = status;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}	
}
