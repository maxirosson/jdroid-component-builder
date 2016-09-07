package com.jdroid.component.builder.tasks

import org.gradle.api.DefaultTask
import org.gradle.process.ExecResult

public class AbstractTask extends DefaultTask {

	public ExecResult execute(def command, def workingDirectory, Boolean ignoreExitValueParam) {
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
		}
	}

	public ExecResult execute(def command, def workingDirectory) {
		execute(command, workingDirectory, false)
	}

	public String getGitBranch() {
		return 'git symbolic-ref HEAD'.execute().text.trim().replaceAll(".*/", "")
	}
}
