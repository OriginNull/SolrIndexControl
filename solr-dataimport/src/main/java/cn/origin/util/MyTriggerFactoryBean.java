package cn.origin.util;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;

/**
 * 自定义触发器
 */
public class MyTriggerFactoryBean implements FactoryBean<CronTrigger>, BeanNameAware, InitializingBean {
    @Nullable
    private JobDetail jobDetail;
    private boolean durability = false;

    public boolean isDurability() {
        return durability;
    }

    public void setDurability(boolean durability) {
        this.durability = durability;
    }

    @Nullable
    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(@Nullable JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void setBeanName(String name) {

    }

    @Nullable
    @Override
    public CronTrigger getObject() throws Exception {
        return null;
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
