package com.eikona.tech.api.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.service.impl.EmailScheduleServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
@RestController
public class EmailScheduleController {
	
	@Autowired
	private EmailScheduleServiceImpl reportServiceImpl;
	
	@RequestMapping(value = "/sendmediareportbyassetanddate", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: report_send")
	@PreAuthorize("hasAuthority('report_send')")
	public @ResponseBody void assetDate(@RequestParam Long assetId,
			@RequestParam String datestr) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(datestr);
			
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(date);
			startCalendar.set(Calendar.HOUR, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(date);
			endCalendar.set(Calendar.HOUR, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);
			
			reportServiceImpl.findImagelog(assetId, startCalendar.getTime(), endCalendar.getTime());

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
