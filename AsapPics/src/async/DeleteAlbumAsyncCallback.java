package async;

import java.util.List;

import views.Album_View.ImageAdapter;

import misc.AsyncCallbackResult;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import business.ASAPAlbum;
import business.ASAPUser;
import business.SharingConnection;

import com.example.asappics.R;

public class DeleteAlbumAsyncCallback extends AsyncTask<Void, Void, Integer> {
	
	private Context context;
	private List<ASAPAlbum> albumList;
	private ImageAdapter adapter;
	private int position;
	
	public DeleteAlbumAsyncCallback(Context context, ImageAdapter adapter, int pos) {
		this.albumList = adapter.getAlbumList();
		this.adapter = adapter;
		this.position = pos;
		this.context = context;
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
		
		try {
			boolean r = SharingConnection.deleteAlbum(albumList.get(position).getName(), ASAPUser.id);
			
			if (r) {
				return AsyncCallbackResult.TRUE;
			} else {
				return AsyncCallbackResult.FALSE;
			}
			
		} catch (Exception e) {
			Log.e("AsyncTask", "DeleteAlbumAsyncTask: " + e.toString());
			return AsyncCallbackResult.EXCEPTION;
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		
		if (result == AsyncCallbackResult.TRUE) {				
			String name = albumList.get(position).getName();
			albumList.remove(position);
			adapter.getViews().remove(position);
			adapter.notifyDataSetChanged();
			
			Toast.makeText(context, "Album '" + name + "' supprimé", Toast.LENGTH_LONG).show();
		} else if (result == AsyncCallbackResult.FALSE) {
			Toast.makeText(context, albumList.get(position).getName() + " n'existe pas. Veuillez rafraîchir.", Toast.LENGTH_LONG).show();
		} else if (result == AsyncCallbackResult.EXCEPTION) {
			Toast.makeText(context, context.getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
		}
	}
	
}
