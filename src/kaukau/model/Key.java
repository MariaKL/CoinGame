package kaukau.model;

import java.awt.Graphics;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class Key extends PickupableItem implements Serializable {

	private int code;
	
	public Key(int key) {
		super("Key");
		this.code = key;
	}

	@XmlElement(name = "getCode")
	public int getCode() {
		return code;
	}
	
	public void setCode(int code){
		this.code = code;
	}

}
