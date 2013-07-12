package views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import misc.AsyncCallbackResult;
import webservices.WebServiceManager;
import business.ASAPImage;
import business.ASAPUser;
import business.SharingConnection;

import com.example.asappics.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import async.LoadImageAsyncCallback;
import async.LoadThumbAsyncCallback;

public class ShowPictureActivity extends Activity {
	
	private TextView titleView;
	private ImageView imageView;
	private long albumId;
	private long imageId;
	private AsyncTask<Void, Void, Integer> loadImageAsyncTask;
	private ASAPImage showedImage;
	
	private class AsynCallBack_SuppresImage extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			try{
				SharingConnection.deleteImage((int) imageId, (int) albumId);
			}catch(Exception e){
				Log.e("AsyncTaskException", "ShowPictureActivity.java: " + e.toString());
				return AsyncCallbackResult.EXCEPTION;
			}
			return AsyncCallbackResult.TRUE;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_picture);
		
		Intent intent = getIntent();
		imageId = intent.getLongExtra("imageId", -1);
		albumId = intent.getLongExtra("albumId", -1);
		
		titleView = (TextView) findViewById(R.id.show_picture_image_title);
		imageView = (ImageView) findViewById(R.id.show_picture_image_id);
		
		/* Récupération */
		showedImage = SharingConnection.getThumbCache().get((int) imageId);
		if (showedImage != null) {
			imageView.setImageBitmap(showedImage.getData());
			titleView.setText(showedImage.getName());
		} else {
			imageView.setImageResource(R.drawable.no_picture_album_background);
			titleView.setText("unknown");
		}
		
		/* Thread pour charger l'image haute résolution */
		loadImageAsyncTask = new LoadImageAsyncCallback(this, imageView, imageId, albumId).execute();
	}

	@Override
	public void onBackPressed() {
		if (loadImageAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
			loadImageAsyncTask.cancel(true);
			loadImageAsyncTask = null;
		}
		
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_picture_view_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	
		case R.id.supprimer_image:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setIcon(R.drawable.poubelle)
				.setTitle("Supprimer image")
				.setMessage(getResources().getString(R.string.delete_image_message))
				.setPositiveButton("OK", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
					
				})
				.setNegativeButton("Annuler", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
			break;
		
		case R.id.telecharger_image:
			
				File asapDir = new File(this.getExternalFilesDir(null) + "/ASAPPics_Directory");
				
				/* Création si n'existe pas */
				if (!asapDir.exists()) {
					if (asapDir.mkdir() || asapDir.mkdirs() || asapDir.isDirectory()) {
						Log.d("Verif", "Création success");
					}
					Log.d("Verif", "Création du répertoire: " + this.getExternalFilesDir(null) + "/ASAPPics_Directory");
				}
				
				File imageFile = new File(asapDir, showedImage.getName());
				
				try {
					
					FileOutputStream outStream = new FileOutputStream(imageFile);
					showedImage.getData().compress(Bitmap.CompressFormat.JPEG, 100, outStream);
					outStream.flush();
					outStream.close();
					
				} catch (FileNotFoundException e) {
					Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Toast.makeText(this, "Erreur système de fichier. Carte SD présente?", Toast.LENGTH_LONG).show();
				}
				
				
			break;
		}
		return true;
	}
}
