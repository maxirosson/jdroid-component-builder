package com.jdroid.component.builder;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AbstractFunctionalTest {
	
	@Rule
	public final TemporaryFolder testProjectDir = new TemporaryFolder();
	
	private File buildFile;
	
	@Before
	public void setup() throws IOException {
		buildFile = testProjectDir.newFile("build.gradle");
		onSetup(buildFile);
	}
	
	protected void onSetup(File buildFile) throws IOException {
		// Do nothing
	}
	
	protected void writeFile(File destination, String content) throws IOException {
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(destination));
			output.write(content);
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}
	
	protected BuildResult createBuildResult(String... args) {
		return GradleRunner.create()
				.withProjectDir(testProjectDir.getRoot())
				.withArguments(args)
				.withPluginClasspath()
				.build();
	}
}
