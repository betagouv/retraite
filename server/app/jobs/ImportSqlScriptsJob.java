package jobs;

import java.io.File;
import java.io.FileFilter;
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

	private static final String DIRNAME = "sql-imports";

	@Override
	public void doJob() {

		final File dirSqlImport = new File(DIRNAME);
		final File[] files = dirSqlImport.listFiles(new SqlFileFilter());
		if (files == null || files.length == 0) {
			Logger.info("No .sql files in '" + dirSqlImport.getAbsolutePath() + "' : no SQL scripts to import.");
			return;
		}

		Logger.info("There " + (files.length == 1 ? "is" : "are") + " " + files.length + " file" + (files.length == 1 ? "" : "s") + " in '"
				+ dirSqlImport.getAbsolutePath() + "' : try to import SQL scripts...");
		for (final File file : files) {
			Logger.info("- Process file " + file.getAbsolutePath() + " ...");
			try {
				importScripts(file);
				file.renameTo(new File(file.getAbsolutePath() + ".imported"));
				Logger.info("  File " + file.getAbsolutePath() + " processed !");
			} catch (final Exception e) {
				Logger.error(e, "Error importing SQL scripts from " + file.getAbsolutePath() + " !");
			}
		}
	}

	private void importScripts(final File file) throws IOException, SQLException {
		final List<String> lines = FileUtils.readLines(file);
		for (final String line : lines) {
			if (isToBeImported(line)) {
				final String shortline = line.substring(0, Math.min(50, line.length()));
				Logger.info("  . import : " + shortline);
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

	private static class SqlFileFilter implements FileFilter {

		@Override
		public boolean accept(final File pathname) {
			return pathname.getName().toLowerCase().endsWith(".sql");
		}

	}
}
