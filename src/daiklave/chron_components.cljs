(ns daiklave.chron-components
  (:require [rum.core :as rum]
            [daiklave.general-components :as daigen]
            [daiklave.state :as lys :refer [change-element! get-change-value]]
            [com.rpl.specter :as sp :refer [transform setval]]
            [cljs.tools.reader.edn :as edn]
            [clojure.string :as str]
            [cljs.reader :as reader]
            [daiklave.fragment :as daifrag]))

(rum/defc merit-module < rum/static
  [element patho the-key]
  [:span.merit-module
   (pr-str element)])

(def merit-compare-magnitude
  {:name 3,  :page 1, :type 2})

(defn compare-merit
  [a b]
  (reduce +
    (map
      (fn [[k v]]
        (* v (compare (k a) (k b))))
      merit-compare-magnitude)))

(rum/defc merit-view < rum/static
  [{:keys [view path] :as viewmap}]
  (println "the view" view)
  (daigen/vec-view "Merits"
                   "Merit"
                   path
                   view
                   merit-module
                   {:name ""
                    :description ""
                    :drawback ""
                    :page 158
                    :ranks #{1 3 5}
                    :repurchasable true
                    :type :story}
                   false
                   true
                   compare-merit))

;[section-name singular-name vec-path the-vec element-component new-element buttons-on-top full-page sort-fn]

;[section-name singular-name vec-path the-vec element-component new-element]

#_{:name "Allies"
   :description "Allies, yo!"
   :drawback "Dey be people n shiii"
   :page 158
   :ranks #{1 3 5}
   :repurchasable true
   :type :story}