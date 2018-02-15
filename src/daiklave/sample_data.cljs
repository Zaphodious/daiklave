(ns daiklave.sample-data)

(comment "https://imgur.com/gallery/sZkQj")

(def sample-state

  {:home       {:app-home true
                :name     "Anathema: Reincarnated"
                :category :home
                :img      "img/app-symbol.png"}
   :modal      {:modal-showing   :none
                :modal-arguments {}
                :selected        "0"
                :query           ""}
   :settings   {:name               "Settings"
                :category           :settings
                :img                "https://i.imgur.com/ffipFnl.jpg"
                :description        "Options that effect Anathema's behavior"
                :show-accessibility false
                :show-unused        false}

   :characters {:name        "Characters"
                :category    :characters
                :img         "https://i.imgur.com/Cbmme8c.jpg"
                :description "The Heroes That Move The World"
                "424242"     {:name                 "Rajmael",
                              :description          "Sorcerer Philosopher King"
                              :long-description     "The Starfire Wing Scholar"
                              :anima                "The Five Truths of Wu Du Xun"
                              :player               "Gavyn",
                              :key                  "424242",
                              :img                  "https://i.imgur.com/cFE1bow.jpg"
                              :category             :character,
                              :type                 :solar,
                              :rulebooks            ["0", "242424"],
                              :limit                {:trigger "When something I've done is proven wrong"
                                                     :accrued 3}
                              :health-module        {:levels     [4 1 4 5]
                                                     :bashing    2
                                                     :lethal     1
                                                     :aggravated 3}
                              :willpower            {:temporary 2 :max 9},
                              :crafting             [{:rating 3, :description ""}]
                              :subtype              :twilight,
                              :last-accessed        4535361
                              :attributes           {:strength 2, :dexterity 4, :stamina 2, :charisma 3, :manipulation 3, :appearance 3, :perception 2, :intelligence 5, :wits 4}
                              :abilities            {:athletics 2, :awareness 3, :brawl 1, :bureaucracy 2, :dodge 2, :integrity 3, :linguistics 3, :lore 5, :medicine 3, :socialize 3}
                              :abilities-additional [[:martial-arts, 3, "Single Point Shining Into The Void"]]
                              :favored-abilities    [:awareness, :brawl, :bureaucracy, :dodge, :integrity, :linguistics, :lore, :occult, :socialize, :martial-arts]
                              :supernal             :lore
                              :xp                   {:spent  55
                                                     :wallet 10
                                                     :solar  0
                                                     :silver 3
                                                     :gold   2
                                                     :white  1}
                              :essence              {:rating                     1
                                                     :motes-spent-personal       4
                                                     :motes-spent-peripheral     2
                                                     :motes-committed-peripheral 2
                                                     :motes-committed-personal   0}
                              :specialties          [[:athletics "Acrobatics"]
                                                     [:integrity "Resisting Bribery"]
                                                     [:socialize "Read Intentions"]
                                                     [:awareness "Indiscretions"]
                                                     [:lore "Desert Legends"]
                                                     [:lore "Demonology"]]
                              :merits               [{:name ""}]
                              :intimacies           [{:type :principle, :severity :defining, :feeling "", :description "The Five Truths"}
                                                     {:type :principle, :severity :major, :feeling "", :description "There is no pleasure like a bright pupil"}
                                                     {:type :tie, :severity :major, :feeling "Respect and Fear", :description "The Scarlet Empress"}
                                                     {:type :tie, :severity :minor, :feeling "Gratitude", :description "Amon the Demon's Head"}
                                                     {:type :tie, :severity :minor, :feeling "Profound Discomfort", :description "The Owl Screaming In Torment"}]}

                "963963963"  {:name                 "Alkaia",
                              :long-description     "Of the Lykopis"
                              :description          "Barbarian Adventuress"
                              :anima                "A huge bear"
                              :player               "Victoria Ham",
                              :key                  "963963963",
                              :img                  "https://i.imgur.com/no8PesX.jpg"
                              :category             :character,
                              :type                 :solar,
                              :rulebooks            ["0", "242424"],
                              :limit                {:trigger "To be made utterly helpless"
                                                     :accrued 1}
                              :health-module        {:levels     [3 1 4 5]
                                                     :bashing    2
                                                     :lethal     1
                                                     :aggravated 3}
                              :willpower            {:temporary 2 :max 4}
                              :subtype              :dawn,
                              :last-accessed        0
                              :attributes           {:strength 4, :dexterity 4, :stamina 3, :charisma 3, :manipulation 3, :appearance 3, :perception 3, :intelligence 3, :wits 3}
                              :abilities            {:archery 3, :athletics 4, :brawl 3, :integrity 3, :larceny 3, :melee 5, :resistance 3, :ride 1, :sail 4, :survival 3}
                              :abilities-additional [[:craft, 2, "Bladewright"]]
                              :favored-abilities    [:archery, :athletics, :brawl, :craft, :integrity, :larceny, :melee, :resistance, :sail, :survival]
                              :supernal             :melee
                              :xp                   {:spent  55
                                                     :wallet 10
                                                     :solar  0
                                                     :silver 3
                                                     :gold   2
                                                     :white  1}
                              :essence              {:rating                     1
                                                     :motes-spent-personal       4
                                                     :motes-spent-peripheral     2
                                                     :motes-committed-peripheral 2
                                                     :motes-committed-personal   0}
                              :specialties          [[:sail "Captiancy"]
                                                     [:melee "Swords"]
                                                     [:survival "Tracking"]
                                                     [:larceny "Lockpicking"]]
                              :intimacies           [{:type :principle, :severity :defining, :feeling "", :description "I am the strongest"}
                                                     {:type :tie, :severity :major, :feeling "Thrill", :description "The wind at my sails"}
                                                     {:type :tie, :severity :minor, :feeling "Disgust", :description "City folk"}
                                                     {:type :tie, :severity :minor, :feeling "Distrust", :description "Rajmael"}]}

                "789789789"  {:name              "Daesh",
                              :description       "The Greastest Olympian"
                              :long-description  "The Best Athlete in the world"
                              :anima             "The Great Race"
                              :player            "Alex",
                              :key               "789789789",
                              :category          :character,
                              :img               "https://i.imgur.com/wYnw5RQ.jpg"
                              :type              :solar,
                              :rulebooks         ["0", "452452452"],
                              :limit             {:trigger "Being delt with in bad faith"
                                                  :accrued 7}
                              :subtype           :zenith,
                              :health-module     {:levels     [3 1 4 5]
                                                  :bashing    2
                                                  :lethal     1
                                                  :aggravated 3}
                              :willpower         {:temporary 2 :max 7}
                              :last-accessed     987253
                              :attributes        {:strength   4, :dexterity 4, :stamina 3,
                                                  :charisma   3, :manipulation 2, :appearance 2,
                                                  :perception 3, :intelligence 3, :wits 3}
                              :abilities         {:athletics 5, :awareness 3, :brawl 3, :integrity 2, :linguistics 1, :lore 2, :occult 2, :performance 3, :presence 3, :resistance 3, :thrown 2}
                              :favored-abilities [:athletics, :brawl, :integrity, :lore, :occult, :performance, :presence, :resistance, :thrown, :awareness]
                              :supernal          :athletics
                              :xp                {:spent  55
                                                  :wallet 10
                                                  :solar  0
                                                  :silver 3
                                                  :gold   2
                                                  :white  1}
                              :essence           {:rating                     1
                                                  :motes-spent-personal       4
                                                  :motes-spent-peripheral     2
                                                  :motes-committed-peripheral 2
                                                  :motes-committed-personal   0}
                              :specialties       [[:athletics "Racing"]
                                                  [:performance "Oratory"]
                                                  [:linguistics "Holy Solar Texts"]
                                                  [:awareness "Suprise Attacks"]]
                              :intimacies        [{:type :principle, :severity :defining, :feeling "", :description "He who can, should."}
                                                  {:type :principle, :severity :major, :feeling "", :description "The starting line is straight"}
                                                  {:type :principle, :severity :major, :feeling "", :description "I've Earned My Liberty"}
                                                  {:type :tie, :severity :minor, :feeling "Rush", :description "The wind as I run is the voice of my mother"}
                                                  {:type :tie, :severity :minor, :feeling "Distaste", :description "The cheater's victory of the bane of my father"}]}}

   :rulebooks  {:name        "Rulebooks"
                :category    :rulebooks
                :img         "https://i.imgur.com/f1vYAW7.jpg"
                :description "The Stories That Shape Creation"

                "452452452"  {:name          "Seas of Doom",
                              :category      :rulebook,
                              :type          :solar,
                              :subtype       :adventure
                              :storyteller   "Gavyn"
                              :key           "452452452",
                              :img           "https://i.imgur.com/xLYsQ2j.jpg"
                              :description   "Rust Eyes brings a bunch of shini bois together",
                              :last-accessed 1865961}

                "242424"     {:name          "Under Heaven's Eye",
                              :category      :rulebook,
                              :type          :active,
                              :subtype       :adventure
                              :storyteller   "Alex"
                              :key           "242424",
                              :img           "https://i.imgur.com/yzGVjcX.jpg"
                              :description   "When the sidereals take notice, the world bends...",
                              :last-accessed 1438418}
                "852852852"  {:name          "Under Heavenly Light",
                              :category      :rulebook,
                              :type          :active,
                              :subtype       :adventure
                              :storyteller   "Vexx0r"
                              :key           "852852852",
                              :img           "https://i.imgur.com/yzGVjcX.jpg"
                              :description   "When the sidereals take notice, the world bends...",
                              :last-accessed 1438418}
                "01010101"   {:name          "Under Heavy Burdens",
                              :category      :rulebook,
                              :type          :active,
                              :subtype       :adventure
                              :storyteller   "Deekorz"
                              :key           "01010101",
                              :img           "https://i.imgur.com/yzGVjcX.jpg"
                              :description   "When the sidereals take notice, the world bends...",
                              :last-accessed 1438418}}})


