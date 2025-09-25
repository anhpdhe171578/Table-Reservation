package com.example.demo.service.impl;

import com.example.demo.dto.TableDTO;
import com.example.demo.entity.TableEntity;
import com.example.demo.repository.TableRepository;
import com.example.demo.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {

    @Autowired
    private TableRepository tableRepository;

    @Override
    public List<TableDTO> getAllTables() {
        return tableRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TableDTO getTableById(Long id) {
        return tableRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public TableDTO createTable(TableDTO dto) {
        TableEntity table = toEntity(dto);
        table.setCreatedAt(LocalDateTime.now());
        table = tableRepository.save(table);
        return toDTO(table);
    }

    @Override
    public TableDTO updateTable(Long id, TableDTO dto) {
        return tableRepository.findById(id).map(t -> {
            t.setTableNumber(dto.getTableNumber());
            t.setStatus(dto.getStatus());
            t.setAreaID(dto.getAreaID());
            t.setNumberOfDesk(dto.getNumberOfDesk());
            t.setUpdatedAt(LocalDateTime.now());
            return toDTO(tableRepository.save(t));
        }).orElse(null);
    }

    @Override
    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }

    private TableDTO toDTO(TableEntity t) {
        return new TableDTO(
                t.getTableID(),
                t.getTableNumber(),
                t.getStatus(),
                t.getAreaID(),
                t.getCreatedAt(),
                t.getUpdatedAt(),
                t.getNumberOfDesk()
        );
    }

    private TableEntity toEntity(TableDTO dto) {
        TableEntity t = new TableEntity();
        t.setTableNumber(dto.getTableNumber());
        t.setStatus(dto.getStatus());
        t.setAreaID(dto.getAreaID());
        t.setNumberOfDesk(dto.getNumberOfDesk());
        return t;
    }
}
