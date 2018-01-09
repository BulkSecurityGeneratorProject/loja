# Loja
[![Build Status](https://travis-ci.org/rogrs/loja.svg?branch=master)](https://travis-ci.org/rogrs/loja) [![Dependency Status](https://david-dm.org/rogrs/loja.svg)](https://david-dm.org/rogrs/loja) [![devDependency Status](https://david-dm.org/rogrs/loja/dev-status.svg)](https://david-dm.org/rogrs/loja#info=devDependencies) [![peerDependency Status](https://david-dm.org/rogrs/loja/peer-status.svg)](https://david-dm.org/rogrs/loja#info=peerDependencies) [![peerDependency Status](https://david-dm.org/rogrs/loja/peer-status.svg)](https://david-dm.org/rogrs/loja#info=peerDependencies) [![codecov](https://codecov.io/gh/rogrs/loja/branch/master/graph/badge.svg)](https://codecov.io/gh/rogrs/loja)

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.
2. [Yarn][]: We use Yarn to manage Node dependencies.
   Depending on your system, you can install Yarn either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    yarn install

We use yarn scripts and [Webpack][] as our build system.


Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    yarn start

[Yarn][] is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `yarn update` and `yarn install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `yarn help update`.

The `yarn run` command will list all of the scripts available to run for this project.




## Building for production

To optimize the loja application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    yarn test


### Modelo UML

![picture](https://github.com/rogrs/loja/blob/master/loja.png)

### Urls para Apis

[Gatling]: http://gatling.io/
[Node.js]: https://nodejs.org/
[Yarn]: https://yarnpkg.org/
[Webpack]: https://webpack.github.io/
[Angular CLI]: https://cli.angular.io/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
[Leaflet]: http://leafletjs.com/
[DefinitelyTyped]: http://definitelytyped.org/
