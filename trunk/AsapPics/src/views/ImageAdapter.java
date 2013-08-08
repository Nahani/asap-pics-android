package views;

import business.ASAPImage;

import com.asappics.R;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ImageAdapter extends BaseAdapter {
    
	private Context mContext;
	
	private ASAPImage image;
 
    // Keep all Images in array
    public Integer[] mThumbIds = {
    };
 
    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
        //image = new ASAPImage("cynthia.jpg");
    }
 
    @Override
    public int getCount() {
        return mThumbIds.length;
    }
 
    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageView imageView;
    	
    	int size = (((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth()) / 3;
    	
    	/**  DEBUG  **/
    	
    	//image.setData(((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.cynthia)).getBitmap());
    	
    	/** ======= **/
    	
    	if (convertView == null) {
	    	
    		imageView = new ImageView(mContext);
	        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        imageView.setLayoutParams(new GridView.LayoutParams(size, size));
	        
	        imageView.setOnClickListener(new OnClickListener() {
	        	
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, ShowPictureActivity.class);
					intent.putExtra("image-id", image.getID());
					mContext.startActivity(intent);
				}
				
	        });
    	} else {
    		imageView = (ImageView) convertView;
    	}
    	
    	imageView.setImageResource(mThumbIds[position]);
        
        return imageView;
    }
 
}