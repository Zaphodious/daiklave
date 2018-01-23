(ns daiklave.chron-components
  (:require [rum.core :as rum]
            [daiklave.general-components :as daigen]
            [daiklave.character-components :as daichar]
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
   (daigen/textfield "Name" (conj patho :name))
   (daigen/textarea "Description" (conj patho :description))
   (daigen/textarea "Drawback" (conj patho :drawback))
   (daigen/textfield "Page" (conj patho :page))
   (daigen/dropdown-keyword "Type" (conj patho :type) (:type element) [:innate :story :purchased])
   (daigen/toggle "Repurchasable" (conj patho :repurchasable) (:repurchasable element))
   (merit-rank-widget "Possible Ranks" (:ranks element) (conj patho :ranks))])

(def merit-compare-magnitude
  {:name 3,  :page 1, :type 2})

(defn make-compare-fn
  [magnitude-map]
  (fn [a b]
    (reduce +
            (map
              (fn [[k v]]
                (* v (compare (k a) (k b))))
             magnitude-map))))

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
                   (make-compare-fn {:name 3,  :page 1, :type 2})))

(rum/defc charm-module < rum/static
  [element patho the-key]
  [:span.charm-module {:key the-key}
   (daigen/textfield "Name" (conj patho :name))
   (daigen/read-only-field "Ability" (str/capitalize (name (:ability element))))
   (daigen/dot-dropdown "Prereq Rank" (conj patho :mins 1) (range 1 6) (-> element :mins last))
   (daigen/dot-dropdown "Prereq Essence" (conj patho :mins 0) (range 1 6) (-> element :mins first))
   (daigen/textfield "Keywords" (conj patho :keywords))
   (daigen/textfield "Page" (conj patho :page))
   (daigen/dropdown-keyword "Type" (conj patho :type) (:type element) (sort [:simple, :supplemental, :reflexive, :permanent]))
   (daigen/textfield "Duration" (conj patho :duration))
   (daigen/textarea "Description" (conj patho :description))])

(rum/defc charm-page < rum/static
  [{:keys [view path] :as viewmap}]
  [:#data-view-body
   (map (fn [a]
          (daigen/section-shortcut (str/capitalize (name a)) (conj path a)))
        (sort daichar/ability-all-keys))])

(rum/defc charm-view-by-ability < rum/static
  [{:keys [view path] :as viewmap}]
  (daigen/vec-view "Charms"
                   "Charm"
                   path
                   view
                   charm-module
                   {:name ""
                    :cost ""
                    :mins [1 2]
                    :ability (last path)
                    :keywords ""
                    :type :supplemental
                    :duration "Instant"
                    :prereq-charms "None"
                    :description ""}
                   false
                   true
                   (make-compare-fn {:mins 5 :name 3})))


