# SolrIndexControl

## 基于solr的全量索引更新和增量索引更新的动态定时任务实现
利用Quartz和Spring进行整合处理，同时利用HttpClient来进行HTTP请求的处理

## 动态定时任务处理分析
 ### 四个核心接口：
   ● Job <br/>
   ● JobDetail<br/>
   ● Trigger <br/>
   ● Scheduler <br/>
 
 三个核心概念：  任务、调度器、触发器
 
 ### 几个接口详解：
   ● Job：任务的核心接口<br/>
       只有这一个void execute(JobExecutionContext var1) throws JobExecutionException方法，需要实现该接口来定义需要执行的任务，
       JobExecutionContext类提供了调度上下文的各种信息。Job运行时的信息保存在JobDataMap之中。
 
   ● JobDetail：用来描述Job的实现类及其他相关的静态信息，如：Job名称、描述、关联监听器等信息 <br/>
       Quartz每次执行Job时，都重新创建一个Job实例。所以它不是直接接收一个Job实例，而是接收一个Job实现类，以便运行时通过newInstance()的反射调用机制实例化Job。<br/>
       构造方法：public JobDetailImpl(String name, String group, Class<? extends Job> jobClass)<br/>
           			  |- Job名称，组名称，实现类
          
   ● Trigger:用来描述触发Job执行的时间触发规则。主要有SimpleTrigger（间隔调度）和CronTrigger（CRON调度）两个子类。<br/>
 
   ● Scheduler：代表一个Quartz的独立运行容器，Trigger和JobDetail可以注册到Schduler之中，二者在Scheduler中拥有各自的组及名称。<br/>
   组及名称是Scheduler查找定位容器中某一对象的依据，Trigger的组及名称的组合必须唯一，JobDetail的组及名称的组合也必须唯一（但是可以和Trigger的组合名称相同，因为他们是不同类型的，处在不同的集合中）。可以对Job和Trigger进行相关控制。
 
   ● Scheduler定义了多个接口方法，运行外部通过组及名称访问和控制容器中的Trigger和JobDetail。Scheduler可以将Trigger绑定到某一JobDetail中，这样当Trigger被重复，对应的Job就被执行。一个Job对应多个Trigger，但一个Trigger只能对应一个Job.可以通过SchedulerFactory创建一个Scheduler实例。Scheduler拥有一个SchedulerContext，保存着Scheduler上下文信息，可以按照ServletContext来理解SchedulerContext.Job和Trigger都可以访问SchedulerContext内的信息。SchedulerContext为保存和获取数据提供了多个put()和getXXX()方法.
 
   ● ThreadPool:Scheduler使用一个线程池作为任务运行的基础设施，任务通过共享线程池中的线程来提高运行效率。<br/>
 
 ### Spring提供的支持：
 
  ● 为Quartz的重要组件提供更具Bean风格的扩展类<br/>
  ● 提供创建Scheduler的BeanFactory类，方便在Spring环境下创建对应的组件对象，并结合Spring容器生命周期执行启动和停止的动作。<br/>
	
  > JobDetail ---------- JobDetailFactoryBean  <br/>
    MethodInvokingJobDetailFactoryBean <br/>
    SimpleTrigger ----------- SimpleTriggerFactoryBean <br/>
    CronTrigger --------------- CronTriggerFactoryBean <br/>
    SchedulerFactory  ----------- SchedulerFactoryBean <br/>


**需求**：可以动态的进行任务的控制，暂停旧的任务，开启新的任务，删除旧的任务... 
	总之，任务现在是活的 

### 动态定时任务实现思路：


> 思路一：不结合Spring，单纯的利用Quartz提供的类进行实现。

```
    ① 利用实现Job来定义定时任务
    ② 利用JobDetail对任务的实现类及其他相关的静态信息进行描述
    ③ 利用Trigger进行任务的触发控制(间隔控制、CRON控制)
	|- 此处可以自己手工调用CronTrigger接口或者SimpleTrigger接口的方法进行实现，也可以自己重新创建自己特色的方法
	（它俩内部Quartz针对于间隔调度和CRON调度进行了具体类的控制实现，具体细节可以参看源代码）
    ④ 利用Scheduler进行任务的调度处理，主要进行一些特定的配置文件的处理（集群信息、调度器线程池、任务调度现场数据的保存(默认保存在内存之中，可以进行配置将其保存在数据库之中)）
```


> 思路二：利用Spring给Quartz提供的注入Bean进行配置实现(依旧是Quartz的内容，不过是做了一层的封装) <br/>
		整体执行流程：任务 -  触发器 - 调度器
		
```
    ① 利用实现Job来定义定时任务，通过JobExecutionContext来获取Spring上下文，从而通过自定义的调度器来进行任务的操作
    ② 配置JobDetailFactoryBean，配置SchedulerFactoryBean进行Quartz相关配置文件的设置
    ③ 进行触发器的设置，之后任务就开始执行了。
```


		
