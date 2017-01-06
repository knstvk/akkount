#### Using Polymer server instead of Tomcat

`gradle servePolymer`

Do not forget to change paths accordingly i.e.:

* `<base href="/">`
* `<cuba-app api-url="http://localhost:8080/app/rest/">`

#### Build and run without gradle

* `npm install`
* `bower install`
* `gulp serve`

#### API Docs

While running Polymer server you can access API reference of each component used in your project in the following way:

[http://localhost:8081/components/cuba-data/](http://localhost:8081/components/cuba-data/)