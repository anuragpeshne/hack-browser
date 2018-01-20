(ns hack-browser-front.init
    (:require [hack-browser-front.core :as core]
              [hack-browser-front.conf :as conf]))

(enable-console-print!)

(defn start-descjop! []
  (core/init! conf/setting))

(start-descjop!)
