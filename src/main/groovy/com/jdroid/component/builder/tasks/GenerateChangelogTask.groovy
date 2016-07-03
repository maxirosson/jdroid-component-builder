package com.jdroid.component.builder.tasks

import org.gradle.api.tasks.TaskAction

public class GenerateChangelogTask extends AbstractGitHubTask {

	@TaskAction
	public void doExecute() {

		File projectDir = getProjectDirectory()

		// github_changelog_generator --no-unreleased --no-pull-requests --no-pr-wo-labels --exclude-labels task -t $GIT_HUB_READ_ONLY_TOKEN
		execute(['github_changelog_generator', '--no-unreleased', '--no-pull-requests', '--no-pr-wo-labels', '--exclude-labels', 'task', '-t', getGitHubReadToken()], projectDir)

		// git add CHANGELOG.md
		execute(['git', 'add', 'CHANGELOG.md'], projectDir)

		// git commit -m "Updated CHANGELOG.md"
		execute(['git', 'commit', '-m', '"Updated CHANGELOG.md"'], projectDir)

		// git diff HEAD
		execute(['git', 'diff', 'HEAD'], projectDir)

		// git push origin HEAD:production
		execute(['git', 'push', 'origin', 'HEAD:production'], projectDir)

		println 'Please verify the CHANGELOG.md [' + getRepositoryUrl() + '/blob/production/CHANGELOG.md' + ']'
	}
}
