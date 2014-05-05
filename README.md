# BitFunding #

## Build & Run ##

* You must create a config file for OAuth and place it "src/main/webapp/WEB-INF/secrets.json" see "src/main/webapp/WEB-INF/secrets_example.json" for details about how the file should look like.

```sh
$ cd BitFunding
$ ./sbt
> container:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.
