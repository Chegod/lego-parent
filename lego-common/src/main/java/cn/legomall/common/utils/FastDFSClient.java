package cn.legomall.common.utils;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class FastDFSClient {

	//追踪服务器
	private TrackerServer trackerServer = null;
	//追踪服务器客户端
	private TrackerClient trackerClient = null;
	//存储服务器
	private StorageServer storageServer = null;
	/*
	存储服务器客户端
	FastDFS后的File ID一般形如"group1/M01/00/2A/rBAsVk8ORy2Nf9EoAAIRKo2Da7U901.jpg"形式。
	其中的“group1”称为Group Name，
	后段的“M01/00/08/rBAsVk8ORy2Nf9EoAAlRKo2Da7U901.jpg"称为File Name。
	StorageClient中是将File ID用Group Name和File Name两部分来表示的,是一个String
	而StorageClient1中是将Group Name和File Name统一起来处理的,是一个String[]
	*/
	private StorageClient1 storageClient = null;
	
	public FastDFSClient(String conf) throws Exception {
		if (conf.contains("classpath:")) {
			conf = conf.replace("classpath:", this.getClass().getResource("/").getPath());
		}
		//加载配置文件，配置文件中的内容就是tracker服务的地址。
		ClientGlobal.init(conf);
		//创建一个TrackerClient对象
		trackerClient = new TrackerClient();
		//使用TrackerClient对象创建连接，获得一个TrackerServer对象。
		trackerServer = trackerClient.getConnection();
		//创建一个StorageServer的引用，值为null
		storageServer = null;
		//创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
		storageClient = new StorageClient1(trackerServer, storageServer);
	}
	
	/**
	 * 上传文件方法
	 * <p>Title: uploadFile</p>
	 * <p>Description: </p>
	 * @param fileName 文件全路径
	 * @param extName 文件扩展名，不包含（.）
	 * @param metas 文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
		String result = storageClient.upload_file1(fileName, extName, metas);
		return result;
	}
	
	public String uploadFile(String fileName) throws Exception {
		return uploadFile(fileName, null, null);
	}
	
	public String uploadFile(String fileName, String extName) throws Exception {
		return uploadFile(fileName, extName, null);
	}
	
	/**
	 * 上传文件方法
	 * <p>Title: uploadFile</p>
	 * <p>Description: </p>
	 * @param fileContent 文件的内容，字节数组
	 * @param extName 文件扩展名
	 * @param metas 文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {
		
		String result = storageClient.upload_file1(fileContent, extName, metas);
		return result;
	}
	
	public String uploadFile(byte[] fileContent) throws Exception {
		return uploadFile(fileContent, null, null);
	}
	
	public String uploadFile(byte[] fileContent, String extName) throws Exception {
		return uploadFile(fileContent, extName, null);
	}
}
