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
class OrgGroupResourceMock extends OrgGroupResource {

    static WaterEquipmentService mockService = mock(WaterEquipmentService.class);

    public OrgGroupResourceMock() {
        waterEquipmentService = mockService;
    }

}
