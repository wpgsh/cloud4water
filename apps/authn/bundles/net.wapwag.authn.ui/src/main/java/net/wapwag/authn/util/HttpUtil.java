package net.wapwag.authn.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	 
	public static String httpSendPOST(String reqXml , String httpUrl)
	  {
	    HttpClient client = HttpClientBuilder.create().
	    		setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(30000).build()).
	    	build();

	    HttpPost method = new HttpPost(httpUrl);

	    String respXml = "";
	    try
	    {
	      ByteArrayEntity requestEntity = new ByteArrayEntity(reqXml.getBytes(), ContentType.APPLICATION_XML);
	      requestEntity.setContentEncoding("utf-8");
	      method.setEntity(requestEntity);
	      
	      //method.setRequestHeader("Accept", "application/xml");
	      //method.setRequestHeader("Content-Type", "application/xml; charset=UTF-8");
	      //method.setRequestHeader("Authorization", authorization.toString());

	      HttpResponse response = client.execute(method);

	      if (response.getStatusLine().getStatusCode() == 200)
	      {
	    	  respXml = EntityUtils.toString(response.getEntity());
	      }
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	      method.releaseConnection();
	    }

	    return respXml;
	  }
	
	public static String httpSendGET(String httpUrl)
	  {
	    HttpClient client = HttpClientBuilder.create().
	    		setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(30000).build()).
	    	build();

	    HttpGet method = new HttpGet(httpUrl);

	    String respXml = "";
	    try
	    {
	      HttpResponse response = client.execute(method);

	      if (response.getStatusLine().getStatusCode() == 200)
	      {
	    	  respXml = EntityUtils.toString(response.getEntity());
	      }
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	      method.releaseConnection();
	    }

	    return respXml;
	  }
}
