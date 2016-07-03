package com.jdroid.component.builder

public class ComponentBuilderGradleExtension extends BaseGradleExtension {

	public ComponentBuilderGradleExtension(ComponentBuilderGradlePlugin componentBuilderGradlePlugin) {
		super(componentBuilderGradlePlugin)
	}

	public String getGitHubWriteToken() {
		return getProp('JDROID_GITHUB_WRITE_TOKEN')
	}
	public String getGitHubReadToken() {
		return getProp('JDROID_GITHUB_READ_TOKEN')
	}

	public String getRepositoryOwner() {
		return getProp('JDROID_GITHUB_REPOSITORY_OWNER')
	}

	public String getRepositoryName() {
		return getProp('JDROID_GITHUB_REPOSITORY_NAME')
	}

	public String getGiHubEmail() {
		return getProp('JDROID_GITHUB_EMAIL')
	}

	public String getRepositorySshUrl() {
		return 'git@github.com:' + getRepositoryOwner() + '/' + getRepositoryName() + '.git'
	}

	public String getRepositoryUrl() {
		return 'https://github.com/' + getRepositoryOwner() + '/' + getRepositoryName()
	}
}