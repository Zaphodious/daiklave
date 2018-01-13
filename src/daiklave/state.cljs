(ns daiklave.state
  (:require [daiklave.sample-data :as samples]
            [clojure.string :as str]
            [cljs-time.core :as time]
            [cemerick.url :as url :refer [url url-encode]]
            [daiklave.url :as dfrag]
            [com.rpl.specter :as sp]))

(defn get-current-url-frag []
  (dfrag/parse-fragment (:anchor (url (.-href js/location)))))

(def current-view (atom (get-current-url-frag)))
;"424242"


(defn now [] (time/now))


(defonce changelistener
         (js/addEventListener "hashchange" (fn [a]
                                             (println "changing current view to " (get-current-url-frag))
                                             (swap! current-view (fn [b] (get-current-url-frag))))))

(def
  app-state
  (atom samples/sample-state))

(defn filter-state-by [entitytype state]
  (filter (fn [a] (= (:category a) entitytype)) (vals state)))
(defn sort-by-last-accessed [element-list]
  (sort-by :last-accessed element-list))

(def category-names
  {"characters" :character
   "chrons"     :chron})

(defn fetch-current-view
  ([] (fetch-current-view @current-view @app-state))
  ([{the-view :key the-section :section} the-app-state]
   (cond
       (nil? the-view)
       {:app-home true :name "Anathema: Reincarnated"}
       (= {:key the-view} "home")
       {:app-home true :name "Anathema: Reincarnated"}
       ((-> category-names (keys) (set)) {:key the-view})
       {:name (str/capitalize (name {:key the-view}))
        :elements
              (->> the-app-state
                   (filter-state-by (get category-names the-view))
                   (sort-by-last-accessed))}
       :default
       (get the-app-state the-view))))


(defn change-element!
  [element-key change-fn]
  (println "element being called")
  (swap! app-state (fn [a] (assoc a element-key (change-fn (get a element-key))))))


(defn get-change-value [e] (.. e -target -value))