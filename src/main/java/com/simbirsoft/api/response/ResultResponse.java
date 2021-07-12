package com.simbirsoft.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ResultResponse {
    private String result;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors;

    public ResultResponse(ResultResponseType type) {
        this.result = type.name();
    }

    public ResultResponse(String result) {
        this.result = result;
    }
}
