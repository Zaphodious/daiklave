(ns daiklave.chron-components
  (:require [rum.core :as rum]
            [daiklave.general-components :as daigen]
            [daiklave.category-components :as daicat]
            [daiklave.state :as lys :refer [change-element! get-change-value]]
            [com.rpl.specter :as sp :refer [transform setval]]
            [cljs.tools.reader.edn :as edn]
            [clojure.string :as str]
            [cljs.reader :as reader]))

(rum/defc chron-page < rum/static
  [chron-data app-state]
  [:div
   (daigen/banner (:name chron-data) (str "A " (str/capitalize (name (:type chron-data))) " adventure")
                  (:img chron-data))
   [:.pagesection
    [:ul [:li (daigen/textfield "Name" :name chron-data)]]]
   (daicat/category-view (filter (fn [a] (= (:key chron-data) (:chron a))) (daiklave.state/filter-state-by :character app-state)))
   ])