package com.dipien.component.builder.tasks;

import com.dipien.component.builder.commons.PropertyResolver;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

public abstract class AbstractTask extends DefaultTask {
	@TaskAction
	public final void doExecute() {
		propertyResolver = new PropertyResolver(getProject());
		try {
			onExecute();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void onExecute() throws IOException;

	protected void log(String message) {
		getLogger().log(LogLevel.LIFECYCLE, message);
	}

	protected PropertyResolver propertyResolver;
}
