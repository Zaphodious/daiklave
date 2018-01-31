(ns daiklave.data-help
  [:require [daiklave.seq :as daiseq :refer [vec-of nudge-insert-at]]])

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

(defn flatten-health-module [{:keys [levels bashing lethal aggravated]}]
  (map vec-of
       (conj
         (into []
           (reduce concat
                   (map-indexed (fn [n a]
                                  (take a (repeat (* -1 n))))
                                (nudge-insert-at levels 3 0))))
         "Inc")
       (concat
         (take aggravated (repeat :aggravated))
         (take lethal (repeat :lethal))
         (take bashing (repeat :bashing))
         (take (reduce + levels) (repeat :none)))))

