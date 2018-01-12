(ns gardener
  (:require [garden.core :as g]
            [garden.def :as gd]
            [garden.media :as gm]
            [garden.color :as gc]
            [garden.arithmetic :as ga]
            [clojure.string :as str]))

(gd/defcssfn url)
(gd/defcssfn blur)
(gd/defcssfn calc)


(defn name-or-string [thing]
  (try
    (name thing)
    (catch Exception e
      (str thing))))

(defn name-if-not-symbol [thing]
  (if (symbol? thing) thing (name-or-string thing)))

(def opmap {- " - " + " + " * " * " / " / "})

(defn replace-operators [possible-op]
  (if-let [oper (get opmap possible-op)]
    oper
    possible-op))

(defn calchelper [& args]
  (let [calcstring (->> args (map replace-operators) (map name-or-string) (map #(str % " ")) (reduce str) str/trim)]
    (calc calcstring)))

(defn quoth [n]
  (str "/'" n "/'"))

(def colors
  {:titlebar-back (gc/rgba 0, 0, 0, 0.64)
   :titlebar-text (gc/rgba 1, 1, 1, 1)})

(def color-p-main (gc/hex->rgb "#795548"))
(def color-p-light (gc/hex->rgb "#a98274"))
(def color-p-dark (gc/hex->rgb "#4b2c20"))
(def color-brightest (gc/from-name :white))
(def color-off-bright (gc/hex->rgb "#e0e0e0"))
(def color-darkest (gc/from-name :black))
(def color-off-dark (gc/lighten color-darkest 0.2))


(defn prefix-it [stylekey stylerule]
  (let [prefixes ["moz" "webkit" "o"]
        stylekeys (map (fn [a] (keyword (str "-" a "-" (name stylekey))))
                       prefixes)]
    (into {} (map #(vector % stylerule) stylekeys))))

(def title-bar-height "3em")
(def navshadow "0 0 15px black")
(def elementshadow "0 0 5px grey")

(def mobilestyle
  [[:* {:margin      0 :padding 0
        :font-family "Bellefair, sans-serif"
        :color       color-off-dark}]
   [:h1 {:font-size "2em"
         :padding   ".25em"}]
   [:h2 {:font-size "1.5em"}]
   [:body {:background-image      "none"                    ;(url "../img/ex_map.png")
           :background-color      color-off-bright
           :background-position   "center"
           :background-repeat     "no-repeat"
           :background-attachment "fixed"
           :background-size       "cover"}]
   [:#titlebar {:background-color color-p-main
                :color            color-off-bright
                :padding          "0"
                :margin           0
                :text-align       "center"
                :position         :fixed
                :top              0
                :width            :100%
                :height           title-bar-height
                :box-shadow       navshadow}
    [:h1 {:color       "inherit"
          :text-shadow (str "1px 1px 1px " (gc/as-hex color-p-light))}]]
   [:#menubar (-> {:position         :fixed
                   :z-index          100
                   :bottom           :-45px
                   :right            :-45px
                   :background-color color-p-dark
                   :width            :100px
                   :height           :100px
                   :border-radius    :200px
                   :box-shadow       navshadow}
                  (into (prefix-it :transition "height .07s")))
    [:* {:color :transparent}]]
   [:#menubar:hover {:background-color (gc/darken color-p-dark 5)
                     :padding          "0px"
                     :padding-right    "60px"
                     :width            :160px
                     :height           :210px
                     :border-radius    :0
                     :bottom           0
                     :right            0
                     }
    [:ul {:background-color color-p-dark
          :margin           0
          :padding          :10px
          :height           :100%}
     [:* {:list-style :none}]
     [:li [:h3 {:padding-bottom :8px}
           [:a {:color color-off-bright}]]
      [:ul [:li {:margin-top :-8px}
            [:a {:color color-brightest}]]]]]]
   [:#data-view-body {:position :fixed
                      :top      title-bar-height
                      :width    :100%
                      :height   (calchelper :100% - title-bar-height)
                      :overflow :scroll}]
   [:.pagesection {:padding          "10px"
                   :margin           "20px"
                   :margin-right     :10px
                   :background-color color-brightest
                   :display          :block
                   :float            :left
                   :width            (calchelper :100% - :60px)
                   ;:max-height       :200px
                   :box-shadow       elementshadow
                   }
    [:ul [:li {:list-style "none"}]]
    [:.banner-image {:max-width :100%}]
    [:.profile-image {:max-width  :100px
                      :max-height :250px
                      :float      :right}]
    [:.char-banner-title {:float :left}]
    ]
   [:.field {:width :100%}
    [:label {:width         :70px,
             :display       :inline-block,
             :text-align    :right,
             :padding-right :5px}]
    [:.entry {:min-width (calchelper :100% - :100px - :70px - :15px)
              :max-width (calchelper :100% - :70px)
              :height    :30px,
              :display   :inline}
     [:.spinner {:height  "inherit",
                 :display :inline}]
     [:.dotpart {:display :inline}]]]

   ])

(defn add-generated-statement [csser]
  (str "/*This file is automatically generated. Any changes made will be overwritten by dev/gardener.clj*/\n\n\n" csser))

(defn compile-style! []
  (spit "resources/public/css/style.css"
        (->> mobilestyle
             (map g/css)
             (reduce (fn [a b] (str a "\n\n" b)))
             add-generated-statement)))

(compile-style!)