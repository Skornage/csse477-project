package server;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import protocol.HttpResponseFactory;

/**
 * 
 * @author Chandan R. Rupakheti (rupakhcr@clarkson.edu)
 */
public class Cache {

	private HashMap<String, Integer> numberOfRequests = new HashMap<String, Integer>();
	private HashMap<String, String> cache = new HashMap<String, String>();
	private HashMap<String, Date> lastModified = new HashMap<String, Date>();
	private HashMap<String, Long> contentLength = new HashMap<String, Long>();

	public boolean contains(String requestURI) {
		return cache.containsKey(requestURI);
	}

	public double size() {
		return cache.size();
	}

	public void add(String requestURI, File file) {
		if (cache.size() < 1000) {
			String content = HttpResponseFactory
					.getFileContentsForResponseBody(file);
			numberOfRequests.put(requestURI, 1);
			contentLength.put(requestURI, file.length());
			cache.put(requestURI, content);
			Date d = new Date();
			lastModified.put(requestURI, d);
		}
	}

	public void remove(String requestURI) {
		numberOfRequests.remove(requestURI);
		contentLength.remove(requestURI);
		cache.remove(requestURI);
		lastModified.remove(requestURI);
	}

	public int getNumberRequests(String requestURI) {
		return numberOfRequests.get(requestURI);
	}

	public String getContent(String requestURI) {
		return cache.get(requestURI);
	}

	public Date getLastModified(String requestURI) {
		return lastModified.get(requestURI);
	}

	public Long getContentLength(String requestURI) {
		return contentLength.get(requestURI);
	}
}
