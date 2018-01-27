(ns daiklave.core
  (:require [rum.core :as rum]
            [daiklave.state :as daistate]
            [daiklave.fragment :as daifrag]
            [clojure.string :as str]
            [com.rpl.specter :as sp]
            [daiklave.elem-defs]
            [daiklave.form-and-page :as form-and-page]))

(enable-console-print!)

(rum/mount (form-and-page/app-frame)
           (. js/document (getElementById "app")))

(defn on-js-reload [])
;; optionally touch your app-state to force rerendering depending on
;; your application
;; (swap! app-state update-in [:__figwheel_counter] inc)

