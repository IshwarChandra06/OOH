package com.eikona.tech.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "ooh_media_site")
public class MediaSite extends Auditable<String> implements Serializable{
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;

    @Column(name = "assetCode")
    private String assetCode;
    
    @Column(name = "vendor_asset_code")
    private String vendorAssetCode;
    
    @ManyToOne
	@JoinColumn(name = "owned_by_org_id")
    private Organization ownedByOrgId;
    
    @ManyToOne
	@JoinColumn(name = "managed_by_org_id")
    private Organization managedByOrgId;
    
    @ManyToOne
	@JoinColumn(name = "create_by_org_id")
    private Organization createByOrganization;
    
    @ManyToOne
	@JoinColumn(name = "status")
    private ConstraintSingle status;
    
    @Column(name = "length")
    private String length;
    
    @Column(name = "site_name")
    private String siteName;
    
    @Column(name = "width")
    private String width;
    
    @Column(name = "height")
    private String height;
    
    @Column(name = "toMail")
    @JsonIgnore
    private String toMail;
    
    @ManyToOne
	@JoinColumn(name = "orientation_id")
    private ConstraintSingle orientation;
    
    @ManyToOne
	@JoinColumn(name = "capture_frequency_id")
    private ConstraintSingle captureFrequency;
    
    @ManyToOne
	@JoinColumn(name = "illumination")
    private ConstraintSingle illumination;
    
    @Column(name = "latitude")
    private String latitude;
    
    @Column(name = "longitude")
    private String longitude;
    
    @Column(name = "road")
    private String road;
    
    @Column(name = "locality")
    private String locality;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "district")
    private String district;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "pin_code")
    private String pincode;
    
    @Column(name = "managed_by_start_date")
    private Date managedByStartDate;
    
    @Column(name = "managed_by_end_date")
    private Date managedByEndDate;
    
    @ManyToOne
	@JoinColumn(name = "city_tier_id")
    private ConstraintSingle cityTier;
    
    @ManyToOne
	@JoinColumn(name = "media_class_id")
    private ConstraintSingle mediaClass;
    
    @ManyToOne
	@JoinColumn(name = "structure_type_id")
    private ConstraintSingle structureType;
    
    @ManyToOne
	@JoinColumn(name = "media_type_id")
    private ConstraintSingle mediaType;
    
    @ManyToOne
	@JoinColumn(name = "place_type_id")
    private ConstraintSingle placeType;
    
    @ManyToOne
	@JoinColumn(name = "material_id")
    private ConstraintSingle material;
    
    @ManyToOne
	@JoinColumn(name = "placement_type_id")
    private ConstraintSingle placementType;
    
    @ManyToOne
	@JoinColumn(name = "catchment_strata_id")
    private ConstraintSingle catchmentStrata;
    
    @ManyToOne
	@JoinColumn(name = "location_type_id")
    private ConstraintSingle locationType;
    
    @ManyToOne
	@JoinColumn(name = "traffic_type_id")
    private ConstraintSingle trafficType;
    
    @ManyToOne
	@JoinColumn(name = "traffic_density_id")
    private ConstraintSingle trafficDensity;
    
	@Column(name = "traffic_signal")
    private boolean trafficSignal;
    
    @ManyToOne
	@JoinColumn(name = "age_group_id")
    private ConstraintRange ageGroup;
    
    @Column(name = "viewing_distance")
    private ConstraintSingle viewingDistance;
    
    @Column(name = "viewing_time")
    private String viewingTime;

    @ManyToOne
	@JoinColumn(name = "quality_id")
    private ConstraintSingle quality;
    
    @ManyToOne
   	@JoinColumn(name = "camera_id")
    private Camera camera;
    
    @Column(name = "id_deleted")
    private boolean isDeleted;

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

	public ConstraintSingle getQuality() {
		return quality;
	}

	public void setQuality(ConstraintSingle quality) {
		this.quality = quality;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public ConstraintSingle getCityTier() {
		return cityTier;
	}

	public void setCityTier(ConstraintSingle cityTier) {
		this.cityTier = cityTier;
	}

	public Date getManagedByStartDate() {
		return managedByStartDate;
	}

	public void setManagedByStartDate(Date managedByStartDate) {
		this.managedByStartDate = managedByStartDate;
	}

	public Date getManagedByEndDate() {
		return managedByEndDate;
	}

	public void setManagedByEndDate(Date managedByEndDate) {
		this.managedByEndDate = managedByEndDate;
	}

	public Organization getManagedByOrgId() {
		return managedByOrgId;
	}

	public void setManagedByOrgId(Organization managedByOrgId) {
		this.managedByOrgId = managedByOrgId;
	}

	public String getToMail() {
		return toMail;
	}

	public void setToMail(String toMail) {
		this.toMail = toMail;
	}

	public Organization getCreateByOrganization() {
		return createByOrganization;
	}

	public void setCreateByOrganization(Organization createByOrganization) {
		this.createByOrganization = createByOrganization;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	

}
