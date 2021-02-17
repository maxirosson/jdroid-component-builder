package com.dipien.component.builder;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ComponentBuilderGradlePlugin implements Plugin<Project> {

	public void apply(Project project) {

		// TODO Add configuration here, so ProjectConfigValidationTask on jdroid-gradle-root-plugin can also validate all the
		//  resources in this repo

		for (Project each : project.getAllprojects()) {
			each.setGroup("com.dipien");
			each.getExtensions().getExtraProperties().set("PUBLICATION_CONFIGURATION_ENABLED", true);
			each.getExtensions().getExtraProperties().set("JAVADOC_PUBLICATION_ENABLED", true);
			each.getExtensions().getExtraProperties().set("SOURCES_PUBLICATION_ENABLED", true);
			each.getExtensions().getExtraProperties().set("SIGNING_PUBLICATION_ENABLED", true);
			each.getExtensions().getExtraProperties().set("GITHUB_REPOSITORY_OWNER", "dipien");
			each.getExtensions().getExtraProperties().set("GITHUB_USER_NAME", "dipien-ci");
			each.getExtensions().getExtraProperties().set("GITHUB_USER_EMAIL", "dipien2020@gmail.com");
			each.getExtensions().getExtraProperties().set("GITHUB_CHANGELOG_GENERATOR_ENABLED", true);
			each.getExtensions().getExtraProperties().set("POM_ORGANIZATION_URL", "https://dipien.com");
			each.getExtensions().getExtraProperties().set("POM_URL", "https://dipien.com");
			each.getExtensions().getExtraProperties().set("POM_ORGANIZATION_NAME", "Dipien");
			each.getExtensions().getExtraProperties().set("POM_INCEPTION_YEAR", "2020");
			each.getExtensions().getExtraProperties().set("POM_DEVELOPER_NAME", "Maxi Rosson");
			each.getExtensions().getExtraProperties().set("POM_DEVELOPER_EMAIL", "opensource@dipien.com");
			each.getExtensions().getExtraProperties().set("POM_LICENSE_NAME", "The Apache License, Version 2.0");
			each.getExtensions().getExtraProperties().set("POM_LICENSE_URL", "http://www.apache.org/licenses/LICENSE-2.0.txt");
		}
	}
}