package com.asra.mobileapp.common;

import android.text.TextUtils;

import com.asra.mobileapp.R;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import timber.log.Timber;

public class MessageProvider {

    private static MessageProvider instance;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private static final String newLine = "|||";

    public static MessageProvider getInstance(){
        if (instance == null){
            Timber.e("Message Provider not instantiated");
        }
        return instance;
    }

    private MessageProvider(){
        init();
    }

    public static MessageProvider createInstance(){
        instance = new MessageProvider();
        return instance;

    }

    private void init(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetch()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Timber.i( "Remote config Fetch Succeeded");
                        mFirebaseRemoteConfig.activate();
                    } else {
                        Timber.i( "Remote config Fetch failed");
                    }
                });
    }

    public String getText(String key){
        String data = mFirebaseRemoteConfig.getString(key);
        if(!TextUtils.isEmpty(data)) {
            data = data.replace(newLine, "\n");
        }
        Timber.d("Remote Config - Key - %s , Value - %s", key, data);
        return data;
    }
    public long getLongValue(String key){
        return mFirebaseRemoteConfig.getLong(key);
    }

    public double getDoubleValue(String key){
        return mFirebaseRemoteConfig.getDouble(key);
    }


    public static final String checkout_transaction_success = "checkout_transaction_success";
    public static final String cart_empty_message = "cart_empty_message";
    public static final String msg_cart_event_added = "msg_cart_event_added";
    public static final String msg_cart_gift_added = "msg_cart_gift_added";
    public static final String msg_cart_archie_added = "msg_cart_archie_added";
    public static final String error_cart_event_failed = "error_cart_event_failed";
    public static final String checkout_label_no_billing = "checkout_label_no_billing";
    public static final String checkout_applying_coupon_code = "checkout_applying_coupon_code";
    public static final String msg_cart_membership_added = "msg_cart_membership_added";
    public static final String error_cart_membership_failed = "error_cart_membership_failed";

    
    public static final String msg_reading_cart_summary = "msg_reading_cart_summary";
    public static final String error_out_of_stock_found = "error_out_of_stock_found";
    public static final String saving_policy_agreement = "saving_policy_agreement";

    public static final String err_no_sign_events = "err_no_sign_events";
    public static final String err_no_user_events = "err_no_user_events";

    public static final String error_generic_message = "error_generic_message";
    public static final String error_no_network = "error_no_network";
    public static final String error_no_items_found = "error_no_items_found";


    public static final String error_no_event = "error_no_event";
    public static final String error_no_credit = "error_no_credit";
    public static final String error_no_data = "error_no_data";

    public static final String msg_updating_billing_address = "msg_updating_billing_address";
    public static final String msg_billing_address_updated = "msg_billing_address_updated";
    public static final String msg_updating_mailing_address = "msg_updating_mailing_address";
    public static final String msg_mailing_address_updated = "msg_mailing_address_updated";

    public static final String events_empty_message = "events_empty_message";

    public static final String archie_cards_empty_message = "archie_cards_empty_message";
    public static final String gift_cards_empty_message = "gift_cards_empty_message";
    public static final String products_empty_message = "products_empty_message";

    public static final String msg_adding_to_cart = "msg_add_event_to_cart";
    public static final String msg_cart_product_added = "msg_cart_product_added";
    public static final String msg_adding_event_to_cart = "msg_adding_event_to_cart";
    public static final String msg_syncing_cart = "msg_syncing_cart";
    public static final String msg_cart_deleted = "msg_cart_deleted";

    public static final String msg_logging_out = "msg_logging_out";
    public static final String msg_saving_signature = "msg_saving_signature";
    public static final String msg_add_event_to_cart = "msg_add_event_to_cart";
    public static final String error_signature_not_saved = "error_signature_not_saved";
    public static final String msg_signature_saved = "msg_signature_saved";


    public static final String msg_deleting_from_cart = "msg_deleting_from_cart";
    public static final String error_place_order_failed = "error_place_order_failed";
    public static final String msg_transaction_pending = "msg_transaction_pending";
    public static final String error_transaction_canceled = "error_transaction_canceled";
    public static final String purchase_order_number = "purchase_order_number";
    public static final String item_out_of_stock = "item_out_of_stock";

    public static final String checkout_error_empty_coupon = "checkout_error_empty_coupon";
    public static final String msg_checkout_place_order = "msg_checkout_place_order";
    public static final String checkout_label_paypal_description = "checkout_label_paypal_description";

    public static final String error_specify_receiver_name = "error_specify_receiver_name";
    public static final String error_specify_receiver_email = "error_specify_receiver_email";


    public static final String msg_logging_in = "msg_logging_in";
    public static final String msg_loading = "msg_loading";
    public static final String error_empty_username = "error_empty_username";
    public static final String error_empty_password = "error_empty_password";

    public static final String error_paypal_payment_not_available = "error_paypal_payment_not_available";

    public static final String signature_disclaimer_text = "signature_disclaimer_text";


    public static final String msg_version_update = "msg_version_update";
    public static final String message_skill_not_eligible = "message_skill_not_eligible";
    public static final String msg_version_latest = "msg_version_latest";
    public static final String app_latest_version = "app_latest_version_android";
    public static final String coupon_balance_changed = "coupon_balance_changed";
    public static final String error_no_transponder = "error_no_transponder";
    public static final String error_no_skill_set = "error_no_skill_set";
    public static final String error_no_active_class = "error_no_active_class";
    public static final String private_event_code_required = "private_event_code_required";
    public static final String error_private_event_code = "error_private_event_code";

    public static final String payment_credit_not_supported = "payment_credit_not_supported";


    public static final String error_no_search_result_for_user = "error_no_search_result_for_user";
    public static final String error_no_search_result_for_events = "error_no_search_result_for_events";

    public static final String label_signature_status_indicator = "label_signature_status_indicator";
    public static final String label_user_count = "label_user_count";
    public static final String error_no_mailing_address = "error_no_mailing_address";
    public static final String error_no_billing_address = "error_no_billing_address";

    public static final String profile_updated_successfully = "profile_updated_successfully";
    public static final String updating_profile = "updating_profile";


    public static final String syncing_local_cart = "syncing_local_cart";
    public static final String local_cart_synced_successfully = "local_cart_synced_successfully";
    public static final String syncing_local_cart_failed = "syncing_local_cart_failed";
    public static final String msg_cart_changed_review_required = "msg_cart_changed_review_required";


    public static final String reading_your_preferences = "reading_your_preferences";
    public static final String preferences_updated_successfully = "preferences_updated_successfully";
    public static final String preferences_update_failed = "preferences_update_failed";

    public static final String skill_levels = "skill_levels";
    public static final String track_yes_skill_levels = "track_yes_skill_levels";
    public static final String track_no_skill_levels = "track_no_skill_levels";

    public static final String upgrading_skill_level = "upgrading_skill_level";
    public static final String skill_level_upgrade_failed = "skill_level_upgrade_failed";
    public static final String skill_level_upgraded_successfully = "skill_level_upgraded_successfully";


    public static final String terms_n_conditions = "terms_n_conditions";

    public static final String error_current_password_empty = "error_current_password_empty";
    public static final String error_new_password_empty = "error_new_password_empty";
    public static final String error_confirm_password = "error_confirm_password";
    public static final String msg_changing_password = "msg_changing_password";

    public static final String msg_pwd_reset_email_required = "msg_pwd_reset_email_required";
    public static final String msg_resetting_password = "msg_resetting_password";
    public static final String text_description_reset_password = "text_description_reset_password";
    public static final String password_reset_successfully = "password_reset_successfully";


    public static final String emergency_relation_ships = "emergency_relation_ships";


    public static final String event_cancel_confirm_title = "event_cancel_confirm_title";
    public static final String event_cancel_confirm_message = "event_cancel_confirm_message";
    public static final String event_cancel_indicator_message = "event_cancel_indicator_message";
    public static final String event_cancel_success_message = "event_cancel_success_message";
    public static final String reading_user_signature = "reading_user_signature";

    public static final String app_update_optional_message = "app_update_optional_message";
    public static final String app_update_mandatory_message = "app_update_mandatory_message";
    public static final String app_update_title = "app_update_title";

    public static final String msg_reading_upcoming_events = "msg_reading_upcoming_events";
    public static final String msg_reading_past_events = "msg_reading_past_events";
    public static final String msg_reading_all_events = "msg_reading_all_events";

    public static final String analytics_enabled = "analytics_enabled";

    public static final String msg_signing_up = "msg_signing_up";
    public static final String loading_waiver_events = "loading_waiver_events";
    public static final String loading_waiver_details = "loading_waiver_details";
    public static final String saving_waiver_signature = "saving_waiver_signature";

    public static final String error_no_selfie_passport = "error_no_selfie_passport";
    public static final String error_empty_signature = "error_empty_signature";


}
