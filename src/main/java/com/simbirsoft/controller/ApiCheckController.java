package com.simbirsoft.controller;

import com.simbirsoft.api.dto.CheckDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.service.CheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checks")
public class ApiCheckController {
    final CheckService checkService;

    public ApiCheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping
    public ResponseEntity<List<CheckDto>> getAllChecks() {
        return checkService.getAllChecks();
    }

    @PostMapping
    public CheckDto addCheck(@RequestBody CheckDto dto) {
        return checkService.addCheck(dto);
    }

    @PutMapping
    public ResponseEntity<List<CheckDto>> updateAllChecks(@RequestBody List<CheckDto> request) {
        return checkService.updateAllChecks(request);
    }

    @DeleteMapping
    public ResultResponse deleteAllChecks() {
        return checkService.deleteAllChecks();
    }

    @GetMapping("/{id}")
    public CheckDto getCheckById(@PathVariable("id") Long id) {
        return checkService.getCheckById(id);
    }

    @PutMapping("/{id}")
    public CheckDto updateCheckById(@PathVariable("id") Long id,
                                    @RequestBody CheckDto dto) {
        return checkService.updateCheckById(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCheckById(@PathVariable("id") Long id) {
        checkService.deleteCheckById(id);
    }
}
