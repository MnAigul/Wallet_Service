package domain.dao;


import domain.model.*;
import infrastructure.DBUtils;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AuditDAO {

    private Connection connection = null;

    public void save(Long player_id, AuditType auditType, EventType eventType) throws IOException {
        Audit audit = new Audit(player_id, auditType, eventType);
        try {
            connection = DBUtils.getConnection();
            String insertDataSQL = "INSERT INTO my_schema.audit (player_id, audit_type, event_type) VALUES (?, ?, ?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setLong(1, player_id);
            insertDataStatement.setString(2, auditType.name());
            insertDataStatement.setString(3, eventType.name());
            insertDataStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Ошибка при закрытии соединения: " + closeException.getMessage());
            }
        }
    }

    public List<Audit> findByPlayerEmail(String email) {
        List<Audit> audits = new LinkedList<>();
        try {
            connection = DBUtils.getConnection();
            String selectDataSQL = "SELECT a.id, a.player_id, a.audit_type, a.event_type, a.time FROM my_schema.audit a INNER JOIN my_schema.players p ON a.player_id = p.id WHERE p.email = '" + email + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectDataSQL);
            while (resultSet.next()) {
                Audit audit = new Audit();
                audit.setId(resultSet.getLong("id"));
                audit.setPlayerId(resultSet.getLong("player_id"));
                audit.setAuditType(AuditType.valueOf(resultSet.getString("audit_type")));
                audit.setEventType(EventType.valueOf(resultSet.getString("event_type")));
                audit.setTime(resultSet.getTime("time"));
                audits.add(audit);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Ошибка при закрытии соединения: " + closeException.getMessage());
            }
        }
        return audits;
    }

    public List<String> findAllPlayersEmails() {
        List<String> emails = new LinkedList<>();
        try {
            connection = DBUtils.getConnection();
            String selectDataSQL = "SELECT p.email FROM my_schema.players p WHERE p.email != 'admin'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectDataSQL);
            while (resultSet.next()) {
                String email = resultSet.getString("email");
                emails.add(email);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                System.err.println("Ошибка при закрытии соединения: " + closeException.getMessage());
            }
        }
        return emails;
    }
}
