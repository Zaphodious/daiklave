(ns daiklave.core
  (:require [rum.core :as rum]
            [daiklave.character-components :as daichar]
            [daiklave.category-components :as daicat]
            [daiklave.state :as daistate]
            [clojure.string :as str]))

(enable-console-print!)

(rum/defc content-area < rum/static
  [thing-to-display]
  (if (:elements thing-to-display) (daicat/category-view (:elements thing-to-display))
                                   (daichar/charsheet thing-to-display)))

(rum/defc content-area-reactive < rum/reactive
  []
  (println "updating")
  (content-area (daistate/fetch-current-view (rum/react daistate/current-view) (rum/react daistate/app-state))))

(defn href [n] {:href (str "#" n)})
(rum/defc menu []
  [:ul
   [:li [:h3 [:a (href "characters") "Characters"]]
    [:ul [:li [:a "New"]]]]
   [:li [:h3 [:a (href "chrons") "Chronicles"]]
    [:ul [:li [:a "New"]]]]
   [:li [:h3 [:a "Dice Roller"]]]
   [:li [:h3 [:a "Settings"]]]
   ])

(rum/defc titlebar < rum/reactive
  []
  [:h1 (:name (daistate/fetch-current-view (rum/react daistate/current-view) (rum/react daistate/app-state)))])

(rum/mount (titlebar)
           (. js/document (getElementById "titlebar")))

(rum/mount (menu)
           (. js/document (getElementById "menubar")))

(rum/mount (content-area-reactive)
           (. js/document (getElementById "app")))




(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
