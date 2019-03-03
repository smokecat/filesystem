package com.smoke.filesystem;

import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws IOException {
		/*
		 * 	加载文件系统
		 */
		FileSystem SmokeOS = new FileSystem();
		
		/*
		 * 	加载DISK文件
		 */
		int errCode = SmokeOS.loadDisk();
		switch (errCode) {
		case 0:
			System.out.println("DISK file exists!");
			System.out.println("Format the DISK file? (press Y|y): ");
			Scanner sc = new Scanner(System.in);
			String readStr = sc.next();
			if (readStr.equals("y") || readStr.equals("Y")) {
				System.out.println("Formating the DISK file now, don't close the programme now...");
				SmokeOS.initDisk();
				System.out.println("Success!");
			}
			sc.close();	
			break;
		case 1:
			System.out.println("Not found DISK file!");
			System.out.println("Create a new DISK file...");
			System.out.println("Formating success!");
			
		default:
			break;
		}
		
		
	}
	
	

}
