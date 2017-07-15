package com.jdroid.component.builder

import org.gradle.api.Project

public class BaseGradleExtension {

	protected final BaseGradlePlugin baseGradlePlugin

	public BaseGradleExtension(BaseGradlePlugin baseGradlePlugin) {
		this.baseGradlePlugin = baseGradlePlugin
	}

	public def getProp(String propertyName) {
		return getProp(propertyName, null)
	}

	public def getProp(String propertyName, def defaultValue) {
		def value = getProp(baseGradlePlugin.project, propertyName)
		if (value == null) {
			value = System.getenv(propertyName)
		}
		return value != null ? value : defaultValue
	}

	public def getProp(Project project, String propertyName) {
		return project != null && project.hasProperty(propertyName) ? project.ext.get(propertyName) : null
	}

	public Boolean getBooleanProp(String propertyName, Boolean defaultValue) {
		def value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else if (value.toString() == 'true') {
			return true
		} else if (value.toString() == 'false') {
			return false
		} else {
			throw new RuntimeException("Invalid Boolean value: " + value)
		}
	}

	public String getStringProp(String propertyName) {
		return getStringProp(propertyName, null)
	}

	public String getStringProp(String propertyName, String defaultValue) {
		def value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return value.toString();
		}
	}

	public Integer getIntegerProp(String propertyName, Integer defaultValue) {
		def value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return Integer.parseInt(value);
		}
	}

	public Long getLongProp(String propertyName, Long defaultValue) {
		def value = getProp(propertyName)
		if (value == null) {
			return defaultValue
		} else {
			return Long.parseLong(value);
		}
	}
}