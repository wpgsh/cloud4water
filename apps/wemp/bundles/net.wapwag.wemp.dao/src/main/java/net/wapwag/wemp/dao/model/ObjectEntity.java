package net.wapwag.wemp.dao.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="object_data")
@Inheritance(strategy=InheritanceType.JOINED)
public class ObjectEntity {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	
	@Column(name="type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ObjectType type;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="object_id")
	private Set<ObjectOrganizationScope> organizations;

	public Class<? extends ObjectEntity> getObjectClass() {
		return type.getObjectClass();
	}
	
	@Column(name="title")
	private String title;
	
	public ObjectEntity() {
		
	}
	
	public ObjectEntity(ObjectType type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}		
	
}
