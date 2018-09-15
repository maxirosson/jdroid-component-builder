package com.jdroid.component.builder.config;

import com.jdroid.component.builder.tasks.AbstractTask;
import com.jdroid.java.utils.StreamUtils;

import org.gradle.api.GradleException;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.internal.jvm.Jvm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ProjectConfigVerificationTask extends AbstractTask {

	private static final String EXPECTED_JAVA_VERSION = "1.8";

	public ProjectConfigVerificationTask() {
		setDescription("Validates if the project configuration is up to date");
		setGroup(JavaBasePlugin.VERIFICATION_GROUP);
	}

	@Override
	protected void onExecute() {

		String javaVersion = Jvm.current().getJavaVersion().toString();
		if (!javaVersion.equals(propertyResolver.getStringProp("EXPECTED_JAVA_VERSION", EXPECTED_JAVA_VERSION))) {
			throw new GradleException("Wrong java version. Actual version: " + javaVersion + " | Expected version: " + EXPECTED_JAVA_VERSION);
		}

		File rootDir = getProject().getRootDir();

		for (ProjectConfig projectConfig : ProjectConfig.values()) {
			try {
				log("Validating " + projectConfig.getTarget());
				Boolean valid = StreamUtils.isEquals(getClass().getResourceAsStream(projectConfig.getSource()),
					new FileInputStream(new File(rootDir, projectConfig.getTarget())));
				if (!valid) {
					throw new RuntimeException("The file [" + projectConfig.getTarget() + "] is not up to date");
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
