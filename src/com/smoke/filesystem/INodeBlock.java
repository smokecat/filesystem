package com.smoke.filesystem;

public class INodeBlock {
	private static final int INODES_PER_BLOCK = 4;
	
	public INode[] iNodes = new INode[INODES_PER_BLOCK];
	
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
