(ns daiklave.general-components
  (:require [rum.core :as rum]
            [daiklave.state :as lys :refer [change-element! get-change-value]]
            [com.rpl.specter :as sp :refer [transform setval]]
            [cljs.tools.reader.edn :as edn]
            [clojure.string :as str]
            [cljs.reader :as reader]
            [daiklave.url :as daifrag]))

(rum/defc banner < rum/static
  [charname charsubtitle charimg]
  [:.pagesection.banner
   [:img.banner-image {:src charimg}]
   [:h1.char-banner-title charname]
   [:h2.char-banner-subtitle charsubtitle]])

(defn read-change-event [e]
  (-> e
      get-change-value
      reader/read-string))
(defn wrap-on-change-fn [on-change-fn]
  (fn [e]
    (-> e
        (get-change-value)
        (reader/read-string)
        (on-change-fn))))

(rum/defc dropdown-general < rum/static
  [fieldvalue select-map fieldoptions prewrap-onchange-fn beauty-fn keyprefix]
  [:select
   (into select-map {:on-change (wrap-on-change-fn prewrap-onchange-fn)
                     :value     (pr-str fieldvalue)})
   (map (fn [a]
          [:option
           {:value (pr-str a)
            :key   (str keyprefix "-" a)}
           (beauty-fn a)])
        fieldoptions)])

(rum/defc dropdown-keyword < rum/static
  [fieldname fieldpath fieldvalue fieldoptions]
  [:.field [:label fieldname]
   (dropdown-general
     fieldvalue
     {:class "entry"}
     fieldoptions
     #(change-element! fieldpath %)
     #(str/capitalize (name %))
     fieldname)])

(defn make-dot-vec [min max value active-dot inactive-dot]
  (let [make-dot (fn [d n]
                   (if (= 0 n)
                     ""
                     d))
        make-vec (fn [the-dot this-range]
                   (into []
                         (map (partial make-dot the-dot)
                              this-range)))
        active-vec (make-vec active-dot (range min (inc value)))
        inactive-vec (make-vec inactive-dot (range
                                              (inc value)
                                              (inc max)))
        total-vec (into active-vec inactive-vec)]
    total-vec))

(defn make-dot-string [min max value active-dot inactive-dot]
  (reduce str (make-dot-vec min max value active-dot inactive-dot)))

(rum/defc dot-dropdown < rum/static
  [fieldname fieldpath range value]
  (let [min (first range)
        max (last range)]
    [:div.field.dotview
     [:label fieldname]
     [:div.entry
      [:select.dotspinner {:value     (pr-str value)
                           :on-change (fn [e]
                                        (change-element! fieldpath #(reader/read-string (get-change-value e))))}
       (map
         (fn [n] [:option
                  {:value (pr-str n), :key (str fieldpath " - " n)}
                  n])
         range)]
      [:.dotpart {:class (str "dot-" value)}
       (make-dot-vec min max value
                     [:span.active-dot " ⚫ "]
                     [:span.inactive-dot " ⚪ "])]]]))

(rum/defc textfield < rum/static
  [fieldname fieldpath]
  (let [fieldvalue (:view (daiklave.state/fetch-view-for fieldpath))]
    [:div.field [:label fieldname]
     [:input.entry {:type          "text"
                    :default-value fieldvalue
                    :on-change     (fn [e]
                                     (change-element! fieldpath
                                                      (fn [a] (get-change-value e))))}]]))

(rum/defc read-only-field < rum/static
  [fieldname fieldvalue]
  [:div.field [:label fieldname] [:span.entry (str/capitalize (str fieldvalue))]])

(rum/defc section-shortcut < rum/static
  [section-name section-key chron-data]
  [:div.pagesection
   [:a {:href (daifrag/dep-standard-link-fragment (:key chron-data) section-key)}
    [:h3 section-name]]])

(defn raw-element-div
  [{:keys [view path]}]
  (println "pathy is " path)
  (into [:div]
        (map
          (fn [[k v]]
            [:a {:href (daifrag/path-frag (conj path k))}
             [:.pagesection
              [:img.profile-image {:src (:img v)
                                   :alt (str "Character image for " (:name v))}]
              [:h3 (:name v)]
              [:h4 (str (str/capitalize (name (:type v)))
                        " - "
                        (str/capitalize (name (:subtype v))))]
              [:p "By " (:player v)]]])
          view)))

(rum/defc stat-section < rum/state
  [section-name stat-map the-range section-path]
  [:.pagesection
   [:h3 section-name]
   (map (fn [[k v :as a]]
          [:div {:key (pr-str section-path " value of " a)}
           (dot-dropdown
             (str/capitalize (name k))
             (conj section-path k)
             the-range
             v)])
        stat-map)])

(rum/defc fixed-set-view < rum/static
  [section-name set-path the-set element-count options beauty-fn]
  (let [options-set (set options)
        the-sorted-vec (into []
                             (sort the-set))
        replace-fn! (fn [[i k]]
                      (change-element! set-path (set (assoc the-sorted-vec i k))))
        wrapped-beauty-fn (fn [[i k]]
                            (beauty-fn k))]
    (println "sorted vec is " the-sorted-vec)
    [:.pagesection
     [:h3 section-name]
     (map-indexed (fn [i k]
                    (dropdown-general
                      [i k]
                      {:key (str section-name "-selector-" i)}
                      (into (sorted-set)
                            (map (fn [a] [i a])
                                 (conj (remove the-set options) k)))
                      replace-fn!
                      wrapped-beauty-fn
                      (str section-name "-subelement-" i "-" k "-")))
                  the-sorted-vec)]))

;[fieldvalue select-map fieldoptions prewrap-onchange-fn beauty-fn keyprefix]