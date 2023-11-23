(ns UnitTests
  (:require [clojure.test :refer :all]
            [Main]))

(deftest simple-tests
         (is (= 11 (Main/get-prime 5)))
         )

(deftest first-prime-test
  (is (= 2 (Main/get-prime 1)))
  )

(deftest second-prime-tests
  (is (= 3 (Main/get-prime 2)))
  )

(deftest zero-tests
  (is (= nil (Main/get-prime 0)))
  )

(deftest below-zero-tests
  (is (= nil (Main/get-prime -10)))
  )

(deftest big-number-tests
  (is (= 541 (Main/get-prime 100)))
  )

(deftest very-big-number-tests
  (is (= 104729 (Main/get-prime 10000)))
  )