package jobs;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.FileUtils;
import utils.SqlUtils;
import utils.db.SqlScriptsImporter;

@OnApplicationStart
public class ImportSqlScriptsJob extends Job {

	@Override
	public void doJob() {
		new SqlScriptsImporter(new FileUtils(), new SqlUtils()).importPresentFiles();
	}
}
