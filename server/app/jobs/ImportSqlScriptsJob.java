package jobs;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import play.Logger;
import play.db.DB;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class ImportSqlScriptsJob extends Job {

	private static final String DIRNAME = "sql-import";
	private static final String FILENAME = "sql-scripts-to-import.sql";

	@Override
	public void doJob() {
		Logger.info("Current directory : " + new File(".").getAbsolutePath());

		final File file = new File(DIRNAME, FILENAME);
		if (!file.exists()) {
			Logger.info("File '" + file.getAbsolutePath() + "' doest not exist : no SQL scripts to import.");
			return;
		}

		Logger.info("File '" + file.getAbsolutePath() + "' exists : try to import SQL scripts...");
		try {
			importScripts(file);
			file.renameTo(new File(DIRNAME, FILENAME + ".imported"));
		} catch (final Exception e) {
			Logger.error(e, "Error importing SQL scripts");
		}
	}

	private void importScripts(final File file) throws IOException, SQLException {
		final List<String> lines = FileUtils.readLines(file);
		for (final String line : lines) {
			if (isToBeImported(line)) {
				final String shortline = line.substring(0, Math.min(50, line.length()));
				Logger.info(". import : " + shortline);
				executeSql(line);
			}
		}
	}

	private boolean isToBeImported(final String line) {
		return !isEmpty(line) && !isComment(line) && !isSqlCommandToIgnore(line) && !isDataToIgnore(line);
	}

	private boolean isEmpty(final String line) {
		return line.trim().isEmpty();
	}

	private boolean isComment(final String line) {
		return line.startsWith("--") || line.startsWith("/*");
	}

	private boolean isSqlCommandToIgnore(final String line) {
		return line.startsWith("LOCK TABLES") || line.startsWith("UNLOCK TABLES");
	}

	private boolean isDataToIgnore(final String line) {
		return line.contains("play_evolutions");
	}

	private void executeSql(final String line) throws SQLException {
		final Connection connection = DB.getDataSource(DB.DEFAULT).getConnection();
		connection.setAutoCommit(true);
		connection.createStatement().execute(adaptSql(line));
	}

	private String adaptSql(final String line) {
		return line.replace("`", "").replace("\\'", "''");
	}
}
