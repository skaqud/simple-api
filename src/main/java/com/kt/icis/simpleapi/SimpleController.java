package com.kt.icis.simpleapi;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v1")
public class SimpleController {

    @GetMapping("/simple")
    public String getSimple() {
        return "Simple-API Service10";
    }
    
}
