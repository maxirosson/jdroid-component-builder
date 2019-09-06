package com.jdroid.component.builder


import com.jdroid.component.builder.config.ProjectConfigSyncTask
import com.jdroid.component.builder.config.ProjectConfigVerificationTask
import com.jdroid.component.builder.tasks.CreateGitHubReleaseTask
import com.jdroid.component.builder.tasks.ReleaseJdroidComponentTask
import org.gradle.api.Project
import org.gradle.api.Task

public class ComponentBuilderGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		applyPlugin("com.gradle.build-scan");

		project.getTasks().create("syncJdroidProjectConfig", ProjectConfigSyncTask.class);
		project.getTasks().create("checkJdroidProjectConfig", ProjectConfigVerificationTask.class);
		project.getTasks().create("createJdroidGitHubRelease", CreateGitHubReleaseTask.class);
		project.getTasks().create("releaseJdroidComponent", ReleaseJdroidComponentTask.class);

		project.afterEvaluate {
			project.getTasks().getByName("closeGitHubMilestone").dependsOn("checkJdroidProjectConfig");
			project.getTasks().getByName("generateChangelog").dependsOn("createJdroidGitHubRelease");
			project.getTasks().getByName("createJdroidGitHubRelease").dependsOn("closeGitHubMilestone");
			Task checkTask = project.getTasks().findByName("check");
			if (checkTask != null) {
				checkTask.dependsOn("checkJdroidProjectConfig");
			}
		}

		project.getAllprojects().each {
			it.setGroup("com.jdroidtools");
			it.ext.set("PUBLICATION_CONFIGURATION_ENABLED", true);
			it.ext.set("JAVADOC_PUBLICATION_ENABLED", true);
			it.ext.set("SOURCES_PUBLICATION_ENABLED", true);
			it.ext.set("SIGNING_PUBLICATION_ENABLED", true);
			it.ext.set("GITHUB_REPOSITORY_OWNER", "maxirosson");
			it.ext.set("GITHUB_USER_NAME", "jdroid-ci");
			it.ext.set("GITHUB_USER_EMAIL", "jdroidtools@gmail.com");
		}

		project.buildScan {
			termsOfServiceUrl = 'https://gradle.com/terms-of-service'
			termsOfServiceAgree = 'yes'
		}
	}


	protected Class<? extends ComponentBuilderGradleExtension> getExtensionClass() {
		return ComponentBuilderGradleExtension.class;
	}
}