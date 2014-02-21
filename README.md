# passport [![Build Status](https://travis-ci.org/jboverfelt/passport.png?branch=master)](https://travis-ci.org/jboverfelt/passport) [![Dependencies Status](http://jarkeeper.com/jboverfelt/passport/status.png)](http://jarkeeper.com/jboverfelt/passport)

A Clojure client for the [OpenCNAM](https://www.opencnam.com/) API.

[API Documentation](http://jboverfelt.github.io/passport/)

## Installation

#### Leiningen
```clojure
[passport "0.1.0"]
```
#### Maven
```xml
<dependency>
  <groupId>passport</groupId>
  <artifactId>passport</artifactId>
  <version>0.1.0</version>
</dependency>
```
#### Gradle
```groovy
compile "passport:passport:0.1.0"
```

## Usage

[OpenCNAM](https://www.opencnam.com/) is a lightweight API for caller ID information.

Acceptable phone number formats and error responses are detailed [here](https://www.opencnam.com/docs/v2/apiref)

The differences between the Hobbyist and Professional tiers are detailed [here](https://www.opencnam.com/docs/v2)

Returns a Clojure map containing the Caller ID results or an error and the status code

```clojure

(ns my.ns
  (:require [passport.core :refer :all]))
  
;; Hobbyist Tier query

(caller-id "+16502530000")
;; => {:price 0, :uri "/v2/phone/%2B16502530000", :created "2012-10-12T06:53:05.194858", 
;;     :number "+16502530000",
;;     :updated "2014-02-20T18:59:27.279412", :name "GOOGLE INC"}

; Query for a number that doesn't exist in the CNAM database

(caller-id "+16502530001")
;; => {:error "No CNAM information available for phone number", :status 404}

;; Professional Tier Query
; sid and token can either be passed in or 
; set in OPEN_CNAM_SID and OPEN_CNAM_TOKEN env variables

(caller-id "+16502530000" "sid" "token")

```

## License

Copyright © 2014- Justin Overfelt

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
