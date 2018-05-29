(ns sol-clj.drag-n-drop
  (:require [cljs.reader :as reader]
            [sol-clj.card :as c]
            [sol-clj.modals :as modals]
            [sol-clj.history :as hist]))

(def drag-data (atom nil))

(defn- get-drag-data [] @drag-data)

(defn- targ-match? [ev] (= (.-target ev) (.-currentTarget ev)))
(defn- ev-elem [ev] (.-currentTarget ev))

(defn drag-start [value location ev]
  (when (targ-match? ev)
    (let [target (.-currentTarget ev)]
      (.requestAnimationFrame
       js/window
       #(set! (-> target .-style .-opacity) "0")))
    (set! (-> ev .-dataTransfer .-dropEffect) "move")
    (-> ev .-dataTransfer (.setData "text/plain" (str value)))
    (reset! drag-data
            {:value value
             :location location})))

(defn drag-end [ev]
  (when (targ-match? ev)
    (let [targ (ev-elem ev)]
      (.requestAnimationFrame
       js/window
       #(set! (-> targ .-style .-opacity) "1")
       500))))

(defn drop-check [state location ev]
  (when (c/card-drop-allow
         @state
         (:location (get-drag-data))
         location)
    (.preventDefault ev)))

(defn drag-enter [state location ev]
  (when  (c/card-drop-allow
          @state
          (:location (get-drag-data))
          location)
    (.preventDefault ev)
    (-> ev ev-elem .-classList (.add "drag-over"))))

(defn drag-exit [ev]
  (-> ev ev-elem .-classList (.remove "drag-over")))

(defn on-drop [state location ev]
  (.preventDefault ev)
  (-> ev ev-elem .-classList (.remove "drag-over"))
  (let [{cloc :location} (get-drag-data)]
    (c/move-card state cloc location)
    (modals/game-complete-open state)))
