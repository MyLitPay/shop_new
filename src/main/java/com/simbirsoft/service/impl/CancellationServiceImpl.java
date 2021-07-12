package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.CancellationDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.exception.CancellationNotFoundException;
import com.simbirsoft.mapper.CancellationMapper;
import com.simbirsoft.model.Cancellation;
import com.simbirsoft.repo.CancellationRepository;
import com.simbirsoft.service.CancellationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CancellationServiceImpl implements CancellationService {
    final CancellationRepository cancellationRepository;

    public CancellationServiceImpl(CancellationRepository cancellationRepository) {
        this.cancellationRepository = cancellationRepository;
    }

    @Override
    public List<CancellationDto> getAllCancellations() {
        return cancellationRepository.findAll().stream()
                .map(CancellationMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CancellationDto addCancellation(CancellationDto dto) {
        Cancellation cancellation = CancellationMapper.INSTANCE.toEntity(dto);
        Cancellation cancellationFromDB = cancellationRepository.saveAndFlush(cancellation);
        return CancellationMapper.INSTANCE.toDTO(cancellationFromDB);
    }

    @Override
    public List<CancellationDto> updateAllCancellations(List<CancellationDto> request) {
        List<Cancellation> cancellationList = new ArrayList<>();

        for (CancellationDto dto : request) {
            Cancellation cancellation = updateCancellationData(findCancellationById(dto.getId()), dto);
            cancellationList.add(cancellation);
        }
        cancellationRepository.saveAll(cancellationList);
        return getAllCancellations();
    }

    @Override
    public ResultResponse deleteAllCancellations() {
        try {
            List<Cancellation> cancellations = cancellationRepository.findAll();
            cancellationRepository.deleteAll(cancellations);
            return new ResultResponse(ResultResponseType.OK);
        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
        }
    }

    @Override
    public CancellationDto getCancellationById(Long id) {
        Cancellation cancellation = findCancellationById(id);
        return CancellationMapper.INSTANCE.toDTO(cancellation);
    }

    @Override
    public CancellationDto updateCancellationById(Long id, CancellationDto dto) {
        Cancellation cancellation = updateCancellationData(findCancellationById(id), dto);
        Cancellation cancellationFromDB = cancellationRepository.saveAndFlush(cancellation);
        return CancellationMapper.INSTANCE.toDTO(cancellationFromDB);
    }

    @Override
    public ResultResponse deleteCancellationById(Long id) {
        try {
            cancellationRepository.deleteById(id);
            return new ResultResponse(ResultResponseType.OK);
        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
        }
    }

    public Cancellation findCancellationById(long id) {
        return cancellationRepository.findById(id)
                .orElseThrow(() -> new CancellationNotFoundException("Cancellation not found"));
    }

    private Cancellation updateCancellationData(Cancellation cancellation, CancellationDto dto) {
        cancellation.setReason(dto.getReason());
        cancellation.setDate(dto.getDate());
        return cancellation;
    }
}
