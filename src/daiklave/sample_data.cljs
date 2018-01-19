(ns daiklave.sample-data)

(comment "https://imgur.com/gallery/sZkQj")

(def sample-state

  {:home       {:app-home true
                :name     "Anathema: Reincarnated"
                :category :home}
   :characters {"424242"    {:name                 "Rajmael",
                             :description          "The Starfire Wing Scholar"
                             :concept              "Sorcerer Philosopher King0"
                             :anima                "The Five Truths of Wu Du Xun"
                             :player               "Gavyn",
                             :key                  "424242",
                             :img                  "https://i.imgur.com/cFE1bow.jpg"
                             :category             :character,
                             :type                 :solar,
                             :chrons ["0","242424"],
                             :subtype              :twilight,
                             :last-accessed        4535361
                             :attributes           {:strength 2, :dexterity 4, :stamina 2, :charisma 3, :manipulation 3, :appearance 3, :perception 2, :intelligence 5, :wits 4}
                             :abilities            {:athletics 2, :awareness 3, :brawl 1, :bureaucracy 2, :dodge 2, :integrity 3, :linguistics 3, :lore 5, :medicine 3, :socialize 3}
                             :abilities-additional [[:martial-arts, 3, "Single Point Shining Into The Void"]]
                             :favored-abilities    #{:awareness, :brawl, :bureaucracy, :dodge, :integrity, :linguistics, :lore, :occult, :socialize, :martial-arts}
                             :supernal             :lore
                             :specialties          [[:athletics "Acrobatics"]
                                                    [:integrity "Resisting Bribery"]
                                                    [:socialize "Read Intentions"]
                                                    [:awareness "Indiscretions"]
                                                    [:lore "Desert Legends"]
                                                    [:lore "Demonology"]]
                             :intimacies           [[:defining "Firm Conviction" "The Five Truths"]
                                                    [:major "Joy" "There is no pleasure like a bright pupil"]
                                                    [:major "Respect and Fear" "The Scarlet Empress"]
                                                    [:minor "Gratitude" "Amon the Demon's Head"]
                                                    [:minor "Profound Discomfort" "The Owl Screaming In Torment"]]}

                "963963963" {:name                 "Alkaia",
                             :description          "Of the Lykopis"
                             :concept              "Barbarian Adventuress"
                             :anima                "A huge bear"
                             :player               "Victoria Ham",
                             :key                  "963963963",
                             :img                  "https://i.imgur.com/no8PesX.jpg"
                             :category             :character,
                             :type                 :solar,
                             :chrons ["0","242424"],
                             :subtype              :dawn,
                             :last-accessed        0
                             :attributes           {:strength 4, :dexterity 4, :stamina 3, :charisma 3, :manipulation 3, :appearance 3, :perception 3, :intelligence 3, :wits 3}
                             :abilities            {:archery 3, :athletics 4, :brawl 3, :integrity 3, :larceny 3, :melee 5, :resistance 3, :ride 1, :sail 4, :survival 3}
                             :abilities-additional [[:craft, 2, "Bladewright"]]
                             :favored-abilities    #{:archery, :athletics, :brawl, :craft, :integrity, :larceny, :melee, :resistance, :sail, :survival}
                             :supernal             :melee
                             :specialties          [[:sail "Captiancy"]
                                                    [:melee "Swords"]
                                                    [:survival "Tracking"]
                                                    [:larceny "Lockpicking"]]
                             :intimacies           [[:defining "Principle" "I am the strongest"]
                                                    [:major "Thrill" "The wind at my sails"]
                                                    [:minor "Disgust" "City folk"]
                                                    [:minor "Distrust" "Rajmael"]]}
                "789789789" {:name              "Daesh",
                             :description       "The Greastest Olympian"
                             :concept           "The Best Athlete in the world"
                             :anima             "The Great Race"
                             :player            "Alex",
                             :key               "789789789",
                             :category          :character,
                             :img               "https://i.imgur.com/wYnw5RQ.jpg"
                             :type              :solar,
                             :chrons ["0","452452452"],
                             :subtype           :zenith,
                             :chron             "452452452",
                             :last-accessed     987253
                             :attributes        {:strength   4, :dexterity 4, :stamina 3,
                                                 :charisma   3, :manipulation 2, :appearance 2,
                                                 :perception 3, :intelligence 3, :wits 3}
                             :abilities         {:athletics 5, :awareness 3, :brawl 3, :integrity 2, :linguistics 1, :lore 2, :occult 2, :performance 3, :presence 3, :resistance 3, :thrown 2}
                             :favored-abilities #{:athletics, :brawl, :integrity, :lore, :occult, :performance, :presence, :resistance, :thrown, :awareness}
                             :supernal          :athletics
                             :specialties       [[:athletics "Racing"]
                                                 [:performance "Oratory"]
                                                 [:linguistics "Holy Solar Texts"]
                                                 [:awareness "Suprise Attacks"]]
                             :intimacies        [[:defining "Principle" "He who can, should."]
                                                 [:major "Principle" "The starting line is straight"]
                                                 [:major "Principle" "I've Earned My Liberty"]
                                                 [:minor "Rush" "The wind as I run is the voice of my mother"]
                                                 [:minor "distaste" "The cheater's victory of the bane of my father"]]}}
   :chrons     {"452452452" {:name          "Seas of Doom",
                             :category      :chron,
                             :type          :solar,
                             :subtype       :adventure
                             :key           "452452452",
                             :img           "https://i.imgur.com/xLYsQ2j.jpg"
                             :description   "Rust Eyes brings a bunch of shini bois together",
                             :last-accessed 1865961,
                             :merits        [{:name "Allies"}]}

                "242424"    {:name          "Under Heaven's Eye",
                             :category      :chron,
                             :type          :active,
                             :subtype       :adventure
                             :key           "242424",
                             :img           "https://i.imgur.com/yzGVjcX.jpg"
                             :description   "When the sidereals take notice, the world bends...",
                             :last-accessed 1438418}}})


