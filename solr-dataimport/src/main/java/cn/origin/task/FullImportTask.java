//package cn.origin.task;

//@Component
//@PropertySource("classpath:config/dataimport.properties")
//public class FullImportTask {
//	@Value("${full-import.url}")
//	private String fullImportUrl ;  // 全导入的配置地址
//	@Scheduled(cron="0 0 0 1,5,15,20,25 * ?")
//	public void runFullImportTask() {
//		System.out.println("【Full-Import】进行Solr索引的完全导入。");
//		HttpRequestUtil.send(this.fullImportUrl) ;
//	}
//}
