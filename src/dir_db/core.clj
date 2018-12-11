(ns dir-db.core
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clj-uuid :as uuid]
            [clojure.string :as str]))

(def home-dir (get (System/getProperties) "user.home"))
(def edn-dir (str/join "/" [home-dir ".dirdb" "edn/"]))
(def db-dir (str/join "/" [home-dir ".dirdb" "db/"]))

(defn save-edn
  "This saves the edn into a generated directory based on date."
  [to-save]
  (let [time-uuid (uuid/v1)
        path (str "resources/edn/" #_repo-dir time-uuid)]
    (io/make-parents path)
    (spit path
          to-save)
    time-uuid))

(defn conj-in
  [col keys & values]
  (let [depth (get-in col keys)
        new-value (concat depth values)]
    (assoc-in col keys new-value)))

(def blank-post
  {:meta    {}
   :history []})

(defn slurp-nil
  "wraps slurp, returns nil if file dne"
  [file]
  (try (slurp file) (catch Exception e)))

(defn save-db
  "saves blog posts"
  [{:keys [uuid edn meta]}]
  (let [db-path (str/join "/" ["resources/db" (or uuid (uuid/v1))])
        edn-uuid (save-edn edn)
        db-data (or (edn/read-string (slurp-nil db-path))
                    blank-post)]
    (io/make-parents db-path)
    (spit db-path (conj-in db-data [:history] edn-uuid))))

(defn db-entries
  "returns list of db entries"
  []
  (map #(edn/read-string (slurp %))
       (drop 1 (file-seq (io/file "resources/db/")))))

(defn get-latest
  "returns latest data of db entry gives edn for now"
  [db]
  (let [uuid (last (:history db))]
    (edn/read-string (slurp (io/file (str "resources/edn/" uuid))))))
