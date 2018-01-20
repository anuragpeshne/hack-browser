(ns hack-browser-front.core)

(defonce app-state (atom {:message "Hello Minimum app world!"}))

(def back    (.getElementById js/document "back"))
(def forward (.getElementById js/document "forward"))
(def refresh (.getElementById js/document "refresh"))
(def omni    (.getElementById js/document "url"))
(def dev     (.getElementById js/document "console"))
(def fav     (.getElementById js/document "fav"))
(def popup   (.getElementById js/document "fav-popup"))
(def view    (.getElementById js/document "view"))

(defn go-to-url [event]
  (if (= (.-keyCode event) 13)
    (let [omni-val (.-value omni)]
      (.loadURL view omni-val))))

(defn reload-view  [] (.reload    view))
(defn back-view    [] (.goBack    view))
(defn forward-view [] (.goForward view))

(defn init-event-listeners []
  (.addEventListener refresh "click"   reload-view)
  (.addEventListener back    "click"   back-view)
  (.addEventListener forward "click"   forward-view)
  (.addEventListener omni    "keydown" go-to-url))

(defn mount-root [setting]
  (let [app (. js/document (getElementById "app"))]
    (set! (.-innerHTML app) (:my-env setting))))

(defn init! [setting]
  (init-event-listeners)
  (mount-root setting))
