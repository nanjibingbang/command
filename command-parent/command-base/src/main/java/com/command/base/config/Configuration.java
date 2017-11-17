package com.command.base.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private Properties properties;

    public Configuration load(String configFile) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(configFile);
        if (is == null) {
            throw new NullPointerException("config file not find");
        }
        properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("read config file error");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return this;
    }

    public String getPropertie(String key) {
        return properties.getProperty(key);
    }
    
}
