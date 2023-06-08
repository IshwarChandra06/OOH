package com.eikona.tech.util;

import static com.eikona.tech.constants.MultimediaProcessingConstants.DELIMETER_FORWARD_SLASH;
import static com.eikona.tech.constants.MultimediaProcessingConstants.ROOT_DIR_NAME;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.eikona.tech.domain.MediaSite;

import net.coobird.thumbnailator.Thumbnails;

@Component
public class ImageProcessingUtil {
	
	
	public String [] saveImageDahuaIntoFileSystem(BufferedImage bufferImage, MediaSite mediasite, String dateFolder,
			String imageName) {

		String[] stringArray = new String[2];
		try {
			String writeOrginalImage = "";
			String writeThumbnailImage = "";

			String path = mediasite.getAssetCode() + "/" + dateFolder;
			
			String orginalReadPath = "ooh_images/"+ path + "/original";
			String orginalPath = ROOT_DIR_NAME+ "/"+orginalReadPath;//path + "/original";
			File orginal = new File(orginalPath);
			
			if (!orginal.exists()) {
				orginal.mkdirs();
			} 
			writeOrginalImage = orginalPath + "/" + imageName;
			stringArray[0] = orginalReadPath + "/" + imageName;
			
			String thumbnialReadPath = "ooh_images/"+path + "/thumbnails";
			String thumbnialPath =ROOT_DIR_NAME + "/" + thumbnialReadPath;//path + "/thumbnails";
			File thumbnial = new File(thumbnialPath);
			
			if (!thumbnial.exists()) {
				thumbnial.mkdirs();
			} 
			writeThumbnailImage =  thumbnialPath + "/" + imageName;
			stringArray[1] = thumbnialReadPath + "/" + imageName;
			

			if (null != bufferImage) {
				ImageIO.write(bufferImage, "jpg", new File(writeOrginalImage));

				BufferedImage outputImageForThumbnail = resizeImage(bufferImage, 400, 300);

				ImageIO.write(outputImageForThumbnail, "jpg", new File(writeThumbnailImage));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stringArray;
	}
	
	public String[] saveImageIntoFileSystem(BufferedImage bufferImage, String assetFolder, String dateFolder,
			String imageName) {

		String[] stringArray = new String[2];
		try {

			String year = imageName.substring(0, 4);
			String month = imageName.substring(4, 6);
			String day = imageName.substring(6, 8);

			String orginalReadPath = "ooh_images/"+assetFolder + "/" + year + "-" + month + "-" + day + "/original";
			String orginalPath = ROOT_DIR_NAME+DELIMETER_FORWARD_SLASH + orginalReadPath;//assetFolder + "/" + year + "-" + month + "-" + day + "/original";
			File orginal = new File(orginalPath);
			
			if (!orginal.exists())
				orginal.mkdirs();
			stringArray[0] = orginalReadPath + "/" + imageName;
			String writeOrginalImage =  orginalPath + "/" + imageName;
			
			String thumbnialReadPath = "ooh_images/"+ assetFolder + "/" + year + "-" + month + "-" + day + "/thumbnails";
			String thumbnialPath = ROOT_DIR_NAME+DELIMETER_FORWARD_SLASH +thumbnialReadPath;// assetFolder + "/" + year + "-" + month + "-" + day + "/thumbnails";
			File thumbnial = new File(thumbnialPath);
			if (!thumbnial.exists())
				thumbnial.mkdirs();
			stringArray[1] = thumbnialReadPath + "/" + imageName;
			String writeThumbnailImage =  thumbnialPath + "/" + imageName;

			if (null != bufferImage) {
				ImageIO.write(bufferImage, "jpg", new File(writeOrginalImage));

				BufferedImage outputImageForThumbnail = resizeImage(bufferImage, 400, 300);

				ImageIO.write(outputImageForThumbnail, "jpg", new File(writeThumbnailImage));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stringArray;
	}

	public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight)
			throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Thumbnails.of(originalImage).size(targetWidth, targetHeight).outputFormat("JPEG").outputQuality(0.90)
				.toOutputStream(outputStream);
		byte[] data = outputStream.toByteArray();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		return ImageIO.read(inputStream);
	}
}
