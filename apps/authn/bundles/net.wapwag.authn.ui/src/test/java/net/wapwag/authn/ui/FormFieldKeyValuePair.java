package net.wapwag.authn.ui;

import java.io.Serializable;

public class FormFieldKeyValuePair implements Serializable{

	private static final long serialVersionUID = 1L;
	// The form field used for receivinguser's input,
	// such as "username" in "<inputtype="text" name="username"/>"
	private String key;
	// The value entered by user in thecorresponding form field,
	// such as "Patrick" the abovementioned formfield "username"
	private String value;

	public FormFieldKeyValuePair(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
