package net.wapwag.wemp.dao.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "province_data")
@PrimaryKeyJoinColumn(name = "id")
public class Province extends ObjectEntity {

	@ManyToOne(cascade=CascadeType.ALL)
	private Area area;
	
	public Province() {
		super(ObjectType.PROVINCE);
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
}
