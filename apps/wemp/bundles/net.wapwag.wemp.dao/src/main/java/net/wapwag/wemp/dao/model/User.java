package net.wapwag.wemp.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="user_data")
@Inheritance(strategy=InheritanceType.JOINED)
public class User {

	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	
	@Column(name="external_id")
	private String externalId;
	
	@OneToMany(
			fetch=FetchType.LAZY, 
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, 
			mappedBy="id.user")	
	private Set<UserOrganizationRole> organizations = new HashSet<UserOrganizationRole>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Set<UserOrganizationRole> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set<UserOrganizationRole> organizations) {
		this.organizations = organizations;
	}		
	
}
