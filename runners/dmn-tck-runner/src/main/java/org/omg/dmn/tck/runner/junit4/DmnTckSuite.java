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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

public class DmnTckSuite extends Suite {

    private final DmnTckVendorTestSuite ntsuite;
    private final List<Runner> runners;

    public DmnTckSuite(Class<?> clazz) throws InitializationError {
        super(clazz, Collections.<Runner>emptyList());
        runners = new ArrayList<>();
        try {
            ntsuite = (DmnTckVendorTestSuite) clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new InitializationError(e);
        }

        initializeResultFiles(ntsuite.getVendorResultFiles());
        for (URL url : ntsuite.getTestCases()) {
            File tcFolder = null;
            try {
                tcFolder = new File(url.toURI());
            } catch (URISyntaxException e) {
                throw new InitializationError( e );
            }
            final String tcName = tcFolder.getName();

            final File model = new File(tcFolder + "/" + tcName + ".dmn");
            if (!model.exists()) {
                continue;
            }

            File[] tcfiles = tcFolder.listFiles( (FilenameFilter) (dir, name) -> name.matches( tcName + "-test-\\d\\d.xml" ) );
            Arrays.sort( tcfiles );
            for ( File tcfile : tcfiles ) {
                DmnTckRunner runner = new DmnTckRunner( ntsuite, tcfile );
                runners.add( runner );
            }
        }
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

    private void initializeResultFiles(final ResultFiles tckResultFiles) {
        final File results = tckResultFiles.getVendorResultsFile();
        try {
            Files.deleteIfExists(results.toPath());
            results.getParentFile().mkdirs();
            results.createNewFile();
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
        tckResultFiles.savePropertiesToVendorResults();
    }

}
