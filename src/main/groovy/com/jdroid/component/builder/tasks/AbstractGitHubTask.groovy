package com.jdroid.component.builder.tasks

import com.jdroid.github.IRepositoryIdProvider
import com.jdroid.github.RepositoryId;
import com.jdroid.github.client.GitHubClient;
import org.gradle.api.DefaultTask

public class AbstractGitHubTask extends AbstractTask {

	public GitHubClient createGitHubClient() {
		GitHubClient client = new GitHubClient();
		client.setSerializeNulls(false);
		client.setOAuth2Token(project.jdroidComponentBuilder.getProp('GITHUB_WRITE_TOKEN'));
		return client
	}

	public IRepositoryIdProvider getIRepositoryIdProvider() {
		return RepositoryId.create(getRepositoryOwner(), getRepositoryName());
	}

	public String getRepositoryOwner() {
		project.jdroidComponentBuilder.getProp('REPOSITORY_OWNER')
	}

	public String getRepositoryName() {
		project.jdroidComponentBuilder.getProp('REPOSITORY_NAME')
	}
}
