(ns Main)

(defn enqueue [sieve candidate step]
  (let [m (+ candidate step)]
    (if (sieve m)
      (recur sieve m step)
      (assoc sieve m step))))

(defn next-sieve [sieve candidate]
  (if-let [step (sieve candidate)]
    (enqueue (dissoc sieve candidate) candidate step)
    (enqueue sieve candidate (+ candidate candidate))))

(defn next-prime [sieve candidate]
  (if (sieve candidate)
    (next-prime (next-sieve sieve candidate) (+ 2 candidate))
    (cons candidate
          (lazy-seq (next-prime (next-sieve sieve candidate) (+ 2 candidate))))))

(def primes (cons 2 (next-prime {} 3)) )

(print (take 10 primes) )