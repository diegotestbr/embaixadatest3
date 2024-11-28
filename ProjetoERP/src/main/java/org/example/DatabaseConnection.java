package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/embaixada_br";
        private static final String USER = "root"; // Substitua pelo seu usuário do MySQL
        private static final String PASSWORD = "88888888"; // Substitua pela sua senha

        public static void main(String[] args) {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                if (conn != null) {
                    System.out.println("Conexão bem-sucedida com o banco de dados!");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            }
        }

        public static Connection connectorBanco() {

            return null;
        }
    }

