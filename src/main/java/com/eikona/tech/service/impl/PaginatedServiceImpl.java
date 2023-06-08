package com.eikona.tech.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.eikona.tech.domain.Campaign;
import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.Organization;
import com.eikona.tech.domain.User;

@Component
public class PaginatedServiceImpl<T> {
	@SuppressWarnings({ "unchecked" })
	public Specification<T> fieldSpecification(Map<String, String> searchMap) {

		Specification<T> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Set<String> searchSet = searchMap.keySet();

		for (String searchKey : searchSet) {
			Object obj = searchMap.get(searchKey);
			if (searchMap.get(searchKey) instanceof String) {
				if ("id".equalsIgnoreCase(searchKey)) {
					String idStr = searchMap.get(searchKey);
					Specification<T> idSpec = (root, query, cb) -> {
						if (idStr == null || idStr.isEmpty()) {
							return cb.conjunction();
						}
						long id = Long.parseLong(idStr);
						return cb.equal(root.get("id"), id);
					};
					isDeleted = isDeleted.and(idSpec);
				} else if ("roles".equalsIgnoreCase(searchKey)) {
					String roles = searchMap.get(searchKey);
					String[] rolesArray = roles.split(",");
					
					Specification<T> roleSpec = (root, query, cb) -> {
						query.distinct(true);
						if (roles == null || roles.isEmpty()) {
							return cb.equal(root.get("isDeleted"), false);
						}
						return root.join(searchKey).get("name").in(rolesArray);
					};
					isDeleted = isDeleted.and(roleSpec);
					
				}else if (searchKey.toLowerCase().contains("date")) {
					isDeleted = isDeleted.and((Specification<T>) genericDateSpecification(searchKey, searchMap.get(searchKey)));
				}else 
					isDeleted = isDeleted.and((Specification<T>) genericSpecification(searchKey, searchMap.get(searchKey)));
			} else if (obj instanceof ArrayList) {
				List<String> list = (List<String>) obj;

				if (!list.isEmpty()) {
					Specification<T> listSpce = (root, query, cb) -> {
						query.distinct(true);
						return cb.equal(root.get("id"), 0l);
					};
					for (String string : list) {
						
						if("id".equalsIgnoreCase(searchKey)) {
							long id = Long.parseLong(string);
							listSpce = listSpce.or((Specification<T>)orgIdSpecification(id));
						}
						else
							listSpce = listSpce.or((Specification<T>) genericSpecification(searchKey, string));
					}
					isDeleted = isDeleted.and(listSpce);
				}
			} else {
				Object mapObj = searchMap.get(searchKey);
				for (String name : ((Map<String, Object>) mapObj).keySet()) {
					Object searchData = ((Map<String, Object>) mapObj).get(name);
					List<Long> id = (List<Long>) searchData;
					isDeleted = isDeleted.and((Specification<T>) genericSpecification(searchKey, name, id));
				}
			}
		}
		return Specification.where(isDeleted);
	}
	
	public Specification<T> arraySpecification(String searchKey, long id) {
		return (root, query, cb) -> {
			if (id == 0l) {
				return cb.conjunction();
			}
			return cb.equal(root.join("roles").get("id"), id);
		};
	}

