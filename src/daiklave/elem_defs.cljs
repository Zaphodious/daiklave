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
  [:input.field {:type      :text, :value value, :name name, :key key,
                 :class     (str class " " (if read-only "read-only" ""))
                 :on-change (standard-on-change-for path read-only),
                 :readOnly  read-only}])

(rum/defc text-area < rum/static
  [{:keys [path value options key name read-only]}]
  [:textarea.field {:name      name, :key key, :value value
                    :class (if read-only "read-only" "")
                    :readOnly read-only
                    :on-change (standard-on-change-for path read-only)}])

(rum/defc single-dropdown < rum/static
  [{:keys [path value options key name read-only] :as fieldmap}]
  [:select.field
   {:on-change #(standard-read-on-change-for path read-only)
    :name name
    :class (if read-only "read-only" "")
    :disabled read-only
    :value value}
   (map-indexed (fn [n a] [:option {:value (pr-str a), :key (str key "-select-" n)} (make-pretty a)])
                options)])

#_(rum/defc fixed-set-view < rum/static
    [section-name set-path the-set element-count options beauty-fn]
    (let [options-set (set options)
          the-sorted-vec (into []
                               (sort the-set))
          replace-fn! (fn [[i k]]
                        (change-element! set-path (set (assoc the-sorted-vec i k))))
          wrapped-beauty-fn (fn [[i k]]
                              (beauty-fn k))])
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
                  the-sorted-vec)])




(rum/defc number-field < rum/static
  [{:keys [path value options key name read-only min max] :as fieldmap}]
  [:input.field
   {:type :number
    :value value :name name :key key
    :min min :max max
    :on-change (standard-on-change-for path read-only)}])

(rum/defc dot-field < rum/static
  [{:keys [path value options key name read-only min max] :as fieldmap}]
  [:.field
   [:input.dot-entry
    {:type :number
     :value value :name name :key key
     :min min :max max
     :on-change (standard-on-change-for path read-only)}]
   [:p.dot-bar
    (map (fn [a] (if (or (< (dec a) value) (= value 0))
                    [:span.active-dot {:key      (str "dot-active " a)
                                       :on-click #(daistate/change-element! path a)}]
                    [:span.inactive-dot {:key      (str "dot-inactive " a)
                                         :on-click #(daistate/change-element! path a)}]))
        (range 1 (inc max)))]])

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
                        :name name
                        :key (str key "-selector-" a)
                        :value a}
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
                       {:value (pr-str k)
                        :on-change (standard-read-on-change-for (conj set-path i) false)}
                       (map-indexed
                         (fn [n a] [:option {:value (pr-str a)} (make-pretty a)])
                         (remove
                           (set (remove #{k} the-seq))
                           options))])
                  seq-vec)]))

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
                                   :label (make-pretty k)
                                   :value v, :key (str k "-field"), :path (into path [:attributes k])
                                   :min 1 :max 5})
                                (daihelp/sort-attribute-map (:attributes view))))
               (fp/form-of "Abilities"
                           "abilityinfo"
                           (map (fn [[k v]]
                                  {:field-type :dots, :name (str k "-field"),
                                   :label (make-pretty k)
                                   :value v, :key (str k "-field"), :path (into path [:abilities k])
                                   :min 0 :max 5})
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
                                                     :name (str "intensity-field-" n)
                                                     :label "Intensity"
                                                     :value (first a)
                                                     :key (str "intensity-" n)
                                                     :path (into path [:intimacies 0])
                                                     :options [:defining,:major,:minor]
                                                     :class "first-of-three"}
                                                    {:field-type :text,
                                                     :name (str "intimacy-type-field" n)
                                                     :value (second a)
                                                     :label "Type"
                                                     :key (str "intimacy-type-" n)
                                                     :path (into path [:intimacies 1])
                                                     :class "second-of-three"}
                                                    {:field-type :text,
                                                     :name (str "intimacy-desc-field" n)
                                                     :value (last a)
                                                     :label "Description"
                                                     :key (str "intimacy-desc-" n)
                                                     :path (into path [:intimacies 2])
                                                     :class "third-of-three"}]))
                                               (:intimacies view)))]))

;[path value options key name]


