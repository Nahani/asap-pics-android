package async;

import views.OneAlbumView.ImageAdapter;

import com.example.asappics.R;

import misc.AsyncCallbackResult;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import business.SharingConnection;

public class AddPictureAsyncCallback extends AsyncTask<Void, Void, Integer> {

	private Bitmap image;
	private long albumId;
	private String name;
	private Context context;
	private ImageAdapter adapter;
	private ProgressDialog progressDialog;
	
	public AddPictureAsyncCallback(Context context, ImageAdapter adapter, long albumId, String name, Bitmap image) {
		this.image = image;
		this.albumId = albumId;
		this.name = name;
		this.context = context;
		this.adapter = adapter;
		progressDialog = new ProgressDialog(context);
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog.setMessage("Téléversement de l'image");
		progressDialog.show();
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
		try {
			System.gc();
			
			/* Ajout sur le serveur puis désallocation du Bitmap */
			SharingConnection.addImage((int) albumId, name, image);
			image.recycle();
			
			System.gc();
			
			return AsyncCallbackResult.TRUE;
		} catch (Exception e) {
			Log.e("AsyncTask", "AddPictureAsyncCallback: " + e.toString());
			return AsyncCallbackResult.EXCEPTION;
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		
		progressDialog.dismiss();
		
		if (result == AsyncCallbackResult.TRUE) {
			
			/* Rafraîchi la vue */
			adapter.getViews().clear();
			adapter.notifyDataSetChanged();
			Toast.makeText(context, "Image ajoutée", Toast.LENGTH_LONG).show();
			
		} else if (result == AsyncCallbackResult.EXCEPTION) {
			Toast.makeText(context, context.getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
		}
	}
	
}
