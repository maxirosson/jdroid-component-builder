package com.dipien.component.builder.config;

public enum ProjectConfig {

	CODE_OF_CONDUCT("/CODE_OF_CONDUCT.md"),
	LICENSE("/LICENSE.md"),
	CONTRIBUTING("/github/CONTRIBUTING.md", "/.github/CONTRIBUTING.md");

	private String source;
	private String target;

	ProjectConfig(String source) {
		this(source, source);
	}

	ProjectConfig(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}
}
