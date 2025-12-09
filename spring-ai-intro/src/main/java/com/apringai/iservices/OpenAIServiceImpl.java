package com.apringai.iservices;

import com.apringai.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ChatModel model;
    public OpenAIServiceImpl(ChatModel model){
        this.model = model;
    }

    @Override
    public Answer getAnswer(Question question) {

        PromptTemplate template = new PromptTemplate(question.question());
        Prompt promt = template.create();
        ChatResponse response  = model.call(promt);

        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public String getAnswer(String question) {

        PromptTemplate template = new PromptTemplate(question);
        Prompt promt = template.create();
        ChatResponse response  = model.call(promt);
        return response.getResult().getOutput().getText();
    }
    @Value("classpath:templates/get-capital-prompt.st")
    private Resource getCapitalPrompt;

    @Override
    public Answer getCapital(GetCapitalRequest capitalRequest) {
        //new approach String template
        //PromptTemplate template = new PromptTemplate("What is the capital of " + capitalRequest.stateOrCountry() + "?");
        PromptTemplate template = new PromptTemplate(getCapitalPrompt);
        Prompt prompt = template.create(Map.of("stateOrCountry", capitalRequest.stateOrCountry()));
        ChatResponse response  = model.call(prompt);
        return new Answer(response.getResult().getOutput().getText());
    }

    @Value("classpath:templates/get-capital-prompt-info.st")
    private Resource getCapitalPromptInfo;

    @Override
    public Answer getCapitalInfo(GetCapitalRequest capitalRequest) {
        PromptTemplate template = new PromptTemplate(getCapitalPromptInfo);
        Prompt prompt = template.create(Map.of("stateOrCountry", capitalRequest.stateOrCountry()));
        ChatResponse response  = model.call(prompt);
        return new Answer(response.getResult().getOutput().getText());
    }

    @Value("classpath:templates/get-capital-prompt-info_format.st")
    private Resource getCapitalPromptInfoFormat;

    @Override
    public GetCapitalInfoResponse getCapitalInfoFormatted(GetCapitalRequest capitalRequest) {

        BeanOutputConverter<GetCapitalInfoResponse> converter = new BeanOutputConverter<>(GetCapitalInfoResponse.class);
        String format = converter.getFormat();

        PromptTemplate template = new PromptTemplate(getCapitalPromptInfoFormat);
        Prompt prompt = template.create(Map.of("stateOrCountry", capitalRequest.stateOrCountry(), "format", format));
        ChatResponse response = model.call(prompt);
        System.out.println(response.getResult().getOutput().getText());
        return converter.convert(Objects.requireNonNull(response.getResult().getOutput().getText()));
    }

    @Value("classpath:templates/get-capital-info-json.st")
    private Resource getCapitalInfoJson;

    @Autowired
    private ObjectMapper mapper;
    @Override
    public Answer getCapitalInfoJson(GetCapitalRequest capitalRequest) {
        PromptTemplate template = new PromptTemplate(getCapitalInfoJson);
        Prompt prompt = template.create(Map.of("stateOrCountry", capitalRequest.stateOrCountry()));
        ChatResponse response = model.call(prompt);

        System.out.println(response.getResult().getOutput().getText());
        String respString;

        try{
            JsonNode node =  mapper.readTree(response.getResult().getOutput().getText());
            respString = node.get("answer").asText();
        } catch(JsonProcessingException e){
            throw new RuntimeException(e.getMessage());
        }
        return new Answer(respString);
    }

    @Value("classpath:templates/get-capital-info-converter.st")
    private Resource getCapitalInfoConverter;

    @Override
    public GetCapitalResponse getCapitalInfoConverter(GetCapitalRequest capitalRequest) {

        BeanOutputConverter<GetCapitalResponse> converter = new BeanOutputConverter<>(GetCapitalResponse.class);
        String format = converter.getFormat();

        PromptTemplate template = new PromptTemplate(getCapitalInfoConverter);
        Prompt prompt = template.create(Map.of("stateOrCountry", capitalRequest.stateOrCountry(),
                "format", format));
        System.out.println(format);
        ChatResponse response = model.call(prompt);

        return converter.convert(response.getResult().getOutput().getText());
    }
}
