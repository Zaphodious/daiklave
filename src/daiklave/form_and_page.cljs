(ns daiklave.form-and-page
  (:require [rum.core :as rum]
            [daiklave.state :as daistate]))


(defmulti page-for-view :category)

(defmulti form-field-for :fieldtype)
; request map {:element n :fieldtype m :path p}

(rum/defc page-from-path < rum/reactive
  []
  (let [patho (rum/react daistate/current-view)
        app-data (rum/react daistate/app-state)
        viewmap (daistate/fetch-view-for patho app-data)]
       [:h1 (str "Hello, this is the page for " viewmap)]))

(rum/defc app-frame < rum/static
  [pages-vec]
  [:#app-frame
   [:#menubar [:ul [:li "Thingy"]]]
   pages-vec])


(rum/defc page-of < rum/static
  [page-title page-subtitle page-img page-form-vec]
  [:.page
   [:h1.page-title page-title]
   [:.page-section
    [:h2.page-subtitle page-subtitle
     [:img {:src page-img}]]]])

