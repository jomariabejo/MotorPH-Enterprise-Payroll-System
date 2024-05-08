package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.entity.Position;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PositionRepository {

    private final Connection connection;

    public PositionRepository(Connection connection) {
        this.connection = connection;
    }

    // CREATE operation
    public void create(Position position) throws SQLException {
        String query = "INSERT INTO position (name, date_created, description) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, position.getName());
            statement.setTimestamp(2, position.getDateCreated());
            statement.setString(3, position.getDescription());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int positionID = resultSet.getInt(1);
                position.setPositionID(positionID);
            }
        }
    }

    // GET operation by ID
    public Position get(int positionID) throws SQLException {
        String query = "SELECT * FROM position WHERE position_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, positionID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractPosition(resultSet);
            }
            return null;
        }
    }

    // GET operation for all positions
    public List<Position> getAll() throws SQLException {
        List<Position> positions = new ArrayList<>();
        String query = "SELECT * FROM position";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                positions.add(extractPosition(resultSet));
            }
        }
        return positions;
    }

    // UPDATE operation
    public void update(Position position) throws SQLException {
        String query = "UPDATE position SET name = ?, date_created = ?, description = ? WHERE position_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, position.getName());
            statement.setTimestamp(2, position.getDateCreated());
            statement.setString(3, position.getDescription());
            statement.setInt(4, position.getPositionID());
            statement.executeUpdate();
        }
    }

    // DELETE operation
    public void delete(int positionID) throws SQLException {
        String query = "DELETE FROM position WHERE position_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, positionID);
            statement.executeUpdate();
        }
    }

    // Helper method to extract Position object from ResultSet
    private Position extractPosition(ResultSet resultSet) throws SQLException {
        int positionID = resultSet.getInt("position_id");
        String name = resultSet.getString("name");
        Timestamp dateCreated = resultSet.getTimestamp("date_created");
        String description = resultSet.getString("description");
        return new Position(positionID, name, dateCreated, description);
    }

    public List<String> getPositionsName() throws SQLException {
        List<String> positionsNameList = new ArrayList<>();
        String query = "SELECT name FROM position";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                positionsNameList.add(resultSet.getString("name"));
            }
        }
        return positionsNameList;
    }
}
