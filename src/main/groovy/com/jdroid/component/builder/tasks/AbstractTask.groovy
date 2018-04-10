package com.jdroid.component.builder.tasks

import com.jdroid.component.builder.commons.LogOutputStream
import com.jdroid.component.builder.commons.PropertyResolver
import org.apache.tools.ant.types.Commandline
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec

public abstract class AbstractTask extends DefaultTask {

	protected PropertyResolver propertyResolver;
	private LogLevel logLevel = LogLevel.LIFECYCLE;

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
		getLogger().log(logLevel, message);
	}

	public ExecResult execute(String command, File workingDirectory, Boolean logStandardOutput, Boolean ignoreExitValue) {
		log("Executing command: " + command);
		return getProject().exec(new Action<ExecSpec>() {
			@Override
			public void execute(ExecSpec execSpec) {
				if (workingDirectory != null) {
					execSpec.setWorkingDir(workingDirectory);
				}
				execSpec.setCommandLine((Object[])Commandline.translateCommandline(command));
				execSpec.setIgnoreExitValue(ignoreExitValue);
				if (logStandardOutput) {
					execSpec.setStandardOutput(new LogOutputStream(getLogger(), logLevel));
				}
				execSpec.setErrorOutput(new LogOutputStream(getLogger(), LogLevel.ERROR));
			}
		});
	}

	public ExecResult execute(String command, File workingDirectory) {
		return execute(command, workingDirectory, true, false);
	}

	public ExecResult execute(String command) {
		return execute(command, getProject().getRootProject().getProjectDir());
	}

	public String getGitBranch() {
		return 'git symbolic-ref HEAD'.execute().text.trim().replaceAll(".*/", "")
	}
}
