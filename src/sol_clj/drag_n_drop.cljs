(ns sol-clj.drag-n-drop
  (:require [cljs.reader :as reader]))

(defn get-drag-data [ev]
  (-> ev .-dataTransfer
      (.getData "application/edn")
      reader/read-string))

(defn- dissocv [v idx]
  (vec (concat (subvec v 0 idx) (subvec v (inc idx)))))

(defn drag-start [value location ev]
  (set! (-> ev .-dataTransfer .-dropEffect) "move")
  (let [target (.-currentTarget ev)]
    (.requestAnimationFrame
     js/window
     #(set! (-> target .-style .-opacity) "0")))
  (-> ev .-dataTransfer
      (.setData
       "application/edn"
       (pr-str {:value value
                :location location}))))

(defn drag-end [ev]
  (let [target (.-currentTarget ev)]
    (.requestAnimationFrame
     js/window
     #(set! (-> target .-style .-opacity) "1")
     500)))

(defn drop-check [location ev]
  (.preventDefault ev))

(defn on-drop [state location ev]
  (.preventDefault ev)
  (let [{cloc :location} (get-drag-data ev)
        c (get-in @state cloc)
        c (if (int? c) [c true] c)
        srccolloc (butlast cloc)
        srccol (get-in @state srccolloc)
        nsrccol (dissocv srccol (last cloc))]
    (swap! state assoc-in srccolloc nsrccol)
    (swap! state update-in location conj c)))
