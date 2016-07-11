package com.jdroid.component.builder

import com.jdroid.component.builder.tasks.*
import org.gradle.api.Project
import org.gradle.api.artifacts.maven.MavenDeployment

public class ComponentBuilderGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.group = 'com.jdroidframework'

		project.task('releaseJdroidComponent', type: ReleaseJdroidComponentTask)
		project.task('closeGitHubMilestone', type: CloseGitHubMilestoneTask)
		project.task('createGitHubRelease', type: CreateGitHubReleaseTask)
		project.task('generateChangelogTask', type: GenerateChangelogTask)
		project.task('toolsVerificationTask', type: ToolsVerificationTask)
		project.task('printVersion') << {
			println project.version
		}

		addUploadConfiguration()
	}

	private void addUploadConfiguration() {

		project.apply plugin: 'maven'
		project.apply plugin: 'signing'

		Boolean localUpload = jdroidComponentBuilder.getBooleanProp('LOCAL_UPLOAD', true)
		def localMavenRepo = jdroidComponentBuilder.getProp('LOCAL_MAVEN_REPO')

		if (localUpload && localMavenRepo == null) {
			project.logger.warn("LOCAL_MAVEN_REPO property is not defined. Skipping uploadArchives configuration")
		} else {
			project.afterEvaluate {
				project.uploadArchives {
					repositories {
						mavenDeployer {

							beforeDeployment { MavenDeployment deployment ->
								project.signing.signPom(deployment)
							}

							if (localUpload) {
								repository(url: project.uri(localMavenRepo))
							} else {
								repository(url: "https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
									authentication(userName: getNexusUsername(), password: getNexusPassword())
								}
								snapshotRepository(url: "https://oss.sonatype.org/content/repositories/snapshots/") {
									authentication(userName: getNexusUsername(), password: getNexusPassword())
								}
							}

							pom.artifactId = project.ext.has('ARTIFACT_ID') ? project.ext.get('ARTIFACT_ID') : project.ext.get('JDROID_GITHUB_REPOSITORY_NAME')
							pom.project {
								description project.description
								packaging project.ext.packaging
								url 'http://www.jdroidframework.com'
								inceptionYear '2011'
								organization {
									name 'Jdroid'
									url 'http://www.jdroidframework.com'
								}
								licenses {
									license {
										name 'The Apache License, Version 2.0'
										url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
										distribution 'repo'
									}
								}
								developers {
									developer {
										name 'Maxi Rosson'
										email 'jdroidframework@gmail.com'
										roles {
											role 'architect'
											role 'developer'
										}
									}
								}
								scm {
									connection 'scm:git:' + jdroidComponentBuilder.getRepositorySshUrl()
									developerConnection 'scm:git:' + jdroidComponentBuilder.getRepositorySshUrl()
									url jdroidComponentBuilder.getRepositorySshUrl()
								}
								issueManagement {
									system 'GitHub'
									url jdroidComponentBuilder.getRepositoryUrl() + '/issues'
								}
							}
						}
					}
				}
			}

			if (jdroidComponentBuilder.getBooleanProp('SIGNING_ENABLED', true)) {
				project.signing {
					required { !project.jdroid.isSnapshot && project.gradle.taskGraph.hasTask("uploadArchives") }
					sign project.configurations.archives
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