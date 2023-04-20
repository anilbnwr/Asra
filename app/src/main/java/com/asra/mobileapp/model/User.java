package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

        private static final String ROLE_ADMIN = "administrator";
        private static final String ROLE_COACH = "coach";

        @Expose
        @SerializedName("id")
        public String id;

        @Expose @SerializedName("email")
        public String email ;

        @Expose @SerializedName("first_name")
        public String firstName;

        @Expose @SerializedName("last_name")
        public String lastName;

        @Expose @SerializedName("display_name")
        public String displayName;

        @Expose @SerializedName("skill_level")
        public String skillLevel;

        @Expose @SerializedName("role")
        public String role;

        @Expose @SerializedName("admin_privilege")
        public boolean hasAdminPrevilege;


        public String getUserId(){
                return id;
        }


        public boolean isAdmin(){
            return ROLE_ADMIN.equalsIgnoreCase(role);
        }

        public boolean isCoach(){
            return ROLE_COACH.equalsIgnoreCase(role);
        }

}
