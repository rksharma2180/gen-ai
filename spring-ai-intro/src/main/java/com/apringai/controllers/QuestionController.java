package com.apringai.controllers;

import com.apringai.iservices.OpenAIService;
import com.apringai.model.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/chat")
public class QuestionController {

    private final OpenAIService opnOpenAIService;

    public QuestionController(OpenAIService service){
        System.out.println(" Controller Initialized ");
        this.opnOpenAIService = service;
    }
    //1
    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question){
        return opnOpenAIService.getAnswer(question);
    }
    //2
    @PostMapping("/capital")
    public Answer getCapital(@RequestBody GetCapitalRequest capitalRequest){
        return opnOpenAIService.getCapital(capitalRequest);
    }
    //3
    @PostMapping("/capitalWithInfo")
    public Answer getCapitalInfo(@RequestBody GetCapitalRequest capitalRequest){
        return opnOpenAIService.getCapitalInfo(capitalRequest);
    }
    //4
    @PostMapping("/capitalWithJson")
    public Answer getCapitalJson(@RequestBody GetCapitalRequest capitalRequest){
        return opnOpenAIService.getCapitalInfoJson(capitalRequest);
    }
    //5
    @PostMapping("/capitalWithConverter")
    public GetCapitalResponse getCapitalWithConverter(@RequestBody GetCapitalRequest capitalRequest){
        return opnOpenAIService.getCapitalInfoConverter(capitalRequest);
    }

    //5
    @PostMapping("/capitalInfoFormatted")
    public GetCapitalInfoResponse getCapitalInfoFormatted(@RequestBody GetCapitalRequest capitalRequest){
        return opnOpenAIService.getCapitalInfoFormatted(capitalRequest);
    }

}
