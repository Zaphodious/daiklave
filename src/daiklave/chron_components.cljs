(ns daiklave.chron-components
  (:require [rum.core :as rum]
            [daiklave.general-components :as daigen]
            [daiklave.state :as lys :refer [change-element! get-change-value]]
            [com.rpl.specter :as sp :refer [transform setval]]
            [cljs.tools.reader.edn :as edn]
            [clojure.string :as str]
            [clojure.set :as set]
            [cljs.reader :as reader]
            [daiklave.fragment :as daifrag]))

(rum/defc merit-rank-widget < rum/static
  [fieldname rank-set patho]
  [:.field [:label fieldname]
   [:.entry
    [:ul.rank-list
     (map
       (fn [n]
         [:li
           ;(str (if (rank-set n) n "_"))
          [:button {:type     :button
                     :on-click #(let [nset #{n}
                                      setop (if (rank-set n)
                                              set/difference
                                              set/union)]
                                  (change-element! patho (setop rank-set nset)))}
            (if (rank-set n) n "_")]])
       (range 1 6))]]])


(rum/defc merit-module < rum/static
  [element patho the-key]
  [:span.merit-module
   ;[:h4 (:name element)]
   (daigen/textfield "Name" (conj patho :name))
   (daigen/textarea "Description" (conj patho :description))
   (daigen/textarea "Drawback" (conj patho :drawback))
   (daigen/textfield "Page" (conj patho :page))
   (daigen/dropdown-keyword "Type" (conj patho :type) (:type element) [:innate :story :purchased])
   (daigen/toggle "Repurchasable" (conj patho :repurchasable) (:repurchasable element))
   (merit-rank-widget "Possible Ranks" (:ranks element) (conj patho :ranks))])
   ;(pr-str element)])

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

(rum/defc charm-page < rum/static
  [{:keys [view path] :as viewmap}]
  [:#data-view-body
   (map (fn [a]
          (daigen/section-shortcut (str/capitalize (name a)) (conj path a)))
        (sort daiklave.character-components/ability-all-keys))])

(rum/defc charm-view-by-ability < rum/static
  [{:keys [view path] :as viewmap}]
  (println "viewmappo is " viewmap)
  [:#data-view-body [:.pagesection [:p "Hello"]]])


;[section-name singular-name vec-path the-vec element-component new-element buttons-on-top full-page sort-fn]

;[section-name singular-name vec-path the-vec element-component new-element]

#_{:name "Allies"
   :description "Allies, yo!"
   :drawback "Dey be people n shiii"
   :page 158
   :ranks #{1 3 5}
   :repurchasable true
   :type :story}

