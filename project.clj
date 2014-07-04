(defproject passport "0.1.1"
  :description "Clojure client for the OpenCNAM API"
  :url "https://github.com/jboverfelt/passport"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.3.1"]
                 [clj-http "0.9.2"]]
  :plugins [[lein-cloverage "1.0.2"]]
  :profiles {:dev {:dependencies [[clj-http-fake "0.7.8"]]}})
