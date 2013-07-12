package listeners;

import java.util.List;

import views.OneAlbumView.ImageAdapter;

import com.example.asappics.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;
import async.DeleteImageAsyncCallback;

public class OneAlbumViewOnLongClickListener implements OnLongClickListener {

	private Context context;
	private int position;
	private ImageAdapter adapter;
	private long albumId;
	
	public OneAlbumViewOnLongClickListener(Context context, ImageAdapter adapter, int position, long albumId) {
		this.context = context;
		this.position = position;
		this.adapter = adapter;
		this.albumId = albumId;
	}
	
	@Override
	public boolean onLongClick(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(R.string.one_album_option_dialog_title));
		builder.setItems(R.array.one_album_view_option_array, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if (which == 0) {			// Supprimer
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Suppression")
					.setMessage("Supprimer la photo?")
					.setPositiveButton("Oui", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							new DeleteImageAsyncCallback(context, adapter, position, albumId).execute();
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
					

				} else if (which == 1) {	// Télécharger
					
					Toast.makeText(context, "Télécharger", Toast.LENGTH_LONG).show();

				}
				
			}
			
		});
		builder.create().show();
		
		return false;
	}

}
