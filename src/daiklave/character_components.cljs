(ns daiklave.character-components
  (:require [rum.core :as rum]
            [daiklave.state :as lys :refer [change-element! get-change-value]]
            [com.rpl.specter :as sp :refer [transform setval keypath]]
            [cljs.tools.reader.edn :as edn]
            [clojure.string :as str]
            [daiklave.field-components :as field :refer [textfield read-only-field dropdown dotspinner]]))


(def attribute-keys [:strength :dexterity :stamina :charisma :manipulation :appearance :perception :intelligence :wits])

(rum/defc charbanner < rum/static
  [charname charsubtitle charimg]
  [:.pagesection.banner
   [:img.banner-image {:src charimg}]
   [:h1.char-banner-title charname]
   [:h2.char-banner-subtitle charsubtitle]
   ])

(rum/defc chardata < rum/static
  [char-data-section]
  [:.pagesection
   ;[:img.profile-image {:src (:img char-data-section)}]
   [:h3 "Core Character Info"]

   [:ul
    [:li (textfield "Name" :name char-data-section)]
    [:li (textfield "Epithet" :subtitle char-data-section)]
    [:li (textfield "Player" :player char-data-section)]
    [:li (textfield "Image" :img char-data-section)]
    [:li (read-only-field "Type" :type char-data-section)]
    [:li (dropdown "Caste" :subtype char-data-section {:dawn "Dawn" :twilight "Twilight" :night "Night" :eclipse "Eclipse" :zenith "Zenith"})]
    [:li (textfield "Concept" :concept char-data-section)]
    [:li (textfield "Anima" :anima char-data-section)]
    ]])

(rum/defc attdata < rum/static
  [attsection charkey]
  [:.pagesection
   [:h3 "Attributes"]
   [:ul (map
      (fn [key]
        (let [att-value (key attsection)]
          [:li (dotspinner (str/capitalize (name key)) att-value 1 5 charkey (fn [a e] (setval [(keypath :attributes) (keypath key)] e a)))]
          ))
      attribute-keys)]])

(rum/defc charsheet < rum/static
  [char-data-section]
  [:#data-view-body
   (charbanner (:name char-data-section) (:subtitle char-data-section) (:img char-data-section))
   (chardata char-data-section)
   (attdata (:attributes char-data-section) (:key char-data-section))])