package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.CheckDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.exception.CheckNotFoundException;
import com.simbirsoft.mapper.CheckMapper;
import com.simbirsoft.model.Check;
import com.simbirsoft.repo.CheckRepository;
import com.simbirsoft.service.CheckService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckServiceImpl implements CheckService {
    final CheckRepository checkRepository;

    public CheckServiceImpl(CheckRepository checkRepository) {
        this.checkRepository = checkRepository;
    }

    @Override
    public List<CheckDto> getAllChecks() {
        return checkRepository.findAll().stream()
                .map(CheckMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CheckDto addCheck(CheckDto dto) {
        Check check = CheckMapper.INSTANCE.toEntity(dto);
        Check checkFromDB = checkRepository.saveAndFlush(check);
        return CheckMapper.INSTANCE.toDTO(checkFromDB);
    }

    @Override
    public List<CheckDto> updateAllChecks(List<CheckDto> request) {
        List<Check> checkList = new ArrayList<>();

        for (CheckDto dto : request) {
            Check check = updateCheckData(findCheckById(dto.getId()), dto);
            checkList.add(check);
        }
        checkRepository.saveAll(checkList);
        return getAllChecks();
    }

    @Override
    public ResultResponse deleteAllChecks() {
        try {
            List<Check> checks = checkRepository.findAll();
            checkRepository.deleteAll(checks);
            return new ResultResponse(ResultResponseType.OK);
        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
        }
    }

    @Override
    public CheckDto getCheckById(Long id) {
        Check check = findCheckById(id);
        return CheckMapper.INSTANCE.toDTO(check);
    }

    @Override
    public CheckDto updateCheckById(Long id, CheckDto dto) {
        Check check = updateCheckData(findCheckById(id), dto);
        Check checkFromDB = checkRepository.saveAndFlush(check);
        return CheckMapper.INSTANCE.toDTO(checkFromDB);
    }

    @Override
    public ResultResponse deleteCheckById(Long id) {
        try {
            checkRepository.deleteById(id);
            return new ResultResponse(ResultResponseType.OK);
        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
        }
    }

    public Check findCheckById(long id) {
        return checkRepository.findById(id)
                .orElseThrow(() -> new CheckNotFoundException("Check not found"));
    }

    private Check updateCheckData(Check check, CheckDto dto) {
        check.setDescription(dto.getDescription());
        check.setTotalSum(dto.getTotalSum());
        check.setDate(dto.getDate());
        return check;
    }
}
