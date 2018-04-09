# Jdroid Component Builder
Plugin, Tools &amp; Processes to build Jdroid Components

## Continuous Integration
|Branch|Status|Workflows|Insights|
| ------------- | ------------- | ------------- | ------------- |
|master|[![CircleCI](https://circleci.com/gh/maxirosson/jdroid-component-builder/tree/master.svg?style=svg)](https://circleci.com/gh/maxirosson/jdroid-component-builder/tree/master)|[Workflows](https://circleci.com/gh/maxirosson/workflows/jdroid-component-builder/tree/master)|[Insights](https://circleci.com/build-insights/gh/maxirosson/jdroid-component-builder/master)|
|staging|[![CircleCI](https://circleci.com/gh/maxirosson/jdroid-component-builder/tree/staging.svg?style=svg)](https://circleci.com/gh/maxirosson/jdroid-component-builder/tree/staging)|[Workflows](https://circleci.com/gh/maxirosson/workflows/jdroid-component-builder/tree/staging)|[Insights](https://circleci.com/build-insights/gh/maxirosson/jdroid-component-builder/staging)|
|production|[![CircleCI](https://circleci.com/gh/maxirosson/jdroid-component-builder/tree/production.svg?style=svg)](https://circleci.com/gh/maxirosson/jdroid-component-builder/tree/production)|[Workflows](https://circleci.com/gh/maxirosson/workflows/jdroid-component-builder/tree/production)|[Insights](https://circleci.com/build-insights/gh/maxirosson/jdroid-component-builder/production)|

## New Jdroid component creation

#### GitHub
* Create a github repository. Add the prefix `jdroid-` to the repository name
* Protect the main branches (master, staging, production) and check the option "Require pull request reviews before merging"
* Remove all the issues labels, keeping only bug (red), enhancement (sky blue) and task (white)
* Add the [CODE_OF_CONDUCT](CODE_OF_CONDUCT.md) and [LICENCE](LICENCE.md) files
* Add the project on Jdroid [README](https://github.com/maxirosson/jdroid/blob/master/README.md)

#### Circle CI
* Add the project
* Add JDROID_NEXUS_USERNAME and JDROID_NEXUS_PASSWORD environment variables

#### Local Environment
* Clone the project to the local environment
* Execute the following command on the root directory
  * git config user.email your@email.com

