package net.wapwag.wemp.rest;

import com.thingswise.appframework.jaxrs.utils.OAuth2;
import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;

import static org.mockito.Mockito.mock;

/**
 * Object resource mock
 * Created by Administrator on 2016/11/7 0007.
 */
@OAuth2(tokenHandler = WempTokenHandler.NAME)
class OrgGroupResourceMock extends OrgGroupResource {

    static WaterEquipmentService mockService = mock(WaterEquipmentService.class);

    public OrgGroupResourceMock() {
        waterEquipmentService = mockService;
    }

}
