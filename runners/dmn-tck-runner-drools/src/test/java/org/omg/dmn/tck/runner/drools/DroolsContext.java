package org.omg.dmn.tck.runner.drools;

import java.util.Collection;

import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNRuntime;
import org.omg.dmn.tck.runner.junit4.TestSuiteContext;

public class DroolsContext implements TestSuiteContext {

    private DMNRuntime runtime;
    private DMNModel dmnModel;
    private Collection<DMNModel> additionalModels;
    
    public DroolsContext() {
    }

    public DMNRuntime getRuntime() {
        return runtime;
    }

    public DMNModel getDmnModel() {
        return dmnModel;
    }

    public void setRuntime(final DMNRuntime runtime) {
        this.runtime = runtime;
    }

    public void setDmnModel(final DMNModel dmnModel) {
        this.dmnModel = dmnModel;
    }

    public Collection<DMNModel> getAdditionalModels() {
        return additionalModels;
    }

    public void setAdditionalModels(final Collection<DMNModel> additionalModels) {
        this.additionalModels = additionalModels;
    }

    
}
