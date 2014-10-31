(ns opp.data-uri-test
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  (:require [opp.data-uri :as data-uri]
            #+clj [clojure.test :as t :refer (is deftest testing)]
            #+cljs [cemerick.cljs.test :as t]))


(def empty-data-uri "data:,")

(deftest data-uri-equiv-test
  (is (= (data-uri/data-uri empty-data-uri)
         (data-uri/data-uri empty-data-uri))
      "equivalent"))

(def data "Hello World!")
(def data-b64 "SGVsbG8gV29ybGQh")
(def raw-data-uri (str "data:," data))
(def b64-data-uri (str "data:;base64," data-b64))

(deftest data-uri-test
  (doseq [[input expected]
          [[empty-data-uri {:mediatype nil :encoding :raw :data ""}]
           [raw-data-uri {:mediatype nil :encoding :raw :data "Hello World!"}]
           [b64-data-uri {:mediatype nil :encoding :base64 :data "SGVsbG8gV29ybGQh"}]]]
    (let [actual (select-keys (data-uri/data-uri input) #{:mediatype :encoding :data})]
      (is (= actual expected)))))

(deftest encode-decode-test
  (let [raw (data-uri/data-uri raw-data-uri)
        b64 (data-uri/data-uri b64-data-uri)]

    (is (= (data-uri/encode b64) b64)
        "uri is already encoded")

    (is (= (data-uri/encode raw) b64)
        "uri is now encoded")

    (is (= (data-uri/decode raw) raw)
        "uri is already decoded")

    (is (= (data-uri/decode b64) raw)
        "uri is now decoded")))
