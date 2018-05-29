(ns sol-clj.history
  (:refer-clojure :exclude [pop]))

(def size 5)

(defn init []
  {:position 0
   :states []})

(defn push [state hist]
  (let [{:keys [position states]} @hist
        statesc (count states)
        states (if (< statesc size) states (rest states))
        undod (< position (dec statesc))]
    (reset!
     hist
     {:position (inc position)
      :states
      (conj
       (if undod
         (subvec states 0 (inc position))
         states)
       state)})))

(defn pop [state hist]
  ())
