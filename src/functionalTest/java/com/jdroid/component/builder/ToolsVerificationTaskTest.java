package com.jdroid.component.builder;

import org.gradle.testkit.runner.BuildResult;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ToolsVerificationTaskTest extends AbstractFunctionalTest {
	
	@Override
	protected void onSetup(File buildFile) throws IOException {
		StringBuilder buildFileBuilder = new StringBuilder();
		buildFileBuilder.append("plugins { id 'com.jdroid.component.builder' }\n");
		writeFile(buildFile, buildFileBuilder.toString());
	}
	
	@Test
	public void myFirstTest() throws IOException {
		BuildResult result = createBuildResult("verifyJdroidTools");
		assertNotNull(result.task(":verifyJdroidTools").getOutcome());
	}
}
