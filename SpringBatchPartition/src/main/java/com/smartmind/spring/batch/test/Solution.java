package com.smartmind.spring.batch.test;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static int solve(List<Integer> A) {
        int even=0,odd=0;
        boolean falg=true;
        int item=A.get(0);
        if(item%2==1)falg=false;
        for(int a:A){
            if(a%2==0 && falg){
                even++;
               falg=false; 
            }else if(a%2==1 && falg==false){
             odd++;
            falg=true;
            }
        }
        System.out.println(even+"->"+odd);
        if(even==0|| odd==0)return 1;
        else return Math.min(even,odd)*2-1;
    }
	public static void main(String[] args)  {
		
		List<Integer> arr=List.of(43,46,33,7);
		solve(arr);
	}
}
