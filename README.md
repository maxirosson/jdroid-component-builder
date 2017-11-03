# Jdroid Component Builder
Plugin, Tools &amp; Processes to build Jdroid Components

## Continuous Integration
|Branch|Status|
| ------------- | ------------- |
|Master|[![Build Status](https://travis-ci.org/maxirosson/jdroid-component-builder.svg?branch=master)](https://travis-ci.org/maxirosson/jdroid-component-builder)|
|Staging|[![Build Status](https://api.travis-ci.org/maxirosson/jdroid-component-builder.svg?branch=staging)](https://travis-ci.org/maxirosson/jdroid-component-builder)|
|Production|[![Build Status](https://api.travis-ci.org/maxirosson/jdroid-component-builder.svg?branch=production)](https://travis-ci.org/maxirosson/jdroid-component-builder)|

## New Jdroid component creation

* GitHub
  * Create a github repository. Add the prefix `jdroid-` to the repository name
  * Protect the main branches (master, staging, production) and check the option "Require pull request reviews before merging"
  * Remove all the issues labels, keeping only bug (red), enhancement (sky blue) and task (white)
* Travis
  * Add the project
  * Add JDROID_NEXUS_USERNAME and JDROID_NEXUS_PASSWORD environment variables
* Local Environment
  * Clone the project to the local environment
  * Execute the following command on the root directory
    * git config user.email your@email.com

