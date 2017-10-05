package hua.world.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SimpleWatcher implements Watcher {
	
	ObjectMapper maper = new ObjectMapper();

	@Override
	public void process(WatchedEvent event) {
		try {
			System.out.println("step2>>"+maper.writeValueAsString(event));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
