package com.jdroid.component.builder

import com.jdroid.component.builder.tasks.*
import org.gradle.api.Project
import org.gradle.api.artifacts.maven.MavenDeployment

public class ComponentBuilderGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.task('releaseJdroidComponent', type: ReleaseJdroidComponentTask)
		project.task('closeGitHubMilestone', type: CloseGitHubMilestoneTask)
		project.task('createGitHubRelease', type: CreateGitHubReleaseTask)
		project.task('generateChangelogTask', type: GenerateChangelogTask)
		project.task('toolsVerificationTask', type: ToolsVerificationTask)

		addUploadConfiguration()
	}

	private void addUploadConfiguration() {

		project.getAllprojects().each {

			final eachProject = it
			eachProject.group = 'com.jdroidframework'

			eachProject.apply plugin: 'maven'
			eachProject.apply plugin: 'signing'

			Boolean localUpload = jdroidComponentBuilder.getBooleanProp('LOCAL_UPLOAD', true)
			def localMavenRepo = jdroidComponentBuilder.getProp('LOCAL_MAVEN_REPO')

			if (localUpload && localMavenRepo == null) {
				project.logger.warn("LOCAL_MAVEN_REPO property is not defined. Skipping uploadArchives configuration")
			} else {
				eachProject.afterEvaluate {

					if (eachProject.ext.has('PACKAGING') && (eachProject.ext.PACKAGING == 'jar' || eachProject.ext.PACKAGING == 'aar')) {

						eachProject.uploadArchives {
							repositories {
								mavenDeployer {

									beforeDeployment { MavenDeployment deployment ->
										eachProject.signing.signPom(deployment)
									}

									if (localUpload) {
										repository(url: eachProject.uri(localMavenRepo))
									} else {
										repository(url: "https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
											authentication(userName: getNexusUsername(), password: getNexusPassword())
										}
										snapshotRepository(url: "https://oss.sonatype.org/content/repositories/snapshots/") {
											authentication(userName: getNexusUsername(), password: getNexusPassword())
										}
									}

									pom.artifactId = eachProject.ext.has('ARTIFACT_ID') ? eachProject.ext.get('ARTIFACT_ID') : jdroidComponentBuilder.getRepositoryName()
									pom.project {
										name eachProject.ext.PROJECT_NAME != null ? eachProject.ext.PROJECT_NAME : eachProject.rootProject.ext.PROJECT_NAME
										description eachProject.description != null ? eachProject.description : eachProject.rootProject.description
										packaging eachProject.ext.PACKAGING
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

						if (jdroidComponentBuilder.getBooleanProp('SIGNING_ENABLED', true)) {
							eachProject.signing {
								required { !eachProject.jdroid.isSnapshot && eachProject.gradle.taskGraph.hasTask("uploadArchives") }
								sign eachProject.configurations.archives
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