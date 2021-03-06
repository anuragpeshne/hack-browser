(require 'cljs.nodejs)

(def back    (.getElementById js/document "back"))

(def forward (.getElementById js/document "forward"))

(def refresh (.getElementById js/document "refresh"))

(def omni    (.getElementById js/document "url"))

(def dev     (.getElementById js/document "console"))

(def fav     (.getElementById js/document "fav"))

(def popup   (.getElementById js/document "fav-popup"))

(def view    (.getElementById js/document "view"))

(def console (.getElementById js/document "hack-console"))

(def nav-bar (.getElementById js/document "navigation"))

(def Electron (cljs.nodejs/require "electron"))

(def electron-session (.-session Electron))

(defn go-to-url [url]
  (.loadURL view url)
  (.focus view)
  :ok)

(defn handle-omni-go [event]
  (if (= (.-keyCode event) 13)
    (let [omni-val (.-value omni)]
      (.blur omni)
      (go-to-url omni-val))))

(defn handle-dev-tools []
  (if (.isDevToolsOpened view)
    (.closeDevTools view)
    (.openDevTools view)))

(defn hide
  "hides component on Browser; (a DOM element)"
  [component]
  (set! (-> component .-style .-display) "none")
  :ok)

(defn show
  "hides component on Browser; (a DOM element)"
  [component]
  (set! (-> component .-style .-display) "")
  :ok)

(defn hook-before-send-headers
  "adds hook to Electron.onBeforeSendHeaders"
  [filter cb]
  (-> electron-session .-defaultSession .-webRequest (.onBeforeSendHeaders filter cb)))

(defn update-nav [event]
  (set! (.-value omni) (.-src view)))

(defn reload-view  [] (.reload    view))

(defn back-view    [] (.goBack    view))

(defn forward-view [] (.goForward view))

(defn search-string-doc []
  (.log js/console "it works"))

(defn init-event-listeners []
  (.addEventListener refresh "click"   reload-view)
  (.addEventListener back    "click"   back-view)
  (.addEventListener forward "click"   forward-view)
  (.addEventListener omni    "keydown" handle-omni-go)
  (.addEventListener dev     "click"   handle-dev-tools)
  ;(.addEventListener hack    "click"   toggle-hack-console)
  (.addEventListener view    "did-finish-load" update-nav))

(defn init-key-bindings []
  (.bind js/keyboardJS "mod + l" (fn [] (.focus omni))))

(.log js/console "api-loaded")
