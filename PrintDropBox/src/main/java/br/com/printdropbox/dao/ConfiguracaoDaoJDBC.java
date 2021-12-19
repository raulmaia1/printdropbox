package br.com.printdropbox.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import br.com.printdropbox.bean.Configuracao;
import br.com.printdropbox.factory.SQLLiteFactoryJDBC;

public class ConfiguracaoDaoJDBC {

	private static final String SQL_SELECT_CONFIGURACAO = "select * from configuracao where id = 1";
	private static final String SQL_UPDATE_CONFIGURACAO = "UPDATE configuracao SET local_pasta = ?, impressora = ? WHERE id = 1";

	public Optional<Configuracao> getConfiguracao() {

		Connection conn = SQLLiteFactoryJDBC.getConexao().get();

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT_CONFIGURACAO);

			ResultSet resultSet = preparedStatement.executeQuery();
			
			resultSet.next();
			
			Configuracao cfg = new Configuracao();
			cfg.setID(resultSet.getInt("id"));
			cfg.setLocalPasta(resultSet.getString("local_pasta"));
			cfg.setImpressora(resultSet.getString("impressora"));
			
			conn.close();
			return Optional.ofNullable(cfg);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public void atualiza(String local, String impressora) {
		Connection conn = SQLLiteFactoryJDBC.getConexao().get();

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE_CONFIGURACAO);

			preparedStatement.setString(1, local);
			preparedStatement.setString(2, impressora);
			
			preparedStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
