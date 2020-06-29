(ns clj-stripe.checkouts
    "Functions for Stripe Checkouts API
     https://stripe.com/docs/api/checkout/sessions/create"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn create-checkout-session
  "Creates a payment intent.
  Execute with common/execute."
  ([prices mode success-url cancel-url metadata]
   (create-checkout-session prices mode success-url cancel-url metadata nil))
  ([prices mode success-url cancel-url metadata coupon]
   {:operation :create-checkout-session
    :prices prices
    :success-url success-url
    :cancel-url  cancel-url
    :mode mode
    :metadata metadata
    :coupon coupon
    }))

(defn- make-request-map [{:keys [success-url cancel-url prices mode metadata coupon]}]
  (apply merge
         {"success_url" success-url
          "cancel_url"  cancel-url
          "payment_method_types[]" "card"
          "mode" mode
          "metadata" metadata
          }
         (when coupon
           {"subscription_data[coupon]" coupon})
         (map-indexed
          (fn [idx {:keys [price-id quantity]}]
            {(str "line_items[" idx "][price]") price-id
             (str "line_items[" idx "][quantity]") quantity})
          prices)))

(defmethod execute :create-checkout-session [op-data]
  (util/post-request *stripe-token* (str api-root "/checkout/sessions")
                     (make-request-map op-data)))
