package cn.origin.task;

import cn.origin.util.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全量定时调度任务执行
 */
@Component
@PropertySource("classpath:config/dataimport.properties")
public class QuartzFullImportTask {
	@Value("${full-import.url}")
	private String fullImportUrl ;  // 全导入的配置地址
	//@Scheduled(cron="0 0 0 1,5,15,20,25 * ?")
	public void runFullImportTask(String triggerName) {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println(this.fullImportUrl);
		System.out.println("【Full-Import】进行Solr索引的完全导入。");
		HttpRequestUtil.send(this.fullImportUrl) ;
	}
}