(ns daiklave.form-and-page
  (:require [rum.core :as rum]
            [daiklave.state :as daistate]
            [cemerick.url :as url]))


(defmulti page-for-viewmap (fn [a] (-> a :view :category)))
(defmethod page-for-viewmap nil
  [{:keys [path view] :as viewmap}]
  (page-for-viewmap (assoc (daistate/fetch-view-for (vec (drop-last path))) :path path)))

(defmulti form-field-for :field-type)
(defmethod page-for-viewmap nil [_] nil)
; request map {:element n :fieldtype m :path p}

(rum/defc page-from-path < rum/reactive
  [viewmap]

  [:h1 (str "Hello, this is the page for " viewmap)])

(rum/defc app-frame < rum/reactive
  []
  (let [patho (rum/react daistate/current-view)
        app-data (rum/react daistate/app-state)
        viewmap (daistate/fetch-view-for patho app-data)
        screen-size (rum/react daistate/screen-size)
        vec-of-paths (reduce (fn [c a]
                               (conj c (conj (last c) a)))
                             [[]]
                             patho)]
    [:#app-frame
     [:a.helper-dl-link {:href (str "data:text/plain;charset=utf-8,"
                                    (url/url-encode (prn-str (-> @daistate/app-state :chrons (get "0")))))
                         :download "Anathema_Data.edn"}
      "Download Exalted Core for Alex"]
     (page-for-viewmap viewmap)]))



(rum/defc page-of < rum/static
  [page-title page-subtitle page-img page-section-seq]
  [:.page
   [:h1.page-title page-title]
   [:.page-content
    [:.page-section
     [:h2.page-subtitle page-subtitle]
     [:img.banner-image {:src page-img}]]
    page-section-seq]])

(rum/defc form-of < rum/static
  [form-title form-name form-field-dec-vec]
  [:.page-section
   [:h3 form-title]
   [:form
    (map-indexed (fn [n a]
                   [:p {:key (str  (pr-str (:path a)) "-" n "- p")}
                    [:label {:for (pr-str (:path a))}
                     (:label a)]
                    (form-field-for a)])
                 form-field-dec-vec)]])

(rum/defc soft-table-for < rum/static
  [form-title form-name path new-element sort-fn mini-forms]
  (let [neg-fn-make (fn [n] (fn [] (daistate/change-element! path #(daiklave.seq/remove-nth % n))))
        add-fn (fn [] (daistate/change-element! path #(vec (conj % new-element))))
        sort-button-fn (fn [] (daistate/change-element! path #(vec (sort sort-fn %))))]
    [:.page-section
     [:h3 form-title]
     [:.button-bar [:button {:on-click add-fn} "+"] [:button {:on-click sort-button-fn} "sort"]]
     (map-indexed (fn [n a]
                    [:.soft-table-row
                     [:button.subtract-button
                      {:on-click (neg-fn-make n)}
                      "-"]
                     a])
                  mini-forms)
     [:.button-bar [:button {:on-click add-fn} "+"] [:button {:on-click sort-button-fn} "sort"]]]))
;[page-title page-subtitle page-img page-section-seq]
(rum/defc page-table-for < rum/static
  [{:keys [page-title page-subtitle page-img path elements new-element sort-fn form-fn selector-widget selector-title]}]
  (let [now-state (vec elements)
        neg-fn-make (fn [n] (fn [] (daistate/change-element! path (daiklave.seq/remove-nth now-state n))))
        add-fn (fn [] (daistate/change-element! path #(conj % new-element)))
        sort-button-fn (fn [] (daistate/change-element! path #(into [] (sort sort-fn %))))]
    ;(println "now-state " (pr-str now-state))
    [:.page
     [:h1.page-title page-title]
     [:.page-content
      [:.page-section
       [:h2.page-subtitle page-subtitle]
       [:img.banner-image {:src page-img}]]
      (when selector-widget
        [:.page-section
         [:h3 selector-title]
         selector-widget])
      [:.button-bar.page-section [:button {:on-click add-fn} "+"] [:button {:on-click sort-button-fn} "sort"]]
      (map-indexed (fn [n a]
                     (list
                       [:.element-button-bar
                        [:button.subtract-button
                         {:on-click (neg-fn-make n)
                          :key (pr-str path)}
                         "remove"]]
                       (form-fn a (conj path n))))

                   now-state)
      [:.button-bar.page-section [:button {:on-click add-fn} "+"] [:button {:on-click sort-button-fn} "sort"]]]]))

(rum/defc mini-form-of < rum/static
  [form-name form-field-dec-vec]
  [:form.mini-form
   (map-indexed (fn [n a]
                  (list [:label.mini-label {:for (pr-str (:path a))}
                         (:label a)]
                        (form-field-for a)))

                form-field-dec-vec)])

(rum/defc section-of < rum/static
  [section-title section-name section-comp]
  [:.page-section
   [:h3 section-title]
   section-comp])