package kaukau.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
 public class Row {
	private ArrayList<Tile> row;

	public Row(){
		row = new ArrayList<Tile>();
	}
	
	public void setRow(ArrayList<Tile> row2) {
		this.row = row2;
	}

//	public Row() {
//		this(null);
//	}

	@XmlElementWrapper(name = "getRows")
	@XmlElements({ @XmlElement(name = "tile") })
	public ArrayList<Tile> getRows() {
		return this.row;
	}
}
