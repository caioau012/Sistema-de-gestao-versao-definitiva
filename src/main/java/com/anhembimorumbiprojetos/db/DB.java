package com.anhembimorumbiprojetos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	private static Connection conn = null;
	
	private static final String URL = "jdbc:mysql://localhost:3306/sistema_gerenciamento";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456789";
    
    public static Connection getConnection() {
        if (conn == null || isClosed(conn)) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                Properties props = new Properties();
                props.setProperty("user", USERNAME);
                props.setProperty("password", PASSWORD);
                props.setProperty("useSSL", "false");
                props.setProperty("serverTimezone", "UTC");
                props.setProperty("allowPublicKeyRetrieval", "true");

                conn = DriverManager.getConnection(URL, props);
                System.out.println("✅ Conexão com MySQL estabelecida!");

            } catch (ClassNotFoundException | SQLException e) {
                throw new DbException("Erro ao conectar: " + e.getMessage());
            }
        }
        return conn;
    }
	
	public static void closeConnection() {
		 try {
	            if (conn != null && !conn.isClosed()) {
	                conn.close();
	                System.out.println("✅ Conexão com MySQL fechada!");
	            }
	        } catch (SQLException e) {
	            System.err.println("❌ Erro ao fechar conexão: " + e.getMessage());
	        }
	}
	
	public static boolean testarConexao() throws SQLException {
        getConnection();
		return true;
    }
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar Statement: " + e.getMessage());
            }
        }
    }
	
	private static boolean isClosed(Connection conn) {
	    try {
	        return conn == null || conn.isClosed();
	    } catch (SQLException e) {
	        return true;
	    }
	}
	
}
