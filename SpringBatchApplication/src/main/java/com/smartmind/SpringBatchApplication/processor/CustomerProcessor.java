package com.smartmind.SpringBatchApplication.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import com.smartmind.SpringBatchApplication.entity.EmployeeEntity;

@Service
public class CustomerProcessor implements ItemProcessor<EmployeeEntity, EmployeeEntity> {

    @Override
    public EmployeeEntity process(EmployeeEntity customer) throws Exception {
     /*  if(customer.get().equals("United States")) {
           return customer;
        }else{
           return null;
       }*/
        return customer;
    	//return customer;
    }
}
