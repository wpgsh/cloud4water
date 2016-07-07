package net.wapwag.authn.dao.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * User data model.
 * Updated by Lee on 2016/7/5.
 */
@Entity
@Table(name="users")
public class User {

	@Id
	@Column(name = "id")
	private long id;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "password_salt")
    private String passwordSalt;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "avatar_id")
    private long avartarId;

    @Column(name = "phone1")
    private String phone1;

    @Column(name = "phone2")
    private String phone2;

    @Column(name = "email")
    private String email;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "email_verification_token")
    private String emailVerifiedToken;

    @Column(name = "email_verification_expiration")
    private boolean emailVerifiedExpiration;

	public long getId() {
		return id;
	}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getAvartarId() {
        return avartarId;
    }

    public void setAvartarId(long avartarId) {
        this.avartarId = avartarId;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmailVerifiedToken() {
        return emailVerifiedToken;
    }

    public void setEmailVerifiedToken(String emailVerifiedToken) {
        this.emailVerifiedToken = emailVerifiedToken;
    }

    public boolean isEmailVerifiedExpiration() {
        return emailVerifiedExpiration;
    }

    public void setEmailVerifiedExpiration(boolean emailVerifiedExpiration) {
        this.emailVerifiedExpiration = emailVerifiedExpiration;
    }

    public void setId(long id) {
		this.id = id;
	}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", enabled=" + enabled +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", passwordSalt='" + passwordSalt + '\'' +
                ", homepage='" + homepage + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avartarId=" + avartarId +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                ", emailVerifiedToken='" + emailVerifiedToken + '\'' +
                ", emailVerifiedExpiration=" + emailVerifiedExpiration +
                '}';
    }
}
