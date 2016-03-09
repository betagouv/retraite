package utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;

import org.apache.log4j.Level;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import play.Logger;
import play.Play;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.db.Configuration;
import play.db.DB;
import play.db.jpa.HibernateInterceptor;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;

@SuppressWarnings("deprecation")
public class JpaSchemaExport {

	public static void run() {
		Logger.debug("JpaSchemaExport...");

		final Ejb3Configuration cfg = getPlayEjb3Configuration();
		final org.hibernate.cfg.Configuration configuration = cfg.getHibernateConfiguration();
		try {
			final SchemaExport se = new SchemaExport(configuration, DB.getConnection("default"));
			se.setHaltOnError(true);
			se.setDelimiter(";").setFormat(true);
			// se.execute(true, false, false, false);
			se.create(true, false);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static Ejb3Configuration getPlayEjb3Configuration() {
		final String dbName = "default";
		final Configuration dbConfig = new Configuration(dbName);

		final Ejb3Configuration cfg = new Ejb3Configuration();
		final List<Class> classes = Play.classloader.getAnnotatedClasses(Entity.class);
		for (final Class<?> clazz : classes) {
			if (clazz.isAnnotationPresent(Entity.class)) {
				// Do we have a transactional annotation matching our dbname?
				final PersistenceUnit pu = clazz.getAnnotation(PersistenceUnit.class);
				if (pu != null && pu.name().equals(dbName)) {
					cfg.addAnnotatedClass(clazz);
					Logger.debug("Add JPA Model : %s to db %s", clazz, dbName);
				} else if (pu == null && JPA.DEFAULT.equals(dbName)) {
					cfg.addAnnotatedClass(clazz);
					Logger.debug("Add JPA Model : %s to db %s", clazz, dbName);
				}
			}
		}

		// Add entities
		final String[] moreEntities = Play.configuration.getProperty("jpa.entities", "").split(", ");
		for (final String entity : moreEntities) {
			if (entity.trim().equals("")) {
				continue;
			}
			try {
				final Class<?> clazz = Play.classloader.loadClass(entity);
				// Do we have a transactional annotation matching our dbname?
				final PersistenceUnit pu = clazz.getAnnotation(PersistenceUnit.class);
				if (pu != null && pu.name().equals(dbName)) {
					cfg.addAnnotatedClass(clazz);
					Logger.debug("Add JPA Model : %s to db %s", clazz, dbName);
				} else if (pu == null && JPA.DEFAULT.equals(dbName)) {
					cfg.addAnnotatedClass(clazz);
					Logger.debug("Add JPA Model : %s to db %s", clazz, dbName);
				}
			} catch (final Exception e) {
				Logger.warn("JPA -> Entity not found: %s", entity);
			}
		}

		for (final ApplicationClass applicationClass : Play.classes.all()) {
			if (applicationClass.isClass() || applicationClass.javaPackage == null) {
				continue;
			}
			final Package p = applicationClass.javaPackage;
			Logger.info("JPA -> Adding package: %s", p.getName());
			cfg.addPackage(p.getName());
		}

		final String mappingFile = dbConfig.getProperty("jpa.mapping-file", "");
		if (mappingFile != null && mappingFile.length() > 0) {
			cfg.addResource(mappingFile);
		}

		// if (!dbConfig.getProperty("jpa.ddl", Play.mode.isDev() ? "update" :
		// "none").equals("none")) {
		// cfg.setProperty("hibernate.hbm2ddl.auto",
		// dbConfig.getProperty("jpa.ddl", "update"));
		// }
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");

		final Map<String, String> properties = dbConfig.getProperties();
		properties.put("javax.persistence.transaction", "RESOURCE_LOCAL");
		properties.put("javax.persistence.provider", "org.hibernate.ejb.HibernatePersistence");
		properties.put("hibernate.dialect", JPAPlugin.getDefaultDialect(dbConfig, dbConfig.getProperty("db.driver")));

		if (dbConfig.getProperty("jpa.debugSQL", "false").equals("true")) {
			org.apache.log4j.Logger.getLogger("org.hibernate.SQL").setLevel(Level.ALL);
		} else {
			org.apache.log4j.Logger.getLogger("org.hibernate.SQL").setLevel(Level.OFF);
		}

		cfg.configure(properties);
		cfg.setDataSource(DB.getDataSource(dbName));

		try {
			final Field field = cfg.getClass().getDeclaredField("overridenClassLoader");
			field.setAccessible(true);
			field.set(cfg, Play.classloader);
		} catch (final Exception e) {
			Logger.error(e, "Error trying to override the hibernate classLoader (new hibernate version ???)");
		}

		cfg.setInterceptor(new HibernateInterceptor());
		return cfg;
	}

}
