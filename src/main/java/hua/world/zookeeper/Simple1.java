package hua.world.zookeeper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Simple1 {

	public static void main1(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH");
		String path = "/task1" + sdf.format(new Date());
		ObjectMapper maper = new ObjectMapper();
		try {
			byte data[] = "test".getBytes();
			Watcher watcher = new SimpleWatcher();
			//集群部署 127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002 逗号分隔
			ZooKeeper zk = new ZooKeeper("192.168.99.100:2181", 3000, watcher);
			//永久节点PERSISTENT 临时节点EPHEMERAL
//			zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			zk.delete(path, -1);
			
			Stat e = zk.exists(path, watcher);
			
			System.out.println("step0>>"+maper.writeValueAsString(e));
			
			Stat stat = new Stat();
			byte[] dataStr = zk.getData(path,true,stat);
			System.out.println("step1>>"+new String(dataStr));
			
			List<String> children = zk.getChildren(path, new SimpleWatcher());
			
			zk.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
