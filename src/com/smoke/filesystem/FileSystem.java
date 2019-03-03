package com.smoke.filesystem;

import java.io.IOException;

import com.smoke.disk.Disk;

public class FileSystem {
	/*
	 *	第0行为superblock
	 *	第1到50行为200个inode,即50个inodeblock
	 *	第51行到最后为数据块block
	 */
	
//	INode大小
	private static final int INODE_SIZE = 16;
//	定义空block
	public static final String EMPTY_BLOCK = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0";

	public static int getInodeSize() {
		return INODE_SIZE;
	}
	
	private Disk diskObj;
	
	public FileSystem() {
		diskObj = new Disk();
	}
	
	public int loadDisk() throws IOException {
		int errCode = diskObj.loadDisk();
		return errCode;
	}
	
	public void initDisk() throws IOException {
		/*
		 * 	格式化磁盘
		 */
		diskObj.initDisk();
	}
	
//	创建文件
	
}