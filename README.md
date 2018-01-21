# hack-browser

This project build by descjop v0.7.0

Hack Browser is completely hackable browser with embedded lisp interpreter. Like Emacs.

## Requirements

* leiningen 2.6.x +
* node v0.12.x +
* grunt v0.1.13 +

### (if you don't install grunt yet.)

```
$ npm install -g grunt-cli
```


#### development mode

development mode use figwheel. run alias `descjop-figwheel`.  before run application.
Open other terminal window.

```
$ lein descjop-figwheel
```

and you can run Electron(Atom-Shell) app.

On Windows:

```
$ .\electron\electron.exe app/dev
```

On Linux:

```
$ ./electron/electron app/dev
```

On OS X:

```
$ ./electron/Electron.app/Contents/MacOS/Electron app/dev
```

## License

Copyright ©  Anurag Peshne

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
