package br.com.printdropbox.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class SQLLiteFactoryJDBC {

	static Optional<Connection> conn = Optional.empty();
	{
		
	}
	public static Optional<Connection> getConexao() {
		try {
			
			conn = Optional.ofNullable(
					DriverManager.getConnection("jdbc:sqlite:C:\\banco\\print.db"));
					//DriverManager.getConnection("jdbc:sqlite:/home/edu/banco/print.db"));
			
		} catch ( SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public static void main(String[] args) {
		SQLLiteFactoryJDBC.getConexao().ifPresent((con) ->{
			try {
				System.out.println(con.isClosed());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
	}
}
