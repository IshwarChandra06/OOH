package com.eikona.tech.service;

import com.eikona.tech.domain.MediaSite;

public interface MultimediaProcessingService {
	
	/**
	 * It accept video path and mediasite.
	 * Take first frame from those video and save video and image into file system.
	 *  
	 * @param path
	 * @param mediasite
	 */
	void videoProcessing(String path, MediaSite mediasite, String dateStr);
	
	/**
	 * It accept image path and mediasite.
	 * Take first frame from those video and save video and image into file system.
	 *  
	 * @param path
	 * @param mediasite
	 */
	
	void imageProcessing(String path, MediaSite mediasite,String dateStr);
}
