(ns daiklave.default-data)

(def archery-charms [{:description "With skill and effort, the Exalt guides her arrow to its mark. The Exalt may use this Charm to supplement a withering or decisive attack, reducing the benefits of cover. The defense bonus of heavy and light cover is reduced by one, while characters under no cover suffer a -1 penalty to their normal Defense.\n\nIn addition, after taking an aim action, the Solar may activate this Charm to strike an opponent in full cover. The Exalt shoots along an arc or angle that perfectly matches her opponent’s position. So long as there is some opening where an arrow can get through, Wise Arrow treats a character in full cover as if he merely has +3 Defense.", :category :charm, :prereq-charms "None", :ability :archery, :name "Wise Arrow", :type :supplemental, :duration "Instant", :page 255, :keywords "Uniform", :min-ability 2, :cost "1m", :min-essence 1}
                     {:description "The Exalt opens her eyes not to the visual world, but to the world of Essence, sensing her target in that fashion. She may make an Archery attack without penalties for visual conditions. Smoke, fog, and pitch darkness are no longer a problem for her, though other factors such as high winds and cover still apply against the attack. \n\nAt Archery 5+, Essence 3+, the Solar can momentarily see through cover, perceiving her targets as silhouettes the colors of bright anima.", :category :charm, :prereq-charms "Wise Arrow", :ability :archery, :name "Sight Without Eyes", :keywords "None", :type :supplemental, :duration "One tick", :page 255, :min-ability 3, :cost "1m", :min-essence 1}
                     {:description "Drawing upon the perfect moment to shoot, the Solar sees nothing but her target. Upon becoming the beneficiary of a distract gambit (p. 200), if the Solar’s new Initiative would allow her to act immediately, she may use this Charm to fire a ranged decisive attack from up to long range without an aim action. In addition, if the Lawgiver has under seven Initiative, the base damage of this attack gains (Essence) bonus dice.", :category :charm, :prereq-charms "Sight Without Eyes", :ability :archery, :name "Blood Without Balance", :keywords "Decisive-only", :type :supplemental, :duration "Instant", :page 256, :min-ability 4, :cost "3m", :min-essence 1}
                     {:description "The Solar nocks an arrow with purpose, sending a tremulous pulse through her surroundings as she gathers hurricane force into her bowstring. The Solar makes a withering attack from short or close range, and a pulverizing bolt of force surrounds her arrow as it leaps from her bow. If the attack does at least as much damage as her target’s Stamina, that Initiative is lost rather than transferred to the Solar, and the target is knocked down and back an entire range band. This force is sufficient to end a rush against any target.", :category :charm, :prereq-charms "Sight Without Eyes", :ability :archery, :name "Force Without Fire", :keywords "Withering-only", :type :supplemental, :duration "Instant", :page 256, :min-ability 4, :cost "3m", :min-essence 1}
                     {:description "The Solar strums her bow and fills the air with sharp notes. This Charm allows the Exalt to attack multiple targets, or a single target multiple times, by spreading her total current Initiative between decisive attacks. Each attack must contain at least three Initiative, and extra Initiative must be spread as evenly as possible between shots. For example, an Exalt with 11 Initiative attacking three targets could make two decisive attacks with four raw damage, and a third with only three. In addition, each 10 she rolls on an attack increases the base damage of that attack by one. The Exalt’s Initiative does not reset until she has completed every attack, and she may not make more attacks than she has ammunition or Dexterity. This attack can be made without an aim action.", :category :charm, :prereq-charms "Wise Arrow", :ability :archery, :name "Trance Of Unhesitating Speed", :keywords "Decisive-only", :type :supplemental, :duration "Instant", :page 256, :min-ability 3, :cost "3m, 1wp", :min-essence 1}
                     {:description "Palming a mote of Essence, the Lawgiver pulls a thorn from her heart and fires it at the breast of her enemy. With this Charm, the Solar may continue firing her bow even when she has run out of arrows. \n\nIn addition, once per scene, the Exalt may suffuse a phantom or physical arrow with the import of one of her Intimacies. Doing so gives her attack a number of nonCharm bonus dice equal to the Intimacy’s strength, but also temporarily numbs her to that Intimacy. She may not use this attack again until she has spent significant effort in restoring or remembering the Intimacy, or in the case of a negative Intimacy, has been reminded of her motivation for her ire.\n\nAdamant Arrow Technique: At Essence 3+, the Solar may infuse a single phantom or physical arrow per scene with her enduring Essence. So long as she lives, the arrow cannot be destroyed or pulled from the target. Such arrows can be moved by cutting away the base into which they have landed—a tree can still be cut down, a wall still reduced to rubble—but the arrow will remain inviolate. Only the Solar who fired this arrow, or one blessed with her permission, may remove it from its resting place.", :category :charm, :prereq-charms "None", :ability :archery, :name "Phantom Arrow Technique", :keywords "None", :type :supplemental, :duration "Instant", :page 257, :min-ability 3, :cost "1m", :min-essence 1}
                     {:description "Charging her arrow with Essence, the Exalt fires a heavy shot that rends the air as it passes onto her target, lighting the arrow on fire. If the arrow strikes flammable materials, a violent blaze instantly seeks to consume the struck object. This fire is natural, and may spread to surrounding objects or cause combustible materials to explode. When used in a decisive attack against a target, it adds one automatic success to the damage roll. If the attack does at least three health levels of damage, the target catches on fire, and must contend with (Solar’s Essence) lethal dice of damage every turn until he’s able to extinguish himself.", :category :charm, :prereq-charms "Phantom Arrow Technique", :ability :archery, :name "Fiery Arrow Technique", :keywords "Decisive-only", :type :supplemental, :duration "Instant", :page 257, :min-ability 4, :cost "2m", :min-essence 1}
                     {:description "The Solar graces her shot with unerring precision and fires it along a flow of Essence, causing it to slice through the air between the arrow and its mark. Her attack ignores penalties from non-visual conditions such as high winds, bad weather, flawed ammunition, and so on. In addition, her withering attack accuracy is calculated as if it were made from short range regardless of the distance she’s firing from. With appropriate Awareness Charms, the Solar may use this Charm to make attacks from extreme long range.\n", :category :charm, :prereq-charms "Sight Without Eyes", :ability :archery, :name "There Is No Wind", :keywords "Dual", :type :supplemental, :duration "Instant", :page 257, :min-ability 5, :cost "3m", :min-essence 2}
                     {:description "Seething with remonstrative ire, the Solar palms a storm of Essence and fills the sky with demonstrative fire. With this attack, the Solar unleashes a barrage of arrows around a focus, striking up to (Essence * 3) targets up to medium range from her initial target. Roll a single attack against the defenses of every target, and then apply damage.\n\nThese extra decisive attacks carry a base damage of her Perception—divvy up her current Initiative evenly among the remaining shots to determine the total raw damage of each attack. Arrows reaching out to medium range from her initial target are expressly allowed to ignore the range limitations of her weapon without penalty. The Exalt’s Initiative does not reset until every damage roll has been completed.", :prereq-charms "Trance of Unhesitating Speed", :ability :archery, :name "Arrow Storm Technique", :keywords "Decisive-only", :type :simple, :duration "Instant", :page 257, :min-ability 5, :arrow-storm-technique-cost "5m, 1wp", :cost "5m, 1wp", :character-tags [], :min-essence 2}
                     {:description "To invite the wrath of the Lawgiver is to invoke one’s own doom. The Solar gains (Essence) automatic successes to her Join Battle result, and if she acts before her target, her first attack is unblockable. Flashing Vengeance Draw is expressly permitted to be used in combination with Charms that boost Join Battle results, so long as they are not based in Melee, Thrown, or Brawl.", :prereq-charms "Trance Of Unhesitating Speed", :ability :archery, :name "Flashing Vengeance Draw", :keywords "None", :type :supplemental, :duration "Instant", :page 258, :min-ability 5, :flashing-vengeance-draw-cost "3m", :cost "3m", :character-tags [], :min-essence 2}
                     {:description "The Lawgiver suffers no impunities; with terrific speed and ferocious import, she may answer the blades of her transgressors. When the Solar succeeds at a disengage action, she may unleash a withering or decisive Archery attack from close range, even if she has already attacked that turn. This attack must be directed at the one she broke away from.", :prereq-charms "Flashing Vengeance Draw", :ability :archery, :name "Hunter’s Swift Answer", :type :reflexive, :duration "Instant", :page 258, :keywords "Uniform", :min-ability 5, :cost "5m, 1wp", :character-tags [], :min-essence 2}
                     {:description "Honing her skill and focusing her will, the Exalt births a stunning varicolored bow from her palm. This Charm creates a weapon with stats identical to a powerbow, described on page 598. The weapon is made of solidified Essence forged in all the colors of Solar anima, and glows like a torch.\n\nFor additional purchases, players may add custom Evocations to Immaculate Golden Bow. Players should work with their Storyteller to create Evocations that fit the char\nacter’s personality or iconic anima manifestation. In addition, Immaculate Golden Bow has the following power:\n\nSky-Eater’s Crest: For four motes, Immaculate Golden Bow is transformed for a single turn, growing wings, spines, fins, or other appendages appropriate to the Exalt’s iconic manifestation, providing a barrier of heavy cover against ranged attacks.", :prereq-charms "Phantom Arrow Technique", :ability :archery, :name "Immaculate Golden Bow", :sky-eater’s-crest "For four motes, Immaculate Golden Bow is transformed for a single turn, growing wings, spines, fins, or other appendages appropriate to the Exalt’s iconic manifestation, providing a barrier of heavy cover against ranged attacks.", :type :simple, :duration "One scene", :page 258, :keywords "None", :min-ability 4, :cost "5m, 1wp", :character-tags [], :min-essence 2}
                     {:description "The Solar shoots from her heart; the arrow is part of her. As such, she can sometimes reach out and draw her Essence across an arrow in flight, causing it to flash and flare, surging toward its target like a smite from the sun. This Charm can only be activated on an attack supplemented by Fiery Arrow Attack; when the attack generates at least one 10, this Charm adds one automatic success to the attack, and for each 10 in the attack roll it adds one die to the attack’s raw damage. As the name suggests, Dazzling Flare Attack goes off like a spectacular flare that can be seen for miles—every target using shadows for cover within two range bands must roll Stealth, with a -2 success penalty to the attempt, or be revealed.", :prereq-charms "Fiery Arrow Attack", :ability :archery, :name "Dazzling Flare Attack", :type :reflexive, :duration "Instant", :page 258, :keywords "Decisive-only", :min-ability 5, :cost "3m", :character-tags [], :min-essence 2}
                     {:description "Clearing her mind of all thoughts, the Solar focuses only on the arrow drawn back against her bowstring, feeling for the flow of Essence she needs to make an incredible shot. The Solar must take a special “long aim” action—an aim action lasting three rounds—against a single target in order to use this Charm. Invoking Seven Omens Shot converts the normal +3 bonus dice from aiming into three automatic non-Charm successes and adds any extra successes on the attack roll to the attack’s raw damage. Accuracy Without Distance can shorten the action by a single round, but may only add a single non-Charm success for each round shortened. Killing an opponent with this Charm awards the Solar one point of Willpower.", :prereq-charms "Accuracy Without Distance", :ability :archery, :name "Seven Omens Shot", :type :simple, :duration "Instant", :page 255, :keywords "Decisive-only", :min-ability 5, :cost "3m, 1wp", :character-tags [], :min-essence 3}
                     {:description "Holding arrows between each finger, the Solar draws down on her target, firing with profound speed and focus. The Solar may draw and attack a single target repeatedly, launching multiple withering attacks until she either misses or crashes her opponent. Revolving Bow Discipline can only be used within short or close range, and can’t be used on targets already in crash. Used against a battle group, the Solar attacks until she misses or depletes the group’s Magnitude.\n\nAt Essence 4+, the Solar gains one point of temporary Willpower when she crashes a foe with this attack.", :prereq-charms "Arrow Storm Technique", :ability :archery, :name "Revolving Bow Discipline", :type :simple, :duration "Instant", :page 259, :keywords "Perilous, Withering-only", :min-ability 5, :cost "6m, 1wp", :character-tags [], :min-essence 3}
                     {:description "With instincts honed for the kill, the Exalt feels the momentum of battle break and flee her target. When an opponent within range suffers crash, the Lawgiver draws on this moment to attack again, even if she has already used a combat action that turn. This attack requires no aim action, but if she wishes to use Finishing Snipe, the Lawgiver must not be engaged in a Simple action that would prevent her from attacking.", :prereq-charms "Hunter’s Swift Answer", :ability :archery, :name "Finishing Snipe", :type :reflexive, :duration "Instant", :page 259, :keywords "Decisive-only", :min-ability 5, :cost "7m", :character-tags [], :min-essence 3}
                     {:description "The Lawgiver hones her killing prowess, splitting an arrow into a devastating barrage. The Exalt only needs a single arrow to fire this shot; her one arrow splits into multiple arrows and all are directed against a single target. The Solar may split her shot into a maximum of (Dexterity) arrows. If the attack hits, each created arrow hits with a raw damage equal to the Solar’s current Initiative minus her successes on each damage roll. Therefore, if she rolls 10 damage dice and gains two successes on the first roll, the second roll will feature eight damage dice. If that gains four successes, her third damage roll will be four dice. If the Exalt runs out of damage dice due to successful hits, her created arrows cannot do less than (Essence) damage each.", :prereq-charms "Phantom Arrow Technique", :ability :archery, :name "Rain of Feathered Death", :type :simple, :duration "Instant", :page 259, :keywords "Decisive-only", :min-ability 5, :cost "3m per duplicate, 1wp", :character-tags [], :min-essence 3}
                     {:description "During combat, if the Lawgiver’s Awareness check uncovers an opponent, she may make an attack against that opponent regardless of the Initiative order, without using her attack action for the round, and without having to use an aim action. The Solar may pay to use this Charm multiple times to attack multiple opponents she has uncovered in a single Awareness check.", :prereq-charms "Dazzling Flare Attack", :ability :archery, :name "Shadow-Seeking Arrow", :type :reflexive, :duration "Instant", :page 259, :keywords "Uniform", :min-ability 5, :cost "3m, 2i", :character-tags [], :min-essence 3}
                     {:description "The Lawgiver fires an arrow that courses with burning Essence, streaking down like a falling star to explode at her enemy’s feet. This attack is a difficulty 3 gambit that requires no aim action. However, if the Exalt aims before using the Charm, she may act first on the next round, regardless of her place in the Initiative order. If another character uses magic to act first on the same round, compare their Initiative to determine order of actions. The Initiative roll on this gambit is made with double 9s. If successful, it blasts the target out of position, forcing him to act (2 + extra successes on the gambit’s Initiative roll) ticks later than he would have. If Searing Sunfire Interdiction drops the target to a tick of 0 or less, he loses his turn for the round. Searing Sunfire Interdiction cannot be used on the same target twice consecutively.\n\nAn Essence 3+ repurchase of this Charm lowers the difficulty of the gambit to 2 and waives the Initiative cost upon success. \n\nAn Essence 4+ repurchase of the upgraded Charm resets the Solar’s attack if she drops an opponent from a higher Initiative tick to a lower Initiative tick than her own.\n\nAn Essence 5+ repurchase of the second upgrade allows the Solar to use Searing Sunfire Interdiction twice consecutively on the same target, dropping the Willpower cost from the second shot. If the Solar delays her opponent with both shots, he’s also forced backwards a range band, even if this would force him off a cliff or a rooftop. \n\nAn Essence 6+ repurchase of the previous upgrade allows the Lawgiver to use this gambit repeatedly against a single target: each time she successfully hits with this gambit, she may immediately fire the gambit again, so long as she has ammunition. If she misses or forces her target to lose a turn, the Charm ends. As with the previous upgrade, waive the willpower cost from all but the first shot. If the Solar stops an opponent from acting, award her a point of Willpower. As above, for every two shots the opponent is delayed, he’s forced back a range band. \n\nAn Essence 6+ repurchase of the previous upgrade allows the Solar to immediately target a new opponent with the above effect upon dropping another opponent to tick 0 or lower with Searing Sunfire Interdiction. ", :prereq-charms "Dazzling Flare Attack", :ability :archery, :name "Searing Sunfire Interdiction", :type :simple, :page 259, :keywords "Decisive-only", :min-ability 5, :cost "4m, 1i, 1wp", :character-tags [], :min-essence 3}
                     {:description "The Lawgiver draws a blazing bolt of Essence across her bow and fires it. The attack ignores the range of her weapon, and can be made from medium or long range without an aim action, or extreme range with an aim action. The Exalt must have higher Initiative than her target to use this attack; the conjured Spike does not use the Solar’s Initiative to do damage, but instead has a raw damage equal to her current temporary Willpower multiplied by one or the rating of any Intimacy she is trying to uphold or protect. Solar Spike is incompatible with Fiery Arrow Attack and Rain of Feathered Death, and does not reset the Exalt’s Initiative to base on a hit. Solar Spike may only be used once per scene, but can be reset by landing a withering attack with six 10s rolled across the attack and damage rolls. ", :prereq-charms "Dazzling Flare Attack", :ability :archery, :name "Solar Spike", :type :simple, :duration "Instant", :page 260, :keywords "Decisive-only", :min-ability 5, :cost "5m, 1wp", :character-tags [], :min-essence 3}
                     {:description "The Solar’s judgment scourges her target with devouring flames. She may only pay to use this attack when unleashing Solar Spike, and when her anima is at bonfire. Instead of conjuring Solar Spike, the Solar draws her anima as if she were pulling an arrow from a quiver, her iconic manifestation taking the form of a living, screaming missile. This attack follows the rules of Solar Spike, but adds the Solar’s Initiative to the raw damage of the attack. This does cause the Lawgiver’s Initiative to reset on a hit, but if Heart-Eating Incineration kills her target, the target’s body and soul are engulfed in a torrent of flames and burned away completely, restoring a number of motes to the Solar equal to her Intimacy rating for the target plus his permanent Essence score. Using Heart-Eating Incineration resets the Solar’s anima to the dim level. Spirits destroyed with this attack may still reform as they normally might.", :prereq-charms "Solar Spike", :ability :archery, :name "Heart-Eating Incineration", :type :reflexive, :duration "Instant", :page 260, :keywords "Decisive-only", :min-ability 5, :cost "3m, 3a", :character-tags [], :min-essence 3}
                     {:description "The Lawgiver may use this Charm to shorten the long aim action of Seven Omens Shot by one round, forfeiting the prerequisite’s automatic non-Charm successes to make the attack faster. An Essence 5+ repurchase allows the Solar to add the lost successes back into the attack.", :prereq-charms "Seven Omens Shot", :ability :archery, :name "Dust and Ash Sleight", :type :reflexive, :duration "Instant", :page 260, :keywords "Decisive-only", :min-ability 5, :cost "3m", :character-tags [], :min-essence 4}
                     {:description "When the Lawgiver is cornered and fighting for her life, the burning power of her judgment becomes even more terrifying. Drawing the burning Essence of her life from her very wounds, she can unleash a powerful, Essence laden shot from her bow that can override other attacks and overwhelm her attackers. The Solar may use this Charm to clash attacks against her from short or close range, but only when she is in her -4 health levels. This clash gains (Essence) automatic successes, and the damage roll doubles 9s. However, Initiative damaged by this roll is not transferred to the Solar; it is instead rerolled as decisive damage against her target, ignoring hardness and doubling 10s.\n\nAn Essence 5+ repurchase of this Charm removes the Perilous keyword, allowing the Solar to spend herself into lower negative Initiative numbers while crashed.", :prereq-charms "Revolving Bow Discipline", :ability :archery, :name "Heavens Crash Down", :type :reflexive, :duration "Instant", :page 260, :keywords "Clash, Perilous, Withering-only", :min-ability 5, :characters-at-medium-or-long-range-must-typically-take-an-aim-action-before-they-can-attack.-but-this-is-not-always-true.-a-character-does-not-need-to-aim-if "• She is casting a spell. • She is using a Charm from an Ability or martial art  that is not typically ranged to create a ranged attack. Examples", :cost "6m, 2i, 1wp", :character-tags [], :min-essence 4}
                     {:description "The Lawgiver feels the channels of her bow open when her prey falls into peril. This Charm allows the Exalt to fire on a crashed target from medium or long range without using an aim action.", :prereq-charms "Finishing Snipe", :ability :archery, :name "Streaming Arrow Stance", :type :simple, :duration "One scene", :page 261, :keywords "None", :min-ability 5, :cost "6m", :character-tags [], :min-essence 4} {:description "After using an aim action, the Solar may use this Charm to charge her shot with wrathful Essence, adding (Essence) damage to the attack.", :prereq-charms "Streaming Arrow Stance", :ability :archery, :name "Whispered Prayer of Judgment", :type :supplemental, :duration "Instant", :page 255, :keywords "Uniform", :min-ability 5, :cost "1m", :character-tags [], :min-essence 5}])

