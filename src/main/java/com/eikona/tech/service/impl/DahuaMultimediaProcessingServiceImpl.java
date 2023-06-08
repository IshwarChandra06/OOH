package com.eikona.tech.service.impl;

import static com.eikona.tech.constants.MultimediaProcessingConstants.AUDIO_BIT_RATE;
import static com.eikona.tech.constants.MultimediaProcessingConstants.AUDIO_CHANNELS;
import static com.eikona.tech.constants.MultimediaProcessingConstants.AUDIO_CODEC;
import static com.eikona.tech.constants.MultimediaProcessingConstants.AUDIO_SAMPLING_RATE;
import static com.eikona.tech.constants.MultimediaProcessingConstants.DELIMETER_COLON;
import static com.eikona.tech.constants.MultimediaProcessingConstants.DELIMETER_DOT;
import static com.eikona.tech.constants.MultimediaProcessingConstants.DELIMETER_FORWARD_SLASH;
import static com.eikona.tech.constants.MultimediaProcessingConstants.DELIMETER_HTML_DOT;
import static com.eikona.tech.constants.MultimediaProcessingConstants.DELIMETER_SPACE;
import static com.eikona.tech.constants.MultimediaProcessingConstants.EXTENSION_DAV;
import static com.eikona.tech.constants.MultimediaProcessingConstants.EXTENSION_JPG;
import static com.eikona.tech.constants.MultimediaProcessingConstants.EXTENSION_MP4;
import static com.eikona.tech.constants.MultimediaProcessingConstants.FORMAT_JPG;
import static com.eikona.tech.constants.MultimediaProcessingConstants.FORMAT_MP4;
import static com.eikona.tech.constants.MultimediaProcessingConstants.MIME_TYPE_VIDEO;
import static com.eikona.tech.constants.MultimediaProcessingConstants.ROOT_DIR_NAME;
import static com.eikona.tech.constants.MultimediaProcessingConstants.VIDEO_BIT_RATE;
import static com.eikona.tech.constants.MultimediaProcessingConstants.VIDEO_CODEC;
import static com.eikona.tech.constants.MultimediaProcessingConstants.VIDEO_DIR_NAME;
import static com.eikona.tech.constants.MultimediaProcessingConstants.VIDEO_FRAME_DIR_NAME;
import static com.eikona.tech.constants.MultimediaProcessingConstants.VIDEO_FRAME_RATE;
import static com.eikona.tech.constants.MultimediaProcessingConstants.VIDEO_HEIGHT;
import static com.eikona.tech.constants.MultimediaProcessingConstants.VIDEO_WIDTH;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.eikona.tech.domain.FileSystemContent;
import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.repository.FileContentStore;
import com.eikona.tech.repository.FileRepository;
import com.eikona.tech.repository.MultimediaLogRepository;
import com.eikona.tech.service.MultimediaProcessingService;
import com.eikona.tech.util.AWSUtil;
import com.eikona.tech.util.ImageProcessingUtil;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoAttributes;
import ws.schild.jave.VideoAttributes.X264_PROFILE;
import ws.schild.jave.VideoSize;

@Service
@Qualifier("dahuaMultimediaProcessingService")
public class DahuaMultimediaProcessingServiceImpl implements MultimediaProcessingService {
	
	@Autowired
	private FileContentStore contentStore;

	@Autowired
	private MultimediaLogRepository multimediaLogRepository;

	@Autowired
	private FileRepository filesRepo;
	
	@Autowired
	private AWSUtil awsUtil;

	@Autowired
	private ImageProcessingUtil imageProcessingUtil;

	@Value("${spring.content.fs.filesystemRoot}")
	private String rootPath;

	@Override
	public void videoProcessing(String path, MediaSite mediasite, String dateStr) {
		
		List<String> davPathList = readAllSpecifiedFileList(path+dateStr+"/", EXTENSION_DAV);
		List<MultimediaLog> multimediaList = processDavVideoList(davPathList, path, mediasite);
		
		multimediaLogRepository.saveAll(multimediaList);

	}

