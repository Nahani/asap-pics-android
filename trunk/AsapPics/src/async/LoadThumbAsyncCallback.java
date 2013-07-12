package async;

import com.example.asappics.R;

import misc.AsyncCallbackResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import business.ASAPImage;
import business.SharingConnection;

public class LoadThumbAsyncCallback extends AsyncTask<Bundle, Void, Bundle> {

	private ImageView view;
	
	public LoadThumbAsyncCallback(ImageView v) {
		view = v;
	}
	
	@Override
	protected Bundle doInBackground(Bundle... b) {

		int imageId = b[0].getInt("imageId");
		int albumId = b[0].getInt("albumId");
		ASAPImage img = null;
		Bundle bundle = new Bundle();
		
		// On récupère la première image de l'album et l'ajoute à la vue associée
		if (imageId >= 0) {
			try {
				// img = SharingConnection.getImage(imageId, albumId); // Image complète (lourd)
				img = SharingConnection.getThumb(imageId, albumId);
				bundle.putParcelable("image", img);
				bundle.putInt("result", AsyncCallbackResult.TRUE);
			} catch (Exception e) {
				Log.e("AsyncTaskException", "LoadImage.java: " + e.toString());
				bundle.putInt("result", AsyncCallbackResult.EXCEPTION);
			}
		} else {
			bundle.putInt("result", AsyncCallbackResult.NO_RESULT);
		}
		
		return bundle;
	}
	
	@Override
	public void onPostExecute(Bundle result) {
		super.onPostExecute(result);
		int res = result.getInt("result");
		
		// Si l'image a été chargée, on l'ajoute à la vue
		if (res == AsyncCallbackResult.TRUE) {
			final ASAPImage img = result.getParcelable("image");
			
			Animation fadeOutAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.image_view_fade_out);
			final Animation fadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.image_view_fade_in);
			
			fadeOutAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation animation) {
					/* Ajout de l'image à l'ImageView et lance l'animation de fade in */
					view.setImageBitmap(img.getData());
					view.startAnimation(fadeInAnimation);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {}

				@Override
				public void onAnimationStart(Animation animation) {}
				
			});
			
			view.startAnimation(fadeOutAnimation);
		}
	}
	
}