(def merits [{:description "Each purchase grants the character a single noteworthy Storyteller-controlled ally—a close friend or trusted companion with some useful capabilities.
                The nature of this ally must be defined at the time the Merit is purchased. ",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "The character doesn’t suffer a -1 penalty to actions using his off-hand.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Ambidexterous",
              :upgrading true,
              :type :story,
              :page "",
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 2}}
             {:description "The character owns a magical item",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Artifact",
              :upgrading false,
              :type :story,
              :page "159",
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{4 3 2 5}}
             {:description "The character enjoys official standing in an organization, which is defined when this Merit is obtained",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Backing",
              :upgrading true,
              :type :story,
              :page "159",
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{4 3 2}}
             {:description "Steady as an ox, the character keeps on going long after all others have dropped from exhaustion.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Boundless Enurance",
              :upgrading false,
              :type :story,
              :page 1590,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{2}}
             {:description "The character is the recognized commander of an organized military force of some sort—generally one composed of ordinary mortal soldiers (traits for an average soldier can be found on p. 496).",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Command",
              :upgrading false,
              :type :story,
              :page "",
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{4 3 2 5}}
             {:description "Each purchase grants the character a network of contacts willing to feed him information.
                The player must specify what sort of contacts he’s purchasing at the time the Merit is gained.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Contacts",
              :upgrading false,
              :type :story,
              :page 160,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "The character is the object of organized veneration by a group of mortals.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Cult",
              :upgrading true,
              :type :story,
              :page 160,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 4 3 2 5}}
             {:description "An indefinable “sixth sense” warns the character when she is in danger.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Danger Sense",
              :upgrading false,
              :type :story,
              :page 160,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{3}}
             {:description "The character controls a place of power in Creation.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Demesne",
              :upgrading false,
              :type :story,
              :page 160,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{2}}
             {:description "The character is never completely lost, and is able to naturally determine her orientation relative to the five Poles.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Direction Sense",
              :upgrading false,
              :type :story,
              :page "",
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1}}
             {:description "The character enjoys near-perfect recall.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Eiditic Memory",
              :upgrading false,
              :type :story,
              :page 161,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{2}}
             {:description "Creation is filled with beasts both prosaic and exotic, and the character has formed a deep and profound bond with one of them.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Familiar",
              :upgrading false,
              :type :story,
              :page 161,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 2}}
             {:description "Tempered by war or perhaps by growing up living on the edge, the character’s steady nerves give him an advantage in battle.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Fast Reflexes",
              :upgrading false,
              :type :story,
              :page 161,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{3}}
             {:description "The character is particularly swift and nimble, moving through the world like a shadow racing the light",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Fleet of Foot",
              :upgrading false,
              :type :story,
              :page "159",
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "The character is enormous, standing somewhere between seven and a half to ten feet in height.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Giant",
              :upgrading false,
              :type :story,
              :page 161,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{4}}
             {:description "This Merit bestows ownership of a hearthstone—a gem formed of concentrated geomantic Essence, bearing potent magical powers.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Hearthstone",
              :upgrading false,
              :type :story,
              :page "",
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{4 2}}
             {:description "The character is memorably ugly, possibly as a result of overt deformity, disfiguring scars, inhuman mutations, or just being born that way.Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Hideous",
              :upgrading false,
              :type :story,
              :page 162,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{0}}
             {:description "Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "Each purchase grants the character a single noteworthy Storyteller-controlled ally—a close friend or trusted companion with some useful capabilities.
                            The nature of this ally must be defined at the time the Merit is purchased. ",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "The character doesn’t suffer a -1 penalty to actions using his off-hand.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Ambidexterous",
              :upgrading true,
              :type :story,
              :page "",
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 2}}
             {:description "The character owns a magical item",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Artifact",
              :upgrading false,
              :type :story,
              :page "159",
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{4 3 2 5}}
             {:description "The character enjoys official standing in an organization, which is defined when this Merit is obtained",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Backing",
              :upgrading true,
              :type :story,
              :page "159",
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{4 3 2}}
             {:description "Steady as an ox, the character keeps on going long after all others have dropped from exhaustion.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Boundless Enurance",
              :upgrading false,
              :type :story,
              :page 1590,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{2}}
             {:description "The character is the recognized commander of an organized military force of some sort—generally one composed of ordinary mortal soldiers (traits for an average soldier can be found on p. 496).",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Command",
              :upgrading false,
              :type :story,
              :page "",
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{4 3 2 5}}
             {:description "Each purchase grants the character a network of contacts willing to feed him information.
                            The player must specify what sort of contacts he’s purchasing at the time the Merit is gained.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Contacts",
              :upgrading false,
              :type :story,
              :page 160,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "The character is the object of organized veneration by a group of mortals.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Cult",
              :upgrading true,
              :type :story,
              :page 160,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 4 3 2 5}}
             {:description "An indefinable “sixth sense” warns the character when she is in danger.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Danger Sense",
              :upgrading false,
              :type :story,
              :page 160,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{3}}
             {:description "The character controls a place of power in Creation.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Demesne",
              :upgrading false,
              :type :story,
              :page 160,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{2}}
             {:description "The character is never completely lost, and is able to naturally determine her orientation relative to the five Poles.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Direction Sense",
              :upgrading false,
              :type :story,
              :page "",
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1}}
             {:description "The character enjoys near-perfect recall.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Eiditic Memory",
              :upgrading false,
              :type :story,
              :page 161,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{2}}
             {:description "Creation is filled with beasts both prosaic and exotic, and the character has formed a deep and profound bond with one of them.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Familiar",
              :upgrading false,
              :type :story,
              :page 161,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 2}}
             {:description "Tempered by war or perhaps by growing up living on the edge, the character’s steady nerves give him an advantage in battle.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Fast Reflexes",
              :upgrading false,
              :type :story,
              :page 161,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{3}}
             {:description "The character is particularly swift and nimble, moving through the world like a shadow racing the light",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Fleet of Foot",
              :upgrading false,
              :type :story,
              :page "159",
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "The character is enormous, standing somewhere between seven and a half to ten feet in height.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Giant",
              :upgrading false,
              :type :story,
              :page 161,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{4}}
             {:description "This Merit bestows ownership of a hearthstone—a gem formed of concentrated geomantic Essence, bearing potent magical powers.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Hearthstone",
              :upgrading false,
              :type :story,
              :page "",
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{4 2}}
             {:description "The character is memorably ugly, possibly as a result of overt deformity, disfiguring scars, inhuman mutations, or just being born that way.Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Hideous",
              :upgrading false,
              :type :story,
              :page 162,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{0}}
             {:description "Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "The character is addicted to some substance. This is most often a drug such as alcohol or opium, but might be something more exotic or dangerous, such as Wyld addiction.",
              :name "Addiction",
              :upgrading false,
              :type :story,
              :page "",
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{}}
             {:description "The character is missing a limb; whether this is a birth defect or a battle scar is up to the player.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Amputee",
              :upgrading false,
              :type :story,
              :page "168",
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{}}
             {:description "The character cannot see. Perhaps she was born this way, or lost her eyes in battle, to misfortune, or as part of a draconian punishment",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Blind",
              :upgrading false,
              :type :story,
              :page "168",
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{}}
             {:description "The character can’t hear",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Deaf",
              :upgrading false,
              :type :story,
              :page 168,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{}}
             {:description "The character is afflicted, suffering some malady of the mind. All Derangements are of either Minor, Major, or Defining severity, and may be exploited by social influence as though they were an Intimacy.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Derangement",
              :upgrading false,
              :type :story,
              :page "",
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{}}
             {:description "Allies, yo!",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Allies",
              :upgrading false,
              :type :story,
              :page 158,
              :repurchasable true,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{1 3 5}}
             {:description "The character cannot speak or make vocal utterances. All attempts at communication must be through writing or other non-verbal means.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Mute",
              :upgrading false,
              :type :story,
              :page 169,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{}}
             {:description "The character is infertile, and incapable of reproduction. They may or may not be capable of sexual congress, at the player’s discretion, but they can never sire or bear children.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Sterile",
              :upgrading false,
              :type :story,
              :page 169,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{}}
             {:description "The character is visibly marked by the strangeness of abhuman lineage or the touch of chaos, condemning her to the distrust, fear, and hatred of most people she meets.",
              :confers-merits "Each Merit On\nA New\nLine",
              :name "Wyld Mutant",
              :upgrading false,
              :type :story,
              :page 169,
              :repurchasable false,
              :drawback "Dey be people n shiii",
              :character-tags [[] [] [] [] []],
              :ranks #{}}])

(def chron
  {:name                "Exalted Core",
   :category            :chron,
   :type                :solar,
   :subtype             :adventure
   :key                 "0",
   :img                 "https://i.imgur.com/59mlT7v.jpg"
   :description         "Published by Onyx Path",
   :merits              {:category    :merits
                         :name        "Merits"
                         :img         "https://i.imgur.com/7nhJT8U.png"
                         :description "Customization, in bite-sized-form"
                         :merit-vec merits}

   :charms              {:name          "Charms"
                         :category      :charms
                         :type          :solar
                         :subtype       :charmlist
                         :img           "https://i.imgur.com/Xo9GgHm.png"
                         :archery       archery-charms,
                         :athletics     [],
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
   :martial-arts-styles {:name        "Martial Arts"
                         :category    :martial-arts-styles
                         :description "Powerful displays of perfect combat"
                         :type        :mortal
                         :subtype     :martial-arts
                         :martial-arts-vec [{:name "Black Claw Style"
                                             :category :martial-arts-style
                                             :description "Black Claw style is one of the few martial arts created by a demon, taught to the Exalted in the First Age by Mara, the Shadow-Lover."
                                             :type :martial-arts-style
                                             :subtype :martial-arts
                                             :style-info {:weapons "This style uses exclusively unarmed attacks, emphasizing claw strikes and sudden, lunging kicks."
                                                          :armor "Black Claw style is incompatible with armor."
                                                          :abilities "Black Claw stylists often feign the appearance of fighting on the defensive, using Dodge to both evade attacks and disengage from close combat. Presence is also useful to them, as many of their Charms allow them to sway the hearts and minds of enemies and bystanders alike in combat."}
                                             :charms [{:name "Open Palm Caress"
                                                       :cost "4m"
                                                       :min-essence 1
                                                       :min-ability 2
                                                       :type :supplemental
                                                       :keywords "Mastery"
                                                       :duration "Instant"
                                                       :prereq-charms "None"
                                                       :description "From the very beginning, things start to go wrong. Righteous heroes find themselves cast as vicious bullies when they fight a student of the Black Claw—even when she starts the fight. Open Palm Caress can be used whenever the martial artist rolls Join Battle. As long as at least one enemy received more successes on the roll than the martial artist did, he and his allies are seen as having initiated hostilities, regardless of how the fight actually began. This applies both to any bystanders to the fight and to the enemies themselves, who might suddenly find themselves confused as to why they are attacking the Black Claw stylist. Characters may see through this deception with a reflexive read intentions roll against the martial artist’s Guile. The Black Claw stylist gains a single point of Initiative for each opponent or bystander who was fooled by this ruse, up to a maximum of her Manipulation. \n\n Mastery: The martial artist’s performance is so convincing that if she uses her first turn to make a clinch or decisive attack against an enemy who beat her Join Battle and failed to overcome her Guile, she may pay a point of Willpower to treat that attack as an ambush."}]}]}})


