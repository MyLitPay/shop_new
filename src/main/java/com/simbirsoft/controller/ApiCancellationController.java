package com.simbirsoft.controller;

import com.simbirsoft.api.dto.CancellationDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.service.CancellationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cancellations")
public class ApiCancellationController {
    final CancellationService cancellationService;

    public ApiCancellationController(CancellationService cancellationService) {
        this.cancellationService = cancellationService;
    }

    @GetMapping
    public List<CancellationDto> getAllCancellations() {
        return cancellationService.getAllCancellations();
    }

    @PostMapping
    public CancellationDto addCancellation(@RequestBody CancellationDto dto) {
        return cancellationService.addCancellation(dto);
    }

    @PutMapping
    public List<CancellationDto> updateAllCancellations(@RequestBody List<CancellationDto> request) {
        return cancellationService.updateAllCancellations(request);
    }

    @DeleteMapping
    public ResultResponse deleteAllCancellations() {
        return cancellationService.deleteAllCancellations();
    }

    @GetMapping("/{id}")
    public CancellationDto getCancellationById(@PathVariable("id") Long id) {
        return cancellationService.getCancellationById(id);
    }

    @PutMapping("/{id}")
    public CancellationDto updateCancellationById(@PathVariable("id") Long id,
                                    @RequestBody CancellationDto dto) {
        return cancellationService.updateCancellationById(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResultResponse deleteCancellationById(@PathVariable("id") Long id) {
        return cancellationService.deleteCancellationById(id);
    }
}
