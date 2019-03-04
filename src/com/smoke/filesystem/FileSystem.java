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
	private SuperBlock superBlock;
	private INodeBlock[] iNodeBlocks;
	
	public FileSystem() {
		/*
		 * 	构造函数
		 */
		superBlock = SuperBlock.getSuperBlock();
		
		diskObj = new Disk();
		
		iNodeBlocks = new INodeBlock[diskObj.getBlocksNum()];
	}
	
	public int loadDisk() throws IOException {
		/*
		 * 	加载磁盘文件
		 */
		int errCode = diskObj.loadDisk(superBlock);
		
//		若加载成功，则读取所有iNodeBlock
		if(errCode == 0) {
			superBlock.setSuperBlock(diskObj.Read(0));
			for(int i=1; i<51; i++) {
				iNodeBlocks[i].init(diskObj.Read(i), i);
			}
		}
		return errCode;
	}
	
	public void formatDisk() throws IOException {
		/*
		 * 	格式化磁盘
		 */
		diskObj.formatDisk();
//		格式化磁盘后生成文件系统，即初始化
		initializeOS();
	}
	
	public void initializeOS() throws IOException {
		/*
		 * 	初始化文件系统
		 */
		System.out.println("Initializing the file system ...");

//		写入superBlock
//		设置superBlock的属性
		int[] proterties = new int[] {diskObj.getBlocksNum(),200,0,0,51,51};
		superBlock.setSuperBlock(proterties);
		writeSuperBlock();
		
//		写入根目录
		INode rootDir = new INode(1, 1, 0, new int[] {1,0});
		write(rootDir);
		writeFile(rootDir, new String(" "));
		
	}
	
//	创建文件夹
	public void mkdir() {
		/*
		 * 	创建文件夹
		 */
	}
	
//	写入superblock
	public void writeSuperBlock() throws IOException {
		diskObj.write(superBlock);
	}
	
//	写入inode
	public void write(INode inode) throws IOException {
		int[] pos = inode.getPos();
		iNodeBlocks[pos[0]-1].setiNode(inode, pos[1]);
		diskObj.write(iNodeBlocks[pos[0]-1].getPos(), iNodeBlocks[pos[0]-1]);
	}
	
//	创建目录
	public void mkdir(String dirName) {
		
	}
	
//	向磁盘写文件
	public void writeFile(INode inode,String file) throws IOException {
		/*
		 * 	向磁盘中写入文件
		 */
		int freeNo;
		int fileLen = file.length();
		
		while(fileLen>0) {
			freeNo = superBlock.getFreeFile();
			if(file.length()<=diskObj.getBlockSize()) {
				diskObj.write(freeNo, file);
				inode.setNextPtr(freeNo);
				break;
			}
			diskObj.write(freeNo, file.substring(0, diskObj.getBlockSize()));
			file = file.substring(diskObj.getBlockSize());
		}
		
//		向空闲block写入数据后更新block
		setNextFreeList();
	}
	
//	获取含有空闲inode的行数
	public int getINodeBlockNo() {
		int no = 1;
		return no;
	}
	
//	查找指定id的INode
	
//	更新superblock的freeList
	public void setNextFreeList() throws IOException {
		superBlock.setFreeFile(getNextFreeBlock());
		diskObj.write(superBlock);
	}
	
//	获取下一个空闲block
	public int getNextFreeBlock() throws IOException {
		int freeBlock = superBlock.getFreeFile();
		for(int i=freeBlock+1; i<diskObj.getBlocksNum(); i++) {
			if(diskObj.Read(i).equals(EMPTY_BLOCK))
				return i;
		}
		return freeBlock;
	}
}