(ns daiklave.default-data
  (:require [daiklave.corebooks.charms :as charms]
            [daiklave.corebooks.merits :refer [merit-vec]]
            [clojure.string :as str]))

(defn- charm-sec [ability img charms]
  {:name (str/capitalize (name ability))
   :image img
   :category :charms
   :charms charms})

(def core-book
  {:name                "Exalted Core",
   :category            :rulebook,
   :type                :solar,
   :subtype             :adventure
   :storyteller         "John Mørke"
   :key                 "0",
   :img                 "https://i.imgur.com/59mlT7v.jpg"
   :description         "Published by Onyx Path",
   :merits              {:category    :merits
                         :name        "Merits"
                         :img         "https://i.imgur.com/7nhJT8U.png"
                         :description "Customization, in bite-sized-form"
                         :merit-vec   merit-vec}

   :charms              {:name          "Charms"
                         :category      :charms
                         :type          :solar
                         :subtype       :charmlist
                         :img           "https://i.imgur.com/Xo9GgHm.png"
                         :archery       charms/archery-charms,
                         :athletics     charms/athletics-charms,
                         :awareness     charms/awareness-charms,
                         :brawl         charms/brawl-charms,
                         :bureaucracy   charms/bureaucracy-charms,
                         :craft         [],
                         :dodge         [],
                         :integrity     [],
                         :investigation [],
                         :larceny       [],
                         :linguistics   [],
                         :lore          [],
                         :martial-arts  [],
                         :medicine      [],
                         :melee         [],
                         :occult        [],
                         :performance   [],
                         :presence      [],
                         :resistance    [],
                         :ride          [],
                         :sail          [],
                         :socialize     [],
                         :stealth       [],
                         :survival      [],
                         :thrown        [],
                         :war           []}
   :mundane-weapons     {:name        "Mundane Weapons"
                         :description "Weapons what the normies use"
                         :category    :mundane-weapons
                         :type        :mortal
                         :subtype     :weapon-list
                         :weapons-vec [{:name        "Cestus"
                                        :description "Bronze age brass knucks"
                                        :tags        "Bashing, Brawl, Smashing, Worn"
                                        :category    :light
                                        :cost        1}
                                       {:name        "Baton"
                                        :description "Bit of a club, eh?"
                                        :tags        "Bashing, Melee, Smashing; sometimes Thrown (Short), Improvised"
                                        :category    :light
                                        :cost        0}
                                       {:name        "Hook Sword"
                                        :description "Fishing swords yall"
                                        :tags        "Lethal, Martial Arts, Disarming"
                                        :category    :medium
                                        :cost        3}
                                       {:name        "Lance"
                                        :description "Stiiiiiiiiiiick with a pointy end"
                                        :tags        "Lethal, Melee, Piercing, Reaching; Two-Handed when on foot"
                                        :category    :heavy
                                        :cost        1}]}
   :martial-arts-styles {:name             "Martial Arts"
                         :category         :martial-arts-styles
                         :description      "Powerful displays of perfect combat"
                         :type             :mortal
                         :subtype          :martial-arts
                         :martial-arts-vec [{:name        "Black Claw Style"
                                             :category    :martial-arts-style
                                             :description "Black Claw style is one of the few martial arts created by a demon, taught to the Exalted in the First Age by Mara, the Shadow-Lover."
                                             :type        :martial-arts-style
                                             :subtype     :martial-arts
                                             :style-info  {:weapons   "This style uses exclusively unarmed attacks, emphasizing claw strikes and sudden, lunging kicks."
                                                           :armor     "Black Claw style is incompatible with armor."
                                                           :abilities "Black Claw stylists often feign the appearance of fighting on the defensive, using Dodge to both evade attacks and disengage from close combat. Presence is also useful to them, as many of their Charms allow them to sway the hearts and minds of enemies and bystanders alike in combat."}
                                             :charms      [{:name          "Open Palm Caress"
                                                            :cost          "4m"
                                                            :min-essence   1
                                                            :min-ability   2
                                                            :type          :supplemental
                                                            :keywords      "Mastery"
                                                            :duration      "Instant"
                                                            :prereq-charms "None"
                                                            :description   "From the very beginning, things start to go wrong. Righteous heroes find themselves cast as vicious bullies when they fight a student of the Black Claw—even when she starts the fight. Open Palm Caress can be used whenever the martial artist rolls Join Battle. As long as at least one enemy received more successes on the roll than the martial artist did, he and his allies are seen as having initiated hostilities, regardless of how the fight actually began. This applies both to any bystanders to the fight and to the enemies themselves, who might suddenly find themselves confused as to why they are attacking the Black Claw stylist. Characters may see through this deception with a reflexive read intentions roll against the martial artist’s Guile. The Black Claw stylist gains a single point of Initiative for each opponent or bystander who was fooled by this ruse, up to a maximum of her Manipulation. \n\n Mastery: The martial artist’s performance is so convincing that if she uses her first turn to make a clinch or decisive attack against an enemy who beat her Join Battle and failed to overcome her Guile, she may pay a point of Willpower to treat that attack as an ambush."}]}]}})


(def miracles
  {:name        "Miracles of the Solar Exalted",
   :category    :rulebook,
   :type        :solar,
   :subtype     :adventure
   :storyteller "John Mørke"
   :key         "1",
   :img         "https://imgur.com/4EqEWzB.jpg"
   :description "Published by Onyx Path",
   :charms      {:name          "Charms"
                         :category      :charms
                         :type          :solar
                         :subtype       :charmlist
                         :img           "https://i.imgur.com/Xo9GgHm.png"
                         :archery       []
                         :athletics     charms/athletics-miracles,
                         :awareness     [],
                         :brawl         [],
                         :bureaucracy   [],
                         :craft         [],
                         :dodge         [],
                         :integrity     [],
                         :investigation [],
                         :larceny       [],
                         :linguistics   [],
                         :lore          [],
                         :martial-arts  [],
                         :medicine      [],
                         :melee         [],
                         :occult        [],
                         :performance   [],
                         :presence      [],
                         :resistance    [],
                         :ride          [],
                         :sail          [],
                         :socialize     [],
                         :stealth       [],
                         :survival      [],
                         :thrown        [],
                         :war           []}})