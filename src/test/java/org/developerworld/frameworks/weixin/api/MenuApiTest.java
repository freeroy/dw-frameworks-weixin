package org.developerworld.frameworks.weixin.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.developerworld.frameworks.weixin.api.dto.Menu;
import org.developerworld.frameworks.weixin.api.dto.MenuType;
import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MenuApiTest {

	private static String accessToken;
	private static ObjectMapper objectMapper;

	//@BeforeClass
	public static void before() throws IOException {
		accessToken = AccessTokenApi.getAccessToken(TestConfig.APP_ID,
				TestConfig.SECRET);
		objectMapper = new ObjectMapper();
	}

	//@AfterClass
	public static void after() {
		accessToken = null;
		objectMapper = null;
	}

	//@Test
	public void testGetMenuStringMap() throws HttpException, IOException {
		Map result = new HashMap();
		MenuApi.getMenu(accessToken, result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("menu result:" + result);
	}

	//@Test
	public void testGetMenuString() throws HttpException, IOException {
		List<Menu> menus = MenuApi.getMenu(accessToken);
		Assert.assertNotNull(menus);
		System.out.println("menu json:"
				+ objectMapper.writeValueAsString(menus));
	}

	//@Test
	public void testDeleteMenuStringMap() throws HttpException, IOException {
		Map result = new HashMap();
		MenuApi.deleteMenu(accessToken, result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("delete menu result:" + result);
	}

	//@Test
	public void testDeleteMenuString() throws HttpException, IOException {
		boolean rst = MenuApi.deleteMenu(accessToken);
		System.out.println("delete menu:" + rst);
	}

	//@Test
	public void testCreateMenuStringListOfMenuMap() throws HttpException,
			IOException {
		Map result = new HashMap();
		List<Menu> menus = buildMenus();
		MenuApi.createMenu(accessToken, menus, result);
		Assert.assertTrue(result.size() > 0);
		System.out.println("create menu result:" + result);
	}

	//@Test
	public void testCreateMenuStringListOfMenu() throws HttpException,
			IOException {
		List<Menu> menus = buildMenus();
		boolean rst = MenuApi.createMenu(accessToken, menus);
		System.out.println("create menu:" + rst);
	}

	/**
	 * 创建菜单
	 * 
	 * @return
	 */
	private List<Menu> buildMenus() {
		List<Menu> menus = new ArrayList<Menu>();
		for (int i = 0; i < 3; i++) {
			Menu menu = new Menu();
			menu.setName("name" + i);
			for (int j = 0; j < 5; j++) {
				Menu menu2 = new Menu();
				menu2.setKey("key" + i + "-" + j);
				menu2.setName("name" + i + "-" + j);
				menu2.setUrl("url" + i + "-" + j);
				menu2.setType(j % 2 == 0 ? MenuType.CLICK : MenuType.VIEW);
				menu.getSubButton().add(menu2);
			}
			menus.add(menu);
		}
		return menus;
	}

}
