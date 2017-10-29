package com.jdroid.component.builder.tasks
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.TaskAction

public class ToolsVerificationTask extends DefaultTask {

	private static final String EXPECTED_GRADLE_VERSION = "4.1"
	private static final String EXPECTED_JAVA_VERSION = "1.8"

	public ToolsVerificationTask() {
		group = JavaBasePlugin.VERIFICATION_GROUP
	}

	@TaskAction
	public void doExecute() {

		String gradleVersion = project.gradle.gradleVersion
		if (gradleVersion != EXPECTED_GRADLE_VERSION) {
			throw new GradleException("Wrong gradle version. Actual version: " + gradleVersion + " | Expected version: " + EXPECTED_GRADLE_VERSION)
		}

		String javaVersion = org.gradle.internal.jvm.Jvm.current().getJavaVersion()
		if (javaVersion != EXPECTED_JAVA_VERSION) {
			throw new GradleException("Wrong java version. Actual version: " + javaVersion + " | Expected version: " + EXPECTED_JAVA_VERSION)
		}
	}
}
