(ns dir-db.core
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clj-uuid :as uuid]
            [clojure.string :as str]))

(def home-dir (get (System/getProperties) "user.home"))
(def files-dir (str/join "/" [home-dir ".dirdb" "files/"]))
(def db-dir (str/join "/" [home-dir ".dirdb" "meta/"]))

(defn save-edn
  "This saves the edn into a generated directory based on date."
  [to-save]
  (let [time-uuid (uuid/v1)
        path (str "resources/files/" #_repo-dir time-uuid)]
    (io/make-parents (str "resources/files/" #_repo-dir time-uuid))
    (spit path
          to-save)
    time-uuid))

(defn save-blog
  "saves blog posts"
  [{:keys [uuid edn]}]
  (let [meta-file (str/join "/" ["resources/meta" (or uuid (uuid/v1))])
        file-uuid (save-edn edn)]
    (io/make-parents file)
    file.))
