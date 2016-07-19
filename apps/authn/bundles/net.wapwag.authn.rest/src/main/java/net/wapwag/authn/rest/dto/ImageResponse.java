package net.wapwag.authn.rest.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImageResponse {
	@XmlElement(name = "id")
	private long id;

	@XmlElement(name = "image")
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
