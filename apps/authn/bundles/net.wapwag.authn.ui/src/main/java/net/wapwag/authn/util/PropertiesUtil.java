package net.wapwag.authn.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

enum PropertiesUtil {

	EMAIL_PROTOCOL("email_protocol"),

    EMAIL_USER("email_user"),

    EMAIL_PWD("email_password"),

    EMAIL_SERVER("email_server"),
	
	RESET_HOST("reset_host");

	PropertiesUtil(String key) {
        this.key = key;
    }

	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	private static final String AUTHN_CONFIG = "email.file";
	
	public static final String AUTHN_EMAIl_TXT = readJVMProperties("email.txt");

	static Map<String, String> AUTHN_CONSTANT_MAP;

	private String key;

	public String value() {
		return AUTHN_CONSTANT_MAP.get(key);
	}

	static {
		AUTHN_CONSTANT_MAP = loadProperties(readJVMProperties(AUTHN_CONFIG));
	}

	public static Set<String> readJVMProperties(String... paramNames) {
		Set<String> preloadSet = new HashSet<>();
		if (ArrayUtils.isNotEmpty(paramNames)) {

			for (String paramName : paramNames) {
				String configPath = System.getProperty(paramName);

				if (StringUtils.isNotBlank(configPath)) {
					preloadSet.add(configPath);
				}
			}

		} else {
			throw new IllegalArgumentException("jvm param name couldn't be empty");
		}

		if (logger.isInfoEnabled()) {
			logger.info("read {} params,\n{}", paramNames.length, preloadSet);
		}

		return preloadSet;
	}
	
	public static String readJVMProperties(String paramName) {
		String preloadPath = null;
		if (!StringUtil.isEmp(paramName)) {
			String configPath = System.getProperty(paramName);
	
			if (StringUtils.isNotBlank(configPath)) {
				preloadPath = configPath;
			}
			else {
				throw new IllegalArgumentException("jvm param name couldn't be empty");
			}
			if (logger.isInfoEnabled()) {
				logger.info("read {} params,\n{}", paramName, preloadPath);
			}
		}
		return preloadPath;
	}

	public static Map<String, String> loadProperties(Collection<String> paths) {
		return loadProperties(paths.toArray(new String[paths.size()]));
	}

	public static Map<String, String> loadProperties(String... paths) {
		Map<String, String> preloadMap = new HashMap<>();

		if (ArrayUtils.isNotEmpty(paths)) {

			Properties properties;
			for (String path : paths) {
				if (StringUtils.isNotBlank(path)) {

					properties = new Properties();
					try {
						properties.load(Files.newBufferedReader(Paths.get(path)));

						Set<String> keySet = properties.stringPropertyNames();
						if (!keySet.isEmpty()) {
							for (String key : properties.stringPropertyNames()) {
								preloadMap.put(key, properties.getProperty(key));
							}
						}

					} catch (IOException e) {
						throw new IllegalArgumentException("couldn't load the config file");
					} finally {
						properties.clear();
					}
				}
			}
		} else {
			throw new IllegalArgumentException("the path of the config file couldn't be empty");
		}

		if (logger.isInfoEnabled()) {
			logger.info("Load {} config,\n{}", paths.length, preloadMap);
		}

		return preloadMap;
	}
	
	public static void main(String[] args) {
		Set<String> preloadSet = new HashSet<>();
		String url = System.getProperty("user.dir") + "/config/email.properties";
		preloadSet.add(url);
		System.out.println(loadProperties(preloadSet));
	}
}
