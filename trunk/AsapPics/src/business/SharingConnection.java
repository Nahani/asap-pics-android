package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import webservices.WebServiceManager;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

public abstract class SharingConnection {

	private static HashMap<Integer, ASAPAlbum> albums_in_cache = new HashMap<Integer, ASAPAlbum>();
	private static HashMap<Integer, ASAPImage> images_in_cache = new HashMap<Integer, ASAPImage>();
	private static SparseArray<ASAPImage> thumb_cache = new SparseArray<ASAPImage>();

//	synchronized public static HashMap<Integer, ASAPImage> getImages_in_cache() {
//		return images_in_cache;
//	}

	synchronized public static HashMap<Integer, ASAPAlbum> getAlbums_in_cache() {
		return albums_in_cache;
	}
	
	public static SparseArray<ASAPImage> getThumbCache() {
		return thumb_cache;
	}

	synchronized public static int addImage(int idAlbum, String name, Bitmap img) throws Exception {
		WebServiceManager.add_image(idAlbum, name, img);
		int imageId = WebServiceManager.get_image_ID(idAlbum, name);
		
//		images_in_cache.put(imageId, new ASAPImage(imageId, idAlbum, name, img));
		
		thumb_cache.put(imageId, new ASAPImage(imageId, idAlbum, name, WebServiceManager.get_thumb(imageId, idAlbum)));
		
		/* Met à jour la liste des images de l'album en cache */
		albums_in_cache.get(idAlbum).getImages().add(imageId);
		
		return imageId;
	}

	synchronized public static boolean addAlbum(String name, int idProp) throws Exception {
		boolean result = WebServiceManager.add_album(name, idProp);

		if (result) {
			int idAlbum = WebServiceManager.get_album_ID(name, idProp);
			albums_in_cache.put(
					idAlbum,
					new ASAPAlbum(idAlbum, name, WebServiceManager
							.get_images_ID_from_album(idAlbum)));
		}
		return result;
	}

	synchronized public static ASAPImage getImage(int id, int idAlbum) throws Exception {
		/* Consomme trop de cache -> OutOfMemoryException */
//		if (!images_in_cache.containsKey(id)) {
//			ASAPImage current = new ASAPImage(id,
//					idAlbum,
//					WebServiceManager.get_image_name(id, idAlbum),
//					WebServiceManager.get_image(id, idAlbum));
//			images_in_cache.put(id, current);
//			return current;
//		}
		
		ASAPImage current = new ASAPImage(id,
				idAlbum,
				WebServiceManager.get_image_name(id, idAlbum),
				WebServiceManager.get_image(id, idAlbum));

		return current;
	}

	synchronized public static ASAPImage getThumb(int id, int idAlbum) throws Exception {
			ASAPImage image = thumb_cache.get(id);
			
			/* Si l'image n'existe pas dans le cache, on la récupère du serveur
			 * et on l'ajoute au cache avant de la retourner */
			if (image == null) {
				
				image = new ASAPImage(id, idAlbum,
					WebServiceManager.get_image_name(id, idAlbum),
					WebServiceManager.get_thumb(id, idAlbum));
				
				thumb_cache.put(id, image);
			}
			
			return image;
	}
	
	synchronized public static ASAPAlbum getAlbum(int idAlbum) throws Exception {
		if (!albums_in_cache.containsKey(idAlbum)) {
			Log.i("ASAP PICS", "je ne passe par le cache pour l'image " + idAlbum);
			ASAPAlbum targeted = new ASAPAlbum(idAlbum,
												WebServiceManager.get_album_name(idAlbum),
												WebServiceManager.get_images_ID_from_album(idAlbum));
			
			albums_in_cache.put(idAlbum, targeted);
			
			return targeted;
		}
		Log.i("ASAP PICS", "je passe par le cache pour l'image " + idAlbum);
		return albums_in_cache.get(idAlbum);
	}
	
	synchronized public static List<ASAPAlbum> getAlbumsFromUser(int idUser) throws Exception {
		List<ASAPAlbum> targeted = new ArrayList<ASAPAlbum>();
		List<Integer> idAlbums = WebServiceManager.get_albums_ID_from_user(idUser);
		for(Integer id : idAlbums) {
			targeted.add(getAlbum(id));
		}
		return targeted;
	}

	synchronized public static boolean deleteImage(int id, int idAlbum) throws Exception {
		boolean result = WebServiceManager.delete_image(id, idAlbum);
		if (result) {
			albums_in_cache.get(idAlbum).getImages().remove(Integer.valueOf(id));
//			images_in_cache.remove(id);
			thumb_cache.remove(id);
		}  
		return result;
	}

	synchronized public static boolean deleteAlbum(String name, int idProp) throws Exception {
		int idAlbum = WebServiceManager.get_album_ID(name, idProp);
		boolean result = WebServiceManager.delete_album(name, idProp);
		if (result && albums_in_cache.containsKey(idAlbum)) {
			albums_in_cache.remove(idAlbum);
		}
		return result;
	}
}