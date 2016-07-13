package net.wapwag.authn.util;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

public class HttpUtil {
	 
	public static String httpSendPOST(String reqXml , String httpUrl)
	  {
	    HttpClient client = new HttpClient();

	    client.getHttpConnectionManager().getParams().setConnectionTimeout(Integer.parseInt("30000"));

	    PostMethod method = new PostMethod(httpUrl);

	    String respXml = "";
	    try
	    {
	      RequestEntity requestEntity = new ByteArrayRequestEntity(reqXml.getBytes(), "UTF-8");
	      method.setRequestEntity(requestEntity);

	      //method.setRequestHeader("Accept", "application/xml");
	      //method.setRequestHeader("Content-Type", "application/xml; charset=UTF-8");
	      //method.setRequestHeader("Authorization", authorization.toString());

	      int responseCode = client.executeMethod(method);

	      if (200 == responseCode)
	      {
	          respXml = method.getResponseBodyAsString();
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
	    HttpClient client = new HttpClient();

	    client.getHttpConnectionManager().getParams().setConnectionTimeout(Integer.parseInt("30000"));

	    GetMethod method = new GetMethod(httpUrl);

	    String respXml = "";
	    try
	    {
	      int responseCode = client.executeMethod(method);

	      if (200 == responseCode)
	      {
	    	  respXml = method.getResponseBodyAsString();
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
