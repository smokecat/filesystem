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
		
		iNodeBlocks = new INodeBlock[50];
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
		
//		初始化inodeblocks
		initINodeBlocks();
		
//		写入根目录
		INode rootDir = iNodeBlocks[0].getiNodes()[0];
		rootDir.setPos(new int[] {1,0});
		rootDir.setId();
		rootDir.setFlag(1);
		rootDir.setOwner(0);
		write(rootDir);
		writeFile(rootDir, new String("1 ."));
		
//		添加配置文件夹
		mkdir(rootDir, 0, "etc");
//			添加用户配置文件
		createFile(getINode(getFileId(rootDir, "etc")), 0, "shadow", "0:root:123456 1:smoke:smoke");
		
//		添加用户家目录
		mkdir(rootDir,  0, "home");
//			添加root和smoke家目录
		mkdir(getINode(getFileId(rootDir, "home")), 0, "root");
		mkdir(getINode(getFileId(rootDir, "home")), 0, "smoke");
		
		
		System.out.println("success: Initialize the SmokeOS!");
	}
	
//	初始化inodeblocks
	public void initINodeBlocks() {
		for(int i=0; i<iNodeBlocks.length; i++) {
			iNodeBlocks[i] = new INodeBlock(EMPTY_BLOCK, i);
		}
	}
	
//	创建目录
	public void mkdir(INode parent, int owner, String dirName) throws IOException {
		/*
		 * 	创建目录
		 */
		INode dir = getFreeINode();
		dir.setId();
		dir.setFlag(1);
		dir.setOwner(owner);
		dir.setParent(parent);
		String file = new String(dir.getId() + " . " + parent.getId() + " .. ");	
		addChild(parent, dir, dirName, file);
	}
	
//	创建文件
	public void createFile(INode parent, int owner, String fileName, String file) throws IOException {
		INode fileInode = getFreeINode();
		fileInode.setId();
		fileInode.setFlag(2);
		fileInode.setOwner(owner);
		fileInode.setParent(parent);
		addChild(parent, fileInode, fileName, file);
	}
	
//	向目录中添加子文件
	public void addChild(INode parent, INode child, String childName, String file) throws IOException {
		/*
		 * 	增加父目录中的节点
		 * 	更新父目录
		 * 	添加子文件
		 */
		int[] dirPtr = parent.getPtr();
		String parFile = readFile(parent);
		parFile = (parFile + " " + child.getId() + " " + childName);
		updateFile(parent, parFile);
		writeFile(child, file);
	}
	
//	写入superblock
	public void writeSuperBlock() throws IOException {
		diskObj.write(superBlock);
	}
	
//	写入inode
	public void write(INode inode) throws IOException {
		int[] pos = inode.getPos();
		iNodeBlocks[pos[0]-1].setiNode(inode, pos[1]);
		diskObj.write(pos[0], iNodeBlocks[pos[0]-1]);
	}
	

	
//	向磁盘写文件
	public void writeFile(INode inode,String file) throws IOException {
		/*
		 * 	向磁盘中写入文件
		 */
		int freeNo;
		
		while(file.length()>0) {
			freeNo = superBlock.getFreeFile();
			if(file.length()<=diskObj.getBlockSize()) {
				diskObj.write(freeNo, file);
				inode.setPtrNo(freeNo);
				break;
			}
			diskObj.write(freeNo, file.substring(0, diskObj.getBlockSize()));
			file = file.substring(diskObj.getBlockSize());

		}
//			写入inode
			write(inode);
//			向空闲block写入数据后更新block
			setNextFreeList();
	}
	
//	写文件到指定block
	public void writeFile(INode inode, String file, int[]ptr) throws IOException {
		/*
		 * 	写文件
		 */
		int freeNo;
		int[] newPtr = new int[ptr.length];
		for(int i=0; i<ptr.length && file.length()>0; i++) {
			if(file.length() <= diskObj.getBlockSize()) {
				freeNo = ptr[i];
				newPtr[i] = ptr[i];
				diskObj.write(freeNo, file);
				break;
			}else {
				freeNo = ptr[i];
				newPtr[i] = ptr[i];
				diskObj.write(freeNo, file.substring(0, diskObj.getBlockSize()));
				file = file.substring(diskObj.getBlockSize());
			}
		inode.setPtr(newPtr);
		}
	}
	
