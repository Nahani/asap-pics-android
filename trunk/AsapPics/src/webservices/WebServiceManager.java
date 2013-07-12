package webservices;

import java.util.List;

import org.json.JSONException;

import android.graphics.Bitmap;

public abstract class WebServiceManager {

	public static String get_image_name(int id, int idAlbum) throws Exception {
		try{
			String name = ImageService.get_image_name(id, idAlbum);
			return name;
		}catch(JSONException e){
			return "Sans nom";
		}
	}

	public static int get_image_ID(int idAlbum, String name) throws Exception {
		return ImageService.get_image_ID(idAlbum, name);
	}
	
	public static Bitmap get_image(int id, int idAlbum) throws Exception {
		return ImageService.get(id, idAlbum);
	}
	
	public static Bitmap get_thumb(int id, int idAlbum) throws Exception {
		return ImageService.get_thumb(id, idAlbum);
	}

	public static List<Integer> get_images_ID_from_album(int idAlbum) throws Exception {
		return ImageService.get_images_ID_from_album(idAlbum);
	}

	public static void add_image(int idAlbum, String name, Bitmap img) throws Exception {
		ImageService.add(idAlbum, name, img);
	}

	public static boolean delete_image(int id, int idAlbum) throws Exception {
		return ImageService.delete(id, idAlbum);
	}

	public static String get_album_name(int id) throws Exception {
		return AlbumService.get_album_name(id);
	}

	public static int get_album_ID(String name, int idProp) throws Exception {
		return AlbumService.get_album_ID(name, idProp);
	}

	public static List<Integer> get_albums_ID_from_user(int idProp) throws Exception {
		return AlbumService.get_albums_ID_from_user(idProp);
	}

	public static boolean add_album(String name, int idProp) throws Exception {
		return AlbumService.add(name, idProp);
	}

	public static boolean delete_album(String name, int idProp) throws Exception {
		return AlbumService.delete(name, idProp);
	}

	public static boolean get_user_level(String login) throws Exception {
		return UserService.get_user_level(login);
	}

	public static int get_user_ID(String login) throws Exception {
		return UserService.get_user_ID(login);
	}

	public static boolean add_user(String first_name, String last_name,
			String login, String mail, String pwd) throws Exception {
		return UserService.add(first_name, last_name, login, mail, pwd);
	}

	public static boolean delete_user(String login) throws Exception {
		return UserService.delete(login);
	}

	public static boolean check_password(String login, String password) throws Exception {
		return UserService.check_password(login, password);
	}

}
