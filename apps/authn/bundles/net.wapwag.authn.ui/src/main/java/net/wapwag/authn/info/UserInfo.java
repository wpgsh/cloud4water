package net.wapwag.authn.info;

import javax.xml.bind.annotation.XmlElement;

/**
 * 用户信息
 * 
 * @author gongll
 *
 */
public class UserInfo {

	@XmlElement(name = "id")
	private long id;

    @XmlElement(name = "enabled")
    private String enabled;

    @XmlElement(name = "username")
    private String username;

    @XmlElement(name = "password_hash")
    private String password_hash;

    @XmlElement(name = "password_salt")
    private String passwordSalt;

    @XmlElement(name = "homepage")
    private String homepage;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "avatar")
    private String avatar;

    @XmlElement(name = "avatar_id")
    private long avartarId;

    @XmlElement(name = "phone1")
    private String phone1;

    @XmlElement(name = "phone2")
    private String phone2;

    @XmlElement(name = "email")
    private String email;

    @XmlElement(name = "email_verified")
    private String emailVerified;

    @XmlElement(name = "email_verification_token")
    private String emailVerifiedToken;

    @XmlElement(name = "email_verification_expiration")
    private String emailVerifiedExpiration;

    @XmlElement(name = "errorMessage")
    private String errorMessage;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmailVerifiedToken() {
        return emailVerifiedToken;
    }

    public void setEmailVerifiedToken(String emailVerifiedToken) {
        this.emailVerifiedToken = emailVerifiedToken;
    }

    public String getEmailVerifiedExpiration() {
        return emailVerifiedExpiration;
    }

    public void setEmailVerifiedExpiration(String emailVerifiedExpiration) {
        this.emailVerifiedExpiration = emailVerifiedExpiration;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", enabled=" + enabled +
                ", username='" + username + '\'' +
                ", password_hash='" + password_hash + '\'' +
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
                ", errorMessage=" + errorMessage +
                '}';
    }
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}

}
