# <img src="https://raw.githubusercontent.com/OwnYourData/oyd-pia/master/src/main/resources/logo_grey.png" width="92"> Datentresor

[![Build Status](https://travis-ci.org/OwnYourData/oyd-pia.svg?branch=master)](https://travis-ci.org/OwnYourData/oyd-pia)
[![Dependency Status](https://david-dm.org/OwnYourData/oyd-pia.svg)](https://david-dm.org/OwnYourData/oyd-pia) 

## Security

Before building your pia make sure you adapt the security settings in:

* `application.yml`
* `application-dev.yml`
* `application-prod.yml`
    
_clientId_ and _secret_ in `application.yml` must be in sync with `auth.ouath2.service.js`

## Developers
This application was generated using JHipster, you can find documentation and help at [https://jhipster.github.io](https://jhipster.github.io).

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Grunt][] as our build system. Install the grunt command-line tool globally with:

    npm install -g grunt-cli

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn
    grunt

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

### Building for production

To optimize the pia client for production, run:

    mvn -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

### Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript` and can be run with:

    grunt test



