package net.wapwag.wemp;

import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.dao.WaterEquipmentDaoImpl;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.model.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Suite;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class WaterEquipmentServiceTest {

    @Parameter
    public long userId;

    @Parameter
    public long groupId;

    @Parameter
    public long orgId;

    private static final long objId = 1L;

    @Mock
    private WaterEquipmentService waterEquipmentService;

    @Mock
	private WaterEquipmentDao waterEquipmentDao;

    @Before
    public void before() throws Exception {
        ObjectData objectData = new ObjectData();
        objectData.setId(objId);
        objectData.setName("中国");
        objectData.setType(ObjectType.COUNTRY);

        when(waterEquipmentDao.getObjectData(objId)).thenReturn(objectData);
    }

    @Test
    public void isAuthorized() throws Exception {

    }

    @Test
    public void lookupToken() throws Exception {

    }

    @Test
    public void getObject() throws Exception {
        ObjectData objectData = waterEquipmentDao.getObjectData(objId);

        verify(waterEquipmentDao, times(1)).getObjectData(objId);
        assertNotNull(objectData);
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
