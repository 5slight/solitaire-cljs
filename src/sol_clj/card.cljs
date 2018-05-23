(ns sol-clj.card)

(def color {:heart   :red
            :diamond :red
            :spade   :black
            :club    :black})

(def suite {0 :heart
            1 :diamond
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

(defn home-check [[src-card _] [dest-card _]]
  (let [[snum ssuite] (get-card src-card)
        [dnum dsuite] (get-card dest-card)]
    (or
     (and (nil? dest-card)
          (= snum 1))
     (and (= ssuite dsuite)
            (= dnum (dec snum))))))

(defn column-check [[src-card _] [dest-card f]]
  (let [[snum ssuite] (get-card src-card)
        [dnum dsuite] (get-card dest-card)
        scolor (get color ssuite)
        dcolor (get color dsuite)]
    (or
     (and (nil? dest-card)
          (= snum 13))
     (and (not= scolor dcolor)
          (= (inc snum) dnum)
          f))))

(defn card-drop-allow [state src-loc dest-loc]
  (let [dest (second dest-loc)
        src-card (let [c (get-in state src-loc)]
                   (if (int? c) [c true] c))
        dest-card (last (get-in state dest-loc))]
    ((condp = dest
       :homes home-check
       :columns column-check)
     src-card dest-card)))
