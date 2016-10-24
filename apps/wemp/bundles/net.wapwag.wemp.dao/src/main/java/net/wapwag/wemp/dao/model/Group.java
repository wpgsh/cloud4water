package net.wapwag.wemp.dao.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * User group model
 * Created by Administrator on 2016/9/28 0028.
 */
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
