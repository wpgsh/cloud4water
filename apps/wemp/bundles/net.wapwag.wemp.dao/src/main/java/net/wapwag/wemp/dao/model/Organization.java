package net.wapwag.wemp.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "organization_data")
@Inheritance(strategy=InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "id")
public class Organization extends ObjectEntity {

	@OneToMany(
			fetch=FetchType.LAZY, 
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, 
			mappedBy="id.organization")
	private Set<UserOrganizationRole> users = new HashSet<UserOrganizationRole>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy="id.organization")
	private Set<ObjectOrganizationScope> objects;
	
	public Organization() {
		
	}
	
	public Organization(ObjectType type) {
		super(type);
	}
}
