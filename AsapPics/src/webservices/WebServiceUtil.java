package webservices;

import org.json.JSONObject;

public abstract class WebServiceUtil {
	protected static final String WEBSITE_LOCATION = "http://asap-pics.com/REST/";

	protected static JSONParser jParser = new JSONParser();

	protected static JSONObject jObject;

	public static String construct_URL(String base_url, Object... params) {
		String computed_URL = base_url;
		for (Object arg : params) {
			computed_URL += arg + "/";
		}
		return computed_URL.substring(0, computed_URL.length() - 1);
	}
}
