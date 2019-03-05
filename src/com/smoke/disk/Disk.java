package com.smoke.disk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.smoke.filesystem.FileSystem;
import com.smoke.filesystem.INodeBlock;
import com.smoke.filesystem.SuperBlock;

public class Disk {
	/*
	 *	文件每一行为1个block
	 */
//	磁盘文件名
	private static final String DISK_NAME = "DISK";
//	磁盘中包含的总BLOCK数量
	private static final int BLOCKS_NUM = 1024;
//	BLOCK大小，即每个BLOCK包含的字符数
	private static final int BLOCK_SIZE = 64;


	public int getBlocksNum() {
		return BLOCKS_NUM;
	}

	public Integer getBlockSize() {
		return BLOCK_SIZE;
	}
	
	
	private File disk;
	private FileReader fr;
	private FileWriter fw;
	private BufferedReader br;
	private BufferedWriter bw;
	
	private SuperBlock superBlock;
	
	public Disk() {
		/*
		 *	构造函数
		 *	启动磁盘文件
		 *	若没有磁盘文件，则创建并初始化一个磁盘文件
		 */
		disk = new File(DISK_NAME);
	}
	
	public int loadDisk(SuperBlock sb) throws IOException {
		/*
		 * 	返回0 DISK文件已存在且加载成功
		 * 	返回1 DISK文件不存在，创建并初始化一个DISK文件
		 */
		Boolean flag = disk.exists();
		superBlock = sb;
		if (flag) {
			return 0;
		}else {
			disk.createNewFile();
			formatDisk();
			return 1;
		}
		
	}
	
	public void formatDisk() throws IOException {
		/*
		 *	使用字符串格式化磁盘
		 */
		setOut(); 
		for(int i=0; i<BLOCKS_NUM-1; i++) {
			bw.write(FileSystem.EMPTY_BLOCK);
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
			if(i == blockNo) {
				blockLine = br.readLine();
				break;
			}
		}
		closeIn();
		return blockLine.strip();
	}
	
//	Write
	public void write(SuperBlock superBlock) throws IOException {
		/*
		 *	向磁盘内写入SuperBlock
		 */
		setIn();
		setOut();
		
		String disk = "";
		for(int i = 0; i < BLOCKS_NUM; i++) {
			disk = br.readLine();
			bw.newLine();
			bw.write(disk);
		}
		
		
		bw.write(disk);
//		bw.flush();

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
		if (blockNo < superBlock.getFileBegan()) {
			System.out.println("非法写入");
			return;
		}
		
		setIn();
		setOut();
		
		file = file.strip();
		
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
