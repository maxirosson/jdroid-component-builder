package com.jdroid.component.builder.tasks

import com.jdroid.github.IRepositoryIdProvider
import com.jdroid.github.Release
import com.jdroid.github.client.GitHubClient
import com.jdroid.github.service.ReleaseService
import org.gradle.api.tasks.TaskAction

public class CreateGitHubReleaseTask extends AbstractGitHubTask {

	public CreateGitHubReleaseTask() {
		description = 'Create the GitHub Release'
	}

	@TaskAction
	public void doExecute() {
		GitHubClient client = createGitHubClient();

		String tagName = "v${project.version}"

		IRepositoryIdProvider repositoryIdProvider = getIRepositoryIdProvider()
		ReleaseService releaseService = new ReleaseService(client)
		Release release = releaseService.listReleases(repositoryIdProvider).find { it.tagName == tagName }
		if (release == null) {
			String releaseNotes = getReleaseNotes()
			createRelease(releaseService, repositoryIdProvider, tagName, releaseNotes);
			println 'Verify that the release is present on Releases [' + getRepositoryUrl() + '/releases]'
		} else {
			getLogger().warn('Skipping ' + tagName + ' release creation because it already exists.')
		}
	}

	private String getReleaseNotes() {
		File projectDir = getProjectDirectory()

		execute(['github_changelog_generator', '--unreleased-only', '--no-compare-link', '--no-pull-requests', '--no-pr-wo-labels', '--exclude-labels', 'task', '-t', getGitHubReadToken()], projectDir)

		File changeLogFile = new File(projectDir, "/CHANGELOG.md")

		Boolean started = false
		Boolean exit = false
		StringBuilder builder = new StringBuilder()
		changeLogFile.eachLine {
			if (!exit) {
				if (!started && it.startsWith("## [Unreleased")) {
					started = true
				} else if (started) {
					if (it.contains('This Change Log was automatically generated')) {
						exit = true
					} else {
						builder.append(it)
						builder.append('\n')
					}
				}
			}
		}

		execute(['git', 'add', '-A'], projectDir)
		execute(['git', 'stash'], projectDir)

		return builder.toString().trim()
	}

	private void createRelease(ReleaseService releaseService, IRepositoryIdProvider repositoryIdProvider, String name, String body) throws IOException {

		Release release = new Release();
		release.setBody(body);
		release.setDraft(false);
		release.setName(name);
		release.setTagName(name);
		release.setPrerelease(false);
		release.setTargetCommitish(getGitBranch());

		releaseService.createRelease(repositoryIdProvider, release);
	}
}
