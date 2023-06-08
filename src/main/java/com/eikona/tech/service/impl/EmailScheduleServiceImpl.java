package com.eikona.tech.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.dto.ImageCountDto;
import com.eikona.tech.repository.MultimediaLogRepository;
import com.eikona.tech.repository.MediaSiteRepository;

@Service
@EnableScheduling
public class EmailScheduleServiceImpl {

	@Autowired
	private MultimediaLogRepository imageLogRepository;

	@Value("${mail.fromMail}")
	private String fromMail;

	@Value("${mail.password}")
	private String fromPassword;
	
	@Value("${mail.toMail}")
	private String toMail;

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

	@Value("${ftp.write.path}")
	private String ftpWritePath;

	@Autowired
	private MediaSiteRepository mediaSiteRepository;

	public void findImagelog(Long assetId, Date startDate, Date endDate) {

		List<MultimediaLog> imageLogList = imageLogRepository.findImageLog(assetId, startDate, endDate);
		MultimediaLog image = imageLogList.get(0);
		try {
			sendImageInBodyByEmail(image);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Scheduled(fixedDelay=3000)
	public void sendMailOfImageCountReport() throws Exception{
		String fileName = imageCountExcelReportGenerate();
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
			props.put("mail.smtp.port", "587"); //TLS Port
			props.put("mail.smtp.auth", "true"); //enable authentication
			props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
			
	        //create Authenticator object to pass in Session.getInstance argument
			Authenticator auth = new Authenticator() {
				//override the getPasswordAuthentication method
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromMail, fromPassword);
				}
			};
			Session session = Session.getInstance(props, auth);
			MimeMessage msg = new MimeMessage(session);
			//set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			
			msg.setFrom(new InternetAddress(fromMail));
			
			msg.setReplyTo(InternetAddress.parse(fromMail, false));
			  
			InternetAddress[] myBccList = InternetAddress.parse("shravan@eikona.tech");
			//InternetAddress[] myCcList = InternetAddress.parse("NEHA.SIVA@xyz.com");
			msg.setRecipients(Message.RecipientType.BCC,myBccList);
			//msg.setRecipients(Message.RecipientType.CC,myCcList);

			// Set Subject: header field
			msg.setSubject("Mediasite Image Count Report On Daily Basis");

			// Create the message part 
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText("");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(fileName);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			msg.setContent(multipart);

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail, false));
			System.out.println("Message is ready");
			Transport.send(msg);

			System.out.println("Email Sent Successfully!!");
	    }
		catch (AddressException e) {
            throw new AddressException("[sendEmail]: Incorrect email address");

        } catch (MessagingException e) {
            throw new MessagingException("[sendEmail]:Authentication Failed for SMTP port No or specific Email Id");

        } catch (Exception e) {
            throw  new Exception("[sendEmail]: Error in method " + e.getMessage());
        }
		
	}


	private String imageCountExcelReportGenerate() {
		try {

		List<ImageCountDto> imageCountList = getImageCountListFromFtp();

		DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy_hh mm ss");
		String currentDateTime = dateFormat.format(new Date());
//		File theDir = new File(localPath);
//		if (!theDir.exists()) {
//			theDir.mkdirs();
//		}
		String filename = "Image_Count_Report_ExcelFile_" + currentDateTime + ".xls";
		Workbook workBook = new XSSFWorkbook();
		Sheet sheet = workBook.createSheet();

		int rowCount = 0;
		Row row = sheet.createRow(rowCount++);

		Font font = workBook.createFont();
		font.setBold(true);

		CellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setBorderTop(BorderStyle.THICK);
		cellStyle.setBorderBottom(BorderStyle.THICK);
		cellStyle.setBorderLeft(BorderStyle.THICK);
		cellStyle.setBorderRight(BorderStyle.THICK);
		cellStyle.setFont(font);
		
		Cell cell = row.createCell(0);
		cell.setCellValue("Date");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(1);
		cell.setCellValue("Asset Code");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(2);
		cell.setCellValue("Locality");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(3);
		cell.setCellValue("Total Image Count");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(4);
		cell.setCellValue("Zero Kb Image Count");
		cell.setCellStyle(cellStyle);

		cellStyle = workBook.createCellStyle();
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);

		for (ImageCountDto imageCountDto : imageCountList) {
			row = sheet.createRow(rowCount++);

			int columnCount = 0;
			
			cell = row.createCell(columnCount++);
			cell.setCellValue(imageCountDto.getDate());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(imageCountDto.getAssetCode());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(imageCountDto.getLocality());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(imageCountDto.getTotalImage());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(imageCountDto.getZeroKbImage());
			cell.setCellStyle(cellStyle);
		}

		FileOutputStream fileOut = new FileOutputStream(filename);
		workBook.write(fileOut);
		fileOut.close();
		workBook.close();

		return filename;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	private List<ImageCountDto> getImageCountListFromFtp() {

		List<ImageCountDto> imageCountList = new ArrayList<>();
		FTPClient ftp = new FTPClient();
		SimpleDateFormat ftpDateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			ftp.connect(host);
			boolean isSuccess = ftp.login(user, password);
			FTPFile[] directoryFTP = null;
			FTPFile[] dateDirectory = null;
			FTPFile[] imageDirectory = null;
			if (isSuccess) {

				directoryFTP = ftp.listFiles(ftpWritePath);
				for (FTPFile directory : directoryFTP) {
					MediaSite mediasite = mediaSiteRepository.findByAssetCodeAndIsDeletedFalse(directory.getName());
					if (null != mediasite) {
						dateDirectory = ftp.listFiles(ftpWritePath + directory.getName());
						
						Date currentDate = new Date();
						Calendar startDate = Calendar.getInstance();
						startDate.setTime(currentDate);
						startDate.add(Calendar.DATE, -1);
						String currDateStr = ftpDateFormat.format(startDate.getTime());
						System.out.println(currentDate);
						System.out.println(startDate.getTime());
						for (FTPFile datefolder : dateDirectory) {
							if ((datefolder.getName()).equalsIgnoreCase(currDateStr)) {
								ImageCountDto imageCountDto = new ImageCountDto();
								imageCountDto.setDate(dateFormat.format(currentDate));
								imageCountDto.setAssetCode(mediasite.getAssetCode());
								imageCountDto.setLocality(mediasite.getLocality());
								imageDirectory = ftp
										.listFiles(ftpWritePath + directory.getName() + "/" + datefolder.getName());
								long totalImageCount = 0l;
								long zeroKbImageCount = 0l;
								for (FTPFile imageFile : imageDirectory) {
									String imazeSize = String.valueOf(imageFile.getSize());
									if (!("test".equalsIgnoreCase(imageFile.getName()))) {
										if (("0".equalsIgnoreCase(imazeSize))) {
											zeroKbImageCount += 1;
										}
										totalImageCount += 1;
									}

								}
								imageCountDto.setTotalImage(totalImageCount);
								imageCountDto.setZeroKbImage(zeroKbImageCount);
								imageCountList.add(imageCountDto);
							}

						}

					}
				}
			}
			return imageCountList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void sendImageInBodyByEmail(MultimediaLog image) throws Exception {
		try {
			final String subject = "Mediasite Daily Report";
			final String toEmail = image.getMediaSite().getToMail();
			final String fromEmail = fromMail;
			final String password = fromPassword;

			final String bodyassetCode = image.getMediaSite().getAssetCode();
			final String bodylocation = image.getMediaSite().getLocality();
			final String bodycity = image.getMediaSite().getCity();
			final String bodyDistrict = image.getMediaSite().getDistrict();
			final String bodyState = image.getMediaSite().getState();
			final String bodyPin = image.getMediaSite().getPincode();

			final String base64 = image.getImage();

			final String bodyhtml = "<html><body> <img src=\"cid:qrImage \" alt=\"qr code\">"//
					+ "<p>AssetCode: " + bodyassetCode + "<br> Locality: " + bodylocation + "<br> City: " + bodycity
					+ "<br> District: " + bodyDistrict + "<br> State: " + bodyState + "<br> Pin: " + bodyPin + "</p>"
					+ "<a  style=\"background-color:green;\" href=\"mailto:" + toEmail
					+ "?subject=Request Approved &body=Hi, Your request is Approved by " + toEmail
					+ " for the below mentioned responsibility.\">Approve</a> "
					+ "<a style=\"background-color:red;\" href=\"mailto:" + toEmail
					+ "?subject=Request Rejected &body=Hi, Your request is rejected by " + toEmail
					+ " for the below mentioned responsibility\">Reject</a></body></html>";
			// MimeMessage message = mailSender.createMimeMessage();
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
			props.put("mail.smtp.port", "587"); // TLS Port
			props.put("mail.smtp.auth", "true"); // enable authentication
			props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

			// create Authenticator object to pass in Session.getInstance argument
			Authenticator auth = new Authenticator() {
				// override the getPasswordAuthentication method
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			};
			Session session = Session.getInstance(props, auth);
			MimeMessage message = new MimeMessage(session);
			message.setSubject(subject);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent("Report", "text/plain; charset=UTF-8");

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(bodyhtml, "text/html; charset=UTF-8");

			Multipart multiPart = new MimeMultipart("alternative");

			// create a new imagePart and add it to multipart so that the image is inline
			// attached in the email
			byte[] rawImage = Base64.getDecoder().decode(base64);
			BodyPart imagePart = new MimeBodyPart();
			ByteArrayDataSource imageDataSource = new ByteArrayDataSource(rawImage, "image/jpeg");

			imagePart.setDataHandler(new DataHandler(imageDataSource));
			imagePart.setHeader("Content-ID", "<qrImage>");
			imagePart.setFileName("mediasite.jpeg");

			multiPart.addBodyPart(imagePart);
			multiPart.addBodyPart(textPart);
			multiPart.addBodyPart(htmlPart);

			message.setContent(multiPart);

			// mailSender.send(message);
			Transport.send(message);
			System.out.println("Email Send Successfully!!");
		} catch (MessagingException exception) {
			exception.printStackTrace();
		}
	}
}
