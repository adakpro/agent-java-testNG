package com.epam.reportportal.testng.integration.bug;

import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.step.StepReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.epam.reportportal.testng.integration.util.TestUtils.MINIMAL_TEST_PAUSE;

public class RetryWithStepsAndDependentMethodTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(RetryWithStepsAndDependentMethodTest.class);
	private static final int MAXIMUM_RETRIES = 1;

	private final AtomicInteger testRetryNumber = new AtomicInteger();

	private final StepReporter sr = Launch.currentLaunch().getStepReporter();

	@Test(retryAnalyzer = Retry.class)
	public void retryTest() throws InterruptedException {
		sr.sendStep("Retry test");
		Thread.sleep(MINIMAL_TEST_PAUSE);
		int retry = testRetryNumber.incrementAndGet();
		if (retry <= MAXIMUM_RETRIES) {
			LOGGER.warn("Failed attempt: " + retry);
			Assert.fail();
		}
		LOGGER.info("Success attempt");
	}

	@Test(dependsOnMethods = "retryTest")
	public void dependencyOnRetry() throws InterruptedException {
		sr.sendStep("Dependency on retry test");
		Thread.sleep(MINIMAL_TEST_PAUSE);
		LOGGER.info("Dependent test");
	}

	public static class Retry implements IRetryAnalyzer {
		private final AtomicInteger retryNumber = new AtomicInteger();

		@Override
		public boolean retry(ITestResult result) {
			int retry = retryNumber.incrementAndGet();
			LOGGER.info("Retry attempt: " + retry);
			return retry <= MAXIMUM_RETRIES;
		}
	}
}
