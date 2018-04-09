package com.jdroid.component.builder.tasks

public class ReleaseJdroidComponentTask extends AbstractGitHubTask {

	@Override
	protected void onExecute() throws IOException {

		// Checking out

		File projectDir = getProjectDirectory()
		if (!projectDir.exists()) {
			execute('git clone ' + getRepositoryCloneUrl() + " " + getRepositoryName(), projectDir.getParentFile())
		}

		String ciGithubUserName = getGiHubUsername()
		if (ciGithubUserName != null) {
			execute('git config user.name '+ ciGithubUserName)
		}
		String ciGithubUserEmail = getGiHubEmail()
		if (ciGithubUserEmail != null) {
			execute('git config user.email ' + ciGithubUserEmail)
		}

		// TODO Signed commits should be forced
		execute('git config commit.gpgsign false')

		// Synch production branch

		execute('git add -A')
		execute('git stash')
		execute('git checkout production')
		execute('git pull')

		// TODO Temporary acceptance of SNAPSHOT dependencies. Revert this
		execute('./gradlew clean :verifyJdroidTools :closeJdroidGitHubMilestone :createJdroidGitHubRelease ' +
				':generateJdroidChangelog uploadArchives --refresh-dependencies --stacktrace -PSNAPSHOT=false ' +
				'-PLOCAL_UPLOAD=false -PRELEASE_BUILD_TYPE_ENABLED=true -PRELEASE_FAKE_ENABLED=true -PACCEPT_SNAPSHOT_DEPENDENCIES=true')
	}
}
