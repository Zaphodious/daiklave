(ns gardener
  (:require [garden.core :as g]
            [garden.def :as gd]
            [garden.media :as gm]
            [garden.color :as gc]
            [garden.arithmetic :as ga]
            [garden.selectors :as gs]
            [clojure.string :as str]
            [clojure.set :as set]
            [garden.stylesheet :as gss :refer [at-media]]
            [garden.types :as gt]))

(gd/defcssfn url)
(gd/defcssfn blur)
(gd/defcssfn calc)

(defn supports [support-statement garden-seq]
  (fn [previous-css] (str previous-css "\n\n\n" "@Supports (" support-statement ") {\n     " (g/css garden-seq) "}")))

(defn grid-area-strings [& stringers]
  (reduce str
    (map
      (fn [a] (str "\n\"" a "\""))
      stringers)))

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
             :border-radius (-px 2)
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
                              :padding-right    0
                              :vertical-align :bottom}
    ;:margin-left (-px 3)}
    [:&:focus {:outline          :none
               :box-shadow       :none
               :border-width     :3px
               :border-bottom    :double
               :margin-bottom    (-px 1)
               :background-color color-off-bright}]]

   ;:box-shadow   focusshadowtext}]]
   ;:border-width :3px}]]
   ;:border-radius (-px 5)}]
   ["input[type=checkbox]" {:height :20px
                            :width :20px}
       [:&:focus {:height :30px}]]
   [:html {:height :100%}]
   [:body {:background-color color-off-bright
           :height           :100%
           :overflow :hide}]
   [:#app {:width :100% :height :100% :overflow :hide}]
   [:#app-frame {:position   :fixed
                 :overflow-y :hidden
                 :overflow-x :auto
                 :top        0
                 :left       0
                 :width      :100%
                 :height     :100%}]
   [:.minimized-field {:transition "opacity .5s"}]
   [:.page.minimized [:* [:.minimized-field {:opacity 0
                                             :display :none}]]]
   [:.page.maximized [:* [:.minimized-field {:opacity 1
                                             :display :inherit}]]]
   [:.page {:width  :100%
            :height :100%
            :position :relative}
    [:h1.page-title {:width            :100%
                     :background-color color-p-main
                     :color            color-off-bright
                     :text-shadow      title-text-shadow
                     :text-align       :center
                     :font-weight      :bolder
                     :box-shadow       elementshadow
                     :position :relative
                     :z-index          10}]

    [:.menu-assembly {:position :absolute
                      :width :100%}
     [:button.menu-toggle {:position :absolute
                           :right :20px
                           :top :-28px
                           :z-index 20}]
     [:.page-menu {:background-color color-p-light
                     :position :absolute

                     :z-index 9
                     :box-shadow elementshadow
                     :transition "top .5s, opacity .5s"
                     :width (calchelper :100% - :40px)
                     :padding :5px}
      [:ul
       [:li {:display :inline-block
             :padding-right :5px}]]]

     [:.menu-showing {:top :0px
                        :left :13px
                      :opacity 1}]
     [:.menu-hidden {;:position :fixed
                       :opacity 0
                       :top :-70px
                       :left :13px}]]
                       ;:right :0px}]]
    [:.page-content {:height   (calchelper :100vh - title-bar-height - :10px)
                     :overflow-y :auto
                     :overflow-x :hidden}
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


                ;:height (calchelper :100% - :40px)}


   [:form
    [:* {:padding :.5em}]
    [:p
     [:label {:width      (calchelper :20% - :10px)
              :height (-% 100)
              :display    :inline-block
              :text-align :right}]
     [:.field {:width   (calchelper :100% - :20% + :10px - :10px - :2em - page-content-margin - page-content-margin)
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
     [:.number-field
      [:input {:width (calchelper :100% - :80px)}]
      [:button {:width :20px}]]
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
   [:span.rank-selection {:width   :1.5em
                          :height :1.5em
                          :padding 0
                          :margin  0
                          ;:margin-top :1em
                          :display :inline-block}
                         [:&:focus-within
                          [:span.select-helper {:display          :inline-block
                                                :float            :right
                                                :opacity :0.5
                                                :background-color (gc/darken color-off-bright 30)
                                                :height           :20px
                                                :width            :10px
                                                :padding          :0px
                                                :margin           0
                                                :position         :relative
                                                :bottom           :24px
                                                :right            :8px
                                                :border-radius    :10px
                                                :z-index 2}]]
    [:span.select-helper {:display :none}]

    [:input {:display :inline
             :margin 0
             :opacity 0
             :width :15px
             :height :15px
             :position :relative
             :z-index 10
             :right :0px
             :bottom :7px
             ;:margin-bottom :-30px}]
             ;:width :1px}]
             :margin-left :-18px}]

    [:label {:width   :10px
             :height :10px
             :padding :5px
             :padding-bottom :12px
             :text-align :center
             :display :inline-block
             :border-radius :10px
             :z-index 6
             :position :relative}]

    [:.checked {:border :solid
                :border-width :1px}]]
   [:.balanced-number-field {:width :110%;:max-width :200px}
                             ;:border :solid
                             :margin-right :-10px}
    [:input {:width :15px}]
    [:label {:font-size :0.5em
             :padding :0}]
    [:button {:width :15px
              :padding 0}]]
   [:ul.health-track  {:padding 0
                       :text-align :justify}
    [:li.health-box {:display :inline-block
                     :padding 0
                     :margin :2px
                     :box-shadow elementshadow
                     :min-width :28px
                     :min-height :28px
                     :max-width :100px
                     :max-height :100px
                     :width :36px
                     :height :36px
                     :border :outset
                     :border-width :1px
                     :border-radius :10px
                     :background-color :white
                     :vertical-align :center
                     ;:background-image (url "../img/damage_blank.png")
                     :background-size :70%
                     :background-repeat :no-repeat
                     :background-position "right bottom"}]
    [:li.bashing-damage {:border-color :blue
                         :background-image (url "../img/quick-slash.png")}]
    [:li.lethal-damage {:border-color :red
                        :background-image (url "../img/perpendicular-rings.png")}]

    [:li.aggravated-damage {:border-color :green
                            :background-image (url "../img/spikes-half.png")}]]
   [:button.bashing {:border-color :blue}]
   [:button.lethal {:border-color :red}]
   [:button.aggravated {:border-color :green}]])