	private Specification<T> genericDateSpecification(String searchField, String searchValue) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null ;
		try {
			date = format.parse(searchValue);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(searchField.toLowerCase().contains("startdate")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			return (root, query, cb) -> {
				if (searchField == null || searchValue.isEmpty()) {
					return cb.conjunction();
				}
				return cb.greaterThan(root.get(searchField), calendar.getTime());
			};
		}else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			return (root, query, cb) -> {
				if (searchField == null || searchValue.isEmpty()) {
					return cb.conjunction();
				}
				return cb.lessThan(root.get(searchField), calendar.getTime());
			};
		}
	}

	private Specification<T> genericSpecification(String searchField, String searchValue) {
		return (root, query, cb) -> {
			if (searchField == null || searchValue.isEmpty()) {
				return cb.conjunction();
			}
			return cb.like(cb.lower(root.<String>get(searchField)), "%" + searchValue + "%");
		};
	}

	private Specification<T> genericSpecification(String searchObj, String searchField, List<Long> searchValue) {
		return (root, query, cb) -> {
			if (searchObj == null || searchField == null || searchValue.isEmpty()) {
				return cb.conjunction();
			}
			return root.get(searchObj).get(searchField).in(searchValue);
		};
	}

	@SuppressWarnings("unused")
	private Specification<T> genericSpecification(String searchKey, Object searchValue) {
		return (root, query, cb) -> {
			if (searchKey == null || searchValue == null) {
				return cb.conjunction();
			}
			return root.get(searchKey).in(searchValue);
		};
	}

	public Specification<MediaSite> managedByIdSpecification(Long orgId) {
		return (root, query, cb) -> {
			if (orgId == 0) {
				return cb.conjunction();
			}
			return cb.equal(root.<String>get("managedByOrgId").get("id"), orgId);
		};
	}
	
	public Specification<MediaSite> CreateBySpecification(Long orgId) {
		return (root, query, cb) -> {
			if (orgId == 0) {
				return cb.conjunction();
			}
			return cb.equal(root.<String>get("createByOrganization").get("id"), orgId);
		};
	}

	public Specification<Campaign> campaignOrganizationSpecification(Long orgId) {
		return (root, query, cb) -> {
			if (orgId == 0) {
				return cb.conjunction();
			}
			return cb.equal(root.get("organization").get("id"), orgId);
		};
	}
	
	public Specification<Campaign> campaignCreatedSpecification(Long orgId) {
		return (root, query, cb) -> {
			if (orgId == 0) {
				return cb.conjunction();
			}
			return cb.equal(root.get("createByOrganization").get("id"), orgId);
		};
	}
	
	public Specification<Campaign> campaignAgencySpecification(Long orgId) {
		return (root, query, cb) -> {
			if (orgId == 0) {
				return cb.conjunction();
			}
			return cb.equal(root.get("agency").get("id"), orgId);
		};
	}
	public Specification<Campaign> campaignBrandSpecification(Long orgId) {
		return (root, query, cb) -> {
			if (orgId == 0) {
				return cb.conjunction();
			}
			return cb.equal(root.<String>get("brand").get("id"), orgId);
		};
	}

	public Specification<User> usercreatedBySpecification(Long orgId) {
		return (root, query, cb) -> {
			if (orgId == 0) {
				return cb.conjunction();
			}
			return cb.equal(root.<String>get("createByOrganization").get("id"), orgId);
		};
	}
	
	public Specification<User> userOrganizationSpecification(Long orgId) {
		return (root, query, cb) -> {
			if (orgId == 0) {
				return cb.conjunction();
			}
			return cb.equal(root.<String>get("organization").get("id"), orgId);
		};
	}

	public Specification<Organization> createdByorganizationSpecification(Long orgId) {
		return (root, query, cb) -> {
			if (orgId == 0) {
				return cb.conjunction();
			}
			return cb.equal(root.<String>get("createByOrganization"), orgId);
		};
	}

	public Specification<Organization> orgIdSpecification(Long orgId) {
		return (root, query, cb) -> {
			if (orgId == 0) {
				return cb.conjunction();
			}
			return cb.equal(root.<Long>get("id"), orgId);
		};
	}

	
	public Specification<MediaSite> fieldSpecificationSingle(Map<String, String> mapSingle) {
		Specification<MediaSite> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};
		Set<String> searchSet = mapSingle.keySet();
		for (String searchKey : searchSet) {
			String searchValue = mapSingle.get(searchKey);
			Specification<MediaSite> searchSpec = (root, query, cb) -> {
				if (searchKey == null || searchValue.isEmpty()) {
					return cb.conjunction();
				}
				return cb.like(root.get(searchKey).get("value"), "%"+searchValue+"%");
			};
			isDeleted = isDeleted.and(searchSpec);
		}
		return isDeleted;
	}

	public Specification<MediaSite> fieldSpecificationRange(Map<String, String> mapRange) {
		Specification<MediaSite> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};
		Set<String> searchSet = mapRange.keySet();
		for (String searchKey : searchSet) {
			String searchValue = mapRange.get(searchKey);
			Specification<MediaSite> searchSpec = (root, query, cb) -> {
				if (searchKey == null || searchValue.isEmpty()) {
					return cb.conjunction();
				}
				return cb.like(root.get(searchKey).get("value"), "%"+searchValue+"%");
			};
			isDeleted = isDeleted.and(searchSpec);
		}
		return isDeleted;
	}

	public Specification<T> fieldSpecificationOrg(Map<String, String> mapOrg) {
		Specification<T> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};
		Set<String> searchSet = mapOrg.keySet();
		for (String searchKey : searchSet) {
			String searchValue = mapOrg.get(searchKey);
			Specification<T> searchSpec = (root, query, cb) -> {
				if (searchKey == null || searchValue.isEmpty()) {
					return cb.conjunction();
				}
				return cb.like(root.get(searchKey).get("name"),  "%"+searchValue+"%");
			};
			isDeleted = isDeleted.and(searchSpec);
		}
		return isDeleted;
	}
}
