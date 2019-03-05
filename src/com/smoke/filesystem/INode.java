package com.smoke.filesystem;



public class INode {
	
	private int id;				//	inode号
	private int flag;			//	文件类型，0为空闲inode，1为文件夹，2为文件
	private int owner;			//	文件所属者id,root用户为0
	private int[] ptr;			//	指向存储数据块
	private int[] pos;			//	此inode的位置信息{行，列}
	private INode parent;		//	父节点
	
	public INode() {
		id = 0;
		flag = 0;
		owner = 0;
		ptr = new int[FileSystem.getInodeSize()-3];
		pos = new int[2];
	}
	
	public INode(int f, int o, int[] p) {
		flag = f;
		owner = o;
		ptr = new int[FileSystem.getInodeSize()-3];
		pos = p;
	}
	

	
	public void setPtrNo(int ptrNo) {
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
		iNodeStr = (String.valueOf(id)+" "+String.valueOf(flag)+" "+String.valueOf(owner)+" ");
		for(int i=0; i<ptr.length; i++) {
			iNodeStr = (iNodeStr+String.valueOf(ptr[i])+" ");
		}
		
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

	public void setId() {
		id = (pos[0]-1)*4 + pos[1] + 1;
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

	public int[] getPos() {
		return pos;
	}

	public void setPos(int[] pos) {
		this.pos = pos;
	}

	public void setPtr(int[] ptr) {
		this.ptr = ptr;
	}

	public INode getParent() {
		return parent;
	}

	public void setParent(INode parent) {
		this.parent = parent;
	}
	
}
