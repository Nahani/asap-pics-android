package webservices;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class AlbumService extends WebServiceUtil {
	
	private static final String SERVICE_NAME = "AlbumService.svc/";
	
	private static final String URL_DELETE = WEBSITE_LOCATION + SERVICE_NAME + "delete/";
	private static final String URL_ADD = WEBSITE_LOCATION + SERVICE_NAME + "add/";
	private static final String URL_GET_ALBUM_ID = WEBSITE_LOCATION + SERVICE_NAME + "get_album_id/";
	private static final String URL_GET_ALBUM_NAME = WEBSITE_LOCATION + SERVICE_NAME + "get_name_album/";
	private static final String URL_GET_ALBUMS_ID_FROM_USER = WEBSITE_LOCATION + SERVICE_NAME + "get_albums_from_user/";

	private static final String RESPONSE_TAG_DELETE = "DeleteResult";
	private static final String RESPONSE_TAG_ADD = "AddResult";
	private static final String RESPONSE_TAG_GET_ALBUM_ID = "Get_Album_IDResult";
	private static final String RESPONSE_TAG_GET_ALBUM_NAME = "Get_Name_AlbumResult";
	private static final String RESPONSE_TAG_GET_ALBUMS_ID_FROM_USER = "Get_AlbumsID_From_UserResult";
	
	public static String get_album_name(int id) throws JSONException {
		jObject = jParser.getJSONFromUrl(construct_URL(URL_GET_ALBUM_NAME,id), Method_Type.GET);
		String response_string = "";
		try {
			response_string = jObject.getString(RESPONSE_TAG_GET_ALBUM_NAME);
		} catch (JSONException e) {
			Log.e("ASAP PICS", "Get_Album_Name_result - Error : " + e.getMessage());
			throw e;
		}
		return response_string;
	}
	
	public static int get_album_ID(String name, int idProp) throws JSONException {
		jObject = jParser.getJSONFromUrl(construct_URL(URL_GET_ALBUM_ID,name,idProp), Method_Type.GET);

		int response_int = -1;
		try {
			response_int = jObject.getInt(RESPONSE_TAG_GET_ALBUM_ID);
		} catch (JSONException e) {
			Log.e("ASAP PICS", "Get_Album_IDResult - Error : " + e.getMessage());
			throw e;
		}
		return response_int;
	}
	
	public static List<Integer> get_albums_ID_from_user(int idProp) throws JSONException {
		jObject = jParser.getJSONFromUrl(construct_URL(URL_GET_ALBUMS_ID_FROM_USER,idProp), Method_Type.GET);

		List<Integer> response_array = new ArrayList<Integer>();
		try {
			JSONArray jsonArray = jObject.getJSONArray(RESPONSE_TAG_GET_ALBUMS_ID_FROM_USER);

			for (int i=0; i<jsonArray.length(); i++) {
				response_array.add(jsonArray.getInt(i));
			}
		} catch (JSONException e) {
			Log.e("ASAP PICS", "Get_Albums_ID_From_UserResult - Error : " + e.getMessage());
			throw e;
		}
		return response_array;
	}

	public static boolean add(String name, int idProp) throws JSONException {
		jObject = jParser.getJSONFromUrl(construct_URL(URL_ADD,name,idProp), Method_Type.GET);

		boolean response_boolean = false;
		try {
			response_boolean = jObject.getBoolean(RESPONSE_TAG_ADD);
		} catch (JSONException e) {
			Log.e("ASAP PICS", "addResult - Error : " + e.getMessage());
			throw e;
		}
		return response_boolean;
	}
	
	public static boolean delete(String name, int idProp) throws JSONException {
		jObject = jParser.getJSONFromUrl(construct_URL(URL_DELETE,name,idProp), Method_Type.GET);

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
