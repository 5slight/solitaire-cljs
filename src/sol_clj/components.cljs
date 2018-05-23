(ns sol-clj.components
  (:require [reagent.core :as r]
            [sol-clj.card :as card]
            [sol-clj.drag-n-drop :as dnd]))

(defn space [args] [:div.card.empty args])

(defn card
  [{:keys [front value on-click card-num location suite child]
    :or {front false suite "" value "" on-click identity}}]
  [:div.card-wrapper
   {:draggable front
    :on-drag-start (partial dnd/drag-start value location)
    :on-drag-end dnd/drag-end}
   [:div.card
    {:class (if front
              (str (name suite) " val_" (name value)
                   " front " (-> suite card/color name))
              "back")
     :on-click on-click}
    [:div.content
     (when front
       [:div
        [:h1 (name value)]
        [:h1 (name suite)]])]]
   child])

(defn- card-container-loop [location cidx cards]
  (when-let [fc (first cards)]
    (let [[cn f] fc
          [v s] (card/get-card cn)]
      [card {:front f :value (card/display-card v) :suite s
             :location (conj location cidx)
             :child (card-container-loop location (inc cidx) (rest cards))}])))

(defn- card-container
  ([{:keys [location state cards] :as args}]
   [:div.card-container
    (let [dcheck (partial dnd/drop-check location)]
      {:on-drop (partial dnd/on-drop state location)
       :on-drag-enter dcheck :on-drag-over dcheck})
    (if (empty? @cards)
      [space]
      [card-container-loop location 0 @cards]
      #_(for [[cidx [cn front]] (map-indexed vector @cards)]
          (let [[v s] (card/get-card cn)]
            #^{:key cidx}
            [card {:front front :value (card/display-card v) :suite s
                   :location (conj location cidx)}])))]))

(defn column [state cards idx]
  [card-container
   {:location [:table :columns idx]
    :state state
    :cards cards}])

(defn home [state cards idx]
  [card-container
   {:location [:table :homes idx]
    :state state :cards cards}])
