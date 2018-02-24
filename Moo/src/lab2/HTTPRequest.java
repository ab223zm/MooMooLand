package lab2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class HTTPRequest {

	private static final String CRLF = "\r\n";
	private MethodType method;
	private String path;
	private String protocol;
	private String header;
	private long contentLength = 0;

	public enum MethodType {
		GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH;
	}

	/*
	 * Constructor that parses the header
	 * throws HTTPException if request is invalid
	 */
	public HTTPRequest(BufferedReader reader) throws HTTPException, IOException {
		try {
			parseHeader(readHeader(reader));
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new HTTPException(500);
		} catch (IllegalArgumentException e) {
			throw new HTTPException(500);
		} catch (NullPointerException e) {
			throw new HTTPException(500);
		}
	}

	/*
	 * This method parses the request header. 
	 * throws HTTPException.
	 */
	private void parseHeader(String head) throws HTTPException {

		try {
			// Split the String by tabs and new line
			String[] parts = head.split(CRLF);
			String firstLine = parts[0];
			// Separate the first line by spaces
			String[] request = firstLine.split("\\s");
			//If lenght is not 3, throws bad request
			if (request.length != 3) {
				throw new HTTPException(400);
			}
			// Update method, path and protocol 
			this.method = MethodType.valueOf(request[0]);
			this.path = request[1];
			this.protocol = request[2];
			// check if it is 1.1
			if (!request[2].equals("HTTP/1.1")) {
				throw new HTTPException(400);
			}

		} catch (Exception e) {
			throw new HTTPException(400);
		}
	}

	/*
	 * Reads the header and stops when the line is null or empty. After each header property a new
	 * tab and new line is added.
	 */
	private String readHeader(BufferedReader reader) throws IOException, NumberFormatException {

		StringBuilder line = new StringBuilder();

		while (true) {
			String lineRead = reader.readLine();

			if (lineRead == null || lineRead.equals("\r\n") || lineRead.isEmpty() || lineRead.equals("")) {
				break;
			}

			line.append(lineRead);
			line.append("\r\n");

			//Substring is 16 because "Content-Length: ".length() = 16.
			if (lineRead.startsWith("Content-Length")) {
				contentLength = Integer.parseInt(line.substring(16));
			}
		}

		return line.toString();
	}

	/*
	 * Changes url into absolute path of the file in server
	 * throws HTTPException if the file does not exist
	 */
	public String getUrl(String path) throws HTTPException {
		String[] nope = { "NOPE" };
		String[] redirect = { "main" };

		if (path == null)
			throw new HTTPException(StatusCode.BadRequest);

		String allPath = Server.contentPath + path;

		if (new File(allPath).isDirectory()) {
			if (allPath.charAt(allPath.length() - 1) != '/') {
				throw new HTTPException(StatusCode.Found); //FIXME redirect maybe? throws E when http://localhost:4950//imagesS
			}

			String temp = allPath + "index.htm";
			if (new File(temp).exists())
				return temp;
			temp = allPath + "index.html";
			if (new File(temp).exists())
				return temp;
		}

		if (new File(allPath).exists()) {
			return allPath;
		}

		for (String str : nope) {
			if (path.contains(str)) {
				throw new HTTPException(403);
			}
		}

		for (String str : redirect) {
			if (path.contains(str))
				throw new HTTPException(StatusCode.Found); //FIXME redirect
		}
		//don't think that is correct
		throw new HTTPException(StatusCode.NotFound);
	}

	/*
	 * Returns the method used for request as enum RequestType
	 */
	public MethodType getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}
}
