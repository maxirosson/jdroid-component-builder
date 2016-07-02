package com.jdroid.component.builder.tasks

import com.jdroid.github.IRepositoryIdProvider
import com.jdroid.github.Release
import com.jdroid.github.client.GitHubClient
import com.jdroid.github.service.ReleaseService
import com.jdroid.github.RepositoryId;
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public class CreateGitHubReleaseTask extends AbstractGitHubTask {

	public IncrementMajorVersionTask() {
		description = 'Close the GitHub Milestone'
	}

	@TaskAction
	public void doExecute() {
		GitHubClient client = createGitHubClient();

		def releaseNotesFile = project.file("./etc/releaseNotes.txt")

		createRelease(client, getIRepositoryIdProvider(), "v${project.version}", releaseNotesFile.getText());

		System.console().readLine('\nVerify that the release is present on Releases [https://github.com/' + getRepositoryOwner() + '/' + getRepositoryName() + '/releases] and press [Enter] key to continue...')
	}

	private void createRelease(GitHubClient client, IRepositoryIdProvider repositoryIdProvider, String name, String body) throws IOException {

		Release release = new Release();
		release.setBody(body);
		release.setDraft(false);
		release.setName(name);
		release.setTagName(name);
		release.setPrerelease(false);
		release.setTargetCommitish("production");

		ReleaseService releaseService = new ReleaseService(client);
		releaseService.createRelease(repositoryIdProvider, release);
	}
}
