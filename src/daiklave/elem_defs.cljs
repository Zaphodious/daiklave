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
  [{:keys [path value options read-only class]}]
  [:input.field {:type      :text, :value value, :id (pr-str path)
                 :key       (pr-str path)
                 :class     (str class " " (if read-only "read-only" ""))
                 :on-change (standard-on-change-for path read-only),
                 :readOnly  read-only}])

(rum/defc text-area < rum/static
  [{:keys [path value options read-only]}]
  [:textarea.field {:id        (pr-str path)
                    :key       (pr-str path)
                    :value     value
                    :class     (if read-only "read-only" "")
                    :readOnly  read-only
                    :on-change (standard-on-change-for path read-only)}])

(rum/defc single-dropdown < rum/static
  [{:keys [path value options read-only special-change-fn] :as fieldmap}]
  [:select.field
   {:on-change (if special-change-fn
                 special-change-fn
                 #(standard-read-on-change-for path read-only))
    :id        (pr-str path)
    :class     (if read-only "read-only" "")
    :disabled  read-only
    :value     value}
   (map-indexed (fn [n a] [:option {:value (pr-str a), :key (str (pr-str path) "-select-" n)} (make-pretty a)])
                options)])

(rum/defc number-field < rum/static
  [{:keys [path value options read-only min max] :as fieldmap}]
  [:input.field
   {:type      :number
    :value     value :id (pr-str path) :key (pr-str path)
    :min       min :max max
    :on-change (standard-on-change-for path read-only)}])

(rum/defc dot-field < rum/static
  [{:keys [path value options read-only min max] :as fieldmap}]
  [:.field
   [:input.dot-entry
    {:type      :number
     :value     value :id (pr-str path) :key (pr-str path)
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
  [{:keys [path value readonly] :as fieldmap}]
  [:.field
   (map-indexed (fn [n a]
                  [:span.rank-selection
                   [:label.rank-label {:for   (pr-str (conj path n))
                                       :class (if (value n) "checked" "unchecked")}
                    n]
                   [:input {:type      :checkbox
                            :key       (pr-str (conj path n))
                            :id        (pr-str (conj path n))
                            :checked   (value n)
                            :on-change (fn [e]
                                         (daistate/change-element! path (if (value a)
                                                                          (set (remove #{a} value))
                                                                          (conj value a))))}]])

                (range 0 6))])

(rum/defc checkbox-field
  [{:keys [path value] :as fieldmap}]
  [:input.field
   {:type      :checkbox
    :id        (pr-str path)
    :key       (pr-str path)
    :checked   value
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

(rum/defc health-module < rum/state
  [{:keys [health-track path]}]
  [:health-module
   [:.button-bar
    [:button "Heal 1"]
    [:button "+ Bashing"]
    [:button "+ Lethal"]
    [:button "+ Aggravated"]]
   [:.button-bar [:button "Add Health Level"]]
   (fp/form-field-for {:field-type :select-single
                       :value :1
                       :options [:0 :1 :2 :4]})

   [:ul.track
    (map (fn [a]
           [:li.health-box {:class a} (make-pretty a)])
         (daihelp/inflate-health-track health-track))]])


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

(println "getting to home!")

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
  (fp/page-of
    (:name view)
    (:description view)
    (:img view)
    (into
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
         "Martial Arts"
         "martial-arts-section-link"
         (conj path :martial-arts-styles)
         [:p "Super powerful special stuff"])])))

(defmethod fp/page-for-viewmap :merits
  [{:keys [path view] :as viewmap}]
  ;[page-title page-subtitle page-img path elements new-element sort-fn form-fn]
  (fp/page-table-for
    {:page-title    (:name view)
     :page-subtitle (:description view)
     :page-img      (:img view)
     :path          (conj path :merit-vec)
     :elements      (:merit-vec view)
     :new-element   {:name          "Allies"
                     :description   "Allies, yo!"
                     :drawback      "Dey be people n shiii"
                     :page          158
                     :ranks         #{1 3 5}
                     :repurchasable true
                     :type          :story}
     :sort-fn       (daihelp/map-compare-fn-for {:name 3})
     :form-fn       (fn [a p]
                      (fp/form-of
                        (:name a)
                        (str (:name a) "-form")
                        [{:field-type :text, :label "Name", :value (:name a), :path (conj p :name)},
                         {:field-type :big-text, :label "Description", :value (:description a), :path (conj p :description)}
                         {:field-type :number, :label "Page", :value (:page a), :path (conj p :page)}
                         {:field-type :select-single, :label "Type", :value (:type a), :path (conj p :type), :options [:story :purchased :innate :flaw]},
                         {:field-type :merit-possible-ranks, :label "Ranks", :path (conj p :ranks), :value (:ranks a)}
                         {:field-type :boolean, :label "Repurchasable", :path (conj p :repurchasable), :value (:repurchasable a)}
                         (when (:repurchasable a)
                           {:field-type :boolean, :label "Upgrading", :path (conj p :upgrading), :value (:upgrading a)})]))}))

(defmethod fp/page-for-viewmap :mundane-weapons
  [{:keys [path view] :as viewmap}]
  (fp/page-table-for
    {:page-title    (:name view)
     :page-subtitle (:description view)
     :page-img      (:img view)
     :path          (conj path :weapons-vec)
     :elements      (:weapons-vec view)
     :new-element   {:name        "Wind and Fire Wheel"
                     :description "An elegant weapon, from a more... civilized age."
                     :tags        "Lethal, Martial Arts, Disarming"
                     :category    :light
                     :cost        2}
     :sort-fn       (daihelp/map-compare-fn-for {:category 5 :name 2})
     :form-fn       (fn [a p]
                      (fp/form-of
                        (:name a)
                        (str (:name a) "-form")
                        [{:field-type :text, :label "Name", :value (:name a), :path (conj p :name)},
                         {:field-type :big-text, :label "Description", :value (:description a), :path (conj p :name)}
                         {:field-type :select-single, :label "Type", :value (:type a), :path (conj p :type), :options [:light :medium :heavy]}
                         {:field-type :dots, :label "Cost", :value (:cost a), :path (conj p :cost), :min 0, :max 5,}]))}))


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
    (fp/page-table-for
      {:page-title      (:name base-view)
       :page-subtitle   (:description base-view)
       :page-img        (:img base-view)
       :path            path
       :elements        (get base-view (last path))
       :selector-title  "Which Ability"
       :selector-widget (charm-selector path)
       :new-element     {:name          "Wise Arrow"
                         :cost          "1m"
                         :min-essence   1
                         :min-ability   2
                         :category      :charm
                         :ability       charm-focus
                         :page          255
                         :keywords      ""
                         :type          :supplemental
                         :duration      "Instant"
                         :prereq-charms "None"
                         :description   "Fire an arrow which knows which way is up"}
       :sort-fn         (daihelp/map-compare-fn-for {:page 5, :min-essence 3, :min-ability 1})
       :form-fn         (fn [a p]
                          (fp/form-of
                            (:name a)
                            (str (:name a) "-form")
                            [{:field-type :text, :label "Name", :value (:name a), :path (conj p :name)},
                             {:field-type :text, :label "Cost", :value (:cost a), :path (conj p :cost)},
                             {:field-type :select-single, :label "Ability", :value (:ability a), :path (conj p :ability), :options daihelp/ability-all-keys, :read-only true},
                             {:field-type :dots, :label "Min Essence", :value (:min-essence a), :path (conj p :min-essence), :min 1, :max 5,}
                             {:field-type :dots, :label (str "Min " (make-pretty (:ability a))), :value (:min-ability a), :path (conj p :min-ability), :min 1, :max 5,}
                             {:field-type :big-text, :label "Description", :value (:description a), :path (conj p :name)},
                             {:field-type :number, :label "Page", :value (:page a), :path (conj p :page)}
                             {:field-type :select-single, :label "Type", :value (:type a), :path (conj p :type), :options [:simple :supplemental :reflexive :permanent]}
                             {:field-type :text, :label "Keywords", :value (:keywords a), :path (conj p :keywrods)}
                             {:field-type :text, :label "Duration", :value (:duration a), :path (conj p :duration)}
                             {:field-type :text, :label "Prereq Charms", :value (:prereq-charms a), :path (conj p :prereq-charms)}]))})))

(defmethod fp/page-for-viewmap :charms
  [viewmap]
  (charm-page viewmap))

(defmethod fp/page-for-viewmap :martial-arts-styles
  [{:keys [path view] :as viewmap}]
  (fp/page-table-for
    {:page-title    (:name view)
     :page-subtitle (:description view)
     :page-img      (:img view)
     :path          (conj path :martial-arts-vec)
     :elements      (:martial-arts-vec view)
     :new-element   {:name        "Black Claw Style"
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
                                    :description   "From the very beginning, things start to go wrong. Righteous heroes find themselves cast as vicious bullies when they fight a student of the Black Claw—even when she starts the fight. Open Palm Caress can be used whenever the martial artist rolls Join Battle. As long as at least one enemy received more successes on the roll than the martial artist did, he and his allies are seen as having initiated hostilities, regardless of how the fight actually began. This applies both to any bystanders to the fight and to the enemies themselves, who might suddenly find themselves confused as to why they are attacking the Black Claw stylist. Characters may see through this deception with a reflexive read intentions roll against the martial artist’s Guile. The Black Claw stylist gains a single point of Initiative for each opponent or bystander who was fooled by this ruse, up to a maximum of her Manipulation. \n\n Mastery: The martial artist’s performance is so convincing that if she uses her first turn to make a clinch or decisive attack against an enemy who beat her Join Battle and failed to overcome her Guile, she may pay a point of Willpower to treat that attack as an ambush."}]}
     :sort-fn       (daihelp/map-compare-fn-for {:name 2})
     :form-fn       (fn [a p]
                      (fp/form-of
                        (:name a)
                        (str (:name a) "-form")
                        [{:field-type :text, :label "Name", :value (:name a), :path (conj p :name)},
                         {:field-type :big-text, :label "Description", :value (:description a), :path (conj p :name)}]))}))




(defmethod fp/page-for-viewmap :character
  [{:keys [path view] :as viewmap}]
  (fp/page-of (:name view)
              (:description view)
              (:img view)
              [(fp/form-of
                 "Core Info"
                 "coreinfo"
                 [{:field-type :text, :label "Name", :value (:name view), :path (conj path :name)}
                  {:field-type :text, :label "Epithet", :value (:description view), :path (conj path :description)}
                  {:field-type :text, :label "Player", :value (:player view), :path (conj path :player)}
                  {:field-type :select-single, :label "Type", :value (:type view), :path (conj path :type), :options [:solar, :mortal], :read-only (not (= :mortal (:type view)))}
                  {:field-type :text, :label "Image", :value (:img view), :path (conj path :img)}
                  {:field-type :big-text, :label "Anima", :value (:anima view), :path (conj path :anima), :read-only (= :mortal (:type view))}
                  {:field-type :select-single, :label "Caste", :value (:subtype view), :path (conj path :subtype), :options [:dawn, :eclipse, :night, :twilight, :zenith]}
                  {:field-type :select-single :label "Supernal" :read-only (= :mortal (:type view)) :path (conj path :supernal), :value (:supernal view), :options (into [] (set/intersection (set (:favored-abilities view)) (get daihelp/caste-abilities (:subtype view))))}])
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
                           (map (fn [[k v]]
                                  {:field-type :dots,
                                   :label      (make-pretty k)
                                   :value      v, :path (into path [:abilities k])
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
               (fp/soft-table-for "Specialties"
                                  "specialtyinfo"
                                  (conj path :specialties)
                                  [(:supernal view) "Doing Awesome Things"]
                                  compare
                                  (map-indexed (fn [n a]
                                                 (fp/mini-form-of
                                                   (last a)
                                                   [{:field-type :select-single,
                                                     :label      "Ability"
                                                     :value      (first a)
                                                     :path       (into path [:specialties n 0])
                                                     :options    daihelp/ability-all-keys
                                                     :class      "first-of-three"}
                                                    {:field-type :text,
                                                     :value      (second a)
                                                     :label      "Description"
                                                     :path       (into path [:specialties n 1])
                                                     :class      "third-of-three"}]))
                                               (:specialties view)))
               (fp/soft-table-for "Intimacies"
                                  "intimacyinfo"
                                  (conj path :intimacies)
                                  [:major "Disgust" "Dishonorable Combat"]
                                  compare
                                  (map-indexed (fn [n a]
                                                 (fp/mini-form-of
                                                   (last a)
                                                   [{:field-type :select-single,
                                                     :label      "Intensity"
                                                     :value      (first a)
                                                     :path       (into path [:intimacies n 0])
                                                     :options    [:defining, :major, :minor]
                                                     :class      "first-of-three"}
                                                    {:field-type :text,
                                                     :value      (second a)
                                                     :label      "Type"
                                                     :path       (into path [:intimacies n 1])
                                                     :class      "second-of-three"}
                                                    {:field-type :text,
                                                     :value      (last a)
                                                     :label      "Description"
                                                     :path       (into path [:intimacies n 2])
                                                     :class      "third-of-three"}]))
                                               (:intimacies view)))]))



;[path value options key name]