	private List<String> readAllSpecifiedFileList(String assetFolderPath, String extension) {
		List<String> filePathList = new ArrayList<String>();
		try {
			List<File> fileList = Files.walk(Paths.get(assetFolderPath))
					.filter(path -> path.toString().endsWith(extension)).map(Path::toFile).collect(Collectors.toList());

			for (File file : fileList) {
				String absolutePath = file.getAbsoluteFile().toString();
//				filePathList.add(absolutePath.substring(assetFolderPath.length() - 1, absolutePath.length()));
				filePathList.add(absolutePath.substring(assetFolderPath.length() - 12, absolutePath.length()));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePathList;
	}

	private List<MultimediaLog> processDavVideoList(List<String> davFileAbsolutePathList, String assetFolderPath,
			MediaSite mediasite) {
		List<MultimediaLog> multimedialogList = new ArrayList<MultimediaLog>();
		try {
			for (String davFilePath : davFileAbsolutePathList) {
				MultimediaLog multimedialog = new MultimediaLog();
				String rootPath = assetFolderPath + davFilePath.substring(1);
				String[] dateArray = davFilePath.split("\\\\");
				String date = davFilePath.split("\\\\")[1];

				String time =dateArray[dateArray.length-1].substring(0, 8);
				Date dateTime = getVideoDateTime(date, time);
				multimedialog.setTimeStamp(dateTime);
				multimedialog.setMediaSite(mediasite);

				File file = new File(rootPath);
				multimedialog = saveVideoInContentStore(file, mediasite, date, multimedialog);
				
				String[] stringArray = new String[2];
				stringArray[0]=multimedialog.getVideoUrl();
				stringArray[1]=multimedialog.getVideoFirstFramePath();
				
				awsUtil.uploadFileToAWS(stringArray);

				String videoUrl = "https://eikona-ooh.s3.ap-south-1.amazonaws.com/"+multimedialog.getVideoUrl();
				String imageUrl = "https://eikona-ooh.s3.ap-south-1.amazonaws.com/"+multimedialog.getVideoFirstFramePath();
				multimedialog.setVideoUrl(videoUrl);
				multimedialog.setVideoFirstFramePath(imageUrl);
				
				multimedialogList.add(multimedialog);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return multimedialogList;
	}

	private MultimediaLog saveVideoInContentStore(File file, MediaSite mediasite, String date,
			MultimediaLog multimedialog) {
		FileSystemContent fileObj = new FileSystemContent();
		fileObj.setContentMimeType(MIME_TYPE_VIDEO);

		File mp4File = convertVideoFormat(file, mediasite.getAssetCode(), date);
		String videoPath = saveIntoContentStore(fileObj, mediasite.getAssetCode(), date, mp4File, VIDEO_DIR_NAME);
		multimedialog.setVideoUrl(videoPath);

		String videoFramePath = saveVideoFrameInContentStore(mp4File, mediasite, date);
		multimedialog.setVideoFirstFramePath(videoFramePath);
//		mp4File.delete();
		return multimedialog;
	}

	
	private String saveVideoFrameInContentStore(File mp4File, MediaSite mediasite, String date) {
		FileSystemContent frameObj = new FileSystemContent();
		frameObj.setContentMimeType(MimeTypeUtils.IMAGE_JPEG_VALUE);

		File videoFrame = new File(mp4File.getParentFile().getAbsolutePath() + DELIMETER_FORWARD_SLASH
				+ mp4File.getName().substring(0, mp4File.getName().lastIndexOf(DELIMETER_DOT)) + EXTENSION_JPG);
		String videoFramePath = saveIntoContentStore(frameObj, mediasite.getAssetCode(), date, videoFrame,
				VIDEO_FRAME_DIR_NAME);

//		videoFrame.delete();
		return videoFramePath;
	}

	private Date getVideoDateTime(String date, String time) {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date dateTime = null;
		try {
			Date dateStamp = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			date = dateFormat.format(dateStamp);

			String[] timeArray = time.split(DELIMETER_HTML_DOT);
			time = timeArray[0] + DELIMETER_COLON + timeArray[1] + DELIMETER_COLON + timeArray[2];
			dateTime = dateTimeFormat.parse(date + DELIMETER_SPACE + time);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	private File convertVideoFormat(File davFile, String assetCode, String date) {
		File targetMp4 = null;
		try {
			
			targetMp4 = new File(davFile.getParentFile().getAbsolutePath() + DELIMETER_FORWARD_SLASH
					+ davFile.getName().substring(0, davFile.getName().lastIndexOf(DELIMETER_DOT)) + EXTENSION_MP4);

			MultimediaObject readfile = new MultimediaObject(new File(davFile.getAbsolutePath()));

			/* Step 2. Set Audio Attrributes for conversion */
			AudioAttributes audio = setAudioAttributes();

			/* Step 3. Set Video Attributes for conversion */
			VideoAttributes video = setVideoAttributes();

			/* Step 4. Set Encoding Attributes */
			EncodingAttributes attrs = setEncodingAttributes(audio, video);

			/* Step 5. Do the Encoding */
			Encoder encoder = new Encoder();
			encoder.encode(readfile, targetMp4, attrs);

			//davFile.delete();

			captureVideoFrame(targetMp4);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return targetMp4;

	}

	private AudioAttributes setAudioAttributes() {
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec(AUDIO_CODEC);
		// here 64kbit/s is 64000
		audio.setBitRate(AUDIO_BIT_RATE);
		audio.setChannels(AUDIO_CHANNELS);
		audio.setSamplingRate(AUDIO_SAMPLING_RATE);

		return audio;
	}

	private VideoAttributes setVideoAttributes() {
		VideoAttributes video = new VideoAttributes();
		video.setCodec(VIDEO_CODEC);
		video.setX264Profile(X264_PROFILE.BASELINE);
		// Here 160 kbps video is 160000
		video.setBitRate(VIDEO_BIT_RATE);
		// More the frames more quality and size, but keep it low based on devices like
		// mobile
		video.setFrameRate(VIDEO_FRAME_RATE);
		video.setSize(new VideoSize(VIDEO_WIDTH, VIDEO_HEIGHT));

		return video;
	}

	private EncodingAttributes setEncodingAttributes(AudioAttributes audio, VideoAttributes video) {
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat(FORMAT_MP4);
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);

		return attrs;
	}

	private File captureVideoFrame(File mp4File) {
		File jpgFrame = null;
		try {
			int frameNumber = 1;

			jpgFrame = new File(mp4File.getParentFile().getAbsolutePath() + DELIMETER_FORWARD_SLASH
					+ mp4File.getName().substring(0, mp4File.getName().lastIndexOf(DELIMETER_DOT)) + EXTENSION_JPG);
			Picture picture = FrameGrab.getFrameFromFile(mp4File, frameNumber);
			BufferedImage bufferImg = AWTUtil.toBufferedImage(picture);
			ImageIO.write(bufferImg, FORMAT_JPG, jpgFrame);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jpgFrame;

	}

	private String saveIntoContentStore(FileSystemContent fileObj, String assetCode, String date, File file,
			String flag) {
		String path = null;
		try {
			fileObj.setContentPath("ooh_videos/"+assetCode + DELIMETER_FORWARD_SLASH + date + DELIMETER_FORWARD_SLASH + flag + DELIMETER_FORWARD_SLASH + file.getName());
			InputStream inputStream = new FileInputStream(file);
			path = fileObj.getContentPath();
			contentStore.setContent(fileObj, inputStream);
			filesRepo.save(fileObj);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;

	}

	@Override
	public void imageProcessing(String path, MediaSite mediasite, String dateStr) {
		
		List<String> jpgPathList = readAllSpecifiedFileList(path+dateStr+"/", EXTENSION_JPG);

		saveImageListFromDirectory(path, jpgPathList, mediasite);

	}

	private void saveImageListFromDirectory(String assetPath, List<String> jpgPathList, MediaSite mediasite) {
		try {
			List<MultimediaLog> imageLogList = new ArrayList<>();
			File dateFolder = null;
			for (String imagePath : jpgPathList) {

				File imgFile = new File(assetPath + imagePath);

				String date = imagePath.split("\\\\")[1];

				dateFolder = new File(assetPath + date);

				Date dateTime = getImageDateTime(date, imgFile);

				MultimediaLog multimedia = new MultimediaLog();
				multimedia.setTimeStamp(dateTime);
				multimedia.setMediaSite(mediasite);
				multimedia.setImageUrl(ROOT_DIR_NAME +"ooh_images/"+mediasite.getAssetCode()+"/"+ imagePath);
				BufferedImage bufferImage = ImageIO.read(new File(assetPath + imagePath));
				String[] imagePathList = imageProcessingUtil.saveImageDahuaIntoFileSystem(bufferImage,
						mediasite, date, imgFile.getName());
				
				awsUtil.uploadFileToAWS(imagePathList);
				
				multimedia.setOriginalPath("https://eikona-ooh.s3.ap-south-1.amazonaws.com/"+imagePathList[0]);
				multimedia.setThumbnailPath("https://eikona-ooh.s3.ap-south-1.amazonaws.com/"+imagePathList[1]);
				imageLogList.add(multimedia);
				
				
//				imgFile.delete();
			}

			multimediaLogRepository.saveAll(imageLogList);
//			 if (0 == dateFolder.listFiles().length)
//				 dateFolder.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Date getImageDateTime(String date, File imgFile) {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date dateTime = null;
		try {
			Date dateStamp = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			date = dateFormat.format(dateStamp);

			String[] arrayParentPath = imgFile.getAbsolutePath().split("\\\\");
//			String parentPath = imgFile.getParentFile().getAbsolutePath();
//			String hourMinute = parentPath.substring(parentPath.length() - 5, parentPath.length());
//			String hour = hourMinute.split(DELIMETER_FORWARD_SLASH)[0];
//			String min = hourMinute.split(DELIMETER_FORWARD_SLASH)[1];
			
			String hour = arrayParentPath[arrayParentPath.length-2];
			String min = arrayParentPath[arrayParentPath.length-1].substring(0,2);
			
			String second = imgFile.getName().substring(3, 5);

			String time = hour + DELIMETER_COLON + min + DELIMETER_COLON + second;
			dateTime = dateTimeFormat.parse(date + DELIMETER_SPACE + time);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateTime;
	}

}
