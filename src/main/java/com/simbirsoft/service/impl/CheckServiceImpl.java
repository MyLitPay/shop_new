package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.CheckDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.exception.CancellationNotFoundException;
import com.simbirsoft.exception.CheckNotFoundException;
import com.simbirsoft.exception.IncorrectDateException;
import com.simbirsoft.mapper.CheckMapper;
import com.simbirsoft.model.Check;
import com.simbirsoft.model.OperationType;
import com.simbirsoft.repo.CheckRepository;
import com.simbirsoft.service.CheckService;
import liquibase.pro.packaged.D;
import org.springframework.format.datetime.joda.LocalDateParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
        List<CheckDto> list = checkRepository.findAll().stream()
                .map(CheckMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new CheckNotFoundException();
        }
        return list;
    }

    @Override
    public CheckDto addCheck(CheckDto dto) {
        Check check = CheckMapper.INSTANCE.toEntity(dto);
        check.setClosed(false);
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
    public void deleteAllChecks() {
        List<Check> checks = checkRepository.findAll();
        checkRepository.deleteAll(checks);
        if (checks.isEmpty()) {
            throw new CheckNotFoundException();
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
    public void deleteCheckById(Long id) {
        findCheckById(id);
        checkRepository.deleteById(id);
    }

    @Override
    public Check findCheckById(long id) {
        return checkRepository.findById(id)
                .orElseThrow(CheckNotFoundException::new);
    }

    @Override
    public List<Check> findClosedChecks(OperationType operationType) {
        String type = operationType.name();
        List<Check> checkList = checkRepository.findClosedChecks(type);
        if (checkList == null || checkList.isEmpty()) {
            throw new CheckNotFoundException();
        }
        return checkList;
    }

    @Override
    public List<Check> findClosedChecksByDateBetween(OperationType operationType,
                                                     String dateFrom, String dateTo) {
        String type = operationType.name();
        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);
        List<Check> checkList = checkRepository.findClosedChecksByDateBetween(type, from, to);
        if (checkList == null || checkList.isEmpty()) {
            throw new CheckNotFoundException();
        }
        return checkList;
    }

    private Check updateCheckData(Check check, CheckDto dto) {
        check.setDescription(dto.getDescription());
        check.setTotalSum(dto.getTotalSum());
        check.setDate(dto.getDate());
        check.setClosed(dto.getClosed());
        return check;
    }
}
