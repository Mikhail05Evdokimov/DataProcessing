(ns Main)

(defn init-indexes [collections]
  (reduce #(for [x %1 y %2] (conj x y)) [[]] collections))

(defn string-generator [char-list n]
  (for [indexes (apply init-indexes (list (repeat n (range (count char-list)))))]
    (apply str (map #(nth char-list %) indexes))))

(def no-repeats?
  (fn [s] (not-any? #(apply = %) (partition 2 1 s))))

(defn main [char-list N]
  (filter no-repeats? (string-generator char-list N)))

(def char-list ["a" "b" "c"])
(def N 3)

(println(main char-list N))