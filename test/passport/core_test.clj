(ns passport.core-test
  (:require [clojure.test :refer :all]
            [passport.core :refer :all]
            [clj-http.fake :refer [with-fake-routes]]))

(def ^:const ^:private base-url "https://api.opencnam.com/v2/phone/")
(def ^:const ^:private error-text-400 "Invalid phone number")
(def ^:const ^:private error-text-401 "Incorrect/invalid Professional Tier credentials")
(def ^:const ^:private error-text-402 "Insufficient funds for Professional Tier query")
(def ^:const ^:private error-text-403 "Hourly limit for hobbyist tier exceeded")
(def ^:const ^:private error-text-404 "No CNAM information available for phone number")
(def ^:const ^:private error-text-generic "Something went wrong. Please check response status")
(def found-url (str base-url "+16502530000"))
(def not-found-url (str base-url "+16502530001"))
(def bogus-phone-url (str base-url "bogus"))

(deftest phone-found
  (testing "request for a phone in the CNAM db"
    (with-fake-routes
      {found-url
      (fn [_] {:status 200 :headers {} :body "{\n  \"price\": 0, \n  \"uri\": \"/v2/phone/%2B16502530000\", 
        \n  \"created\": \"2012-10-12T06:53:05.1    94858\", 
        \n  \"number\": \"+16502530000\", \n  \"updated\": \"2014-02-20T18:59:27.279412\", \n  \"name\": \"GOOGLE INC\"\n}"})}

      (is (nil? (:error (caller-id "+16502530000"))))
      (is (= (:name (caller-id "+16502530000")) "GOOGLE INC")))))

(deftest phone-invalid
  (testing "request for a phone number that is in an invalid form"
    (with-fake-routes
      {bogus-phone-url
      (fn [_] {:status 400 :error error-text-400})}

      (is (:error (caller-id "bogus")))
      (is (= (:status (caller-id "bogus")) 400)))))

(deftest phone-not-found
  (testing "request for a phone number not in the CNAM db"
    (with-fake-routes
      {not-found-url
      (fn [_] {:status 401 :error error-text-401})}

      (is (:error (caller-id "+16502530001")))
      (is (= (:status (caller-id "+16502530001")) 404)))))

(deftest invalid-credentials
  (testing "specifying invalid Professional Tier credentials"
    (with-fake-routes
      {found-url
      (fn [_] {:status 404 :error error-text-404})}

      (is (:error (caller-id "+16502530001" "test" "test")))
      (is (= (:status (caller-id "+16502530001" "test" "test")) 401)))))
