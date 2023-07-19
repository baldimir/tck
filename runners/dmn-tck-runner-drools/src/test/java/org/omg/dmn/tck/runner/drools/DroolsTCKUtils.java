package org.omg.dmn.tck.runner.drools;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.internal.utils.KieHelper;

public final class DroolsTCKUtils {

    private static final String DROOLS_PROPERTIES_PATH = "/drools.properties";
    private static final String DROOLS_VERSION_KEY = "drools.version";
    private static final String VENDOR_NAME_KEY = "vendor.name";
    private static final String VENDOR_URL_KEY = "vendor.url";
    private static final String PRODUCT_NAME_KEY = "product.name";
    private static final String PRODUCT_URL_KEY = "product.url";
    private static final String PRODUCT_COMMENT_KEY = "product.comment";

    private static Properties droolsProperties; 

    private DroolsTCKUtils() {
        // It is not allowed to create instances of util classes.
    }

    public static DMNRuntime createRuntime(final URL mainModel, final Collection<? extends URL> additionalModels) {
        final KieServices ks = KieServices.get();
        KieHelper kieHelper = new KieHelper();
        kieHelper.addResource(ks.getResources().newFileSystemResource(mainModel.getFile()));
        for (URL model : additionalModels) {
            kieHelper.addResource(ks.getResources().newFileSystemResource(model.getFile()));
        }
        final DMNRuntime runtime = KieRuntimeFactory.of(kieHelper.getKieContainer().getKieBase()).get(DMNRuntime.class);
        if (runtime == null) {
            throw new RuntimeException("Unable to create DMNRuntime! Please see the logs for errors.");
        } else if (runtime.getModels().isEmpty()) {
            throw new RuntimeException("Unable to load model for URL '" + mainModel + "'!");
        }
        return runtime;
    }

    public static String getDroolsVersion() {
        loadDroolsProperties();
        return droolsProperties.getProperty(DROOLS_VERSION_KEY);
    }

    public static String getVendorName() {
        return droolsProperties.getProperty(VENDOR_NAME_KEY);
    }

    public static String getVendorUrl() {
        return droolsProperties.getProperty(VENDOR_URL_KEY);
    }

    public static String getProductName() {
        return droolsProperties.getProperty(PRODUCT_NAME_KEY);
    }

    public static String getProductUrl() {
        return droolsProperties.getProperty(PRODUCT_URL_KEY);
    }

    public static String getProductComment() {
        return droolsProperties.getProperty(PRODUCT_COMMENT_KEY);
    }

    private synchronized static void loadDroolsProperties() {
        if (droolsProperties == null) {
            droolsProperties = new Properties();
            try {
                droolsProperties.load(DroolsTCKUtils.class.getResourceAsStream(DROOLS_PROPERTIES_PATH));
            } catch (IOException e) {
                throw new UncheckedIOException(e.getMessage(), e);
            }
        }
    }
}
