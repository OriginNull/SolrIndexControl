package cn.origin.util;

import cn.origin.task.QuartzDeltaImportTask;
import cn.origin.task.QuartzFullImportTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 全量定时任务类
 */
public class MyQuartzFullJobBean extends QuartzJobBean {
    @Autowired
    private QuartzFullImportTask quartzFullImportTask1;
    @Autowired
    private QuartzDeltaImportTask quartzDeltaImportTask1;

    public QuartzFullImportTask getQuartzFullImportTask1() {
        return quartzFullImportTask1;
    }

    public void setQuartzFullImportTask1(QuartzFullImportTask quartzFullImportTask1) {
        this.quartzFullImportTask1 = quartzFullImportTask1;
    }

    public QuartzDeltaImportTask getQuartzDeltaImportTask1() {
        return quartzDeltaImportTask1;
    }

    public void setQuartzDeltaImportTask1(QuartzDeltaImportTask quartzDeltaImportTask1) {
        this.quartzDeltaImportTask1 = quartzDeltaImportTask1;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        Trigger trigger = jobexecutioncontext.getTrigger();
        String triggerName = trigger.getKey().getName();
        System.out.println("**************************************");
        try {
            // 方式一：获取JobExecutionContext中的service对象
            //SchedulerContext context = jobexecutioncontext.getScheduler().getContext();
            //获取spring中的上下文
            //ApplicationContext applicationContext = (ApplicationContext)context.get("applicationContextKey");

            //方式二
            JobDataMap jobDataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
            ApplicationContext applicationContext =  (ApplicationContext)jobDataMap.get("applicationContextKey");


            //获取全量定时任务的bean  之前的@Component注解相当于已经在Spring容器之中注册过了
            QuartzFullImportTask quartzFullImportTask = applicationContext.getBean("quartzFullImportTask", QuartzFullImportTask.class);
           // QuartzDeltaImportTask quartzDeltaImportTask = applicationContext.getBean("quartzDeltaImportTask", QuartzDeltaImportTask.class);
            quartzFullImportTask.runFullImportTask(triggerName);
           // quartzDeltaImportTask.runDeltaImportTask(triggerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
