(ns daiklave.corebooks.weapons)

(def weapons
  [{:name "Cestus",
    :description "The cestus is an arrangement of leather straps that fits around the hand like a fingerless glove. These straps are covered in brass or iron studs, serving to enhance the wearer’s punches. The metal gauntlets that are part of most forms of heavy armor can also be designed to work as cestuses. They’re usually used in pairs.",
    :tags "Bashing, Brawl, Smashing, Worn",
    :cost 1,
    :type :light}
   {:name "Club/Cudgel/Baton",
    :description "A club may be a simple billet of wood, or it may be wrapped in metal bands with a leather grip. Clubs are popular among barbaric tribes, peasants in revolt and relatively poor outlaws. A club may be balanced as a thrown weapon (using the Thrown Ability). Small improvised weapons like chair legs are treated as clubs, but gain the Improvised tag.",
    :tags "Bashing, Melee, Smashing; sometimes Thrown (Short), Improvised",
    :cost 0,
    :type :light}
   {:name "Hook Sword",
    :description "The hook sword is similar in appearance to a straight sword, except it ends in a J-shaped hook that curves back along the inside of the blade. They’re usually wielded paired.",
    :tags "Lethal, Martial Arts, Disarming",
    :cost 3,
    :type :medium}
   {:name "Khatar",
    :description "The khatar, also known as the punch dagger, is a singlebladed knife with a hilt assembly perpendicular to the blade. Held in a closed fist, the blade juts from the front of the character’s fist so that the character’s punches deliver deadly stabs.",
    :tags "Lethal, Brawl, Piercing",
    :cost 2,
    :type :light}
   {:name "Knife",
    :description "A knife is a weapon with a chopping and stabbing blade about a foot long, which can have a single or double edge. Most adults in the world of Exalted carry a knife, for utility as well as personal defense. Knives are used as tableware and to cut and pry, as well as for combat. Knives can also be used as thrown weapons.",
    :tags "Lethal, Melee, Thrown (Short)",
    :cost 0,
    :type :light}
   {:name "Sai",
    :description "Sai are three-pronged, fork-shaped weapons as long as large daggers. They are specially designed to twist weapons out of an opponent’s grasp by catching the blade between the prongs.",
    :tags "Lethal, Melee, Disarming",
    :cost 2,
    :type :light}
   {:name "Short Sword",
    :description "Short swords are weapons with blades about two feet long. Some are single-edged, while others have cutting edges on both sides. They are typically carried as weapons by soldiers and as tools by barbarians. Soldiers use short swords because they make excellent formation-fighting weapons—longer weapons are difficult to manage in close quarters. Barbarians use them as tools because they’re small enough for skinning, butchering and brush-clearing and large enough to fight with in a pinch. Short swords are also favored as backup weapons by spearmen and archers.",
    :tags "Lethal, Melee, Balanced",
    :cost 1,
    :type :light}
   {:name "Tiger Claws",
    :description "Tiger claws consist of a glove or gauntlet with three or four curved talons extending from the back. Tiger claws are most common in cultures that live close to big cats, but their intimidating appearance and the terrifying wounds they leave have spread them throughout Creation.",
    :tags "Lethal, Brawl, Worn",
    :cost 1,
    :type :light}
   {:name "Unarmed",
    :description "The most basic of all weapons, this “weapon” models strikes and kicks made with the character’s fists and feet.",
    :tags "Bashing, Brawl, Grappling, Natural",
    :cost 0,
    :type :light}
   {:name "Whip",
    :description "A whip is a handle with a coil of leather thongs or tightly woven cord, primarily used by drovers to control beasts. Whips inflict more pain than damage, but a skilled user can entangle foes or wrench weapons from their grip. Most whips are between two and five yards long.",
    :tags "Bashing, Melee, Disarming, Flexible, Grappling, Reaching",
    :cost 1,
    :type :light}
   {:name "Wind and Fire Wheel",
    :description "This weapon can be as simple as an undecorated steel ring, but it typically has handles built into it and sharpened protrusions jutting from the top, bottom and front. These weapons are often wielded paired; they deliver crushing or slashing blows and can block or catch enemy weapons.",
    :tags "Lethal, Martial Arts, Disarming",
    :cost 2,
    :type :light}
   {:name "Axe/Hatchet",
    :description "A broad-bladed weapon balanced for use in one hand, an axe is easy to manufacture and of great utility as a tool as well as a weapon. Axes can also be thrown.",
    :tags "Lethal, Melee, Thrown(Short), Chopping",
    :cost 0,
    :type :medium}
   {:name "Chopping Sword",
    :description "his weapon is a sword with a chopping blade about three feet long. Some versions have a square or angled tip, while others sport a curve with a wicked back clip. Unlike the slashing sword, chopping swords are designed not for fencing and agility, but for delivering solid blows that hack through armor and bone.",
    :tags "Lethal, Melee, Chopping",
    :cost 2,
    :type :medium}
   {:name "Fighting Chain",
    :description "Fighting chains are slender, sturdy chains with small weights on either end. Most fighting chains are between three and five yards long. In addition to making ordinary attacks, fighting chains can be used to perform clinches and stunts involving grabbing, pulling, and swinging from objects.",
    :tags "Bashing, Martial Arts, Disarming, Flexible, Grappling, Reaching",
    :cost 1,
    :type :medium}
   {:name "Javelin",
    :description "The javelin is a small, light spear designed for throwing. It can also be used in close combat if necessary",
    :tags "Lethal, Melee, Thrown",
    :cost 1,
    :type :medium}
   {:name "Mace/Hammer",
    :description "A mace is nothing more than a heavy weight on the end of a handle. Hammers are better balanced but lighter. Both weapons deliver crushing, stunning blows to targets. In the world of Exalted, the heads of maces and hammers are often elaborately decorated. Some are sculpted into the shape of animal heads, while others have been cut so that, whatever angle they’re viewed from, they depict a symbol important to the wielder. Large or bulky improvised weapons like tables or chairs are treated as a mace or hammer, but with the improvised tag.",
    :tags "Bashing, Melee, Smashing; sometimes Improvised",
    :cost 1,
    :type :medium}
   {:name "Wind and Fire Wheel",
    :description "An elegant weapon, from a more... civilized age.",
    :tags "Lethal, Martial Arts, Disarming",
    :cost 2,
    :type :light}
   {:name "Seven Section Staff",
    :description "This weapon, which can actually have from three to 12 sections, is made up of a number of metal or wood segments connected by very short lengths of chain. It’s wielded as a combination of staff and flail and is difficult to master",
    :tags "Bashing, Martial Arts, Disarming, Flexible",
    :cost 2,
    :type :medium}
   {:name "Shield",
    :description "A relatively flat weapon of metal or wood that the wielder holds or straps to her arm and used to deflect attacks and bash foes.",
    :tags "Bashing, Melee, Shield",
    :cost 1,
    :type :medium}
   {:name "Short Spear",
    :description "A short-hafted weapon used primarily for stabbing, the short spear often has a long head so that it can be used for slashing and chopping in a pinch. Short spears are versatile weapons and much-favored by warrior cultures and elite troops.",
    :tags "Lethal, Melee, Thrown(Short), Piercing",
    :cost 1,
    :type :medium}
   {:name "Slashing Sword",
    :description "A slashing sword has a single-edged blade about three feet long. These swords are quick and used primarily for arcing cuts. The slashing sword is favored by cavalry, as its shape and length make it suited for use from horseback.",
    :tags "Lethal, Melee, Balanced",
    :cost 2,
    :type :medium}
   {:name "Spear",
    :description "One of the most versatile and simple weapons ever produced, the spear is a foot-long head attached to the end of a five- to six-foot-long pole. The head of the spear typically has extensions built into the sides to prevent it from going too deep into a target. Spears are inexpensive and effective and, as a result, are one of the most common armaments of soldiers everywhere.",
    :tags "Lethal, Melee, Reaching, Piercing",
    :cost 1,
    :type :medium}
   {:name "Straight Sword",
    :description "A straight sword has a double-edged blade about threefeet long, usually with a narrow blade or one that tapers slightly toward the tip. The tip is either pointed or chiselshaped. Straight swords are well-balanced, accurate weapons, capable of deadly thrusts as well as slashing attacks",
    :tags "Lethal, Melee, Balanced",
    :cost 2,
    :type :medium}
   {:name "Staff",
    :description "A staff is a length of sturdy wood, sometimes wrapped in metal bands or leather. Many staves are clearly weapons, but more than one thug has been thrashed by a traveler’s well-worn walking stick. Staves are also excellent defensive weapons.",
    :tags "Bashing, Melee, Reaching",
    :cost 0,
    :type :medium}
   {:name "Great Axe/Scythe",
    :description "These fearsome long-hafted weapons are typically used for striking downward or for wide, sweeping blows. The great axe is a narrow-bladed axe designed for use in both hands, and it is a favorite weapon of the Realm’s heavy infantry, which uses them during sieges and assaults. Scythes are thick, curved blades attached to a long handle, used by farmers for cutting swathes of grain—and occasionally employed on the battlefield to do the same to troops",
    :tags "Lethal, Melee, Chopping, Reaching, Two-Handed",
    :cost 1,
    :type :heavy}
   {:name "Great Sword",
    :description "A great sword is a two-handed weapon used for hacking blows. These massive swords can be either single- or double-edged. Although these weapons are slow and unsuited to fencing, a strong blow from a great sword can cut a man in half.",
    :tags "Lethal, Melee, Balanced, Reaching, Two-Handed",
    :cost 2,
    :type :heavy}
   {:name "Lance",
    :description "The lance is a strong-shafted spear designed to be used from horseback. Lances are typically not used against other cavalry, but to ride down infantry. Using a lance effectively when on foot requires two hands",
    :tags "Lethal, Melee, Piercing, Reaching, Two-Handed when on foot",
    :cost 1,
    :type :heavy}
   {:name "Poleaxe/Halberd",
    :description "Similar to the great axe but equipped with a longer haft and a heavier blade. While less deadly than the great sword, the poleaxe is considerably cheaper. As a result, it’s a popular armament for heavy infantry.",
    :tags "Lethal, Melee, Chopping, Reaching, Two-Handed",
    :cost 2,
    :type :heavy}
   {:name "Sledge/Tetsubo",
    :description "The sledge is a two-handed mace or hammer whose powerful strikes are slow and unwieldy. Like maces, many sledges are elaborately decorated. Their huge heads are often carved with battle scenes or fabulous beasts. The tetsubo, favored by pragmatists for its simpler construction, is a massive war-club studded with iron knobs.",
    :tags "Bashing, Melee, Reaching, Smashing, Two-Handed",
    :cost 2,
    :type :heavy}
   {:name "Wind and Fire Wheel",
    :description "An elegant weapon, from a more... civilized age.",
    :tags "Lethal, Martial Arts, Disarming",
    :cost 2,
    :type :light}
   {:name "Chakram",
    :description "A chakram is a small disc or ring of steel with razor sharp\nouter edges",
    :tags "Lethal, Thrown (Medium), Cutting, Mounted",
    :cost 1,
    :type :light}
   {:name "Dart",
    :description "Essentially a shorter, lighter javelin, typically around one\nfoot long.",
    :tags "Lethal, Thrown (Medium), Concealable, Mounted, Poisonable",
    :cost 1,
    :type :light}
   {:name "Needle",
    :description "Needles are the ultimate in subtle assassination instruments. These tiny spikes are as long as a small finger and as wide as a housecat’s fang. They can be thrown with the flick of a finger or even held in the mouth and spat at a target. While they inflict very little damage, most assassins will equip their needles with poison. Using a blowgun to propel needles extends their range to Thrown (Medium)",
    :tags "Subtle, Thrown (Short), Concealable, Poisonable",
    :cost 1,
    :type :light}
   {:name "Shuriken",
    :description "Shuriken are small star-shaped blades that can be thrown\neasily",
    :tags "Lethal, Thrown (Short), Concealable, Mounted, Poisonable",
    :cost 1,
    :type :light}
   {:name "Sling",
    :description "Slings are small pouches with thongs attached to either side. A stone or metal sling “bullet” is placed in the pouch, and the sling is swung in a circle at high speed, hurling its projectile at great velocity. Slings are inexpensive and can use any rock of approximately the correct size as ammunition, but they are less accurate than bows. By taking an additional aim action before firing, this weapon’s range can be extended to long for one attack. This aim action provides no benefit beyond enabling long-range attacks.",
    :tags "Bashing, Thrown (Medium), Concealable, Special",
    :cost 1,
    :type :medium}
   {:name "Staff Sling",
    :description "This weapon is essentially a sling on a shaft between three and five feet long. When the wielder swings the shaft, the sling releases its stone with greater force than a normal sling. By taking an additional aim action before firing, this weapon’s range can be extended to long for one attack. This aim action provides no benefit beyond enabling longrange attacks",
    :tags "Bashing, Thrown (Medium), Special",
    :cost 1,
    :type :medium}
   {:name "War Boomerang",
    :description "These weapons are curved, flat pieces of sharpened metal or specially hardened ironwood that spin through the air with deadly force. While not normally designed to return, highly skilled characters can perform this impressive feat. Players of characters who have three or more dots in Thrown may make a second, reflexive (Dexterity + Thrown) roll if the boomerang misses its target. If this second roll succeeds, the boomerang curves around and lands within close range of the thrower. If the thrower’s player rolls three or more successes, then the boomerang actually returns to the thrower’s hands.",
    :tags "Lethal, Thrown (Medium), Cutting, Mounted, Special",
    :cost 1,
    :type :medium}
   {:name "Hand Crossbow",
    :description "Most hand crossbows are small pistol-shaped crossbows that can be used in one hand. A few specially made and more expensive hand crossbows are designed to strap to the user’s forearm and can be concealed in a loose sleeve. These concealable crossbows are most often used by assassins.",
    :tags "Lethal, Archery (Medium), Crossbow, Mounted, One-Handed, Piercing, Slow",
    :cost 2,
    :type :light}
   {:name "Self Bow",
    :description "A stout length of spring wood about three feet long, the self bow is bent along the wood’s natural shape. Of simple construction, it’s is not very powerful.",
    :tags "Lethal, Archery (Long), Mounted",
    :cost 1,
    :type :light}
   {:name "Composite Bow",
    :description "Composite bows are made of layers of different woods, often reinforced with layers of horn. This reinforced construction gives them more power in a smaller size— like the self bow, they’re about three feet long. The only weakness of a composite bow, other than its expense, is that it’s held together with glue. If it becomes wet, the glue weakens, and the bow can come to pieces. As a result, most composite bows are bound in waterproof leather covers, and very few cultures native to humid climes make common use of them.",
    :tags "Lethal, Archery (Long), Mounted",
    :cost 2,
    :type :medium}
   {:name "Hand Crossbow (Concealable)",
    :description "Most hand crossbows are small pistol-shaped crossbows that can be used in one hand. A few specially made and more expensive hand crossbows are designed to strap to the user’s forearm and can be concealed in a loose sleeve. These concealable crossbows are most often used by assassins.",
    :tags "Lethal, Archery (Medium), Crossbow, Mounted, One-Handed, Piercing, Slow, Concealable",
    :cost 3,
    :type :light}
   {:name "Flame Piece",
    :description "A flame piece is a small firewand, made for one-handed use. Most are wielded by elite Southern cavalry. Like larger firewands, this weapon uses firedust or specialized alchemical reagents as ammunition. See the entry for firewands for further information",
    :tags "Lethal, Archery (Short), Flame, Mounted, One- Handed, Slow",
    :cost 2,
    :type :medium}
   {:name "Long Bow",
    :description "Longer than a self bow, the long bow is harder to pull and\noffers considerably more power",
    :tags "Lethal, Archery (Long)",
    :cost 2,
    :type :medium}
   {:name "Crossbow",
    :description "Crossbows are large, mechanically-powered weapons that\nrequire both hands to load and fire.",
    :tags "Lethal, Archery (Long), Crossbow, Piercing, Powerful, Slow",
    :cost 3,
    :type :medium}
   {:name "Firewand",
    :description "Firewands are one-shot flamethrowers powered by firedust, a substance found naturally occurring deep in the Southern deserts or synthesized from a variety of exotic reagents found elsewhere in that Direction. Made from finely turned brass with wooden or metal stocks, firewands are prized by any soldier lucky enough to own one. Firedust must be loaded down the front of the barrel, and the weapon can hold only a single shot. While readily available in the South, firedust can be expensive and difficult to find elsewhere (Resources • in the South, •• or higher elsewhere). Many soldiers fit bayonets on the ends of their firewands and use them as spears once the weapon has been fired (treat as a short spear).",
    :tags "Lethal, Archery (Short), Flame, Slow",
    :cost 3,
    :type :heavy}])