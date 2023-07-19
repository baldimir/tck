package org.omg.dmn.tck.runner.junit4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for handling of TCK results files. 
 * Example workflow:
 *  - Create instance of this class using the constructor. 
 *    There are two constructors available as instructionsURL is an optional value.
 *  - Call savePropertiesToVendorResults() to save the properties to a file in appropriate directory based on vendor and product version. 
 * 
 * It is also possible to just return the Properties and skip the file saving, using method buildProperties(). 
 */
public final class ResultFiles {

    private static final Logger logger = LoggerFactory.getLogger(ResultFiles.class);

    // These keys may be moved to some common module to be reused also by Reporter.
    // For now, let's have them here, they will be moved when a common module will
    // be created.
    private static final String PRODUCT_VERSION_KEY = "product.version";
    private static final String PRODUCT_NAME_KEY = "product.name";
    private static final String PRODUCT_URL_KEY = "product.url";
    private static final String PRODUCT_COMMENT_KEY = "product.comment";
    private static final String VENDOR_NAME_KEY = "vendor.name";
    private static final String VENDOR_URL_KEY = "vendor.url";
    private static final String INSTRUCTIONS_URL_KEY = "instructions.url";
    private static final String LAST_UPDATED_KEY = "last.update";

    private static final String TCK_RESULTS_FOLDER_PATH = "../../TestResults/";
    private static final String TCK_RESULTS_PROPERTIES_FILENAME = "tck_results.properties";
    private static final String TCK_RESULTS_FILENAME = "tck_results.csv";

    private String productVersion;
    private String productName;
    private String productURL;
    private String productComment;
    private String vendorName;
    private String vendorURL;
    private String instructionsURL;

    public ResultFiles(final String productVersion, final String productName, final String productURL,
            final String productComment, final String vendorName, final String vendorURL) {
        this(productVersion, productName, productURL, productComment, vendorName, vendorURL, "");
    }

    public ResultFiles(final String productVersion, final String productName, final String productURL,
            final String productComment, final String vendorName, final String vendorURL, final String instructionsURL) {
        this.productVersion = Objects.requireNonNull(productVersion);
        this.productName = Objects.requireNonNull(productName);
        this.productURL = Objects.requireNonNull(productURL);
        this.productComment = Objects.requireNonNull(productComment);
        this.vendorName = Objects.requireNonNull(vendorName);
        this.vendorURL = Objects.requireNonNull(vendorURL);
        this.instructionsURL = Objects.requireNonNull(instructionsURL);
    }

    public void setInstructionsURL(final String instructionsURL) {
        this.instructionsURL = Objects.requireNonNull(instructionsURL);
    }

    /**
     * Builds an instance of Properties, filled with property values set when creating an instance of this class. 
     * 
     * @return Instance of Properties class. 
     */
    public Properties buildProperties() {
        final Properties tckResultsProperties = new Properties();
        tckResultsProperties.setProperty(PRODUCT_VERSION_KEY, productVersion);
        tckResultsProperties.setProperty(PRODUCT_NAME_KEY, productName);
        tckResultsProperties.setProperty(PRODUCT_URL_KEY, productURL);
        tckResultsProperties.setProperty(PRODUCT_COMMENT_KEY, productComment);
        tckResultsProperties.setProperty(VENDOR_NAME_KEY, vendorName);
        tckResultsProperties.setProperty(VENDOR_URL_KEY, vendorURL);
        tckResultsProperties.setProperty(INSTRUCTIONS_URL_KEY, instructionsURL);
        tckResultsProperties.setProperty(LAST_UPDATED_KEY, ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        return tckResultsProperties;
    }

    /**
     * This method saves the TCK results properties file into an appropriate results folder. 
     * It writes to a file ../TestsResults/vendorName/productVersion/tck_results.properties. 
     */
    public void savePropertiesToVendorResults() {
        final Properties tckResultsProperties = buildProperties();
        final File resultsFile = new File(getVendorResultsDirectoryPath() + TCK_RESULTS_PROPERTIES_FILENAME);
        try (final FileOutputStream fileOutputStream = new FileOutputStream(resultsFile)) {
            tckResultsProperties.store(fileOutputStream, null);
        } catch (IOException e) {
            logger.warn("Exception while writing the file: " + resultsFile.getAbsolutePath(), e);
        }
    }

    public File getVendorResultsFile() {
        return new File(getVendorResultsDirectoryPath() + TCK_RESULTS_FILENAME);
    }

    private String getVendorResultsDirectoryPath() {
        return TCK_RESULTS_FOLDER_PATH + vendorName + File.separator + productVersion + File.separator;
    }
}
