package com.jdroid.component.builder.tasks

import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult

public class GenerateChangelogTask extends AbstractGitHubTask {

	@TaskAction
	public void doExecute() {

		sleep(1000 * 80)

		File projectDir = getProjectDirectory()

		execute('github_changelog_generator --no-unreleased --no-pull-requests --no-pr-wo-labels --exclude-labels task -t ' + getGitHubReadToken(), projectDir)

		execute('git add CHANGELOG.md', projectDir)

		ExecResult result = execute('git commit -m "Updated CHANGELOG.md"', projectDir, true, true)
		if (result.exitValue == 0) {
			execute('git diff HEAD', projectDir)
			execute('git push origin HEAD:production', projectDir)

			println 'Please verify the CHANGELOG.md [' + getRepositoryUrl() + '/blob/production/CHANGELOG.md' + ']'
		} else {
			getLogger().warn('Skipping CHANGELOG update because it already exists.')
		}
	}
}
