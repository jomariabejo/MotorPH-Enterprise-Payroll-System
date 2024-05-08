package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Position;
import com.jomariabejo.motorph.repository.PositionRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PositionService {

    private final PositionRepository positionRepository;

    public PositionService() throws SQLException {
        // Establish database connection
        Connection connection = DatabaseConnectionUtility.getConnection();
        // Initialize repository with connection
        this.positionRepository = new PositionRepository(connection);
    }

    // CREATE operation
    public void createPosition(String name, String description) throws SQLException {
        Position position = new Position(name, null, description); // Assuming dateCreated will be set automatically
        positionRepository.create(position);
    }

    // GET operation by ID
    public Position getPosition(int positionID) throws SQLException {
        return positionRepository.get(positionID);
    }

    // GET operation for all positions

    public List<String> getPositionsName() throws SQLException {
        return positionRepository.getPositionsName();
    }

    // UPDATE operation
    public void updatePosition(int positionID, String name, String description) throws SQLException {
        Position position = positionRepository.get(positionID);
        if (position != null) {
            position.setName(name);
            position.setDescription(description);
            positionRepository.update(position);
        } else {
            System.out.println("Position not found.");
        }
    }

    // DELETE operation
    public void deletePosition(int positionID) throws SQLException {
        positionRepository.delete(positionID);
    }
}
