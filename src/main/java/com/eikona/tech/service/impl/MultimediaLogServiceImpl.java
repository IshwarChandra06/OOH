package com.eikona.tech.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.util.Base64;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.MediaSiteRepository;
import com.eikona.tech.repository.MultimediaLogRepository;
import com.eikona.tech.service.MultimediaLogService;
import com.eikona.tech.service.MultimediaProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
@EnableScheduling
public class MultimediaLogServiceImpl implements MultimediaLogService {

	@Autowired
	protected MultimediaLogRepository imageLogRepository;

	@SuppressWarnings("rawtypes")
	@Autowired
	protected PaginatedServiceImpl paginatedServiceImpl;

	@Autowired
	private MediaSiteRepository mediaSiteRepository;
	
	@Value("${spring.content.fs.filesystemRoot}")
	private String rootPath;
	
	@Autowired
	@Qualifier("dahuaMultimediaProcessingService")
	private MultimediaProcessingService dahuaMultimediaProcessingService;
	
	@Autowired
	@Qualifier("unvMultimediaProcessingService")
	private MultimediaProcessingService unvMultimediaProcessingService;

	@Value("${ftp.host}")
	private String host;

	@Value("${ftp.port}")
	private int port;

	@Value("${ftp.username}")
	private String user;

	@Value("${ftp.password}")
	private String password;

	@Value("${server.local.path}")
	private String localPath;

	@Value("${ftp.read.path}")
	private String ftpReadPath;

	@Value("${ftp.write.path}")
	private String ftpWritePath;
	
	//@Scheduled(cron = "0 0/20 12,22 ? * *")
//	@Scheduled(fixedDelay = 300000)
	@Override
	public void multimediaProcessingFromDirectory() {
		
		String directoryPath = "D:/workspace/UFO/Images/";

		File rootFileStr = new File("D:/workspace/UFO/Images/");
		String assetCode = null;
		for (File mediasiteFile : rootFileStr.listFiles()) {

			assetCode = mediasiteFile.getName();
			MediaSite mediasite = mediaSiteRepository.findByAssetCode(assetCode);
			String assetFolderPath = directoryPath + assetCode + "/";
			
			if(null!=mediasite.getCamera()) {
				

				if("Dahua".equalsIgnoreCase(mediasite.getCamera().getName())) {
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String dateStr = format.format(new Date());
					
					if(mediasite.getCamera().isSaveVideo())
						dahuaMultimediaProcessingService.videoProcessing(assetFolderPath, mediasite, dateStr);
					if(mediasite.getCamera().isSaveImage())
						dahuaMultimediaProcessingService.imageProcessing(assetFolderPath, mediasite, dateStr);
					
				} else{
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					String dateStr = format.format(new Date());
						
					if(mediasite.getCamera().isSaveImage())
						unvMultimediaProcessingService.imageProcessing(assetFolderPath, mediasite, dateStr);
				}
			}
		}
	}
	
//	@Scheduled(fixedDelay = 3000000)
//	public void test() {
//		multimediaProcessingFromDirectory("20220501");
//	}
	
