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

(defn get-named-elements
  "Returns a lazy seq of vectors [rulebook-id, any number of matching elements]"
  [{:keys [thing-name path-before-id id-vec path-after-id exact-match?]}]
  (if (and thing-name path-before-id id-vec)
    (->> id-vec
         (map #(:view (fetch-view-for (vec (concat path-before-id [%] path-after-id)))))
         (map (fn [a] (sp/select
                        (sp/walker #(if exact-match?
                                      (= thing-name (str (:name %)))
                                      (str/includes? (str/lower-case (str (:name %))) (str/lower-case thing-name))))
                        a)))
         (map #(conj %2 %1) id-vec)
         (filter second)
         (map reverse)
         (map vec))))

(defn put-id-into-named-elements
  [[id & elems]]
  (map #(assoc % :parent-id id) elems))

(defn remove-later-duplicates
  [unique-field-key element-seq]
  (let [unique-atom (atom #{})]
    (filter
      (fn [a]
        (let [unique-field (get a unique-field-key)]
          (when (not (@unique-atom unique-field))
            (do (swap! unique-atom #(conj % unique-field))
                true))))
      element-seq)))

(defn get-setting-for-key [setting-key]
  (:view (fetch-view-for [:settings setting-key])))

(defn change-element!
  [element-path change-val]
  ;(println "you gotsa " element-path ", and you gotsa " change-val)
  (if (and element-path change-val
           (not (= change-val (:view (fetch-view-for element-path)))))
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
                   (conj
                     {:modal-showing   modal-type
                      :modal-title     modal-title}
                     modal-arguments-map))
  nil)

(defn apply-modal-and-hide
  [path change-val]
  (println "\uD83D\uDD25 path is " path " with val " change-val)
  ;Apply Modal
  (change-element! path change-val)
  ;Hide
  (change-element!
    [:modal]
    {:modal-showing   :none}))


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