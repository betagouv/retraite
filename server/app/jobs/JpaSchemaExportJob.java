package jobs;

import java.io.File;

import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.JpaSchemaExport;

@OnApplicationStart
public class JpaSchemaExportJob extends Job {

	@Override
	public void doJob() {
		Logger.info("Current directory : " + new File(".").getAbsolutePath());
		final String exportShema = Play.configuration.getProperty("export.shema");
		if ("true".equals(exportShema)) {
			JpaSchemaExport.run();
		}
	}
}
