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


(rum/defc dropdown < rum/static
  [fieldname fieldkey char-data-section options]
  [:div.field [:label fieldname]
   [:select.entry {:on-change (fn [e]
                                (change-element! (:key char-data-section)
                                                 (fn [a] (assoc char-data-section fieldkey (reader/read-string (get-change-value e)))))
                                (print (get-change-value e)))}

    (map (fn [[k v]] [:option {:value (pr-str k),
                               :key   (str fieldname "_" k)}


                      v])
         options)]])



(rum/defc dep-dotspinner < rum/static
  [fieldname value min max char-key change-fn]
  [:div.field.dotview
   [:label fieldname]
   [:div.entry
    [:select.spinner {:value     (pr-str value)
                      :on-change (fn [e]
                                   (change-element! char-key (fn [a] (change-fn a (reader/read-string (get-change-value e))))))}

     (map
       (fn [n] [:option {:value (pr-str n), :key (str fieldname "-dots-" n)} (str n)])
       (range min max))]

    [:.dotpart {:class (str "dot-" value)}]]])

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

(rum/defc dotspinner < rum/static
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



(rum/defc dotview < rum/static
  ([fieldname fieldpath range] (dotview fieldname fieldpath range (daiklave.state/fetch-view-for fieldpath)))
  ([fieldname fieldpath range value]
   (let [make-radio-change-fn (fn [a]
                                (fn [] (change-element! fieldpath #(int a))))]
     [:div.field.dotview
      [:label fieldname]
      [:span (str "value is " value)]
      [:div.entry
       (map (fn [e]
              [:input {:type      :radio
                       :checked   (= e value)
                       :name      (pr-str fieldpath)
                       :on-change (make-radio-change-fn e)
                       :key       (pr-str fieldpath " value of " e)}])
            range)]])))

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