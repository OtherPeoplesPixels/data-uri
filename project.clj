(defproject com.otherpeoplespixels/data-uri "0.1.0-SNAPSHOT"
  :description "An isomorphic data-uri library for Clojure[Script]"
  :url "http://github.com/otherpeoplespixels/data-uri"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["target/generated-src"]
  :test-paths ["target/generated-test"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.codec "0.1.0"]
                 [com.novemberain/pantomime "2.3.0"]
                 [org.clojure/clojurescript "0.0-2371" :optional true]]

  :profiles {:dev {:plugins [[lein-cljsbuild "1.0.3"]
                             [com.keminglabs/cljx "0.4.0"]
                             [com.cemerick/clojurescript.test "0.3.1"]]}}

  :cljx {:builds [{:source-paths ["src"]
                   :output-path "target/generated-src"
                   :rules :clj}
                  {:source-paths ["src"]
                   :output-path "target/generated-src"
                   :rules :cljs}
                  {:source-paths ["test"]
                   :output-path "target/generated-test"
                   :rules :clj}
                  {:source-paths ["test"]
                   :output-path "target/generated-test"
                   :rules :cljs}]}

  :cljsbuild {:builds [{:id "test"
                        :source-paths ["target/generated-src" "target/generated-test"]
                        :compiler {:optimizations :whitespace
                                   :output-to "target/test.js"
                                   :pretty-print true}}]
              :test-commands {"test" ["phantomjs" :runner "target/test.js"]}})
