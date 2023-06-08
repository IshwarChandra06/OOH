package com.eikona.tech.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eikona.tech.domain.ConstraintRange;
import com.eikona.tech.domain.ConstraintSingle;
import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.Organization;
import com.eikona.tech.domain.User;
import com.eikona.tech.dto.MediaSiteImportDto;
import com.eikona.tech.repository.ConstraintRangeRepository;
import com.eikona.tech.repository.ConstraintSingleRepository;
import com.eikona.tech.repository.MediaSiteRepository;
import com.eikona.tech.repository.OrganizationRepository;
import com.eikona.tech.repository.UserRepository;

@Component
public class MediaSiteImport {
	
	@Autowired
	private ConstraintSingleRepository constraintSingleRepository;

	@Autowired
	private ConstraintRangeRepository constraintRangeRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MediaSiteRepository mediaSiteRepository;
	
	public List<MediaSite> importMediaSite(InputStream is) {

		try {
			String[] singleList = {"Status","Orientation","Capture Frequency","Illumination","City Tier","Media Class","Structure Type","Media Type",
					"Place Type","Material","Placement Type","Catchment Strata","Location Type","Traffic Type","Traffic Density","Viewing Distance",
					"Quality"};
			String[] rangeList = {"Age Group"};
			
			List<ConstraintSingle> constraintSingleList = (List<ConstraintSingle>) constraintSingleRepository.findByType(singleList);
			Map<String, Long> mapSingle = new HashMap<String, Long>();
			
			for(ConstraintSingle constraintSingle: constraintSingleList) {
				String key = constraintSingle.getType()+"-"+constraintSingle.getValue();
				Long value = constraintSingle.getId();
				
				mapSingle.put(key, value);
			}
			
			List<ConstraintRange> constraintRangeList = (List<ConstraintRange>) constraintRangeRepository.findByType(rangeList);
			Map<String, Long> mapRange = new HashMap<String, Long>();
			
			for(ConstraintRange constraintRange: constraintRangeList) {
				String key = constraintRange.getType()+"-"+constraintRange.getValue();
				Long value = constraintRange.getId();
				
				mapRange.put(key, value);
			}
			
//			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
			Workbook workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rows = sheet.iterator();
			List<MediaSite> transaction = new ArrayList<MediaSite>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber < 1) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();
				int cellIndex = 0;
				MediaSite mediaSite = new MediaSite();
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					if (null == mediaSite) {
						break;
					} else if (cellIndex == 0) {
						mediaSite.setAssetCode(currentCell.getStringCellValue());
					} else if (cellIndex == 1) {
						mediaSite.setVendorAssetCode(currentCell.getStringCellValue());
					} else if (cellIndex == 2) {
						String field = currentCell.getStringCellValue();
						Organization obj = organizationRepository.findByName(field);
						if(null != obj) {
							obj.setName(currentCell.getStringCellValue());
							mediaSite.setOwnedByOrgId(obj);
						}
					} else if (cellIndex == 3) {
						String key = "Status-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setStatus(conSingle);
						}
					} else if (cellIndex == 4) {
						mediaSite.setLength(currentCell.getStringCellValue());
					} else if (cellIndex == 5) {
						mediaSite.setWidth(currentCell.getStringCellValue());
					} else if (cellIndex == 6) {
						mediaSite.setHeight(currentCell.getStringCellValue());
					} else if (cellIndex == 7) {
						String key = "Orientation-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setOrientation(conSingle);
						}
					} else if (cellIndex == 8) {
						String key = "Capture Frequency-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setCaptureFrequency(conSingle);
						}
					} else if (cellIndex == 9) {
						String key = "Illumination-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setIllumination(conSingle);
						}
					} else if (cellIndex == 10) {
						mediaSite.setLatitude(currentCell.getStringCellValue());
					} else if (cellIndex == 11) {
						mediaSite.setLongitude(currentCell.getStringCellValue());
					} else if (cellIndex == 12) {
						mediaSite.setRoad(currentCell.getStringCellValue());
					} else if (cellIndex == 13) {
						mediaSite.setLocality(currentCell.getStringCellValue());
					} else if (cellIndex == 14) {
						mediaSite.setCity(currentCell.getStringCellValue());
					} else if (cellIndex == 15) {
						mediaSite.setDistrict(currentCell.getStringCellValue());
					} else if (cellIndex == 16) {
						mediaSite.setState(currentCell.getStringCellValue());
					} else if (cellIndex == 17) {
						mediaSite.setPincode(currentCell.getStringCellValue());
					} else if (cellIndex == 18) {
						String key = "City Tier-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setCityTier(conSingle);
						}
					} else if (cellIndex == 19) {
						String key = "Media Class-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setMediaClass(conSingle);
						}
					} else if (cellIndex == 20) {
						String key = "Structure Type-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setStructureType(conSingle);
						}
					} else if (cellIndex == 21) {
						String key = "Media Type-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setMediaType(conSingle);
						}
					} else if (cellIndex == 22) {
						String key = "Place Type-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setPlaceType(conSingle);
						}
					} else if (cellIndex == 23) {
						String key = "Material-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setMaterial(conSingle);
						}
					} else if (cellIndex == 24) {
						String key = "Placement Type-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setPlacementType(conSingle);
						}
					} else if (cellIndex == 25) {
						String key = "Catchment Strata-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setCatchmentStrata(conSingle);
						}
					} else if (cellIndex == 26) {
						String key = "Location Type-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setLocationType(conSingle);
						}
					} else if (cellIndex == 27) {
						String key = "Traffic Type-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setTrafficType(conSingle);
						}
					} else if (cellIndex == 28) {
						String key = "Traffic Density-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setTrafficDensity(conSingle);
						}
					} else if (cellIndex == 29) {
						mediaSite.setTrafficSignal("true".equalsIgnoreCase(currentCell.getStringCellValue()));
					} else if (cellIndex == 30) {
						String key = "Age Group-"+currentCell.getStringCellValue();
						Long id = mapRange.get(key);
						ConstraintRange conRange = new ConstraintRange();
						conRange.setId(id);
						if(null != conRange.getId()) {
							mediaSite.setAgeGroup(conRange);
						}
					} else if (cellIndex == 31) {
						String key = "Viewing Distance-"+currentCell.getStringCellValue();
						Long id = mapSingle.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setViewingDistance(conSingle);
						}
					}else if (cellIndex == 32) {
						mediaSite.setViewingTime(currentCell.getStringCellValue());
					} else if (cellIndex == 33) {
						String key = "Quality-"+currentCell.getStringCellValue();
						Long id = mapRange.get(key);
						ConstraintSingle conSingle = new ConstraintSingle();
						conSingle.setId(id);
						if(null != conSingle.getId()) {
							mediaSite.setQuality(conSingle);
						}
					}
					cellIndex++;
				}
				mediaSite.setDeleted(false);
				transaction.add(mediaSite);
			}
			workbook.close();

			return transaction;
		} catch (IOException e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}
	}

	public String importMediaSite(List<MediaSiteImportDto> mediaSiteImportDtoList, Principal principal) throws ParseException {
		
		User user = userRepository.findByUserName(principal.getName()).get();
		
		String[] singleList = {"Status","Orientation","Capture Frequency","Illumination","City Tier","Media Class","Structure Type","Media Type",
				"Place Type","Material","Placement Type","Catchment Strata","Location Type","Traffic Type","Traffic Density","Viewing Distance",
				"Quality"};
		String[] rangeList = {"Age Group"};
		
		List<ConstraintSingle> constraintSingleList = (List<ConstraintSingle>) constraintSingleRepository.findByType(singleList);
		
		Map<String, ConstraintSingle> mapSingle = new HashMap<String, ConstraintSingle>();
		
		for(ConstraintSingle constraintSingle: constraintSingleList) {
			String key = constraintSingle.getType()+"-"+constraintSingle.getValue();
			
			mapSingle.put(key, constraintSingle);
		}
		
		List<ConstraintRange> constraintRangeList = (List<ConstraintRange>) constraintRangeRepository.findByType(rangeList);
		
		Map<String, ConstraintRange> mapRange = new HashMap<String, ConstraintRange>();
		
		for(ConstraintRange constraintRange: constraintRangeList) {
			String key = constraintRange.getType()+"-"+constraintRange.getValue();
			
			mapRange.put(key, constraintRange);
		}
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		List<MediaSite> mediaSiteList = new ArrayList<MediaSite>();
		for(MediaSiteImportDto mediaSiteImportDto : mediaSiteImportDtoList) {
			MediaSite mediaSite = new MediaSite();
			
			//start string
			mediaSite.setAssetCode(mediaSiteImportDto.getAssetCode());
			mediaSite.setVendorAssetCode(mediaSiteImportDto.getVendorAssetCode());
			mediaSite.setLength(mediaSiteImportDto.getLength());
			mediaSite.setWidth(mediaSiteImportDto.getWidth());
			mediaSite.setHeight(mediaSiteImportDto.getHeight());
			mediaSite.setToMail(mediaSiteImportDto.getToMail());
			mediaSite.setLatitude(mediaSiteImportDto.getLatitude());
			mediaSite.setLongitude(mediaSiteImportDto.getLongitude());
			mediaSite.setRoad(mediaSiteImportDto.getRoad());
			mediaSite.setLocality(mediaSiteImportDto.getLocality());
			mediaSite.setCity(mediaSiteImportDto.getCity());
			mediaSite.setDistrict(mediaSiteImportDto.getDistrict());
			mediaSite.setState(mediaSiteImportDto.getState());
			mediaSite.setPincode(mediaSiteImportDto.getPincode());
			mediaSite.setViewingTime(mediaSiteImportDto.getViewingTime());
			//
			
			//start organization
			mediaSite.setOwnedByOrgId(organizationRepository.findByName(mediaSiteImportDto.getOwnedByOrgId()));
			mediaSite.setManagedByOrgId(organizationRepository.findByName(mediaSiteImportDto.getManagedByOrgId()));
			mediaSite.setCreateByOrganization(user.getOrganization());
			//
			
			//start consig
			mediaSite.setStatus(mapSingle.get("State-"+mediaSiteImportDto.getState()));
			mediaSite.setOrientation(mapSingle.get("Orientation-"+mediaSiteImportDto.getOrientation()));
			mediaSite.setCaptureFrequency(mapSingle.get("Capture Frequency-"+mediaSiteImportDto.getCaptureFrequency()));
			mediaSite.setIllumination(mapSingle.get("Illumination-"+mediaSiteImportDto.getIllumination()));
			mediaSite.setCityTier(mapSingle.get("City Tier-"+mediaSiteImportDto.getCityTier()));
			mediaSite.setMediaClass(mapSingle.get("Media Class-"+mediaSiteImportDto.getMediaClass()));
			mediaSite.setStructureType(mapSingle.get("Structure Type-"+mediaSiteImportDto.getStructureType()));
			mediaSite.setMediaType(mapSingle.get("Media Type-"+mediaSiteImportDto.getMediaType()));
			mediaSite.setMaterial(mapSingle.get("Material-"+mediaSiteImportDto.getMaterial()));
			mediaSite.setPlacementType(mapSingle.get("Placement Type-"+mediaSiteImportDto.getPlacementType()));
			mediaSite.setCatchmentStrata(mapSingle.get("Catchment Strata-"+mediaSiteImportDto.getCatchmentStrata()));
			mediaSite.setLocationType(mapSingle.get("Location Type-"+mediaSiteImportDto.getLocationType()));
			mediaSite.setTrafficType(mapSingle.get("Traffic Type-"+mediaSiteImportDto.getTrafficType()));
			mediaSite.setTrafficDensity(mapSingle.get("Traffic Density-"+mediaSiteImportDto.getTrafficDensity()));
			mediaSite.setTrafficSignal("true".equalsIgnoreCase(mediaSiteImportDto.getTrafficSignal()));
			mediaSite.setViewingDistance(mapSingle.get("Viewing Distance-"+mediaSiteImportDto.getViewingDistance()));
			mediaSite.setQuality(mapSingle.get("Quality-"+mediaSiteImportDto.getQuality()));
			//
			
			//start conrang
			mediaSite.setAgeGroup(mapRange.get("Age Group-"+mediaSiteImportDto.getAgeGroup()));
			//
			
			//Date
			if(null != mediaSiteImportDto.getManagedByStartDate() && !mediaSiteImportDto.getManagedByStartDate().isEmpty()) {
				Date manageStartDate = format.parse(mediaSiteImportDto.getManagedByStartDate());
				mediaSite.setManagedByStartDate(manageStartDate);
			}
				
			if(null != mediaSiteImportDto.getManagedByEndDate() && !mediaSiteImportDto.getManagedByEndDate().isEmpty()) {
				Date manageEndDate = format.parse(mediaSiteImportDto.getManagedByEndDate());
				mediaSite.setManagedByEndDate(manageEndDate);
			}
			
			//
			mediaSite.setDeleted(false);
			MediaSite mediaSiteObj = mediaSiteRepository.findByAssetCodeAndIsDeletedFalse(mediaSite.getAssetCode());
			if(null != mediaSiteObj)
				mediaSite.setId(mediaSiteObj.getId());
			
			mediaSiteList.add(mediaSite);
		}
		
		mediaSiteRepository.saveAll(mediaSiteList);
		return "Import Success!!";
	}
}
