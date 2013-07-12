package views;

import java.util.ArrayList;
import java.util.List;

import listeners.AlbumViewOnClickListener;
import listeners.AlbumViewOnLongClickListener;
import misc.AsyncCallbackResult;

import business.ASAPAlbum;
import business.ASAPUser;
import business.SharingConnection;

import com.example.asappics.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import async.LoadThumbAsyncCallback;

public class Album_View extends Activity {
	
	private List<ASAPAlbum> albums;
	private ListView listView;
	
	/**
	 * Tâche asyncrhone. Charge les albums de l'utilisateur.
	 * @author Alex
	 *
	 */
	private class LoadAlbumsAsyncCallback extends AsyncTask<Void, Void, Integer> {
		
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(Album_View.this);
			progressDialog.setMessage("Chargement des albums");
			progressDialog.show();
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			try {
			    albums = SharingConnection.getAlbumsFromUser(ASAPUser.id);
			} catch(Exception e) {
				Log.e("AsyncTaskException", "Album_View.java::LoadAlbumAsyncCallback: " + e.toString());
				return AsyncCallbackResult.EXCEPTION;
			}
			
			return AsyncCallbackResult.TRUE;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			
			if (progressDialog.isShowing())
				progressDialog.dismiss();
			
			if (result == AsyncCallbackResult.TRUE)
				refreshAdapter();
			else if (result == AsyncCallbackResult.EXCEPTION)
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
		}		
	}
	
	/**
	 * Tâche asynchrone. Ajout d'un album sur le serveur.
	 * @author Alex
	 *
	 */
	private class AddAlbumAsyncCallback extends AsyncTask<String, Void, Integer>{

		@Override
		protected Integer doInBackground(String... params) {
			boolean result;
			
			try {
				result = SharingConnection.addAlbum(params[0], ASAPUser.id);
			} catch(Exception e){
				Log.e("AsyncTaskException", "Album_View.java::AddAlbumAsyncCallback: " + e.toString());
				return AsyncCallbackResult.EXCEPTION;
			}
			
			if (result)
				return AsyncCallbackResult.TRUE;
			else
				return AsyncCallbackResult.FALSE;
		}
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list_view);
        
        listView = (ListView) findViewById(R.id.album_view_list_view);
        albums = new ArrayList<ASAPAlbum>();
        
        /* Chargement asynchrone des albums */
        (new LoadAlbumsAsyncCallback()).execute();
    } 
	
	private void refreshAdapter() {
		ImageAdapter imageAdapter = new ImageAdapter(this, (ArrayList<ASAPAlbum>) albums);
		listView.setAdapter(imageAdapter);
	}
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);		
		builder.setTitle("Quitter l'application")
				.setMessage(this.getResources().getString(R.string.quit_message))
				.setPositiveButton("Oui", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
					
				})
				.setNegativeButton("Non",  new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
					
				})
				.create()
				.show();
	}
	
	/**
	 * Création du menu déroulant.
	 * Activé par le bouton "menu" du téléphone
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.album_view_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Listener. écoute la sélection du menu déroulant
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {	
		case R.id.album_view_menu_ajout:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			View addAlbumView = inflater.inflate(R.layout.add_album_dialog, null);
			final EditText editText = (EditText) addAlbumView.findViewById(R.id.add_album_text_field);
			
			builder.setTitle("Nouvel album")
					.setView(addAlbumView)
					.setPositiveButton("Créer", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							String newAlbumName = editText.getText().toString();
							AsyncTask<String, Void, Integer> asyncTask = (new AddAlbumAsyncCallback()).execute(newAlbumName);
							Integer result = AsyncCallbackResult.EXCEPTION;
							
							try {
								result = asyncTask.get();
							} catch (Exception e) {}
							
							if (result == AsyncCallbackResult.TRUE) {
								
								/* Ferme le Dialog, récupère la nouvelle liste des albums et rafraichi l'adapter */
								dialog.dismiss();
								(new LoadAlbumsAsyncCallback()).execute();
								
							} else if (result == AsyncCallbackResult.FALSE) {
								
								Toast.makeText(getApplicationContext(), "L'album existe déjà", Toast.LENGTH_LONG).show();
								
							} else {
								
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
								
							}
						}
						
					})
					.setNegativeButton("Annuler",  new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
						
					})
					.create()
					.show();
			
					break;
			
		case R.id.album_view_refresh_menu:
			(new LoadAlbumsAsyncCallback()).execute();
			break;
			
		case R.id.album_view_disconnect_menu:
			startActivity(new Intent(this, LoginView.class));
			finish();
			break;				
		}
		
		return true;
	}
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    private ArrayList<ASAPAlbum> mThumbIds;
	    private SparseArray<View> views;

	    public ImageAdapter(Context c, ArrayList<ASAPAlbum> albums) {
	        mContext = c;
	        mThumbIds = albums;
	        views = new SparseArray<View>();
	    }
	    
	    @Override
		public int getCount() {
	        return mThumbIds.size();
	    }

	    @Override
		public Object getItem(int position) {
	        return mThumbIds.get(position);
	    }

	    @Override
		public long getItemId(int position) {
	        return position;
	    }

	    public List<ASAPAlbum> getAlbumList() {
	    	return mThumbIds;
	    }
	    
	    public SparseArray<View> getViews() {
	    	return views;
	    }
	    
	    @Override
		public View getView(int position, View convertView, ViewGroup parent) {
	    	Point p = new Point();
	    	Display display = getWindowManager().getDefaultDisplay();
//	    	display.getRealSize(p); // Compatible uniquement API >= 17
//	    	int size = p.x / 4;
	    	int size = display.getWidth() / 4;
	    	View rowView = views.get(position);
	    	
	        if (rowView == null) {
	        	rowView = new View(mContext);
	            
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				rowView = (View) inflater.inflate(R.layout.album_view_row, null);
				
				TextView rowTitleView = (TextView) rowView.findViewById(R.id.album_view_row_title);
				TextView rowInfoView = (TextView) rowView.findViewById(R.id.album_view_row_info);
				ImageView imageView = (ImageView) rowView.findViewById(R.id.album_view_image);
				
				rowTitleView.setText(mThumbIds.get(position).getName());
				rowInfoView.setText(mThumbIds.get(position).getImages().size() + " photo(s)");
				
				/* Image par défaut en attendant le chargement */
				imageView.setImageResource(R.drawable.no_picture_album_background);
				
				/* Paramètres layout */
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
				
				/* Listeners */
				rowView.setOnClickListener(new AlbumViewOnClickListener(mContext, mThumbIds.get(position)));
				rowView.setOnLongClickListener(new AlbumViewOnLongClickListener(this, mContext, position));
				
		        /* Bundle pour transfert vers les threads */
		        Bundle bundle = new Bundle();
		        bundle.putInt("albumId", mThumbIds.get(position).getId());
		        int lsize = mThumbIds.get(position).getImages().size();
		        
		        if (lsize > 0)
		        	bundle.putInt("imageId", mThumbIds.get(position).getImages().get(0));
		        else
		        	bundle.putInt("imageId", -1);

		        views.put(position, rowView);
		        
		        (new LoadThumbAsyncCallback(imageView)).execute(bundle);
	        }
	        	        
	        return rowView;
	    }
	}

}
