package org.example;

import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexao {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.connectorBanco()) {
            if (conn != null) {
                System.out.println("Conexão bem-sucedida com o banco de dados!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}

