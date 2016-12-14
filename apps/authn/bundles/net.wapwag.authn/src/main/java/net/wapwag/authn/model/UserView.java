package net.wapwag.authn.model;

import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;

public class UserView {

    public final long id;

    public final String sub;

    public final String username;

    public final String homepage;

    public final String name;

    public final String title;

    public final String avatar;

    public final String avartarId;

    public final String phone1;

    public final String phone2;

    public final String email;

    private UserView(User user, RegisteredClient client) {
        this.sub = client.getClientId();
        this.id = user.getId();
        this.username = user.getUsername();
        this.homepage = user.getHomepage();
        this.name = user.getName();
        this.title = user.getTitle();
        this.avatar = user.getAvatar();
        this.avartarId = user.getAvartarId();
        this.phone1 = user.getPhone1();
        this.phone2 = user.getPhone2();
        this.email = user.getEmail();
    }


    public static UserView newInstance(User user, RegisteredClient client) {
		return new UserView(user, client);
	}

}
