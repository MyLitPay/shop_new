package com.simbirsoft.service;

import com.simbirsoft.api.dto.CheckDto;
import com.simbirsoft.exception.CheckNotFoundException;
import com.simbirsoft.mapper.CheckMapper;
import com.simbirsoft.model.Check;
import com.simbirsoft.model.OperationType;
import com.simbirsoft.repo.CheckRepository;
import com.simbirsoft.service.impl.CheckServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckServiceTest {

    @Mock
    CheckRepository checkRepositoryMock;

    @InjectMocks
    CheckServiceImpl checkServiceImpl;

    private Check check1;
    private Check check2;
    private Check check3;
    private CheckDto checkDto1;
    private List<Check> checkList;
    private List<CheckDto> checkDtoList;

    @Before
    public void setup() {
        check1 = new Check(1L, "OAO", true, 100.0, new Date(1001001001));
        check2 = new Check(2L, "OOO", true, 200.0, new Date(1002002002));
        checkDto1 = CheckMapper.INSTANCE.toDTO(check1);
        checkList = new ArrayList<>();
        checkList.add(check1);
        checkList.add(check2);
        checkDtoList = checkList.stream().map(CheckMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Test
    public void getAllChecks_shouldReturnListOfCheckDto() {
        when(checkRepositoryMock.findAll()).thenReturn(checkList);

        List<CheckDto> actual = checkServiceImpl.getAllChecks();

        verify(checkRepositoryMock, times(1)).findAll();
        assertEquals(checkDtoList, actual);
    }

    @Test(expected = CheckNotFoundException.class)
    public void getAllChecks_shouldThrowExceptionIfNotFound() {
        when(checkRepositoryMock.findAll()).thenThrow(CheckNotFoundException.class);
        checkServiceImpl.getAllChecks();
        verify(checkRepositoryMock, times(0)).findAll();

    }

    @Test
    public void addCheck_shouldCreateCheckDto() {
        CheckDto newCheckDto = new CheckDto();
        Check newCheck = CheckMapper.INSTANCE.toEntity(newCheckDto);
        newCheck.setClosed(false);

        doAnswer(invocationOnMock -> {
            Check c = invocationOnMock.getArgument(0);
            c.setId(5L);
            return c;
        }).when(checkRepositoryMock.saveAndFlush(newCheck));

        CheckDto dto = checkServiceImpl.addCheck(new CheckDto());

        verify(checkRepositoryMock, times(1)).saveAndFlush(isA(Check.class));
        assertFalse(dto.getClosed());

    }

    @Test
    public void updateAllChecks_shouldReturnUpdatedCheckDtoList() {
        List<CheckDto> newDtoList = checkList.stream().map(CheckMapper.INSTANCE::toDTO).collect(Collectors.toList());
        for (CheckDto dto : newDtoList) {
            dto.setDescription("PAO");
        }
        List<Check> newList = newDtoList.stream().map(CheckMapper.INSTANCE::toEntity).collect(Collectors.toList());

        when(checkRepositoryMock.findById(1L)).thenReturn(Optional.of(check1));
        when(checkRepositoryMock.findById(2L)).thenReturn(Optional.of(check2));
        when(checkRepositoryMock.saveAll(newList)).thenReturn(newList);
        when(checkRepositoryMock.findAll()).thenReturn(newList);

        List<CheckDto> actual = checkServiceImpl.updateAllChecks(newDtoList);

        verify(checkRepositoryMock, times(2)).findById(anyLong());
        verify(checkRepositoryMock, times(1)).saveAll(anyList());
        verify(checkRepositoryMock, times(1)).findAll();
        assertEquals(newDtoList, actual);
    }

    @Test(expected = CheckNotFoundException.class)
    public void updateAllChecks_shouldThrowExceptionIfNotFound() {
        List<CheckDto> dtoList = new ArrayList<>();
        when(checkRepositoryMock.findById(anyLong())).thenThrow(CheckNotFoundException.class);

        checkServiceImpl.updateAllChecks(dtoList);
        verify(checkRepositoryMock, times(0)).findById(anyLong());
    }

    @Test
    public void deleteAllChecks_shouldBeEmptyChecklist() {
        when(checkRepositoryMock.findAll()).thenReturn(checkList);

        doAnswer(invocationOnMock -> {
            if (invocationOnMock.getArgument(0).equals(checkList)) {
                checkList.clear();
            }
            return null;
        }).when(checkRepositoryMock).deleteAll(anyList());

        checkServiceImpl.deleteAllChecks();

        verify(checkRepositoryMock, times(1)).findAll();
        verify(checkRepositoryMock, times(1)).deleteAll(anyList());
        assertEquals(0, checkList.size());
    }

    @Test(expected = CheckNotFoundException.class)
    public void deleteAllChecks_shouldThrowExceptionIfNotFound() {
        when(checkRepositoryMock.findAll()).thenThrow(CheckNotFoundException.class);
        checkServiceImpl.deleteAllChecks();
        verify(checkRepositoryMock, times(0)).findAll();

    }

    @Test
    public void getCheckById_shouldReturnCheckDto() {
        when(checkRepositoryMock.findById(1L)).thenReturn(Optional.of(check1));
        CheckDto dto = checkServiceImpl.getCheckById(1L);

        verify(checkRepositoryMock, times(1)).findById(anyLong());
        assertEquals(checkDto1, dto);
    }

    @Test(expected = CheckNotFoundException.class)
    public void getCheckById_shouldThrowExceptionIfNotFound() {
        when(checkRepositoryMock.findById(1L)).thenThrow(CheckNotFoundException.class);
        checkServiceImpl.getCheckById(1L);
        verify(checkRepositoryMock, times(0)).findById(anyLong());
    }

    @Test
    public void updateCheckById_shouldReturnUpdatedCheckDto() {
        CheckDto newDto = CheckMapper.INSTANCE.toDTO(check1);
        newDto.setDescription("PAO");

        when(checkRepositoryMock.findById(1L)).thenReturn(Optional.of(check1));
        when(checkRepositoryMock.saveAndFlush(check1)).thenReturn(check1);

        CheckDto actualDto = checkServiceImpl.updateCheckById(1L, newDto);
        verify(checkRepositoryMock, times(1)).findById(anyLong());
        verify(checkRepositoryMock, times(1)).saveAndFlush(isA(Check.class));
        assertEquals("PAO", actualDto.getDescription());

    }

    @Test(expected = CheckNotFoundException.class)
    public void updateCheckById_shouldThrowExceptionIfNotFound() {
        when(checkRepositoryMock.findById(1L)).thenThrow(CheckNotFoundException.class);
        checkServiceImpl.updateCheckById(1L, new CheckDto());
        verify(checkRepositoryMock, times(0)).findById(anyLong());
    }

    @Test
    public void deleteCheckById_shouldChangeCheckList() {
        when(checkRepositoryMock.findById(1L)).thenReturn(Optional.of(check1));

        doAnswer((invocation) -> {
            if (invocation.getArgument(0) == check1.getId()) {
                checkList.remove(check1);
            }
            return null;
        }).when(checkRepositoryMock).deleteById(anyLong());

        checkServiceImpl.deleteCheckById(1L);

        verify(checkRepositoryMock, times(1)).findById(anyLong());
        assertEquals(1, checkList.size());
    }

    @Test(expected = CheckNotFoundException.class)
    public void deleteCheckById_shouldThrowExceptionIfNotFound() {
        when(checkRepositoryMock.findById(1L)).thenThrow(CheckNotFoundException.class);
        checkServiceImpl.deleteCheckById(1L);
        verify(checkRepositoryMock, times(0)).findById(anyLong());
    }

    @Test
    public void findCheckById_shouldReturnCheck() {
        when(checkRepositoryMock.findById(1L)).thenReturn(Optional.of(check1));
        Check actual = checkServiceImpl.findCheckById(1L);
        verify(checkRepositoryMock, times(1)).findById(anyLong());
        assertEquals(check1, actual);
    }

    @Test(expected = CheckNotFoundException.class)
    public void findCheckById_shouldThrowExceptionIfNotFound() {
        when(checkRepositoryMock.findById(1L)).thenThrow(CheckNotFoundException.class);
        checkServiceImpl.findCheckById(1L);
        verify(checkRepositoryMock, times(0)).findById(anyLong());
    }

    @Test
    public void findClosedChecks_shouldReturnListOfClosedChecks() {
        when(checkRepositoryMock.findClosedChecks(OperationType.SELLING.name())).thenReturn(checkList);
        List<Check> actual = checkServiceImpl.findClosedChecks(OperationType.SELLING);
        verify(checkRepositoryMock, times(1)).findClosedChecks(anyString());
        assertEquals(checkList, actual);
    }

    @Test(expected = CheckNotFoundException.class)
    public void findClosedChecks_shouldThrowExceptionIfNotFound() {
        when(checkRepositoryMock.findClosedChecks(OperationType.SELLING.name())).thenReturn(List.of());
        checkServiceImpl.findClosedChecks(OperationType.SELLING);
        verify(checkRepositoryMock, times(1)).findClosedChecks(anyString());
    }

    @Test
    public void findClosedChecksByDateBetween_shouldReturnListOfClosedChecks() {
        String dateFrom = "2000-03-12";
        String dateTo = "2021-07-01";
        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);

        when(checkRepositoryMock.findClosedChecksByDateBetween(
                OperationType.SELLING.name(), from, to))
                .thenReturn(List.of(check1));

        List<Check> actual = checkServiceImpl
                .findClosedChecksByDateBetween(OperationType.SELLING, dateFrom, dateTo);

//        verify(checkRepositoryMock, times(anyInt()))
//                .findClosedChecksByDateBetween(anyString(), LocalDate.parse(anyString()), LocalDate.parse(anyString()));
        assertEquals(List.of(check1), actual);
    }

    @Test(expected = CheckNotFoundException.class)
    public void findClosedChecksByDateBetween_shouldThrowExceptionIfNotFound() {
        String dateFrom = "2000-03-12";
        String dateTo = "2021-07-01";

        when(checkRepositoryMock.findClosedChecksByDateBetween(
                OperationType.SELLING.name(), LocalDate.now(), LocalDate.now()))
                .thenThrow(CheckNotFoundException.class);

        checkServiceImpl.findClosedChecksByDateBetween(OperationType.SELLING, dateFrom, dateTo);

//        verify(checkRepositoryMock, times(anyInt()))
//                .findClosedChecksByDateBetween(anyString(), LocalDate.parse(anyString()), LocalDate.parse(anyString()));
    }

}
