package com.jdroid.component.builder.tasks

import com.jdroid.component.builder.commons.LogOutputStream
import org.gradle.api.DefaultTask
import org.gradle.process.ExecResult
import org.gradle.api.logging.LogLevel;

public class AbstractTask extends DefaultTask {

	public ExecResult execute(def command, def workingDirectory, Boolean logStandardOutput, Boolean ignoreExitValueParam) {
		StringBuilder builder = new StringBuilder()
		command.each {
			builder.append(it)
			builder.append(" ")
		}
		println "Executing command: " + builder.toString()
		project.exec {
			workingDir workingDirectory
			commandLine command
			ignoreExitValue ignoreExitValueParam
			if (logStandardOutput) {
				standardOutput new LogOutputStream(logger, LogLevel.ERROR)
			}
			errorOutput new LogOutputStream(logger, LogLevel.ERROR)
		}
	}

	public ExecResult execute(def command, def workingDirectory) {
		execute(command, workingDirectory, true, false)
	}

	public String getGitBranch() {
		return 'git symbolic-ref HEAD'.execute().text.trim().replaceAll(".*/", "")
	}
}
