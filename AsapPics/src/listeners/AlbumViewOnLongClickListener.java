package listeners;

import views.Album_View.ImageAdapter;

import com.asappics.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.View.OnLongClickListener;
import async.DeleteAlbumAsyncCallback;

public class AlbumViewOnLongClickListener implements OnLongClickListener {

	private Context context;
	private int position;
	private ImageAdapter imageAdapter;
	
	public AlbumViewOnLongClickListener(ImageAdapter imageAdapter, Context context, int position) {
		this.context = context;
		this.position = position;
		this.imageAdapter = imageAdapter;
	}
	
	@Override
	public boolean onLongClick(final View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(R.string.one_album_option_dialog_title));
		builder.setItems(R.array.album_view_option_array, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if (which == 0) {			// Supprimer
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Suppression")
					.setMessage("Supprimer l'album '" + imageAdapter.getAlbumList().get(position).getName() + "'?")
					.setPositiveButton("Oui", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							new DeleteAlbumAsyncCallback(context, imageAdapter, position).execute();
						}
						
					})
					.setNegativeButton("Annuler", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
						
					})
					.create()
					.show();
					

				}			
			}
			
		});
		builder.create().show();
		
		return false;
	}
	
}
