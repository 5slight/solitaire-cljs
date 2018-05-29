(ns sol-clj.table
  (:require [reagent.core :as r]
            [sol-clj.components :as c]
            [sol-clj.deck :as d]
            [sol-clj.modals :as modals]
            [lightscale.recomps.modal :as m]))

(defn columns [state columns]
  [:div.columns
   (for [[idx _] (map-indexed vector @columns)]
     #^{:key idx} [c/column state (r/cursor columns [idx]) idx])])

(defn- homes [state homes]
  [:div.homes
   (for [[idx _] (map-indexed vector @homes)]
     #^{:key idx}[c/home state (r/cursor homes [idx]) idx])])

(defn table [state]
  (let [tab (r/cursor state [:table])
        homs (r/cursor tab [:homes])
        cols (r/cursor tab [:columns])]
    [:div
     [:div.top
      [d/deck state]
      [:div.card-spacer
       [:button {:on-click #(m/open modals/confirm-restart
                                    {:state state})}
        "New game"]
       [:button "Un"]
       [:button "Re"]]
      [homes state homs]]
     [columns state cols]]))
