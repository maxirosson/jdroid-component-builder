package com.dipien.component.builder;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class BaseGradlePlugin implements Plugin<Project> {

	protected Project project;

	protected ComponentBuilderGradleExtension dipienComponentBuilder;

	public void apply(Project project) {
		this.project = project;

		dipienComponentBuilder = (ComponentBuilderGradleExtension)project.getExtensions().create("dipienComponentBuilder", getExtensionClass(), project);
	}

	protected Class<? extends BaseGradleExtension> getExtensionClass() {
		return BaseGradleExtension.class;
	}
}