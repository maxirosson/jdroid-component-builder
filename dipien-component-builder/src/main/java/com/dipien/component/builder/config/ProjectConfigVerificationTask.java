package com.dipien.component.builder.config;

import com.dipien.component.builder.tasks.AbstractTask;
import com.jdroid.java.utils.StreamUtils;

import org.gradle.api.plugins.JavaBasePlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ProjectConfigVerificationTask extends AbstractTask {

	public ProjectConfigVerificationTask() {
		setDescription("Validates if the project configuration is up to date");
		setGroup(JavaBasePlugin.VERIFICATION_GROUP);
	}

	@Override
	protected void onExecute() {
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
