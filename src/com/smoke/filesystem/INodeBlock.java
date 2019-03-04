package com.smoke.filesystem;

public class INodeBlock {
	private static final int INODES_PER_BLOCK = 4;
	
	public INode[] iNodes = new INode[INODES_PER_BLOCK];
	
	public INodeBlock(String line) {
		/*
		 * 	构造
		 */
		String[] blockStr = line.split(" ");
		if(blockStr.length!=64)
			System.out.println("error: INodeBlock's length not equal to 64!");
		int j=0;
		for(int i=0; i<INODES_PER_BLOCK; i++) {
			iNodes[i].setId(Integer.parseInt(blockStr[j++]));
			iNodes[i].setFlag(Integer.parseInt(blockStr[j++]));
			iNodes[i].setOwner(Integer.parseInt(blockStr[j++]));
			for(int k=0; k<iNodes[i].getPtr().length; k++) {
				iNodes[i].setNextPtr(Integer.parseInt(blockStr[j++]));
			}
		}
	}
	
	public int[] searchNode(int  iid) {
		/*
		 * 	根据id查找iNode
		 * 	返回包含两个int值的int数组
		 * 	第一位，0表示没有查找的INode， 1表示查找成功，第二位是INode在此INodeBlock的序号
		 */
		for(int i=0; i<INODES_PER_BLOCK; i++) {
			if(iNodes[i].getId() == iid) {
				return new int[] {1,i};
			}
		}
		
		return new int[] {0,0};
	}
	
	public void writeINodeBlock() {
		
	}
	
	
	public String getLine() {
		String iNodeBlockLine = "";
		for(int i = 0; i < INODES_PER_BLOCK; i++) {
			iNodeBlockLine += iNodes[i].toString();
		}
		
		return iNodeBlockLine;
	}
}
