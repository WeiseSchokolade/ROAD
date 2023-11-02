package de.schoko.road.server.shared;

/**
 * Reasons for disconnections
 */
public enum DisconnectionReason {
	/**
	 * The disconnection is expected by both the client and the server.
	 */
	NORMAL(0, 200, "Normal"),
	/**
	 * The client uses a protocol version not supported by the server.
	 */
	DIFFERENT_PROTOCOL_VERSION(1, 400, "Different protocol version"),
	/**
	 * The value provided/requested is not known.
	 */
	UNKNOWN_VALUE(2, 404, "Unknown Value"),
	/**
	 * The client sent a packet that was recognized as not possible without modifications to the code.
	 */
	ILLEGAL_ACTION(3, 405, "Illegal Action"),
	/**
	 * The value provided in a packet cannot be applied.
	 */
	ILLEGAL_VALUE(4, 422, "Illegal Value"),
	/**
	 * A packet of a type not known by the client/server was sent.
	 */
	UNKNOWN_PACKET(5, 500, "Unknown Packet");
	
	private int id;
	private int code;
	private String message;

	private DisconnectionReason(int id, int code, String message) {
		this.id = id;
		this.code = code;
		this.message = message;
	}
	
	public int getId() {
		return id;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
}