package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Position;
import com.jomariabejo.motorph.repository.PositionRepository;

import java.util.List;

public class PositionService {

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Position getPositionById(Integer id) {
        return positionRepository.findById(id);
    }

    public Position getPositionByName(String positionName) {
        return positionRepository.findByPositionName(positionName);
    }

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public void savePosition(Position position) {
        positionRepository.save(position);
    }

    public void updatePosition(Position position) {
        positionRepository.update(position);
    }

    public void deletePosition(Position position) {
        positionRepository.delete(position);
    }
}
