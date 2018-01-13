(ns daiklave.core
  (:require [rum.core :as rum]
            [daiklave.character-components :as daichar]
            [daiklave.category-components :as daicat]
            [daiklave.chron-components :as daichron]
            [daiklave.state :as daistate]
            [daiklave.url :as daifrag]
            [clojure.string :as str]))

(enable-console-print!)
(rum/defc home-page < rum/reactive
  []
  [:#data-view-body

    [:.pagesection [:p "Welcome to Anathema: Reincarnated"]]
    [:.pagesection [:h2 "Utilities"]]
    [:.pagesection [:h2 "Chrons"]]
   (daicat/category-view (daistate/filter-state-by :chron (rum/react daistate/app-state)))])




(rum/defc content-area < rum/static
  [thing-to-display]
  (println "thing is " thing-to-display)
  [:#data-view-body
   (cond
     (:app-home thing-to-display) (home-page)
     (:elements thing-to-display) (daicat/category-view (:elements thing-to-display))
     (= (:category thing-to-display) :character) (daichar/charsheet thing-to-display)
     (= (:category thing-to-display) :chron) (daichron/chron-page thing-to-display @daistate/current-view @daistate/app-state))])



(rum/defc content-area-reactive < rum/reactive
  []
  (println "updating")
  (content-area (daistate/fetch-current-view (rum/react daistate/current-view) (rum/react daistate/app-state))))



(defn href [n] {:href (str "#" n)})
(rum/defc menu []
  [:ul
   [:li [:h3 [:a (href "home") "Home"]]]
   ;[:li [:h3 [:a (href "characters") "Characters"]] [:ul [:li [:a "New"]]]]
   [:li [:h3 [:a (href "chrons") "Chronicles"]]
    [:ul [:li [:a "New"]]]]
   [:li [:h3 [:a "Dice Roller"]]]
   [:li [:h3 [:a "Settings"]]]])


(defn get-page-title-for [entity]
  (cond (= (:category entity) :chron) (:name entity)
        (= (:category entity) :character) [:span [:a {:href (daifrag/standard-link-fragment (:chron entity) "root")}
                                                  (str (:name (get @daistate/app-state (:chron entity))))]
                                                 (str " / " (:name entity))]))

(rum/defc titlebar < rum/reactive
  []
  [:h1 (get-page-title-for (daistate/fetch-current-view (rum/react daistate/current-view) (rum/react daistate/app-state)))])

(rum/mount (titlebar)
           (. js/document (getElementById "titlebar")))

(rum/mount (menu)
           (. js/document (getElementById "menubar")))

(rum/mount (content-area-reactive)
           (. js/document (getElementById "app")))




(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)

