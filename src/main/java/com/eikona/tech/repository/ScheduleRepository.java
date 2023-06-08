package com.eikona.tech.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.Schedule;

@Repository
public interface ScheduleRepository extends DataTablesRepository<Schedule, Long>{
	List<Schedule> findAllByIsDeletedFalse();
}
