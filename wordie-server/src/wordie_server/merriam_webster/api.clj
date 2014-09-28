(ns wordie-server.merriam-webster.api
  (:import java.net.URLEncoder
           java.io.ByteArrayInputStream)
  (:require [wordie-server.merriam-webster.keys :as mw-keys]
            [clojure.data.zip.xml :refer :all]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip :as zf]
            [clojure.string :as clj-str]))

(def base-url
  "http://www.dictionaryapi.com/api/v1/references/")

(def dictionary-url
  (str base-url "collegiate/xml/"))

(defn- zip-string
  [s]
  (zip/xml-zip (xml/parse (ByteArrayInputStream.
                            (.getBytes s "UTF-8")))))

(defn- text-preserve
  [loc]
  (let [string-node (xml-> loc zf/descendants zip/node string?)]
    (clj-str/join " " (map clj-str/trim string-node))))

(defn parse-xml
  [s]
  (let [xz (zip-string s)]
    (for [entry (xml-> xz :entry)]
      {:word (xml1-> entry :ew text)
       :spelling (xml1-> entry :hw text)
       :definitions (xml-> entry :def :dt text-preserve)})))

(defn build-dictionary-query-url
  [s]
  (str dictionary-url (URLEncoder/encode (.toLowerCase s)) "?key=" mw-keys/dictionary))

(defn query-dictionary
  [s]
  (parse-xml (slurp (build-dictionary-query-url s))))

