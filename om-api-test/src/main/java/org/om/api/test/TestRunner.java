package org.om.api.test;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;
import java.util.Optional;

public class TestRunner {
    public static void main(String[] args) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage("org.om.api.test"))
                .filters(ClassNameFilter.includeClassNamePatterns(".*Test"))
                .build();

        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();
        TestExecutionListener executionListener = new TestExecutionListener() {
            @Override
            public void executionStarted(TestIdentifier testIdentifier) {
                if (testIdentifier.isTest()) {
                    System.out.println("Running test: " + getTestName(testIdentifier));
                }
            }

            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult result) {
                if (testIdentifier.isTest()) {
                    String status = switch (result.getStatus()) {
                        case SUCCESSFUL -> "+ PASSED";
                        case FAILED -> "X FAILED";
                        case ABORTED -> "️- SKIPPED";
                    };
                    System.out.println(status + " - " + getTestName(testIdentifier));

                    // If test failed, print exception details
                    result.getThrowable().ifPresent(throwable ->
                            System.out.println("   !!! Reason: " + throwable.getMessage())
                    );
                }
            }
        };

        launcher.registerTestExecutionListeners(summaryListener, executionListener);
        launcher.execute(request);

        // Print final test summary
        TestExecutionSummary summary = summaryListener.getSummary();
        summary.printTo(new PrintWriter(System.out));

        // Exit with non-zero code if tests fail
        if (summary.getTotalFailureCount() > 0) {
            System.exit(1);
        }
    }

    private static String getTestName(TestIdentifier testIdentifier) {
        Optional<TestSource> source = testIdentifier.getSource();
        if (source.isPresent() && source.get() instanceof MethodSource methodSource) {
            return methodSource.getClassName() + "#" + methodSource.getMethodName();
        }
        return testIdentifier.getDisplayName();
    }
}
