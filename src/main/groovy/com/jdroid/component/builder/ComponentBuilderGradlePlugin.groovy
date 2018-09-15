package com.jdroid.component.builder

import com.jdroid.component.builder.tasks.*
import org.gradle.api.Project
import org.gradle.api.artifacts.maven.MavenDeployment

public class ComponentBuilderGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		applyPlugin("com.gradle.build-scan");

		project.task('verifyJdroidTools', type: ToolsVerificationTask)
		project.task('closeJdroidGitHubMilestone', type: CloseGitHubMilestoneTask).dependsOn 'verifyJdroidTools'
		project.task('createJdroidGitHubRelease', type: CreateGitHubReleaseTask).dependsOn 'closeJdroidGitHubMilestone'
		project.task('generateJdroidChangelog', type: GenerateChangelogTask).dependsOn 'createJdroidGitHubRelease'

		project.task('releaseJdroidComponent', type: ReleaseJdroidComponentTask)

		addUploadConfiguration()

		project.buildScan {
			termsOfServiceUrl = 'https://gradle.com/terms-of-service'
			termsOfServiceAgree = 'yes'
		}
	}

	private void addUploadConfiguration() {

		project.getAllprojects().each {

			final eachProject = it
			eachProject.setGroup("com.jdroidtools");

			eachProject.apply plugin: 'maven'
			eachProject.apply plugin: 'signing'

			Boolean localUpload = jdroidComponentBuilder.getBooleanProp('LOCAL_UPLOAD', true)
			String localMavenRepo = jdroidComponentBuilder.getStringProp('LOCAL_MAVEN_REPO')

			String projectName = jdroidComponentBuilder.getStringProp('PROJECT_NAME')
			if (projectName == null) {
				projectName = jdroidComponentBuilder.getProp(eachProject.rootProject, 'PROJECT_NAME')
				if (projectName == null) {
					projectName = eachProject.getName()
				}
			}

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

									pom.artifactId = eachProject.ext.has('ARTIFACT_ID') ? eachProject.ext.get('ARTIFACT_ID') : eachProject.getName()
									pom.project {
										name projectName
										description eachProject.description != null ? eachProject.description : eachProject.rootProject.description
										packaging eachProject.ext.PACKAGING
										url 'https://jdroidtools.com'
										inceptionYear '2011'
										organization {
											name 'Jdroid'
											url 'https://jdroidtools.com'
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
												email 'contact@jdroidtools.com'
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
								required { !eachProject.version.isSnapshot && eachProject.gradle.taskGraph.hasTask("uploadArchives") }
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