package com.jdroid.component.builder.tasks

import com.sun.corba.se.impl.util.RepositoryId
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import com.jdroid.github.IRepositoryIdProvider;
import com.jdroid.github.Milestone;
import com.jdroid.github.Release;
import com.jdroid.github.RepositoryId;
import com.jdroid.github.client.GitHubClient;
import com.jdroid.github.service.MilestoneService;
import com.jdroid.github.service.ReleaseService;

public class CloseGitHubMilestoneTask extends DefaultTask {

	public IncrementMajorVersionTask() {
		description = 'Close the GitHub Milestone'
	}

	@TaskAction
	public void doExecute() {
		GitHubClient client = new com.jdroid.github.client.GitHubClient();
		client.setSerializeNulls(false);
		client.setOAuth2Token(project.jdroidComponentBuilder.getProp('GITHUB_OATH_TOKEN'));

		IRepositoryIdProvider repositoryIdProvider = RepositoryId.create(project.jdroidComponentBuilder.getProp('REPOSITORY_OWNER'), project.jdroidComponentBuilder.getProp('REPOSITORY_NAME'));

		closeMilestone(client, repositoryIdProvider, "v${project.version}");
	}

	private void closeMilestone(GitHubClient client, IRepositoryIdProvider repositoryIdProvider, String milestoneTitle) throws IOException {

		MilestoneService milestoneService = new MilestoneService(client);
		for (Milestone each : milestoneService.getMilestones(repositoryIdProvider, "open")) {
			if (each.getTitle().equals(milestoneTitle)) {

				Milestone newMilestone = new Milestone();
				newMilestone.setNumber(each.getNumber());
				newMilestone.setTitle(each.getTitle());
				newMilestone.setDescription(each.getDescription());
				newMilestone.setDueOn(new Date());
				newMilestone.setState("closed");
				milestoneService.editMilestone(repositoryIdProvider, newMilestone);
				break;
			}
		}
	}
}
