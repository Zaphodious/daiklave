(ns daiklave.scratch)

(comment "This namespace is for nothing. It exists as a place where I can play around with the code without hurting the rest of the project.")

(defn square-for-n [n]
  (reduce + (filter odd? (range 1 (* 2 n)))))

(defn square [n]
  (* n n))
(defn is-square? [numbah]
  (loop [n 1]
    (let [s (square n)]
      (cond
        (< s numbah) (recur (inc n))
        (> s numbah) false
        (= s numbah) true))))
