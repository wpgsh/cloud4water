package net.wapwag.wemp.dao.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="user_organization_role_data")
public class UserOrganizationRole {
	
	@Embeddable
	public static class UserOrganizationId implements Serializable {
				
		@ManyToOne(fetch=FetchType.EAGER)
		protected User user;
		
		@ManyToOne(fetch=FetchType.EAGER)
		protected Organization organization;
		
	}
	
	@EmbeddedId
	private UserOrganizationId id; 

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="role")
	private RoleType role;

	public User getUser() {
		return id.user;
	}

	public void setUser(User user) {
		this.id.user = user;
	}

	public Organization getOrganization() {
		return id.organization;
	}

	public void setOrganization(Organization organization) {
		this.id.organization = organization;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}		

}
