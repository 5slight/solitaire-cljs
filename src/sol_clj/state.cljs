(ns sol-clj.state
  (:require [reagent.core :as r]))

(defn create-table []
  {:homes   [[] [] [] []]
   :columns [[] [] [] [] [] [] []]})

(defn create-state []
  {:deck []
   :shown []
   :table (create-table)})

(defn create-deck []
  (range 1 (inc (* 13 4))))

(defn shuffle-deck [deck]
  (reduce #(shuffle %1) deck (range 0 5)))

(defn columns-row [pos]
  (reduce
   (fn [r num] (let [r' (+ num r)] (if (> pos r') r' (reduced [r num]))))
   0 (reverse (range 1 8))))

(defn current-column [pos]
  (let [[count row] (columns-row pos)
        diff (- 7 row)]
    (+ diff (- pos count))))

(defn deal-reducer
  [{:keys [deck]
    {tcols :columns :as table} :table
    :as state} pos]
  (let [cur (first deck)
        drest (rest deck)
        col (dec (current-column pos))
        ncols (update-in tcols [col] conj [cur false])
        ntable (assoc table :columns ncols)]
    (assoc state :deck drest :table ntable)))

(defn column-cards-count []
  (reduce #(+ %1 %2) 0 (range 1 8)))

(defn deal [state]
  (reduce
   deal-reducer
   state (range 1 (inc (column-cards-count)))))

(defn show-top [{{cols :columns} :table
                 :as state}]
  (assoc-in
   state [:table :columns]
   (mapv
    #(update-in % [(-> % count dec)] (fn [[cnum _]] [cnum true]))
    cols)))

(defn init [state]
  (letfn [(shuffle [s] (update-in s [:deck] (comp shuffle-deck
                                                  create-deck)))
          (swapfn [s] (-> s shuffle deal show-top))]
    (swap! state swapfn)))

(defn new-game [state]
  (reset! state (create-state))
  (init state))
