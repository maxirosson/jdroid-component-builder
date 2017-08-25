package com.jdroid.component.builder.tasks

import com.jdroid.github.IRepositoryIdProvider
import com.jdroid.github.RepositoryId
import com.jdroid.github.client.GitHubClient
import com.jdroid.java.exception.UnexpectedException

public class AbstractGitHubTask extends AbstractTask {

	public GitHubClient createGitHubClient() {
		GitHubClient client = new GitHubClient();
		client.setSerializeNulls(false);
		client.setOAuth2Token(getGitHubWriteToken());
		return client
	}

	public IRepositoryIdProvider getIRepositoryIdProvider() {
		return RepositoryId.create(getRepositoryOwner(), getRepositoryName());
	}

	public String getGitHubWriteToken() {
		return project.jdroidComponentBuilder.getGitHubWriteToken()
	}
	public String getGitHubReadToken() {
		return project.jdroidComponentBuilder.getGitHubReadToken()
	}

	public String getRepositoryOwner() {
		return project.jdroidComponentBuilder.getRepositoryOwner()
	}

	public String getRepositoryName() {
		return project.jdroidComponentBuilder.getRepositoryName()
	}

	public String getGiHubUsername() {
		return project.jdroidComponentBuilder.getGiHubUsername()
	}

	public String getGiHubEmail() {
		return project.jdroidComponentBuilder.getGiHubEmail()
	}

	public String getRepositoryCloneUrl() {
		return project.jdroidComponentBuilder.getRepositoryCloneUrl()
	}

	public String getRepositorySshUrl() {
		return project.jdroidComponentBuilder.getRepositorySshUrl()
	}

	public String getRepositoryUrl() {
		return project.jdroidComponentBuilder.getRepositoryUrl()
	}

	// The path to a directory where the code will be checked out and the assemblies would be generated. For example: /home/user/build
	public File getBuildDirectory() {
		String buildDirectory = project.jdroidComponentBuilder.getProp('JDROID_BUILD_DIRECTORY')
		if (buildDirectory == null) {
			throw new UnexpectedException("The JDROID_BUILD_DIRECTORY parameter is required")
		}
		File buildDirectoryFile = new File(buildDirectory)
		if (!buildDirectoryFile.exists()) {
			throw new UnexpectedException("The JDROID_BUILD_DIRECTORY directory [" + buildDirectoryFile.absolutePath + "] does not exist.")
		}
		return buildDirectoryFile
	}

	public File getProjectDirectory() {
		return new File(getBuildDirectory(), getRepositoryName())
	}
}
