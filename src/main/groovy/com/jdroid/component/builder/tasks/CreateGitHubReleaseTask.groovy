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

		String tagName = "v${project.version}"
		def releaseNotesFile = project.file("./etc/releaseNotes.txt")

		IRepositoryIdProvider repositoryIdProvider = getIRepositoryIdProvider();
		ReleaseService releaseService = new ReleaseService(client);
		Release release = releaseService.getReleaseByTagName(repositoryIdProvider, tagName);
		if (release == null) {
			createRelease(client, repositoryIdProvider, tagName, releaseNotesFile.getText());
			println 'Verify that the release is present on Releases [https://github.com/' + getRepositoryOwner() + '/' + getRepositoryName() + '/releases]'
		} else {
			getLogger().warn('Skipping ' + tagName + ' release creation because it already exists.')
		}
	}

	private void createRelease(ReleaseService releaseService, IRepositoryIdProvider repositoryIdProvider, String name, String body) throws IOException {

		Release release = new Release();
		release.setBody(body);
		release.setDraft(false);
		release.setName(name);
		release.setTagName(name);
		release.setPrerelease(false);
		release.setTargetCommitish("production");

		releaseService.createRelease(repositoryIdProvider, release);
	}
}
