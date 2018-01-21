(ns daiklave.page
  (:require [daiklave.fragment :as daifrag]
            [rum.core :as rum]
            [cemerick.url :as url]
            [clojure.string :as str]
            [daiklave.general-components :as daigen]
            [daiklave.state :as daistate]
            [daiklave.seq :as daiseq :refer [second-to-last]]
            [daiklave.character-components :as daichar]
            [daiklave.chron-components :as daichron]))

(rum/defc multi-page < rum/static
  [viewmap]
  (daigen/raw-element-div viewmap))

(rum/defc home-page < rum/reactive
  []
  [:div
   [:.pagesection [:p "Welcome to Anathema: Reincarnated"]]
   [:.pagesection [:a
                   {:href (str "data:text/plain;charset=utf-8,"
                               (url/url-encode (prn-str {:chrons (:chrons @daistate/app-state)}
                                                        :characters (:characters @daistate/app-state))))
                    :download "Anathema_Data.edn"}

                   "Download Chronicles and Characters"]]
   [:.pagesection [:h2 "Utilities"]]
   [:.pagesection [:a {:href (daifrag/link-fragment-for [:characters])}
                   [:h2 "Characters"]]]
   [:.pagesection [:a {:href (daifrag/link-fragment-for [:chrons])}
                   [:h2 "Chronicles"]]]
   #_(daigen/raw-element-div {:view (:chrons (rum/react daistate/app-state)), :path [:chrons]})])

(rum/defc chron-page < rum/static
  [{chron-data :view the-path :path}]
  [:div
   (daigen/banner (:name chron-data)
                  (:description chron-data)
                  (:img chron-data))
   [:.pagesection
    [:ul
     [:li (daigen/textfield "Name" (conj the-path :name))]]]
   (daigen/raw-element-div vals)
   (daigen/section-shortcut "Merits" (conj the-path :merits))
   (daigen/section-shortcut "Charms" (conj the-path :charms))
   (daigen/section-shortcut "Artifacts" (conj the-path :artifacts))]
  #_ (daigen/raw-element-div {:path (conj the-path :characters)
                              :view (:characters chron-data)}))

(rum/defc charsheet < rum/static
  [{char-data-section :view the-path :path :as total-view}]
  [:div
   (daigen/banner (:name char-data-section) (:description char-data-section) (:img char-data-section))
   (daichar/chardata total-view)
   (daigen/stat-section "Attributes"
                         (into (sorted-map-by daichar/sort-attributes-by)
                           (:attributes char-data-section))
                         (range 1 6)
                         (conj the-path :attributes))
   (daigen/stat-section "Abilities"
                        (daichar/inflate-ability-map (:abilities char-data-section))
                        (range 0 6)
                        (conj the-path :abilities))
   (daigen/fixed-set-view "Favored and Caste Abilities"
                          (conj the-path :favored-abilities)
                          (:favored-abilities char-data-section)
                          10
                          daichar/ability-all-keys
                          #(str/capitalize (name %)))
   (daigen/vec-view "Specialties"
                    "Specialty"
                    (conj the-path :specialties)
                    (:specialties char-data-section)
                    daichar/specialty-module
                    [:archery "Long-range shots"])
   (daigen/vec-view "Intimacies"
                    "Intimacy"
                    (conj the-path :intimacies)
                    (:intimacies char-data-section)
                    daichar/intimacy-module
                    [:minor "Disgust" "Injustice visited upon the meek."])])
;[section-name set-path the-set element-count options beauty-fn]



(defn page-fn-for
  [{:keys [view path] :as viewmap}]
  (println "changing to viewmap " viewmap)
  (if
    (and view (:category view))
    (case (:category view)
      :home #(home-page)
      :chron #(chron-page viewmap)
      :character #(charsheet viewmap)
      :charms #(daichron/charm-page viewmap))
    (cond
      (map? view) #(multi-page viewmap)
      (= :merits (last path)) #(daichron/merit-view viewmap)
      (= :charms (second-to-last path)) #(daichron/charm-view-by-ability viewmap))))


