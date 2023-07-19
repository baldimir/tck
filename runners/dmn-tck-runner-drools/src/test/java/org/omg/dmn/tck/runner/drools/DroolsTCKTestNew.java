package org.omg.dmn.tck.runner.drools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.ast.DecisionNode;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.kie.dmn.backend.marshalling.v1x.DMNMarshallerFactory;
import org.kie.dmn.model.api.Definitions;
import org.omg.dmn.tck.marshaller._20160719.TestCases;
import org.omg.dmn.tck.marshaller._20160719.TestCases.TestCase;
import org.omg.dmn.tck.runner.junit4.DmnTckSuite;
import org.omg.dmn.tck.runner.junit4.DmnTckVendorTestSuite;
import org.omg.dmn.tck.runner.junit4.ResultFiles;
import org.omg.dmn.tck.runner.junit4.TestResult;
import org.omg.dmn.tck.runner.junit4.TestSuiteContext;

@RunWith(DmnTckSuite.class)
public class DroolsTCKTestNew implements DmnTckVendorTestSuite {

    @Override
    public TestSuiteContext createContext() {
        return new DroolsContext();
    }

    @Override
    public void beforeTestCases(TestSuiteContext context, TestCases testCases, URL modelURL) {
        beforeTestCases(context, testCases, modelURL, Collections.emptyList());
    }

    @Override
    public void beforeTestCases(TestSuiteContext context, TestCases testCases, URL modelURL, Collection<? extends URL> additionalModels) {
        final DroolsContext testSuiteContext = (DroolsContext) context;
        testSuiteContext.setRuntime(DroolsTCKUtils.createRuntime(modelURL, additionalModels));
        try {
            final Definitions mainModelXML = DMNMarshallerFactory.newDefaultMarshaller().unmarshal(new FileReader(modelURL.getFile()));
            testSuiteContext.setDmnModel(testSuiteContext.getRuntime().getModel(mainModelXML.getNamespace(), mainModelXML.getName()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to locate model file for URL '" + modelURL + "'!");
        }
    }

    @Override
    public TestResult executeTest(Description description, TestSuiteContext context, TestCase testCase) {
        final DroolsContext droolsTckContext = (DroolsContext) context;
        final DMNContext dmnContext = droolsTckContext.getRuntime().newContext();
        
        testCase.getInputNode().forEach(in -> {
            InputDataNode input = ctx.dmnmodel.getInputByName(in.getName());
            DecisionNode decision = ctx.dmnmodel.getDecisionByName(in.getName());
            if (input != null) {
                dmnctx.set(in.getName(), parseValue(in, input)); // normally data input from file, should be pointing at
                                                                 // a InputData in the model
            } else if (decision != null) {
                dmnctx.set(in.getName(), parseValue(in, decision)); // the test case offers a pre-evaluated Decision
            } else {
                logger.warn("Override input name {} with value {}", in.getName(), in);
                dmnctx.set(in.getName(), parseType(in, REGISTRY.unknown()));
            }
        });

        throw new UnsupportedOperationException("Unimplemented method 'executeTest'");
    }

    @Override
    public ResultFiles getVendorResultFiles() {
        return new ResultFiles(DroolsTCKUtils.getDroolsVersion(), 
            DroolsTCKUtils.getProductName(), 
            DroolsTCKUtils.getProductUrl(), 
            DroolsTCKUtils.getProductComment(), 
            DroolsTCKUtils.getVendorName(), 
            DroolsTCKUtils.getVendorUrl(),
            "https://github.com/dmn-tck/tck/tree/master/runners/dmn-tck-runner-drools/README.md");
    }
}
