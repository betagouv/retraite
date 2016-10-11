package utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

public class FileUtils {

	public File[] listFiles(final String path, final String filterFileExtension) {
		final File dir = new File(path);
		return dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(final File pathname) {
				if (filterFileExtension != null) {
					return pathname.getName().toLowerCase().endsWith(filterFileExtension.toLowerCase());
				}
				return true;
			}
		});
	}

	public static List<String> readLines(final File file) throws IOException {
		return org.apache.commons.io.FileUtils.readLines(file);
	}

}
