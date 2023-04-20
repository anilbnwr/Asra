package com.asra.mobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaiverEventDetails {



    public static class WaiverData{

        @SerializedName("title")
        @Expose
        public String title;

        @SerializedName("hosting")
        @Expose
        public String hostedBy;

        @SerializedName("date")
        @Expose
        public String date;

        @SerializedName("logo")
        @Expose
        public String logo;

        @SerializedName("terms_html")
        @Expose
        public String termsHtml;

        @SerializedName("checkbox_label")
        @Expose
        public String checkboxLabel;

        @SerializedName("button_text")
        @Expose
        public String buttonText;

    }
    public static class WaiverState{
        @SerializedName("code")
        @Expose
        public String shortName;


        @SerializedName("name")
        @Expose
        public String name;
    }
}


