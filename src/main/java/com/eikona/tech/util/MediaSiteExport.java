package com.eikona.tech.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.eikona.tech.domain.MediaSite;

@Component
public class MediaSiteExport {
	
	public void excelGenerator(List<MediaSite> mediaSiteList, HttpServletResponse response) {
		
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
		cell.setCellValue("Id");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(1);
		cell.setCellValue("assetCode");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(2);
		cell.setCellValue("vendorAssetCode");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(3);
		cell.setCellValue("ownedByOrgId");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(4);
		cell.setCellValue("status");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(5);
		cell.setCellValue("length");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(6);
		cell.setCellValue("width");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(7);
		cell.setCellValue("height");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(8);
		cell.setCellValue("orientation");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(9);
		cell.setCellValue("captureFrequency");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(10);
		cell.setCellValue("illumination");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(11);
		cell.setCellValue("latitude");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(12);
		cell.setCellValue("longitude");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(13);
		cell.setCellValue("road");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(14);
		cell.setCellValue("locality");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(15);
		cell.setCellValue("city");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(16);
		cell.setCellValue("district");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(17);
		cell.setCellValue("state");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(18);
		cell.setCellValue("pincode");
		cell.setCellStyle(cellStyle);
		
		cell = row.createCell(19);
		cell.setCellValue("cityTier");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(20);
		cell.setCellValue("mediaClass");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(21);
		cell.setCellValue("structureType");
		cell.setCellStyle(cellStyle);
		
		cell = row.createCell(22);
		cell.setCellValue("mediaType");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(23);
		cell.setCellValue("placeType");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(24);
		cell.setCellValue("material");
		cell.setCellStyle(cellStyle);
		
		cell = row.createCell(25);
		cell.setCellValue("placementType");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(26);
		cell.setCellValue("catchmentStrata");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(27);
		cell.setCellValue("locationType");
		cell.setCellStyle(cellStyle);
		
		cell = row.createCell(28);
		cell.setCellValue("trafficType");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(29);
		cell.setCellValue("trafficDensity");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(30);
		cell.setCellValue("trafficSignal");
		cell.setCellStyle(cellStyle);
		
		cell = row.createCell(31);
		cell.setCellValue("ageGroup");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(32);
		cell.setCellValue("viewingDistance");
		cell.setCellStyle(cellStyle);
		
		cell = row.createCell(33);
		cell.setCellValue("viewingTime");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(34);
		cell.setCellValue("quality");
		cell.setCellStyle(cellStyle);

		cellStyle = workBook.createCellStyle();
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);

		for (MediaSite mediaSite : mediaSiteList) {
			row = sheet.createRow(rowCount++);

			int columnCount = 0;

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getAssetCode());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getVendorAssetCode());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getOwnedByOrgId())
				cell.setCellValue(mediaSite.getOwnedByOrgId().getName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getStatus())
				cell.setCellValue(mediaSite.getStatus().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getLength());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getWidth());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getHeight());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getOrientation())
				cell.setCellValue(mediaSite.getOrientation().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getCaptureFrequency())
				cell.setCellValue(mediaSite.getCaptureFrequency().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getIllumination())
				cell.setCellValue(mediaSite.getIllumination().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getLatitude());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getLongitude());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getRoad());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getLocality());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getCity());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getDistrict());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getState());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getPincode());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(columnCount++);
			if (null != mediaSite.getCityTier())
				cell.setCellValue(mediaSite.getCityTier().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getMediaClass())
				cell.setCellValue(mediaSite.getMediaClass().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getStructureType())
				cell.setCellValue(mediaSite.getStructureType().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getMediaType())
				cell.setCellValue(mediaSite.getMediaType().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getPlaceType())
				cell.setCellValue(mediaSite.getPlaceType().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getMaterial())
				cell.setCellValue(mediaSite.getMaterial().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getPlacementType())
				cell.setCellValue(mediaSite.getPlacementType().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getCatchmentStrata())
				cell.setCellValue(mediaSite.getCatchmentStrata().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getLocationType())
				cell.setCellValue(mediaSite.getLocationType().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getTrafficType())
				cell.setCellValue(mediaSite.getTrafficType().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getTrafficDensity())
				cell.setCellValue(mediaSite.getTrafficDensity().getValue());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.isTrafficSignal());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(columnCount++);
			if (null != mediaSite.getAgeGroup())
				cell.setCellValue(mediaSite.getAgeGroup().getValue());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(columnCount++);
			if (null != mediaSite.getViewingDistance())
				cell.setCellValue(mediaSite.getViewingDistance().getValue());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(columnCount++);
			cell.setCellValue(mediaSite.getViewingTime());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(columnCount++);
			if (null != mediaSite.getQuality())
				cell.setCellValue(mediaSite.getQuality().getValue());
			cell.setCellStyle(cellStyle);

		}

		ServletOutputStream outputStream;
		try {
			outputStream = response.getOutputStream();
			workBook.write(outputStream);
			workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
