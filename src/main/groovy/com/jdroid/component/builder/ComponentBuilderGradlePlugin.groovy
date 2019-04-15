package com.jdroid.component.builder

import com.jdroid.component.builder.config.ProjectConfigSyncTask
import com.jdroid.component.builder.config.ProjectConfigVerificationTask
import com.jdroid.component.builder.tasks.CloseGitHubMilestoneTask
import com.jdroid.component.builder.tasks.CreateGitHubReleaseTask
import com.jdroid.component.builder.tasks.GenerateChangelogTask
import com.jdroid.component.builder.tasks.ReleaseJdroidComponentTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.maven.MavenDeployment
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc

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
		addPublishingConfiguration()
		//addUploadConfiguration()

		project.buildScan {
			termsOfServiceUrl = 'https://gradle.com/terms-of-service'
			termsOfServiceAgree = 'yes'
		}
	}

	private void addPublishingConfiguration() {
		project.getAllprojects().each {

			final eachProject = it
			eachProject.setGroup("com.jdroidtools");

			eachProject.apply plugin: 'maven-publish'

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
				project.logger.warn("LOCAL_MAVEN_REPO property is not defined. Skipping publish configuration")
			} else {

				//eachProject.afterEvaluate {

					if (eachProject.ext.has('PACKAGING')) {

						def pomClosure = {
							name = projectName
							description = eachProject.description != null ? eachProject.description : eachProject.rootProject.description
							packaging = eachProject.ext.PACKAGING
							url = 'https://jdroidtools.com'
							inceptionYear = '2011'
							organization {
								name = 'Jdroid'
								url = 'https://jdroidtools.com'
							}
							licenses {
								license {
									name = 'The Apache License, Version 2.0'
									url = 'http://www.apache.org/licenses/LICENSE-2.0.txt'
									distribution = 'repo'
								}
							}
							developers {
								developer {
									name = 'Maxi Rosson'
									email = 'contact@jdroidtools.com'
								}
							}
							scm {
								connection = 'scm:git:' + jdroidComponentBuilder.getRepositorySshUrl()
								developerConnection = 'scm:git:' + jdroidComponentBuilder.getRepositorySshUrl()
								url = jdroidComponentBuilder.getRepositorySshUrl()
							}
							issueManagement {
								system = 'GitHub'
								url = jdroidComponentBuilder.getRepositoryUrl() + '/issues'
							}
						}

						if (eachProject.ext.PACKAGING == 'jar') {

							Boolean isJavaDocPublicationEnabled = jdroidComponentBuilder.getBooleanProp("JAVADOC_PUBLICATION_ENABLED", true)
							if (isJavaDocPublicationEnabled) {
								eachProject.task('javadocJar', type: Jar) {
									classifier = 'javadoc'
									from eachProject.javadoc
								}
							}

							Boolean isSourcesPublicationEnabled = jdroidComponentBuilder.getBooleanProp("SOURCES_PUBLICATION_ENABLED", true)
							if (isSourcesPublicationEnabled) {
								eachProject.task('sourcesJar', type: Jar) {
									classifier = 'sources'
									from eachProject.sourceSets.main.allSource
								}
							}

							def javaPublicationsClosure = {
								javaLibrary(MavenPublication) {
									from eachProject.components.java
									if (isSourcesPublicationEnabled) {
										artifact eachProject.sourcesJar
									}
									if (isJavaDocPublicationEnabled) {
										artifact eachProject.javadocJar
									}
									pom(pomClosure)

								}
							}
							javaPublicationsClosure.setDelegate(eachProject)

							eachProject.publishing {
								publications(javaPublicationsClosure)
							}

							if (jdroidComponentBuilder.getBooleanProp('SIGNING_ENABLED', true)) {
								eachProject.signing {
									required { !eachProject.version.isSnapshot }
									sign eachProject.publishing.publications.javaLibrary
								}
							}

						} else if (eachProject.ext.PACKAGING == 'war') {

							Boolean isJavaDocPublicationEnabled = jdroidComponentBuilder.getBooleanProp("JAVADOC_PUBLICATION_ENABLED", true)
							if (isJavaDocPublicationEnabled) {
								eachProject.task('javadocJar', type: Jar) {
									classifier = 'javadoc'
									from eachProject.javadoc
								}
							}

							Boolean isSourcesPublicationEnabled = jdroidComponentBuilder.getBooleanProp("SOURCES_PUBLICATION_ENABLED", true)
							if (isSourcesPublicationEnabled) {
								eachProject.task('sourcesJar', type: Jar) {
									classifier = 'sources'
									from eachProject.sourceSets.main.allSource
								}
							}

							def javaWarPublicationsClosure = {
								javaWar(MavenPublication) {
									from eachProject.components.web
									if (isSourcesPublicationEnabled) {
										artifact eachProject.sourcesJar
									}
									if (isJavaDocPublicationEnabled) {
										artifact eachProject.javadocJar
									}
									pom(pomClosure)
								}
							}
							javaWarPublicationsClosure.setDelegate(eachProject)

							eachProject.publishing {
								publications(javaWarPublicationsClosure)
							}

							if (jdroidComponentBuilder.getBooleanProp('SIGNING_ENABLED', true)) {
								eachProject.signing {
									required { !eachProject.version.isSnapshot }
									sign eachProject.publishing.publications.javaWar
								}
							}
						} else if (eachProject.ext.PACKAGING == 'aar') {

							Boolean isJavaDocPublicationEnabled = jdroidComponentBuilder.getBooleanProp("JAVADOC_PUBLICATION_ENABLED", true)
							if (isJavaDocPublicationEnabled) {
								eachProject.task('androidJavadocs', type: Javadoc) {
									source = eachProject.android.sourceSets.main.java.srcDirs
									classpath += eachProject.files(eachProject.android.getBootClasspath().join(File.pathSeparator))
									eachProject.android.libraryVariants.all { variant ->
										if (variant.name == 'release') {
											owner.classpath += variant.javaCompile.classpath
										}
									}
									exclude '**/R.html', '**/R.*.html', '**/index.html'
								}

								eachProject.task('androidJavadocsJar', type: Jar, dependsOn: 'androidJavadocs') {
									classifier = 'javadoc'
									from eachProject.androidJavadocs.destinationDir
								}
							}

							Boolean isSourcesPublicationEnabled = jdroidComponentBuilder.getBooleanProp("SOURCES_PUBLICATION_ENABLED", true)
							if (isSourcesPublicationEnabled) {
								eachProject.task('androidSourcesJar', type: Jar) {
									classifier = 'sources'
									from eachProject.android.sourceSets.main.java.srcDirs
								}
							}

							def androidPublicationsClosure = {
								androidLibrary(MavenPublication) {
									if (isSourcesPublicationEnabled) {
										artifact source: eachProject.androidSourcesJar, classifier: 'sources'
									}
									if (isJavaDocPublicationEnabled) {
										artifact source: eachProject.androidJavadocsJar, classifier: 'javadoc'
									}
									artifact source: eachProject.bundleDebugAar, classifier: 'debug'
									artifact source: eachProject.bundleReleaseAar, classifier: 'release'

									pom(pomClosure)
								}
							}
							androidPublicationsClosure.setDelegate(eachProject)

							eachProject.publishing {
								publications(androidPublicationsClosure)
							}

							if (jdroidComponentBuilder.getBooleanProp('SIGNING_ENABLED', true)) {
								eachProject.apply plugin: 'signing'
								eachProject.afterEvaluate {

									eachProject.signing {
										required { !eachProject.version.isSnapshot }
										sign eachProject.publishing.publications.androidLibrary
									}
								}
							}
						} else {
							eachProject.getLogger().warn("Unsupported PACKAGING property: " + eachProject.ext.PACKAGING)
						}

						eachProject.publishing {

							if (localUpload) {
								repositories {
									maven {
										name = "localMavenRepo"
										url = eachProject.uri(localMavenRepo)
									}
								}
							} else {
								repositories {
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

				//}
			}
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