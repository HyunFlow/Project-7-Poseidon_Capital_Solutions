package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.repositories.CurvePointRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CurveService {

    private final CurvePointRepository curvePointRepository;

    @Transactional
    public void createCurvePoint(CurvePointDto request) {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setCurveId(request.getCurveId());
        curvePoint.setTerm(request.getTerm());
        curvePoint.setValue(request.getValue());

        curvePointRepository.save(curvePoint);
    }

    @Transactional
    public void updateCurvePoint(Integer id, CurvePointDto request) {
        CurvePoint currentCurveP = curvePointRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Curve Point not found"));

        currentCurveP.setCurveId(request.getCurveId());
        currentCurveP.setTerm(request.getTerm());
        currentCurveP.setValue(request.getValue());

        curvePointRepository.save(currentCurveP);
    }

    @Transactional
    public void deleteCurvePoint(Integer id) {
        if (!curvePointRepository.existsById(id)) {
            throw new IllegalArgumentException("Curve Point not found");
        }
        curvePointRepository.deleteById(id);
    }

    public List<CurvePointDto> loadAllCurvePoints() {
        return curvePointRepository.findAll().stream().map(curvePoint -> {
            CurvePointDto dto = new CurvePointDto();
            dto.setId(curvePoint.getId());
            dto.setCurveId(curvePoint.getCurveId());
            dto.setTerm(curvePoint.getTerm());
            dto.setValue(curvePoint.getValue());
            return dto;
        }).toList();
    }

    public CurvePointDto loadCurvePointById (Integer id) {
        CurvePoint currentPoint = curvePointRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Curve Point not found"));

        CurvePointDto dto = new CurvePointDto();
        dto.setId(currentPoint.getId());
        dto.setCurveId(currentPoint.getCurveId());
        dto.setTerm(currentPoint.getTerm());
        dto.setValue(currentPoint.getValue());

        return dto;
    }
}
