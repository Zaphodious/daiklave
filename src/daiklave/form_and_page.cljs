(ns daiklave.form-and-page
  (:require [rum.core :as rum]
            [daiklave.state :as daistate]
            [cemerick.url :as url]
            [daiklave.seq :as daiseq]
            [daiklave.data-help :as daihelp]
            [daiklave.text-to-data :as daitext]
            [daiklave.fragment :as daifrag]
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

(defmulti modal-for :modal-showing)
(defmethod modal-for nil [_] nil)

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

(defn download-data-at [patho statemap]
  (let [pathos (vec-of-paths-for patho)
        fetches (map #(daistate/fetch-view-for % statemap) pathos)
        names (map
                (fn [{:keys [view path]}]
                  (if (:name view) (:name view) (make-pretty (last path))))
                fetches)
        elem-name (reduce #(str %1 "-" %2) names)
        fetch-elem (daistate/fetch-view-for patho)]
    {:href     (str "data:text/plain;charset=utf-8,"
                    (url/url-encode (prn-str fetch-elem)))
     :download (str elem-name ".edn")}))

(defn build-breadcrumb [page-path]
  (when page-path
    [:ul
     (map
       (fn [a]
         (let [view-name (:name (:view (daistate/fetch-view-for a)))
               display-name (if view-name view-name (make-pretty (last a)))]
           [:li [:a {:href (daifrag/link-fragment-for a)} display-name]]))
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
       ;[:li [:a {:on-click #(swap! minimized-atom not)} (if (rum/react minimized-atom) "Show Irrelevant Fields" "Hide Irrelevant Fields")]]
       [:li [:a {:href (daifrag/link-fragment-for [:settings])} "Settings"]]
       [:li [:a "Print"]]
       [:li [:a (download-data-at page-path (rum/react daistate/app-state))
             "Download"]]]]]))

(rum/defcs app-frame < rum/reactive (scroll-app-frame-right-mixin) (rum/local true :minimized)
  [{:keys [minimized]}]
  (let [patho (rum/react daistate/current-view)
        app-data (rum/react daistate/app-state)
        viewmap (daistate/fetch-view-for patho app-data)
        viewmap-one-up (daistate/fetch-view-for (drop-last patho) app-data)
        screen-size (rum/react daistate/screen-size)
        vec-of-paths (vec-of-paths-for patho)
        {:keys [modal-showing modal-arguments modal-title] :as modal-map} (:view (daistate/fetch-view-for [:modal]))
        showing-a-modal? (not (= :none modal-showing))]
    [:#app-frame
     {:class (str
               (if (< 700 (:width (daistate/get-screen-size))) "desktop" "mobile")
               " "
               (when showing-a-modal? "modal-having")
               " "
               (if @minimized "minimized" "maximized"))}
     (println "vec of paths " vec-of-paths)
     (when showing-a-modal? [:.modal-blur])
     (when showing-a-modal?
       [:.modal-window [:h3.modal-title modal-title]
        [:.interior
         (modal-for modal-map)]])
     (page-menu-assembly patho minimized)
     (if (< 700 (:width (daistate/get-screen-size)))
       [:.pages
        (page-for-viewmap viewmap-one-up)
        (page-for-viewmap viewmap)]
       (page-for-viewmap viewmap))]))

(rum/defc modal-interior-for < rum/static
  [{:keys [modal-component buttons] :as modal-args}]
  [:span
   modal-component
   [:.button-bar buttons [:button
                          {:on-click #(daistate/change-element! [:modal :modal-showing] :none)}
                          "Cancel"]]])





(rum/defc page-of < rum/static
  [{:keys [title subtitle header-content img class sections path]}]
  [:.page {:class (str " " class)}
   [:h1.page-title title]

   [:.page-content
    [:.page-section.page-header
     {:style {:background-image (str "url("  (daihelp/thumbnail-for img) ")")}}
     (when (not (str/blank? subtitle)) [:h3.page-subtitle subtitle])
     header-content]
    sections]])

(rum/defc in-section-form-of < rum/static
  [form-name form-field-dec-vec]
  [:form
   (map-indexed (fn [n a]
                  [:p {:class (:class a)
                       :key   (str (pr-str (:path a)) "-" n "- p")}
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
  [{:keys [form-title form-name path new-element sort-fn mini-forms table-row-data on-add-fn] :as table-map}]
  (let [neg-fn-make (fn [n] (fn [] (daistate/change-element! path #(daiseq/remove-nth % n))))
        add-fn (fn []
                 (let [elem (if on-add-fn (on-add-fn) new-element)]
                   (when elem
                     (daistate/change-element! path #(vec (conj % new-element))))))
        sort-button-fn (fn [] (daistate/change-element! path #(vec (sort sort-fn %))))]
    [:.page-section {:class (str form-name " soft-table")}
     [:h3 form-title]
     [:.button-bar [:button {:on-click add-fn} "+"] [:button {:on-click sort-button-fn} "sort"]]
     (when mini-forms
       [:span.row-container
        (map-indexed (fn [n a]
                       [:.soft-table-row
                        [:button.subtract-button
                         {:on-click (neg-fn-make n)}
                         "-"]
                        a])
                     mini-forms)])
     (when table-row-data
       [:.table-container
        [:table
         [:tbody
          [:tr [:th {:class "button"} "Remove"]
           (map (fn [a] [:th
                         {:class (:cell-type a)}
                         (:label a)])
                (first table-row-data))]
          (map-indexed (fn [n a]
                         [:tr
                          [:td [:button.subtract-button
                                 {:on-click (neg-fn-make n)}
                                 "-"]]
                          (map (fn [b]
                                 [:td (form-field-for b)])
                               a)])
               table-row-data)]]])

     [:.button-bar [:button {:on-click add-fn} "+"] [:button {:on-click sort-button-fn} "sort"]]]))
;[page-title page-subtitle page-img page-section-seq]

(rum/defcs page-table-for < rum/static (rum/local "" :paste-entry)
  [{:keys [paste-entry]} {:keys [page-title page-subtitle page-img path elements new-element sort-fn form-fn selector-widget selector-title class text-to-element-fn]}]
  (let [now-state (vec elements)
        neg-fn-make (fn [n] (fn [] (daistate/change-element! path (daiklave.seq/remove-nth now-state n))))
        add-fn (fn [] (daistate/change-element!
                        path
                        #(conj % (if (= "" @paste-entry)
                                   new-element
                                   (do
                                     (let [stringer @paste-entry
                                           elem (text-to-element-fn stringer)]
                                       (println "to-add-is " stringer)
                                       (println "charmer is " elem)
                                       (reset! paste-entry "")
                                       elem))))))

        sort-button-fn (fn [] (daistate/change-element! path #(into [] (sort sort-fn %))))]
    ;(println "now-state " (pr-str now-state))
    (page-of {:title    page-title
              :subtitle page-subtitle
              :img      page-img
              :class    class
              :path     path
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
                         [:.button-bar.page-section [:button {:on-click add-fn} "+"] [:button {:on-click sort-button-fn} "sort"]]
                         (when text-to-element-fn
                           [:.page-section
                            [:h3 "New By Paste"]
                            [:p "If a properly formatted element is in the text field below, it will be used as the new element when the '+' button is clicked."]
                            [:textarea.paste-entry-field
                             {:on-change (fn [e]
                                           (println paste-entry)
                                           (reset! paste-entry (daistate/get-change-value e)))}]])]})))

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

(rum/defc modal-search-component
  [{:keys [change-path query on-query-change selected element-data sort-fn aux-component] :as modal-map}]
  [:.element-search
   aux-component
   [:input {:type      :text
            :value     query
            :on-change on-query-change}]
   [:ul
    (when (not (= "" query))
      (map (fn [{:keys [title img key element-full disallowed lines]}]
             [:li {:style    {:background-image (str "url(" (daihelp/thumbnail-for img) ")")}
                   :class    (str
                               (when disallowed " disallowed ")
                               (when (and selected key (= selected key)) " selected "))
                   :on-click (fn []
                               (daistate/change-element! [:modal :selected-full] element-full)
                               (daistate/change-element! [:modal :selected] key))}
              [:.select-title title]
              (when lines (map (fn [a] [:.line a]) lines))])
           (if sort-fn
             (sort sort-fn element-data)
             element-data)))]])
