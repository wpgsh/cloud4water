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
@Table(name="object_organization_scope_data")
public class ObjectOrganizationScope {

	@Embeddable
	public static class ObjectOrganizationScopeId implements Serializable {
	
		private static final long serialVersionUID = 1L;

		@ManyToOne(fetch=FetchType.EAGER)
		@JoinColumn(name="object_id", referencedColumnName="id")
		protected ObjectEntity object;
		
		@ManyToOne(fetch=FetchType.EAGER)
		@JoinColumn(name="organization_id", referencedColumnName="id")
		protected Organization organization;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((object == null) ? 0 : Long.hashCode(object.getId()));
			result = prime * result + ((organization == null) ? 0 : Long.hashCode(organization.getId()));
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
			ObjectOrganizationScopeId other = (ObjectOrganizationScopeId) obj;
			if (object == null) {
				if (other.object != null)
					return false;
			} else {
				if (other.object == null) {
					return false;
				} else {
					if (object.getId() != other.object.getId()) {
						return false;
					}
				}
			}
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

			return true;
		}
		
		public ObjectOrganizationScopeId() {
			
		}

		public ObjectOrganizationScopeId(ObjectEntity object, Organization organization) {
			super();
			this.object = object;
			this.organization = organization;
		}

		public ObjectEntity getObject() {
			return object;
		}

		public void setObject(ObjectEntity object) {
			this.object = object;
		}

		public Organization getOrganization() {
			return organization;
		}

		public void setOrganization(Organization organization) {
			this.organization = organization;
		}
		
	}

	@EmbeddedId
	private ObjectOrganizationScopeId id;
}
