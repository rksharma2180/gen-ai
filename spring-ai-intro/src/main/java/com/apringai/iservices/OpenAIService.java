package com.apringai.iservices;

import com.apringai.model.*;

public interface OpenAIService {

    Answer getAnswer(Question question);

    String getAnswer(String question);

    Answer getCapital(GetCapitalRequest capitalRequest);

    Answer getCapitalInfo(GetCapitalRequest capitalRequest);

    Answer getCapitalInfoJson(GetCapitalRequest capitalRequest);

    GetCapitalResponse getCapitalInfoConverter(GetCapitalRequest capitalRequest);

    GetCapitalInfoResponse getCapitalInfoFormatted(GetCapitalRequest capitalRequest);
}
