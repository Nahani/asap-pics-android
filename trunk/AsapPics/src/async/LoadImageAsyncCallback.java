package async;

import com.example.asappics.R;

import misc.AsyncCallbackResult;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import business.ASAPImage;
import business.SharingConnection;

public class LoadImageAsyncCallback extends AsyncTask<Void, Void, Integer> {

	private Context context;
	private long imageId;
	private long albumId;
	private ASAPImage img;
	private ImageView view;
	
	public LoadImageAsyncCallback(Context context, ImageView view, long imageId, long albumId) {
		this.imageId = imageId;
		this.albumId = albumId;
		this.view = view;
		this.context = context;
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
		
		try {
			Log.d("Verif", "Loading .. ");
			img = SharingConnection.getImage((int) imageId, (int) albumId);
			Log.d("Verif", "Loaded.");
		}catch(Exception e){
			Log.e("AsyncTaskException", "LoadImageAsyncCallback.java: " + e.toString());
			return AsyncCallbackResult.EXCEPTION;
		}

		return AsyncCallbackResult.TRUE;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		if (result == AsyncCallbackResult.TRUE) {
			
			view.setImageBitmap(img.getData());
			view.setTag("full_loaded");
			
		} else {
			
			Toast.makeText(context, context.getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
			
		}
	}
	
}
