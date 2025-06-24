package org.example.Exercicio005;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TExercicio005 {

        private Connection connection;

        public User findUserByEmailVulneravel(String email) throws SQLException {
            String sql = "SELECT * FROM users WHERE email = '" + email + "'";
            // Vulnerável a: admin@test.com' OR '1'='1' --
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // ... processamento
            return user;
        }
    }

    // ✅ SEGURO - Usando PreparedStatement
    public class UserDAOSeguro {
        private Connection connection;

        public User findUserByEmail(String email) throws SQLException {
            String sql = "SELECT id, name, email, address, phone FROM users WHERE email = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, email);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new User(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("address"),
                                rs.getString("phone")
                        );
                    }
                }
            }
            return null;
        }

        public List<User> searchUsers(String nameFilter, String cityFilter) throws SQLException {
            StringBuilder sql = new StringBuilder("SELECT id, name, email, address, phone FROM users WHERE 1=1");
            List<Object> parameters = new ArrayList<>();

            if (nameFilter != null && !nameFilter.trim().isEmpty()) {
                sql.append(" AND name LIKE ?");
                parameters.add("%" + nameFilter + "%");
            }

            if (cityFilter != null && !cityFilter.trim().isEmpty()) {
                sql.append(" AND address LIKE ?");
                parameters.add("%" + cityFilter + "%");
            }

            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
                // Definir parâmetros dinamicamente
                for (int i = 0; i < parameters.size(); i++) {
                    pstmt.setObject(i + 1, parameters.get(i));
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    List<User> users = new ArrayList<>();
                    while (rs.next()) {
                        users.add(new User(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("address"),
                                rs.getString("phone")
                        ));
                    }
                    return users;
                }
            }
        }
    }



