(ns daiklave.default-data)

(def chron
  {:name          "Exalted Core",
   :category      :chron,
   :type          :solar,
   :subtype       :adventure
   :key           "0",
   :img           "https://i.imgur.com/59mlT7v.jpg"
   :description   "Published by Onyx Path",
   :merits [{:name "Allies"
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
             :type :story}]
   :charms {:name "Charms"
            :category :charms
            :type :solar
            :subtype :charmlist
            :img "https://i.imgur.com/Xo9GgHm.png"
            :athletics []}})
