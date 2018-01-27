(ns daiklave.elem-defs
  (:require [daiklave.form-and-page :as fp]
            [rum.core :as rum]
            [daiklave.fragment :as daifrag]
            [daiklave.state :as daistate]
            [daiklave.data-help :as daihelp]
            [clojure.string :as str]
            [clojure.tools.reader :as reader]
            [clojure.set :as set]))



(defn make-pretty [keyw]
  (cond
    (keyword? keyw) (str/capitalize (name keyw))
    :else (str keyw)))

(defn standard-on-change-for
  [path readonly?]
  (fn [a] (when (not readonly?)
            (daistate/change-element! path (daistate/get-change-value a)))))

(defn standard-read-on-change-for
  [path readonly?]
  (fn [a]
    (println "changing to " (daistate/get-change-value a) " and readonly is " readonly?)
    (when (not readonly?)
      (daistate/change-element! path (reader/read-string (daistate/get-change-value a))))))

(rum/defc dummy-of < rum/static
  [numbah]
  [:.field [:label (str "numbah " numbah)]
   [:input {:type "text" :value (str numbah) :disabled true}]])

(rum/defc text-field < rum/static
  [{:keys [path value options key name read-only class]}]
  [:input.field {:type      :text, :value value, :id key,
                 :key key,
                 :class     (str class " " (if read-only "read-only" ""))
                 :on-change (standard-on-change-for path read-only),
                 :readOnly  read-only}])

(rum/defc text-area < rum/static
  [{:keys [path value options key name read-only]}]
  [:textarea.field {:id key, :key key, :value value
                    :class     (if read-only "read-only" "")
                    :readOnly  read-only
                    :on-change (standard-on-change-for path read-only)}])

(rum/defc single-dropdown < rum/static
  [{:keys [path value options key name read-only] :as fieldmap}]
  [:select.field
   {:on-change #(standard-read-on-change-for path read-only)
    :id      key
    :class     (if read-only "read-only" "")
    :disabled  read-only
    :value     value}
   (map-indexed (fn [n a] [:option {:value (pr-str a), :key (str key "-select-" n)} (make-pretty a)])
                options)])

(rum/defc number-field < rum/static
  [{:keys [path value options key name read-only min max] :as fieldmap}]
  [:input.field
   {:type      :number
    :value     value :id key :key key
    :min       min :max max
    :on-change (standard-on-change-for path read-only)}])

