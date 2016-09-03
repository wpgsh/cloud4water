package net.wapwag.wemp.ui;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

//
// TODO This servlet and accompanying JSP implement the UI
//   to allow the user to manage (grant/revoke) permissions
//   to the applications to use the user's resources
//   (may not be needed if all client apps are developed by WPG)
@WebServlet(urlPatterns = "/access", name = "WEMP_AuthorizeServlet")
public class ClientAccessManagementServlet extends HttpServlet {

}
