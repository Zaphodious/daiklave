(ns daiklave.seq)

(defn remove-nth [vecc n]
  (let [first-sub (subvec vecc 0 n)
        last-sub (subvec vecc (inc n) (count vecc))]
    (into first-sub last-sub)))

(defn second-to-last
  [indexed-thing]
  (nth indexed-thing (- (count indexed-thing ) 2)))

(defn clear-nil [s]
  (filter #(not (nil? %)) s))