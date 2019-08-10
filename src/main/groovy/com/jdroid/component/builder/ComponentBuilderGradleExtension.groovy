package com.jdroid.component.builder

import org.gradle.api.Project

public class ComponentBuilderGradleExtension extends BaseGradleExtension {

	public ComponentBuilderGradleExtension(Project project) {
		super(project)
	}

	public String getGitHubWriteToken() {
		return getProp('GITHUB_WRITE_TOKEN')
	}
	public String getGitHubReadToken() {
		return getProp('GITHUB_READ_TOKEN')
	}

	public String getRepositoryOwner() {
		return getProp('GITHUB_REPOSITORY_OWNER', 'maxirosson')
	}

	public String getRepositoryName() {
		return getProp('GITHUB_REPOSITORY_NAME')
	}

	public String getGiHubUsername() {
		return getProp('GITHUB_USER_NAME')
	}

	public String getGiHubEmail() {
		return getProp('GITHUB_USER_EMAIL')
	}

	public String getRepositoryCloneUrl() {
		return 'https://' + getGitHubWriteToken() + '@github.com/' + getRepositoryOwner() + '/' + getRepositoryName() + '.git'
	}

	public String getRepositorySshUrl() {
		return 'git@github.com:' + getRepositoryOwner() + '/' + getRepositoryName() + '.git'
	}

	public String getRepositoryUrl() {
		return 'https://github.com/' + getRepositoryOwner() + '/' + getRepositoryName()
	}
}