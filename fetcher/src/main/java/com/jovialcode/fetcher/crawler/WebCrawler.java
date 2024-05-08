package com.jovialcode.fetcher.crawler;

import com.jovialcode.common.crawler.CrawlItem;
import com.jovialcode.common.urls.CleanedUrl;
import com.jovialcode.fetcher.infra.httpclients.HttpAgent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Duration;

/***
* 만약 여기서
 * Crawl Delay를 어떻게 적용하는게 좋을까?
 *
* **/

public class WebCrawler extends AbstractCrawler{
    private final HttpAgent httpAgent;
    private final Consumer<String, CrawlItem> consumer;
    private final MongoRepository<CrawlData, String> crawlDataRepository;

    public WebCrawler(final HttpAgent httpAgent,
                      final Consumer<String, CrawlItem> consumer,
                      MongoRepository<CrawlData, String> crawlDataRepository) {
        this.httpAgent = httpAgent;
        this.consumer = consumer;
        this.crawlDataRepository = crawlDataRepository;
    }

    @Override
    public CrawlResult crawl(CrawlItem crawlItem) {
        String download = this.httpAgent.download(crawlItem.getUri());
        return CrawlResult.of(crawlItem.getUri(), download);
    }

    @Override
    public void run() {
        try{
            while (true) {
                ConsumerRecords<String, CrawlItem> records = this.consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, CrawlItem> record : records) {

                    CrawlResult result = crawl(record.value());
                    save(result);
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void save(CrawlResult crawlResult){
        CleanedUrl cleanedUrl = new CleanedUrl(crawlResult.getUri());
        CrawlData crawlData = CrawlData.of(cleanedUrl.getValue(), crawlResult.getResponse(), 1);
        this.crawlDataRepository.save(crawlData);
    }
}
