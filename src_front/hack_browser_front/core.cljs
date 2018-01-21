(ns hack-browser-front.core
  (:require [clojure.tools.reader.edn :as edn]
            [cljs.js :refer [empty-state eval js-eval]]
            [cljs.pprint :refer [pprint]]
            [cljs.nodejs :as nodejs]))

(def hack    (.getElementById js/document "hack"))
(def console (.getElementById js/document "hack-console"))

(def state
  "A compiler state, which is shared across compilations."
  (empty-state))

(def hack-console (js/CodeMirror console (clj->js
                                          {"mode" "clojure",
                                           "lineNumbers" true,
                                           "matchBrackets" true,
                                           "autoCloseBrackets" true})))

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

(defn eval-str [s]
  (.log js/console s)
  (eval state
        (edn/read-string s)
        {:eval       js-eval
         :source-map true
         :verbose    true
         :context    :expr}
        (fn [result] (update result :error #(some-> % .-cause .-message)))))

(defn eval-browser-api []
  (.log js/console "eval api started")
  (let [fs (nodejs/require "fs")
        browser-api-str (.readFileSync fs
                                       "src_front/hack_browser_front/browser_api.cljs",
                                       "utf8")]
    (run! #(eval-str %) (clojure.string/split browser-api-str #"\n\n"))
    (.log js/console "eval api done")))

(defn eval-hack-console [event]
  (let [input-code (.getValue hack-console)]
    (.preventRepeat event)
    (let [output (eval-str input-code)]
      (.setValue hack-console (str (:value output)))
      (.bind js/keyboardJS "ctrl + c" eval-hack-console))))

(defn init! [setting]
  (.bind js/keyboardJS "ctrl + c" eval-hack-console)
  (.bind js/keyboardJS "ctrl + s" eval-browser-api)
  (.bind js/keyboardJS "alt + x"  toggle-hack-console)
  (toggle-hack-console))
