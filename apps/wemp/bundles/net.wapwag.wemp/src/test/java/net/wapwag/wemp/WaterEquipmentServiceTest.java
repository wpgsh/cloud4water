package net.wapwag.wemp;

import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.dao.WaterEquipmentDaoException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.AccessToken;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.model.*;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.wapwag.wemp.MockData.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WaterEquipmentServiceTest {


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
        when(waterEquipmentDao.isAuthorized(userId, action, objId)).thenReturn(true);

        boolean isAuthorized = waterEquipmentService.isAuthorized(userId + "", action, objId + "");

        assertEquals(true, isAuthorized);
    }

    @Test
    public void testIsAuthorized_UnAuthorize() throws Exception {
        when(waterEquipmentDao.isAuthorized(userId, "write", objId)).thenReturn(false);

        boolean isAuthorized = waterEquipmentService.isAuthorized(userId + "", "write", objId + "");

        assertEquals(false, isAuthorized);
    }

    @Test
    public void testIsAuthorized_Exception() throws Exception {
        when(waterEquipmentDao.isAuthorized(invalidId, invalidString, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        boolean isAuthorized = waterEquipmentService.isAuthorized(invalidId + "", invalidString, invalidId + "");

        assertEquals(false, isAuthorized);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testLookupToken() throws Exception {
        when(waterEquipmentDao.lookupAccessToken(handle)).thenReturn(accessToken);

        AccessTokenMapper accessTokenMapper = waterEquipmentService.lookupToken(handle);

        assertNotNull(accessTokenMapper);
        assertTrue(Long.parseLong(accessTokenMapper.userId) == userId);
        assertTrue(clientIdentity.equals(accessTokenMapper.clientId));
        assertTrue(accessTokenMapper.scope.size() == 2);
    }

    @Test
    public void testLookupToken_CanNotFindToken() throws Exception {
        when(waterEquipmentDao.lookupAccessToken(anyString())).thenReturn(null);

        AccessTokenMapper accessTokenMapper = waterEquipmentService.lookupToken(handle);

        assertNull(accessTokenMapper);
    }

    @Test
    public void testLookupToken_Exception() throws Exception {
        when(waterEquipmentDao.lookupAccessToken(invalidString)).thenThrow(WaterEquipmentDaoException.class);

        AccessTokenMapper accessTokenMapper = waterEquipmentService.lookupToken(invalidString);

        assertNull(accessTokenMapper);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testGetAuthorizationCode_WPGClient() throws Exception {
        ArgumentCaptor<AccessToken> tokenArgumentCaptor = ArgumentCaptor.forClass(AccessToken.class);

        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(wpgClient);
        when(waterEquipmentDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken);
        when(waterEquipmentDao.saveAccessToken(any(AccessToken.class))).thenReturn(1L);

        String resultCode = waterEquipmentService.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);

        verify(waterEquipmentDao).saveAccessToken(tokenArgumentCaptor.capture());

        assertNotNull(resultCode);
        assertEquals(tokenArgumentCaptor.getValue().getAuthrizationCode(), resultCode);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testGetAuthorizationCode_NonWPGClientWithAuthzedScope() throws Exception {
        ArgumentCaptor<AccessToken> tokenArgumentCaptor = ArgumentCaptor.forClass(AccessToken.class);

        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(nonWPGclientWithAuthzedScope);
        when(waterEquipmentDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken_nonWPGclientWithAuthzedScope);
        when(waterEquipmentDao.saveAccessToken(any(AccessToken.class))).thenReturn(1L);

        String resultCode = waterEquipmentService.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);

        verify(waterEquipmentDao).saveAccessToken(tokenArgumentCaptor.capture());

        assertNotNull(resultCode);
        assertEquals(tokenArgumentCaptor.getValue().getAuthrizationCode(), resultCode);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_NonWPGclientWithNoScope() throws Exception {
        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(nonWPGclientWithNoScope);
        when(waterEquipmentDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken_nonWPGclientWithNoScope);

        waterEquipmentService.getAuthorizationCode(userId, clientIdentity, redirectURI, null);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testGetAuthorizationCode_NonWPGClientWithNonAuthzedScope() throws Exception {
        ArgumentCaptor<AccessToken> tokenArgumentCaptor = ArgumentCaptor.forClass(AccessToken.class);

        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(nonWPGclientWithNonAuthzedScope);
        when(waterEquipmentDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken_nonWPGclientWithNonAuthzedScope);
        when(waterEquipmentDao.saveAccessToken(any(AccessToken.class))).thenReturn(1L);

        String resultCode = waterEquipmentService.getAuthorizationCode(userId, clientIdentity, redirectURI, nonAuthzedscopes);

        verify(waterEquipmentDao).saveAccessToken(tokenArgumentCaptor.capture());

        assertNotNull(resultCode);
        assertEquals(tokenArgumentCaptor.getValue().getAuthrizationCode(), resultCode);
    }


    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_Exception() throws Exception {
        when(waterEquipmentDao.getClientByRedirectURI(invalidString)).thenThrow(WaterEquipmentDaoException.class);

        waterEquipmentService.getAuthorizationCode(userId, clientIdentity, invalidString, scopes);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_SaveFailed() throws Exception {
        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(client);
        when(waterEquipmentDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken);
        when(waterEquipmentDao.saveAccessToken(any(AccessToken.class))).thenReturn(0L);

        waterEquipmentService.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_ErrorClientCredential() throws Exception {
        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(null);

        waterEquipmentService.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testGetAccessToken() throws Exception {
        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(client);
        when(waterEquipmentDao.getAccessTokenByCode(code)).thenReturn(accessToken);
        when(waterEquipmentDao.saveAccessToken(accessToken)).thenReturn(1L);

        String token = waterEquipmentService.getAccessToken(clientIdentity, clientSecret,  code, redirectURI);

        assertEquals(handle, token);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAccessToken_UnRegistredClient() throws Exception {
        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(null);

        waterEquipmentService.getAccessToken(clientIdentity, clientSecret, code, redirectURI);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAccessToken_InvalidClientIdOrClientSecret() throws Exception {
        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(client);

        waterEquipmentService.getAccessToken("invalid_clientIdentity", "invalid_clientSecret", code, redirectURI);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAccessToken_InvalidAuthorizationCode() throws Exception {
        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(client);
        when(waterEquipmentDao.getAccessTokenByCode(anyString())).thenReturn(null);

        waterEquipmentService.getAccessToken(clientIdentity, clientSecret,  "invalidAuthorizationCode", redirectURI);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAccessToken_ExpiredAuthorizationCode() throws Exception {
        when(waterEquipmentDao.getClientByRedirectURI(redirectURI)).thenReturn(client);
        when(waterEquipmentDao.getAccessTokenByCode(code)).thenReturn(accessToken_expired);

        waterEquipmentService.getAccessToken(clientIdentity, clientSecret,  code, redirectURI);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAccessToken_Exception() throws Exception {
        when(waterEquipmentDao.getClientByRedirectURI(invalidString)).thenThrow(WaterEquipmentDaoException.class);

        waterEquipmentService.getAccessToken(clientIdentity, clientSecret, code, invalidString);
    }

    @Test
    public void testGetObject() throws Exception {
        when(waterEquipmentDao.getObjectData(objId)).thenReturn(objectData);

        ObjectView objectView = waterEquipmentService.getObject(objId);

        assertNotNull(objectView);
        assertEquals(objectData.getId(), objectView.id);
        assertEquals(objectData.getName(), objectView.name);
        assertEquals(objectData.getType(), objectView.objectType);
    }

    @Test
    public void testGetObject_InvalidObjectId() throws Exception {
        when(waterEquipmentDao.getObjectData(objId)).thenThrow(WaterEquipmentDaoException .class);

        ObjectView objectView = waterEquipmentService.getObject(objId);

        assertNull(objectView);
    }

    @Test
    public void testGetUsersByObject() throws Exception {
        when(waterEquipmentDao.getUsersByObject(objId, action)).thenReturn(userList);

        List<UserView> userViewList = waterEquipmentService.getUsersByObject(objId, action);

        assertNotNull(userViewList);
        assertTrue(userViewList.size() == userList.size());
    }

    @Test
    public void testGetUsersByObject_Exception() throws Exception {
        when(waterEquipmentDao.getUsersByObject(invalidId, invalidString)).thenThrow(WaterEquipmentDaoException.class);

        List<UserView> userViewList = waterEquipmentService.getUsersByObject(invalidId, invalidString);

        assertNull(userViewList);
    }

    @Test
    public void testGetObjectByUser() throws Exception {
        when(waterEquipmentDao.getObjectByUser(objId, userId)).thenReturn(objectData);

        ObjectView objectView = waterEquipmentService.getObjectByUser(objId, userId);

        assertNotNull(objectView);
        assertEquals(objectData.getId(), objId);
        assertEquals(objectData.getName(), objectView.name);
    }

    @Test
    public void testGetObjectByUser_Exception() throws Exception {
        when(waterEquipmentDao.getObjectByUser(invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        ObjectView objectView = waterEquipmentService.getObjectByUser(invalidId, invalidId);

        assertNull(objectView);
    }

    @Test
    public void testAddObjectByUser() throws Exception {
        when(waterEquipmentDao.addObjectByUser(objId, userId)).thenReturn(addCount);

        ResultView resultView = waterEquipmentService.addObjectByUser(objId, userId);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testAddObjectByUser_Exception() throws Exception {
        when(waterEquipmentDao.addObjectByUser(invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.addObjectByUser(invalidId, invalidId);

        assertNull(resultView);
    }

    @Test
    public void testRemoveObjectByUser() throws Exception {
        when(waterEquipmentDao.removeObjectByUser(objId, userId, action)).thenReturn(removeCount);

        ResultView resultView = waterEquipmentService.removeObjectByUser(objId, userId, action);

        assertNotNull(resultView);
        assertEquals(removeCount, resultView.count);
    }

    @Test
    public void testRemoveObjectByUser_Exception() throws Exception {
        when(waterEquipmentDao.removeObjectByUser(invalidId, invalidId, action)).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.removeObjectByUser(invalidId, invalidId, action);

        assertNull(resultView);
    }

    @Test
    public void testGetGroupsByObject() throws Exception {
        when(waterEquipmentDao.getGroupsByObject(objId, action)).thenReturn(groupList);
        
        List<GroupView> groupViews = waterEquipmentService.getGroupsByObject(objId, action);
        
        assertNotNull(groupViews);
        assertTrue(groupViews.size() == count);
        
    }

    @Test
    public void testGetGroupsByObject_Exception() throws Exception {
        when(waterEquipmentDao.getGroupsByObject(invalidId, action)).thenThrow(WaterEquipmentDaoException.class);

        List<GroupView> groupViews = waterEquipmentService.getGroupsByObject(invalidId, action);

        assertNull(groupViews);
    }

    @Test
    public void testGetObjectByGroup() throws Exception {
        when(waterEquipmentDao.getObjectByGroup(objId, groupId)).thenReturn(objectData);
        
        ObjectView objectView = waterEquipmentService.getObjectByGroup(objId, groupId);
        
        assertNotNull(objectView);
        assertTrue(objectView.id == objId && objectData.getName().equals(objectView.name));
    }

    @Test
    public void testGetObjectByGroup_Exception() throws Exception {
        when(waterEquipmentDao.getObjectByGroup(invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        ObjectView objectView = waterEquipmentService.getObjectByGroup(invalidId, invalidId);

        assertNull(objectView);

    }

    @Test
    public void testAddObjectByGroup() throws Exception {
        when(waterEquipmentDao.addObjectByGroup(objId, groupId)).thenReturn(addCount);
        
        ResultView resultView = waterEquipmentService.addObjectByGroup(objId, groupId);
        
        assertNotNull(resultView);
        assertTrue(resultView.count == addCount);
    }

    @Test
    public void testAddObjectByGroup_Exception() throws Exception {
        when(waterEquipmentDao.addObjectByGroup(invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.addObjectByGroup(invalidId, invalidId);

        assertNull(resultView);
    }

    @Test
    public void testRemoveObjectByGroup() throws Exception {
        when(waterEquipmentDao.removeObjectByGroup(objId, groupId, action)).thenReturn(removeCount);


        ResultView resultView = waterEquipmentService.removeObjectByGroup(objId, groupId, action);

        assertNotNull(resultView);
        assertEquals(removeCount, resultView.count);
    }

    @Test
    public void testRemoveObjectByGroup_Exception() throws Exception {
        when(waterEquipmentDao.removeObjectByGroup(invalidId, invalidId, action)).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.removeObjectByGroup(invalidId, invalidId, action);
        
        assertNull(resultView);
    }

    @Test
    public void testGetGroupsByOrg() throws Exception {
        when(waterEquipmentDao.getGroupsByOrg(orgId)).thenReturn(groupList);

        List<GroupView> groupViewList = waterEquipmentService.getGroupsByOrg(orgId);

        assertNotNull(groupViewList);
        assertEquals(count, groupViewList.size());
    }

    @Test
    public void testGetGroupsByOrg_Exception() throws Exception {
        when(waterEquipmentDao.getGroupsByOrg(invalidId)).thenThrow(WaterEquipmentDaoException.class);

        List<GroupView> groupViewList = waterEquipmentService.getGroupsByOrg(invalidId);

        assertNull(groupViewList);
    }

    @Test
    public void testAddGroupByOrg() throws Exception {
        when(waterEquipmentDao.addGroupByOrg(orgId, group)).thenReturn(addCount);

        ResultView resultView = waterEquipmentService.addGroupByOrg(orgId, group);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testAddGroupByOrg_Exception() throws Exception {
        when(waterEquipmentDao.addGroupByOrg(eq(invalidId), any(Group.class))).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.addGroupByOrg(invalidId, group);

        assertNull(resultView);
    }

    @Test
    public void testGetGroupByOrg() throws Exception {
        when(waterEquipmentDao.getGroupByOrg(orgId, groupId)).thenReturn(group);

        GroupView groupView = waterEquipmentService.getGroupByOrg(orgId, groupId);

        assertNotNull(groupView);
        assertEquals(group.getId(), groupView.id);
        assertEquals(group.getName(), groupView.name);
    }

    @Test
    public void testGetGroupByOrg_Exception() throws Exception {
        when(waterEquipmentDao.getGroupByOrg(invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        GroupView groupView = waterEquipmentService.getGroupByOrg(invalidId, invalidId);

        assertNull(groupView);
    }

    @Test
    public void testUpdateGroupByOrg() throws Exception {
        when(waterEquipmentDao.updateGroupByOrg(orgId, groupId, group)).thenReturn(updateCount);

        ResultView resultView = waterEquipmentService.updateGroupByOrg(orgId, groupId, group);

        assertNotNull(resultView);
        assertEquals(updateCount, resultView.count);
    }

    @Test
    public void testUpdateGroupByOrg_Exception() throws Exception {
        when(waterEquipmentDao.updateGroupByOrg(eq(invalidId), eq(invalidId), any(Group.class))).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.updateGroupByOrg(invalidId, invalidId, group);

        assertNull(resultView);
    }

    @Test
    public void removeGroupByOrg() throws Exception {
        when(waterEquipmentDao.removeGroupByOrg(orgId, groupId)).thenReturn(removeCount);

        ResultView resultView = waterEquipmentService.removeGroupByOrg(orgId, groupId);

        assertNotNull(resultView);
        assertEquals(removeCount, resultView.count);
    }

    @Test
    public void removeGroupByOrg_Exception() throws Exception {
        when(waterEquipmentDao.removeGroupByOrg(invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.removeGroupByOrg(invalidId, invalidId);

        assertNull(resultView);
    }

    @Test
    public void testGetUsersByGroup() throws Exception {
        when(waterEquipmentDao.getUsersByGroup(objId, groupId)).thenReturn(userList);

        List<UserView> userViewList = waterEquipmentService.getUsersByGroup(orgId, groupId);

        assertNotNull(userViewList);
        assertEquals(userList.size(), userViewList.size());
    }

    @Test
    public void testGetUsersByGroup_Exception() throws Exception {
        when(waterEquipmentDao.getUsersByGroup(invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        List<UserView> userViewList = waterEquipmentService.getUsersByGroup(invalidId, invalidId);

        assertNull(userViewList);
    }

    @Test
    public void testAddUserByGroup() throws Exception {
        when(waterEquipmentDao.addUserByGroup(orgId, groupId, userId)).thenReturn(addCount);

        ResultView resultView = waterEquipmentService.addUserByGroup(orgId, groupId, userId);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testAddUserByGroup_Exception() throws Exception {
        when(waterEquipmentDao.addUserByGroup(invalidId, invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.addUserByGroup(invalidId, invalidId, invalidId);

        assertNull(resultView);
    }

    @Test
    public void testRemoveUserByGroup() throws Exception {
        when(waterEquipmentDao.removeUserByGroup(orgId, groupId, userId)).thenReturn(removeCount);

        ResultView resultView = waterEquipmentService.removeUserByGroup(orgId, groupId, userId);

        assertNotNull(resultView);
        assertEquals(removeCount, resultView.count);
    }

    @Test
    public void testRemoveUserByGroup_Exception() throws Exception {
        when(waterEquipmentDao.removeUserByGroup(invalidId, invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.removeUserByGroup(invalidId, invalidId, invalidId);

        assertNull(resultView);
    }

    @Test
    public void testGetObjectsByGroup() throws Exception {
        when(waterEquipmentDao.getObjectsByGroup(orgId, groupId)).thenReturn(objectDataList);

        List<ObjectView> objectViews = waterEquipmentService.getObjectsByGroup(orgId, groupId);

        assertNotNull(objectViews);
        assertEquals(objectDataList.size(), objectViews.size());
    }

    @Test
    public void testGetObjectsByGroup_Exception() throws Exception {
        when(waterEquipmentDao.getObjectsByGroup(invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        List<ObjectView> objectViews = waterEquipmentService.getObjectsByGroup(invalidId, invalidId);

        assertNull(objectViews);
    }

    @Test
    public void testGetObjectByGroupWithAction() throws Exception {
        when(waterEquipmentDao.getObjectByGroup(orgId, groupId, objId, action)).thenReturn(objectData);

        ObjectView objectView = waterEquipmentService.getObjectByGroup(orgId, groupId, objId, action);

        assertNotNull(objectView);
        assertEquals(objectData.getId(), objectView.id);
        assertEquals(objectData.getName(), objectView.name);
        assertEquals(objectData.getType(), objectView.objectType);
    }

    @Test
    public void testGetObjectByGroupWithAction_Exception() throws Exception {
        when(waterEquipmentDao.getObjectByGroup(invalidId, invalidId, invalidId, action)).thenThrow(WaterEquipmentDaoException.class);

        ObjectView objectView = waterEquipmentService.getObjectByGroup(invalidId, invalidId, invalidId, action);

        assertNull(objectView);
    }

    @Test
    public void testGetUsersByOrg() throws Exception {
        when(waterEquipmentDao.getUsersByOrg(orgId)).thenReturn(userList);

        List<UserView> userViews = waterEquipmentService.getUsersByOrg(orgId);

        assertNotNull(userViews);
        assertEquals(userList.size(), userViews.size());
    }

    @Test
    public void testGetUsersByOrg_Exception() throws Exception {
        when(waterEquipmentDao.getUsersByOrg(invalidId)).thenThrow(WaterEquipmentDaoException.class);

        List<UserView> userViews = waterEquipmentService.getUsersByOrg(invalidId);

        assertNull(userViews);
    }

    @Test
    public void testAddUserByOrg() throws Exception {
        when(waterEquipmentDao.addUserByOrg(eq(invalidId), any(User.class))).thenReturn(addCount);

        ResultView resultView = waterEquipmentService.addUserByOrg(invalidId, user);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testAddUserByOrg_Exception() throws Exception {
        when(waterEquipmentDao.addUserByOrg(eq(invalidId), any(User.class))).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.addUserByOrg(invalidId, user);

        assertNull(resultView);
    }

    @Test
    public void testRemoveUserByOrg() throws Exception {
        when(waterEquipmentDao.removeUserByOrg(orgId, userId)).thenReturn(removeCount);

        ResultView resultView = waterEquipmentService.removeUserByOrg(orgId, userId);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testRemoveUserByOrg_Exception() throws Exception {
        when(waterEquipmentDao.removeUserByOrg(invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.removeUserByOrg(invalidId, invalidId);

        assertNull(resultView);
    }

    @Test
    public void testGetObjectsByOrg() throws Exception {
        when(waterEquipmentDao.getObjectsByOrg(orgId)).thenReturn(objectDataList);

        List<ObjectView> objectViews = waterEquipmentService.getObjectsByOrg(orgId);

        assertNotNull(objectViews);
        assertEquals(objectDataList.size(), objectViews.size());
    }

    @Test
    public void testGetObjectsByOrg_Exception() throws Exception {
        when(waterEquipmentDao.getObjectsByOrg(invalidId)).thenThrow(WaterEquipmentDaoException.class);

        List<ObjectView> objectViews = waterEquipmentService.getObjectsByOrg(invalidId);

        assertNull(objectViews);
    }

    @Test
    public void testAddObjectByOrg() throws Exception {
        when(waterEquipmentDao.addObjectByOrg(orgId, objectData)).thenReturn(addCount);

        ResultView resultView = waterEquipmentService.addObjectByOrg(orgId, objectData);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testAddObjectByOrg_Exception() throws Exception {
        when(waterEquipmentDao.addObjectByOrg(eq(invalidId), any(ObjectData.class))).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.addObjectByOrg(invalidId, objectData);

        assertNull(resultView);
    }

    @Test
    public void testRemoveObjectByOrg() throws Exception {
        when(waterEquipmentDao.removeObjectByOrg(orgId, objId)).thenReturn(removeCount);

        ResultView resultView = waterEquipmentService.removeObjectByOrg(orgId, objId);

        assertNotNull(resultView);
        assertEquals(removeCount, resultView.count);
    }

    @Test
    public void testRemoveObjectByOrg_Exception() throws Exception {
        when(waterEquipmentDao.removeObjectByOrg(invalidId, invalidId)).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.removeObjectByOrg(invalidId, invalidId);

        assertNull(resultView);
    }

    @Test
    public void testCheckPermission_True() throws Exception {
        when(waterEquipmentDao.checkPermission(userId, objectData)).thenReturn(true);

        ResultView resultView = waterEquipmentService.checkPermission(userId, objectData);

        assertNotNull(resultView);
        assertEquals(1, resultView.count);
    }

    @Test
    public void testCheckPermission_False() throws Exception {
        when(waterEquipmentDao.checkPermission(userId, objectData)).thenReturn(false);

        ResultView resultView = waterEquipmentService.checkPermission(userId, objectData);

        assertNotNull(resultView);
        assertEquals(0, resultView.count);
    }

    @Test
    public void testCheckPermission_Exception() throws Exception {
        when(waterEquipmentDao.checkPermission(eq(invalidId), any(ObjectData.class))).thenThrow(WaterEquipmentDaoException.class);

        ResultView resultView = waterEquipmentService.checkPermission(invalidId, objectData);

        assertNull(resultView);
    }

    @Test
    public void testGetObjectsByUser() throws Exception {
        when(waterEquipmentDao.getObjectsByUser(userId, "read")).thenReturn(new HashSet<>(objectDataList));

        Set<ObjectView> objectViews = waterEquipmentService.getObjectsByUser(userId, "read");

        assertNotNull(objectViews);
        assertEquals(objectDataList.size(), objectViews.size());
    }

    @Test
    public void testGetObjectsByUser_Exception() throws Exception {
        when(waterEquipmentDao.getObjectsByUser(invalidId, "read")).thenThrow(WaterEquipmentDaoException.class);

        Set<ObjectView> objectViews = waterEquipmentService.getObjectsByUser(invalidId, "read");

        assertNull(objectViews);
    }

    @Test
    public void testSaveAuthnUser_ExistUser() throws Exception {
        when(waterEquipmentDao.getUserByExternalId(userId)).thenReturn(user);

        int exist = waterEquipmentService.saveAuthnUser(authnUser);

        assertEquals(exist, 0);
    }

    @Test
    public void testSaveAuthnUser_NonExistUser() throws Exception {
        when(waterEquipmentDao.getUserByExternalId(userId)).thenReturn(null);
        when(waterEquipmentDao.addUser(any(User.class))).thenReturn(1);

        int exist = waterEquipmentService.saveAuthnUser(authnUser);

        assertEquals(exist, 1);
    }

    @Test
    public void testSaveAuthnUser_Exception() throws Exception {
        when(waterEquipmentDao.getUserByExternalId(userId)).thenThrow(WaterEquipmentDaoException.class);

        int exist = waterEquipmentService.saveAuthnUser(authnUser);

        assertEquals(exist, 0);
    }
}
