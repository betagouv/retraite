package utils.db;

import static org.mockito.Matchers.endsWith;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.FileUtils;
import utils.SqlUtils;

public class SqlScriptsImporterTest {

	private static final String DIRNAME = "sql-imports";

	private FileUtils fileUtilsMock;

	private SqlScriptsImporter sqlScriptsImporter;
	private SqlUtils sqlUtilsMock;

	@Before
	public void setUp() throws Exception {
		fileUtilsMock = mock(FileUtils.class);
		sqlUtilsMock = mock(SqlUtils.class);

		sqlScriptsImporter = new SqlScriptsImporter(fileUtilsMock, sqlUtilsMock);
	}

	@Test
	public void should_do_nothing_if_directory_does_not_exist() {
		when(fileUtilsMock.listFiles("sql-imports", ".sql")).thenReturn(null);

		sqlScriptsImporter.importPresentFiles();
	}

	@Test
	public void should_do_nothing_if_directory_is_empty() {
		when(fileUtilsMock.listFiles("sql-imports", ".sql")).thenReturn(new File[0]);

		sqlScriptsImporter.importPresentFiles();
	}

	@Test
	public void should_execute_simple_commands() throws SQLException {

		final File fileSimple = getFile("res/SqlImportsSimple.sql");
		when(fileUtilsMock.listFiles("sql-imports", ".sql")).thenReturn(new File[] { fileSimple });

		sqlScriptsImporter.importPresentFiles();

		verify(sqlUtilsMock).executeSqlLine("INSERT INTO Caisse VALUES...");
		verify(sqlUtilsMock).executeSqlLine("INSERT INTO CaisseDepartementale VALUES xxx...");
		verify(fileUtilsMock).renameTo(same(fileSimple), endsWith("res/SqlImportsSimple.sql.imported"));
	}

	@Test
	public void should_transform_commands() throws SQLException {

		final File fileSimple = getFile("res/SqlImportsWithSpecialChars.sql");
		when(fileUtilsMock.listFiles("sql-imports", ".sql")).thenReturn(new File[] { fileSimple });

		sqlScriptsImporter.importPresentFiles();

		verify(sqlUtilsMock).executeSqlLine("INSERT INTO Caisse VALUES ...de l''Euro Côte d''Azur L''Assurance");
		verify(fileUtilsMock).renameTo(same(fileSimple), endsWith("res/SqlImportsWithSpecialChars.sql.imported"));
	}

	@Test
	public void should_ignore_comments() throws SQLException {

		final File fileSimple = getFile("res/SqlImportsWithComments.sql");
		when(fileUtilsMock.listFiles("sql-imports", ".sql")).thenReturn(new File[] { fileSimple });

		sqlScriptsImporter.importPresentFiles();

		verify(sqlUtilsMock).executeSqlLine("INSERT INTO Caisse VALUES...");
		verify(sqlUtilsMock).executeSqlLine("INSERT INTO CaisseDepartementale VALUES xxx...");
		verify(fileUtilsMock).renameTo(same(fileSimple), endsWith("res/SqlImportsWithComments.sql.imported"));
	}

	@Test
	public void should_ignore_some_commands() throws SQLException {

		final File fileSimple = getFile("res/SqlImportsWithCommandToIgnore.sql");
		when(fileUtilsMock.listFiles("sql-imports", ".sql")).thenReturn(new File[] { fileSimple });

		sqlScriptsImporter.importPresentFiles();

		verify(sqlUtilsMock).executeSqlLine("INSERT INTO Caisse VALUES...");
		verify(sqlUtilsMock).executeSqlLine("INSERT INTO CaisseDepartementale VALUES xxx...");
		verify(fileUtilsMock).renameTo(same(fileSimple), endsWith("res/SqlImportsWithCommandToIgnore.sql.imported"));
	}

	@Test
	public void should_ignore_lines_with_some_data() throws SQLException {

		final File fileSimple = getFile("res/SqlImportsWithDataToIgnore.sql");
		when(fileUtilsMock.listFiles("sql-imports", ".sql")).thenReturn(new File[] { fileSimple });

		sqlScriptsImporter.importPresentFiles();

		verify(sqlUtilsMock).executeSqlLine("INSERT INTO Caisse VALUES...");
		verify(sqlUtilsMock).executeSqlLine("INSERT INTO CaisseDepartementale VALUES xxx...");
		verify(fileUtilsMock).renameTo(same(fileSimple), endsWith("res/SqlImportsWithDataToIgnore.sql.imported"));
	}

	@Test
	public void should_rename_file_with_extension_after_import() throws SQLException {

		final File fileSimple = getFile("res/SqlImportsWithDataToIgnore.sql");
		when(fileUtilsMock.listFiles("sql-imports", ".sql")).thenReturn(new File[] { fileSimple });

		sqlScriptsImporter.importPresentFiles();

		verify(sqlUtilsMock).executeSqlLine("INSERT INTO Caisse VALUES...");
		verify(sqlUtilsMock).executeSqlLine("INSERT INTO CaisseDepartementale VALUES xxx...");
		verify(fileUtilsMock).renameTo(same(fileSimple), endsWith("res/SqlImportsWithDataToIgnore.sql.imported"));

	}

	@After
	public void noMoreMockInteractions() {
		verify(fileUtilsMock).listFiles("sql-imports", ".sql");
		verifyNoMoreInteractions(fileUtilsMock, sqlUtilsMock);
	}

	private File getFile(final String pathname) {
		return new File(getClass().getResource(pathname).getFile());
	}
}
