(ns daiklave.default-data)

(def chron
  {:name          "Exalted Core",
   :category      :chron,
   :type          :solar,
   :subtype       :adventure
   :key           "0",
   :img           "https://i.imgur.com/59mlT7v.jpg"
   :description   "Published by Onyx Path",
   :merits {:category :merits
            :name "Merits"
            :img "https://i.imgur.com/7nhJT8U.png"
            :description "Customization, in bite-sized-form"
            :merit-vec
            [{:name "Allies"
              :description "Allies, yo!"
              :drawback "Dey be people n shiii"
              :page 158
              :ranks #{1 3 5}
              :repurchasable true
              :type :story}
             {:name "Ambidextrous"
              :description "Look, ma! Two hands!"
              :drawback ""
              :page 158
              :ranks #{1,2}
              :type :innate
              :repurchasable false}
             {:name "Artifact"
              :description "Mah Boomstick!"
              :drawback ""
              :page 159
              :ranks #{2 3 4 5}
              :repurchasable true
              :type :story}]}
   :charms {:name "Charms"
            :category :charms
            :type :solar
            :subtype :charmlist
            :img "https://i.imgur.com/Xo9GgHm.png"
            :archery [{:name "Wise Arrow"
                       :cost "1m"
                       :min-essence 1
                       :min-ability 2
                       :category :charm
                       :ability :archery
                       :page 255
                       :keywords "Uniform"
                       :type :supplemental
                       :duration "Instant"
                       :prereq-charms "None"
                       :description "Fire an arrow which knows which way is up"}],
            :athletics [],
            :awareness [],
            :brawl [],
            :bureaucracy [],
            :craft [],
            :dodge [],
            :integrity [],
            :investigation [],
            :larceny [],
            :linguistics [],
            :lore [],
            :martial-arts [],
            :medicine [],
            :melee [],
            :occult [],
            :performance [],
            :presence [],
            :resistance [],
            :ride [],
            :sail [],
            :socialize [],
            :stealth [],
            :survival [],
            :thrown [],
            :war []}})
