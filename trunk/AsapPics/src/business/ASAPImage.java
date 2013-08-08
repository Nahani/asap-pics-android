package business;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

public class ASAPImage implements Parcelable {

	private long id;
	private long albumId;
	private String name;
	private Bitmap data;

	public static final Parcelable.Creator<ASAPImage> CREATOR = new Parcelable.Creator<ASAPImage>() {

		@Override
		public ASAPImage createFromParcel(Parcel source) {
			
			long id = source.readLong();
			long albumId = source.readLong();
			String name = source.readString();
			Bitmap bmp = Bitmap.CREATOR.createFromParcel(source);
			
			return new ASAPImage(id, albumId, name, bmp);
		}

		@Override
		public ASAPImage[] newArray(int size) {
			return new ASAPImage[size];
		}
		
	};
	
	public ASAPImage() {}
	
	public ASAPImage(String name) {
		this.name = name;
	}
		
	public ASAPImage(long id, long albumId, String name, Bitmap data) {
		this.name = name;
		this.id = id;
		this.data = data;
	}

	public long getAlbumId() { return albumId; }
	
	public String getName() {
		return name;
	}

	public Bitmap getData() {
		return data;
	}

	public long getID() {
		return id;
	}
	
	public void setData(Bitmap data) {
		this.data = data;
	}

	// ContentResolver : getContentResolver() (de par l'activity)
	public void download(ContentResolver r) throws IOException {

		final String DIRECTORY_PICTURES = "DCIM";
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		data.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

		File f = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ DIRECTORY_PICTURES
				+ File.separator 
				+ name
				);
		f.createNewFile();
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(bytes.toByteArray());
		fo.close();
		MediaStore.Images.Media.insertImage(r, f.getAbsolutePath(),
				f.getName(), f.getName());
	}
	
	public void downloadTheo(){
		FileOutputStream fos = null;
		/*
		String sdCard = Environment.getExternalStorageDirectory().toString();
		File file = new File(sdCard+"/ASAP");
		file.mkdirs();
		String imageName = name+".jpg";
		File finalFile = new File(file,imageName);*/
		
		File sdCard = Environment.getExternalStorageDirectory();
		File file = new File(sdCard, "DCIM/Camera/"+name+".jpg");
		
		
		try
		{
			file.createNewFile();
			fos = new FileOutputStream(file.getAbsolutePath());
			//BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
			data.compress(CompressFormat.JPEG, 90, fos);
			fos.flush();
			fos.close();
		}
		catch (FileNotFoundException e)
		{
			Log.i("EXCP FNF", e.getMessage());
		}
		catch (IOException e)
		{
			Log.i("EXCP IO", e.getMessage());
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(albumId);
		dest.writeString(name);
		data.writeToParcel(dest, 0);
	}

}
