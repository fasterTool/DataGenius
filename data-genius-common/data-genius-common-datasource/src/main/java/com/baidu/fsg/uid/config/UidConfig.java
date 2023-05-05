package com.baidu.fsg.uid.config;

import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/28 14:13
 * @Description:
 */
@Configuration
public class UidConfig {

    @Bean(name = "disposableWorkerIdAssigner")
    public DisposableWorkerIdAssigner initDisposableWorkerIdAssigner() {
        DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
        return disposableWorkerIdAssigner;
    }

    /**
     * # 初始时间, 默认:"2019-02-20"
     * uid.epochStr=2020-05-08
     * # 时间位, 默认:30
     * uid.timeBits=41
     * # 机器位, 默认:16
     * uid.workerBits=10
     * # 序列号, 默认:7
     * uid.seqBits=12
     * # 是否容忍时钟回拨, 默认:true
     * uid.enableBackward=true
     * # RingBuffer size扩容参数, 可提高UID生成的吞吐量, 默认:3
     * uid.CachedUidGenerator.boostPower=3
     * # 指定何时向RingBuffer中填充UID, 取值为百分比(0, 100), 默认为50
     * uid.CachedUidGenerator.paddingFactor=50
     *
     * @param workerIdAssigner
     * @return
     */
    @Bean(name = "cachedUidGenerator")
    public CachedUidGenerator initCachedUidGenerator(WorkerIdAssigner workerIdAssigner) {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(workerIdAssigner);
        // 属性参考链接  https://github.com/baidu/uid-generator/blob/master/README.zh_cn.md
        // 以下为可选配置, 如未指定将采用默认值
        // cachedUidGenerator.setTimeBits(28);
        // cachedUidGenerator.setWorkerBits(22);
        // cachedUidGenerator.setSeqBits(13);
        // cachedUidGenerator.setEpochStr("2016-09-20");
        cachedUidGenerator.setBoostPower(3);
        cachedUidGenerator.setScheduleInterval(60L);
//		// 拒绝策略: 当环已满, 无法继续填充时
        // 默认无需指定, 将丢弃Put操作, 仅日志记录. 如有特殊需求, 请实现RejectedPutBufferHandler接口(支持Lambda表达式)
        // 拒绝策略: 当环已空, 无法继续获取时
        // 默认无需指定, 将记录日志, 并抛出UidGenerateException异常. 如有特殊需求, 请实现RejectedTakeBufferHandler接口(支持Lambda表达式)
        return cachedUidGenerator;
    }
}