	public void multimediaProcessingFromDirectory(String dateStr) {
		
		String directoryPath = "D:/workspace/UFO/Images/";

		File rootFileStr = new File("D:/workspace/UFO/Images/");
		String assetCode = null;
		for (File mediasiteFile : rootFileStr.listFiles()) {

			assetCode = mediasiteFile.getName();
			MediaSite mediasite = mediaSiteRepository.findByAssetCode(assetCode);
			String assetFolderPath = directoryPath + assetCode + "/";
			
			if(null!=mediasite.getCamera()) {
				SimpleDateFormat dahuaFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat unvFormat = new SimpleDateFormat("yyyyMMdd");
				if("Dahua".equalsIgnoreCase(mediasite.getCamera().getName())) {
					
					try {
						Date date = unvFormat.parse(dateStr);
					
						String dahuaDate = dahuaFormat.format(date);
						
						if(mediasite.getCamera().isSaveVideo())
							dahuaMultimediaProcessingService.videoProcessing(assetFolderPath, mediasite, dahuaDate);
						if(mediasite.getCamera().isSaveImage())
							dahuaMultimediaProcessingService.imageProcessing(assetFolderPath, mediasite, dahuaDate);
					
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else{
					if(mediasite.getCamera().isSaveImage())
						unvMultimediaProcessingService.imageProcessing(assetFolderPath, mediasite, dateStr);
				}
			}
		}
	}

	//@Scheduled(fixedDelay = 30000)
	//@Scheduled(cron="0 55 23 L * ?")
	public void deleteFileFromDirectory() {

		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(new Date());
		startCalendar.add(Calendar.MONTH, -3);
		
		int firstDayOfMonth = 1;
		int lastDayOfMonth = startCalendar.getActualMaximum(Calendar.DATE);
		
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(startCalendar.getTime());
		
		startCalendar.set(Calendar.DATE, firstDayOfMonth);
		endCalendar.set(Calendar.DATE, lastDayOfMonth);
		
		List<MediaSite> mediaSitesList = mediaSiteRepository.findAllByIsDeletedFalse();
		
		getMultimediaLogAccordingToMediasiteAndDate(mediaSitesList,startCalendar,endCalendar);
		
	}
	public void getMultimediaLogAccordingToMediasiteAndDate(List<MediaSite> mediaSitesList,Calendar startCalendar,Calendar endCalendar){
		
		for(MediaSite mediaSite: mediaSitesList) {
			
			List<MultimediaLog> multimediaLogList = imageLogRepository.findImageLog(mediaSite.getId(), startCalendar.getTime(), endCalendar.getTime());
			
			if(null != multimediaLogList && !multimediaLogList.isEmpty()) {
				MultimediaLog multimediaLogObj = multimediaLogList.get(0);
				
				Date date = multimediaLogObj.getTimeStamp();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.HOUR, 00);
				calendar.set(Calendar.MINUTE, 00);
				calendar.set(Calendar.SECOND, 00);
				
				deleteLastThreeMonthMultimediaLog(multimediaLogList,calendar);
				
			}
		}
	}
	public void deleteLastThreeMonthMultimediaLog(List<MultimediaLog> multimediaLogList,Calendar calendar){
		//List<MultimediaLog> multimediaLogDeleteList =  new ArrayList<MultimediaLog>();
		for(MultimediaLog multimediaLog : multimediaLogList) {
			
			if(multimediaLog.getTimeStamp().before(calendar.getTime())) {
				//multimediaLogDeleteList.add(multimediaLog);
				
				File dateFile=new File(multimediaLog.getOriginalPath()).getParentFile().getParentFile().getAbsoluteFile();
				
				deleteDirectory(dateFile);
				dateFile.delete();
				
				imageLogRepository.delete(multimediaLog);
			}
			
		}
	}
	 public  void deleteDirectory(File file) {
	        // store all the paths of files and folders present
	        // inside directory
	        for (File subfile : file.listFiles()) {
	  
	            // if it is a subfolder,e.g Rohan and Ritik,
	            // recursiley call function to empty subfolder
	            if (subfile.isDirectory()) {
	                deleteDirectory(subfile);
	            }
	  
	            // delete files and empty subfolders
	            subfile.delete();
	        }
	    }
	
	// @Override
	public MultimediaLog find(Long id) {
		MultimediaLog result = null;

		Optional<MultimediaLog> catalog = imageLogRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
		}

		return result;
	}

	// @Override
	public List<MultimediaLog> findAll() {
		return imageLogRepository.findAllByIsDeletedFalse();
	}

	// @Override
	public MultimediaLog save(MultimediaLog entity) {
		entity.setDeleted(false);
		return imageLogRepository.save(entity);
	}

