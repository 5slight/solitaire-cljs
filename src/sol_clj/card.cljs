(ns sol-clj.card)

(def color {:hart   :red
            :dimond :red
            :spade  :black
            :club   :black})

(def suite {0 :hart
            1 :dimond
            2 :club
            3 :spade})

(defn display-card [c]
  (cond
    (and (> c 0) (< c 11)) (str c)
    (= c 11) "Jack"
    (= c 12) "Queen"
    (= c 13) "King"))

(defn get-card [c]
  (let [num (inc (mod c 13))
        suite-num (.floor js/Math (/ (dec c) 13))]
    [num (get suite suite-num)]))
