package com.cn.boot.sample.kafka.partitioner;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * 自定义分区器，可参考RoundRobinPartitioner
 *
 * @author Chen Nan
 */
@Slf4j
public class TestPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int total = cluster.partitionCountForTopic(topic);
        List<PartitionInfo> partitionInfos = cluster.availablePartitionsForTopic(topic);
        int available = partitionInfos.size();
        log.info("topic={} total={} available={}", topic, total, available);
        if (key != null) {
            if (available > 0) {
                return key.hashCode() % available;
            } else {
                return key.hashCode() % total;
            }
        }
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
