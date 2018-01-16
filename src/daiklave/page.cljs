(ns daiklave.page
  (:require [daiklave.url :as daifrag]
            [rum.core :as rum]
            [clojure.string :as str]
            [daiklave.general-components :as daigen]
            [daiklave.state :as daistate]
            [daiklave.character-components :as daichar]))

(rum/defc multi-page < rum/static
  [viewmap]
  (daigen/raw-element-div viewmap))

(rum/defc home-page < rum/reactive
  []
  [:div
   [:.pagesection [:p "Welcome to Anathema: Reincarnated"]]
   [:.pagesection [:h2 "Utilities"]]
   [:.pagesection [:h2 "Chrons"]]
   (daigen/raw-element-div {:view (:chrons (rum/react daistate/app-state)), :path [:chrons]})])

(rum/defc chron-page < rum/static
  [{chron-data :view the-path :path}]
  (println "chron-data is " chron-data)
  [:div
   (daigen/banner (:name chron-data)
                  (:description chron-data)
                  (:img chron-data))
   [:.pagesection
    [:ul
     [:li (daigen/textfield "Name" (conj the-path :name))]]]
   (daigen/raw-element-div vals)
   (daigen/section-shortcut "Merits" :merits chron-data)
   (daigen/section-shortcut "Charms" :charms chron-data)
   (daigen/section-shortcut "Artifacts" :artifacts chron-data)
   (daigen/raw-element-div {:path (conj the-path :characters)
                            :view (:characters chron-data)})])


(rum/defc charsheet < rum/static
  [{char-data-section :view the-path :path :as total-view}]
  [:div
   (daigen/banner (:name char-data-section) (:subtitle char-data-section) (:img char-data-section))
   (daichar/chardata total-view)
   (daichar/stat-section "Attributes"
                         (:attributes char-data-section)
                         (range 1 6)
                         (conj the-path :attributes))])

(defn page-fn-for
  [{:keys [view path] :as viewmap}]
  (if
    (not (:category view))
    #(multi-page viewmap)
    (case (:category view)
      :home #(home-page)
      :chron #(chron-page viewmap)
      :character #(charsheet viewmap))))
