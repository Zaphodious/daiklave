(ns daiklave.state
  (:require [daiklave.sample-data :as samples]
            [clojure.string :as str]
            [cljs-time.core :as time]
            [daiklave.default-data :as daifault]
            [cemerick.url :as url :refer [url url-encode]]
            [daiklave.fragment :as dfrag]
            [com.rpl.specter :as sp]
            [com.rpl.specter.navs :as spn]))

(defn get-current-url-frag []
  (dfrag/dep-parse-fragment (:anchor (url (.-href js/location)))))

(def current-view (atom (get-current-url-frag)))

(defn now [] (time/now))

(defonce changelistener
         (js/addEventListener
           "hashchange" (fn [a]
                            (println "changing current view to " (get-current-url-frag))
                            (swap! current-view (fn [b] (get-current-url-frag))))))

(def
  app-state
  (atom (into
          (sp/transform
            [:chrons]
            (fn [a] (conj a {"0" daifault/chron}))
            samples/sample-state))))


(defn unwrap-if-singular [coll]
  (if (= 1 (count coll))
      (first coll)
      coll))

(defn fetch-view-for
  ([] (fetch-view-for @current-view @app-state))
  ([path] (fetch-view-for path @app-state))
  ( [frag state]
    (let [sanifrag (if (or (nil? frag)
                           (empty? frag)
                           (= "" frag))
                     [:home]
                     frag)
          pathvec (apply sp/keypath sanifrag)]
      {:path sanifrag
       :view (unwrap-if-singular (sp/select pathvec state))})))

(defn change-element!
  [element-path change-val]
  (let [change-fn (if (fn? change-val)
                    change-val
                    (fn [a] change-val))]
    (sp/transform [sp/ATOM
                   (apply sp/keypath element-path)]
                change-fn app-state)))

(defn get-change-value [e] (.. e -target -value))