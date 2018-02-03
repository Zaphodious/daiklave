(ns daiklave.form-and-page
  (:require [rum.core :as rum]
            [daiklave.state :as daistate]
            [cemerick.url :as url]
            [daiklave.seq :as daiseq]
            [clojure.browser.dom :as dom]
            [clojure.string :as str]))

(defn make-pretty [keyw]
  (cond
    (keyword? keyw) (str/capitalize (name keyw))
    :else (str keyw)))

(defmulti page-for-viewmap (fn [a] (-> a :view :category)))
(defmethod page-for-viewmap nil
  [{:keys [path view] :as viewmap}]
  (page-for-viewmap (assoc (daistate/fetch-view-for (vec (drop-last path))) :path path)))

(defmulti form-field-for :field-type)
; request map {:element n :fieldtype m :path p}
(defmethod form-field-for nil [_] nil)

(rum/defc page-from-path < rum/reactive
  [viewmap]

  [:h1 (str "Hello, this is the page for " viewmap)])

(defn scroll-app-frame-right-mixin []
  (let [cache-atom (atom [])]
    {:after-render (fn [s]
                     (when (not (= @cache-atom @daistate/current-view))
                       (reset! cache-atom @daistate/current-view)
                       (set! (.-scrollLeft (dom/get-element "app-frame")) 1000000))
                     s)}))
(defn vec-of-paths-for [path]
  (reduce (fn [c a]
            (conj c (conj (last c) a)))
          [[]]
          path))

(defn download-data-at [path]
  (let [pathos (vec-of-paths-for path)
        fetches (map daistate/fetch-view-for pathos)
        names (map
                (fn [{:keys [view path]}]
                  (if (:name view) (:name view) (make-pretty (last path))))
                fetches)
        elem-name (reduce #(str %1 "-" %2) names)
        fetch-elem (daistate/fetch-view-for path)]
       {:href (str "data:text/plain;charset=utf-8,"
                   (url/url-encode (prn-str fetch-elem)))
        :download (str elem-name ".edn")}))

(rum/defc app-frame < rum/reactive (scroll-app-frame-right-mixin)
  []
  (let [patho (rum/react daistate/current-view)
        app-data (rum/react daistate/app-state)
        viewmap (daistate/fetch-view-for patho app-data)
        screen-size (rum/react daistate/screen-size)
        vec-of-paths (vec-of-paths-for patho)]
    ;
    [:#app-frame
     (println "vec of paths " vec-of-paths)
     (if (> 600 (:width (daistate/get-screen-size)))
       (page-for-viewmap viewmap)
       [:table.page-list
        [:tbody
         [:tr
          (map (fn [a] [:td a]) (map page-for-viewmap (map daistate/fetch-view-for vec-of-paths)))]]])]))

(defn build-breadcrumb [page-path]
  (when page-path
    [:ul
     (map
       (fn [a]
         (let [view-name (:name (:view (daistate/fetch-view-for a)))
               display-name (if view-name view-name (make-pretty (last a)))]
           [:li [:a display-name]]))
       (vec-of-paths-for page-path))]))

(rum/defcs page-menu-assembly < rum/static (rum/local false :menu-showing) rum/reactive
  [local-state page-path minimized-atom]
  (let [show-menu-atom (:menu-showing local-state)]
    [:.menu-assembly
     [:button.menu-toggle
      {:on-click #(swap! show-menu-atom not)}
      (if @show-menu-atom "Hide Menu" "Show Menu")]
     [:.page-menu {:class (if @show-menu-atom "menu-showing" "menu-hidden")}
      (build-breadcrumb page-path)
      [:ul
       [:li [:a {:on-click #(swap! minimized-atom not)} (if (rum/react minimized-atom) "Show Irrelevant Fields" "Hide Irrelevant Fields")]]
       [:li [:a "Print"]]
       [:li [:a (download-data-at page-path) "Download"]]]]]))


(rum/defcs page-of < rum/static (rum/local true :minimized)
  [{:keys [minimized]} {:keys [title subtitle img class sections path]}]
  [:.page {:class (str (if @minimized "minimized" "maximized") " " class)}
   [:h1.page-title title]
   (page-menu-assembly path minimized)
   [:.page-content
    [:.page-section.page-header
     [:h2.page-subtitle subtitle]
     [:img.banner-image {:src img}]]
    sections]])

(rum/defc in-section-form-of < rum/static
  [form-name form-field-dec-vec]
  [:form
   (map-indexed (fn [n a]
                  [:p {:class (:class a)
                       :key (str (pr-str (:path a)) "-" n "- p")}
                   [:label {:for (pr-str (:path a))}
                    (:label a)]
                   (form-field-for a)])
                (daiseq/clear-nil form-field-dec-vec))])
(rum/defc form-of < rum/static
  [form-title form-name form-field-dec-vec]
  [:.page-section {:class form-name}
   [:h3 form-title]
   (in-section-form-of form-name form-field-dec-vec)])


(rum/defc soft-table-for < rum/static
  [form-title form-name path new-element sort-fn mini-forms]
  (let [neg-fn-make (fn [n] (fn [] (daistate/change-element! path #(daiseq/remove-nth % n))))
        add-fn (fn [] (daistate/change-element! path #(vec (conj % new-element))))
        sort-button-fn (fn [] (daistate/change-element! path #(vec (sort sort-fn %))))]
    [:.page-section {:class form-name}
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
  [{:keys [page-title page-subtitle page-img path elements new-element sort-fn form-fn selector-widget selector-title class]}]
  (let [now-state (vec elements)
        neg-fn-make (fn [n] (fn [] (daistate/change-element! path (daiklave.seq/remove-nth now-state n))))
        add-fn (fn [] (daistate/change-element! path #(conj % new-element)))
        sort-button-fn (fn [] (daistate/change-element! path #(into [] (sort sort-fn %))))]
    ;(println "now-state " (pr-str now-state))
    (page-of {:title page-title
              :subtitle page-subtitle
              :img page-img
              :class class
              :path path
                :sections [(when selector-widget
                            [:.page-section
                             [:h3 selector-title]
                             selector-widget])
                           [:.button-bar.page-section [:button {:on-click add-fn} "+"] [:button {:on-click sort-button-fn} "sort"]]
                           (map-indexed (fn [n a]
                                          (list
                                            [:.element-button-bar
                                             [:button.subtract-button
                                              {:on-click (neg-fn-make n)
                                               :key      (pr-str path)}
                                              "remove"]]
                                            (form-fn a (conj path n))))

                                        now-state)
                           [:.button-bar.page-section [:button {:on-click add-fn} "+"] [:button {:on-click sort-button-fn} "sort"]]]})))

(rum/defc mini-form-of < rum/static
  [form-name form-field-dec-vec]
  [:form.mini-form
   (map-indexed (fn [n a]
                  (list [:label.mini-label {:for (pr-str (:path a))}
                         (:label a)]
                        (form-field-for a)))

                (daiseq/clear-nil form-field-dec-vec))])



(rum/defc section-of < rum/static
  [section-title section-name section-comp]
  [:.page-section {:class section-name}
   [:h3 section-title]
   section-comp])