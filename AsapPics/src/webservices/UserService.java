package webservices;

import org.json.JSONException;

import android.util.Log;

public class UserService extends WebServiceUtil {
	private static final String SERVICE_NAME = "UserService.svc/";
	
	private static final String URL_GET_USER_ID = WEBSITE_LOCATION + SERVICE_NAME + "get_user_id/";
	private static final String URL_GET_USER_LEVEL = WEBSITE_LOCATION + SERVICE_NAME + "get_user_level/";
	private static final String URL_CHECK_PASSWORD = WEBSITE_LOCATION + SERVICE_NAME + "check_password/";
	private static final String URL_DELETE = WEBSITE_LOCATION + SERVICE_NAME + "delete/";
	private static final String URL_ADD = WEBSITE_LOCATION + SERVICE_NAME + "add/";
	
	private static final String RESPONSE_TAG_DELETE = "DeleteResult";
	private static final String RESPONSE_TAG_ADD = "AddResult";
	private static final String RESPONSE_TAG_CHECK_PASSWORD = "Check_passwordResult";
	private static final String RESPONSE_TAG_GET_USER_ID = "Get_User_IDResult";
	private static final String RESPONSE_TAG_GET_USER_LEVEL = "Get_User_LevelResult";
	
	public static boolean get_user_level(String login) throws JSONException{
		jObject = jParser.getJSONFromUrl(construct_URL(URL_GET_USER_LEVEL,login), Method_Type.GET);
		boolean response_boolean = false;
		try {
			response_boolean = jObject.getBoolean(RESPONSE_TAG_GET_USER_LEVEL);
		} catch (JSONException e) {
			Log.e("ASAP PICS", "Get_User_Level_result - Error : " + e.getMessage());
			throw e;
		}
		return response_boolean;
	}
	
	public static int get_user_ID(String login) throws JSONException{
		jObject = jParser.getJSONFromUrl(construct_URL(URL_GET_USER_ID,login), Method_Type.GET);

		int response_int = -1;
		try {
			response_int = jObject.getInt(RESPONSE_TAG_GET_USER_ID);
		} catch (JSONException e) {
			Log.e("ASAP PICS", "Get_User_IDResult - Error : " + e.getMessage());
			throw e;
		}
		return response_int;
	}
	
	public static boolean check_password(String login, String password) throws Exception {
		jObject = jParser.getJSONFromUrl(construct_URL(URL_CHECK_PASSWORD,login,password), Method_Type.GET);

		boolean response_boolean = false;
		try {
			response_boolean = jObject.getBoolean(RESPONSE_TAG_CHECK_PASSWORD);
		} catch (Exception e) {
			Log.e("ASAP PICS", "Check_PasswordResult - Error : " + e.getMessage());
			throw e;
		}
		return response_boolean;
	}
	
	public static boolean add(String first_name, String last_name, String login, String mail, String pwd) throws JSONException{
		jObject = jParser.getJSONFromUrl(construct_URL(URL_ADD,first_name,last_name,login,mail,pwd), Method_Type.GET);

		boolean response_boolean = false;
		try {
			response_boolean = jObject.getBoolean(RESPONSE_TAG_ADD);
		} catch (JSONException e) {
			Log.e("ASAP PICS", "addResult - Error : " + e.getMessage());
			throw e;
		}
		return response_boolean;
	}
	
	public static boolean delete(String login) throws JSONException{
		jObject = jParser.getJSONFromUrl(construct_URL(URL_DELETE,login), Method_Type.GET);

		boolean response_boolean = false;
		try {
			response_boolean = jObject.getBoolean(RESPONSE_TAG_DELETE);
		} catch (JSONException e) {
			Log.e("ASAP PICS", "deleteResult - Error : " + e.getMessage());
			throw e;
		}
		return response_boolean;
	}

}
