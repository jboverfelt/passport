(defproject passport "0.1.2"
  :description "Clojure client for the OpenCNAM API"
  :url "https://github.com/jboverfelt/passport"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.4.0"]
                 [clj-http "1.1.2"]]
  :plugins [[lein-cloverage "1.0.6"]]
  :profiles {:dev {:dependencies [[clj-http-fake "1.0.1"]]}})
