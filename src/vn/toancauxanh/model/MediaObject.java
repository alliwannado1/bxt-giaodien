package vn.toancauxanh.model;

import org.zkoss.util.media.Media;

public class MediaObject {
	private Media media;
	private Image image;
	private boolean checkRemove;

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public MediaObject() {
		super();
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public MediaObject(Media media, Image image) {
		super();
		this.media = media;
		this.image = image;
	}

	public boolean isCheckRemove() {
		return checkRemove;
	}

	public void setCheckRemove(boolean checkRemove) {
		this.checkRemove = checkRemove;
	}

}
