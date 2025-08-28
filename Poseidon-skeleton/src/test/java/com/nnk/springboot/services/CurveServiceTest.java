package com.nnk.springboot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.repositories.CurvePointRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CurveServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurveService curveService;

    @Test
    void createCurvePoint_savesEntityWithRequest() {
        // given
        CurvePointDto request = new CurvePointDto();
        request.setCurveId(1);
        request.setTerm(5.6);
        request.setValue(3.1);

        //when
        curveService.createCurvePoint(request);

        //then
        ArgumentCaptor<CurvePoint> captor = ArgumentCaptor.forClass(CurvePoint.class);
        verify(curvePointRepository).save(captor.capture());
        verifyNoMoreInteractions(curvePointRepository);

        CurvePoint saved = captor.getValue();
        assertEquals(1, saved.getCurveId());
        assertEquals(5.6, saved.getTerm(), 0.0001);
        assertEquals(3.1, saved.getValue(), 0.0001);
    }

    @Test
    void updateCurvePoint_updatesFoundEntityAndSaves() {
        // given
        Integer id = 1;

        CurvePoint existing = new CurvePoint();
        existing.setId(1);
        existing.setCurveId(9);
        existing.setTerm(1.0);
        existing.setValue(2.0);

        when(curvePointRepository.findById(id)).thenReturn(Optional.of(existing));

        CurvePointDto request = new CurvePointDto();
        request.setCurveId(4);
        request.setTerm(5.6);
        request.setValue(3.1);

        // when
        curveService.updateCurvePoint(id, request);

        // then
        ArgumentCaptor<CurvePoint> captor = ArgumentCaptor.forClass(CurvePoint.class);
        verify(curvePointRepository).findById(id);
        verify(curvePointRepository).save(captor.capture());
        verifyNoMoreInteractions(curvePointRepository);

        CurvePoint saved = captor.getValue();
        assertEquals(4, saved.getCurveId());
        assertEquals(5.6, saved.getTerm(), 0.0001);
        assertEquals(3.1, saved.getValue(), 0.0001);
    }

    @Test
    void updateCurvePoint_throws_whenNotFound() {
        // given
        Integer id = 1;
        when(curvePointRepository.findById(id)).thenReturn(Optional.empty());

        CurvePointDto request = new CurvePointDto();
        request.setCurveId(4);
        request.setTerm(5.6);
        request.setValue(3.1);

        // when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> curveService.updateCurvePoint(id, request));
        assertEquals("Curve Point not found", ex.getMessage());

        // then
        verify(curvePointRepository).findById(id);
        verifyNoMoreInteractions(curvePointRepository);
    }

    @Test
    void deleteCurvePoint_deletes_whenExists() {
        // when
        when(curvePointRepository.existsById(1)).thenReturn(true);
        curveService.deleteCurvePoint(1);

        // then
        verify(curvePointRepository).existsById(1);
        verify(curvePointRepository).deleteById(1);
    }

    @Test
    void deleteCurvePoint_throws_whenNotFound() {
        // when
        when(curvePointRepository.existsById(1)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> curveService.deleteCurvePoint(1));

        // then
        verify(curvePointRepository).existsById(1);
        verifyNoMoreInteractions(curvePointRepository);
    }
}
