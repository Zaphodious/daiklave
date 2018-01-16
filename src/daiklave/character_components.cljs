(ns daiklave.character-components
  (:require [rum.core :as rum]
            [daiklave.state :as lys :refer [change-element! get-change-value]]
            [com.rpl.specter :as sp :refer [transform setval keypath]]
            [cljs.tools.reader.edn :as edn]
            [clojure.string :as str]
            [daiklave.general-components :as daigen :refer [textfield read-only-field dropdown banner]]))


(def attribute-keys [:strength :dexterity :stamina :charisma :manipulation :appearance :perception :intelligence :wits])


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
    [:li (dropdown "Caste" (conj the-path :subtype)
                   char-data-section {:dawn "Dawn" :twilight "Twilight" :night "Night" :eclipse "Eclipse" :zenith "Zenith"})]
    [:li (textfield "Concept" (conj the-path :concept))]
    [:li (textfield "Anima" (conj the-path :anima))]]])


;(rum/defc dep-attdata < rum/static
;  [attsection charkey]
;  [:.pagesection
;   [:h3 "Attributes"]
;   [:ul (map
;         (fn [key]
;           (let [att-value (key attsection)]
;             [:li (dotspinner (str/capitalize (name key)) att-value 1 5 charkey (fn [a e] (setval [(keypath :attributes) (keypath key)] e a)))]))
;
;         attribute-keys)]])

(defn att-section-helper
  [att-section char-path]
  (let [att-path (conj char-path :attributes)]
    (println "att-path is " att-path)
    [:div.pagesection
     [:h3 "Attributes"]
     (map
       (fn [[k v]]
         (let [spcific-att-path (conj att-path k)]
           (daigen/dotview (str/capitalize (name k))
                           spcific-att-path
                           (range 1 5)
                           v)))
       att-section)]))



(rum/defc att-section < rum/state
  [attsection char-path]
  (println "att path is " (into char-path [:attributes :strength]))
  [:.pagesection
   [:h3 "Attributes"]
   (map (fn [a] [:div {:key (pr-str char-path " value of " a)}
                 (daigen/dotspinner
                        (str/capitalize (name (first a)))
                        (into char-path [:attributes (first a)])
                        (range 1 6)
                        (last a))])
        attsection)])