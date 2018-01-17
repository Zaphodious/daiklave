(ns daiklave.character-components
  (:require [rum.core :as rum]
            [daiklave.state :as lys :refer [change-element! get-change-value]]
            [com.rpl.specter :as sp :refer [transform setval keypath]]
            [cljs.tools.reader.edn :as edn]
            [clojure.string :as str]
            [daiklave.general-components :as daigen :refer [textfield read-only-field dropdown-keyword banner]]))

(def attribute-keys [:strength :dexterity :stamina :charisma :manipulation :appearance :perception :intelligence :wits])

(def attribute-display-order (into {}
                                   (map (fn [a n]
                                          [a n])
                                        attribute-keys
                                        (range 1 (inc (count attribute-keys))))))
(defn sort-attributes-by [a b]
  (< (a attribute-display-order) (b attribute-display-order)))

(def ability-keys [:archery, :athletics, :awareness, :brawl, :bureaucracy, :craft, :dodge, :integrity, :investigation, :larceny, :linguistics, :lore, :medicine, :melee, :occult, :performance, :presence, :resistance, :ride, :sail, :socialize, :stealth, :survival, :thrown, :war])

(def ability-additional-keys [:craft, :martial-arts])


(defn- inflate-ability-map-imp [old-ab-map]
  (into (sorted-map)
        (map (fn [ab]
               (let [v (get old-ab-map ab)]
                 {ab (if v v 0)}))
             ability-keys)))
(def inflate-ability-map (memoize inflate-ability-map-imp))


(rum/defc chardata < rum/static
  [{char-data-section :view the-path :path}]
  (println (conj the-path :name))
  (println (str "the path is " the-path))
  [:.pagesection
   ;[:img.profile-image {:src (:img char-data-section)}]
   [:h3 "Core Character Info"]
   [:ul
    [:li (textfield "Name" (conj the-path :name))]
    [:li (textfield "Epithet" (conj the-path :subtitle))]
    [:li (textfield "Player" (conj the-path :player))]
    [:li (textfield "Image" (conj the-path :img))]
    [:li (read-only-field "Type" (str/capitalize (name (:type char-data-section))))]
    [:li (dropdown-keyword "Caste" (conj the-path :subtype)
                           (:subtype char-data-section)
                           [:dawn :twilight :night :eclipse :zenith])]
    [:li (textfield "Concept" (conj the-path :concept))]
    [:li (textfield "Anima" (conj the-path :anima))]]])
