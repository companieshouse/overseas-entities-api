package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Logger;

/**
 *  Utilities to read test resources.
 */
public final class TestUtils {

    private static final Logger LOGGER = Logger.getLogger(TestUtils.class.getName());

    public String readFileReturnString(File filename) {
        BufferedReader buff;
        StringBuilder builder = new StringBuilder();
        try {
            buff = new BufferedReader(
                    new FileReader(filename));
            String line;
            while ((line = buff.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            LOGGER.severe("Parsing json file error: " + e.getMessage());
        }
        return builder.toString();
    }

    public File getFile(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }
}