(def desktop-style
  [[:table.page-list {:display :block}
    ;:overflow :scroll
    [:tbody {;:display :block
             :height (calchelper :100% - :50px)}
     [:tr
      [:td {};:display :inline

       [:.page {:width :400px
                :display :block}]
       [:.character-page {:width :1000px}]
       [:.merits-page :.charms-list-page {:width :700px}]]]]]])
        ;[:h1.page-title {:width :900px}]]]]]]])
   ;[(gt/->CSSAtRule "Supports" {:grid-template-areas "\"...\""})]]);"@Supports (grid-template-areas: \"...\")"


(def character-page-desktop-style
  (supports "grid-template-areas: \"...\""
            [:table
             [:.character-page
              [:.page-content {:display               :grid
                               :grid-template-columns "1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr"
                               :grid-template-areas (grid-area-strings
                                                      "head head core core core core core core core"
                                                      "head head .... .... .... .... .... .... ...."
                                                      "attr attr attr attr attr attr attr attr attr"
                                                      "abil abil abil will will will favo favo favo"
                                                      "abil abil abil essi essi essi favo favo favo"
                                                      "abil abil abil essi essi essi xpxp xpxp xpxp"
                                                      "abil abil abil essi essi essi xpxp xpxp xpxp"
                                                      "abil abil abil .... .... .... xpxp xpxp xpxp"
                                                      "abil abil abil .... .... .... .... .... ...."
                                                      ".... .... .... .... .... .... .... .... ...."
                                                      "heal heal heal heal heal heal heal heal heal"
                                                      ".... .... .... .... .... .... .... .... ...."
                                                      "spec spec spec inti inti inti inti inti inti"
                                                      "spec spec spec inti inti inti inti inti inti"
                                                      ".... .... .... inti inti inti inti inti inti")}


               [:.page-header {:grid-area "head"}]
               [:.coreinfo {:grid-area "core"}
                [:form {:column-count 2}]]
               [:.attributeinfo {:grid-area "attr"}
                [:form {:column-count 3}]]
               [:.abilityinfo {:grid-area "abil"}]
               [:.attributeinfo :.abilityinfo
                [:form
                 [:p {;:border :solid
                      :padding-left :20px
                      :padding-right 0}
                  [:label {:width :20%}]
                  [:p {:float :right
                       :margin-right :-30px
                       :padding :7px}]]]]
               [:.favoredabilities {:grid-area "favo"}]
               [:.specialtyinfo {:grid-area "spec"}]
               [:.health-track-module {:grid-area "heal"}
                [:.button-bar {:display :inline-block}]]
               [:.intimacyinfo {:grid-area "inti"}]
               [:.essence-module {:grid-area "essi"}
                [:form {:column-count 1}]]
               [:.willpower-module {:grid-area "will"}]
               [:.experience-module {:grid-area "xpxp"}]]]]))



(defn add-generated-statement [csser]
  (str "/*This file is automatically generated. Any changes made will be overwritten by dev/gardener.clj*/\n\n\n" csser))



(defn compile-style! []
  (spit "resources/public/css/style.css"
        (->> (into mobilestyle desktop-style)
             (map g/css)
             (reduce (fn [a b] (str a "\n\n" b)))
             character-page-desktop-style
             add-generated-statement)))

(compile-style!)