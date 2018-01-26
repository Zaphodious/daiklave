(ns daiklave.core
  (:require [rum.core :as rum]
            [daiklave.character-components :as daichar]
            [daiklave.chron-components :as daichron]
            [daiklave.state :as daistate]
            [daiklave.fragment :as daifrag]
            [clojure.string :as str]
            [com.rpl.specter :as sp]
            [daiklave.page :as page]
            [daiklave.elem-defs]
            [daiklave.form-and-page :as form-and-page]))

(enable-console-print!)

(rum/defc new-content-area < rum/static
  [{:keys [view path] :as viewmap}]
  (let [pagefn (page/page-fn-for viewmap)]
    [:#data-view-body
     (pagefn)]))

(rum/defc content-area-reactive < rum/reactive
  []
  (new-content-area (daistate/fetch-view-for (rum/react daistate/current-view) (rum/react daistate/app-state))))

(rum/defc menu []
  [:ul
   [:li [:h3 [:a {:href (daifrag/path-frag :home)} "home"]]]
   ;[:li [:h3 [:a (href "characters") "Characters"]] [:ul [:li [:a "New"]]]]
   [:li [:h3 [:a {:href (daifrag/path-frag :chrons)}
              "Chronicles"]]]
   [:li [:h3 [:a {:href (daifrag/path-frag :characters)}
              "Characters"]]]
   ;[:ul [:li [:a "New"]]]]
   [:li [:h3 [:a "Dice Roller"]]]
   [:li [:h3 [:a "Settings"]]]])


(rum/defc titlebar < rum/reactive
  []
  (let [current (rum/react daistate/current-view)
        appy (rum/react daistate/app-state)
        the-view (:view (daistate/fetch-view-for current appy))]
    [:h1 (if (:category the-view)
           (:name the-view)
           (str/capitalize (name (last current))))]))

(rum/mount (form-and-page/app-frame)
           (. js/document (getElementById "app")))

(defn on-js-reload [])
;; optionally touch your app-state to force rerendering depending on
;; your application
;; (swap! app-state update-in [:__figwheel_counter] inc)

