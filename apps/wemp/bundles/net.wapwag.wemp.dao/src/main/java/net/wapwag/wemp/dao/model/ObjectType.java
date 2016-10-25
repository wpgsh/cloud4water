package net.wapwag.wemp.dao.model;

import net.wapwag.wemp.dao.model.geo.*;
import net.wapwag.wemp.dao.model.org.*;
import net.wapwag.wemp.dao.model.project.Project;
import net.wapwag.wemp.dao.model.project.PumpEquipment;
import net.wapwag.wemp.dao.model.project.PumpRoom;

/**
 * Define some type of the object,
 * So we can do some specific actions.
 *
 * Created by ChangWei Li on 2016/10/25 0025.
 */
public enum ObjectType {

    /**
     * geo location type definition
     */
    COUNTRY(Country.class),

    AREA(Area.class),

    PROVINCE(Province.class),

    CITY(City.class),

    COUNTY(County.class),

    /**
     * project and related object type definition
     */
    PROJECT(Project.class),

    PUMPROOM(PumpRoom.class),

    PUMPEQUIPMENT(PumpEquipment.class),

    /**
     * Organization type definition
     */
    WPG_COMPANY(WPGCompany.class),

    WPG_BRANCH(WPGBranch.class),

    WPG_DEPARTMENT(WPGDepartment.class),

    PROPERTY_MANAGE_COMPANY(PropertyManageCompany.class),

    WATER_MANAGEMENT_COMPANY(WaterManageCompany.class),

    WATER_MANAGE_AUTH(WaterManageAuth.class);

    private final Class<? extends ObjectData> typeClass;

    ObjectType(Class<? extends ObjectData> typeClass) {
        this.typeClass = typeClass;
    }

    public Class<? extends ObjectData> getObjectClass() {
        return typeClass;
    }

}
