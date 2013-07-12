package listeners;

import business.ASAPAlbum;
import views.OneAlbumView;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

public class AlbumViewOnClickListener implements AdapterView.OnClickListener {

	private Context context;
	private ASAPAlbum album;
	
	public AlbumViewOnClickListener(Context context, ASAPAlbum album) {
		this.context = context;
		this.album = album;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, OneAlbumView.class);		
		intent.putExtra("albumId", (long) album.getId());
		intent.putExtra("albumName", album.getName());
		context.startActivity(intent);
	}

}
