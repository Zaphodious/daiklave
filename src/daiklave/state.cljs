(ns daiklave.state
  (:require [daiklave.sample-data :as samples]
            [clojure.string :as str]
            [cljs-time.core :as time]
            [daiklave.default-data :as daifault]
            [cemerick.url :as url :refer [url url-encode]]
            [daiklave.fragment :as dfrag]
            [com.rpl.specter :as sp]
            [com.rpl.specter.navs :as spn]
            [clojure.browser.dom :as dom]
            [daiklave.data-help :as daihelp]))

(defn get-current-url-frag []
  (dfrag/parse-fragment (:anchor (url (.-href js/location)))))

(def current-view (atom (get-current-url-frag)))

(defn now [] (time/now))

(defonce changelistener
         (js/addEventListener
           "hashchange" (fn [a]
                          (swap! current-view (fn [b] (get-current-url-frag))))))



(def
  app-state
  (atom (->> samples/sample-state
          (sp/transform
            [:rulebooks]
            (fn [a] (conj a {"0" daifault/core-book})))
          (sp/transform
            [:rulebooks]
            (fn [a] (conj a {"1" daifault/miracles}))))))


(defn get-screen-size []
  {:width  (-> js/document .-documentElement .-clientWidth)
   :height (-> js/document .-documentElement .-clientHeight)})

(def screen-size
  (atom (get-screen-size)))

(defonce resizelistener
         (js/addEventListener
           "resize"
           (fn [a]
             (println "RESIZE! " (get-screen-size))
             (reset! screen-size (get-screen-size)))))

(defn unwrap-if-singular [coll]
  (if (= 1 (count coll))
    (first coll)
    coll))

(defn fetch-view-for
  ([] (fetch-view-for @current-view @app-state))
  ([path] (fetch-view-for path @app-state))
  ([path state]
   (let [sanifrag (if (or (nil? path)
                          (empty? path)
                          (= "" path))
                    [:home]
                    path)
         pathvec (apply sp/keypath sanifrag)
         the-view (unwrap-if-singular (sp/select pathvec state))]
     {:path sanifrag
      :view the-view})))



(defn- search-in-map-of-named [map-of-elements query fields]
  (filter
    (fn [[k v]]
      (println k)
      (reduce #(or %1 %2) (map (fn [nk] (str/includes? (str (get v nk)) query)) fields)))
    map-of-elements))

(defn- search-in-seq [seq-of-elements query fields]
  (filter
    (fn [v]
      (reduce #(or %1 %2) (map (fn [nk] (str/includes? (str (get v nk)) query)) fields)))
    seq-of-elements))

(defn search-in
  ([path query] (search-in path query [:name]))
  ([path query fields]
   (let [element-coll (:view (fetch-view-for path))]
     {:path path
      :view
            (if (map? element-coll)
              (map second (search-in-map-of-named element-coll query fields))
              (search-in-seq element-coll query fields))})))

(defn get-named-elements [{:keys [thing-name path-before-id id-vec path-after-id relevant-fields]}]
  (if (and thing-name path-before-id id-vec)
    (->> id-vec
         (map #(:view (fetch-view-for (vec (concat path-before-id [%] path-after-id)))))
         (map (fn [a] (sp/select
                        (sp/walker #(str/includes? (str/lower-case (str (:name %))) thing-name))
                        a)))
         (map #(conj %2 %1) id-vec)
         (map reverse))))


(defn get-setting-for-key [setting-key]
  (:view (fetch-view-for [:settings setting-key])))

(defn change-element!
  [element-path change-val]
  (println "you gotsa " element-path ", and you gotsa " change-val)
  (if (and element-path change-val)
    (let [change-fn (if (fn? change-val)
                      change-val
                      (fn [a] change-val))]
      (sp/transform [sp/ATOM
                     (apply sp/keypath element-path)]
                    change-fn app-state))))

(defn get-change-value [e] (.. e -target -value))

(defn show-modal
  [modal-type modal-title modal-arguments-map]
  (change-element! [:modal]
                   {:modal-showing   modal-type
                    :modal-arguments modal-arguments-map
                    :modal-title     modal-title})
  nil)

(defn apply-modal-and-hide
  [path change-val]
  (println "\uD83D\uDD25 path is " path " with val " change-val)
  ;Apply Modal
  (change-element! path change-val)
  ;Hide
  (change-element!
    [:modal]
    {:modal-showing   :none
     :modal-arguments {}}))

(defn viewmaps-for-children [patho]
  (let [{:keys [path view]
         :as   first-viewmap}
        (fetch-view-for patho)]
    (->> view
         (map
           (fn [[k v]]
             (conj path k)))
         (map
           fetch-view-for))))

(def current-paste-handler (atom (fn [a] (println "thing is " a))))

(defonce pastelistener
         (js/addEventListener
           "paste" (fn [a]
                     (when
                       (and (.hasFocus js/document)
                            (not (= "INPUT" (-> js/document .-activeElement .-tagName)))
                            (not (= "TEXTAREA" (-> js/document .-activeElement .-tagName))))
                       (@current-paste-handler (-> a .-clipboardData (.getData "text/plain")))))))