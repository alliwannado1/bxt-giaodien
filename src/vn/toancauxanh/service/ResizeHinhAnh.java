package vn.toancauxanh.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import vn.toancauxanh.model.Setting;
import vn.toancauxanh.model.Video;

import com.mysema.commons.lang.Pair;

public class ResizeHinhAnh {

	public static final Integer SIZE_HEIGHT_IMAGE_ITEM_SMALL = 86 * 3;
	public static final Integer SIZE_WIDTH_IMAGE_ITEM_SMALL = 120 * 3;

	public static final Integer SIZE_HEIGHT_IMAGE_ITEM_DETAIL = 215 * 3;
	public static final Integer SIZE_WIDTH_IMAGE_ITEM_DETAIL = 300 * 3;

	public static final Integer SIZE_HEIGHT_IMAGE_ITEM_BANNER = 235 * 2;
	public static final Integer SIZE_WIDTH_IMAGE_ITEM_BANNER = 320 * 3;

	public static final Integer SIZE_HEIGHT_IMAGE_ITEM_VIDEO_IMAGE = 211 * 3;
	public static final Integer SIZE_WIDTH_IMAGE_ITEM_VIDEO_IMAGE = 294 * 3;

	public static void saveMediumAndSmall(String strFolderStore, vn.toancauxanh.model.Image image) throws IOException {
		String fileUrl = strFolderStore + (image.getNameFileHash().contains(".png") ? image.getNameFileHash().replace(".png", ".jpg") : image.getNameFileHash());
		File file = new File(fileUrl);
		if (file.exists()) {
			// BufferedImage originalImage = ImageIO.read(file);
			BufferedImage originalImage = readImage(file);

			List<Pair<Integer, Integer>> list_size = getHeightSmallAndMedium();
			String extension = image.getNameFileHash().substring(image.getNameFileHash().lastIndexOf(".") + 1);
			
			String outFileStr = strFolderStore + "s_" + image.getNameFileHash();
			File outFile = new File(outFileStr);
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizeImagePng = resizeImage(originalImage, type, list_size.get(0).getFirst(),
					list_size.get(0).getSecond());
			ImageIO.write(resizeImagePng, extension, outFile);

			String outFileStr2 = strFolderStore + "d_" + image.getNameFileHash();
			File outFile2 = new File(outFileStr2);
			int type2 = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizeImagePng2 = resizeImage(originalImage, type2, list_size.get(1).getFirst(),
					list_size.get(1).getSecond());
			ImageIO.write(resizeImagePng2, extension, outFile2);

			String outFileStr3 = strFolderStore + "b_" + image.getNameFileHash();
			File outFile3 = new File(outFileStr3);
			int type3 = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizeImagePng3 = resizeImage(originalImage, type3, list_size.get(2).getFirst(),
					list_size.get(2).getSecond());
			ImageIO.write(resizeImagePng3, extension, outFile3);

			String outFileStr4 = strFolderStore + "v_" + image.getNameFileHash();
			File outFile4 = new File(outFileStr4);
			int type4 = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizeImagePng4 = resizeImage(originalImage, type4, list_size.get(3).getFirst(),
					list_size.get(3).getSecond());
			ImageIO.write(resizeImagePng4, extension, outFile4);
		}
	}
	
	public static void saveMediumAndSmallVideo(String strFolderStore, Video video) throws IOException {
		String fileUrl = strFolderStore + video.getPathAvatar();
		File file = new File(fileUrl);
		if (file.exists()) {
			// BufferedImage originalImage = ImageIO.read(file);
			BufferedImage originalImage = readImage(file);

			List<Pair<Integer, Integer>> list_size = getHeightSmallAndMedium();
			String extension = video.getPathAvatar().substring(video.getPathAvatar().lastIndexOf(".") + 1);
			
			String outFileStr4 = strFolderStore + "v_" + video.getPathAvatar();
			File outFile4 = new File(outFileStr4);
			int type4 = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizeImagePng4 = resizeImage(originalImage, type4, list_size.get(3).getFirst(),
					list_size.get(3).getSecond());
			ImageIO.write(resizeImagePng4, extension, outFile4);
		}
	}

	private static List<Pair<Integer, Integer>> getHeightSmallAndMedium() {
		List<Pair<Integer, Integer>> list = new ArrayList<>();
		Setting setting = new BasicService<>().core().getSetting();
		int widthMediumConf = setting.getWidthMedium();
		int widthSmallConf = setting.getWidthSmall();
		if (widthMediumConf != 0) {
			Pair<Integer, Integer> pair_medium = new Pair<>(SIZE_WIDTH_IMAGE_ITEM_SMALL, SIZE_HEIGHT_IMAGE_ITEM_SMALL);
			list.add(pair_medium);
		}
		if (widthSmallConf != 0) {
			Pair<Integer, Integer> pair_small = new Pair<>(SIZE_WIDTH_IMAGE_ITEM_DETAIL, SIZE_HEIGHT_IMAGE_ITEM_DETAIL);
			list.add(pair_small);
		}

		if (widthSmallConf != 0) {
			Pair<Integer, Integer> pair_small = new Pair<>(SIZE_WIDTH_IMAGE_ITEM_BANNER, SIZE_HEIGHT_IMAGE_ITEM_BANNER);
			list.add(pair_small);
		}

		if (widthSmallConf != 0) {
			Pair<Integer, Integer> pair_small = new Pair<>(SIZE_WIDTH_IMAGE_ITEM_VIDEO_IMAGE,
					SIZE_HEIGHT_IMAGE_ITEM_VIDEO_IMAGE);
			list.add(pair_small);
		}

		return list;
	}

