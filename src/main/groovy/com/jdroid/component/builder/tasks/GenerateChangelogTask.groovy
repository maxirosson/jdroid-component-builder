package com.jdroid.component.builder.tasks

import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult

public class GenerateChangelogTask extends AbstractGitHubTask {

	@TaskAction
	public void doExecute() {

		sleep(1000 * 80)

		execute('github_changelog_generator --no-unreleased --no-pull-requests --no-pr-wo-labels --exclude-labels task -t ' + getGitHubReadToken())

		execute('git add CHANGELOG.md')

		ExecResult result = execute('git commit -m "Updated CHANGELOG.md"', true, true)
		if (result.exitValue == 0) {
			execute('git diff HEAD')
			execute('git push origin HEAD:production')

			println 'Please verify the CHANGELOG.md [' + getRepositoryUrl() + '/blob/production/CHANGELOG.md' + ']'
		} else {
			getLogger().warn('Skipping CHANGELOG update because it already exists.')
		}
	}
}
