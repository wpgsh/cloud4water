package net.wapwag.wemp.dao.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.wapwag.wemp.dao.WaterEquipmentDao;

/**
 * 
 * This annotation is used to mark a field that is a reference to a parent
 * object (e.g. a reference from Area to Country). This information is used
 * by {@link WaterEquipmentDao#isAuthorized(long, String, long)} to build
 * a DB query that takes into account transitive permission relations.
 * 
 * Example:
 * 
 * <pre>
 *   &#64;Parent
 *   &#64;ManyToOne
 *   &#64;JoinColumn(name = "country_id")
 *   private Country country;
 *
 * </pre>
 * 
 * @author Alexander Lukichev
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Parent {

}
