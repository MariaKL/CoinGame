package kaukau.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Key extends PickupableItem implements Serializable {

	private final int code;

	public Key(int key) {
		super("Key");
		this.code = key;
	}
	
	public Key (){
		this(-1);
	}

	@XmlElement(name = "getCode")
	public int getCode() {
		return code;
	}

}
