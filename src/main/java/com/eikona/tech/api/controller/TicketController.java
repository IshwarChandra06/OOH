package com.eikona.tech.api.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eikona.tech.domain.Ticket;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.TicketDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.TicketService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

	@Autowired
	TicketService ticketService;

	@GetMapping("/all")
	public List<Ticket> getAllTickets() {
		return ticketService.findAll();
	}

	@GetMapping("/id/{id}")
	public Ticket getTicket(@PathVariable long id) {
		return ticketService.find(id);
	}

	@PostMapping("/create")
	public ResponseEntity<Void> createTicket(@RequestBody Ticket ticket) {
		Ticket createdTicket = ticketService.save(ticket);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdTicket.getId())
				.toUri();

		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<Ticket> updateTicket(@PathVariable long id, @RequestBody Ticket ticket) {
		Ticket updatedTicket = ticketService.save(ticket);

		return new ResponseEntity<Ticket>(updatedTicket, HttpStatus.OK);
	}

	@DeleteMapping("/id/{id}")
	public ResponseEntity<Void> deleteTicket(@PathVariable long id) {
		ticketService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public PaginatedDto<TicketDto> findPaginated(@RequestBody SearchRequestDto paginatedDto, Model model) {

		Page<Ticket> page = ticketService.findPaginated(paginatedDto.getPageNo(), paginatedDto.getPageSize(),
				paginatedDto.getSortField(), paginatedDto.getSortOrder());

		List<Ticket> feedbackList = page.getContent();
		List<TicketDto> ticketDtoList = new ArrayList<>();
		for (Ticket ticket : feedbackList) {
			TicketDto ticketDto = new TicketDto();
			
			ticketDto.setBody(ticket.getBody());
			ticketDto.setMediaSite(ticket.getMediaSite());
			ticketDto.setStatus(ticket.getStatus());
			ticketDto.setSubject(ticket.getSubject());
			ticketDto.setUser(ticket.getUser());

			ticketDtoList.add(ticketDto);
		}

		List<Ticket> totalList = ticketService.findAll();
		Page<Ticket> totalPage = new PageImpl<Ticket>(totalList);

		PaginatedDto<TicketDto> paginatedDtoList = new PaginatedDto<TicketDto>(ticketDtoList, page.getTotalPages(),
				page.getNumber(), page.getSize(), page.getTotalElements(), totalPage.getTotalElements(), "Success", "");

		return paginatedDtoList;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody PaginatedDto<TicketDto> search(@RequestBody SearchRequestDto paginatedDto) {

		String message = "";
		String messageType = "";
		PaginatedDto<TicketDto> paginatedDtoList = null;
		try {

			Page<Ticket> page = ticketService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(),
					paginatedDto.getSortField(), paginatedDto.getSortOrder(), paginatedDto);

			List<Ticket> ticketList = page.getContent();
			List<TicketDto> ticketDtoList = new ArrayList<>();
			for (Ticket ticket : ticketList) {
				TicketDto ticketDto = new TicketDto();
				
				ticketDto.setBody(ticket.getBody());
				ticketDto.setMediaSite(ticket.getMediaSite());
				ticketDto.setStatus(ticket.getStatus());
				ticketDto.setSubject(ticket.getSubject());
				ticketDto.setUser(ticket.getUser());

				ticketDtoList.add(ticketDto);
			}

			List<Ticket> totalList = ticketService.findAll();
			Page<Ticket> totalPage = new PageImpl<Ticket>(totalList);

			message = "Success";
			messageType = "";
			paginatedDtoList = new PaginatedDto<TicketDto>(ticketDtoList, page.getTotalPages(), page.getNumber(),
					page.getSize(), page.getTotalElements(), totalPage.getTotalElements(), message, messageType);

		} catch (Exception e) {
			System.out.println(e);
			message = "Failed";
			messageType = "";
			paginatedDtoList = new PaginatedDto<TicketDto>(message, messageType);
		}
		return paginatedDtoList;
	}
}
