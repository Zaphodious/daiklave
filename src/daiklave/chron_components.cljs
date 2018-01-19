(ns daiklave.chron-components
  (:require [rum.core :as rum]
            [daiklave.general-components :as daigen]
            [daiklave.category-components :as daicat]
            [daiklave.state :as lys :refer [change-element! get-change-value]]
            [com.rpl.specter :as sp :refer [transform setval]]
            [cljs.tools.reader.edn :as edn]
            [clojure.string :as str]
            [cljs.reader :as reader]
            [daiklave.fragment :as daifrag]))