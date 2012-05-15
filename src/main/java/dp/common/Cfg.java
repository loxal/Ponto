/*
 * Copyright 2012 digital publishing AG. All rights reserved.
 */

package dp.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alexander Orlov <a.orlov@digitalpublishing.de>
 */
public class Cfg {
    private final Map<String, Object> puConfig = new HashMap<String, Object>();

    public Cfg() {
        final String propertiesFilePathSuffix = "/opt/cfg/persistence.properties";
        final String propertiesFilePathPrefix = System.getProperty("catalina.home");
        final String persistencePropertiesFilePath = propertiesFilePathPrefix + propertiesFilePathSuffix;
        try {
            final FileInputStream propertiesStream = new FileInputStream(persistencePropertiesFilePath);
            try {
                final PropertyResourceBundle persistenceCfg = new PropertyResourceBundle(propertiesStream);
                puConfig.put("javax.persistence.jdbc.driver", persistenceCfg.getString("javax.persistence.jdbc.driver"));
                puConfig.put("javax.persistence.jdbc.url", persistenceCfg.getString("javax.persistence.jdbc.url"));
                puConfig.put("javax.persistence.jdbc.user", persistenceCfg.getString("javax.persistence.jdbc.user"));
                puConfig.put("javax.persistence.jdbc.password", persistenceCfg.getString("javax.persistence.jdbc.password"));
                Logger.getAnonymousLogger().log(Level.INFO, puConfig.get("javax.persistence.jdbc.url").toString());
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Properties file could not be read: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Properties file does not exist.");
        }
    }

    public String getPuHandle() {
        return "default";
    }

    public Map<String, Object> getPuConfig() {
        return this.puConfig;
    }
}
