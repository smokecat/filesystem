package com.smoke.filesystem;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;



public class Shell {
	private FileSystem smokeOS;
	
	private String prompt;		//	提示符
	private String user;		//	用户名
	private int uid;			//	用户id
	private String curDir;		//	当前目录
	private INode parent;		//	当前目录节点
	
	
	public Shell(FileSystem OS) {
		smokeOS = OS;
	}
	
	public void run() throws IOException, InterruptedException {
		/*
		 * 	
		 */
//		Shell信息
		System.out.println("\n\nSmokeOS 0.1.0 smoke-machine tty1");
		System.out.println("");
		
//		登陆
		Scanner sc = new Scanner(System.in);
		login(sc);
//			定位到家目录
		parent = smokeOS.getINode("/home/"+user);
		curDir = smokeOS.getINodeName(parent.getId());
		System.out.print("\nCurrent login: "+new Date().toString());
		System.out.println("\nWelcome to SmokeOS 0.1.0");
		System.out.println("\n * project: \t https://github.com/SmokeCat/filesystem");
		System.out.println("\n");
		setPrompt();
		
//		交互
		String command;
		while(true) {
			System.out.print(prompt);
			command = sc.nextLine().strip();
			if(command.equals("shutdown")) {
				break;
			}
			doCommand(command);
		}
		sc.close();
		shutDown();
	}
	
	public void login(Scanner sc) throws IOException {
		/*
		 * 	登陆认证
		 */
		while(true) {
			System.out.print("smoke-machine login:");
			user = sc.nextLine();
			System.out.print("Password:");
			String passwd = sc.nextLine();
			
//			身份验证
			uid = userAuthen(user, passwd);
			if(uid != -1) {
				break;
			}else {
				System.out.println("Login incorrect");
			}
		}
		
	}
	
	public int userAuthen(String user, String passwd) throws IOException {
		/*
		 * 	认证用户名和密码
		 */
//		获取user配置文件
		String shadow = smokeOS.readFile(smokeOS.getINode("/etc/shadow"));
		String[] userList = shadow.strip().split(" ");
		String[] userfile;
		int id;
		for(int i=0; i<userList.length; i++) {
			userfile = userList[i].split(":");
			if(userfile[1].equals(user)) {
				if(userfile[2].equals(passwd)) {
					id = Integer.valueOf(userfile[0]);
					return id;
				}
			}
		}
		return -1;
	}
	
	public void setPrompt() {
		/*
		 * 	更新提示符
		 */
		String proChar;
		if(user.equals("root")) {
			proChar = "#";
		}else {
			proChar = "$";
		}
		prompt = (user+"@"+"smoke-machine:"+curDir+proChar+" ");
	}
	
