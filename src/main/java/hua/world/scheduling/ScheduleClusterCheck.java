package hua.world.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import hua.world.zookeeper.SimpleWatcher;

@Aspect
@Component
public class ScheduleClusterCheck {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH");
	private final Logger LOG = LoggerFactory.getLogger(ScheduleClusterCheck.class);
	
//	public static void main(String[] args) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH");
//		System.out.println(sdf.format(new Date()));
//	}
	
	@Autowired
	private ZooKeeper zooKeeper;
//	private boolean pass = false;

	// 定义一个切入点
	@Pointcut("execution(* hua.world.scheduling.*.*(..))")
	private void method() {

	}

//	@Before("method()")
//	public void before(JoinPoint joinPoint) throws Exception {
//		String methodName = joinPoint.getSignature().getName();
//		String path = "/" + methodName + sdf.format(new Date());
//		byte[] data = methodName.getBytes();
//		try {
//			zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//			pass = true;
//		} catch (Exception e) {
//			LOG.error(e.getMessage());
//			throw new Exception(e.getMessage());
//		}
//		if(pass) {
//			System.out.println("拦截前" + methodName + "-" + joinPoint.getTarget().getClass().getSimpleName());
//		}
//	}
	
	@Around("method()")
	public void test(ProceedingJoinPoint pjp) throws Throwable {
		boolean pass = false;
		String methodName = pjp.getSignature().getName();
		String path = "/" + methodName + sdf.format(new Date());
		byte[] data = methodName.getBytes();
		try {
			zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			pass = true;
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		if(pass) {
			System.out.println("拦截前" + methodName + "-" + pjp.getTarget().getClass().getSimpleName());
			
			try {
				pjp.proceed();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			
			try {
				//锁1分钟后，删除节点
				Thread.sleep(1000 * 60 * 1);
				zooKeeper.delete(path, -1);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			System.out.println("拦截后" + methodName + "-" + pjp.getTarget().getClass().getSimpleName());
		}
		
	}

//	@After("method()")
//	public void after(JoinPoint joinPoint) throws Exception {
//		String methodName = joinPoint.getSignature().getName();
//		String path = "/" + methodName + sdf.format(new Date());
//		try {
//			//锁30秒后，删除节点
//			Thread.sleep(1000 * 60 * 1);
//			if(pass) {
//				zooKeeper.delete(path, -1);
//				pass = false;
//				System.out.println("拦截后" + methodName + "-" + joinPoint.getTarget().getClass().getSimpleName());
//			}
//		} catch (Exception e) {
//			LOG.error(e.getMessage());
//			throw new Exception(e.getMessage());
//		}
//	}
	
	@Bean
	public ZooKeeper zooKeeper() {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper("192.168.99.100:2181", 3000, simpleWatcher());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zk;
	}
	
	@Bean
	public SimpleWatcher simpleWatcher() {
		return new SimpleWatcher();
	}
}
