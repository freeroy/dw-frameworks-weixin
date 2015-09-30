package org.developerworld.frameworks.weixin.api;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.developerworld.commons.lang.StringUtils;

public abstract class AbstractBaseApi {

	/**
	 * 序列化对象属性到xml字符串
	 * @param object
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	protected static String objectToXmlStr(Object object)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		StringBuilder rst = new StringBuilder();
		if (object != null) {
			PropertyDescriptor[] pros = PropertyUtils
					.getPropertyDescriptors(object);
			for (int i = 0; i < pros.length; i++) {
				PropertyDescriptor pro = pros[i];
				String name = pro.getName();
				if ("class".equals(name))
					continue; // No point in trying to set an object's class
				String value = BeanUtils.getProperty(object, name);
				if (value != null) {
					String field = StringUtils.camelToUnderling(name);
					rst.append("<").append(field).append(">").append(value)
							.append("</").append(field).append(">");
				}
			}
		}
		return rst.toString();
	}
	
}