	@SuppressWarnings("unused")
	private static List<Pair<Integer, Integer>> getHeightSmallAndMedium(float size) {
		int heightMedium = 0;
		int heightSmall = 0;
		List<Pair<Integer, Integer>> list = new ArrayList<>();
		Setting setting = new BasicService<>().core().getSetting();
		int widthMediumConf = setting.getWidthMedium();
		int widthSmallConf = setting.getWidthSmall();
		if (widthMediumConf != 0) {
			heightMedium = (int) (widthMediumConf * size);
			Pair<Integer, Integer> pair_medium = new Pair<>(widthMediumConf, heightMedium);
			list.add(pair_medium);
		}
		if (widthSmallConf != 0) {
			heightSmall = (int) (widthSmallConf * size);
			Pair<Integer, Integer> pair_small = new Pair<>(widthSmallConf, heightSmall);
			list.add(pair_small);
		}
		return list;
	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		BufferedImage thumbImage = new BufferedImage(width, height, type);

		if ((double) width / height >= (double) originalImage.getWidth() / originalImage.getHeight()) {
			// Fit to width
			resizedImage = Scalr.resize(originalImage, Scalr.Mode.FIT_TO_WIDTH, width, height, Scalr.OP_ANTIALIAS);
			Graphics2D tGraphics2D = thumbImage.createGraphics(); // create a graphics object to paint to
			tGraphics2D.drawImage(resizedImage, 0, 0, width, height, 0, (resizedImage.getHeight() - height) / 2, width,
					(resizedImage.getHeight() - height) / 2 + height, null); // draw the image scaled */
			tGraphics2D.dispose();
			// Fit to height
		} else {
			resizedImage = Scalr.resize(originalImage, Scalr.Mode.FIT_TO_HEIGHT, width, height, Scalr.OP_ANTIALIAS);
			Graphics2D tGraphics2D = thumbImage.createGraphics(); // create a graphics object to paint to
			tGraphics2D.drawImage(resizedImage, 0, 0, width, height, (resizedImage.getWidth() - width) / 2, 0,
					(resizedImage.getWidth() - width) / 2 + width, height, null); // draw the image scaled */
			tGraphics2D.dispose();
		}
		return thumbImage;
	}

	static BufferedImage readImage(File file) throws IOException {
		return readImage(new FileInputStream(file));
	}

	static BufferedImage readImage(InputStream stream) throws IOException {
		Iterator<ImageReader> imageReaders = ImageIO.getImageReadersBySuffix("jpg");
		ImageReader imageReader = imageReaders.next();
		ImageInputStream iis = ImageIO.createImageInputStream(stream);
		imageReader.setInput(iis, true, true);
		Raster raster = imageReader.readRaster(0, null);
		int w = raster.getWidth();
		int h = raster.getHeight();

		BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		int rgb[] = new int[3];
		int pixel[] = new int[3];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				raster.getPixel(x, y, pixel);
				int Y = pixel[0];
				int CR = pixel[1];
				int CB = pixel[2];
				toRGB(Y, CB, CR, rgb);
				int r = rgb[0];
				int g = rgb[1];
				int b = rgb[2];
				int bgr = ((b & 0xFF) << 16) | ((g & 0xFF) << 8) | (r & 0xFF);
				result.setRGB(x, y, bgr);
			}
		}
		return result;
	}

	// Based on http://www.equasys.de/colorconversion.html
	private static void toRGB(int y, int cb, int cr, int rgb[]) {
		float Y = y / 255.0f;
		float Cb = (cb - 128) / 255.0f;
		float Cr = (cr - 128) / 255.0f;

		float R = Y + 1.4f * Cr;
		float G = Y - 0.343f * Cb - 0.711f * Cr;
		float B = Y + 1.765f * Cb;

		R = Math.min(1.0f, Math.max(0.0f, R));
		G = Math.min(1.0f, Math.max(0.0f, G));
		B = Math.min(1.0f, Math.max(0.0f, B));

		int r = (int) (R * 255);
		int g = (int) (G * 255);
		int b = (int) (B * 255);

		rgb[0] = r;
		rgb[1] = g;
		rgb[2] = b;
	}
}