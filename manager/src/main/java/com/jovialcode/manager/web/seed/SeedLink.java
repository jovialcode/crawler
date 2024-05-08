package com.jovialcode.manager.web.seed;

import com.jovialcode.common.crawler.CrawlItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SeedLink {
    private final KafkaTemplate<String, CrawlItem> kafkaTemplate;

    public void send(String topic, CrawlItem payload) {
        log.info("sending payloa={} to topic={}", payload, topic);
        kafkaTemplate.send(topic, payload);
    }

}
