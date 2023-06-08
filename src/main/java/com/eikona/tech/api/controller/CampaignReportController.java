package com.eikona.tech.api.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.dto.AssetReportDto;
import com.eikona.tech.service.impl.CampaignReportServiceImpl;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/api/campaign-report")
public class CampaignReportController {
	
	@Autowired
	private CampaignReportServiceImpl reportServiceImpl;

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/asset-date", method = RequestMethod.GET)
	public @ResponseBody AssetReportDto assetDate(@RequestParam Long assetId, @RequestParam String datestr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date sDate = null;
		try {
			sDate = format.parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(sDate);
		startCalendar.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(sDate);
		int daysInMonth = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		endCalendar.set(Calendar.DAY_OF_MONTH,daysInMonth);
		
		AssetReportDto reportDto = reportServiceImpl.findCampaign(assetId , startCalendar.getTime(), endCalendar.getTime());
		
		reportDto.setTotalDays(daysInMonth);
		reportDto.setEmptyDays(daysInMonth - reportDto.getOccupiedDays());
		return reportDto;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/asset-daterange", method = RequestMethod.GET)
	public @ResponseBody AssetReportDto assetDateRange(@RequestParam Long assetId, @RequestParam String startDateStr, @RequestParam String endDateStr) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = format.parse(startDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		AssetReportDto reportDto = reportServiceImpl.findCampaign(assetId , startDate, endDate);
		
		long diff = endDate.getTime()-startDate.getTime();
		int totalDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		reportDto.setEmptyDays(totalDays - reportDto.getOccupiedDays());
		reportDto.setTotalDays(totalDays);
		return reportDto;
	}
	
}
