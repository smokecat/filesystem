package com.smoke.disk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.smoke.filesystem.INodeBlock;
import com.smoke.filesystem.SuperBlock;

public class Disk {
	/*
	 *	第0行为superblock
	 *	第1到50行为200个inode,即50个inodeblock
	 *	第51行到最后为数据块block
	 */
//	磁盘文件名
	private static final String DISK_NAME = "DISK";
//	磁盘中包含的总BLOCK数量
	private static final int BLOCKS_NUM = 1024;
//	BLOCK大小，即每个BLOCK包含的字符数
	private static final int BLOCK_SIZE = 64;
//	INode大小
	private static final int INODE_SIZE = 16;
//	定义空block
	public static final String EMPTY_BLOCK = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0";
	


	public static int getBlocksNum() {
		return BLOCKS_NUM;
	}

	public static int getInodeSize() {
		return INODE_SIZE;
	}

	public static Integer getBlockSize() {
		return BLOCK_SIZE;
	}
	
	
	private File disk;
	private FileReader fr;
	private FileWriter fw;
	private BufferedReader br;
	private BufferedWriter bw;
	
	public Disk() throws IOException {
		/*
		 *	构造函数
		 *	启动磁盘文件
		 *	若没有磁盘文件，则创建并初始化一个磁盘文件
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
	
	public void initDisk() throws IOException {
		/*
		 *	初始化磁盘
		 */
		setOut(); 
		for(int i=0; i<BLOCKS_NUM-1; i++) {
			bw.write(EMPTY_BLOCK);
			bw.newLine();
		}
		bw.newLine();
		closeOut();
	}
	
//	Read
	public String Read(int blockNo) throws IOException {
		/*
		 * 	读取数据块
		 */
		setIn();
		String blockLine = "";
		for(int i=0; i<BLOCKS_NUM; i++) {
			br.readLine();
			if(i == blockNo) {
				blockLine = br.readLine();
				break;
			}
		}
		closeIn();
		return blockLine;
	}
	
//	Write
	public void write(SuperBlock superBlock) throws IOException {
		/*
		 *	向磁盘内写入SuperBlock
		 */
		setIn();
		setOut();
		
		String disk = "";
		bw.write(SuperBlock.getLine());
		br.readLine();
		for(int i = 1; i < BLOCKS_NUM; i++) {
			disk = br.readLine();
			bw.newLine();
			bw.write(disk);
		}

		closeIn();
		closeOut();
	}
	
	public void write(int blockNo, INodeBlock iNodeBlock) throws IOException {
		/*
		 *	 向磁盘内写入INode
		 */
		setIn();
		setOut();
		
		String disk = ""; 
		bw.write(SuperBlock.getLine());
		br.readLine();
		for(int i = 1; i < BLOCKS_NUM; i++) {
			disk = br.readLine();
			bw.newLine();
			if(i == blockNo) {
				bw.write(iNodeBlock.getLine());
			}else {
				bw.write(disk);
			}
		}

		closeIn();
		closeOut();
	}
	
	public void write(int blockNo, String file) throws IOException {
		/*
		 *	向磁盘内写入文件数据
		 */
//		判断写入位置是否为数据块
		if (blockNo < SuperBlock.fileBegan) {
			System.out.println("非法写入");
			return;
		}
		
		setIn();
		setOut();
		
		String disk = ""; 
		bw.write(SuperBlock.getLine());
		br.readLine();
		for(int i = 1; i < BLOCKS_NUM; i++) {
			disk = br.readLine();
			bw.newLine();
			if(i == blockNo) {
				bw.write(file);
			}else {
				bw.write(disk);
			}
		}
		
		closeIn();
		closeOut();
		
	}
	
//	关闭磁盘文件
	public void stop() throws IOException {
		
	}
	
//	设置disk文件IO
	public void setIn() throws IOException {
		fr = new FileReader(disk);
		br = new BufferedReader(fr);
		br.mark(Integer.MAX_VALUE);
	}
	public void setOut() throws IOException {
		fw = new FileWriter(disk);
		bw = new BufferedWriter(fw);
	}
	
//	 关闭disk文件IO
	public void closeIn() throws IOException {
		br.close();
		fr.close();
	}
	public void closeOut() throws IOException {
		bw.flush();
		bw.close();
		fw.close();		
	}
	
	
}
