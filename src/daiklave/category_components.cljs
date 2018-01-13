(ns daiklave.category-components
  (:require [rum.core :as rum]
            [clojure.string :as str]
            [daiklave.url :as daifrag]))

(defn make-presentable [keywordi]
  (str/capitalize (name keywordi)))


(defn catview-helper [item-seq]
  (into [:div] (map
                (fn [a]
                  (println "doing a thing")
                  [:a {:href (daifrag/standard-link-fragment (:key a) "root")}
                   [:.pagesection
                    [:img.profile-image {:src (:img a) :alt (str "Character image for " (:name a))}]
                    [:h3 (:name a)]
                    [:h4 (str (make-presentable (:type a)) " - " (make-presentable (:subtype a)))]
                    [:p "By " (:player a)]]])


                item-seq)))
(rum/defc category-view [item-seq]
  (catview-helper item-seq))