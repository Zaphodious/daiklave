(ns daiklave.fragment
  (:require [cemerick.url :as u]
            #?(:cljs [cljs.tools.reader :as r]
               :clj [clojure.tools.reader :as r])
            [com.rpl.specter :as s]))


(defn make-fragment [thingy]
  (u/url-encode (pr-str thingy)))

(defn link-fragment-for [thingy]
  (str "#" (make-fragment thingy)))

(defn- make-path-vec [path-elements]
  (let [pathvec (if (seq? (first path-elements))
                  (first path-elements)
                  (into [] path-elements))]
    (into [] pathvec)))

(defn- pre-hash-path-frag [path-elements]
  (u/url-encode
    (pr-str
      (into []
        path-elements))))

(defn path-frag
   [& path-elements]
   (str "#" (pre-hash-path-frag
              (if (vector? (first path-elements))
                (first path-elements)
                path-elements))))

(defn dep-parse-fragment [frag]
  (r/read-string (u/url-decode frag)))