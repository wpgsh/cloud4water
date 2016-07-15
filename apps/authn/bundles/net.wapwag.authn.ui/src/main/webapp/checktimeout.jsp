<html>
<%  
if(null != session && null != session.getAttribute("userName") && !"".equals(session.getAttribute("userName"))) {
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