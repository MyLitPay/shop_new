package com.simbirsoft.service;

import com.simbirsoft.api.dto.CancellationDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Cancellation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CancellationService {
    List<CancellationDto> getAllCancellations();
    CancellationDto addCancellation(CancellationDto dto);
    List<CancellationDto> updateAllCancellations(List<CancellationDto> request);
    ResultResponse deleteAllCancellations();
    CancellationDto getCancellationById(Long id);
    CancellationDto updateCancellationById(Long id, CancellationDto dto);
    void deleteCancellationById(Long id);

    Cancellation findCancellationById(long id);
}
