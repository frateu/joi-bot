package br.frateu.joi.database;

import br.frateu.joi.economia.GastoFixo;

import java.sql.*;

public class EconomiaDB {
    public long inserirGastoFixo(GastoFixo gastoFixo) {
        String SQL = "INSERT INTO gasto_fixo(usuario, descricao, valor) VALUES(?, ?, ?)";

        long id = 0;

        try (Connection conn = new Conexao().connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)){

            pstmt.setString(1, gastoFixo.getUsuario());
            pstmt.setString(2, gastoFixo.getDescricao());
            pstmt.setBigDecimal(3, gastoFixo.getValor());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id;
    }
}
