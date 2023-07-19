/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omg.dmn.tck.runner.junit4;

import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runner.Description;
import org.omg.dmn.tck.marshaller._20160719.TestCases;

/**
 * An interface to allow the TCK runner integration
 * with vendor engines.
 *
 * A vendor that would like to use the jUnit runner
 * to execute TCK tests needs to create a class that
 * implements this interface and annotate the class
 * with:
 *
 * <code>@RunWith( DmnTckSuite.class )</code>
 *
 */
public interface DmnTckVendorTestSuite {

    /**
     * Returns a list of URI for all the test
     * case <b>FOLDERS</b> containing each individual
     * test.
     * 
     * Default implementation returns all test cases from compliance level 2 and 3. 
     *
     * @return List of test cases that are run as part of the runner. 
     */
    default List<URL> getTestCases() {
        final File testsComplianceLevel2 = new File("../../TestCases/compliance-level-2");
        final File testsComplianceLevel3 = new File("../../TestCases/compliance-level-3");

        final List<URL> testCases = new ArrayList<>();
        
		try {
            for (File file : testsComplianceLevel2.listFiles(File::isDirectory)) {
                testCases.add(file.toURI().toURL());
            }
            for (File file : testsComplianceLevel3.listFiles(File::isDirectory)) {
                testCases.add(file.toURI().toURL());
            }
		} catch (MalformedURLException e) {
			throw new UncheckedIOException(e.getMessage(), e);
		}

        testCases.sort(Comparator.comparing(URL::getPath));
        return testCases;
    }

    /**
     * Creates a context object to share vendor specific
     * data between lifecycle calls.
     *
     * This is not mandatory, and can return null if the
     * vendor desires. The runner itself just passes
     * forward the context object returned by this method
     * call into each subsequent call.
     *
     * @return
     */
    default TestSuiteContext createContext() {
        return null;
    }

    /**
     * A callback to give vendors an opportunity for initialization
     * before running each test file.
     *
     * @param context the context created by the <code>createContext()</code>
     *                call.
     * @param testCases the parsed content of a test file.
     *
     * @param modelURL the resolved URL to the DMN model file.
     * @deprecated To support DMN features involving multiple models, see {@link #beforeTestCases(TestSuiteContext, TestCases, URL, List)}
     */
    @Deprecated
    default void beforeTestCases(TestSuiteContext context, TestCases testCases, URL modelURL ) {
    }

    /**
     * A callback to give vendors an opportunity for initialization
     * before running each test file.
     *
     * @param context the context created by the <code>createContext()</code>
     *                call.
     * @param testCases the parsed content of a test file.
     *
     * @param modelURL the resolved URL to the DMN model file.
     */
    default void beforeTestCases(TestSuiteContext context, TestCases testCases, URL modelURL, Collection<? extends URL> additionalModels) {
        beforeTestCases(context, testCases, modelURL);
    }

    /**
     * A callback to give vendors an opportunity to prepare for a
     * single test execution.
     *
     * @param description
     * @param context the context created by the <code>createContext()</code>
     *                call.
     * @param testCase a single test case that will be executed next.
     */
    default void beforeTest(Description description, TestSuiteContext context, TestCases.TestCase testCase){
    }

    /**
     * Executes a single test case and returns the result.
     *
     *
     * @param description
     * @param context the context created by the <code>createContext()</code>
     *                call.
     * @param testCase a single test case that will be executed next.
     *
     * @return the result of the test execution. Optionally it includes
     *         a string message.
     */
    TestResult executeTest(Description description, TestSuiteContext context, TestCases.TestCase testCase);

    /**
     * A callback to give vendors an opportunity to clean up after a
     * single test execution.
     *
     * @param description
     * @param context the context created by the <code>createContext()</code>
     *                call.
     * @param testCase a single test case that will be executed next.
     */
    default void afterTest(Description description, TestSuiteContext context, TestCases.TestCase testCase){
    }

    /**
     * A callback to give vendors an opportunity for clean up after a
     * full test file.
     *
     * @param context the context created by the <code>createContext()</code>
     *                call.
     * @param testCases the parsed content of a test file.
     *
     */
    default void afterTestCase( TestSuiteContext context, TestCases testCases ) {
    }

    /**
     * Defines the files representing the results of the TCK tests execution for a specific vendor. 
     * Override to customize the different values of vendor specific result properties or results filenames. 
     * 
     * @return Instance of TckResultFiles filled with vendor specific information.
     */
    default ResultFiles getVendorResultFiles() {
        return new ResultFiles("0.0.1", "DMN engine", "", "", "DMN vendor", "");
    }
}
