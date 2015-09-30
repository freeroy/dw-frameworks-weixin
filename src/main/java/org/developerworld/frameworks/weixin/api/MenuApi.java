package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.developerworld.frameworks.weixin.api.dto.Menu;
import org.developerworld.frameworks.weixin.api.dto.MenuType;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 菜单api
 * 
 * @author Roy Huang
 * @version 20140309
 * 
 */
public class MenuApi {

	private final static String CREATE_MENU_API = "https://api.weixin.qq.com/cgi-bin/menu/create";
	private final static String GET_MENU_API = "https://api.weixin.qq.com/cgi-bin/menu/get";
	private final static String DELETE_MENU_API = "https://api.weixin.qq.com/cgi-bin/menu/delete";

	private final static ObjectMapper objectMapper = new ObjectMapper();

	private final static String REQUEST_CONTENT_CHARSET="UTF-8";
	
	/**
	 * 创建菜单
	 * 
	 * @param accessToken
	 * @param menus
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void createMenu(String accessToken, List<Menu> menus,
			Map result) throws HttpException, IOException {
		String url = CREATE_MENU_API + "?access_token=" + accessToken;
		String jsonStr = toJsonStr(menus);
		PostMethod method = new PostMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient httpClient = new HttpClient();
			RequestEntity requestEntity = new StringRequestEntity(jsonStr,
					"application/x-www-form-urlencoded", "utf-8");
			method.setRequestEntity(requestEntity);
			int status = httpClient.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map json = objectMapper.readValue(response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 获取json字符串
	 * 
	 * @param menus
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static String toJsonStr(List<Menu> menus)
			throws JsonGenerationException, JsonMappingException, IOException {
		Map root = new LinkedHashMap();
		List<Map> button = buildMenus(menus);
		root.put("button", button);
		return objectMapper.writeValueAsString(root);
	}

	/**
	 * 构建菜单
	 * 
	 * @param subButton
	 * @return
	 */
	private static List<Map> buildMenus(List<Menu> menus) {
		List<Map> rst = new ArrayList<Map>();
		if (menus != null && menus.size() > 0) {
			for (Menu menu : menus) {
				Map _m = new LinkedHashMap();
				_m.put("name", menu.getName());
				if (menu.getSubButton() != null
						&& menu.getSubButton().size() > 0)
					_m.put("sub_button", buildMenus(menu.getSubButton()));
				else {
					_m.put("type", menu.getType().toString());
					if (MenuType.CLICK.equals(menu.getType()))
						_m.put("key", menu.getKey());
					else
						_m.put("url", menu.getUrl());
				}
				rst.add(_m);
			}
		}
		return rst;
	}

	/**
	 * 创建菜单
	 * 
	 * @param accessToken
	 * @param menus
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static boolean createMenu(String accessToken, List<Menu> menus)
			throws HttpException, IOException {
		boolean rst = false;
		Map result = new HashMap();
		createMenu(accessToken, menus, result);
		if (result.get("errcode") != null)
			rst = result.get("errcode").toString().equals("0");
		return rst;
	}

	/**
	 * 
	 * @param accessToken
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void getMenu(String accessToken, Map result)
			throws HttpException, IOException {
		String url = GET_MENU_API + "?access_token=" + accessToken;
		GetMethod method = new GetMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient client = new HttpClient();
			int status = client.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map json = objectMapper.readValue(response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 获取菜单
	 * 
	 * @param accessToken
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static List<Menu> getMenu(String accessToken) throws HttpException,
			IOException {
		List<Menu> rst = new ArrayList<Menu>();
		Map result = new HashMap();
		getMenu(accessToken, result);
		if (result.get("menu") != null && result.get("menu") instanceof Map) {
			Map menuM = (Map) result.get("menu");
			if (menuM.get("button") != null
					&& menuM.get("button") instanceof Collection) {
				Collection menus = (Collection) menuM.get("button");
				rst.addAll(getSubButton(menus));
			}
		}
		return rst;
	}

	/**
	 * 获取子菜单
	 * 
	 * @param menus
	 * @return
	 */
	private static List<Menu> getSubButton(Collection menus) {
		List<Menu> rst = new ArrayList<Menu>();
		for (Object menu : menus) {
			Map _menu = (Map) menu;
			Menu menuObj = new Menu();
			menuObj.setName((String) _menu.get("name"));
			if (_menu.get("sub_button") != null
					&& _menu.get("sub_button") instanceof Collection
					&& ((Collection) _menu.get("sub_button")).size() > 0) {
				menuObj.setSubButton(getSubButton((Collection) _menu
						.get("sub_button")));
			} else {
				if (_menu.get("type").equals(MenuType.CLICK.toString())) {
					menuObj.setType(MenuType.CLICK);
					menuObj.setKey((String) _menu.get("key"));
				} else {
					menuObj.setType(MenuType.VIEW);
					menuObj.setUrl((String) _menu.get("url"));
				}
			}
			rst.add(menuObj);
		}
		return rst;
	}

	/**
	 * 删除菜单
	 * 
	 * @param accessToken
	 * @param result
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void deleteMenu(String accessToken, Map result)
			throws HttpException, IOException {
		String url = DELETE_MENU_API + "?access_token=" + accessToken;
		GetMethod method = new GetMethod(url);
		try {
			method.getParams().setContentCharset(REQUEST_CONTENT_CHARSET);
			HttpClient client = new HttpClient();
			int status = client.executeMethod(method);
			if (status == HttpStatus.SC_OK) {
				if (result != null) {
					String response = method.getResponseBodyAsString();
					Map json = objectMapper.readValue(response, Map.class);
					result.putAll(json);
				}
			}
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 删除菜单
	 * 
	 * @param accessToken
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static boolean deleteMenu(String accessToken) throws HttpException,
			IOException {
		boolean rst = false;
		Map result = new HashMap();
		deleteMenu(accessToken, result);
		if (result.get("errcode") != null)
			rst = result.get("errcode").toString().equals("0");
		return rst;
	}
}
