package hua.world.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestTask1 {
	
	@Scheduled(cron="0 0/2 * * * ?")
	public void task1() throws InterruptedException {
		System.out.println("task1 is starting! now time is " + System.currentTimeMillis());
	}
}
