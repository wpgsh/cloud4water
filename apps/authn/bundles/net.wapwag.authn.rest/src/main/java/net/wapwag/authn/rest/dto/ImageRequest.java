package net.wapwag.authn.rest.dto;

import javax.ws.rs.FormParam;

public class ImageRequest {
	@FormParam("id")
	private long id;

	@FormParam("image")
	private String image;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Image{" + "id=" + id + ",  image=" + image + '}';
	}

}
