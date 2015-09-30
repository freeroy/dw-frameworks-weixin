package org.developerworld.frameworks.weixin.message.response;

/**
 * 客服相应信息
 * 
 * @author David.LU
 * @date 2015年1月28日 下午8:17:16
 * @emil david.lu@gzgi.com
 * @version 1.0
 */
public class CustomerServiceResponseMessage extends AbstractResponseMessage {

	private String KfAccount;
	
	public String getKfAccount() {
		return KfAccount;
	}

	public void setKfAccount(String kfAccount) {
		KfAccount = kfAccount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((KfAccount == null) ? 0 : KfAccount.hashCode());
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
		CustomerServiceResponseMessage other = (CustomerServiceResponseMessage) obj;
		if (KfAccount == null) {
			if (other.KfAccount != null)
				return false;
		} else if (!KfAccount.equals(other.KfAccount))
			return false;
		return true;
	}

}
