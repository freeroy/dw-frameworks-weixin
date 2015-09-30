package org.developerworld.frameworks.weixin.message.request;

/**
 * 地理位置信息
 * 
 * @author Roy Huang
 * @version 20140307
 * 
 */
public class LocationRequestMessage extends AbstractRequestMessage {

	private Double locationX;
	private Double locationY;
	private Integer scale;
	private String label;

	public Double getLocationX() {
		return locationX;
	}

	public void setLocationX(Double locationX) {
		this.locationX = locationX;
	}

	public Double getLocationY() {
		return locationY;
	}

	public void setLocationY(Double locationY) {
		this.locationY = locationY;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result
				+ ((locationX == null) ? 0 : locationX.hashCode());
		result = prime * result
				+ ((locationY == null) ? 0 : locationY.hashCode());
		result = prime * result + ((scale == null) ? 0 : scale.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationRequestMessage other = (LocationRequestMessage) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (locationX == null) {
			if (other.locationX != null)
				return false;
		} else if (!locationX.equals(other.locationX))
			return false;
		if (locationY == null) {
			if (other.locationY != null)
				return false;
		} else if (!locationY.equals(other.locationY))
			return false;
		if (scale == null) {
			if (other.scale != null)
				return false;
		} else if (!scale.equals(other.scale))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocationRequestMessage [locationX=" + locationX
				+ ", locationY=" + locationY + ", scale=" + scale + ", label="
				+ label + "]";
	}

}
