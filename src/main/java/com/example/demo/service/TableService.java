package com.example.demo.service;

import com.example.demo.dto.TableDTO;
import java.util.List;

public interface TableService {
    List<TableDTO> getAllTables();
    TableDTO getTableById(Long id);
    TableDTO createTable(TableDTO tableDTO);
    TableDTO updateTable(Long id, TableDTO tableDTO);
    void deleteTable(Long id);
}
