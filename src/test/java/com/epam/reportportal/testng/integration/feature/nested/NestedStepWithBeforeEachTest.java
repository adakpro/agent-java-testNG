package com.epam.reportportal.testng.integration.feature.nested;

import com.epam.reportportal.annotations.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.epam.reportportal.testng.NestedStepTest.NESTED_STEP_NAME_TEMPLATE;
import static com.epam.reportportal.testng.NestedStepTest.PARAM;

/**
 * @author <a href="mailto:ivan_budayeu@epam.com">Ivan Budayeu</a>
 */
public class NestedStepWithBeforeEachTest {

	@BeforeMethod
	public void before() {
		method(PARAM);
	}

	@Test
	public void test() {
	}

	@Step(NESTED_STEP_NAME_TEMPLATE)
	public void method(String param) {

	}
}
