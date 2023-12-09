package br.frateu.joi.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    Dotenv dotenvConfig = Dotenv.configure().load();
    private final String url = dotenvConfig.get("DATABASE_URL");
    private final String user = dotenvConfig.get("DATABASE_USER");
    private final String password = dotenvConfig.get("DATABASE_PASSWORD");

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
}
