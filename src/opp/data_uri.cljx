(ns opp.data-uri
  (:require [opp.data-uri.codec.base64 :as b64]))

;; DataURI

(defn- data-uri-prefix [mime-type encoding]
  (let [prefix (str "data:" mime-type)]
    (if (= :base64 encoding)
      (str prefix ";base64,")
      (str prefix ","))))

(defrecord DataURI [mediatype encoding data]
  Object
  (toString [_]
    (str (data-uri-prefix mediatype encoding)
         data)))

(def ^:private data-uri-re #"data:([^;,]+.*?)?(;base64)?,(.*)")

(defn- data-uri* [uri]
  (when-let [[_ media-type b64? data] (re-find data-uri-re uri)]
    (let [encoding (if b64? :base64 :raw)]
      (DataURI. media-type encoding data))))

(defn data-uri
  "Returns a new DataURI record for the given data-uri string.

  If a DataURI record is provided, it will be returned as-is"
  [uri]
  (if-not (instance? DataURI uri)
    (data-uri* uri)
    uri))

;; Encode/Decode

(defmulti ^:internal transcode-data
  "Translate the data-uri data from one encoding to another."
  (fn [data from to] [from to]))

;; decode
(defmethod transcode-data [:raw    :raw]    [data _ _] data)
(defmethod transcode-data [:base64 :raw]    [data _ _] (b64/decode-string data))

;; encode
(defmethod transcode-data [:base64 :base64] [data _ _] data)
(defmethod transcode-data [:raw    :base64] [data _ _] (b64/encode-string data))


(defn transcode [uri to]
  (let [from (:encoding uri)]
    (if (not= to from)
      (-> (update-in uri [:data] transcode-data from to)
          (assoc :encoding to))
      uri)))

(defn encode [uri]
  (transcode uri :base64))

(defn decode [uri]
  (transcode uri :raw))
