<html>
<%@page import="net.wapwag.authn.util.AsyncLoginUtil"%>
<%  
if(null != session && null != session.getAttribute("userName") 
&& !"".equals(session.getAttribute("userName")) &&
AsyncLoginUtil.checkSession(session)) {
  %>  
<script>  
</script>  
<%  
}else
{
%>
<script>  
  window.location="login.jsp";  
</script>
<%
}
%> 
  <body>
  
  
  </body>


</html>