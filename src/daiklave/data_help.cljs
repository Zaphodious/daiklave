(ns daiklave.data-help)

(def attribute-keys [:strength :dexterity :stamina :charisma :manipulation :appearance :perception :intelligence :wits])

(def attribute-display-order (into {}
                                   (map (fn [a n]
                                          [a n])
                                        attribute-keys
                                        (range 1 (inc (count attribute-keys))))))
(defn sort-attributes-by [a b]
  (< (a attribute-display-order) (b attribute-display-order)))

(defn sort-attribute-map [attr-map]
  (into (sorted-map-by sort-attributes-by) attr-map))

(def ability-keys [:archery, :athletics, :awareness, :brawl, :bureaucracy, :craft, :dodge, :integrity, :investigation, :larceny, :linguistics, :lore, :medicine, :melee, :occult, :performance, :presence, :resistance, :ride, :sail, :socialize, :stealth, :survival, :thrown, :war])

(def ability-additional-keys [:craft, :martial-arts])

(def ability-all-keys (into ability-keys ability-additional-keys))

(def caste-abilities {:dawn     #{:archery, :awareness, :brawl, :martial-arts, :dodge, :melee, :resistance, :thrown, :war}
                      :zenith   #{:athletics, :integrity, :performance, :lore, :presence, :resistance, :survival, :war}
                      :twilight #{:bureaucracy, :craft, :integrity, :investigation, :linguistics, :lore, :medicine, :occult}
                      :night    #{:athletics, :awareness, :dodge, :investigation, :larceny, :ride, :stealth, :socialize}
                      :eclipse  #{:breaucracy, :larceny, :linguistics, :occult, :presence, :ride, :sail, :socialize}})

(def intimacy-intensities [:defining :major :minor])

(defn- always [x] (fn [& _] x))
(defn- inflate-health-track-imp [{:keys [levels bashing lethal aggravated] [zero one two four] :levels}]
  (let [base-track
        (reduce into []
                [(take zero (repeat 0))
                 (take one (repeat -1))
                 (take two (repeat -2))
                 (take four (repeat -4))])
        damage-track
        (reduce into []
              [(take bashing (repeat :bashing))
               (take lethal (repeat :lethal))
               (take aggravated (repeat :aggravated))
               (take (- (reduce + levels) (+ bashing lethal aggravated)) (repeat :empty))])]
    (map list
         base-track
         damage-track)))

(def inflate-health-track (memoize inflate-health-track-imp))

(defn- inflate-ability-map-imp [old-ab-map]
  (into (sorted-map)
        (map (fn [ab]
               (let [v (get old-ab-map ab)]
                 {ab (if v v 0)}))
             ability-keys)))
(def inflate-ability-map (memoize inflate-ability-map-imp))

(defn map-compare-fn-for
  [magnitude-map]
  (fn [a b]
    (reduce +
            (map
              (fn [[k v]]
                (* v (compare (k a) (k b))))
              magnitude-map))))