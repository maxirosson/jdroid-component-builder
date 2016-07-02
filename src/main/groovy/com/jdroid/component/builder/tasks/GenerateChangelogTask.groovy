package com.jdroid.component.builder.tasks

import org.gradle.api.tasks.TaskAction

public class GenerateChangelogTask extends AbstractTask {

	@TaskAction
	public void doExecute() {

		// github_changelog_generator --no-unreleased --no-pull-requests --no-pr-wo-labels --exclude-labels task -t $GIT_HUB_READ_ONLY_TOKEN
		def arguments = []
		arguments.add('--no-unreleased')
		arguments.add('--no-pull-requests')
		arguments.add('--no-pr-wo-labels')
		arguments.add('--exclude-labels')
		arguments.add('task')
		arguments.add('-t')
		arguments.add(project.jdroidComponentBuilder.getProp('GIT_HUB_READ_ONLY_TOKEN'))
		executeCommand("github_changelog_generator", arguments)

		// git add CHANGELOG.md
		arguments = []
		arguments.add('add')
		arguments.add('CHANGELOG.md')
		executeCommand("git", arguments)

		// git commit -m "Updated CHANGELOG.md"
		arguments = []
		arguments.add('commit')
		arguments.add('-m')
		arguments.add('"Updated CHANGELOG.md"')
		executeCommand("git", arguments)

		// git diff HEAD
		arguments = []
		arguments.add('diff')
		arguments.add('HEAD')
		executeCommand("git", arguments)

		System.console().readLine('\n"Please verify the ' + project.projectDir + '/CHANGELOG.md and press [Enter] key to continue...')

		// git push origin HEAD:production
		arguments = []
		arguments.add('push')
		arguments.add('origin')
		arguments.add('HEAD:production')
		executeCommand("git", arguments)
	}
}
