package com.jdroid.component.builder.tasks

import com.jdroid.github.IRepositoryIdProvider
import com.jdroid.github.Milestone
import com.jdroid.github.client.GitHubClient
import com.jdroid.github.service.MilestoneService
import org.gradle.api.tasks.TaskAction

public class CloseGitHubMilestoneTask extends AbstractGitHubTask {

	public IncrementMajorVersionTask() {
		description = 'Close the GitHub Milestone'
	}

	@TaskAction
	public void doExecute() {
		GitHubClient client = createGitHubClient();

		closeMilestone(client, getIRepositoryIdProvider(), "v${project.version}");

		println 'Verify that the milestone is closed on Milestones [https://github.com/' + getRepositoryOwner() + '/' + getRepositoryName() + '/milestones]'
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
