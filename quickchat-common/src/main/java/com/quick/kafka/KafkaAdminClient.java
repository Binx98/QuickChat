package com.quick.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @Author 徐志斌
 * @Date: 2023/5/13 21:45
 * @Version 1.0
 * @Description: Kafka工具类
 */
public class KafkaAdminClient {
    private static String url = "101.42.13.186:9092";

    /**
     *
     */
    public static void main(String[] args) throws Exception {
//        createTopic(MQConstant.COMMUNITY_POST_TOPIC);
//        createTopic(MQConstant.IM_SEND_TOPIC);
//        deleteTopic("COMMUNITY-POST-TOPIC");
        getTopic();
    }

    /**
     * 创建Topic
     */
    public static void createTopic(String topicName) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        try (AdminClient adminClient = AdminClient.create(props)) {
            // 主题名、分区
            NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
            adminClient.createTopics(Collections.singleton(newTopic));
        }
    }

    /**
     * 查询Topic
     */
    public static void getTopic() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        AdminClient adminClient = AdminClient.create(props);
        ListTopicsResult topicsResult = adminClient.listTopics();
        System.out.println("==================" + topicsResult.names().get() + "==================");
    }

    /**
     * 删除Topic
     */
    public static void deleteTopic(String topicName) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, url);

        try (AdminClient adminClient = AdminClient.create(props)) {
            DeleteTopicsOptions options = new DeleteTopicsOptions();
            options.timeoutMs(5000);
            DeleteTopicsResult result = adminClient.deleteTopics(Collections.singleton(topicName), options);
            KafkaFuture<Void> allDone = result.values().get(topicName);
            allDone.get();
            System.out.println("主题 " + topicName + " 已成功删除");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}