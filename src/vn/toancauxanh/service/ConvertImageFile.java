package vn.toancauxanh.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;

public class ConvertImageFile {
	public static void convertImageFromPngToJpg(String folder, String nameFile) throws Exception {

		BufferedImage bufferedImage = null;
		try {

			// read image file
			bufferedImage = ImageIO.read(new File(folder + File.separator + nameFile));

			// create a blank, RGB, same width and height, and a white background
			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg",
					new File(folder + File.separator + nameFile.replace(".png", ".jpg")));

		} catch (Exception e) {
			 Iterator<ImageReader> iter = ImageIO.getImageReaders(new File(folder + File.separator + nameFile));

		        Exception lastException = null;
		        while (iter.hasNext()) {
		            ImageReader reader = null;
		                reader = (ImageReader)iter.next();
		                ImageReadParam param = reader.getDefaultReadParam();
		                reader.setInput(new File(folder + File.separator + nameFile), true, true);
		                Iterator<ImageTypeSpecifier> imageTypes = reader.getImageTypes(0);
		                while (imageTypes.hasNext()) {
		                    ImageTypeSpecifier imageTypeSpecifier = imageTypes.next();
		                    int bufferedImageType = imageTypeSpecifier.getBufferedImageType();
		                    if (bufferedImageType == BufferedImage.TYPE_BYTE_GRAY) {
		                        param.setDestinationType(imageTypeSpecifier);
		                        break;
		                    }
		                }
		                bufferedImage = reader.read(0, param);
		                if (null != bufferedImage) break;
		        }
		        // If you don't have an image at the end of all readers
		        if (null == bufferedImage) {
		            if (null != lastException) {
		                throw lastException;
		            }
		        }
		}
	}
}
