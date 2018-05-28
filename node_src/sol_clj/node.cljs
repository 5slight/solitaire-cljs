(ns sol-clj.node
  (:require [reagent.core :as r]
            [reagent.dom.server :as rs]
            [sol-clj.table :as t]
            [sol-clj.state :as s]
            [sol-clj.modals :as modals]
            [lightscale.recomps.modal :as m]
            [cljs.nodejs :as node]))

(enable-console-print!)

(def fcgi (node/require "node-fastcgi"))

(defn page [state]
  [:html
   [:head
    [:meta {:charset "UTF-8"}]
    [:title "Solitaire"]
    [:link {:rel "stylesheet" :type "text/css" :href "css/main.css"}]]
   [:body
    [:div#app_container
     [:div
      [t/table state]
      [m/view modals/game-complete]
      [m/view modals/confirm-restart]]]
    [:script {:src "main.js"}]]])

(defn render []
  (let [state (r/atom (s/create-state))]
    (rs/render-to-string
     [page state])))

(defn handler [req res]
  (doto res
    (.writeHead 200 {:ContentType "text/html"})
    (.end (render))))

(defn -main []
  (-> fcgi (.createServer handler) (.listen 9000)))

(-main)
