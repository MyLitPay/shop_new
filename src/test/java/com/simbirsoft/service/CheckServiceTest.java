package com.simbirsoft.service;

import com.simbirsoft.api.dto.CheckDto;
import com.simbirsoft.exception.CheckNotFoundException;
import com.simbirsoft.mapper.CheckMapper;
import com.simbirsoft.model.Check;
import com.simbirsoft.repo.CheckRepository;
import com.simbirsoft.service.impl.CheckServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckServiceTest {

    @Mock
    CheckRepository checkRepositoryMock;

    @InjectMocks
    CheckServiceImpl checkServiceImpl;

    private static Check check1;
    private static Check check2;
    private static CheckDto checkDto1;
    private static List<Check> checkList;
    private static List<CheckDto> checkDtoList;

    @BeforeClass
    public static void setup() {
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
        assertEquals(checkDtoList, checkServiceImpl.getAllChecks());
    }

    @Test(expected = CheckNotFoundException.class)
    public void getAllChecks_shouldThrowExceptionIfNotFound() {
        checkServiceImpl.getAllChecks();
    }

    @Test
    public void addCheck_shouldReturnCheckDto() {
        when(checkRepositoryMock.saveAndFlush(new Check())).thenReturn(check1);
        assertEquals(checkDto1, checkServiceImpl.addCheck(new CheckDto()));
    }

    @Test
    public void updateAllChecks_shouldReturnCheckDtoList() {
        when(checkRepositoryMock.saveAll(checkList)).thenReturn(checkList);
        when(checkRepositoryMock.findAll()).thenReturn(checkList);
        when(checkRepositoryMock.findById(1L)).thenReturn(Optional.of(check1));
        when(checkRepositoryMock.findById(2L)).thenReturn(Optional.of(check2));
        assertEquals(checkDtoList, checkServiceImpl.updateAllChecks(checkDtoList));
    }

    @Test(expected = CheckNotFoundException.class)
    public void updateAllChecks_shouldThrowExceptionIfNotFound() {
        checkServiceImpl.updateAllChecks(checkDtoList);
    }

    @Test
    public void deleteAllChecks_shouldBeEmptyChecklist() {
        when(checkRepositoryMock.findAll()).thenReturn(checkList);
        doAnswer(invocationOnMock -> {
            checkList.clear();
            return null;
        }).when(checkRepositoryMock).deleteAll(checkList);
        checkServiceImpl.deleteAllChecks();
        assertEquals(0, checkList.size());
    }

    @Test(expected = CheckNotFoundException.class)
    public void deleteAllChecks_shouldThrowExceptionIfNotFound() {
        checkServiceImpl.deleteAllChecks();
    }

    @Test
    public void getCheckById_shouldReturnCheckDto() {
        when(checkRepositoryMock.findById(1L)).thenReturn(Optional.of(check1));
        assertEquals(checkDto1, checkServiceImpl.getCheckById(1L));
    }

    @Test(expected = CheckNotFoundException.class)
    public void getCheckById_shouldThrowExceptionIfNotFound() {
        checkServiceImpl.getCheckById(1L);
    }

    @Test
    public void updateCheckById_shouldReturnCheckDto() {
        when(checkRepositoryMock.findById(1L)).thenReturn(Optional.of(check1));
        when(checkRepositoryMock.saveAndFlush(check1)).thenReturn(check1);
        assertEquals(checkDto1, checkServiceImpl.updateCheckById(1L, checkDto1));
    }

    @Test(expected = CheckNotFoundException.class)
    public void updateCheckById_shouldThrowExceptionIfNotFound() {
        checkServiceImpl.updateCheckById(1L, checkDto1);
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
        assertEquals(1, checkList.size());
    }

    @Test(expected = CheckNotFoundException.class)
    public void deleteCheckById_shouldThrowExceptionIfNotFound() {
        checkServiceImpl.deleteCheckById(1L);

    }

}
