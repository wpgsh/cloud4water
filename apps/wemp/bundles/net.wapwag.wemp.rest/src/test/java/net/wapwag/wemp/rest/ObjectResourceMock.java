package net.wapwag.wemp.rest;

import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.model.GroupView;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.model.ResultView;
import net.wapwag.wemp.model.UserView;

import java.util.stream.Collectors;

import static net.wapwag.wemp.rest.MockData.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Object resource mock
 * Created by Administrator on 2016/11/7 0007.
 */
class ObjectResourceMock extends ObjectResource {

    static WaterEquipmentService mockService = mock(WaterEquipmentService.class);

    public ObjectResourceMock() {
        waterEquipmentService = mockService;

        try {
            when(waterEquipmentService.getObject(objId))
                    .thenReturn(ObjectView.newInstance(objectData));
            when(waterEquipmentService.getUsersByObject(objId, action))
                    .thenReturn(userList.stream().map(UserView::newInstance).collect(Collectors.toList()));
            when(waterEquipmentService.getObjectByUser(objId, userId))
                    .thenReturn(ObjectView.newInstance(objectData));
            when(waterEquipmentService.addObjectByUser(objId, userId))
                    .thenReturn(ResultView.newInstance(addCount));
            when(waterEquipmentService.removeObjectByUser(objId, userId, action))
                    .thenReturn(ResultView.newInstance(removeCount));
            when(waterEquipmentService.getGroupsByObject(objId, action))
                    .thenReturn(groupList.stream().map(GroupView::newInstance).collect(Collectors.toList()));
            when(waterEquipmentService.getObjectByGroup(objId, groupId))
                    .thenReturn(ObjectView.newInstance(objectData));
            when(waterEquipmentService.addObjectByGroup(objId, groupId))
                    .thenReturn(ResultView.newInstance(addCount));
            when(waterEquipmentService.removeObjectByGroup(objId, groupId, action))
                    .thenReturn(ResultView.newInstance(removeCount));
        } catch (WaterEquipmentServiceException e) {
            e.printStackTrace();
        }
    }

}
