package com.dipien.component.builder;

import com.dipien.component.builder.commons.PropertyResolver;

import org.gradle.api.Project;

public class BaseGradleExtension {

	protected final Project project;

	public PropertyResolver propertyResolver;

	public BaseGradleExtension(Project project) {
		this.project = project;

		this.propertyResolver = new PropertyResolver(project);
	}
}