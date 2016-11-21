package net.wapwag.wemp.ui;

import com.google.common.collect.Maps;
import net.wapwag.wemp.WaterEquipmentServiceImpl;
import net.wapwag.wemp.dao.WaterEquipmentDao;
import org.junit.Test;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class WempReturnServletTest extends BaseMultipleServletTest {

    private static final int port = 9100;

    private static final int maxServerThreads = 10;

    private static final int acceptQueueSize = 1;

    private WaterEquipmentServiceImpl waterEquipmentService = mock(WaterEquipmentServiceImpl.class);

    public WempReturnServletTest() {
        super(port, maxServerThreads, acceptQueueSize);
    }

    @Override
    protected Filter createFilter() throws Exception {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                if (request instanceof HttpServletRequest) {
                    HttpServletRequest httpRequest = (HttpServletRequest) request;
                    HttpSession session = httpRequest.getSession();
                    if (session != null && session.getAttribute("wempRedirect") == null) {
                        session.setAttribute("wempRedirect", "response_type=code&client_id=swm&redirect_uri=http://www.baidu.com&scope=user:*");
                    }
                }
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {

            }
        };
    }

    @Override
    Map<String, Servlet> createMultipleServlet() throws Exception {
        Map<String, Servlet> servletMap = Maps.newHashMap();
        servletMap.put("/wemp/return", new WempReturnServlet());
        servletMap.put("/authorize", new AuthorizeServlet());
        servletMap.put("/authn/access_token", new MockAccessTokenServlet());
        servletMap.put("/authn/userinfo", new MockUserInfoServlet());
        return servletMap;
    }

    protected WaterEquipmentDao createWaterEquipmentDao() throws Exception {
        return WaterEquipmentDaoMock.getWaterEquipmentDao();
    }

    private static final String WEMP_RETURN_PATH = "http://localhost:" + port + "/wemp/return";

    @Override
    public void setupAuthenticationService() throws Exception {
        OSGIUtil.setWaterEquipmentService(waterEquipmentService);
    }

    @Test
    public void noAuthorizationCode() throws Exception {
        QueryComponentResponse response = getAcceptQueryComponent(WEMP_RETURN_PATH, APPLICATION_X_WWW_FORM_URLENCODED);

        assertEquals(SC_UNAUTHORIZED, response.responseCode);
    }

    @Test
    public void processReturn() throws Exception {
        QueryComponentResponse response = getAcceptQueryComponent(String.format(WEMP_RETURN_PATH + "?code=%s", "code"),
                APPLICATION_X_WWW_FORM_URLENCODED);

        assertEquals(SC_FOUND, response.responseCode);
    }

}
