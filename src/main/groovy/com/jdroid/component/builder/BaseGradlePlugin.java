package com.jdroid.component.builder;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.HashMap;
import java.util.Map;

public class BaseGradlePlugin implements Plugin<Project> {

	protected Project project;

	protected ComponentBuilderGradleExtension jdroidComponentBuilder;

	public void apply(Project project) {
		this.project = project;

		jdroidComponentBuilder = (ComponentBuilderGradleExtension)project.getExtensions().create("jdroidComponentBuilder", getExtensionClass(), project);
	}

	protected Class<? extends BaseGradleExtension> getExtensionClass() {
		return BaseGradleExtension.class;
	}

	protected void applyPlugin(String plugin) {
		Map<String, String> map = new HashMap<>();
		map.put("plugin", plugin);
		project.apply(map);
	}
}