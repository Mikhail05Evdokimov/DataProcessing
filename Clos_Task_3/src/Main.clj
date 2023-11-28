(ns Main)

(defn parallel-filter [f coll]
  (if (empty? coll)
    coll
    (let [;n 4                                                  ; n - Number of threads (futures)
          ;parts (partition-all (/ (count coll) n) coll)       ; Partition the collection into n parts
          parts (partition-all 2000001 coll)
          futures (map #(future (filter f %)) parts)]       ; Put each part in a separate future
      (apply concat (map deref futures)))))

(def data (range))
(def result (parallel-filter even? data))

(time (doall (take 4000000 result)))
(time (doall (take 4000000 (filter even? data))))


