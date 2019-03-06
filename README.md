# filesystem

smokeOS：a simulation of linux file system

一个模拟文件系统的程序

## 运行程序

- 运行环境：JDK 11

```
C:\Users\Smoke> java -version
java version "11.0.1" 2018-10-16 LTS
Java(TM) SE Runtime Environment 18.9 (build 11.0.1+13-LTS)
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.1+13-LTS, mixed mode)
```

- 克隆项目到本地

```
$ git clone git@github.com:SmokeCat/filesystem.git
```

- 命令行下进入项目根目录

```
$ cd filesystem
```

- 运行打包好的jar文件，重新运行前需要把生成的DISK文件删除

```
$ java -jav ./smokeOS.jar
```

## 程序描述

创建一个DISK文件，用于模拟磁盘，并在此磁盘上实现一个简单的文件系统，并提供一个shell用于用户交互。

com.smoke.disk.Disk	：提供读写文件的接口，设定磁盘的规格，默认为1024行，即有1024个block，每个block可以写入64个字符

com.smoke.filesystem.FileSystem	：提供文件系统的接口，实现此文件系统的功能

com.smoke.filesystem.Shell	：提供用户与文件系统交互的功能

### 程序执行流程

- 开机

- 加载DISK磁盘文件

	- 若DISK文件已存在，读取DISK文件并装载文件系统（未实现）
	- DISK文件不存在，创建一个空白DISK文件并格式化，即写入1024行64个0
	
	<img src="resources/format_disk.png" width=500 height=300 alt="empty disk" />

- 在格式化的DISK文件上初始化文件系统
	
	- 第1行存储superblock，记录磁盘block总数，inode总数，inode已使用数，数据块block已使用数，数据块block开始位置，控线数据块block位置
	- 第2行到第51行存储inode，每个block可以存储4个inode，即每个inode有16个字符
		
		- inode 第1到3个字符分别存储inode的id，flag，onwer id，剩余用来存储文件的block块号

	- 剩余都是存储数据的block
	
	<img src="resources/initial_smokeOS.png" width=500 height=300 alt="initial system" />

- 初始化文件系统后运行Shell，用户进行操作

	- 登录，认证用户身份

	- 提供命令行

	<img src="resources/shell.png" width=500 alt="shell">

- shutdown： 退出系统，关闭程序

### 用户指南

只是简单的实现了文件系统的逻辑，对用户操作不是很友好

### 文件系统的工作

一切皆文件，初始化文件系统生成了根目录，etc配置目录和home目录

etc目录中生成了一个shadow文件，用于记录系统用户的信息，包括用户id，用户名和用户密码，用于登录认证和权限认证，文件中包括root和smoke两个用户的信息

home目录中生成了root用户和smoke用户的家目录


#### 读取文件