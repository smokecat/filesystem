package com.smoke.filesystem;


public class INode {
	
	private int id;				//	inode号
	private int flag;			//	文件类型，0为空闲inode，1为文件夹，2为文件
	private int owner;			//	文件所属者id,root用户为0
	private int[] ptr;			//	指向存储数据块
	
	public INode(int i, int f, int o) {
		id = i;
		flag = f;
		owner = o;
		ptr = new int[FileSystem.getInodeSize()-3];
	}
	
	public void setNextPtr(int ptrNo) {
		/*
		 * 	设置文件存储块指针
		 */
		int i = 0;
		while(ptr[i]!=0 && i<ptr.length) {
			i++;
		}
		ptr[i] = ptrNo;
	}
	
	public void freeINode(){
		flag = 0;
		owner = 0;
		for(int i=0; i<ptr.length; i++) {
			ptr[i] = 0;
		}
	}
	
	@Override
	public String toString() {
		String iNodeStr = "";
		
		return iNodeStr;
	}

	public int getFlag() {
		return flag;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public int[] getPtr() {
		return ptr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwner() {
		return owner;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
