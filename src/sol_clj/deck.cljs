(ns sol-clj.deck
  (:require [reagent.core :as r]
            [sol-clj.card :as card]
            [sol-clj.components :as c]))

(defn deck [state]
  (let [deck (r/cursor state [:deck])
        shown (r/cursor state [:shown])
        next (fn []
               (if (empty? @deck)
                 (do (reset! deck @shown)
                     (reset! shown []))
                 (do (swap! shown conj (first @deck))
                     (swap! deck rest))))]
    (fn [state]
      (let [shown' @shown]
        [:div.deck
         [:div.card-container
          [(if (empty? @deck) c/space c/card) {:on-click next}]]
         [:div.card-container
          (if (empty? @shown)
            [c/space]
            [:div.stack
             (for [[idx c] (map-indexed vector (take-last 2 shown'))]
               (let [[v s] (card/get-card c)]
                 #^{:key c}
                 [c/card
                  {:front true
                   :location [:shown (dec (count shown'))]
                   :value (card/display-card v) :suite s}]))])]]))))
