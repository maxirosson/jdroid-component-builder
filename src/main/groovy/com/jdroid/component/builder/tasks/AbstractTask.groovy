package com.jdroid.component.builder.tasks

import org.gradle.api.DefaultTask

public class AbstractTask extends DefaultTask {

	public void executeCommand(String executableCommand, def arguments) {
		StringBuilder builder = new StringBuilder()
		builder.append(executableCommand)
		arguments.each {
			builder.append(it)
			builder.append(" ")
		}
		println "Command executed: " + builder.toString()

		// Execute the command
		project.exec {
			executable executableCommand
			args arguments.toArray()
		}
	}
}
