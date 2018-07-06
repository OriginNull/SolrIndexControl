package cn.origin.main;
import java.util.concurrent.TimeUnit;

import cn.origin.service.ISchedulerDeltaService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cn.origin.service.ISchedulerService;

public class StartMainSolrImportQuartzMain {
    public static void main(String[] args) throws Exception {
        ApplicationContext springContext = new ClassPathXmlApplicationContext("classpath:spring/spring-task.xml");
//        StdSchedulerFactory ssf=new StdSchedulerFactory();
//        Properties pro=new Properties();
//        pro.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
//        pro.put("org.quartz.scheduler.instanceName", "Scheduler");
//        pro.put("org.quartz.threadPool.threadCount", "10");
//        try {
//            ssf.initialize(pro);
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }

        //全量任务执行
        ISchedulerService schedulerService = springContext.getBean("schedulerServiceImpl", ISchedulerService.class);
        //schedulerService.schedule("0/10 * * ? * * *");  //10秒执行一次
       // schedulerService.schedule("0 * * * * ?"); //一分钟执行一次
        schedulerService.schedule("full","group1","0/10 * * ? * * *"); //10秒执行一次

        //增量任务执行
        ISchedulerDeltaService schedulerDeltaService = springContext.getBean("schedulerDeltaServiceImpl",ISchedulerDeltaService.class);
        schedulerDeltaService.schedule("delta","group2","0/5 * * ? * * *");  //5秒执行一次



        //休眠30秒之后移除触发器
        TimeUnit.SECONDS.sleep(30);
        schedulerService.removeTrigdger("full","group1");  //移除全量触发器
        schedulerDeltaService.removeTrigdger("delta","group2");  //移除增量触发器

        //休眠10秒之后重新设置触发器
        TimeUnit.SECONDS.sleep(10);
        schedulerService.schedule("full","group1","0/10 * * ? * * *");
        schedulerDeltaService.schedule("delta","group2","0/5 * * ? * * *");  //5秒执行一次

    }

}