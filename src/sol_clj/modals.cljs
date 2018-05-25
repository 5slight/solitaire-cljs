(ns sol-clj.modals
  (:require [lightscale.recomps.modal :as m]
            [sol-clj.state :as s]
            [sol-clj.card :as c]))

(defn game-complete-view [self state]
  [:div
   [:h1 "Game complete"]
   [:p "You have successully comleted the game"]
   [:button {:on-click (fn [_]
                         (s/new-game (:state @state))
                         (m/close self))}
    "New game"]])

(def game-complete (m/modal game-complete-view))

(defn game-complete-open [state]
  (when (c/check-complete @state)
    (m/open game-complete)))

(def confirm-restart
  (m/confirm {:question "Are you sure you want to restart the game?"
              :on-success
              (fn [self {state :state}]
                (reset! state (s/create-state))
                (s/init state)
                (m/close self))}))
