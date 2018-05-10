package com.jdroid.component.builder;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.UnexpectedBuildFailure;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.Assert.assertEquals;

public class ToolsVerificationTaskTest extends AbstractFunctionalTest {
	
	@Override
	protected void onSetup(File buildFile) throws IOException {
		StringBuilder buildFileBuilder = new StringBuilder();
		buildFileBuilder.append("plugins { id 'com.jdroid.component.builder' }\n");
		writeFile(buildFile, buildFileBuilder.toString());
	}
	
	@Test
	public void testExecution() throws IOException {
		try {
			BuildResult result = createBuildResult("verifyJdroidTools");
			assertEquals(result.task(":verifyJdroidTools").getOutcome(), SUCCESS);
		} catch (UnexpectedBuildFailure e) {
			// If we change the Gradle Version, this is going to fail
			assertTrue(true);
		}
	}
}
