package com.hakmesb.lelabovert.config;

public class AppConstants {
	
	public static final String[] PUBLIC_URLS = {"/api/v1/login/**", "/api/v1/register/**", "/api/v1/public/**"};
	public static final String[] USER_URLS = {"/api/v1/user/**", "/api/v1/logout"};
	public static final String[] ADMIN_URLS = {"/api/v1/admin/**"};

//	@Value("${lelabovert.const.page.number:0}")
	public static final String PAGE_NUMBER = "0";
	public static final String PAGE_SIZE = "10";
	public static final String SORT_CATEGORIES_BY = "id";
	public static final String SORT_PRODUCTS_BY = "id";
	public static final String SORT_DIR = "asc";
}