(rum/defc dot-field < rum/static
  [{:keys [path value options key name read-only min max] :as fieldmap}]
  [:.field
   [:input.dot-entry
    {:type      :number
     :value     value :id key :key key
     :min       min :max max
     :on-change (standard-on-change-for path read-only)}]
   [:p.dot-bar
    (map (fn [a] (if (or (< (dec a) value) (= value 0))
                   [:span.active-dot {:key      (str "dot-active " a)
                                      :on-click #(daistate/change-element! path a)}]
                   [:span.inactive-dot {:key      (str "dot-inactive " a)
                                        :on-click #(daistate/change-element! path a)}]))
         (range 1 (inc max)))]])

(rum/defc merit-possible-ranks-field < rum/static
  [{:keys [path value] :as fieldmap}]
  [:.field
   (map-indexed (fn [n a]
                  [:span.rank-selection
                   [:label.rank-label {:for (pr-str (conj path n))
                                       :class (if (value a) "checked" "unchecked")}
                                      (inc n)]
                   [:input {:type :checkbox
                            :key (pr-str (conj path n))
                            :id (pr-str (conj path n))
                            :checked (value a)
                            :on-change (fn [e]
                                         (daistate/change-element! path (if (value a)
                                                                          (set (remove #{a} value))
                                                                          (conj value a))))}]])

             (range 1 6))])

(rum/defc checkbox-field
  [{:keys [path value] :as fieldmap}]
  [:input.field
   {:type :checkbox
    :id (pr-str path)
    :key (pr-str path)
    :checked value
    :on-change (fn [e]
                 (daistate/change-element! path (not value)))}])

(defmethod fp/form-field-for :dummy
  [{:keys [path value]}]
  (dummy-of value))
(defmethod fp/form-field-for :text
  [n] (text-field n))
(defmethod fp/form-field-for :big-text
  [n] (text-area n))
(defmethod fp/form-field-for :select-single
  [n] (single-dropdown n))
(defmethod fp/form-field-for :number
  [n] (number-field n))
(defmethod fp/form-field-for :dots
  [n] (dot-field n))
(defmethod fp/form-field-for :merit-possible-ranks
  [n] (merit-possible-ranks-field n))
(defmethod fp/form-field-for :boolean
  [n] (checkbox-field n))

(rum/defc content-link-section < rum/static
  [{:keys [view path] :as viewmap}]
  [:a {:href (daifrag/link-fragment-for path)}
   [:.profile-text
    [:p (:name view)]
    [:p (:description view)]
    [:p (str (make-pretty (:type view))
             " - "
             (make-pretty (:subtype view)))]]
   [:img.profile-image {:src (:img view)}]])

#_(rum/defc fixed-set-selectors < rum/static
    [{:keys [path value options key name read-only] :as fieldmap}]
    (let [sorted-value (into [] (sort value))
          value-set (set options)
          replace-fn-for (fn [i]
                           (fn [k] (daistate/change-element! path (set (assoc sorted-value i k)))))]
      [:.set-selectors
       (map-indexed (fn [n a]
                      (println "element " a " of " sorted-value)
                      [:select
                       {:on-change (replace-fn-for n)
                        :name      name
                        :key       (str key "-selector-" a)
                        :value     a}
                       (map-indexed (fn [n a] [:option {:value (pr-str a), :key (str key "-select-" n)} (make-pretty a)])
                                    options)])
                    ;(remove (set (remove #(= a %) sorted-value)) options))])
                    sorted-value)]))

(rum/defc fixed-seq-selectors < rum/static
  [set-path the-seq options]
  (let [options-set (set options)
        seq-vec (into [] the-seq)
        the-sorted-vec (into []
                             (sort the-seq))]
    [:.set-selectors
     [:.button-bar [:button {:on-click (fn []
                                         (println "changing " seq-vec " to " the-sorted-vec)
                                         (daistate/change-element! set-path the-sorted-vec))} "sort"]]
     (map-indexed (fn [i k]
                    (println "path for " k " is " (conj set-path i))
                    #_(fp/form-field-for
                        {:field-type :select-single, :name (str "set-select-" i)}
                        :value k, :key (str "castefield"), :path (conj set-path i)
                        :options options)
                    [:select.set-selector
                     {:value     (pr-str k)
                      :on-change (standard-read-on-change-for (conj set-path i) false)}
                     (map-indexed
                       (fn [n a] [:option {:value (pr-str a)} (make-pretty a)])
                       (remove
                         (set (remove #{k} the-seq))
                         options))])
                  seq-vec)]))

(rum/defc section-link-of < rum/build-defc
  [section-title section-name section-path extra-link-info]
  [:a {:href (daifrag/link-fragment-for section-path)}
   (fp/section-of section-title section-name extra-link-info)])

(defmethod fp/page-for-viewmap :home
  [{:keys [path view] :as viewmap}]
  (fp/page-of "Anathema Home" "Exalted 3rd Ed"
              (:img view)

              (into
                (->> [:characters]
                     (daistate/viewmaps-for-children)
                     (map content-link-section)
                     (map (fn [a] (fp/section-of
                                    "\uD83D\uDE42 Character"
                                    (str "character" (:name (:view a)))
                                    a)))
                     (into []))
                (->> [:chrons]
                     (daistate/viewmaps-for-children)
                     (map content-link-section)
                     (map (fn [a] (fp/section-of
                                    "\uD83D\uDDFA️Chronicle"
                                    (str "chron" (:name (:view a)))
                                    a)))
                     (into [])))))

(defmethod fp/page-for-viewmap :chron
  [{:keys [path view] :as viewmap}]
  (fp/page-of (:name view)
              (:description view)
              (:img view)
              (into
                [(fp/form-of
                   "Core Info"
                   "coreinfo"
                   [{:field-type :text, :name "charname", :label "Name", :value (:name view), :key (str "namefield"), :path (conj path :name)}
                    {:field-type :text, :name "chardesc", :label "Epithet", :value (:description view), :key (str "descfield"), :path (conj path :description)}
                    {:field-type :text, :name "charimg", :label "Image", :value (:img view), :key (str "imgfield") :path (conj path :img)}])
                 (section-link-of "Merits"
                                  "merit-section-link"
                                  (conj path :merits)
                                  [:p "meritas"])])))



(defmethod fp/page-for-viewmap :merits
  [{:keys [path view] :as viewmap}]
  ;[page-title page-subtitle page-img path elements new-element sort-fn form-fn]
  (fp/page-table-for
    (:name view)
    (:description view)
    (:img view)
    (conj path :merit-vec)
    (:merit-vec view)
    {:name          "Allies"
     :description   "Allies, yo!"
     :drawback      "Dey be people n shiii"
     :page          158
     :ranks         #{1 3 5}
     :repurchasable true
     :type          :story}
    (fn [a b] (compare (:name a) (:name b)))
    (fn [a p]
      (fp/form-of
        (:name a)
        (str (:name a) "-form")
        [{:field-type :text, :name "meritname", :label "Name", :value (:name a), :key (pr-str (conj p :name)) :path (conj p :name)},
         {:field-type :big-text, :name "meritdesc", :label "Description", :value (:description a), :key (pr-str (conj p :description)), :path (conj p :name)}
         {:field-type :number, :name "meritpage", :label "Page", :value (:page a), :key (pr-str (conj p :page)), :path (conj p :page)}
         {:field-type :select-single, :name "merittype", :label "Type", :value (:type a), :key (pr-str (conj p :type)), :path (conj p :type), :options [:story :purchased :innate]},
         {:field-type :merit-possible-ranks, :label "Ranks", :path (conj p :ranks), :value (:ranks a)}
         {:field-type :boolean, :label "Repurchasable", :path (conj p :repurchasable), :value (:repurchasable a)}]))))


(defmethod fp/page-for-viewmap :character
  [{:keys [path view] :as viewmap}]
  (fp/page-of (:name view)
              (:description view)
              (:img view)
              [(fp/form-of
                 "Core Info"
                 "coreinfo"
                 [{:field-type :text, :name "charname", :label "Name", :value (:name view), :key (str "namefield"), :path (conj path :name)}
                  {:field-type :text, :name "chardesc", :label "Epithet", :value (:description view), :key (str "descfield"), :path (conj path :description)}
                  {:field-type :text, :name "playername", :label "Player", :value (:player view), :key (str "playerfield"), :path (conj path :player)}
                  {:field-type :select-single, :name "chartype", :label "Type", :value (:type view), :key (str "typefield"), :path (conj path :type), :options [:solar, :mortal], :read-only (not (= :mortal (:type view)))}
                  {:field-type :text, :name "charimg", :label "Image", :value (:img view), :key (str "imgfield"), :path (conj path :img)}
                  {:field-type :big-text, :name "charanima", :label "Anima", :value (:anima view), :key (str "animafield"), :path (conj path :anima), :read-only (= :mortal (:type view))}
                  {:field-type :select-single, :name "charcaste", :label "Caste", :value (:subtype view), :key (str "castefield"), :path (conj path :subtype), :options [:dawn, :eclipse, :night, :twilight, :zenith]}
                  {:field-type :select-single :name "charsupernal" :label "Supernal" :read-only (= :mortal (:type view)) :path (conj path :supernal), :value (:supernal view), :options (into [] (set/intersection (set (:favored-abilities view)) (get daihelp/caste-abilities (:subtype view))))}])
               (fp/form-of "Attributes"
                           "attributeinfo"
                           (map (fn [[k v]]
                                  {:field-type :dots, :name (str k "-field")
                                   :label      (make-pretty k)
                                   :value      v, :key (str k "-field"), :path (into path [:attributes k])
                                   :min        1 :max 5})
                                (daihelp/sort-attribute-map (:attributes view))))
               (fp/form-of "Abilities"
                           "abilityinfo"
                           (map (fn [[k v]]
                                  {:field-type :dots, :name (str k "-field"),
                                   :label      (make-pretty k)
                                   :value      v, :key (str k "-field"), :path (into path [:abilities k])
                                   :min        0 :max 5})
                                (into (sorted-map)
                                      (:abilities view))))
               (fp/section-of "Favored Abilities"
                              "favoredabilities"
                              ;[set-path the-set element-count options beauty-fn]
                              (fixed-seq-selectors
                                (conj path :favored-abilities)
                                (:favored-abilities view)
                                daihelp/ability-all-keys))
               (fp/soft-table-for "Intimacies"
                                  "intimacyinfo"
                                  (conj path :intimacies)
                                  [:major "Disgust" "Dishonorable Combat"]
                                  compare
                                  (map-indexed (fn [n a]
                                                 (fp/mini-form-of
                                                   (last a)
                                                   [{:field-type :select-single,
                                                     :name       (str "intensity-field-" n)
                                                     :label      "Intensity"
                                                     :value      (first a)
                                                     :key        (str "intensity-" n)
                                                     :path       (into path [:intimacies 0])
                                                     :options    [:defining, :major, :minor]
                                                     :class      "first-of-three"}
                                                    {:field-type :text,
                                                     :name       (str "intimacy-type-field" n)
                                                     :value      (second a)
                                                     :label      "Type"
                                                     :key        (str "intimacy-type-" n)
                                                     :path       (into path [:intimacies 1])
                                                     :class      "second-of-three"}
                                                    {:field-type :text,
                                                     :name       (str "intimacy-desc-field" n)
                                                     :value      (last a)
                                                     :label      "Description"
                                                     :key        (str "intimacy-desc-" n)
                                                     :path       (into path [:intimacies 2])
                                                     :class      "third-of-three"}]))
                                               (:intimacies view)))]))

;[path value options key name]


