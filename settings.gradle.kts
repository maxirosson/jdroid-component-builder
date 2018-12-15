pluginManagement {
	repositories {
		jcenter()
		gradlePluginPortal()
//		var localMavenRepoEnabled = project.hasProperty("LOCAL_MAVEN_REPO_ENABLED") ? project.ext.get("LOCAL_MAVEN_REPO_ENABLED") : System.getenv("LOCAL_MAVEN_REPO_EMABLED")
//		boolean isLocalMavenRepoEnabled = localMavenRepoEnabled != null && localMavenRepoEnabled == "true"
//		if (isLocalMavenRepoEnabled) {
//			String localMavenRepo = project.hasProperty("LOCAL_MAVEN_REPO") ? project.ext.get("LOCAL_MAVEN_REPO") : System.getenv("LOCAL_MAVEN_REPO")
//			if (localMavenRepo != null) {
//				maven(url = localMavenRepo)
//			}
//		}
		maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
		mavenCentral()
	}
}