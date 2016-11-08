package net.wapwag.wemp.rest;

import net.wapwag.wemp.WaterEquipmentService;

import static org.mockito.Mockito.mock;

/**
 * user resource mock
 * Created by Administrator on 2016/11/7 0007.
 */
class UserResourceMock extends UserResource {

    static WaterEquipmentService mockService = mock(WaterEquipmentService.class);

    public UserResourceMock() {
        waterEquipmentService = mockService;
    }

}
