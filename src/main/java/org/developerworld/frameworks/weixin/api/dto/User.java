package org.developerworld.frameworks.weixin.api.dto;

import java.io.Serializable;

/**
 * 用户对象
 * 
 * @author Roy Huang
 * @version 20140309
 * 
 */
public class User implements Serializable{

	/**
	 * 已关注
	 */
	public final static Byte SUBSCRIPE_SUBSCRIPE = 1;
	/**
	 * 未关注
	 */
	public final static Byte SUBSCRIPE_UNSUBSCRIPE = 0;	
	/**
	 * 不清楚性别
	 */
	public final static Byte SEX_UNKNOW = 0;
	/**
	 * 男性
	 */
	public final static Byte SEX_MAN = 1;
	/**
	 * 女性
	 */
	public final static Byte SEX_LADY = 2;

	private Byte subscribe;
	private String openId;
	private String nickname;
	private Byte sex;
	private String language;
	private String country;
	private String province;
	private String city;
	private String headImgUrl;
	private Long subscribeTime;

	public Byte getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Byte subscribe) {
		this.subscribe = subscribe;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Byte getSex() {
		return sex;
	}

	public void setSex(Byte sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public Long getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Long subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	@Override
	public String toString() {
		return "User [subscribe=" + subscribe + ", openId=" + openId
				+ ", nickname=" + nickname + ", sex=" + sex + ", language="
				+ language + ", country=" + country + ", province=" + province
				+ ", city=" + city + ", headImgUrl=" + headImgUrl
				+ ", subscribeTime=" + subscribeTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((headImgUrl == null) ? 0 : headImgUrl.hashCode());
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result
				+ ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + ((openId == null) ? 0 : openId.hashCode());
		result = prime * result
				+ ((province == null) ? 0 : province.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result
				+ ((subscribe == null) ? 0 : subscribe.hashCode());
		result = prime * result
				+ ((subscribeTime == null) ? 0 : subscribeTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (headImgUrl == null) {
			if (other.headImgUrl != null)
				return false;
		} else if (!headImgUrl.equals(other.headImgUrl))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (openId == null) {
			if (other.openId != null)
				return false;
		} else if (!openId.equals(other.openId))
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (subscribe == null) {
			if (other.subscribe != null)
				return false;
		} else if (!subscribe.equals(other.subscribe))
			return false;
		if (subscribeTime == null) {
			if (other.subscribeTime != null)
				return false;
		} else if (!subscribeTime.equals(other.subscribeTime))
			return false;
		return true;
	}

}
