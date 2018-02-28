(ns daiklave.elem-defs
  (:require [daiklave.form-and-page :as fp :refer [make-pretty]]
            [rum.core :as rum]
            [daiklave.fragment :as daifrag]
            [daiklave.state :as daistate]
            [daiklave.data-help :as daihelp]
            [clojure.string :as str]
            [clojure.tools.reader :as reader]
            [clojure.set :as set]
            [daiklave.text-to-data :as ttd]))





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
  [{:keys [path value options read-only class special-change-fn]}]
  (if read-only
    [:span.input-readonly {:class class} value]
    [:input.field {:type      :text, :value value, :id (pr-str path)
                   :key       (pr-str path)
                   :class     (str class " " (if read-only "read-only" ""))
                   :on-change (if special-change-fn special-change-fn
                                                    (standard-on-change-for path read-only))
                   :readOnly  read-only}]))

(rum/defc text-area < rum/static
  [{:keys [path value options read-only]}]
  (if read-only
    [:span.input-readonly value]
    [:textarea.field {:id        (pr-str path)
                      :key       (pr-str path)
                      :value     value
                      :class     (if read-only "read-only" "")
                      :readOnly  read-only
                      :on-change (standard-on-change-for path read-only)}]))

(rum/defc single-dropdown < rum/static
  [{:keys [path value options read-only special-change-fn display-fn] :as fieldmap}]
  (if read-only
    [:span.input-readonly
     (if display-fn
       (display-fn value)
       (make-pretty value))]
    [:select.field
     {:on-change (if special-change-fn
                   special-change-fn
                   (standard-read-on-change-for path read-only))
      :id        (pr-str path)
      :class     (if read-only "read-only" "")
      :disabled  read-only
      :value     value}
     (map-indexed (fn [n a] [:option {:value (pr-str a), :key (str (pr-str path) "-select-" n)}
                             (if display-fn
                               (display-fn a)
                               (make-pretty a))])
                  options)]))

