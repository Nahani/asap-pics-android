package views;

import java.util.ArrayList;
import java.util.List;
import listeners.OneAlbumViewOnClickListener;
import listeners.OneAlbumViewOnLongClickListener;
import misc.AsyncCallbackResult;

import business.SharingConnection;

import com.asappics.R;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import async.LoadThumbAsyncCallback;

public class OneAlbumView extends Activity {

	private GridView gridView;
	private List<Integer> imageIdList;
	private long albumId;
	private String albumName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_album_view);
		
		/* Paramètre de la vue */
		Intent intent = getIntent();
        albumId = intent.getLongExtra("albumId", -1);
        albumName = intent.getStringExtra("albumName");
        
        setTitle(albumName);
        
        /* Instanciation des objets */
        imageIdList = new ArrayList<Integer>();
		gridView = (GridView) findViewById(R.id.one_album_grid_view);
		
		/* Chargement asynchrone des images */
        (new LoadPicturesAsyncCallback()).execute();
	}
	
	/**
	 * Charge la liste des id des images de l'album d'id 'albumId'.
	 * @author Alex
	 *
	 */
	private class LoadPicturesAsyncCallback extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				imageIdList = SharingConnection.getAlbum((int) albumId).getImages();
			} catch(Exception e) {
				Log.e("AsyncTaskException", "Album_View.java::LoadPicturesAsyncCallback: " + e.toString());
				return AsyncCallbackResult.EXCEPTION;
			}
			return AsyncCallbackResult.TRUE;
		}
		
		@Override
		public void onPostExecute(Integer result) {
			if (result == AsyncCallbackResult.EXCEPTION) {
	        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
	        } else {
	        	refreshAdapter();
	        }
		}
		
	}
	
	private void refreshAdapter() {
		ImageAdapter imageAdapter = new ImageAdapter(this, imageIdList);
		gridView.setAdapter(imageAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.one_album_view_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.one_album_view_add_picture_menu_item) {
			
			/* Ouvre la gallerie photos */
			Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, 1);
			
			return true;
		} else if (item.getItemId() == R.id.one_album_view_refresh_menu_item) {
			
			new LoadPicturesAsyncCallback().execute();
			
		}
		
		return false;
	}
	
	/**
	 * Callback. Récupère la photo sélectionnée dans la gallerie par le Photo Picker et lance un thread pour l'uploader.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    if (resultCode == RESULT_OK)
	    {
	        Uri chosenImageUri = data.getData();
	        Bitmap image = null;
	        
	        try {
				image = Media.getBitmap(this.getContentResolver(), chosenImageUri);
			} catch (Exception e) {}
	        
	        /* Lancement thread pour upload */
	        if (image != null) {
	        	
	        	/* Nom de l'image + extension */
	        	String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(chosenImageUri, filePathColumn, null, null, null);
	        	cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                //Error sur Nexus 7
                String fileName;
                if(filePath != null)
                	fileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length());
                else
                	fileName = "Default";
                
                new AddPictureAsyncCallback(albumId, fileName, image).execute();
	
	        	
	        }
	    }
	}
	
	/**
	 * Adapter pour la liste de widgets
	 * @author Alex
	 *
	 */
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    private List<Integer> mThumbIds;
	    private SparseArray<View> views;

	    public ImageAdapter(Context c, List<Integer> images) {
	        mContext = c;
			mThumbIds = images;
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
	    
	    public List<Integer> getImageList() {
	    	return mThumbIds;
	    }
	    
	    public SparseArray<View> getViews() {
	    	return views;
	    }
	    
	    public void addThumbID(int id){
	    	mThumbIds.add(id);
	    }

	    /**
	     * Crée un nouvel ImageView avec chargement asynchrone, ou la récupère
	     * dans le HashMap s'il existe déjà
	     */
	    @Override
		public View getView(int position, View convertView, ViewGroup parent) {
	    	/* Dimensions de l'écran */
	    	Point p = new Point();
	    	Display display = getWindowManager().getDefaultDisplay();
//	    	display.getSize(p); // Uniquement API level >= 17
//	    	int size = p.x/3;
	    	int size = display.getWidth() / 3;
	    	
	    		        
	        if (convertView == null) {
	            convertView = new ImageView(mContext);
	            /* Paramètres grid layout */
	            ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
	            convertView.setLayoutParams(new GridView.LayoutParams(size, size));
	            
	           
	        }     
	                  
            /* Listener */
            convertView.setOnClickListener(new OneAlbumViewOnClickListener(mContext, (long) mThumbIds.get(position), albumId));
            convertView.setOnLongClickListener(new OneAlbumViewOnLongClickListener(mContext, this, position, albumId));
	        
	        /* Bundle et lancement d'un thread pour chargement asynchrone de l'image */
            Bundle bundle = new Bundle();
            bundle.putInt("imageId", (int) mThumbIds.get(position));
            bundle.putInt("albumId", (int) albumId);
	        
	        (new LoadThumbAsyncCallback((ImageView) convertView)).execute(bundle);
	        
	        return convertView;
	    }
	}
	
	private class AddPictureAsyncCallback extends AsyncTask<Void, Void, Integer> {

		private Bitmap image;
		private long albumId;
		private int imageId;
		private String name;
		private ProgressDialog progressDialog;
		
		public AddPictureAsyncCallback(long albumId, String name, Bitmap image) {
			this.image = image;
			this.albumId = albumId;
			this.name = name;
			progressDialog = new ProgressDialog(OneAlbumView.this);
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
				image.recycle();
				
				System.gc();
				
				/* Rafraîchi la vue */
				refreshAdapter();
				
				Toast.makeText(OneAlbumView.this, "Image ajoutée", Toast.LENGTH_LONG).show();
				
			} else if (result == AsyncCallbackResult.EXCEPTION) {
				Toast.makeText(OneAlbumView.this, OneAlbumView.this.getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG).show();
			}
		}
		
	}
}
