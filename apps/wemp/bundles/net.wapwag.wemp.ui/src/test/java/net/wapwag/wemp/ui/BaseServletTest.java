package net.wapwag.wemp.ui;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.wapwag.wemp.WaterEquipmentServiceImpl;
import net.wapwag.wemp.dao.WaterEquipmentDao;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.After;
import org.junit.Before;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EnumSet;
import java.util.Map;

@SuppressWarnings("Duplicates")
public abstract class BaseServletTest {
	
	private Server server;
	
	private final int port, maxServerThreads, acceptQueueSize;
	
	public BaseServletTest(int port, int maxServerThreads, int acceptQueueSize) {
		super();
		this.port = port;
		this.maxServerThreads = maxServerThreads;
		this.acceptQueueSize = acceptQueueSize;
	}

	static Server createServer(int port, int maxServerThreads, int acceptQueueSize, Filter filter, Servlet servlet) {
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

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		ServletHolder servletHolder = new ServletHolder(servlet);
        handler.addServlet(servletHolder, "/*");
        if (filter != null) {
            handler.addFilter(new FilterHolder(filter), "/*", EnumSet.of(DispatcherType.REQUEST,
                    DispatcherType.FORWARD, DispatcherType.ERROR, DispatcherType.INCLUDE));
        }

        server.setHandler(handler);

		return server;
	}

	protected abstract Filter createFilter() throws Exception;

	protected abstract Servlet createServlet() throws Exception;
	
	protected abstract WaterEquipmentDao createWaterEquipmentDao() throws Exception;
	
	@Before
	public void setupServer() throws Exception {
		if (port >= 0) {		
			server = createServer(port, maxServerThreads, acceptQueueSize, createFilter(), createServlet());
			server.start();
		}
	}
	
	@Before
	public void setupAuthenticationService() throws Exception {
		WaterEquipmentServiceImpl waterEquipmentService = new WaterEquipmentServiceImpl();
		waterEquipmentService.setWaterEquipmentDao(createWaterEquipmentDao());		
		OSGIUtil.setWaterEquipmentService(waterEquipmentService);		
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
    public static final int SC_FOUND = 302;

	public static class QueryComponentResponse {

		public final int responseCode;

		public final Map<String, Object> body;

		public QueryComponentResponse(int responseCode, Map<String, Object> body) {
			this.responseCode = responseCode;
			this.body = body;
		}
	}

	public static class JsonResponse {
		
		public final int responseCode;
		
		public final Map<String, Object> body;

		public JsonResponse(int responseCode, Map<String, Object> body) {
			super();
			this.responseCode = responseCode;
			this.body = body;
		}
		
	}

	public static QueryComponentResponse getAcceptQueryComponent(String _url,String contentType) throws Exception {
        URL url = new URL(_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("content-type", contentType);
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        int respCode = conn.getResponseCode();
        Map<String, Object> result = Maps.newHashMap();
        result.put("redirectURI", conn.getHeaderField("Location"));
        System.out.println(conn.getHeaderFields());
        if (respCode == HttpServletResponse.SC_FOUND) {
            result.put("redirectURI", conn.getHeaderField("Location"));
        }

        result.put("statusCode", respCode);

        conn.disconnect();

        return new QueryComponentResponse(respCode, result);
    }

    public static QueryComponentResponse getAcceptQueryComponent(String _url, boolean auth, String basicAuth, String contentType, Type type) throws Exception {
        URL url = new URL(_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("content-type", contentType);
        if (auth) {
            conn.addRequestProperty("Authorization",
                    "Bearer "+ basicAuth);
        }
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        int respCode = conn.getResponseCode();
        Map<String, Object> result = Maps.newHashMap();
        System.out.println(conn.getHeaderFields());
        if (respCode == HttpServletResponse.SC_OK) {
            result.put("result", new Gson().fromJson(new InputStreamReader(conn.getInputStream()), type));
        }

        result.put("statusCode", respCode);

        conn.disconnect();

        return new QueryComponentResponse(respCode, result);
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
        Map<String, Object> result;
        if (respCode == SC_UNAUTHORIZED) {
            result = ImmutableMap.of();
        } else if (respCode == SC_BAD_REQUEST) {
            result = new Gson().fromJson(
                    new InputStreamReader(conn.getErrorStream()),
                    new TypeToken<Map<String, Object>>(){}.getType());
        } else {
            result = new Gson().fromJson(
                    new InputStreamReader(conn.getInputStream()),
                    new TypeToken<Map<String, Object>>(){}.getType());
        }
        conn.disconnect();

        return new JsonResponse(respCode, result);
    }	

}