(rum/defc number-field < rum/static
  [{:keys [path value options read-only min max fraction? class] :as fieldmap}]
  (if read-only
    [:span.input-readonly
     {:class class}
     (str value)]
    (let [button-fn (partial daistate/change-element! path)]
      [:.field.number-field
       [:button {:type :button, :on-click #(button-fn dec)} "-"]
       [:input
        {:type      :number
         :class (when fraction? "fraction-top")
         :value     value :id (pr-str path) :key (pr-str path)
         :min       min :max max
         :readOnly  read-only
         :on-change (standard-on-change-for path read-only)}]
       (when fraction? [:span.fraction-bottom max])
       [:button {:type :button, :on-click #(button-fn inc)} "+"]])))

(rum/defc balanced-number-field < rum/static
  [{:keys [path-a path-b value-a value-b read-only min-a min-b max-a max-b label-a label-b] :as fieldmap}]
  (let [button-fn-for (fn [up?]
                        (fn [] (daistate/change-element! path-a (if up? inc dec))
                          (daistate/change-element! path-b (if up? dec inc))))]
    [:.field.balanced-number-field
     [:label.text-a {:for (pr-str path-a)} label-a]
     [:input.side-a {:value     value-a :id (pr-str path-a) :key (pr-str path-a) :min min-a :max max-a
                     :on-change (fn [a]) :readOnly true}]
     [:button {:on-click (button-fn-for true)} "⇐"]
     "+"
     [:button {:on-click (button-fn-for false)} "⇒"]
     [:input.side-b {:value     value-b :id (pr-str path-b) :key (pr-str path-b) :min min-b :max max-b
                     :on-change (standard-on-change-for path-b read-only) :readOnly read-only}]
     [:label.text-b {:for (pr-str path-b)} label-b]]))


(rum/defc dot-field < rum/static
  [{:keys [path value options read-only min max aux-widget on-change class] :as fieldmap}]
  [:.field
   (when (daistate/get-setting-for-key :show-accessibility)
     [:input.dot-entry
      {:type      :number
       :value     value :id (pr-str path) :key (pr-str path)
       :min       min :max max
       :on-change (if on-change on-change
                    (standard-on-change-for path read-only))}])
   (when aux-widget aux-widget)
   [:p.dot-bar
    {:class class}
    [:button.minus
     {:on-click (fn [] (if (>= (dec value) min) (daistate/change-element! path (dec value))))}
     "-"]
    (map (fn [a] (if (and (< (dec a) value) (not (= a 0)))
                   [:span.active-dot {:key      (str "dot-active " a)
                                      :on-click #(if on-change
                                                   (on-change a)
                                                   (daistate/change-element! path a))}]
                   [:span.inactive-dot {:key      (str "dot-inactive " a)
                                        :class (when (= a 0) "zero-dot")
                                        :on-click #(if on-change
                                                     (on-change a)
                                                     (daistate/change-element! path a))}]))
         (range
           (if (> min 0) min 1)
           (inc max)))
    [:button.plus
     {:on-click (fn [] (if (>= max (inc value)) (daistate/change-element! path (inc value))))}
     "+"]]])

(defn- make-merit-ranks-field-data
  [{:keys [path value readonly] :as fieldmap}]
  [:.field
   (map
     (fn [n]
       [:span.rank-selection
        [:label.rank-label {:for   (pr-str (conj path n))
                            :class (if (value n) "checked" "unchecked")}
         n]
        [:input {:type      :checkbox
                 :key       (pr-str (conj path n))
                 :id        (pr-str (conj path n))
                 :checked   (value n)
                 :on-change (fn [e]
                              (daistate/change-element! path (if (value n)
                                                               (set (remove #{n} value))
                                                               (conj value n))))}]
        [:span.select-helper]])

     (range 0 6))])
(rum/defc merit-possible-ranks-field < rum/static
  [{:keys [path value readonly] :as fieldmap}]
  (make-merit-ranks-field-data fieldmap))

(rum/defc checkbox-field
  [{:keys [path value] :as fieldmap}]
  [:input.field
   {:type      :checkbox
    :id        (pr-str path)
    :key       (pr-str path)
    :checked   value
    :on-change (fn [e]
                 (daistate/change-element! path not))}])

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
(defmethod fp/form-field-for :balanced-number
  [n] (balanced-number-field n))
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
   [:img.profile-image {:src (daihelp/thumbnail-for (:img view))}]])

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

(rum/defcs navlist < (rum/local false ::key) rum/static
  [{local-atom ::key} {:keys [path options] :as fieldmap}]
  (println "navilistor for " path " contains " options)
  (let [should-drop @local-atom
        path-points-to-option? ((set options) (last path))
        current-element (if path-points-to-option? (last path) (first options))
        base-path (vec (if path-points-to-option? (drop-last path) path))
        toggle-button [:button
                       {:on-click (fn [a] (swap! local-atom not))}
                       (if should-drop "Hide Options" "Show Options")]]
    (println "should drop is " should-drop)
    [:.navlist-container
     [:p.navlist-selected
      {:focusable true}
      (make-pretty current-element)]
     toggle-button
     [:ul.field.navlist
      {:class (if should-drop
                "shown"
                "hidden")}
      (map (fn [a] [:li [:a {:href  (daifrag/link-fragment-for (conj base-path a))
                             :class (if (= a current-element) "selected" "")}
                         (make-pretty a)]])
           options)]
     (when should-drop toggle-button)]))

(rum/defc charm-selector < rum/static
  [path]
  (navlist {:path path, :options daihelp/ability-all-keys}))

(rum/defc section-link-of < rum/build-defc
  [section-title section-name section-path extra-link-info]
  [:a {:href (daifrag/link-fragment-for section-path)}
   (fp/section-of section-title section-name extra-link-info)])

(rum/defcs health-track < rum/static (rum/local {:level-add :add, :damage-add :add} :field-states)
  [{:keys [field-states] :as this-state} {:keys [path view] :as viewmap}]
  (println "this atom is " field-states)
  (let [add-remove-fn (fn [{:keys [modifier path]}]
                        (case (modifier @field-states)
                          :add (fn [] (daistate/change-element! path inc))
                          :remove (fn [] (daistate/change-element! path dec))))
        change-field-states (fn [k v] (swap! field-states #(assoc % k (reader/read-string (daistate/get-change-value v)))))
        add-remove-drop (fn [k]
                          (fp/form-field-for {:field-type        :select-single,
                                              :value             (k @field-states)
                                              :path              (conj path k),
                                              :options           [:add :remove],
                                              :special-change-fn (fn [a]
                                                                   (change-field-states k a)
                                                                   (println "level add is " (daistate/get-change-value a))
                                                                   (println "level-add-atom is " field-states))}))]
    [:.health-module
     [:.button-bar (add-remove-drop :damage-add)
      [:button.bashing {:on-click (add-remove-fn {:modifier :damage-add, :path (conj path :bashing)})} "Bashing"]
      [:button.lethal {:on-click (add-remove-fn {:modifier :damage-add, :path (conj path :lethal),})} "Lethal"]
      [:button.aggravated {:on-click (add-remove-fn {:modifier :damage-add, :path (conj path :aggravated),})} "Aggravated"]]
     [:.button-bar (add-remove-drop :level-add)
      [:button {:on-click (add-remove-fn {:modifier :level-add, :path (into path [:levels 0])})} "0"]
      [:button {:on-click (add-remove-fn {:modifier :level-add, :path (into path [:levels 1])})} "-1"]
      [:button {:on-click (add-remove-fn {:modifier :level-add, :path (into path [:levels 2])})} "-2"]
      [:button {:on-click (add-remove-fn {:modifier :level-add, :path (into path [:levels 3])})} "-4"]]
     [:ul.health-track
      (map (fn [[level damage]]
             [:li.health-box {:class (str level "-level" " " (name damage) "-damage")} (str level)])
           (daihelp/flatten-health-module view))]]))

(rum/defc solar-essence-section < rum/static
  [{character-view :view patho :path}]
  [:.essence-module
   [:.button-bar "Essence Level" [:button "Inc"] [:button "Dec"]]])


(defmethod fp/page-for-viewmap :home
  [{:keys [path view] :as viewmap}]
  (fp/page-of {:title    "Anathema Home"
               :subtitle "Exalted 3rd Ed"
               :img      (:img view)
               :class    "home-page"
               :path     path
               :sections [(section-link-of
                            "Characters"
                            "character-link"
                            [:characters]
                            {})
                          (section-link-of
                            "Rulebooks"
                            "rulebook-link"
                            [:rulebooks]
                            {})
                          (section-link-of
                            "Settings"
                            "settings-link"
                            [:settings]
                            {})]}))

(defmethod fp/page-for-viewmap :settings
  [{:keys [path view] :as viewmap}]
  (println "------ settings page " viewmap)
  (fp/page-of {:title    (:name view)
               :subtitle (:description view)
               :img      (:img view)
               :class    "settings-page"
               :path     path
               :sections [(fp/form-of "Show/Hide Elements"
                                      "showhide"
                                      [{:field-type :boolean, :label "Show Accessability Elements",
                                        :path       (conj path :show-accessibility), :value (:show-accessibility view)}
                                       {:field-type :boolean, :label "Show Unused Elements",
                                        :path       (conj path :show-unused), :value (:show-unused view)}])]}))


(defn print-pass [n] (println n) n)
(defmethod fp/page-for-viewmap :characters
  [{:keys [path view] :as viewmap}]
  (fp/page-of {:title    (:name view)
               :subtitle (:description view)
               :img      (:img view)
               :path     path
               :class    "character-select-page"
               :sections (->> [:characters]
                              (daistate/viewmaps-for-children)
                              (filter (fn [a] (map? (:view a))))
                              (map content-link-section)
                              (map (fn [a] (fp/section-of
                                             "Character"
                                             (str "character" (:name (:view a)))
                                             a)))
                              (into []))}))
;[title subtitle img class sections]
(defmethod fp/page-for-viewmap :rulebooks
  [{:keys [path view] :as viewmap}]
  (fp/page-of {:title    (:name view)
               :subtitle (:description view)
               :img      (:img view)
               :class    "rulebook-select-page"
               :path     path
               :sections (->> [:rulebooks]
                              (daistate/viewmaps-for-children)
                              (filter (fn [a] (map? (:view a))))
                              (map content-link-section)
                              (map (fn [a] (fp/section-of
                                             "Rulebooks"
                                             (str "rulebook" (:name (:view a)))
                                             a)))
                              (into []))}))



(defmethod fp/page-for-viewmap :rulebook
  [{:keys [path view] :as viewmap}]
  (fp/page-of {:title    (:name view)
               :subtitle (:description view)
               :img      (:img view)
               :class    "rulebook-page"
               :path     path
               :sections (into
                           [(fp/form-of
                              "Core Info"
                              "coreinfo"
                              [{:field-type :text, :label "Name", :value (:name view), :path (conj path :name)}
                               {:field-type :text, :label "Epithet", :value (:description view), :path (conj path :description)}
                               {:field-type :text, :label "Image", :value (:img view), :path (conj path :img)}])
                            (section-link-of
                              "Merits"
                              "merit-section-link"
                              (conj path :merits)
                              [:p "meritas"])
                            (section-link-of
                              "Charms"
                              "charm-section-link"
                              (conj path :charms)
                              [:p "charmers"])
                            (section-link-of
                              "Mundane Weapons"
                              "mundane-weapons-section-link"
                              (conj path :mundane-weapons)
                              [:p "Weapons what normies use"])
                            (section-link-of
                              "Mundane Armor"
                              "mundane-armor-section-link"
                              (conj path :mundane-armor)
                              [:p "Shirts what stop da k-nif-ies."])
                            (section-link-of
                              "Martial Arts"
                              "martial-arts-section-link"
                              (conj path :martial-arts-styles)
                              [:p "Super powerful special stuff"])])}))

(defmethod fp/page-for-viewmap :merits
  [{:keys [path view] :as viewmap}]
  ;[page-title page-subtitle page-img path elements new-element sort-fn form-fn]
  (fp/page-table-for
    {:page-title    (:name view)
     :page-subtitle (:description view)
     :page-img      (:img view)
     :path          (conj path :merit-vec)
     :class         "merits-page"
     :elements      (:merit-vec view)
     :new-element   {:name           "Allies"
                     :description    "Allies, yo!"
                     :drawback       "Dey be people n shiii"
                     :page           158
                     :ranks          #{1 3 5}
                     :repurchasable  true
                     :upgrading      false
                     :type           :story
                     :character-tags [[] [] [] [] []]
                     :confers-merits "Each Merit On\nA New\nLine"}
     :sort-fn       (daihelp/map-compare-fn-for {:name 3})
     :form-fn       (fn [a p]
                      (fp/form-of
                        (:name a)
                        (str (:name a) "-form")
                        (into
                          [{:field-type :text, :label "Name", :value (:name a), :path (conj p :name)},
                           {:field-type :big-text, :label "Description", :value (:description a), :path (conj p :description)}
                           {:field-type :text, :label "Page", :value (str (:page a)), :path (conj p :page)}
                           {:field-type :select-single, :label "Type", :value (:type a), :path (conj p :type), :options [:story :purchased :innate :flaw] :special-change-fn false},
                           {:field-type :merit-possible-ranks, :label "Ranks", :path (conj p :ranks), :value (:ranks a)}
                           {:field-type :boolean, :label "Repurchasable", :path (conj p :repurchasable), :value (:repurchasable a)}
                           (if (:repurchasable a)
                             {:field-type :boolean, :label "Upgrade On Repurchase", :path (conj p :upgrading), :value (:upgrading a)})
                           {:field-type :big-text, :label "Merits Conferred", :value (:confers-merits a), :path (conj p :confers-merits)}]
                          #_(map (fn [z]
                                   {:field-type        :text,
                                    :label             (str "Player-Tags at " z),
                                    :value             (->> (dec z) (nth (:character-tags a)) daihelp/keyword-vec-to-string)
                                    :path              (into p [:character-tags (dec z)]),
                                    :special-change-fn (fn [e]
                                                         (daistate/change-element!
                                                           (into p [:character-tags (dec z)])
                                                           (daihelp/string-to-keyword-vec (daistate/get-change-value e))))}
                                   (into (sorted-set) (:ranks a)))))))}))

(defmethod fp/page-for-viewmap :mundane-weapons
  [{:keys [path view] :as viewmap}]
  (fp/page-table-for
    {:page-title    (:name view)
     :page-subtitle (:description view)
     :page-img      (:img view)
     :path          (conj path :weapons-vec)
     :class         "mundane-weapons-page"
     :elements      (:weapons-vec view)
     :new-element   {:name        "Wind and Fire Wheel"
                     :description "An elegant weapon, from a more... civilized age."
                     :tags        "Lethal, Martial Arts, Disarming"
                     :type    :light
                     :cost        2}
     :sort-fn       (daihelp/map-compare-fn-for {:category 5 :name 2})
     :form-fn       (fn [a p]
                      (fp/form-of
                        (:name a)
                        (str (:name a) "-form")
                        [{:field-type :text, :label "Name", :value (:name a), :path (conj p :name)},
                         {:field-type :big-text, :label "Description", :value (:description a), :path (conj p :description)}
                         {:field-type :select-single, :label "Type", :value (:type a), :path (conj p :type), :options [:light :medium :heavy]}
                         {:field-type :text, :label "Tags", :value (:tags a), :path (conj p :tags)},
                         {:field-type :dots, :label "Cost", :value (:cost a), :path (conj p :cost), :min 0, :max 5,}]))}))

(defmethod fp/page-for-viewmap :mundane-armor
  [{:keys [path view] :as viewmap}]
  (fp/page-table-for
    {:page-title    (:name view)
     :page-subtitle (:description view)
     :page-img      (:img view)
     :path          (conj path :armor-vec)
     :class         "mundane-weapons-page"
     :elements      (:armor-vec view)
     :new-element   {:name        "Lamellar"
                     :description "Lamellar armor is a breastplate composed of a series of interlocking horizontal bands."
                     :tags        "None"
                     :type    :medium
                     :cost        3}
     :sort-fn       (daihelp/map-compare-fn-for {:category 10 :name 1})
     :form-fn       (fn [a p]
                      (fp/form-of
                        (:name a)
                        (str (:name a) "-form")
                        [{:field-type :text, :label "Name", :value (:name a), :path (conj p :name)},
                         {:field-type :big-text, :label "Description", :value (:description a), :path (conj p :description)}
                         {:field-type :select-single, :label "Type", :value (:type a), :path (conj p :type), :options [:light :medium :heavy]}
                         {:field-type :text, :label "Tags", :value (:tags a), :path (conj p :tags)},
                         {:field-type :dots, :label "Cost", :value (:cost a), :path (conj p :cost), :min 0, :max 5,}]))}))

(rum/defc charms-for-ability-page < rum/static
  [{:keys [path view] :as viewmap}]
  (let [charm-focus (:type view)]
    (fp/page-table-for
      {:page-title         (:name view)
       :page-subtitle      (get daihelp/ability-descriptions (:type view))
       :page-img           (:img view)
       :path               (conj path :charms)
       :class              "charms-list-page"
       :elements           (:charms view)
       ; :selector-title  "Which Ability"
       ;:selector-widget (charm-selector path)
       :text-to-element-fn ttd/charm-to-data
       :new-element        {:name          "Wise Arrow"
                            :cost          "1m"
                            :min-essence   1
                            :min-ability   2
                            :category      :charm
                            :ability       charm-focus
                            :page          "255"
                            :keywords      ""
                            :type          :supplemental
                            :duration      "Instant"
                            :prereq-charms "None"
                            :description   "Fire an arrow which knows which way is up"}
       :sort-fn            (daihelp/map-compare-fn-for {:page 5, :min-essence 3, :min-ability 1})
       :form-fn            (fn [a p]
                             (fp/form-of
                               (:name a)
                               (str (:name a) "-form")
                               [{:field-type :text, :label "Name", :value (:name a), :path (conj p :name)},
                                {:field-type :text, :label "Cost", :value (:cost a), :path (conj p :cost)},
                                {:field-type :select-single, :label "Ability", :value (:ability a), :path (conj p :ability), :options daihelp/ability-all-keys, :read-only true},
                                {:field-type :dots, :label "Min Essence", :value (:min-essence a), :path (conj p :min-essence), :min 1, :max 5,}
                                {:field-type :dots, :label (str "Min " (make-pretty (:ability a))), :value (:min-ability a), :path (conj p :min-ability), :min 1, :max 5,}
                                {:field-type :big-text, :label "Description", :value (:description a), :path (conj p :description), :read-only false},
                                {:field-type :text, :label "Page", :value (:page a), :path (conj p :page)}
                                {:field-type :select-single, :label "Type", :value (:type a), :path (conj p :type), :options [:simple :supplemental :reflexive :permanent]}
                                {:field-type :text, :label "Keywords", :value (:keywords a), :path (conj p :keywords)}
                                {:field-type :text, :label "Duration", :value (:duration a), :path (conj p :duration)}
                                {:field-type :text, :label "Prereq Charms", :value (:prereq-charms a), :path (conj p :prereq-charms)}
                                {:field-type :text, :label "Character-Tags Added", :value (daihelp/keyword-vec-to-string (:character-tags a)), :path (conj p :character-tags), :special-change-fn (fn [x] (daistate/change-element! (conj p :character-tags) (daihelp/string-to-keyword-vec (daistate/get-change-value x))))}]))})))

(rum/defc charm-page < rum/static
  [{:keys [path view] :as viewmap}]
  (let [charm-focus (if ((set daihelp/ability-all-keys) (last path))
                      (last path)
                      (first daihelp/ability-all-keys))
        base-path (if (= (last path) :charms)
                    path
                    (drop-last path))
        base-view (:view (daistate/fetch-view-for base-path))]
    (println "base-patho is " base-path)
    (println "base-view is " base-view)
    (fp/page-of {:title    "Charms"
                 :subtitle "Dingys what shini bois do"
                 :img      (:img base-view)
                 :class    "charm-ability-page"
                 :path     path
                 :sections (map
                             (fn [k] (section-link-of (make-pretty k)
                                                      (str "section-link-for" k)
                                                      (conj (vec path) k)
                                                      {}))
                             (conj daihelp/ability-keys :craft))})))

(defmethod fp/page-for-viewmap :charms
  [viewmap]
  (charm-page viewmap))

(defmethod fp/page-for-viewmap :charms-for-ability
  [viewmap]
  (charms-for-ability-page viewmap))
(defmethod fp/page-for-viewmap :charm
  [viewmap]
  (charms-for-ability-page viewmap))


(defmethod fp/page-for-viewmap :martial-arts-styles
  [{:keys [path view] :as viewmap}]
  (fp/page-table-for
    {:page-title    (:name view)
     :page-subtitle (:description view)
     :page-img      (:img view)
     :path          (conj path :martial-arts-vec)
     :class         "martial-arts-styles-page"
     :elements      (:martial-arts-vec view)
     :new-element   [{:name        "Black Claw Style"
                      :category    :martial-arts-style
                      :description "Black Claw style is one of the few martial arts created by a demon, taught to the Exalted in the First Age by Mara, the Shadow-Lover."
                      :type        :martial-arts-style
                      :subtype     :martial-arts
                      :style-info  {:weapons   "This style uses exclusively unarmed attacks, emphasizing claw strikes and sudden, lunging kicks."
                                    :armor     "Black Claw style is incompatible with armor."
                                    :abilities "Black Claw stylists often feign the appearance of fighting on the defensive, using Dodge to both evade attacks and disengage from close combat. Presence is also useful to them, as many of their Charms allow them to sway the hearts and minds of enemies and bystanders alike in combat."}
                      :charms      [{:name          "Open Palm Caress"
                                     :cost          "4m"
                                     :min-essence   1
                                     :min-ability   2
                                     :type          :supplemental
                                     :keywords      "Mastery"
                                     :duration      "Instant"
                                     :prereq-charms "None"
                                     :description   "From the very beginning, things start to go wrong. Righteous heroes find themselves cast as vicious bullies when they fight a student of the Black Claw—even when she starts the fight. Open Palm Caress can be used whenever the martial artist rolls Join Battle. As long as at least one enemy received more successes on the roll than the martial artist did, he and his allies are seen as having initiated hostilities, regardless of how the fight actually began. This applies both to any bystanders to the fight and to the enemies themselves, who might suddenly find themselves confused as to why they are attacking the Black Claw stylist. Characters may see through this deception with a reflexive read intentions roll against the martial artist’s Guile. The Black Claw stylist gains a single point of Initiative for each opponent or bystander who was fooled by this ruse, up to a maximum of her Manipulation. \n\n Mastery: The martial artist’s performance is so convincing that if she uses her first turn to make a clinch or decisive attack against an enemy who beat her Join Battle and failed to overcome her Guile, she may pay a point of Willpower to treat that attack as an ambush."}]}]
     :sort-fn       (daihelp/map-compare-fn-for {:name 2})
     :form-fn       (fn [a p]
                      (fp/form-of
                        (:name a)
                        (str (:name a) "-form")
                        [{:field-type :text, :label "Name", :value (:name a), :path (conj p :name)},
                         {:field-type :big-text, :label "Description", :value (:description a), :path (conj p :name)}]))}))




(defmethod fp/page-for-viewmap :character
  [{:keys [path view] :as viewmap}]
  (fp/page-of {:title          (:name view)
               :subtitle       (:description view)
               :header-content [:.info-header
                                [:p (str (make-pretty (:type view)) " Exalted")]
                                [:p (str (make-pretty (:supernal view)) " Supernal " (make-pretty (:subtype view)))]
                                [:p (:long-description view)]]
               :img            (:img view)
               :class          (str "character-page " (name (:subtype view)))
               :path           path
               :sections
                               [(fp/form-of
                                  "Information"
                                  "coreinfo"
                                  [{:field-type :text, :label "Name", :value (:name view), :path (conj path :name)}
                                   {:field-type :text, :label "Concept", :value (:description view), :path (conj path :description)}
                                   {:field-type :big-text, :label "Description", :value (:long-description view), :path (conj path :long-description)}
                                   {:field-type :text, :label "Player", :value (:player view), :path (conj path :player)}
                                   {:field-type :select-single, :label "Type", :value (:type view), :path (conj path :type), :options [:solar, :mortal], :read-only (not (= :mortal (:type view)))}
                                   {:field-type :text, :label "Image", :value (:img view), :path (conj path :img)}
                                   {:field-type :big-text, :label "Anima", :value (:anima view), :path (conj path :anima), :read-only (= :mortal (:type view))}
                                   {:field-type :select-single, :label "Caste", :value (:subtype view), :path (conj path :subtype), :options [:dawn, :eclipse, :night, :twilight, :zenith]}
                                   {:field-type :select-single :label "Supernal" :read-only (= :mortal (:type view)) :path (conj path :supernal), :value (:supernal view), :options (into [] (set/intersection (set (:favored-abilities view)) (get daihelp/caste-abilities (:subtype view))))}])


                                ;[form-title form-name path new-element sort-fn mini-forms]
                                (fp/soft-table-for {:form-title  "Rulebooks Used"
                                                    :form-name   "rulebooks-used"
                                                    :path        (conj path :rulebooks)
                                                    :new-element "0"
                                                    :sort-fn     compare
                                                    :on-add-fn   #(daistate/show-modal :rulebook-change-sheet "Select a Rulebook" {:change-path (conj path :rulebooks)})
                                                    :table-row-data
                                                                 (map-indexed (fn [n a]
                                                                                [{:field-type :text
                                                                                  :read-only  true
                                                                                  :label      "Rulebook Used"
                                                                                  :value      (->> a (conj [:rulebooks]) daistate/fetch-view-for :view :name str)
                                                                                  :path       (conj path :rulebooks n)}])

                                                                              ;:display-fn (fn [a] (->> a (conj [:rulebooks]) daistate/fetch-view-for :view :name str))}]))
                                                                              ;:options    (->> [:rulebooks] daistate/fetch-view-for :view (filter #(-> % second :name)) (map first))}]))
                                                                              (:rulebooks view))})
                                (fp/form-of "Attributes"
                                            "attributeinfo"
                                            (map (fn [[k v]]
                                                   {:field-type :dots,
                                                    :label      (make-pretty k)
                                                    :value      v, :path (into path [:attributes k])
                                                    :min        1 :max 5})
                                                 (daihelp/sort-attribute-map (:attributes view))))
                                (fp/form-of "Abilities"
                                            "abilityinfo"
                                            (map (fn [k]
                                                   (let [-v (-> view :abilities k)
                                                         v (if -v -v 0)]
                                                     (when (or (daistate/get-setting-for-key :show-unused)
                                                               (not (= v 0)))
                                                       {:field-type :dots,
                                                        :label      (make-pretty k)
                                                        :class      "ability"
                                                        :value      v, :path (into path [:abilities k])
                                                        :min        0 :max 5
                                                        :aux-widget (if ((set (:favored-abilities view)) k)
                                                                      [:span.favored.selected
                                                                       {:class (:subtype view)}]
                                                                      [:span.favored.not-selected])})))

                                                 (sort daihelp/ability-keys)))
                                (fp/soft-table-for {:form-title  "Additional abilities"
                                                    :form-name   "additional-ability-info"
                                                    :path        (conj path :abilities-additional)
                                                    :new-element {:ability :craft, :rank 3, :description "Glass Blowing"}
                                                    :sort-fn     (daihelp/map-compare-fn-for {:ability 100 :rank 10 :description 1})
                                                    :table-row-data
                                                                 (map-indexed (fn [n a]
                                                                                [
                                                                                 {:field-type :select-single
                                                                                  :label      "Ability"
                                                                                  :value      (:ability a)
                                                                                  :path       (conj path :abilities-additional n :ability)
                                                                                  :options    daihelp/ability-all-keys
                                                                                  :cell-type  "word"}
                                                                                 {:field-type :dots
                                                                                  :label      "Rank"
                                                                                  :value      (:rank a)
                                                                                  :path       (conj path :abilities-additional n :rank)
                                                                                  :min        0
                                                                                  :max        5
                                                                                  :cell-type  "dots"}
                                                                                 {:field-type :text
                                                                                  :label      "Description"
                                                                                  :value      (:description a)
                                                                                  :path       (conj path :abilities-additional n :description)
                                                                                  :cell-type  "name"}])
                                                                              (:abilities-additional view))})
                                #_(fp/section-of "Favored Abilities"
                                                 "favoredabilities"
                                                 ;[set-path the-set element-count options beauty-fn]
                                                 (fixed-seq-selectors
                                                   (conj path :favored-abilities)
                                                   (:favored-abilities view)
                                                   daihelp/ability-all-keys))
                                (fp/soft-table-for {:form-title  "Specialties"
                                                    :form-name   "specialtyinfo"
                                                    :path        (conj path :specialties)
                                                    :new-element [(:supernal view) "Doing Awesome Things"]
                                                    :sort-fn     compare
                                                    :table-row-data
                                                                 (map-indexed (fn [n a]
                                                                                [{:field-type :select-single,
                                                                                  :label      "Ability"
                                                                                  :value      (first a)
                                                                                  :path       (into path [:specialties n 0])
                                                                                  :options    daihelp/ability-all-keys
                                                                                  :cell-type  "word"}

                                                                                 {:field-type :text,
                                                                                  :value      (second a)
                                                                                  :label      "Description"
                                                                                  :path       (into path [:specialties n 1])
                                                                                  :cell-type  "name"}])
                                                                              (:specialties view))})
                                (let [has-crafting? (->> view (:abilities-additional) (map :ability) (filter #(= % :craft)) (take 1) (empty?) (not))]
                                  (when has-crafting?
                                    [(fp/soft-table-for {:form-title     "Crafting Slots"
                                                         :form-name      "crafting-slot-info"
                                                         :path           (conj path :crafting-slots)
                                                         :new-element    {:type :major, :contains "Spider Bomb", :time-type :days, :time-required 10, :time-passed 3}
                                                         :sort-fn        (daihelp/map-compare-fn-for {:type 1000 :time-type 100 :time-required 10 :time-passed 1})
                                                         :table-row-data (map-indexed (fn [n {:keys [type contains time-type time-required time-passed] :as a}]
                                                                                        [{:field-type :select-single
                                                                                          :label      "Type"
                                                                                          :value      type
                                                                                          :path       (conj path :crafting-slots n :type)
                                                                                          :options    [:major :superior :legendary]
                                                                                          :cell-type  "word"}
                                                                                         {:field-type :text
                                                                                          :label      "Contains"
                                                                                          :value      contains
                                                                                          :path       (conj path :crafting-slots n :contains)
                                                                                          :cell-type  "name"}
                                                                                         {:field-type :select-single
                                                                                          :label      "Time"
                                                                                          :value      time-type
                                                                                          :path       (conj path :crafting-slots n :time-type)
                                                                                          :options    [:days :weeks :months :years]
                                                                                          :cell-type  "word"}
                                                                                         {:field-type :number
                                                                                          :label      "Required"
                                                                                          :value      time-required
                                                                                          :path       (conj path :crafting-slots n :time-required)
                                                                                          :cell-type  "word"}
                                                                                         {:field-type :number
                                                                                          :label      "Passed"
                                                                                          :value      time-passed
                                                                                          :path       (conj path :crafting-slots n :time-passed)
                                                                                          :cell-type  "word"}])
                                                                                      (:crafting-slots view))})
                                     (fp/form-of "Crafting Experience"
                                                 "crafting-experience-module"
                                                 [{:field-type :number, :label "Silver", :value (-> view :xp :silver), :min 0, :max 100000, :path (conj path :xp :silver)}
                                                  {:field-type :number, :label "Gold", :value (-> view :xp :gold), :min 0, :max 100000, :path (conj path :xp :gold)}
                                                  {:field-type :number, :label "White", :value (-> view :xp :white), :min 0, :max 100000, :path (conj path :xp :white)}])]))
                                (fp/soft-table-for {:form-title     "Weapons"
                                                    :form-name      "weapon-info"
                                                    :path           (conj path :weapon-inventory)
                                                    :new-element    {:name "Wind and Fire Wheel", :category :mundane-weapons}
                                                    :table-row-data (map-indexed (fn [n {:keys [name as category]}]
                                                                                   (println "Weapon Category is " category)
                                                                                   (let [[book-id {:keys [cost type tags description] :as weapon}]
                                                                                         (->> {:thing-name     name
                                                                                               :path-before-id [:rulebooks]
                                                                                               :id-vec         (:rulebooks view)
                                                                                               :path-after-id  [category :weapons-vec]
                                                                                               :exact-match?   true}
                                                                                              (daistate/get-named-elements)
                                                                                              first)
                                                                                         tags-have (fn [substr] (-> tags str/lower-case (str/includes? substr)))
                                                                                         thrown? (tags-have "thrown")
                                                                                         archery? (tags-have "archery")
                                                                                         {:keys [accuracy damage defense overwhelming]} (-> daihelp/weapon-values category type)]
                                                                                     [{:field-type :text
                                                                                       :read-only  true
                                                                                       :value      (if as as name)
                                                                                       :label      "Name"
                                                                                       :cell-type  "name"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Cost"
                                                                                       :value      cost
                                                                                       :cell-type  "number"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Type"
                                                                                       :value      (make-pretty type)
                                                                                       :cell-type  "word"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Tags"
                                                                                       :value      tags
                                                                                       :cell-type  "name"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Acc"
                                                                                       :value      (daihelp/plus-or-minus accuracy)
                                                                                       :cell-type  "number"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Dmg"
                                                                                       :value      (daihelp/plus-or-minus damage)
                                                                                       :cell-type  "number"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Def"
                                                                                       :value      (daihelp/plus-or-minus defense)
                                                                                       :cell-type  "number"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Description"
                                                                                       :cell-type  "description"
                                                                                       :value      (str
                                                                                                     (->> description
                                                                                                          (take 80)
                                                                                                          (reduce str))
                                                                                                     "...")}]))
                                                                                 (:weapon-inventory view))})
                                (fp/form-of "Limit"
                                            "limit-info"
                                            [{:field-type :text
                                              :label      "Trigger"
                                              :value      (-> view :limit :trigger)
                                              :path       (conj path :limit :trigger)}
                                             {:field-type :dots
                                              :label      "Accrued"
                                              :value      (-> view :limit :accrued)
                                              :path       (conj path :limit :accrued)
                                              :min        0 :max 10}])
                                (fp/soft-table-for {:form-title     "Intimacies"
                                                    :form-name      "intimacyinfo"
                                                    :path           (conj path :intimacies)
                                                    :on-add-fn      #(daistate/show-modal :intimacy-add "Add an Intimacy" {:change-path (conj path :intimacies)})
                                                    :new-element    {:type        :tie
                                                                     :severity    :major
                                                                     :feeling     "Disgust"
                                                                     :description "Fighting without honor"}
                                                    :sort-fn        (daiklave.data-help/map-compare-fn-for
                                                                      {:severity    100
                                                                       :description 10})
                                                    :table-row-data (map-indexed (fn [n a]
                                                                                   [{:field-type :select-single
                                                                                     :value      (:severity a)
                                                                                     :label      "Intensity"
                                                                                     :read-only  true
                                                                                     :cell-type  "word"
                                                                                     :options    daihelp/intimacy-intensities}
                                                                                    {:field-type :text
                                                                                     :read-only  true
                                                                                     :value      (if (= :tie (:type a))
                                                                                                   (:feeling a)
                                                                                                   "Principle")
                                                                                     :label      "Type"
                                                                                     :cell-type  "word"}
                                                                                    {:field-type :text
                                                                                     :read-only  true
                                                                                     :value      (:description a)
                                                                                     :label      "Description"
                                                                                     :cell-type  "description"}])
                                                                                 (:intimacies view))})
                                (fp/soft-table-for {:form-title     "Merits"
                                                    :form-name      "meritinfo"
                                                    :path           (conj path :merits)
                                                    :on-add-fn      #(daistate/show-modal
                                                                       :merit-add
                                                                       "Add a Merit"
                                                                       {:change-path  (conj path :merits)
                                                                        :rulebook-ids (:rulebooks view)
                                                                        :query        ""
                                                                        :dots         1
                                                                        :note         ""})
                                                    :new-element    {:name "Amputee"
                                                                     :rank 0
                                                                     :note "Head above the neck."}
                                                    :sort-fn        (daiklave.data-help/map-compare-fn-for
                                                                      {:name 100
                                                                       :rank 10
                                                                       :note 1})
                                                    :table-row-data (map-indexed (fn [n a]
                                                                                   [{:field-type :text
                                                                                     :read-only  true
                                                                                     :value      (:name a)
                                                                                     :label      "Name"
                                                                                     :class      "name"
                                                                                     :cell-type  "name"
                                                                                     :path       (conj path :merits n :name)}
                                                                                    {:field-type :number
                                                                                     :value      (:rank a)
                                                                                     :label      "Rank"
                                                                                     :class      "rank"
                                                                                     :min        0
                                                                                     :max        5
                                                                                     :read-only  true
                                                                                     :cell-type  "number"
                                                                                     :path       (conj path :merits n :rank)}
                                                                                    {:field-type :text
                                                                                     :value      (:note a)
                                                                                     :label      "Note"
                                                                                     :class      "note"
                                                                                     :path       (conj path :merits n :note)
                                                                                     :read-only  true
                                                                                     :cell-type  "name"}])
                                                                                 (:merits view))})

                                (fp/form-of "Experience"
                                            "experience-module"
                                            [{:field-type :number, :label "Earned", :value (-> view :xp :earned), :path (conj path :xp :earned)}
                                             {:field-type :number, :label "Spent", :value (-> view :xp :spent), :path (conj path :xp :spent), :min 0, :max (-> view :xp :earned), :fraction? true}
                                             {:field-type :number, :label "Solar", :value (-> view :xp :solar), :min 0, :max 100000, :path (conj path :xp :solar)}])
                                (let [{essence-view :view essence-path :path} (daistate/fetch-view-for (conj path :essence))
                                      essence-expanded (daihelp/inflate-essence-map essence-view)]
                                  (fp/form-of "Essence"
                                              "essence-module"
                                              [{:field-type :dots, :label "Rating", :min 1, :max 5, :value (:rating essence-expanded), :path (conj essence-path :rating)}
                                               #_{:field-type :balanced-number, :label "XP",
                                                  :value-a    (:xp-spent essence-expanded), :path-a (conj essence-path :xp-spent), :min-a 1, :max-a 1000000, :label-a "Spent"
                                                  :value-b    (:xp-wallet essence-expanded), :path-b (conj essence-path :xp-wallet), :min-b 0, :max-b 1000000, :label-b "Unspent"}

                                               {:field-type :balanced-number, :label "Personal",
                                                :value-a    (:essence-personal-remaining essence-expanded), :path-a nil, :min-a 1, :max-a 1000000, :label-a "Remaining"
                                                :value-b    (:motes-spent-personal essence-expanded), :path-b (conj essence-path :motes-spent-personal), :min-b 0, :max-b 1000000, :label-b "Spent"}
                                               #_{:field-type :balanced-number, :label "Pool",
                                                  :value-a    (:essence-max-personal essence-expanded), :path-a nil, :min-a 1, :max-a 1000000, :label-a "Max"
                                                  :value-b    (:motes-committed-personal essence-expanded), :path-b (conj essence-path :motes-committed-personal), :min-b 0, :max-b 1000000, :label-b "Committed"}

                                               {:field-type :balanced-number, :label "Peripheral",
                                                :value-a    (:essence-peripheral-remaining essence-expanded), :path-a nil, :min-a 1, :max-a 1000000, :label-a "Remaining"
                                                :value-b    (:motes-spent-peripheral essence-expanded), :path-b (conj essence-path :motes-spent-peripheral), :min-b 0, :max-b 1000000, :label-b "Spent"}
                                               #_{:field-type :balanced-number, :label "Pool",
                                                  :value-a    (:essence-max-peripheral essence-expanded), :path-a nil, :min-a 1, :max-a 1000000, :label-a "Max"
                                                  :value-b    (:motes-committed-peripheral essence-expanded), :path-b (conj essence-path :motes-committed-peripheral), :min-b 0, :max-b 1000000, :label-b "Committed"}
                                               {:field-type :number, :label "Committed", :min 0, :max (:essence-peripheral-remaining essence-expanded), :value (:motes-committed-peripheral essence-expanded), :path (conj essence-path :motes-committed-peripheral)}]))

                                (fp/form-of "Willpower"
                                            "willpower-module"
                                            [{:field-type :dots, :label "Max", :class "max-willpower" :value (-> view :willpower :max), :path (conj path :willpower :max) :min 1, :max 10},
                                             {:field-type :dots, :label "Temporary", :value (-> view :willpower :temporary), :path (conj path :willpower :temporary), :min 0, :max (-> view :willpower :max)}])
                                (fp/soft-table-for {:form-title     "Charms"
                                                    :form-name      "charminfo"
                                                    :path           (conj path :charms)
                                                    :new-element    "Wise Arrow"
                                                    :sort-fn        compare
                                                    :on-add-fn      #(daistate/show-modal
                                                                       :charm-add
                                                                       "Add a Charm"
                                                                       {:change-path         (conj path :charms)
                                                                        :rulebook-ids        (:rulebooks view)
                                                                        :ability             :archery
                                                                        :query               ""
                                                                        :character-essence   (:rating (:essence view))
                                                                        :character-abilities (:abilities view)
                                                                        :character-supernal  (:supernal view)
                                                                        :character-charms    (:charms view)})
                                                    :table-row-data (map-indexed (fn [n a]
                                                                                   (let [[book-id charm]
                                                                                         (->> {:thing-name     a
                                                                                               :path-before-id [:rulebooks]
                                                                                               :id-vec         (:rulebooks view)
                                                                                               :path-after-id  [:charms]
                                                                                               :exact-match?   true}
                                                                                              (daistate/get-named-elements)
                                                                                              first)]
                                                                                     (println "Charm is " charm)
                                                                                     [{:field-type :text
                                                                                       :read-only  true
                                                                                       :value      a
                                                                                       :label      "Charm Name"
                                                                                       :path       (conj path :charms n)
                                                                                       :cell-type  "name"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Cost"
                                                                                       :value      (:cost charm)
                                                                                       :cell-type  "word"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Type"
                                                                                       :value      (make-pretty (:type charm))
                                                                                       :cell-type  "word"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Duration"
                                                                                       :value      (:duration charm)
                                                                                       :cell-type  "word"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Ability"
                                                                                       :value      (make-pretty (:ability charm))
                                                                                       :cell-type  "word"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Book"
                                                                                       :value      (:view (daistate/fetch-view-for [:rulebooks book-id :name]))
                                                                                       :cell-type  "name"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Page"
                                                                                       :value      (:page charm)
                                                                                       :cell-type  "number"}
                                                                                      {:field-type :text
                                                                                       :read-only  true
                                                                                       :label      "Description"
                                                                                       :cell-type  "description"
                                                                                       :value      (->> charm
                                                                                                        (:description)
                                                                                                        (take 100)
                                                                                                        (reduce str))}]))



                                                                                 (:charms view))})

                                (fp/section-of "Health Track"
                                               "health-track-module"
                                               (health-track {:path (conj path :health-module) :view (:health-module view)}))]}))

;[path value options key name]

(defn get-rulebook-contains [rulebook]
  (map #(str % " ")
       (filter #(not (nil? %))
               (map
                 (fn [k] (when (k rulebook) (str/capitalize (name k))))
                 [:charms :merits :evocations :spells :martial-arts-styles :mundane-weapons]))))

(defmethod fp/modal-for :intimacy-add
  [{:keys [change-path intensity feeling description]}]
  (let []
    (when (nil? intensity)
      (daistate/change-element! [:modal :intensity] :defining))
    (when (nil? feeling)
      (daistate/change-element! [:modal :feeling] ""))
    (when (nil? description)
      (daistate/change-element! [:modal :description] ""))
    (fp/modal-interior-for
      {:modal-component
       [:.intimacy-add [:p "For principles, please leave 'Feeling' text box blank."
                        (fp/in-section-form-of
                          "Add Intimacy"
                          [{:field-type :select-single, :label "Intensity"
                            :options    daihelp/intimacy-intensities, :value intensity,
                            :path       [:modal :intensity]}
                           {:field-type :text, :label "Feeling"
                            :value      feeling, :path [:modal :feeling]}
                           {:field-type :big-text, :label "Description",
                            :value      description, :path [:modal :description]}])]]
       :buttons [[:button
                  {:on-click
                   #(daistate/apply-modal-and-hide change-path
                                                   (fn [a] (conj a {:severity    intensity
                                                                    :type        (case feeling
                                                                                   "" :principle
                                                                                   "Principle" :principle
                                                                                   "principle" :principle
                                                                                   :tie)
                                                                    :feeling     feeling
                                                                    :description description})))}
                  "Add Intimacy"]]})))

