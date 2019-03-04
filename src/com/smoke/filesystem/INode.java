package com.smoke.filesystem;


public class INode {
//	文件类型，0表示此INode未使用，1表示为文件，2表示为文件夹
	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public int getOwner() {
		return owner;
	}
	
	public void setOwner(int owner) {
		this.owner = owner;
	}

	public int[] getPtr() {
		return ptr;
	}

	public void setPtr(int[] ptr) {
		this.ptr = ptr;
	}

	private int fileType;
	private String fileName;
	private int owner;
	private static int size = FileSystem.getInodeSize();
	private int[] ptr;
	
	public INode() {
		fileType = 0;
		owner = 0;
		ptr = new int[size-3];
	}
	

	
	
	@Override
	public String toString() {
		String iNodeStr = "";
		
		return iNodeStr;
	}
	
	
	
}
