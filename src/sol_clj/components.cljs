(ns sol-clj.components
  (:require [reagent.core :as r]
            [sol-clj.card :as card]
            [sol-clj.drag-n-drop :as dnd]))

(defn space [args] [:div.card.empty args])

(defn card
  [{:keys [front value on-click card-num location suite child state]
    :or {front false suite "" value "" on-click identity}}]
  [:div.card-wrapper
   {:draggable front
    :on-drag-start (partial dnd/drag-start value location)
    :on-drag-end dnd/drag-end}
   [:div.card
    {:class (if front
              (str " front " (-> suite card/color name))
              "back")
     :on-click on-click
     :on-double-click #(card/double-click state location %)}
    [:div.content
     (when front
       [:div
        [:h1 (name value)]
        [:h1 (name suite)]])]]
   child])

(defn- card-container-loop [state location cidx cards]
  (when-let [fc (first cards)]
    (let [[cn f] fc
          [v s] (card/get-card cn)]
      [card {:front f :value (card/display-card v) :suite s
             :location (conj location cidx) :state state
             :child (card-container-loop state location
                                         (inc cidx) (rest cards))}])))

(defn- card-container
  ([{:keys [location state cards] :as args}]
   [:div.card-container
    {:on-drop (partial dnd/on-drop state location)
     :on-drag-enter (partial dnd/drag-enter state location)
     :on-drag-over (partial dnd/drop-check state location)
     :on-drag-exit dnd/drag-exit}
    (if (empty? @cards)
      [space]
      [card-container-loop state location 0 @cards])]))

(defn column [state cards idx]
  [card-container
   {:location [:table :columns idx]
    :state state
    :cards cards}])

(defn home [state cards idx]
  [card-container
   {:location [:table :homes idx]
    :state state :cards cards}])
