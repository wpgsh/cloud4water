package net.wapwag.wemp.dao.model;

/**
 * The list of <em>concrete</em> types goes here. No abstract
 * types should be allowed.
 * 
 * @author Alexander Lukichev
 *
 */
public enum ObjectType {
	AREA(Area.class),
	PROVINCE(Province.class),
	WATER_MANAGEMENT_AUTHORITY(WaterManagementAuthority.class);
	
	private final Class<? extends ObjectEntity> clazz;
	
	ObjectType(Class<? extends ObjectEntity> clazz) {
		this.clazz = clazz;
	}
	
	public Class<? extends ObjectEntity> getObjectClass() {
		return clazz;
	}
}
