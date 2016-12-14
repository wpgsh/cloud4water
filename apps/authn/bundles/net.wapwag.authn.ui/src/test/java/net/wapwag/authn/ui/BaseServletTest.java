package net.wapwag.authn.ui;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.After;
import org.junit.Before;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
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
	
	protected abstract UserDao createUserDao() throws Exception;
	
	@Before
	public void setupServer() throws Exception {
		if (port >= 0) {		
			server = createServer(port, maxServerThreads, acceptQueueSize, createFilter(), createServlet());
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
	
    private static final String BOUNDARY = "----------HV2ymHFg03ehbqgZCaKO6jyH";  
	
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
	
    public QueryComponentResponse sendHttpPostRequest(String serverUrl,  
            ArrayList<FormFieldKeyValuePair> generalFormFields,  
            ArrayList<UploadFileItem> filesToBeUploaded) throws Exception {
        URL url = new URL(serverUrl);  
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
        connection.setDoOutput(true);  
        connection.setDoInput(true);  
        connection.setUseCaches(false);  
        connection.setRequestMethod("POST");  
        connection.setRequestProperty("Connection", "Keep-Alive");  
        connection.setRequestProperty("Charset", "UTF-8");  
        connection.setRequestProperty("Content-Type",  
                "multipart/form-data; boundary=" + BOUNDARY);  
        String boundary = BOUNDARY;  
        StringBuffer contentBody = new StringBuffer("--" + BOUNDARY);  
        String endBoundary = "\r\n--" + boundary + "--\r\n";  
        OutputStream out = connection.getOutputStream();  
        for (FormFieldKeyValuePair ffkvp : generalFormFields)
        {  
            contentBody.append("\r\n")  
            .append("Content-Disposition: form-data; name=\"")  
            .append(ffkvp.getKey() + "\"")  
            .append("\r\n")  
            .append("\r\n")  
            .append(ffkvp.getValue())  
            .append("\r\n")  
            .append("--")  
            .append(boundary);  
        }  
        String boundaryMessage1 = contentBody.toString();  
        out.write(boundaryMessage1.getBytes("utf-8"));  
        for (UploadFileItem ufi : filesToBeUploaded)  
        {  
            contentBody = new StringBuffer();  
            contentBody.append("\r\n")  
            .append("Content-Disposition:form-data; name=\"")  
            .append(ufi.getFormFieldName() + "\"; ") // form中field的名称  
            .append("filename=\"")  
            .append(ufi.getFileName() + "\"") // 上传文件的文件名，包括目录  
            .append("\r\n")  
            .append("Content-Type:application/octet-stream")  
            .append("\r\n\r\n");  
            String boundaryMessage2 = contentBody.toString();  
            out.write(boundaryMessage2.getBytes("utf-8"));  
            File file = new File(ufi.getFileName());  
            DataInputStream dis = new DataInputStream(new FileInputStream(file));  
            int bytes = 0;  
            byte[] bufferOut = new byte[(int) file.length()];  
            bytes = dis.read(bufferOut);  
            out.write(bufferOut, 0, bytes);  
            dis.close();  
            contentBody.append("------------HV2ymHFg03ehbqgZCaKO6jyH");  
            String boundaryMessage = contentBody.toString();  
            out.write(boundaryMessage.getBytes("utf-8"));  
            // System.out.println(boundaryMessage);  
        }  
        out.write("------------HV2ymHFg03ehbqgZCaKO6jyH--\r\n"  
                .getBytes("UTF-8"));  
        out.write(endBoundary.getBytes("utf-8"));  
        out.flush();  
        out.close();  
        String strLine = "";  
        String strResponse = "";  
        InputStream in = connection.getInputStream();  
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
        while ((strLine = reader.readLine()) != null)  
        {  
            strResponse += strLine + "\n";  
        }
        int code = connection.getResponseCode();
        Map<String, Object> result = Maps.newHashMap();
        result.put("code", code);
        result.put("response", strResponse);
        System.out.print(strResponse);  
        return new QueryComponentResponse(code, result);
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
