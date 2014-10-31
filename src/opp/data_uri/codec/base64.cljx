(ns opp.data-uri.codec.base64
  #+clj (:require [clojure.data.codec.base64 :as b64]))

#+clj
(defn encode-bytes [bs]
  (String. (b64/encode bs) "UTF-8"))

#+clj
(defn decode-bytes [bs]
  (String. (b64/decode bs) "UTF-8"))

(defn encode-string [s]
  #+clj (encode-bytes (.getBytes s "UTF-8"))
  #+cljs (js/btoa s))

(defn decode-string [s]
  #+clj (decode-bytes (.getBytes s "UTF-8"))
  #+cljs (js/atob s))