	public void doCommand(String com) throws IOException {
		/*
		 * 	处理命令
		 */
		String[] coms = com.split(" ");
		int length = coms.length;
		switch (coms[0]) {
		case "mkdir":
//			创建文件夹
			if(length>=2) {
				for(int i=1; i<length; i++) {
					smokeOS.mkdir(parent, uid, coms[i]);
				}
			}else {
				System.out.println("error: Illegal param");
			}
			break;
		case "cd":
//			切换目录
			if(length==2) {
				String path = coms[1];
				parent = smokeOS.getINode(path);
				curDir = smokeOS.getINodeName(parent.getId());
				setPrompt();
			}else {
				System.out.println("error: Illegal param");
			}
			break;
		case "rmdir":
//			删除目录和目录下所有文件
			if(length==2) {
				String path = coms[1];
				INode dirNode = smokeOS.getINode(path);
				if(dirNode.getFlag()!=1) {
//					判断是否为目录
					System.out.println("error: "+smokeOS.getINodeName(dirNode.getId())+" is not a directory");
					break;
				}else if(dirNode.getOwner()!=uid) {
//					判断权限
					System.out.println("error: permission denied");
					break;
				}
				rmdir(dirNode);
			}else {
				System.out.println("error: Illegal param");
			}
			break;
		case "createf":
//			创建文件
			if(length==3) {
				String fileName = coms[1];
				String fileContent = coms[2];
				smokeOS.createFile(parent, uid, fileName, fileContent);
			}else {
				System.out.println("error: Illegal param");
			}
			break;
		case "rmf":
//			删除文件
			if(length==2) {
				String fileName = coms[1];
				INode file = smokeOS.getINode(smokeOS.getFileId(parent, fileName));
				if(file.getOwner()!=uid) {
//					判断权限
					System.out.println("error: permission denied");
					break;
				}
				smokeOS.deleteFile(file);
			}else {
				System.out.println("error: Illegal param");
			}
			break;
		case "openf":
//			显示文件
			if(length==2) {
				String path = coms[1];
				INode file = smokeOS.getINode(path);
				if(file.getOwner()!=uid) {
//					判断权限
					System.out.println("error: permission denied");
					break;
				}
				String fileContent = smokeOS.readFile(file);
				System.out.println(fileContent);
			}else {
				System.out.println("error: Illegal param");
			}
			break;
		case "closef":
//			if(length==2) {
//				
//			}else {
//				System.out.println("error: Illegal param");
//			}
		case "ls":
//			显示目录内容
			if(length==2) {
				String path = coms[1];
				INode dirNode = smokeOS.getINode(path);
				String[] dirC = smokeOS.getDirChilds(dirNode);
				String fileContent = "";
				for(int i=0; i<dirC.length; i+=2) {
					fileContent = (fileContent + " " + dirC[i+1] + " ");
				}
				System.out.println(fileContent);
			}else {
				System.out.println("error: Illegal param");
			}
			break;
		default:
			System.out.println("error: Command " + coms[0] + " not fund.");
			break;
		}
	}
	
	public void shutDown() throws InterruptedException {
		/*
		 * 	关机
		 */
		System.out.println("shutdown now ...");
		Thread.sleep(1000);   
//		清屏
		
	}
	
//	命令功能
	public void rmdir(INode dirNode) throws IOException {
		/*
		 * 	
		 */
		String[] childs = smokeOS.getDirChilds(dirNode);
		if(childs.length<=4) {
			INode dirPar = dirNode.getParent();
			String dirStr = "";
			String[] parChilds = smokeOS.getDirChilds(dirPar);
			for(int i=0; i<parChilds.length; i+=2) {
				if(Integer.valueOf(parChilds[i]) == dirNode.getId()) {
					i+=2;
					if(i>=parChilds.length)
						break;
				}
				dirStr = (dirStr + parChilds[i]+ " "+parChilds[i+1]+" ");
			}
			smokeOS.updateFile(dirPar, dirStr);
			smokeOS.deleteFile(dirNode);
			return;
		}
		for(int i=4; i<childs.length; i+=2) {
			INode child = smokeOS.getINode(Integer.valueOf(childs[i]));
			if(child.getFlag()==1) {
				rmdir(child);
			}else {
				smokeOS.deleteFile(dirNode);
			}
		}
		INode dirPar = dirNode.getParent();
		String dirStr = "";
		String[] parChilds = smokeOS.getDirChilds(dirPar);
		for(int i=0; i<parChilds.length; i+=2) {
			if(Integer.valueOf(parChilds[i]) == dirNode.getId()) {
				i+=2;
			}
			dirStr = (dirStr + parChilds[i]+ " "+parChilds[i+1]+" ");
		}
		smokeOS.updateFile(dirPar, dirStr);
		smokeOS.deleteFile(dirNode);
	}
//	
//	public void createf(String fileName) {
//		
//	}
//	
//	public void rmf(String fileName) {
//		
//	}
//	
//	public void openf(String fileName) {
//		
//	}
//	
//	public void closef(String fileName) {
//		
//	}
//	
//	public void ls() {
//		
//	}

}
