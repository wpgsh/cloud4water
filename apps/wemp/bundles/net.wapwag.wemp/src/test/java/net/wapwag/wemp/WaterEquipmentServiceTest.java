package net.wapwag.wemp;

import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.dao.WaterEquipmentDaoException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.permission.AccessToken;
import net.wapwag.wemp.dao.model.permission.AccessTokenId;
import net.wapwag.wemp.dao.model.permission.RegisteredClient;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.model.AccessTokenMapper;
import net.wapwag.wemp.model.ObjectView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WaterEquipmentServiceTest {

    @Parameter
    public long userId;

    @Parameter
    public long groupId;

    @Parameter
    public long orgId;

    private static final long objId = 1L;

    private static WaterEquipmentServiceImpl waterEquipmentService;

    @Mock
	private WaterEquipmentDao waterEquipmentDao;

    @SuppressWarnings("unchecked")
    @Before
    public void before() throws Exception {
        when(waterEquipmentDao
                .txExpr(any(WaterEquipmentDao.ComplexActionWithResult.class), any(Class.class)))
                .then(method -> {
                    WaterEquipmentDao.ComplexActionWithResult a = method.getArgument(0);
                    return a.apply();
                });

        waterEquipmentService = new WaterEquipmentServiceImpl();
        waterEquipmentService.setWaterEquipmentDao(waterEquipmentDao);

    }

    @Test
    public void testIsAuthorized() throws Exception {

    }

    @Test
    public void testLookupToken() throws Exception {
        //Use base64 encoding handle:testToken
        String handle = "testToken";
        String encodeHandle = "dGVzdFRva2Vu";

        long userId = 1L;
        long clientId = 1L;
        String clientIdentity = "clientId";

        User user = new User();
        user.setId(userId);

        RegisteredClient client = new RegisteredClient();
        client.setId(clientId);
        client.setClientId(clientIdentity);

        AccessTokenId accessTokenId = new AccessTokenId(user, client);
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessTokenId(accessTokenId);
        accessToken.setHandle(handle);
        accessToken.setAuthrizationCode("testCode");
        accessToken.setExpiration(Long.MAX_VALUE);
        accessToken.setScope("user:name user:avatar");

        when(waterEquipmentDao.lookupAccessToken(handle)).thenReturn(accessToken);

        AccessTokenMapper accessTokenMapper = waterEquipmentService.lookupToken(encodeHandle);

        assertNotNull(accessTokenMapper);
        assertTrue(Long.parseLong(accessTokenMapper.userId) == userId);
        assertTrue(clientIdentity.equals(accessTokenMapper.clientId));
        assertTrue(accessTokenMapper.scope.size() == 2);
    }

    @Test
    public void testLookupToken_canNotFindToken() throws Exception {
        when(waterEquipmentDao.lookupAccessToken(anyString())).thenReturn(null);

        AccessTokenMapper accessTokenMapper = waterEquipmentService.lookupToken("invalid_handle");

        assertNull(accessTokenMapper);
    }

    @Test(expected = WaterEquipmentServiceException.class)
    public void testLookupToken_handle_illegalBase64Format() throws Exception {
        AccessTokenMapper accessTokenMapper = waterEquipmentService.lookupToken("invalid_handle");

        assertNull(accessTokenMapper);
    }

    @Test
    public void testLookupToken_Exception() throws Exception {
        when(waterEquipmentDao.lookupAccessToken(anyString())).thenThrow(WaterEquipmentDaoException.class);

        AccessTokenMapper accessTokenMapper = waterEquipmentService.lookupToken("dGVzdFRva2Vu");

        assertNull(accessTokenMapper);
    }

    @Test
    public void testGetObject() throws Exception {
        ObjectData objectData = new ObjectData();
        objectData.setId(objId);
        objectData.setName("中国");
        objectData.setType(ObjectType.COUNTRY);

        when(waterEquipmentDao.getObjectData(objId)).thenReturn(objectData);

        ObjectView objectView = waterEquipmentService.getObject(objId);

        assertNotNull(objectView);
    }

    @Test
    public void testGetObjectUnfind() throws Exception {
        when(waterEquipmentDao.getObjectData(objId)).thenThrow(WaterEquipmentDaoException .class);

        ObjectView objectView = waterEquipmentService.getObject(objId);

        assertNull(objectView);
    }

    @Test
    public void getUsersByObject() throws Exception {

    }

    @Test
    public void getObjectByUser() throws Exception {

    }

    @Test
    public void addObjectByUser() throws Exception {

    }

    @Test
    public void removeObjectByUser() throws Exception {

    }

    @Test
    public void getGroupsByObject() throws Exception {

    }

    @Test
    public void getObjectByGroup() throws Exception {

    }

    @Test
    public void addObjectByGroup() throws Exception {

    }

    @Test
    public void removeObjectByGroup() throws Exception {

    }

    @Test
    public void getGroupsByOrg() throws Exception {

    }

    @Test
    public void addGroupByOrg() throws Exception {

    }

    @Test
    public void getGroupByOrg() throws Exception {

    }

    @Test
    public void updateGroupByOrg() throws Exception {

    }

    @Test
    public void removeGroupByOrg() throws Exception {

    }

    @Test
    public void getUsersByGroup() throws Exception {

    }

    @Test
    public void addUserByGroup() throws Exception {

    }

    @Test
    public void removeUserByGroup() throws Exception {

    }

    @Test
    public void getObjectsByGroup() throws Exception {

    }

    @Test
    public void getObjectByGroup1() throws Exception {

    }

    @Test
    public void getUsersByOrg() throws Exception {

    }

    @Test
    public void addUserByOrg() throws Exception {

    }

    @Test
    public void removeUserByOrg() throws Exception {

    }

    @Test
    public void getObjectsByOrg() throws Exception {

    }

    @Test
    public void addObjectByOrg() throws Exception {

    }

    @Test
    public void removeObjectByOrg() throws Exception {

    }

    @Test
    public void checkPermission() throws Exception {

    }

    @Test
    public void getObjectsByUser() throws Exception {

    }
}
