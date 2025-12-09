package com.apringai.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record GetCapitalInfoResponse(@JsonPropertyDescription("The capital of ") String city,
                                     @JsonPropertyDescription("The city has a population of") Integer population,
                                     @JsonPropertyDescription("The city is located in") String region,
                                     @JsonPropertyDescription("The primary language spoken is") String language,
                                     @JsonPropertyDescription("The currency used is") String currency) {
}
