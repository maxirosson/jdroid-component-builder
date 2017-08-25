package com.jdroid.component.builder.tasks

import org.gradle.api.tasks.TaskAction

public class ReleaseJdroidComponentTask extends AbstractGitHubTask {

	@TaskAction
	public void doExecute() {

		// Checking out

		File projectDir = getProjectDirectory()
		if (!projectDir.exists()) {
			execute(['git', 'clone', getRepositoryCloneUrl(), getRepositoryName()], projectDir.getParentFile())
		}

		execute(['git', 'config', 'user.name', getGiHubUsername()], projectDir)
		execute(['git', 'config', 'user.email', getGiHubEmail()], projectDir)

		// TODO Signed commits should be forced
		execute(['git', 'config', 'commit.gpgsign', 'false'], projectDir)

		// Synch production branch

		execute(['git', 'add', '-A'], projectDir)
		execute(['git', 'stash'], projectDir)
		execute(['git', 'checkout', 'production'], projectDir)
		execute(['git', 'pull'], projectDir)

		execute(['./gradlew', 'clean', ':verifyTools', ':closeGitHubMilestone', ':createGitHubRelease', ':generateChangelog',
				 'uploadArchives', '--refresh-dependencies', '--stacktrace', '-PSNAPSHOT=false', '-PLOCAL_UPLOAD=false',
				 '-PRELEASE_BUILD_TYPE_ENABLED=true', '-PRELEASE_FAKE_ENABLED=true', '-PACCEPT_SNAPSHOT_DEPENDENCIES=false'], projectDir)
	}
}
