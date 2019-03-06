# filesystem

a simulation of linux file system

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
	
	![格式化磁盘](resources/format_disk.png)

- 在格式化的DISK文件上初始化文件系统
	
	![格式化磁盘](resources/initial_smokeOS.png)
	
	- 