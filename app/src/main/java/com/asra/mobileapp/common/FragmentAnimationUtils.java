package com.asra.mobileapp.common;



import com.asra.mobileapp.R;
import com.ncapdevi.fragnav.FragNavTransactionOptions;

public class FragmentAnimationUtils {

    public static FragNavTransactionOptions getAnimation(){
        return new FragNavTransactionOptions.Builder()
                .customAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                        R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                .build();

    }
}
