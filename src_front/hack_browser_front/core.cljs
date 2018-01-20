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
(def hack    (.getElementById js/document "hack"))
(def console (.getElementById js/document "hack-console"))

(def hack-console (js/CodeMirror console (clj->js
                                          {"mode" "clojure",
                                           "lineNumbers" true,
                                           "matchBrackets" true,
                                           "autoCloseBrackets" true})))

(defn go-to-url [event]
  (if (= (.-keyCode event) 13)
    (let [omni-val (.-value omni)]
      (.blur omni)
      (.loadURL view omni-val))))

(defn handle-dev-tools []
  (if (.isDevToolsOpened view)
    (.closeDevTools view)
    (.openDevTools view)))

(def toggle-hack-console
  (let [is-console-visible (atom true)]
    (fn []
      (if @is-console-visible
        (do
          (.blur console)
          (set! (-> console .-style .-display) "none")
          (reset! is-console-visible false))
        (do
          (set! (-> console .-style .-display) "")
          (.focus hack-console)
          (reset! is-console-visible true))))))

(defn update-nav [event]
  (set! (.-value omni) (.-src view)))

(defn reload-view  [] (.reload    view))
(defn back-view    [] (.goBack    view))
(defn forward-view [] (.goForward view))

(defn eval-hack-console [event]
  (let [input-code (.getValue hack-console)]
    (.preventRepeat event)
    (.setValue hack-console "")
    (.log js/console input-code)
    (eval input-code)))

(defn search-string-doc []
  (.log js/console "it works"))

(defn init-event-listeners []
  (.addEventListener refresh "click"   reload-view)
  (.addEventListener back    "click"   back-view)
  (.addEventListener forward "click"   forward-view)
  (.addEventListener omni    "keydown" go-to-url)
  (.addEventListener dev     "click"   handle-dev-tools)
  (.addEventListener hack    "click"   toggle-hack-console)
  (.addEventListener view    "did-finish-load" update-nav))

(defn init-key-bindings []
  (.bind js/keyboardJS "alt + x"  toggle-hack-console)
  (.bind js/keyboardJS "ctrl + c" eval-hack-console)
  (.bind js/keyboardJS "mod + l" #(.focus omni)))

(defn mount-root [setting])

(defn init! [setting]
  (init-event-listeners)
  (init-key-bindings)
  (mount-root setting))
