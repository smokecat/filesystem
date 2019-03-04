package com.smoke.filesystem;


public class SuperBlock {
	/*
	 * 单例模式
	 */
	private static int blocksNum;				//block总数
	private static int iNodeNum;				//inode总数
	private static int iNodeUsed;				//inode已使用数
	private static int fileBlocksUsed;			//数据块block已使用数
	private static int fileBegan;				//数据块block开始位置
	private static int freeFile;				//空闲数据块block位置
	
	private static SuperBlock superBlock = new SuperBlock(); 
	
	private SuperBlock(){
	}
	
	public void setSuperBlock(int[] properties) {
		blocksNum = properties[0];
		iNodeNum = properties[1];
		iNodeUsed = properties[2];
		fileBlocksUsed = properties[3];
		fileBegan = properties[4];
		freeFile = properties[5];
	}
	
	public static SuperBlock getSuperBlock() {
		return superBlock;
	}
	
	public static String getLine() {
		/*
		 * 类似toString，方便写入disk
		 */
		String superBlockLine = "";
		for(int i = 0; i < 64; i++) {
			switch (i) {
			case 0:
				superBlockLine += (String.valueOf(blocksNum)+" ");
				break;
			case 1:
				superBlockLine += (String.valueOf(iNodeNum)+" ");
				break;
			case 2:
				superBlockLine += (String.valueOf(iNodeUsed)+" ");
				break;
			case 3:
				superBlockLine += (String.valueOf(fileBlocksUsed)+" ");
				break;
			case 4:
				superBlockLine += (String.valueOf(fileBegan)+" ");
				break;
			case 5:
				superBlockLine += (String.valueOf(freeFile)+" ");
				break;

			default:
				superBlockLine += "0 ";
				break;
			}
		}
		
		return superBlockLine;
	}

	public int getBlocksNum() {
		return blocksNum;
	}

	public void setBlocksNum(int blocksNum) {
		SuperBlock.blocksNum = blocksNum;
	}

	public int getiNodeNum() {
		return iNodeNum;
	}

	public void setiNodeNum(int iNodeNum) {
		SuperBlock.iNodeNum = iNodeNum;
	}

	public int getiNodeUsed() {
		return iNodeUsed;
	}

	public void setiNodeUsed(int iNodeUsed) {
		SuperBlock.iNodeUsed = iNodeUsed;
	}

	public int getFileBlocksUsed() {
		return fileBlocksUsed;
	}

	public void setFileBlocksUsed(int fileBlocksUsed) {
		SuperBlock.fileBlocksUsed = fileBlocksUsed;
	}

	public int getFileBegan() {
		return fileBegan;
	}

	public void setFileBegan(int fileBegan) {
		SuperBlock.fileBegan = fileBegan;
	}

	public int getFreeFile() {
		return freeFile;
	}

	public void setFreeFile(int freeFile) {
		SuperBlock.freeFile = freeFile;
	}
	
	
}