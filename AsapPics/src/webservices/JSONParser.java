package webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

enum Method_Type {
	POST, GET, PUT
};

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String url, Method_Type type) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpRequestBase request = new HttpGet(url);

			if (type.equals(Method_Type.PUT)) {

				request = new HttpPut(url);

				/*
				 * request.addHeader("Content-Type", "application/json");
				 * request.addHeader("Accept", "application/json"); JSONObject
				 * keyArg = new JSONObject(); try { keyArg.put("image",img); }
				 * catch (JSONException e) { Log.e("ASAP PICS",
				 * "JSONException Error : " + e.getMessage()); }
				 * 
				 * StringEntity input = null; try { input = new
				 * StringEntity(keyArg.toString()); } catch
				 * (UnsupportedEncodingException e) { Log.e("ASAP PICS",
				 * "UnsupportedEncodingException Error : " + e.getMessage()); }
				 * ((HttpPut)request).setEntity(input);
				 */

			} else if (type.equals(Method_Type.POST)) {
				request = new HttpPost(url);
			}

			HttpResponse httpResponse = httpClient.execute(request);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			Log.e("ASAP PICS", "UnsupportedException Error : " + e.getMessage());
		} catch (ClientProtocolException e) {
			Log.e("ASAP PICS",
					"ClientProtocolException Error : " + e.getMessage());
		} catch (IOException e) {
			Log.e("ASAP PICS", "IOException Error : " + e.getMessage());
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		Log.i("JSON Parser", json.toString());

		if (!type.equals(Method_Type.PUT)) {
			try {
				jObj = new JSONObject(json);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
		}
		return jObj;
	}
}