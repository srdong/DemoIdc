此项目文档由唐总提供，svn ： 
doc：http://windows-98u7boe/svn/TN-IDC/doc
code：http://windows-98u7boe/svn/TN-IDC/java/trunk/idc_teenet

4个Job ：拉取频率来自 scheduler_cron
1，BreakPointTask 高频数据断点续传
2，HighFrequencyTask 高频数据上传
3，PullTimeDelTask 删除一个月前的数据
4，UnsuccessfulDataTask 失败客户数据重新拉取并上传

UnsuccessfulDataTask和BreakPointTask的进入用的是同一个逻辑
根据scheduler_cron频率设置，先执行的是UnsuccessfulDataTask
然后是BreakPointTask


主要数据处理逻辑： Transfer.class


新增用户，实现 CustomerInterface
application.properties中的idc.customer=实现类的注解名字

数据的倍率在数据拉取时就做处理

发送企业微信，需要配置
企业微信 -》 应用管理-》 IdcServer -》 配置 企业可信任IP

application.properties 增加属性,以下3个类都要相应增加
IdcConfig.class,GlobalParam.class,InitializationCommandLineRunner

字典表 dic 新增数据，需要调用/dic/clearType1 接口