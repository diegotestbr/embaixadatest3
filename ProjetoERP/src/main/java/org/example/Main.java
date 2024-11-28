package org.example;// CLASSE PRINCIPAL
import java.sql.*;
import java.sql.Date;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        try (Connection conn = conectarBanco()) {
            Scanner scanner = new Scanner(System.in);


            while (true) {
                System.out.println("Escolha uma opção:");
                System.out.println("1. Cadastrar Cidadão");
                System.out.println("2. Agendar Serviço");
                System.out.println("3. Gerar Relatório de Serviços");
                System.out.println("4. Sair");


                int opcao = scanner.nextInt();
                scanner.nextLine();  // Consome a nova linha


                switch (opcao) {
                    case 1:
                        cadastrarCidadao(conn, scanner);
                        break;
                    case 2:
                        agendarServico(conn, scanner);
                        break;
                    case 3:
                        gerarRelatorioServicos(conn);
                        break;
                    case 4:
                        System.out.println("Saindo...");
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection conectarBanco() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/embaixada_br";
        String user = "root";
        String password = "8888888"; // Substitua pela sua senha
        return DriverManager.getConnection(url, user, password);
    }

    private static void cadastrarCidadao(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Digite o nome do cidadão:");
        String nome = scanner.nextLine();


        System.out.println("Digite o documento do cidadão:");
        String documento = scanner.nextLine();


        System.out.println("Digite o status do cidadão:");
        String status = scanner.nextLine();


        Cidadao cidadao = new Cidadao(0, nome, documento, status);
        Cidadao.cadastrarCidadao(conn, cidadao);
    }


    private static void agendarServico(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Digite o ID do cidadão para o agendamento:");
        int cidadaoId = scanner.nextInt();
        scanner.nextLine(); // Consome a nova linha


        System.out.println("Digite o tipo de serviço:");
        String tipoServico = scanner.nextLine();


        System.out.println("Digite a data do serviço (yyyy-mm-dd):");
        String dataString = scanner.nextLine();
        Date data = Date.valueOf(dataString);


        Servico servico = new Servico(0, tipoServico, data, cidadaoId);
        Servico.agendarServico(conn, servico);
    }

    private static void gerarRelatorioServicos(Connection conn) throws SQLException {
        Relatorio.gerarRelatorioServicos(conn);
    }
}




//CLASSE CIDADÃO


class Cidadao {
    private final int id;
    private final String nome;
    private final String documento;
    private final String status;

    public Cidadao(int id, String nome, String documento, String status) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public String getStatus() {
        return status;
    }

    public static void cadastrarCidadao(Connection conn, Cidadao cidadao) throws SQLException {
        String sql = "INSERT INTO cidadao (id, nome, documento, status) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, cidadao.getId());
        stmt.setString(2, cidadao.getNome());
        stmt.setString(3, cidadao.getDocumento());
        stmt.setString(4, cidadao.getStatus());

        stmt.executeUpdate();
        System.out.println("Cidadão cadastrado com sucesso! ID: " + cidadao.getId());
    }

    public static void atualizarCidadao(Connection conn, Cidadao cidadao) throws SQLException {
        String sql = "UPDATE cidadao SET nome = ? WHERE id = ?;";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, cidadao.getNome());
        stmt.setInt(2, cidadao.getId());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Cidadão atualizado com sucesso! ID: " + cidadao.getId());
        } else {
            System.out.println("Cidadão não encontrado. ID: " + cidadao.getId());
        }
    }

    public static void deletarCidadao(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM cidadao WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);


        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Cidadão deletado com sucesso! ID: " + id);
        } else {
            System.out.println("Cidadão não encontrado. ID: " + id);
        }
    }
}



//CLASSE SERVIÇO


 class Servico {
    private int id;
    private final String tipoServico;
    private final Date data;
    private final int cidadaoId;

    public Servico(int id, String tipoServico, Date data, int cidadaoId) {
        this.id = id;
        this.tipoServico = tipoServico;
        this.data = data;
        this.cidadaoId = cidadaoId;
    }

    public int getId() { return id; }
    public String getTipoServico() { return tipoServico; }
    public Date getData() { return data; }
    public int getCidadaoId() { return cidadaoId; }

    public static void agendarServico(Connection conn, Servico servico) throws SQLException {
        String sql = "INSERT INTO servico (tipo_servico, data, cidadao_id) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, servico.getTipoServico());
        stmt.setDate(2, new java.sql.Date(servico.getData().getTime()));
        stmt.setInt(3, servico.getCidadaoId());


        stmt.executeUpdate();
        System.out.println("Serviço agendado com sucesso!");
    }
}

//CLASSE RELATÓRIO


class Relatorio {

    public static void gerarRelatorioServicos(Connection conn) throws SQLException {
        String sql = "SELECT s.id, s.tipo_servico, s.data, c.nome FROM servico s JOIN cidadao c ON s.cidadao_id = c.id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("Relatório de Serviços Agendados:");
        while (rs.next()) {
            int id = rs.getInt("id");
            String tipoServico = rs.getString("tipo_servico");
            Date data = rs.getDate("data");
            String nomeCidadao = rs.getString("nome");

            System.out.println("ID: " + id + ", Serviço: " + tipoServico + ", Data: " + data + ", Cidadão: " + nomeCidadao);
        }
    }
}