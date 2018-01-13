(ns daiklave.url
  (:require [cemerick.url :as u]
            [cljs.tools.reader :as r]))


(defn make-fragment [thingy]
  (u/url-encode (pr-str thingy)))

(defn link-fragment-for [thingy]
  (str "#" (make-fragment thingy)))

(defn standard-link-fragment [the-key the-section] (link-fragment-for {:key the-key :section the-section}))

(defn parse-fragment [frag]
  (r/read-string (u/url-decode frag)))