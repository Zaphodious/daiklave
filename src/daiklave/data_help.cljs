(ns daiklave.data-help
  [:require [daiklave.seq :as daiseq :refer [vec-of nudge-insert-at]]
            [clojure.string :as str]
            [com.rpl.specter :as sp]])

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

(def ability-keys [:archery, :athletics, :awareness, :brawl, :bureaucracy, :dodge, :integrity, :investigation, :larceny, :linguistics, :lore, :medicine, :melee, :occult, :performance, :presence, :resistance, :ride, :sail, :socialize, :stealth, :survival, :thrown, :war])

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
       (into (list "Inc")
             (reverse
               (reduce
                 concat
                 (map-indexed (fn [n a]
                                (take a (repeat (* -1 n))))
                              (nudge-insert-at levels 3 0)))))
       (concat
         (take aggravated (repeat :aggravated))
         (take lethal (repeat :lethal))
         (take bashing (repeat :bashing))
         (take (reduce + levels) (repeat :none)))))

(defn string-to-keyword-vec [stringer]
  (->> (-> stringer str/trim str/lower-case (str/replace " " ", ") (str/replace "," "  ") (str/split " "))
       (filter #(not (= "" %)))
       (map keyword)
       (into [])))
(defn keyword-vec-to-string [keyvec]
  (->> keyvec
       (map name)
       (map str/capitalize)
       (map #(str % ", "))
       (reduce str)
       (str/trim)))

(defn personal-essence-for [rank]
  (+ 10 (* rank 3)))

(defn peripheral-essence-for [rank]
  (+ 27 (* rank 7)))

(defn inflate-essence-map [{:keys [rating xp-spent xp-wallet rating motes-spent-personal motes-spent-peripheral motes-committed-personal motes-committed-peripheral] :as essence-map}]
  (let [essence-max-peripheral (- (peripheral-essence-for rating) motes-committed-peripheral)
        essence-max-personal (- (personal-essence-for rating) motes-committed-personal)]
    (into essence-map
          {:essence-max-personal         essence-max-personal
           :essence-max-peripheral       essence-max-peripheral
           :essence-personal-remaining   (- essence-max-personal motes-spent-personal)
           :essence-peripheral-remaining (- essence-max-peripheral motes-spent-peripheral)})))

(defn get-these-rulebooks [rulebook-key-vec rulebooks]
  (map
    (fn [sk] (get rulebooks sk))
    rulebook-key-vec))

(defn get-thing-in-rulebooks [thing-name rulebook-vec]
  (map
    (fn [a] (sp/select
              (sp/walker #(= (str (:name %)) "Mutant"))
              rulebook-vec))
    rulebook-vec))

(defn get-imgur-img-id [imgur-src]
  (when
    (and imgur-src
           (str/includes? imgur-src "imgur"))
    (-> imgur-src
        (str/split "imgur.com/")
        last
        (str/split ".")
        first)))

(defn thumbnail-for [img-src]
  (let [imgur-id (get-imgur-img-id img-src)]
    (if imgur-id
      (str "http://i.imgur.com/" imgur-id "l.png")
      img-src)))