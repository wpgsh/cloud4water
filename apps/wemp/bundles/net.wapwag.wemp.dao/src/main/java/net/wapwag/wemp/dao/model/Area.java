package net.wapwag.wemp.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "area_data")
@PrimaryKeyJoinColumn(name = "id")
public class Area extends ObjectEntity {

	@OneToMany(mappedBy="area", cascade=CascadeType.ALL)
	private Set<Province> provinces = new HashSet<Province>();
	
	public Area() {
		super(ObjectType.AREA);
	}

	public Set<Province> getProvinces() {
		return provinces;
	}

	public void setProvinces(Set<Province> provinces) {
		this.provinces = provinces;
	}		
	
}
