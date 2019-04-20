package com.jdroid.component.builder

import com.jdroid.component.builder.config.ProjectConfigSyncTask
import com.jdroid.component.builder.config.ProjectConfigVerificationTask
import com.jdroid.component.builder.tasks.CloseGitHubMilestoneTask
import com.jdroid.component.builder.tasks.CreateGitHubReleaseTask
import com.jdroid.component.builder.tasks.GenerateChangelogTask
import com.jdroid.component.builder.tasks.ReleaseJdroidComponentTask
import org.gradle.api.Project
import org.gradle.api.Task

public class ComponentBuilderGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		applyPlugin("com.gradle.build-scan");

		project.getTasks().create("syncJdroidProjectConfig", ProjectConfigSyncTask.class);
		project.getTasks().create("checkJdroidProjectConfig", ProjectConfigVerificationTask.class);
		project.getTasks().create("closeJdroidGitHubMilestone", CloseGitHubMilestoneTask.class).dependsOn("checkJdroidProjectConfig");
		project.getTasks().create("createJdroidGitHubRelease", CreateGitHubReleaseTask.class).dependsOn("closeJdroidGitHubMilestone");
		project.getTasks().create("generateJdroidChangelog", GenerateChangelogTask.class).dependsOn("createJdroidGitHubRelease");

		project.getTasks().create("releaseJdroidComponent", ReleaseJdroidComponentTask.class);

		project.afterEvaluate {
			Task checkTask = project.getTasks().findByName("check");
			if (checkTask != null) {
				checkTask.dependsOn("checkJdroidProjectConfig");
			}
		}

		// https://docs.gradle.org/current/userguide/publishing_overview.html
		// https://docs.gradle.org/current/userguide/publishing_maven.html
		addPublishingRepositories()

		project.buildScan {
			termsOfServiceUrl = 'https://gradle.com/terms-of-service'
			termsOfServiceAgree = 'yes'
		}
	}

	private void addPublishingRepositories() {
		project.getAllprojects().each {

			final eachProject = it
			eachProject.setGroup("com.jdroidtools");

			if (!eachProject.plugins.hasPlugin("maven-publish")) {
				eachProject.apply plugin: 'maven-publish'
			}

			Boolean localUpload = jdroidComponentBuilder.getBooleanProp('LOCAL_UPLOAD', true)
			String localMavenRepo = jdroidComponentBuilder.getStringProp('LOCAL_MAVEN_REPO')

			if (localUpload && localMavenRepo == null) {
				project.logger.warn("LOCAL_MAVEN_REPO property is not defined. Skipping publish configuration")
			} else {
				eachProject.afterEvaluate {
					eachProject.publishing.repositories {

						if (localUpload) {
							maven {
								name = "localMavenRepo"
								url = eachProject.uri(localMavenRepo)
							}
						} else {
							maven {
								name = "nexusMavenRepo"
								if (eachProject.version.isSnapshot) {
									url = "https://oss.sonatype.org/content/repositories/snapshots/"
								} else {
									url = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
								}
								credentials {
									username getNexusUsername()
									password getNexusPassword()
								}
							}
						}
					}
				}
			}
		}
	}

	private String getNexusUsername() {
		return jdroidComponentBuilder.getProp('JDROID_NEXUS_USERNAME')
	}

	private String getNexusPassword() {
		return jdroidComponentBuilder.getProp('JDROID_NEXUS_PASSWORD')
	}

	protected Class<? extends ComponentBuilderGradleExtension> getExtensionClass() {
		return ComponentBuilderGradleExtension.class;
	}
}