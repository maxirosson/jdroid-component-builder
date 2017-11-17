package com.jdroid.component.builder

import com.jdroid.component.builder.tasks.*
import org.gradle.api.Project

public class ComponentBuilderGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project)

		project.apply plugin: 'com.gradle.build-scan'

		project.task('verifyTools', type: ToolsVerificationTask)
		project.task('closeGitHubMilestone', type: CloseGitHubMilestoneTask).dependsOn 'verifyTools'
		project.task('createGitHubRelease', type: CreateGitHubReleaseTask).dependsOn 'closeGitHubMilestone'
		project.task('generateChangelog', type: GenerateChangelogTask).dependsOn 'createGitHubRelease'

		project.task('releaseJdroidComponent', type: ReleaseJdroidComponentTask)

		addUploadConfiguration()

		project.buildScan {
			licenseAgreementUrl = 'https://gradle.com/terms-of-service'
			licenseAgree = 'yes'
		}
	}

	private void addUploadConfiguration() {

		project.getSubprojects().each {

			final eachProject = it

			eachProject.group = 'com.jdroidtools'

			String projectName = jdroidComponentBuilder.getStringProp('PROJECT_NAME')
			if (projectName == null) {
				projectName = jdroidComponentBuilder.getProp(eachProject.rootProject, 'PROJECT_NAME')
				if (projectName == null) {
					projectName = eachProject.getName()
				}
			}

			eachProject.apply plugin: 'maven'
			eachProject.apply plugin: 'com.jfrog.bintray'

			eachProject.afterEvaluate {
				eachProject.bintray {
					user = jdroidComponentBuilder.getStringProp('BINTRAY_USER')
					key = jdroidComponentBuilder.getStringProp('BINTRAY_API_KEY')
					configurations = ['archives']
//				publish = true //[Default: false] Whether version should be auto published after an upload

					pkg {
						repo = 'maven'
						name = eachProject.ext.has('ARTIFACT_ID') ? eachProject.ext.get('ARTIFACT_ID') : eachProject.getName()
						licenses = ['Apache-2.0']
						issueTrackerUrl = jdroidComponentBuilder.getRepositoryUrl() + '/issues'
						vcsUrl = jdroidComponentBuilder.getRepositoryUrl() + '.git'
						websiteUrl = 'https://jdroidtools.com'
						publicDownloadNumbers = true
						githubRepo = jdroidComponentBuilder.getRepositoryOwner() + '/' + jdroidComponentBuilder.getRepositoryName()
						githubReleaseNotesFile = 'CHANGELOG.md'

						version {
							name = eachProject.version
							desc = eachProject.description != null ? eachProject.description : eachProject.rootProject.description
							released  = new Date()
							vcsTag = eachProject.version
							gpg {
								sign = true
								passphrase = jdroidComponentBuilder.getStringProp('BINTRAY_GPG_PASSPHRASE')
							}
						}
					}
				}

				eachProject.install {
					eachProject.repositories.mavenInstaller {
						// This generates POM.xml with proper parameters
						pom {
							project {
								packaging eachProject.ext.PACKAGING
								groupId eachProject.group
								artifactId eachProject.ext.has('ARTIFACT_ID') ? eachProject.ext.get('ARTIFACT_ID') : eachProject.getName()

								name projectName
								description eachProject.description != null ? eachProject.description : eachProject.rootProject.description

								url 'http://www.jdroidtools.com'
								inceptionYear '2011'
								organization {
									name 'Jdroid'
									url 'http://www.jdroidtools.com'
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
										email 'maxi@jdroidtools.com'
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





//			eachProject.apply plugin: 'signing'
//
//			Boolean localUpload = jdroidComponentBuilder.getBooleanProp('LOCAL_UPLOAD', true)
//			String localMavenRepo = jdroidComponentBuilder.getStringProp('LOCAL_MAVEN_REPO')
//
//			if (localUpload && localMavenRepo == null) {
//				project.logger.warn("LOCAL_MAVEN_REPO property is not defined. Skipping uploadArchives configuration")
//			} else {
//				eachProject.afterEvaluate {
//
//					if (eachProject.ext.has('PACKAGING') && (eachProject.ext.PACKAGING == 'jar' || eachProject.ext.PACKAGING == 'aar')) {
//
//						eachProject.uploadArchives {
//							repositories {
//								mavenDeployer {
//
//									beforeDeployment { MavenDeployment deployment ->
//										eachProject.signing.signPom(deployment)
//									}
//
//									if (localUpload) {
//										repository(url: eachProject.uri(localMavenRepo))
//									} else {
//										repository(url: "https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
//											authentication(userName: getNexusUsername(), password: getNexusPassword())
//										}
//										snapshotRepository(url: "https://oss.sonatype.org/content/repositories/snapshots/") {
//											authentication(userName: getNexusUsername(), password: getNexusPassword())
//										}
//									}
//
//									pom.artifactId = eachProject.ext.has('ARTIFACT_ID') ? eachProject.ext.get('ARTIFACT_ID') : eachProject.getName()
//									pom.project {
//										name projectName
//										description eachProject.description != null ? eachProject.description : eachProject.rootProject.description
//										packaging eachProject.ext.PACKAGING
//										url 'http://www.jdroidframework.com'
//										inceptionYear '2011'
//										organization {
//											name 'Jdroid'
//											url 'http://www.jdroidframework.com'
//										}
//										licenses {
//											license {
//												name 'The Apache License, Version 2.0'
//												url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
//												distribution 'repo'
//											}
//										}
//										developers {
//											developer {
//												name 'Maxi Rosson'
//												email 'jdroidframework@gmail.com'
//												roles {
//													role 'architect'
//													role 'developer'
//												}
//											}
//										}
//										scm {
//											connection 'scm:git:' + jdroidComponentBuilder.getRepositorySshUrl()
//											developerConnection 'scm:git:' + jdroidComponentBuilder.getRepositorySshUrl()
//											url jdroidComponentBuilder.getRepositorySshUrl()
//										}
//										issueManagement {
//											system 'GitHub'
//											url jdroidComponentBuilder.getRepositoryUrl() + '/issues'
//										}
//									}
//								}
//							}
//						}
//
//						if (jdroidComponentBuilder.getBooleanProp('SIGNING_ENABLED', true)) {
////							eachProject.signing {
////								required { !eachProject.version.isSnapshot && eachProject.gradle.taskGraph.hasTask("uploadArchives") }
////								sign eachProject.configurations.archives
////							}
//						}
//					}

//				}

//			}
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