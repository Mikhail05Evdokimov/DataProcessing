(ns Main)

(defn string-generator [charList N]
  (if (zero? N)
    (list "")
    (for [c charList
          tail (string-generator charList (dec N))]
      (str c tail))))

(def no-repeats?
  (fn [s] (not-any? #(apply = %) (partition 2 1 s))))

(defn main [charList N]
  (filter no-repeats? (string-generator charList N)))

(def charList ["a" "b" "c"])
(def N 3)

(println(main charList N))