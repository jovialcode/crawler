package com.jovialcode.fetcher.web;

import com.jovialcode.fetcher.web.dto.MessageResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/crawler")
public class ApiController {

    @PostMapping("/create")
    public MessageResponse createCrawler(){
        return MessageResponse.of("value");
    }

}
