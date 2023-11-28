(ns UnitTests
  (:require [clojure.test :refer :all]
            [Main]))

(deftest even-test
  (is (= 10 (nth (Main/parallel-filter even? (range 100) ) 5)))
  )

(deftest empty-test
  (is (= () (Main/parallel-filter even? () )))
  )

(deftest twenty-test
  (let [data (range 100) result (Main/parallel-filter #(= % 20) data)]
    (is (= 20 (nth result 0) ))
  )
)

(deftest infinite-test
  (is (= 20000 (nth (Main/parallel-filter even? (range) ) 10000) ))
  )



