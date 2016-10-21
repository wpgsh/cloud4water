package net.wapwag.wemp.dao.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="object_organization_scope_data")
public class ObjectOrganizationScope {

	@Embeddable
	public static class ObjectOrganizationScopeId implements Serializable {
	
		@ManyToOne(fetch=FetchType.EAGER)
		protected ObjectEntity object;
		
		@ManyToOne(fetch=FetchType.EAGER)
		protected Organization organization;
		
	}

	@EmbeddedId
	private ObjectOrganizationScopeId id;
}
