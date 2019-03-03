package com.smoke.filesystem;

import com.smoke.disk.Disk;

public class SuperBlock {
	/*
	 * 单例模式
	 */
	public static int blocksNum = Disk.getBlocksNum();	//block总数
	public static int iNodeNum = 200;					//inode总数
	public static int iNodeUsed = 0;					//inode已使用数
	public static int fileBlocksUsed = 0;				//数据块block已使用数
	public static int fileBegan = 51;					//数据块block开始位置
	public static int freeFile = 51;					//空闲数据块block位置
	
	private static SuperBlock superBlock = new SuperBlock(); 
	
	private SuperBlock(){}
	
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
	
	
}