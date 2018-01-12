(ns daiklave.field-components
  (:require [rum.core :as rum]
            [daiklave.state :as lys :refer [change-element! get-change-value]]
            [com.rpl.specter :as sp :refer [transform setval]]
            [cljs.tools.reader.edn :as edn]
            [clojure.string :as str]
            [cljs.reader :as reader]))


(rum/defc dropdown < rum/static
          [fieldname fieldkey char-data-section options]
          [:div.field [:label fieldname]
           [:select.entry {:on-change (fn [e]
                                        (change-element! (:key char-data-section)
                                                         (fn [a] (assoc char-data-section fieldkey (reader/read-string (get-change-value e)))))
                                        (print (get-change-value e))
                                        )}
            (map (fn [[k v]] [:option {:value     (pr-str k),
                                       :key       (str fieldname "_" k)
                                       }

                              v])
                 options)]])

(rum/defc dotspinner < rum/static
  [fieldname value min max char-key change-fn]
  [:div.field.dotview
   [:label fieldname]
   [:div.entry
    [:select.spinner {:value (pr-str value)
                      :on-change (fn [e]
                                   (change-element! char-key (fn [a] (change-fn a (reader/read-string (get-change-value e)))))
                                   )}
     (map
       (fn [n] [:option {:value (pr-str n), :key (str fieldname "-dots-" n)} (str n)])
       (range min max))
     ]
    [:.dotpart {:class (str "dot-" value)}]]])

(rum/defc textfield < rum/static
          [fieldname fieldkey char-data-section & read-only]
          (let [valuekey (if (first read-only) :value :default-value)]
            [:div.field [:label fieldname]
             [:input.entry {:type          "text"
                            :default-value (str (get char-data-section fieldkey))
                            :on-change     (fn [e] (change-element!
                                                     (:key char-data-section)
                                                     (fn [a] (assoc char-data-section fieldkey (str (get-change-value e))))))
                            :read-only     (first read-only)}]])
          )

(rum/defc read-only-field < rum/static
          [fieldname fieldkey char-data-section]
          [:div.field [:label fieldname] [:span.entry (str/capitalize  (str (get char-data-section fieldkey)))]]
          )