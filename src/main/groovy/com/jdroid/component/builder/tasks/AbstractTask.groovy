package com.jdroid.component.builder.tasks

import org.gradle.api.DefaultTask

public class AbstractTask extends DefaultTask {

	public void execute(def command, def workingDirectory) {
		StringBuilder builder = new StringBuilder()
		command.each {
			builder.append(it)
			builder.append(" ")
		}
		println "Executing command: " + builder.toString()
		project.exec {
			workingDir workingDirectory
			commandLine command
		}
	}
}
