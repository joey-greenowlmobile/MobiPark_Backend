package com.greenowl.callisto.service;

import com.greenowl.callisto.domain.ExceptionLog;
import com.greenowl.callisto.repository.ExceptionLogRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ExceptionLogService {

	private static final Logger LOG = LoggerFactory.getLogger(ExceptionLogService.class);
	
	@Inject
	private ExceptionLogRepository exceptionLogRepository;
	
	public void saveExceptionLog(ExceptionLog log){
		exceptionLogRepository.save(log);
	}
	
	
	
}
