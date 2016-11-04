package net.wapwag.wemp;

import net.wapwag.wemp.dao.WaterEquipmentDaoException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * WaterEquipmentServiceException test
 * Created by Administrator on 2016/11/4 0004.
 */
public class WaterEquipmentServiceExceptionTest {

    private static final String message = "exception";

    @Test
    public void testConstructor_NoArg() {
        WaterEquipmentServiceException waterEquipmentServiceException = new WaterEquipmentServiceException();
        assertNotNull(waterEquipmentServiceException);
    }

    @Test
    public void testConstructor_String() {
        WaterEquipmentServiceException waterEquipmentServiceException = new WaterEquipmentServiceException(message);
        assertNotNull(waterEquipmentServiceException);
    }

    @Test
    public void testConstructor_Throwable() {
        WaterEquipmentServiceException waterEquipmentServiceException = new WaterEquipmentServiceException(new NullPointerException());
        assertNotNull(waterEquipmentServiceException);
    }

    @Test
    public void testConstructor_StringAndThrowable() {
        WaterEquipmentServiceException waterEquipmentServiceException = new WaterEquipmentServiceException(message, new NullPointerException());
        assertNotNull(waterEquipmentServiceException);
    }

}