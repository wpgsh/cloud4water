package net.wapwag.wemp.dao.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "water_management_authority_data")
@PrimaryKeyJoinColumn(name = "id")
public class WaterManagementAuthority extends Organization {
	
	public WaterManagementAuthority() {
		super(ObjectType.WATER_MANAGEMENT_AUTHORITY);
	}
	
}
