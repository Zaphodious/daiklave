(ns daiklave.core
  (:require [rum.core :as rum]
            [daiklave.character-components :as daichar]
            [daiklave.category-components :as daicat]
            [daiklave.chron-components :as daichron]
            [daiklave.state :as daistate]
            [daiklave.url :as daifrag]
            [clojure.string :as str]
            [com.rpl.specter :as sp]
            [daiklave.page :as page]))

(enable-console-print!)









(rum/defc new-content-area < rum/static
  [{:keys [view path] :as viewmap}]
  (let [pagefn (page/page-fn-for viewmap)]
    [:#data-view-body
     (pagefn)]))


(rum/defc content-area-reactive < rum/reactive
  []
  (println "updating")
  (new-content-area (daistate/fetch-view-for (rum/react daistate/current-view) (rum/react daistate/app-state))))



(defn href [n] {:href (str "#" n)})
(rum/defc menu []
  [:ul
   [:li [:h3 [:a {:href (daifrag/path-frag :home)} "home"]]]
   ;[:li [:h3 [:a (href "characters") "Characters"]] [:ul [:li [:a "New"]]]]
   [:li [:h3 [:a {:href (daifrag/path-frag :chrons)}
              "Chronicles"]]]
    ;[:ul [:li [:a "New"]]]]
   [:li [:h3 [:a "Dice Roller"]]]
   [:li [:h3 [:a "Settings"]]]])


(defn get-page-title-for [entity]
  (cond (= (:category entity) :chron) (:name entity)
        (= (:category entity) :character) [:span [:a {:href (daifrag/dep-standard-link-fragment (:chron entity) "root")}
                                                  (str (:name (get @daistate/app-state (:chron entity))))]
                                                 (str " / " (:name entity))]))

(rum/defc titlebar < rum/reactive
  []
  [:h1 (:name (:view  (daistate/fetch-view-for (rum/react daistate/current-view) (rum/react daistate/app-state))))])

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

