package com.smartmind.spring.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import com.smartmind.spring.batch.entity.EmployeeEntity;
@Service
public class FileItemProcessor implements ItemProcessor<EmployeeEntity, EmployeeEntity> {

	@Override
	public EmployeeEntity process(EmployeeEntity item) throws Exception {
		
		return item;
	}

}
