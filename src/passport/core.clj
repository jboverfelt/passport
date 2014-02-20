(ns passport.core
  (:require [cheshire.core :as json]
            [clj-http.client :as http]))

(def ^:const ^:private api-username "OPEN_CNAM_SID")
(def ^:const ^:private api-token "OPEN_CNAM_TOKEN")
(def ^:const ^:private base-url "https://api.opencnam.com/v2/phone/")
(def ^:const ^:private error-text-400 "Invalid phone number")
(def ^:const ^:private error-text-401 "Incorrect/invalid Professional Tier credentials")
(def ^:const ^:private error-text-402 "Insufficient funds for Professional Tier query")
(def ^:const ^:private error-text-403 "Hourly limit for hobbyist tier exceeded")
(def ^:const ^:private error-text-404 "No CNAM information available for phone number")
(def ^:const ^:private error-text-generic "Something went wrong. Please check response status")

(defn- get-env-username []
  (get (System/getenv) api-username ""))

(defn- get-env-token []
  (get (System/getenv) api-token ""))

(defn- handle-response [resp]
  (let [status (:status resp)]
    (cond
      (= status 200) (json/parse-string (:body resp) true)
      (= status 400) {:error error-text-400 :status status}
      (= status 401) {:error error-text-401 :status status}
      (= status 402) {:error error-text-402 :status status}
      (= status 403) {:error error-text-403 :status status}
      (= status 404) {:error error-text-404 :status status}
      :else          {:error error-text-generic :status status})))

(defn- passport-get [url username token]
  (-> url
      (http/get {:accept :json :query-params {"format" "json"} :basic-auth [username token] :throw-exceptions false})
      (handle-response)))

(defn caller-id
  "Takes the phone number to query and
   and optional token and sid. Token and 
   sid can be specified in the OPEN_CNAM_TOKEN
   and OPEN_CNAM_SID env variables or passed in
   to this function if use of the professional tier is desired"

  ([phone]
    (let [url (str base-url phone)
          username (get-env-username)
          token (get-env-token)]
      (passport-get url username token)))

  ([phone username token]
    (passport-get (str base-url phone) username token)))