	// @Override
	public void delete(Long id) {
		MultimediaLog result = null;

		Optional<MultimediaLog> catalog = imageLogRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
			result.setDeleted(true);
		}
		this.imageLogRepository.save(result);
	}

	// @Override
	public MultimediaLog update(MultimediaLog entity) {
		return imageLogRepository.save(entity);
	}

	public DataTablesOutput<MultimediaLog> getAll(@Valid DataTablesInput input) {
		Specification<MultimediaLog> isDeletedFalse = (Specification<MultimediaLog>) (root, query, builder) -> {
			return builder.equal(root.get("isDeleted"), false);
		};
		return (DataTablesOutput<MultimediaLog>) imageLogRepository.findAll(input, isDeletedFalse);
	}

	@Override
	public Page<MultimediaLog> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder) {
		if (null == sortOrder || sortOrder.isEmpty()) {
			sortOrder = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Specification<MultimediaLog> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.imageLogRepository.findAll(isDeleted, pageable);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<MultimediaLog> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto) {

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(paginatedDto.getSearchData(), Map.class);
		if (null == sortOrder || sortOrder.isEmpty()) {
			sortOrder = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}

		Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<MultimediaLog> fieldSpc = paginatedServiceImpl.fieldSpecification(map);
		Page<MultimediaLog> allOrganization = imageLogRepository.findAll(fieldSpc, pageable);

		return allOrganization;
	}

	@Deprecated
	@Override
	// @Scheduled(cron="*/20 * * * * ?")
	public String dataFromFtpToDataBase() {

		String writeSubUrl = "ftp://" + host + ftpWritePath;
		String readSubUrl = "ftp://" + host + ftpReadPath;
		String completeReadUrl = null;
		String readUrl = null;
		String completeNewUrl = null;
		String newUrl = null;
		FTPClient ftp = new FTPClient();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		try {
			ftp.connect(host);
			boolean isSuccess = ftp.login(user, password);
			FTPFile[] directoryFTP = null;
			FTPFile[] dateDirectory = null;
			FTPFile[] imageDirectory = null;
			if (isSuccess) {

				directoryFTP = ftp.listFiles(ftpReadPath);

				for (FTPFile directory : directoryFTP) {
					List<MultimediaLog> imageLogList = new ArrayList<>();
					MediaSite mediasite = mediaSiteRepository.findByAssetCodeAndIsDeletedFalse(directory.getName());
					if (null != mediasite) {
						dateDirectory = ftp.listFiles(ftpReadPath + directory.getName());
						for (FTPFile datefolder : dateDirectory) {

							imageDirectory = ftp.listFiles(ftpReadPath + directory.getName() + "/" + datefolder.getName());
							completeReadUrl = writeSubUrl + directory.getName() + "/" + datefolder.getName();
							completeNewUrl = readSubUrl + directory.getName() + "/" + datefolder.getName();

							for (FTPFile imageFile : imageDirectory) {
								String imageFileName = imageFile.getName();
								String imageSize = String.valueOf(imageFile.getSize());
								if (!("test".equalsIgnoreCase(imageFileName)) && !("0".equalsIgnoreCase(imageSize))) {
									
									readUrl = completeReadUrl + "/" + imageFileName;
									newUrl = completeNewUrl + "/" + imageFileName;

									MultimediaLog imagelog = new MultimediaLog();
									imagelog.setImageUrl(readUrl);
									imagelog.setMediaSite(mediasite);

									String year = imageFileName.substring(0, 4);
									String month = imageFileName.substring(4, 6);
									String day = imageFileName.substring(6, 8);
									String hour = imageFileName.substring(8, 10);
									String min = imageFileName.substring(10, 12);
									String sec = imageFileName.substring(12, 14);
									String dateTimeStr = day + "-" + month + "-" + year + " " + hour + ":" + min + ":"
											+ sec;
									try {
										Date date = dateFormat.parse(dateTimeStr);
										imagelog.setTimeStamp(date);
									} catch (ParseException e) {
										e.printStackTrace();
									}
									MultimediaLog imageLog = imageLogRepository.findByImageUrl(readUrl);
									if (null == imageLog) {

										boolean directoryExists = ftp
												.changeWorkingDirectory(ftpWritePath + directory.getName());
										if (directoryExists == false) {
											boolean createDir = ftp.makeDirectory(ftpWritePath + directory.getName());
											boolean dateFolderExists = ftp.changeWorkingDirectory(ftpWritePath + directory.getName() + "/" + datefolder.getName());
											if (dateFolderExists == false) {
												boolean createDateDir = ftp.makeDirectory(ftpWritePath
														+ directory.getName() + "/" + datefolder.getName());
											}

										} else {
											boolean dateFolderExists = ftp.changeWorkingDirectory(
													ftpWritePath + directory.getName() + "/" + datefolder.getName());
											if (dateFolderExists == false) {
												boolean createDateDir = ftp.makeDirectory(ftpWritePath
														+ directory.getName() + "/" + datefolder.getName());
												System.out.println(createDateDir);
											}
										}
										String base64Image = sendFTPfile(directory.getName(), datefolder.getName(),
												imageFileName, newUrl);
										imagelog.setImage(base64Image);
										if (null != imagelog.getImage())
											imageLogList.add(imagelog);
									}
								}

							}
						}
					}
					imageLogRepository.saveAll(imageLogList);
				}
			}

			ftp.logout();

			return "Sync Successfully !";
			
		} catch (IOException e) {
			e.printStackTrace();
			return "Sync Failed !";
		} finally {
			try {
				ftp.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String sendFTPfile(String assetFolder, String dateFolder, String imageName, String url) {

		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(host, port);
			ftpClient.login(user, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			String[] urlpart = url.split(host);
			String remoteFile = urlpart[1];
			File downloadFile = new File(localPath + imageName);
			BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));
			ftpClient.retrieveFile(remoteFile, outputStream1);

			String deleteFileName = ftpReadPath + assetFolder + "/" + dateFolder + "/" + imageName;
			boolean deletefile = ftpClient.deleteFile(deleteFileName);

			String remoteFileName = ftpWritePath + assetFolder + "/" + dateFolder + "/" + imageName;
			File secondLocalFile = new File(localPath + imageName);

			InputStream inputStream = new FileInputStream(secondLocalFile);

			InputStream convertByteStream = new FileInputStream(secondLocalFile);

			byte[] bytes = IOUtils.toByteArray(convertByteStream);
			String base64 = Base64.encodeBase64String(bytes);

			System.out.println("Start uploading file");
			OutputStream outputStream = ftpClient.storeFileStream(remoteFileName);
			byte[] bytesIn = new byte[4096];
			int read = 0;

			while ((read = inputStream.read(bytesIn)) != -1) {
				outputStream.write(bytesIn, 0, read);
			}
			inputStream.close();
			outputStream.close();

			boolean completed = ftpClient.completePendingCommand();
			if (completed) {
				System.out.println("The file is uploaded successfully.");
			}
			return base64;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
