package business;

import java.util.ArrayList;
import java.util.List;

public class ASAPAlbum {
	private String name;
	private int id;
	private List<Integer> images;

	public ASAPAlbum(int id, String name, List<Integer> img) {
		this.id = id;
		this.name = name;
		this.images = img;
	}

	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	public List<Integer> getImages() {
		return images;
	}
}
