package webservices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageService extends WebServiceUtil {

	private static final String SERVICE_NAME = "ImageService.svc/";

	private static final String URL_GET_IMAGE_NAME = WEBSITE_LOCATION + SERVICE_NAME + "get_image_name/";
	private static final String URL_GET_IMAGE_ID = WEBSITE_LOCATION + SERVICE_NAME + "get_image_id/";
	private static final String URL_DELETE = WEBSITE_LOCATION + SERVICE_NAME + "delete/";
	private static final String URL_ADD = WEBSITE_LOCATION + SERVICE_NAME + "add/";
	private static final String URL_GET = WEBSITE_LOCATION + SERVICE_NAME + "get/";
	private static final String URL_GET_THUMB = WEBSITE_LOCATION + SERVICE_NAME + "get_thumb/";
	private static final String URL_GET_IMAGES_ID_FROM_ALBUM = WEBSITE_LOCATION + SERVICE_NAME + "get_images_id_from_album/";

	private static final String RESPONSE_TAG_GET_IMAGE_NAME = "Get_Image_NameResult";
	private static final String RESPONSE_TAG_GET_IMAGE_ID = "Get_Image_IDResult";
	private static final String RESPONSE_TAG_DELETE = "DeleteResult";
	private static final String RESPONSE_TAG_GET_IMAGES_ID_FROM_ALBUM = "Get_Images_ID_From_AlbumResult";

	public static String get_image_name(int id, int idAlbum) throws JSONException{
		jObject = jParser.getJSONFromUrl(construct_URL(URL_GET_IMAGE_NAME,id,idAlbum), Method_Type.GET);

		String response_string = "";
		try {
			response_string = jObject.getString(RESPONSE_TAG_GET_IMAGE_NAME);
		} catch (JSONException e) {
			Log.e("ASAP PICS", "Get_Image_NameResult - Error : " + e.getMessage());
			throw e;
		}
		return response_string;
	}

	public static int get_image_ID(int idAlbum, String name) throws JSONException{
		jObject = jParser.getJSONFromUrl(construct_URL(URL_GET_IMAGE_ID,idAlbum,name), Method_Type.GET);

		int response_int = -1;
		try {
			response_int = jObject.getInt(RESPONSE_TAG_GET_IMAGE_ID);
		} catch (JSONException e) {
			Log.e("ASAP PICS", "Get_Image_IDResult - Error : " + e.getMessage());
			throw e;
		}
		return response_int;
	}

	public static List<Integer> get_images_ID_from_album(int idAlbum) throws JSONException{
		jObject = jParser.getJSONFromUrl(construct_URL(URL_GET_IMAGES_ID_FROM_ALBUM,idAlbum), Method_Type.GET);
		
		List<Integer> response_array = new ArrayList<Integer>();
		try {
			JSONArray jsonArray = jObject.getJSONArray(RESPONSE_TAG_GET_IMAGES_ID_FROM_ALBUM);

			for (int i=0; i<jsonArray.length(); i++) {
				response_array.add(jsonArray.getInt(i));
			}
		} catch (JSONException e) {
			Log.e("ASAP PICS", "Get_Images_ID_From_AlbumResult - Error : " + e.getMessage());
			throw e;
		}
		return response_array;
	}

	public static void add(int idAlbum, String name, Bitmap img) throws Exception{

		HttpPost post = new HttpPost(construct_URL(URL_ADD,idAlbum,name));
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		img.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte[] data = bao.toByteArray();		

		ByteArrayEntity bimg = new ByteArrayEntity(data);
		post.setEntity(bimg);

		new DefaultHttpClient().execute(post);
	}
	
	public static Bitmap get(int id, int idAlbum) throws Exception {
		Bitmap bitmap = null;
		 BitmapFactory.Options bmOptions;
	        bmOptions = new BitmapFactory.Options();
	        bmOptions.inSampleSize = 1;
	        InputStream in = null;

            in = OpenHttpConnection(construct_URL(URL_GET,id,idAlbum));
            bitmap = BitmapFactory.decodeStream(in, null, bmOptions);
            in.close();
	        
	        return bitmap;
	}
	
	public static Bitmap get_thumb(int id, int idAlbum) throws Exception {
		Bitmap bitmap = null;
		 BitmapFactory.Options bmOptions;
	        bmOptions = new BitmapFactory.Options();
	        bmOptions.inSampleSize = 1;
	        InputStream in = null;

            in = OpenHttpConnection(construct_URL(URL_GET_THUMB,id,idAlbum));
            bitmap = BitmapFactory.decodeStream(in, null, bmOptions);
            in.close();
	        
	        return bitmap;
	}
	
	private static InputStream OpenHttpConnection(String strURL) throws IOException {
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        HttpURLConnection httpConn = (HttpURLConnection) conn;
        httpConn.setRequestMethod("GET");
        httpConn.connect();

        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            inputStream = httpConn.getInputStream();
        }

        return inputStream;
    }
	
	

	public static boolean delete(int id, int idAlbum) throws JSONException {
		jObject = jParser.getJSONFromUrl(construct_URL(URL_DELETE,id,idAlbum), Method_Type.GET);

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