(defmethod fp/modal-for :rulebook-change-sheet
  [{:keys [change-path query selected] :as modal-map}]
  (let [existant-rulebooks (daistate/fetch-view-for change-path)]
    (fp/modal-interior-for
      {:modal-component (fp/modal-search-component
                          (into modal-map
                                {:on-query-change (standard-on-change-for [:modal :query] false)
                                 :element-data
                                 (map (fn [a]
                                        {:title (:name a)
                                         :img (:img a)
                                         :lines [(str "By: " (:storyteller a))
                                                 (str "Contains: "(get-rulebook-contains a))]
                                         :key (:key a)})
                                   (:view (daistate/search-in [:rulebooks] query [:name :storyteller])))}))
       :buttons [[:button
                   {:on-click
                    #(daistate/apply-modal-and-hide change-path
                                                    (fn [a] (conj a (:view (daistate/fetch-view-for [:modal :selected])))))}
                   "Add Selected Rulebook"]]})))

;[thing-name path-before-id id-vec path-after-id exact-match?]
(defmethod fp/modal-for :merit-add
  [{:keys [change-path query selected rulebook-ids dots note selected-full] :as modal-map}]
  (let [dots-change-fn (fn [selecto ranko]
                         (if selecto
                           (daistate/change-element! [:modal :dots]
                                                   (if ((:ranks selecto) ranko)
                                                     ranko
                                                     (first (sort (vec (:ranks selecto))))))))]
    (dots-change-fn selected-full dots)
    (fp/modal-interior-for
      {:modal-component
       [:.merit-modal-interior
        (fp/modal-search-component
                          (into modal-map
                                {:on-query-change (standard-on-change-for [:modal :query] false)
                                 :element-data
                                 (when (not (= "" query))
                                  (map (fn [{:keys [name parent-id description ranks] :as a}]
                                         (let [{:keys [img storyteller] :as rulebook rulebook-name :name}
                                               (:view (daistate/fetch-view-for [:rulebooks parent-id]))]
                                           {:title name
                                            :img img
                                            :lines [(str "Possible Ranks: " ranks)
                                                    (str "Description: "(reduce #(str %1 " " %2) (take 9 (str/split description #" "))))]
                                            :key name
                                            :element-full a}))
                                       (->>  {:thing-name (str/lower-case query)
                                              :path-before-id [:rulebooks]
                                              :path-after-id [:merits :merit-vec]
                                              :id-vec rulebook-ids
                                              :exact-match? false}
                                         (daistate/get-named-elements)
                                         (map daistate/put-id-into-named-elements)
                                         (reduce conj))))}))
        (fp/in-section-form-of "Merit Details To Add"
                               [{:field-type :dots
                                 :label "Rank"
                                 :value dots
                                 :min 0
                                 :max 5
                                 :path [:modal :dots]
                                 :on-change (fn [a]
                                              (println "\uD83C\uDF88\n\n\n dots are now going to be...")
                                              (dots-change-fn selected-full a))}
                                {:field-type :big-text
                                 :value (str note)
                                 :label "Note"
                                 :path [:modal :note]
                                 :on-change (standard-on-change-for [:modal :note] false)}])]
       :buttons [[:button
                  {:on-click
                   #(daistate/apply-modal-and-hide change-path
                                                   (fn [a]
                                                     (conj a
                                                           (let [{:keys [selected dots note]} (:view (daistate/fetch-view-for [:modal]))]
                                                                {:name selected
                                                                 :rank dots
                                                                 :note note}))))}
                  "Add This Merit"]]})))

(defmethod fp/modal-for :charm-add
  [{:keys [change-path query selected ability rulebook-ids character-essence character-abilities character-supernal character-charms] :as modal-map}]
  (let [existant-rulebooks (daistate/fetch-view-for change-path)]
    (fp/modal-interior-for
      {:modal-component [:.charm-modal-interior
                         (fp/modal-search-component
                          (into modal-map
                                {:on-query-change (standard-on-change-for [:modal :query] false)
                                 :sort-fn (daihelp/map-compare-fn-for {:essence 100 :ability 10 :title 1})
                                 :aux-component (fp/form-field-for {:field-type :select-single, :value ability, :label "Ability", :path [:modal :ability], :options daihelp/ability-keys})
                                 :element-data
                                                  (map (fn [{:keys [name min-essence min-ability ability parent-id prereq-charms description] :as a}]
                                                         {:title  name
                                                          :essence min-essence
                                                          :ability min-ability
                                                          :img    (:view (daistate/fetch-view-for [:rulebooks (:parent-id a) :img]))
                                                          :lines [(str "Mins: Essence " min-essence ", " (make-pretty ability) " " min-ability)
                                                                  (str "Prerequisite Charms: " prereq-charms)
                                                                  (str "Description: " description)]
                                                          :disallowed (not (and (or (>= character-essence min-essence)
                                                                                    (= character-supernal ability))
                                                                                (>= (ability character-abilities)
                                                                                    min-ability)))
                                                          :key    (:name a)})
                                                       (->> {:thing-name     (str/lower-case query)
                                                             :path-before-id [:rulebooks]
                                                             :path-after-id  [:charms ability :charms]
                                                             :id-vec         rulebook-ids
                                                             :exact-match?   false}
                                                            (daistate/get-named-elements)
                                                            (map daistate/put-id-into-named-elements)
                                                            (reduce conj)
                                                            (filter (fn [a] (not ((set character-charms) (:name a)))))))}))]


       :buttons         [[:button
                          {:on-click
                           #(daistate/apply-modal-and-hide change-path
                                                           (fn [a] (conj a (:view (daistate/fetch-view-for [:modal :selected])))))}
                          "Add Selected Charm"]]})))

[:description
 :confers-merits
 :name
 :upgrading
 :type
 :page
 :repurchasable
 :character-tags
 :ranks]