//	读取文件
	public String readFile(INode inode) throws IOException {
		/*
		 * 	读取inode的文件
		 */
		String file = "";
		int[] filePtr = inode.getPtr();
		for(int i=0; i<filePtr.length; i++) {
			if(filePtr[i]==0)
				break;
			String line = diskObj.Read(filePtr[i]);
			file += line;
		}
		return file;
	}
	
//	更新文件
	public void updateFile(INode inode,String file) throws IOException {
		/*
		 * 	更新已存在的文件
		 */
//		String fileStr = readFile(inode);
		int[] filePtr = inode.getPtr();
		deleteData(filePtr);
		writeFile(inode, file, filePtr);
		write(inode);
	}
	
//	删除文件
	public void deleteFile(INode inode) throws IOException {
		/*
		 * 删除文件
		 */
		if(inode.getFlag()!=2) {
			System.out.println("error: isn't a file.");
			return;
		}
		int[] filePtr = inode.getPtr();
		int[] pos = inode.getPos();
		iNodeBlocks[pos[0]-1].setiNode(new INode(), pos[1]);
		
		deleteData(filePtr);
	}
	
//	清楚指定区域的数据块
	public void deleteData(int[] ptr) throws IOException {
		/*
		 * 	清除数据块block
		 */
		for(int i=0; i<ptr.length; i++) {
			if(ptr[i]==0) {
				break;
			}
			diskObj.write(ptr[i], EMPTY_BLOCK);
		}
	}
	
//	获取空闲inode
	public INode getFreeINode() {
		for(int i=0; i<50; i++) {
			for(int j=0; j<INodeBlock.getInodesPerBlock();j++){
				if(iNodeBlocks[i].getiNodes()[j].getFlag() == 0) {
					INode inode = iNodeBlocks[i].getiNodes()[j];
					inode.setPos(new int[] {i+1, j});
					return inode;
				}
			}
		}
		return null;
	}
	
//	
	public String getChildName(INode inode, int cId) throws IOException {
		/*
		 * 	如果是目录inode，则根据cId获取文件名
		 */
		int[] dirPtr = inode.getPtr();
		String file = new String("");
		for(int i=0; i<dirPtr.length; i++) {
			if(dirPtr[i]!=0)
				file += diskObj.Read(dirPtr[i]);
		}
		String[] ids = file.split(" ");
		for(int i=0; i<ids.length; i+=2) {
			if(ids[i] == String.valueOf(cId)) {
				return ids[i+1];
			}
		}
		return null;
	}
	
//	获取子文件的id
	public int getFileId(INode parent, String name) throws IOException {
		/*
		 * 
		 */
		String file = readFile(parent);
		
//		行
		String[] ids = file.split(" ");
		for(int i=0; i<ids.length; i+=2) {
			if(ids[i+1].equals(name)) {
				return Integer.valueOf(ids[i]);
			}
		}
		return -1;
	}
	
//	查找指定id的INode
	public INode getINode(int id) {
		for(int i=0; i<iNodeBlocks.length; i++) {
			if(iNodeBlocks[i].searchNode(id)[0] == 1)
				return iNodeBlocks[i].getiNodes()[iNodeBlocks[i].searchNode(id)[1]];
		}
		return  null;
	}
	
//	更新superblock的freeList
	public void setNextFreeList() throws IOException {
		superBlock.setFreeFile(getNextFreeBlock());
		diskObj.write(superBlock);
	}
	
//	获取下一个空闲block
	public int getNextFreeBlock() throws IOException {
		int freeBlock = superBlock.getFreeFile();
		for(int i=freeBlock; i!=freeBlock-1; i++) {
			if(diskObj.Read(i).equals(EMPTY_BLOCK)) {
				return i;
			}
			if(i==diskObj.getBlocksNum())
				i = superBlock.getFileBegan();
		}
		return freeBlock;
	}
}