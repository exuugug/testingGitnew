package websocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.CharBuffer;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;

public class HelloMessageInbound extends StreamInbound {

	private String WS_NAME;
	private final String FORMAT = "%s : %s";
	private final String PREFIX = "ws_";
	private String sessionId = "";

	public HelloMessageInbound(int id, String _sessionId) {
		this.WS_NAME = PREFIX + id;
		this.sessionId = _sessionId;
	}

	@Override
	protected void onTextData(Reader reader) throws IOException {
		char[] chArr = new char[1024];
		int len = reader.read(chArr);
		send(String.copyValueOf(chArr, 0, len));
	}

	@Override
	protected void onClose(int status) {
		System.out.println(String.format(FORMAT, WS_NAME, "closing ......"));
		super.onClose(status);
		HelloWebSocketServlet.getSocketList().remove(this);
	}

	@Override
	protected void onOpen(WsOutbound outbound) {
		super.onOpen(outbound);
		try {
			send("hello, my name is " + WS_NAME);
			send("session id = " + this.sessionId);
			HelloWebSocketServlet.getSocketList().add(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void send(String message) throws IOException {
		message = String.format(FORMAT, WS_NAME, message);
		System.out.println(message);
		for (HelloMessageInbound tempInbound : HelloWebSocketServlet
				.getSocketList()) {
			WsOutbound outbound = tempInbound.getWsOutbound();
			outbound.writeTextMessage(CharBuffer.wrap(message));
		}
		// getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
	}

	@Override
	protected void onBinaryData(InputStream arg0) throws IOException {
	}
}