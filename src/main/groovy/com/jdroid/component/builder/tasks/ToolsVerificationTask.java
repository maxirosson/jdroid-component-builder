package com.jdroid.component.builder.tasks;

import org.gradle.api.GradleException;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.internal.jvm.Jvm;

import java.io.IOException;

public class ToolsVerificationTask extends AbstractTask {
	
	private static final String EXPECTED_GRADLE_VERSION = "4.5.1";
	private static final String EXPECTED_JAVA_VERSION = "1.8";
	
	public ToolsVerificationTask() {
		setGroup(JavaBasePlugin.VERIFICATION_GROUP);
	}
	
	@Override
	protected void onExecute() throws IOException {

		String gradleVersion = getProject().getGradle().getGradleVersion();
		if (!gradleVersion.equals(propertyResolver.getStringProp("EXPECTED_GRADLE_VERSION", EXPECTED_GRADLE_VERSION))) {
			throw new GradleException("Wrong gradle version. Actual version: " + gradleVersion + " | Expected version: " + EXPECTED_GRADLE_VERSION);
		}

		String javaVersion = Jvm.current().getJavaVersion().toString();
		if (!javaVersion.equals(propertyResolver.getStringProp("EXPECTED_JAVA_VERSION", EXPECTED_JAVA_VERSION))) {
			throw new GradleException("Wrong java version. Actual version: " + javaVersion + " | Expected version: " + EXPECTED_JAVA_VERSION);
		}

	}

}
