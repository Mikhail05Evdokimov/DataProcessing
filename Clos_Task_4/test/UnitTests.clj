(ns UnitTests
  (:require [clojure.test :refer :all]
            [Main]))

(deftest even-test
  (is (= 10 (nth (Main/parallel-filter 90 even? (range 100) ) 5)))
  )

(deftest empty-test
  (is (= () (Main/parallel-filter 50 even? () )))
  )

(deftest twenty-test
  (let [data (range 100) result (Main/parallel-filter 90 #(= % 20) data)]
    (is (= 20 (nth result 0) ))
  )
)

(deftest infinite-test
  (is (= 20000 (nth (Main/parallel-filter 10000 even? (range) ) 10000) ))
  )



