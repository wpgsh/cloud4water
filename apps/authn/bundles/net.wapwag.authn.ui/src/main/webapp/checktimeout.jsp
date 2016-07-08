<html>
<%  
if(null != session && null != session.getAttribute("userName") && !"".equals(session.getAttribute("userName"))) {
  %>  
<script>  
  alert(123);  
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