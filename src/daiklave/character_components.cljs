(ns daiklave.character-components
  (:require [rum.core :as rum]
            [daiklave.state :as daistate :refer [change-element! get-change-value]]
            [com.rpl.specter :as sp :refer [transform setval keypath]]
            [cljs.tools.reader.edn :as edn]
            [clojure.string :as str]
            [daiklave.general-components :as daigen :refer [textfield read-only-field dropdown-keyword banner]]
            [daiklave.fragment :as daifrag]
            [clojure.set :as set]))

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

(def ability-all-keys (into ability-keys ability-additional-keys))

(def caste-abilities {:dawn     #{:archery, :awareness, :brawl, :martial-arts, :dodge, :melee, :resistance, :thrown, :war}
                      :zenith   #{:athletics, :integrity, :performance, :lore, :presence, :resistance, :survival, :war}
                      :twilight #{:bureaucracy, :craft, :integrity, :investigation, :linguistics, :lore, :medicine, :occult}
                      :night    #{:athletics, :awareness, :dodge, :investigation, :larceny, :ride, :stealth, :socialize}
                      :eclipse  #{:breaucracy, :larceny, :linguistics, :occult, :presence, :ride, :sail, :socialize}})

(def intimacy-intensities [:defining :major :minor])

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
    [:li (textfield "Epithet" (conj the-path :description))]
    [:li (textfield "Player" (conj the-path :player))]
    [:li (textfield "Image" (conj the-path :img))]
    [:li (read-only-field "Type" (str/capitalize (name (:type char-data-section))))]
    [:li (dropdown-keyword "Caste" (conj the-path :subtype)
                           (:subtype char-data-section)
                           [:dawn :twilight :night :eclipse :zenith])]
    [:li (textfield "Concept" (conj the-path :concept))]
    [:li (textfield "Anima" (conj the-path :anima))]
    [:li (dropdown-keyword "Supernal"
                           (conj the-path :supernal)
                           (:supernal char-data-section)
                           (into (sorted-set)
                            (set/intersection ((:subtype char-data-section) caste-abilities)
                                              (:favored-abilities char-data-section))))]]])

(defn get-specialty-attribute-options
  [specialty-module-path]
  (let [new-path (into [] (drop-last 2 specialty-module-path))
        ability-path (conj new-path :abilities)
        extra-ability-path (conj new-path :abilities-additional)
        {ability-section :view}
        (daiklave.state/fetch-view-for ability-path)
        {additional-abilities :view}
        (daiklave.state/fetch-view-for extra-ability-path)
        reduced-additionals (map (fn [[k v _]] [k v]) additional-abilities)
        at-least-one-point (filter #(< 0 (last %))
                                    (into ability-section
                                          reduced-additionals))]
    (println reduced-additionals)
   (into (sorted-set)
     (map first at-least-one-point))))


(rum/defc specialty-module < rum/static
  [element patho the-key]
  (println "path for " element " is " patho)
  [:span {:key the-key}
   (daigen/dropdown-keyword-fieldless (str "Specialty " the-key)
                                      (conj patho 0)
                                      (first element)
                                      (get-specialty-attribute-options patho))
   [:input {:value (last element)
            :type "text"
            :key (str the-key " text")
            :on-change (fn [e] (change-element! (conj patho 1) (str (get-change-value e))))}]])

(rum/defc intimacy-module < rum/static
  [[intensity intimacy-type description :as element] patho the-key]
  (println "\uD83D\uDE31 Making intimacy module for " element)
  [:span {:key the-key}
   (daigen/dropdown-keyword-fieldless (str "Intimacy" the-key)
                                      (conj patho 0)
                                      intensity
                                      intimacy-intensities)
   [:input {:value intimacy-type
            :type "text"
            :key (str the-key " type")
            :on-change #(change-element! (conj patho 1) (str (get-change-value %)))}]
   [:input {:value description
            :type "text"
            :key (str the-key " description")
            :on-change #(change-element! (conj patho 2) (str (get-change-value %)))}]])

;[fieldvalue select-map fieldoptions prewrap-onchange-fn beauty-fn keyprefix]


