(ns Main)

(defn parallel-filter [n f coll]
  (if (empty? coll)
    coll
    (let [
          parts (partition-all (/ n 4) coll)                ; 4 - number of threads, n/4 - numbers set size
          futures (map #(future (filter f %)) parts)]       ; Put each part in a separate future
      (apply concat (map deref futures)))))

(def data (range))
;бенчмарки на clojure
(defn take-from-parallel-filter [n f coll] (take n (parallel-filter n f coll)))

(time (doall (take-from-parallel-filter 400000 even? data)))
(time (doall (take 400000 (filter even? data))))