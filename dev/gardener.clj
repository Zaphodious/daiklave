(ns gardener
  (:require [garden.core :as g]
            [garden.def :as gd]
            [garden.media :as gm]
            [garden.color :as gc]
            [garden.arithmetic :as ga]
            [garden.selectors :as gs]
            [clojure.string :as str]
            [clojure.set :as set]))

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

(defn unit-fn-for [unittype]
  (fn [n] (str (double n) unittype)))

(def -px (unit-fn-for "px"))
(def -em (unit-fn-for "em"))
(def -% (unit-fn-for "%"))

(defn quoth [n]
  (str "/'" n "/'"))

(def colors
  {:titlebar-back (gc/rgba 0, 0, 0, 0.64)
   :titlebar-text (gc/rgba 1, 1, 1, 1)})

(def color-p-main (gc/hex->rgb "#795548"))
(def color-p-light (gc/hex->rgb "#a98274"))
(def color-p-lighter (gc/lighten color-p-light 10))
(def color-p-dark (gc/hex->rgb "#4b2c20"))
(def color-p-darker (gc/darken color-p-dark 0.5))
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
(def buttonshadow (str "0 1px 2px" (gc/as-hex color-p-dark)))
(def focusshadow (str "0 3px 10px" (gc/as-hex color-p-dark)))
(def focusshadowtext (str "0 5px 10px" (gc/as-hex color-p-dark)))

(def title-text-shadow (str "0 0 6px" (gc/as-hex color-p-dark)))

(def page-content-margin-scalar 7)
(def page-content-margin (keyword (str page-content-margin-scalar "px")))
(def standard-field-width (calchelper :100% - :80px - :10px - :2em - page-content-margin - page-content-margin))

(def mobilestyle
  [[:.helper-dl-link {:position :absolute
                      :top :8px
                      :left :10px
                      :color color-brightest
                      :border :solid
                      :border-width :1px}]
   [:&:focus {:outline-style  :solid
              :outline-width  :1px
              :outline-color  (assoc (gc/as-rgb color-p-lighter) :alpha 0.5)
              :outline-offset :0px
              :outline-radius :5px
              :box-shadow     focusshadow
              :z-index        100}]

   [:* {:margin      0
        :font-family "Bellefair, sans-serif"
        :color       color-off-dark}]
   [:button {:background-color color-brightest
             :border-style     :solid
             :border-width     :1px
             :border-color     :none
             :box-shadow       buttonshadow
             :text-shadow      (str "0 0 6px" (gc/as-hex color-p-light))
             ;:border-radius (-px 5)
             :color            color-darkest
             :margin-right     :2px
             :margin-left      :2px}]

   ;:border-radius (-px 5)}]
   [:input :select :textarea {:background-color color-brightest
                              :border-bottom    :solid
                              :border-width     :1px
                              :border-color     :grey
                              :border-left      :none
                              :border-right     :none
                              :border-top       :none
                              :height           (-% 75)
                              :margin-bottom    (-px 3)
                              :margin-left      (-px 5)
                              :padding-right    0}
    ;:margin-left (-px 3)}
    [:&:focus {:outline       :none
               :box-shadow    :none
               :border-width  :3px
               :margin-bottom (-px 1)}]]
   ;:box-shadow   focusshadowtext}]]
   ;:border-width :3px}]]
   ;:border-radius (-px 5)}]
   ["input[type=checkbox]" {:height :20px
                            :width :20px}]
   [:body {:background-color color-off-bright
           :height           :100%}]
   [:#app-frame {:width  :100%
                 :height :100%}]
   [:.page {:width  :100%
            :height :100%}
    [:h1.page-title {:width            :100%
                     :background-color color-p-main
                     :color            color-off-bright
                     :text-shadow      title-text-shadow
                     :text-align       :center
                     :font-weight      :bolder
                     :box-shadow       elementshadow
                     :z-index          10}]
    [:.page-content {:height   (calchelper :100vh - title-bar-height)
                     :overflow :scroll}
     [:.element-button-bar {:background-color :transparent
                            :margin           (-px (* page-content-margin-scalar 2))
                            :margin-bottom    (-px (- (* -1 page-content-margin-scalar 6.6) 1))
                            ;:box-shadow       elementshadow
                            :text-align       :right
                            :padding          (-px 7)}]

     [:.page-section {:background-color color-brightest
                      :margin           (-px (* page-content-margin-scalar 2))
                      :box-shadow       elementshadow
                      :padding          :10px}
      [:img {:max-width :100%}]
      [:img.banner-image {:display    :block
                          :text-align :center
                          :margin     (-px -10)
                          :margin-top (-px 10)
                          :max-width  (calchelper (-% 100) + (-px 20))}]

      [:img.profile-image {:max-width    :50%
                           :display      :inline
                           :margin       (-px 1)
                           :margin-top   (-px 5)
                           :border       :inset
                           :border-width (-px 1)}]
      [:.profile-text {:float      :right
                       :display    :inline
                       :width      (calchelper :50% - (-px 20))
                       :text-align :left
                       :margin     (-px 5)}]
      [:h1 :h2 {:text-align :justify}]
      [:h3 :h4 :h5 :h6 {:text-align       :justify
                        :border-bottom    :solid
                        :border-width     :1px
                        :padding          :4px
                        :padding-left     :15px
                        :padding-top      :7px
                        :margin-top       :-10px
                        :margin-left      :-10px
                        :margin-right     :-10px
                        :background-color color-p-main
                        :color            color-brightest
                        :text-shadow      title-text-shadow}]
      [:.button-bar {:padding :5px}
       [:button {:margin :4px}]]
      [:.navlist-container
       [:.navlist-selected {:tab-index 1}]
       [:ul.field.navlist.hidden {:display :none}]
       [:ul.field.navlist.shown {:display :block}]]]]]

   [:form
    [:* {:padding :.5em}]
    [:p
     [:label {:width      (calchelper :30% - :10px)
              :height (-% 100)
              :display    :inline-block
              :text-align :right}]
     [:.field {:width   (calchelper :100% - :30% + :10px - :10px - :2em - page-content-margin - page-content-margin)
               :display :inline-block
               ;:margin  (-px (/ 2 page-content-margin-scalar))
               :align   :center}
      [:.dot-entry {:width (-em 2.5)}]
      [:.dot-bar {:display :inline-block}]
      [:.inactive-dot :.active-dot {:margin 0, :padding (-px 0)}]
      [:.inactive-dot
       [:&:before {:content "\"⚪\""}]]
      [:.active-dot
       [:&:before {:content "\"⚫\""}]]]
     [:.read-only {:border           0,
                   :background-color color-brightest}]
     [:textarea {:height :3.2em}]]
    [:.mini-label {:display :none}]
    [:.dec-button {:display :inline-block}]
    [:.first-of-three :.second-of-three :.third-of-three {:display :inline-block}]
    [:.first-of-three :.second-of-three {:width (calchelper (-% 26) - :50px)}]
    [:.third-of-three {:width (calchelper (-% 60) - :37px)}]]
   [:form.mini-form {:width   (calchelper (-% 100) - :25px)
                     :display :inline-block}]
   [:.set-selectors
    [:.set-selector {:width (-% 100)}]]
   [:span.rank-selection {:width   :2em
                          :padding 0
                          :margin  0
                          :display :inline-block}
    [:input {:display :inline-block
             :margin 0
             :opacity 0
             :margin-left :-12px}]
    [:label {:width   :10px
             :height :10px
             :padding :5px
             :padding-bottom :12px
             :text-align :center
             :display :inline-block
             :border-radius :10px}]
    [:.checked {:border :solid
                :border-width :1px}]]])




