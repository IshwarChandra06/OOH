package com.eikona.tech.dto;


import java.util.Date;

import com.eikona.tech.domain.ConstraintRange;
import com.eikona.tech.domain.ConstraintSingle;
import com.eikona.tech.domain.Organization;

public class MediaSiteDto {
	
	private Long id;

    private String assetCode;
    
    private String siteName;
    
    private String vendorAssetCode;
    
    private Organization ownedByOrgId;
    
    private ConstraintSingle status;
    
    private String length;
    
    private String width;
    
    private String height;
    
    private ConstraintSingle orientation;
    
    private ConstraintSingle captureFrequency;
    
    private ConstraintSingle illumination;
    
    private String latitude;
    
    private String longitude;
    
    private String road;
    
    private String locality;
    
    private String city;
    
    private String district;
    
    private String state;
    
    private String pincode;
    
    private ConstraintSingle cityTier;
    
    private ConstraintSingle mediaClass;
    
    private ConstraintSingle structureType;
    
    private ConstraintSingle mediaType;
    
    private ConstraintSingle placeType;
    
    private ConstraintSingle material;
    
    private ConstraintSingle placementType;
    
    private ConstraintSingle catchmentStrata;
    
    private ConstraintSingle locationType;
    
    private ConstraintSingle trafficType;
    
    private ConstraintSingle trafficDensity;
    
    private boolean trafficSignal;
    
    private ConstraintRange ageGroup;
    
    private ConstraintSingle viewingDistance;
    
    private String viewingTime;

    private ConstraintSingle quality;
    
    private Date managedByStartDate;
    
    private Date managedByEndDate;
    
    private Organization managedByOrgId;

	public Date getManagedByStartDate() {
		return managedByStartDate;
	}

	public void setManagedByStartDate(Date managedByStartDate) {
		this.managedByStartDate = managedByStartDate;
	}

	public Organization getManagedByOrgId() {
		return managedByOrgId;
	}

	public void setManagedByOrgId(Organization managedByOrgId) {
		this.managedByOrgId = managedByOrgId;
	}

	public Date getManagedByEndDate() {
		return managedByEndDate;
	}

	public void setManagedByEndDate(Date managedByEndDate) {
		this.managedByEndDate = managedByEndDate;
	}

	public ConstraintSingle getQuality() {
		return quality;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getVendorAssetCode() {
		return vendorAssetCode;
	}

	public void setVendorAssetCode(String vendorAssetCode) {
		this.vendorAssetCode = vendorAssetCode;
	}

	public Organization getOwnedByOrgId() {
		return ownedByOrgId;
	}

	public ConstraintSingle getCityTier() {
		return cityTier;
	}

	public void setCityTier(ConstraintSingle cityTier) {
		this.cityTier = cityTier;
	}

	public void setQuality(ConstraintSingle quality) {
		this.quality = quality;
	}

	public void setOwnedByOrgId(Organization ownedByOrgId) {
		this.ownedByOrgId = ownedByOrgId;
	}

	public ConstraintSingle getStatus() {
		return status;
	}

	public void setStatus(ConstraintSingle status) {
		this.status = status;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public ConstraintSingle getOrientation() {
		return orientation;
	}

	public void setOrientation(ConstraintSingle orientation) {
		this.orientation = orientation;
	}

	public ConstraintSingle getCaptureFrequency() {
		return captureFrequency;
	}

	public void setCaptureFrequency(ConstraintSingle captureFrequency) {
		this.captureFrequency = captureFrequency;
	}

	public ConstraintSingle getIllumination() {
		return illumination;
	}

	public void setIllumination(ConstraintSingle illumination) {
		this.illumination = illumination;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public ConstraintSingle getMediaClass() {
		return mediaClass;
	}

	public void setMediaClass(ConstraintSingle mediaClass) {
		this.mediaClass = mediaClass;
	}

	public ConstraintSingle getStructureType() {
		return structureType;
	}

	public void setStructureType(ConstraintSingle structureType) {
		this.structureType = structureType;
	}

	public ConstraintSingle getMediaType() {
		return mediaType;
	}

	public void setMediaType(ConstraintSingle mediaType) {
		this.mediaType = mediaType;
	}

	public ConstraintSingle getPlaceType() {
		return placeType;
	}

	public void setPlaceType(ConstraintSingle placeType) {
		this.placeType = placeType;
	}

	public ConstraintSingle getMaterial() {
		return material;
	}

	public void setMaterial(ConstraintSingle material) {
		this.material = material;
	}

	public ConstraintSingle getPlacementType() {
		return placementType;
	}

	public void setPlacementType(ConstraintSingle placementType) {
		this.placementType = placementType;
	}

	public ConstraintSingle getCatchmentStrata() {
		return catchmentStrata;
	}

	public void setCatchmentStrata(ConstraintSingle catchmentStrata) {
		this.catchmentStrata = catchmentStrata;
	}

	public ConstraintSingle getLocationType() {
		return locationType;
	}

	public void setLocationType(ConstraintSingle locationType) {
		this.locationType = locationType;
	}

	public ConstraintSingle getTrafficType() {
		return trafficType;
	}

	public void setTrafficType(ConstraintSingle trafficType) {
		this.trafficType = trafficType;
	}

	public ConstraintSingle getTrafficDensity() {
		return trafficDensity;
	}

	public void setTrafficDensity(ConstraintSingle trafficDensity) {
		this.trafficDensity = trafficDensity;
	}
	
	public boolean isTrafficSignal() {
		return trafficSignal;
	}

	public void setTrafficSignal(boolean trafficSignal) {
		this.trafficSignal = trafficSignal;
	}

	public ConstraintRange getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(ConstraintRange ageGroup) {
		this.ageGroup = ageGroup;
	}

	public ConstraintSingle getViewingDistance() {
		return viewingDistance;
	}

	public void setViewingDistance(ConstraintSingle viewingDistance) {
		this.viewingDistance = viewingDistance;
	}

	public String getViewingTime() {
		return viewingTime;
	}

	public void setViewingTime(String viewingTime) {
		this.viewingTime = viewingTime;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
    
}
