(ns sol-clj.core
  (:require [reagent.core :as r]
            [sol-clj.table :as t]
            [sol-clj.state :as s]))

(enable-console-print!)

(def elem (.getElementById js/document "app_container"))

(defn init []
  (let [state (s/create-state)]
    (r/render [t/table state] elem)
    (s/init state)))

(init)
