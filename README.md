[![Dipien](https://raw.githubusercontent.com/dipien/dipien-component-builder/master/.github/dipien_logo.png)](http://www.dipien.com)

# Dipien Component Builder
Plugin, Tools &amp; Processes to build Dipien Components

## Continuous Integration
|Branch|Status|Workflows|Insights|
| ------------- | ------------- | ------------- | ------------- |
|master|[![CircleCI](https://circleci.com/gh/dipien/dipien-component-builder/tree/master.svg?style=svg)](https://circleci.com/gh/dipien/dipien-component-builder/tree/master)|[Workflows](https://circleci.com/gh/dipien/workflows/dipien-component-builder/tree/master)|[Insights](https://circleci.com/build-insights/gh/dipien/dipien-component-builder/master)|
|production|[![CircleCI](https://circleci.com/gh/dipien/dipien-component-builder/tree/production.svg?style=svg)](https://circleci.com/gh/dipien/dipien-component-builder/tree/production)|[Workflows](https://circleci.com/gh/dipien/workflows/dipien-component-builder/tree/production)|[Insights](https://circleci.com/build-insights/gh/dipien/dipien-component-builder/production)|

## New dipien component creation

#### GitHub
* Create a github repository.
* Protect the main branches (master & production) and check the option "Require pull request reviews before merging"
* Remove all the issues labels, keeping only bug (red), enhancement (sky blue) and task (white)
* Add the following files/directories:
  * [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)
  * [LICENCE-md](LICENCE.md)
  * [.gitignore](.gitignore)
  * [.idea/codeStyles/](.idea/codeStyles/)

#### Circle CI
* Add the project
* Add the following environment variables:
  * GITHUB_USER_EMAIL
  * GITHUB_USER_NAME
  * GITHUB_WRITE_TOKEN
  * PUBLISHING_REPO_PASSWORD
  * PUBLISHING_REPO_USERNAME
  * RELEASES_HUB_USER_TOKEN

#### Local Environment
* Clone the project to the local environment
* Execute the following command on the root directory
  * git config user.email your@email.com

## Donations

Donations are greatly appreciated. You can help us to pay for our domain and this project development.

* [Donate cryptocurrency](http://coinbase.dipien.com/)
* [Donate with PayPal](http://paypal.dipien.com/)
* [Donate on Patreon](http://patreon.dipien.com/)

## Follow us
* [Twitter](https://twitter.com/ReleasesHub)
* [Medium](http://medium.dipien.com)
* [Blog](http://blog.dipien.com)
