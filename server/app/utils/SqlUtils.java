package utils;

import java.sql.Connection;
import java.sql.SQLException;

import play.db.DB;

public class SqlUtils {

	public void executeSqlLine(final String line) throws SQLException {
		final Connection connection = DB.getDataSource(DB.DEFAULT).getConnection();
		connection.setAutoCommit(true);
		connection.createStatement().execute(line);
	}

}
