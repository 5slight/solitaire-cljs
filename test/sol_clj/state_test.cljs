(ns sol-clj.state-test
  (:require [sol-clj.state :as state]
            [cljs.test :refer [deftest is]]))

(deftest create-table
  (is (contains? (state/create-table) :home) "Contains home"))
