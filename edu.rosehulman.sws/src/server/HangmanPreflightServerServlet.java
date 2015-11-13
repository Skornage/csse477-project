package server;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class HangmanPreflightServerServlet extends AbstractPluginServlet {
	private GameManager mgr;

	public HangmanPreflightServerServlet(GameServer server) {
		this.mgr = server.getGameManager();
	}

	@Override
	public String getPluginURI() {
		return "hangman";
	}

	@Override
	public String getRequestType() {
		return Protocol.PUT;
	}

	@Override
	public String getServletURI() {
		return "preflight";
	}

	@Override
	public HttpResponse HandleRequest(HttpRequest request) {
		HttpResponse response = new HttpResponse(Protocol.VERSION,
				Protocol.OK_CODE, Protocol.OK_TEXT, null);

		response.put("Access-Control-Allow-Methods", "PUT");
		response.put("Access-Control-Allow-Origin", "*");
		response.put("Access-Control-Allow-Headers", "preflightTest");
		return response;
	}
}
