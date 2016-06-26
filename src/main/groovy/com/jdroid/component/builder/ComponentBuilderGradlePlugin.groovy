package com.jdroid.component.builder

import com.jdroid.component.builder.tasks.CloseGitHubMilestoneTask
import com.jdroid.component.builder.tasks.CreateGitHubReleaseTask
import org.gradle.api.Project

public class ComponentBuilderGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.task('closeGitHubMilestone', type: CloseGitHubMilestoneTask)
		project.task('createGitHubRelease', type: CreateGitHubReleaseTask)
		project.task('printVersion') << {
			println project.version
		}
	}

	protected Class<? extends ComponentBuilderGradleExtension> getExtensionClass() {
		return ComponentBuilderGradleExtension.class;
	}
}