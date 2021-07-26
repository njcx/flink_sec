# 使用Flink 开发规则引擎

支持正则、子串、等于，支持多字段 "和" "或" 检测， 支持频率检测,支持自定义函数检测,自定义函数可以满足几乎所有数据类型的检测


输入：

Kafka(Json)


输出： 

E-Mail，Kafka 文件



```go

state:     enable                        //  规则状态 enable   disable 
rule_id :  sqli_get_01                   //  规则ID
rule_tag:  sqli                          //  规则标签
rule_name: sqli_get_select               //  规则名

rule_type:  or                           //  or 类型规则代表，detect_list里面命中任何一条，算命中
rule_type:  and                          //  and 类型规则代表，detect_list里面命中所有规则，算命中
rule_type:  frequency_or                 //  frequency_or 类型规则代表，detect_list里面命中任何一条，且以key计数，单位时间内达到计数值上限算命中
rule_type:  frequency_and                //  frequency_and 类型规则代表，detect_list里面命中所有规则，且以key计数，单位时间内达到计数值上限算命中


detect_list:

  - field : conn.conn_state              //  字段
    type: re                             //  正则
    rule: S0                             //  具体规则
    ignorecase: false                    //  是否忽略大小写

  - field : conn.proto                   //  字段
    type: equal                          //  等于
    rule : tcp                           //  具体规则

  - field : conn.conn_state              // 字段
    type: in                             // 判断是否为子串
    rule : S0                            // 具体规则
 
  - field: conn.ip                       // 字段
    type: customf                        // 自定义函数
    rule: CheckIP                        // 自定义函数名 


key : conn.ip                    // 只有frequency 类型的有，以此字段对应数据为key计数

times: 10     // 只有frequency 类型的有，代表 flink 的滚动的时间窗口内出现 10次


threat_level : high                       // 威胁等级
auth : njcx86                             // 作者
info : about sql injection attack         // 注释

e-mail:                                   // 告警发送的邮箱
    - 868726@gmail.com
    - njcx91@tom.com


```
