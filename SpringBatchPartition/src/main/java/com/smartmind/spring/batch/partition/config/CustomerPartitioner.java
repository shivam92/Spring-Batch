package com.smartmind.spring.batch.partition.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Service;

@Service
public class CustomerPartitioner implements org.springframework.batch.core.partition.support.Partitioner {

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		 int min = 1;
	        int totalrecord = 20;
	        int targetSize = (totalrecord - min) / gridSize + 1;//10
	        System.out.println("targetSize : " + targetSize);
	        Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();

	        int number = 0;
	        int start = min;
	        int end = start + targetSize - 1;
	        //1 to 10
	        // 11 to 20
	        while (start <= totalrecord) {
	            ExecutionContext value = new ExecutionContext();
	            result.put("partition" + number, value);

	            if (end >= totalrecord) {
	                end = totalrecord;
	            }
	            value.putInt("minValue", start);
	            value.putInt("maxValue", end);
	            start += targetSize;
	            end += targetSize;
	            number++;
	        }
	        System.out.println("partition result:" + result.toString());
	        return result;
	}

}
