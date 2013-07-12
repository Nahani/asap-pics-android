package listeners;

import views.ShowPictureActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class OneAlbumViewOnClickListener implements OnClickListener {

	private Context context;
	private long imageId;
	private long albumId;
	
	public OneAlbumViewOnClickListener(Context context, long imageId, long albumId) {
		this.context = context;
		this.imageId = imageId;
		this.albumId = albumId;
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, ShowPictureActivity.class);
		intent.putExtra("imageId", imageId);
		intent.putExtra("albumId", albumId);
		context.startActivity(intent);
	}

}
