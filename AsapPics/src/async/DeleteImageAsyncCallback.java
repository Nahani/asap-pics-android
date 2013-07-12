package async;

import java.util.List;

import views.OneAlbumView.ImageAdapter;

import misc.AsyncCallbackResult;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import business.SharingConnection;

import com.example.asappics.R;

public class DeleteImageAsyncCallback extends AsyncTask<Void, Void, Integer> {
	
	private Context context;
	private List<Integer> imageList;
	private ImageAdapter adapter;
	private int position;
	private long albumId;
	
	public DeleteImageAsyncCallback(Context context, ImageAdapter adapter, int pos, long albumId) {
		this.imageList = adapter.getImageList();
		this.adapter = adapter;
		this.position = pos;
		this.context = context;
		this.albumId = albumId;
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
		
		try {
			boolean r = SharingConnection.deleteImage((int) imageList.get(position), (int) albumId);
			
			if (r) {
				return AsyncCallbackResult.TRUE;
			} else {
				return AsyncCallbackResult.FALSE;
			}
			
		} catch (Exception e) {
			Log.e("AsyncTask", "DeleteImageAsyncTask: " + e.toString());
			return AsyncCallbackResult.EXCEPTION;
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		
		if (result == AsyncCallbackResult.TRUE) {
			
			adapter.getViews().remove(position);
			adapter.notifyDataSetChanged();
			
			Toast.makeText(context, "Image supprimée", Toast.LENGTH_LONG).show();
		} else if (result == AsyncCallbackResult.FALSE) {
			Toast.makeText(context, "L'image n'existe plus. Veuillez rafraîchir.", Toast.LENGTH_LONG).show();
		} else if (result == AsyncCallbackResult.EXCEPTION) {
			Toast.makeText(context, context.getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
		}
	}
	
}
