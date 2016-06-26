package com.jdroid.component.builder

import org.gradle.api.Plugin
import org.gradle.api.Project

public class BaseGradlePlugin implements Plugin<Project> {

	protected Project project;

	protected jdroidComponentBuilder

	public void apply(Project project) {
		this.project = project

		project.extensions.create("jdroidComponentBuilder", getExtensionClass(), this)
		jdroidComponentBuilder = project.jdroidComponentBuilder
	}

	protected Class<? extends BaseGradleExtension> getExtensionClass() {
		return BaseGradleExtension.class;
	}
}