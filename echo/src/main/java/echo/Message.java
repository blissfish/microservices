package echo;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class Message {

	private final long id;
	private String message;
	private String version;
	private String hostname;

	public Message(long id, String message, String version) {
		this.id = id;
		this.message = message;
		this.version = version;
		this.hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public String getVersion() {
		return version;
	}

	public String getMessage() {
		if ("0.0.2".equals(version)) {
			message += " Rock da cloud...";
		}
		return message;
	}

	public String getHostName() {
		return hostname;
	}

	public long getId() {
		return id;
	}

	public String toString() {
		return "{\"id \":" + id + ",\"content\":\"" + message + "\",\"version\":\"" + version + "\",\"hostName\":\""
				+ hostname + "\"}\"";
	}

}
