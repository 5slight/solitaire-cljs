(ns sol-clj.core
  (:require [reagent.core :as r]
            [sol-clj.table :as t]
            [sol-clj.state :as s]
            [sol-clj.modals :as modals]
            [lightscale.recomps.modal :as m]))

(enable-console-print!)

(def elem (.getElementById js/document "app_container"))

(defn init []
  (let [state (r/atom (s/create-state))]
    (r/render
     [:div
      [t/table state]
      [m/view modals/game-complete]
      [m/view modals/confirm-restart]]
     elem)
    (s/init state)))

(init)
