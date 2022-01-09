package com.study.common.util;

import java.text.DecimalFormat;

public class StudyFileUtils {
	private static DecimalFormat df = new DecimalFormat("#,###.0");

	public static String fancySize(long size) {
		if(size < 1024) { // 1k 미만 
			return size + " Bytes";			
		}else if(size < (1024*1024)) { // 1M 미만 
			return df.format(size/1024.0) + " KB";
		}else if(size < (1024*1024*1024)) { // 1G 미만 
			return df.format(size/(1024.0 * 1024.0)) + " MB";
		}else {
			return df.format(size/(1024.0 * 1024.0 * 1024.0)) + " GB";
		}		
	}
	
	public static void main(String[] args) {
		System.out.println(fancySize(453)); 
		System.out.println(fancySize(40054));
		System.out.println(fancySize(45564654563L));
		System.out.println(fancySize(4522525277853L));		
	}
	
}
