package service0;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class Message {

	private final long id;
	private String message;
	private String hostname;

	public Message(long id, String message) {
		this.id = id;
		this.message = message;
		this.hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public String getMessage() {
		return message;
	}

	public String getHostName() {
		return hostname;
	}

	public long getId() {
		return id;
	}

	public String toString() {
		return "{\"id \":" + id + ",\"content\":\"" + message + ",hostName\":\""
				+ hostname + "\"}\"";
	}

}
