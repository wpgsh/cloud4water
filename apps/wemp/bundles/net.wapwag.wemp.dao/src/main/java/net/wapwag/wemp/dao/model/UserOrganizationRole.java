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
				
		private static final long serialVersionUID = 1L;

		@ManyToOne
		@JoinColumn(name="user_id", referencedColumnName="id")
		protected User user;
		
		@ManyToOne
		@JoinColumn(name="organization_id", referencedColumnName="id")
		protected Organization organization;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((organization == null) ? 0 : Long.hashCode(organization.getId()));
			result = prime * result + ((user == null) ? 0 : Long.hashCode(user.getId()));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UserOrganizationId other = (UserOrganizationId) obj;
			if (organization == null) {
				if (other.organization != null)
					return false;
			} else {
				if (other.organization == null) {
					return false;
				} else {
					if (organization.getId() != other.organization.getId()) {
						return false;
					}
				}
			}
				
			if (user == null) {
				if (other.user != null)
					return false;
			} else {
				if (other.user == null) {
					return false;
				} else {
					if (user.getId() != other.user.getId()) {
						return false;
					}
				}
			}

			return true;
		}
		
		public UserOrganizationId() {
			
		}

		public UserOrganizationId(User user, Organization organization) {
			super();
			this.user = user;
			this.organization = organization;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Organization getOrganization() {
			return organization;
		}

		public void setOrganization(Organization organization) {
			this.organization = organization;
		}
		
		
		
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

	public void setId(UserOrganizationId userOrganizationId) {
		this.id = userOrganizationId;
	}		

}
