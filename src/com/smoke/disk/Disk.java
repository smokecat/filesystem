package com.smoke.disk;

import java.io.File;
import java.io.IOException;

public class Disk {
//	磁盘文件名
	private static final String DISK_NAME = "DISK";
//	磁盘中包含的总BLOCK数量
	private static final Integer BLOCKSL_NUM = 1024;
//	BLOCK大小，即每个BLOCK包含的字符数
	private static final Integer BLOCK_SIZE = 64;

	public static String getDiskName() {
		return DISK_NAME;
	}

	public static Integer getBlocksNum() {
		return BLOCKSL_NUM;
	}

	public static Integer getBlockSize() {
		return BLOCK_SIZE;
	}

	private File disk;
	
	public Disk() throws IOException {
		/*
		 * 构造函数
		 * 启动磁盘文件
		 * 若没有磁盘文件，则创建并初始化一个磁盘文件
		 */
		disk = new File(DISK_NAME);
		if (disk.exists()) {
			System.out.println("Found disk!");
		}else {
			System.out.println("Not found disk!");
			System.out.println("Initial disk now ...");
			disk.createNewFile();
			initDisk();
		}
	}
	
	public void initDisk() {
		/*
		 * 初始化磁盘
		 */
		
	}
	
//	Read
	public void Read() {
		
	}
	
//	Write
	public void write(SuperBlock superBlock) {
		/*
		 * 向磁盘内写入SuperBlock
		 */
	}
	
	public void write(int blockNo, INode iNode) {
		/*
		 * 向磁盘内写入INode
		 */
	}
	
	public void write(int blockNo, String file) {
		/*
		 * 向磁盘内写入文件数据
		 */
		
	}
	
	
	
	
}
