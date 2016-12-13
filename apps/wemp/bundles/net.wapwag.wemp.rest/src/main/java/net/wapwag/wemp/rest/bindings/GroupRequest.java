package net.wapwag.wemp.rest.bindings;

import net.wapwag.wemp.dao.model.permission.Group;

public class GroupRequest {

    private long id;

    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group toGroup() {
        Group group = new Group();
        group.setName(name);
        group.setId(id);
        return group;
    }

}
