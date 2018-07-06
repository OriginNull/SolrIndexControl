package cn.origin.task;

import cn.origin.util.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 增量定时调度任务执行
 */
@Component
@PropertySource("classpath:config/dataimport.properties")
public class QuartzDeltaImportTask {
	@Value("${delta-import.url}")
	private String deltaImportUrl ;  // 全导入的配置地址
	//@Scheduled(cron="0 * * * * ?")	// 每分钟进行一次增量索引
	public void runDeltaImportTask(String triggerName) {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println(this.deltaImportUrl);
		System.out.println("【Delta-Import】进行Solr索引的增量导入。");
		HttpRequestUtil.send(this.deltaImportUrl) ;
	}
}