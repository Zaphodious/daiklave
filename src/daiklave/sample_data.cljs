(ns daiklave.sample-data)

(comment "https://imgur.com/gallery/sZkQj")

(def sample-state

  {
   "424242"    {:name          "Rajmael",
                :subtitle      "The Starfire Wing Scholar"
                :concept       "Sorcerer Philosopher King0"
                :anima         "The Five Truths of Wu Du Xun"
                :player        "Gavyn",
                :key           "424242",
                :img           "https://i.imgur.com/QcFrj4t.jpg"
                :category      :character,
                :type          :solar,
                :subtype       :twilight,
                :chron         "242424",
                :last-accessed 4535361
                :attributes    {:strength 2, :dexterity 4, :stamina 2, :charisma 3, :manipulation 3, :appearance 3, :perception 2, :intelligence 5, :wits 4}}
               ,

   "452452452" {:name          "Seas of Doom",
                :category      :chron,
                :type          :solar,
                :subtype       :adventure
                :key           "452452452",
                :img           "https://i.imgur.com/xLYsQ2j.jpg"
                :description   "Rust Eyes brings a bunch of shini bois together",
                :last-accessed 1865961,
                :merits [{:name "Allies"}]}


   "963963963" {:name          "Alkaia",
                :subtitle      "Of the Lykopis"
                :concept       "Barbarian Adventuress"
                :anima         "A huge bear"
                :player        "Victoria Ham",
                :key           "963963963",
                :img           "https://i.imgur.com/no8PesX.jpg"
                :category      :character,
                :type          :solar,
                :subtype       :dawn,
                :chron         "452452452",
                :last-accessed 0
                :attributes    {:strength 4, :dexterity 4, :stamina 3, :charisma 3, :manipulation 3, :appearance 3, :perception 3, :intelligence 3, :wits 3}}
               ,

   "789789789" {:name          "Daesh",
                :subtitle      "Fastest in the Land"
                :concept       "The Best Athlete evar"
                :anima         "The Great Race"
                :player        "Alex",
                :key           "789789789",
                :category      :character,
                :img           "https://i.imgur.com/wYnw5RQ.jpg"
                :type          :solar,
                :subtype       :zenith,
                :chron         "452452452",
                :last-accessed 987253
                :attributes    {:strength 4, :dexterity 4, :stamina 3, :charisma 3, :manipulation 2, :appearance 2, :perception 3, :intelligence 3, :wits 3}}
               ,

   "242424"    {:name          "Under Heaven's Eye",
                :category      :chron,
                :type          :active,
                :subtype       :adventure
                :key           "242424",
                :img           "https://i.imgur.com/yzGVjcX.jpg"
                :description   "When the sidereals take notice, the world bends...",
                :last-accessed 1438418}

               ,})
