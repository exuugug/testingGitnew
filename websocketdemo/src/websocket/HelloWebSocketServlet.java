package websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

public class HelloWebSocketServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;

	private final AtomicInteger connectionIds = new AtomicInteger(0);
	private static List<HelloMessageInbound> socketList = new ArrayList<HelloMessageInbound>();

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,
			HttpServletRequest request) {
		return new HelloMessageInbound(connectionIds.getAndIncrement(), request
				.getSession().getId());
	}

	public static synchronized List<HelloMessageInbound> getSocketList() {
		return socketList;
	}
}
