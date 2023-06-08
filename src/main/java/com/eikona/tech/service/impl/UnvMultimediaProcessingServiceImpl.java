package com.eikona.tech.service.impl;

import static com.eikona.tech.constants.MultimediaProcessingConstants.DELIMETER_COLON;
import static com.eikona.tech.constants.MultimediaProcessingConstants.DELIMETER_FORWARD_SLASH;
import static com.eikona.tech.constants.MultimediaProcessingConstants.DELIMETER_HYPHEN;
import static com.eikona.tech.constants.MultimediaProcessingConstants.DELIMETER_SPACE;
import static com.eikona.tech.constants.MultimediaProcessingConstants.ORIGINAL_DIR_NAME;
import static com.eikona.tech.constants.MultimediaProcessingConstants.ROOT_DIR_NAME;
import static com.eikona.tech.constants.MultimediaProcessingConstants.TEST_DIR_NAME;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.repository.MultimediaLogRepository;
import com.eikona.tech.service.MultimediaProcessingService;
import com.eikona.tech.util.AWSUtil;
import com.eikona.tech.util.ImageProcessingUtil;

@Service
@Primary
@Qualifier("unvMultimediaProcessingService")
public class UnvMultimediaProcessingServiceImpl implements MultimediaProcessingService {

	@Autowired
	private MultimediaLogRepository imageLogRepository;
	
	@Autowired
	private ImageProcessingUtil imageProcessingUtil;
	
	@Autowired
	private AWSUtil awsUtil;

	@Override
	public void imageProcessing(String assetPath, MediaSite mediasite, String dateStr) {

		try {
			File assetFolder = new File(assetPath);
			File[] dateDirList = assetFolder.listFiles();

			for (File dateDir : dateDirList) {
				
				if(dateDir.getName().equalsIgnoreCase(dateStr))
					saveImageFromDateDirectory(dateDir,assetPath,assetFolder,mediasite);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void saveImageFromDateDirectory(File dateDir,String assetPath,File assetFolder,MediaSite mediasite) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			if (dateDir.isDirectory()) {

				List<MultimediaLog> imageLogList = new ArrayList<>();
				File[] imageList = dateDir.listFiles();
				for (File imageFile : imageList) {

					String imgReadPath = assetPath + DELIMETER_FORWARD_SLASH + dateDir.getName();
					String imageFileName = imageFile.getName();
					if (!(TEST_DIR_NAME.equalsIgnoreCase(imageFileName))) {

						String year = imageFileName.substring(0, 4);
						String month = imageFileName.substring(4, 6);
						String day = imageFileName.substring(6, 8);
						String hour = imageFileName.substring(8, 10);
						String min = imageFileName.substring(10, 12);
						String sec = imageFileName.substring(12, 14);
						String dateTimeStr = day + DELIMETER_HYPHEN + month + DELIMETER_HYPHEN + year + DELIMETER_SPACE + hour + DELIMETER_COLON + min + DELIMETER_COLON + sec;

						Long imageLogId = imageLogRepository.findIdByImageUrl(ROOT_DIR_NAME+"ooh_images/"+assetFolder.getName()
								+ DELIMETER_FORWARD_SLASH + year + DELIMETER_HYPHEN + month + DELIMETER_HYPHEN + day +DELIMETER_FORWARD_SLASH+ ORIGINAL_DIR_NAME +DELIMETER_FORWARD_SLASH+ imageFileName);
						if (null == imageLogId) {
							MultimediaLog imagelog = new MultimediaLog();
							try {
								Date date = dateFormat.parse(dateTimeStr);
								imagelog.setTimeStamp(date);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							imagelog.setImageUrl(ROOT_DIR_NAME +"ooh_images/" + assetFolder.getName() + DELIMETER_FORWARD_SLASH + year + DELIMETER_HYPHEN + month
									+ DELIMETER_HYPHEN + day +DELIMETER_FORWARD_SLASH+ ORIGINAL_DIR_NAME +DELIMETER_FORWARD_SLASH+ imageFileName);
							imagelog.setMediaSite(mediasite);

							BufferedImage bufferImage = ImageIO.read(new File(imgReadPath + DELIMETER_FORWARD_SLASH + imageFileName));
							String[] imagePathList = imageProcessingUtil.saveImageIntoFileSystem(bufferImage, assetFolder.getName(),
									dateDir.getName(), imageFileName);
							imagelog.setOriginalPath("https://eikona-ooh.s3.ap-south-1.amazonaws.com/"+imagePathList[0]);
							imagelog.setThumbnailPath("https://eikona-ooh.s3.ap-south-1.amazonaws.com/"+imagePathList[1]);

							awsUtil.uploadFileToAWS(imagePathList);
							imageLogList.add(imagelog);
						}
					}
					
//					imageFile.delete();
				}

				imageLogRepository.saveAll(imageLogList);
				imageLogList.clear();
			}
//			if (0 == dateDir.listFiles().length)
//				dateDir.delete();
		}catch (Exception e) {
			e.printStackTrace();
		}
		

	
	}


	@Override
	public void videoProcessing(String path, MediaSite mediasite, String dateStr) {
		// TODO Auto-generated method stub

	}
}
