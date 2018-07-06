package cn.origin.util;

import cn.origin.task.QuartzDeltaImportTask;
import cn.origin.task.QuartzFullImportTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时任务处理类
 */
public class MyQuartzDeltaJobBean extends QuartzJobBean {
    @Autowired
    private QuartzFullImportTask quartzFullImportTask;
    @Autowired
    private QuartzDeltaImportTask quartzDeltaImportTask;

    public QuartzFullImportTask getQuartzFullImportTask() {
        return quartzFullImportTask;
    }

    public void setQuartzFullImportTask1(QuartzFullImportTask quartzFullImportTask1) {
        this.quartzFullImportTask = quartzFullImportTask1;
    }

    public QuartzDeltaImportTask getQuartzDeltaImportTask1() {
        return quartzDeltaImportTask;
    }

    public void setQuartzDeltaImportTask1(QuartzDeltaImportTask quartzDeltaImportTask1) {
        this.quartzDeltaImportTask = quartzDeltaImportTask1;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        Trigger trigger = jobexecutioncontext.getTrigger();
        String triggerName = trigger.getKey().getName();
        System.out.println("**************************************");
        try {
            //方式一：利用在SchedulerFactoryBean注册，从而获取到上下文，然后对任务进行操作
            //SchedulerContext context = jobexecutioncontext.getScheduler().getContext();
            //获取spring中的上下文
            //ApplicationContext applicationContext = (ApplicationContext)context.get("applicationContextKey");


            //方式二：利用JobDetailFactoryBean注册，获取上下文，然后对任务操作
            JobDataMap jobDataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
            ApplicationContext applicationContext =  (ApplicationContext)jobDataMap.get("applicationContextKey");


            //QuartzFullImportTask quartzFullImportTask = applicationContext.getBean("quartzFullImportTask", QuartzFullImportTask.class);
            //获取增量定时任务的bean
            QuartzDeltaImportTask quartzDeltaImportTask = applicationContext.getBean("quartzDeltaImportTask", QuartzDeltaImportTask.class);
            //quartzFullImportTask.runFullImportTask(triggerName);
            quartzDeltaImportTask.runDeltaImportTask(triggerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
