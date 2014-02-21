(ns passport.core-test
  (:require [clojure.test :refer :all]
            [passport.core :refer :all]
            [clj-http.fake :refer [with-fake-routes-in-isolation]]))

(def route-regex #"https://api\.opencnam\.com/v2/phone/.*")

(deftest phone-found
  (testing "request for a phone in the CNAM db"
    (with-fake-routes-in-isolation
      {route-regex
      (fn [_] {:status 200 :headers {} :body "{\n  \"price\": 0, \n  \"uri\": \"/v2/phone/%2B16502530000\", 
        \n  \"created\": \"2012-10-12T06:53:05.1    94858\", 
        \n  \"number\": \"+16502530000\", \n  \"updated\": \"2014-02-20T18:59:27.279412\", \n  \"name\": \"GOOGLE INC\"\n}"})}

      (is (nil? (:error (caller-id "+16502530000"))))
      (is (= (:name (caller-id "+16502530000")) "GOOGLE INC")))))

(deftest phone-invalid
  (testing "request for a phone number that is in an invalid form"
    (with-fake-routes-in-isolation
      {route-regex
      (fn [_] {:status 400 :headers {} :body ""})}

      (is (:error (caller-id "bogus")))
      (is (= (:status (caller-id "bogus")) 400)))))

(deftest phone-not-found
  (testing "request for a phone number not in the CNAM db"
    (with-fake-routes-in-isolation
      {route-regex
      (fn [_] {:status 404 :headers {} :body ""})}

      (is (:error (caller-id "+16502530001")))
      (is (= (:status (caller-id "+16502530001")) 404)))))

(deftest invalid-credentials
  (testing "specifying invalid Professional Tier credentials"
    (with-fake-routes-in-isolation
      {route-regex
      (fn [_] {:status 401 :headers {} :body ""})}

      (is (:error (caller-id "+16502530001" "test" "test")))
      (is (= (:status (caller-id "+16502530001" "test" "test")) 401)))))

(deftest insufficient-funds  
  (testing "insufficient funds in account for Professional Tier query"
    (with-fake-routes-in-isolation
      {route-regex
      (fn [_] {:status 402 :headers {} :body ""})}

      (is (:error (caller-id "+16502530000")))
      (is (= (:status (caller-id "+16502530000")) 402)))))

(deftest hourly-limit-exceeded 
  (testing "insufficient funds in account for Professional Tier query"
    (with-fake-routes-in-isolation
      {route-regex
      (fn [_] {:status 403 :headers {} :body ""})}

      (is (:error (caller-id "+16502530000")))
      (is (= (:status (caller-id "+16502530000")) 403)))))
