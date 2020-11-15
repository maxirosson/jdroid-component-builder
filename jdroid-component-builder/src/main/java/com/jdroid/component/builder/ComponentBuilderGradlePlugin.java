package com.jdroid.component.builder;

import org.gradle.api.Project;

import java.util.Set;

public class ComponentBuilderGradlePlugin extends BaseGradlePlugin {

	public void apply(Project project) {
		super.apply(project);

//		project.getTasks().create("syncJdroidProjectConfig", ProjectConfigSyncTask.class);
//		project.getTasks().create("checkJdroidProjectConfig", ProjectConfigVerificationTask.class);

		for (Project each : (Set<Project>)project.getAllprojects()) {
			each.setGroup("com.jdroidtools");
			each.getExtensions().getExtraProperties().set("PUBLICATION_CONFIGURATION_ENABLED", true);
			each.getExtensions().getExtraProperties().set("JAVADOC_PUBLICATION_ENABLED", true);
			each.getExtensions().getExtraProperties().set("SOURCES_PUBLICATION_ENABLED", true);
			each.getExtensions().getExtraProperties().set("SIGNING_PUBLICATION_ENABLED", true);
			each.getExtensions().getExtraProperties().set("GITHUB_REPOSITORY_OWNER", "maxirosson");
			each.getExtensions().getExtraProperties().set("GITHUB_USER_NAME", "jdroid-ci");
			each.getExtensions().getExtraProperties().set("GITHUB_USER_EMAIL", "jdroidtools@gmail.com");
			each.getExtensions().getExtraProperties().set("GITHUB_CHANGELOG_GENERATOR_ENABLED", true);
		}

		// TODO The -Dorg.gradle.internal.publish.checksums.insecure=true should be removed when Nexus add support.
		// https://github.com/gradle/gradle/issues/11308
		// https://issues.sonatype.org/browse/NEXUS-21802
//		commandExecutor.execute('./gradlew clean :checkJdroidProjectConfig :closeGitHubMilestone :createGitHubRelease ' +
//			':generateChangelog publish closeAndReleaseRepository --refresh-dependencies --no-parallel --stacktrace -PSNAPSHOT=false ' +
//			'-PLOCAL_UPLOAD=false -PRELEASE_BUILD_TYPE_ENABLED=true -PRELEASE_FAKE_ENABLED=true -PACCEPT_SNAPSHOT_DEPENDENCIES=false ' +
//			'-Dorg.gradle.internal.publish.checksums.insecure=true', projectDir)
	}


	protected Class<? extends ComponentBuilderGradleExtension> getExtensionClass() {
		return ComponentBuilderGradleExtension.class;
	}
}