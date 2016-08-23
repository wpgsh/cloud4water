package net.wapwag.authn.ui;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.After;
import org.junit.Before;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.wapwag.authn.AuthenticationServiceImpl;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.util.OSGIUtil;

public abstract class BaseServletTest {
	
	private Server server;
	
	private final int port, maxServerThreads, acceptQueueSize;
	
	public BaseServletTest(int port, int maxServerThreads, int acceptQueueSize) {
		super();
		this.port = port;
		this.maxServerThreads = maxServerThreads;
		this.acceptQueueSize = acceptQueueSize;
	}

	static Server createServer(int port, int maxServerThreads, int acceptQueueSize, Servlet servlet) {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(maxServerThreads);
		threadPool.setName("JettyServer");
		threadPool.setStopTimeout(10000);
		threadPool.setDaemon(true);
		
		Server server = new Server(threadPool);
		NetworkTrafficServerConnector connector = new NetworkTrafficServerConnector(server);
		connector.setName("input");
		connector.setPort(port);
		connector.setIdleTimeout(1000);
		connector.setHost("0.0.0.0");
		connector.setStopTimeout(10000);
		connector.setAcceptQueueSize(acceptQueueSize);
		server.addConnector(connector);
		
		ServletHandler handler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder(servlet);
		handler.addServletWithMapping(servletHolder, "/*");
		server.setHandler(handler);
		
		return server;
	}
	
	protected abstract Servlet createServlet() throws Exception;
	
	protected abstract UserDao createUserDao() throws Exception;
	
	@Before
	public void setupServer() throws Exception {
		if (port >= 0) {		
			server = createServer(port, maxServerThreads, acceptQueueSize, createServlet());
			server.start();
		}
	}
	
	@Before
	public void setupAuthenticationService() throws Exception {
		AuthenticationServiceImpl authnService = new AuthenticationServiceImpl();
		authnService.setUserDao(createUserDao());		
		OSGIUtil.setAuthenticationService(authnService);		
	}
	
	@After
	public void shutdownServer() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
		}		
	}
	
	public static final String APPLICATION_X_WWW_FORM_URLENCODED =
			"application/x-www-form-urlencoded";
	
	public static final String APPLICATION_JSON =
			"application/json";
	
	public static final int SC_BAD_REQUEST = 400;
	public static final int SC_UNAUTHORIZED = 401;
	
	public static class JsonResponse {
		
		public final int responseCode;
		
		public final Map<String, Object> body;

		public JsonResponse(int responseCode, Map<String, Object> body) {
			super();
			this.responseCode = responseCode;
			this.body = body;
		}
		
	}
	
    public static JsonResponse postAcceptJson(
    		String _url,
    		boolean auth, String basicAuth, 
    		String contentType, String content) throws Exception {
        URL url = new URL(_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.addRequestProperty("content-type", contentType);
        if (auth) {
            conn.addRequestProperty("Authorization",
                    "Basic "+
                            Base64.encodeBase64String(basicAuth.getBytes()));
        }
        conn.connect();
        OutputStream out = conn.getOutputStream();
        out.write(content.getBytes());
        out.flush();
        
        int respCode = conn.getResponseCode();
        String respContentType = conn.getContentType();
        Map<String, Object> result;
        if (APPLICATION_JSON.equals(respContentType)) {
        	result = ImmutableMap.of();
        } else {
        	result = new Gson().fromJson(
                    new InputStreamReader(conn.getErrorStream()),
                    new TypeToken<Map<String, Object>>(){}.getType());
        }        
        conn.disconnect();

        return new JsonResponse(respCode, result);
    }	

}