#_[:span.inactive-dot :span.active-dot
   {:width         (-px 10)
    :height        (-px 10)
    ;:margin        (-px 1)
    :margin        (-px 3)
    :margin-top    :auto
    :margin-bottom :auto
    :padding       (-px 0)
    :border-radius (-px 20)
    :border        :solid
    :border-width  :1px
    :display       :inline-block}
   [:span.active-dot {:content          "1"
                      :background-color :white}]
   [:span.inactive-dot {:content          "0"
                        :background-color :black}]]

(def mobilestyle-old
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
           :background-size       "cover"
           :width                 :100%}]

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
                     :right            0}

    [:ul {:background-color color-p-dark
          :margin           0
          :padding          :10px
          :height           :100%}
     [:* {:list-style :none}]
     [:li [:h3 {:padding-bottom :8px}
           [:a {:color color-off-bright}]]
      [:ul [:li {:margin-top :-8px}
            [:a {:color color-brightest}]]]]]]
   [:.page {:width :100%}
    [:.page-title {:background-color color-p-main
                   :color            color-off-bright
                   :padding          "0"
                   :margin           0
                   :text-align       "center"
                   :position         :fixed
                   :top              0
                   :width            :inherit
                   :height           title-bar-height
                   :box-shadow       navshadow}
     [:h1 {;:color       "inherit"
           :text-shadow (str "1px 1px 1px " (gc/as-hex color-p-light))}
      [:* {:color "inherit"}]]]]

   [:.page-content {:position :fixed
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
                   :box-shadow       elementshadow}

    [:ul [:li {:list-style "none"}]]
    [:.banner-image {:max-width :100%}]
    [:.profile-image {:max-width  :100px
                      :max-height :250px
                      :float      :right}]
    [:.char-banner-title {:float :left}]]

   [:.field {:width   :100%
             :padding :5px}
    [:label {:width          :100px,
             :display        :inline-block,
             :vertical-align :top
             :text-align     :right,
             :padding-right  :5px}]
    [:.entry {:min-width (calchelper :100% - :100px - :70px) ;:15px)
              :max-width (calchelper :100% - :70px)
              :height    :2em,
              :display   :inline-block}
     [:.rank-list
      {:height :100%}
      [:li {:float      :left
            :text-align :center
            :height     :inherit}
       [:* {:display :block
            :width   :20px
            :height  :100%}]]]


     [:.spinner {:height  "inherit",
                 :display :inline}]
     [:.dotpart {:display :inline}]]]])



(defn add-generated-statement [csser]
  (str "/*This file is automatically generated. Any changes made will be overwritten by dev/gardener.clj*/\n\n\n" csser))

(defn compile-style! []
  (spit "resources/public/css/style.css"
        (->> mobilestyle
             (map g/css)
             (reduce (fn [a b] (str a "\n\n" b)))
             add-generated-statement)))

(compile-style!)