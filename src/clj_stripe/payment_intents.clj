(ns clj-stripe.payment-intents
    "Functions for Stripe Payment Intents API
     https://stripe.com/docs/api/payment_intents"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn create-payment-intent
  "Creates a payment intent.
  Execute with common/execute."
  [amount currency & extra-info]
  (apply util/merge-maps {:operation :create-payment-intent
                          :amount amount
                          :currency (name currency)} extra-info))

(defmethod execute :create-payment-intent [op-data]
  (util/post-request *stripe-token* (str api-root "/payment_intents") (dissoc op-data :operation)))
