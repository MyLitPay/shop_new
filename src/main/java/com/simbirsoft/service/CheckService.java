package com.simbirsoft.service;

import com.simbirsoft.api.dto.CheckDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Check;

import java.util.List;

public interface CheckService {
    List<CheckDto> getAllChecks();
    CheckDto addCheck(CheckDto dto);
    List<CheckDto> updateAllChecks(List<CheckDto> request);
    ResultResponse deleteAllChecks();
    CheckDto getCheckById(Long id);
    CheckDto updateCheckById(Long id, CheckDto dto);
    ResultResponse deleteCheckById(Long id);

    Check findCheckById(long id);
}
