package cn.origin.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import cn.origin.service.ISchedulerService;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 如果自定义触发器，那么就需要对调度器和任务进行相关的控制
 */
@Service
public class SchedulerServiceImpl implements ISchedulerService {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerServiceImpl.class);
    private static final String NULLSTRING = null;
    private static final Date NULLDATE = null;

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private JobDetail jobDetail1;

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public JobDetail getJobDetail() {
        return jobDetail1;
    }

    public void setJobDetail1(JobDetail jobDetail1) {
        this.jobDetail1 = jobDetail1;
    }

    @Override
    public void schedule(String cronExpression) {
        schedule(NULLSTRING, cronExpression);
    }

    @Override
    public void schedule(String name, String cronExpression) {
        schedule(name, NULLSTRING, cronExpression);
    }

    @Override
    public void schedule(String name, String group, String cronExpression) {
        try {
            schedule(name, group, new CronExpression(cronExpression));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void schedule(CronExpression cronExpression) {
        schedule(NULLSTRING, cronExpression);
    }

    @Override
    public void schedule(String name, CronExpression cronExpression) {
        schedule(name, NULLSTRING, cronExpression);
    }

    @Override
    public void schedule(String name, String group, CronExpression cronExpression) {

        if (isValidExpression(cronExpression)) {

            if (name == null || name.trim().equals("")) {
                name = UUID.randomUUID().toString();
            }

            CronTriggerImpl trigger = new CronTriggerImpl();
            trigger.setCronExpression(cronExpression);

            TriggerKey triggerKey = new TriggerKey(name, group);

            trigger.setJobName(jobDetail1.getKey().getName());
            trigger.setKey(triggerKey);

            try {
                scheduler.addJob(jobDetail1, true);
                if (scheduler.checkExists(triggerKey)) {
                    scheduler.rescheduleJob(triggerKey, trigger);
                } else {
                    scheduler.scheduleJob(trigger);
                }
            } catch (SchedulerException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public void schedule(Date startTime) {
        schedule(startTime, NULLDATE);
    }

    @Override
    public void schedule(Date startTime, String group) {
        schedule(startTime, NULLDATE, group);
    }

    @Override
    public void schedule(String name, Date startTime) {
        schedule(name, startTime, NULLDATE);
    }

    @Override
    public void schedule(String name, Date startTime, String group) {
        schedule(name, startTime, NULLDATE, group);
    }

    @Override
    public void schedule(Date startTime, Date endTime) {
        schedule(startTime, endTime, 0);
    }

    @Override
    public void schedule(Date startTime, Date endTime, String group) {
        schedule(startTime, endTime, 0, group);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime) {
        schedule(name, startTime, endTime, 0);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, String group) {
        schedule(name, startTime, endTime, 0, group);
    }

    @Override
    public void schedule(Date startTime, int repeatCount) {
        schedule(null, startTime, NULLDATE, 0);
    }

    @Override
    public void schedule(Date startTime, Date endTime, int repeatCount) {
        schedule(null, startTime, endTime, 0);
    }

    @Override
    public void schedule(Date startTime, Date endTime, int repeatCount, String group) {
        schedule(null, startTime, endTime, 0, group);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, int repeatCount) {
        schedule(name, startTime, endTime, 0, 0L);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, int repeatCount, String group) {
        schedule(name, startTime, endTime, 0, 0L, group);
    }

    @Override
    public void schedule(Date startTime, int repeatCount, long repeatInterval) {
        schedule(null, startTime, NULLDATE, repeatCount, repeatInterval);
    }

    @Override
    public void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval) {
        schedule(null, startTime, endTime, repeatCount, repeatInterval);
    }

    @Override
    public void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval, String group) {
        schedule(null, startTime, endTime, repeatCount, repeatInterval, group);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval) {
        schedule(name, startTime, endTime, repeatCount, repeatInterval, NULLSTRING);
    }

    @Override
    public void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval, String group) {

        if (this.isValidExpression(startTime)) {

            if (name == null || name.trim().equals("")) {
                name = UUID.randomUUID().toString();
            }

            TriggerKey triggerKey = new TriggerKey(name, group);

            SimpleTriggerImpl trigger = new SimpleTriggerImpl();
            trigger.setKey(triggerKey);
            trigger.setJobName(jobDetail1.getKey().getName());

            trigger.setStartTime(startTime);
            trigger.setEndTime(endTime);
            trigger.setRepeatCount(repeatCount);
            trigger.setRepeatInterval(repeatInterval);

            try {
                scheduler.addJob(jobDetail1, true);
                if (scheduler.checkExists(triggerKey)) {
                    scheduler.rescheduleJob(triggerKey, trigger);
                } else {
                    scheduler.scheduleJob(trigger);
                }
            } catch (SchedulerException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public void pauseTrigger(String triggerName) {
        pauseTrigger(triggerName, NULLSTRING);
    }

    @Override
    public void pauseTrigger(String triggerName, String group) {
        try {
            scheduler.pauseTrigger(new TriggerKey(triggerName, group));// 停止触发器
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resumeTrigger(String triggerName) {
        resumeTrigger(triggerName, NULLSTRING);
    }

    @Override
    public void resumeTrigger(String triggerName, String group) {
        try {
            scheduler.resumeTrigger(new TriggerKey(triggerName, group));// 重启触发器
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeTrigdger(String triggerName) {
        return removeTrigdger(triggerName, NULLSTRING);
    }

    @Override
    public boolean removeTrigdger(String triggerName, String group) {
        TriggerKey triggerKey = new TriggerKey(triggerName, group);
        try {
            //System.out.println("移除全量触发器*****************************");
            // System.out.println(triggerKey);
            logger.debug("移除全量触发器*****************************");
            logger.debug("获取全量触发器的key，以便对它进行控制 : " + triggerKey);

            scheduler.pauseTrigger(triggerKey);// 停止触发器
            return scheduler.unscheduleJob(triggerKey);// 移除任务
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidExpression(final CronExpression cronExpression) {

        CronTriggerImpl trigger = new CronTriggerImpl();
        trigger.setCronExpression(cronExpression);

        Date date = trigger.computeFirstFireTime(null);

        return date != null && date.after(new Date());
    }

    private boolean isValidExpression(final Date startTime) {

        SimpleTriggerImpl trigger = new SimpleTriggerImpl();
        trigger.setStartTime(startTime);

        Date date = trigger.computeFirstFireTime(null);

        return date != null && date.after(new Date());
    }
}