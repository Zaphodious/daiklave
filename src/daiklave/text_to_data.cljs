(ns daiklave.text-to-data
  (:require [clojure.string :as str]))


(defn split-text-into-keyword-string-pairs [block]
  (let [splitsville (->> (str/split block #"\n")
                         (map str/trim))
        description (last splitsville)
        name (first splitsville)]
    (-> (->> splitsville
             (map #(str/split % #": "))
             (map (fn [[a b :as c]]
                    (let [keyword (if b (-> a (str/lower-case) (str/replace " " "-") (keyword))
                                        :name)
                          content (if b b
                                        a)]
                      [keyword content])))
             (into {}))
        (assoc :name name)
        (assoc :description description))))

;(defn modify-mins-from-old-fn [{:keys [mins] :as thingy}]
;  (-> thingy
;      (assoc :min-ability (first mins))
;      (assoc :min-rank (second mins))
;      (assoc :min-essence (last mins))
;      (dissoc :mins)))
#_{:name          "Wise Arrow"
   :cost          "1m"
   :min-essence   1
   :min-ability   2
   :category      :charm
   :ability       :archery
   :page          255
   :keywords      "Uniform"
   :type          :supplemental
   :duration      "Instant"
   :prereq-charms "None"
   :description   "Fire an arrow which knows which way is up"}
(def replace-mins (fn [{:keys [mins] :as thingy}]
                    (let [[ability-min essence-min] (str/split mins ", ")
                          [ability-str ability-rank-str] (str/split ability-min " ")
                          ability-key (-> ability-str (str/lower-case) (keyword))
                          ability-rank (int ability-rank-str)
                          essence-rank (-> essence-min (last) (int))]
                      (-> thingy
                          (assoc :ability ability-key)
                          (assoc :min-ability ability-rank)
                          (assoc :min-essence essence-rank)
                          (dissoc :mins)))))
(def drop-duration (fn [thingy] (dissoc thingy :duration)))
(def keywordify-type (fn [{:keys [type] :as thingy}]
                       (assoc thingy :type (-> type str/lower-case keyword))))
(def vectorify-keywords (fn [{:keys [keywords] :as thingy}]
                          (assoc thingy :keywords keywords
                                        #_(->> ", "
                                               (str/split keywords)
                                               (map #(str/replace % " " "-"))
                                               (map str/lower-case)
                                               (map keyword)
                                               (remove #(= % :none))
                                               (into [])))))
(def split-up-prereqs (fn [{:keys [prereq-charms] :as thingy}]
                        (assoc thingy :prereq-charms
                                      (into #{} (str/split prereq-charms ", ")))))
(defn add-blank-tags [thingy] (assoc thingy :character-tags []))
(defn add-page-field [thingy] (assoc thingy :page 255))
(defn charm-fix-text-errors [cb]
  (-> cb
      (str/replace "Re exive" "Reflexive")
      (str/replace "; " "\n")
      (str/replace " Type:" "\nType:")
      (str/replace "Martial Arts" "martial-arts")))

(defn change-prereq-field [thing]
  (let [n (:prerequisite-charms thing)]
    (-> thing
        (dissoc :prerequisite-charms)
        (assoc :prereq-charms n))))

(defn fix-prereq-charms [thingy]
  (if (= #{"None"} (:prereq-charms thingy))
    (assoc thingy :prereq-charms #{})
    thingy))
(defn charm-to-data [cb]
  (let []
    (->> cb
         (charm-fix-text-errors)
         (split-text-into-keyword-string-pairs)
         (replace-mins)
         ;(drop-duration)
         (keywordify-type)
         (vectorify-keywords)
         ;(split-up-prereqs)
         (add-blank-tags)
         (add-page-field)
         (change-prereq-field))))
;(fix-prereq-charms))))

(defn bulk-charm-to-data [bcb]
  (->> (-> bcb (str/split "\n\n"))
       (map charm-to-data) (into [])))

(defn seq-to-sss [vecci elm-to-string]
  (->> vecci
       (map elm-to-string)
       (map #(str % "; "))
       (reduce str)
       (str/trim)
       (drop-last)
       (reduce str)))

(defn sss-to-seq [sss into-seq string-to-elm]
  (->> (-> sss (str/split ";"))
       (map str/trim)
       (map string-to-elm)
       (into into-seq)))