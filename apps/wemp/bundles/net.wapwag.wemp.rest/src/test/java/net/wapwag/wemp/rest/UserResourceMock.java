package net.wapwag.wemp.rest;

import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;

import static org.mockito.Mockito.mock;

import com.thingswise.appframework.jaxrs.utils.OAuth2;

/**
 * user resource mock
 * Created by Administrator on 2016/11/7 0007.
 */
@OAuth2(tokenHandler = WempTokenHandler.NAME)
class UserResourceMock extends UserResource {

    static WaterEquipmentService mockService = mock(WaterEquipmentService.class);

    public UserResourceMock() {
        waterEquipmentService = mockService;
    }

}
