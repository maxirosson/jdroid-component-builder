package com.jdroid.component.builder

import com.jdroid.java.collections.Maps
import org.gradle.api.Plugin
import org.gradle.api.Project

public class BaseGradlePlugin implements Plugin<Project> {

	protected Project project;

	protected jdroidComponentBuilder

	public void apply(Project project) {
		this.project = project

		project.extensions.create("jdroidComponentBuilder", getExtensionClass(), project)
		jdroidComponentBuilder = project.jdroidComponentBuilder
	}

	protected Class<? extends BaseGradleExtension> getExtensionClass() {
		return BaseGradleExtension.class;
	}

	protected void applyPlugin(String plugin) {
		Map<String, String> map = Maps.newHashMap();
		map.put("plugin", plugin);
		project.apply(map);
	}
}