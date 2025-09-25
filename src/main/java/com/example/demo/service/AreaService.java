package com.example.demo.service;

import com.example.demo.dto.AreaDTO;
import java.util.List;

public interface AreaService {
    List<AreaDTO> getAllAreas();
    AreaDTO getAreaById(Long id);
    AreaDTO createArea(AreaDTO areaDTO);
    AreaDTO updateArea(Long id, AreaDTO areaDTO);
    void deleteArea(Long id);
}
