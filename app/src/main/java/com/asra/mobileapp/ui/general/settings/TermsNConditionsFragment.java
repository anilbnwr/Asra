package com.asra.mobileapp.ui.general.settings;

import android.text.Html;
import android.text.TextUtils;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.databinding.FragmentHelpBinding;
import com.asra.mobileapp.ui.base.ETFragment;

public class TermsNConditionsFragment extends ETFragment<TermsViewModel, FragmentHelpBinding> {


    public static TermsNConditionsFragment newInstance(){
        return new TermsNConditionsFragment();
    }
    private static final String TXT_TERMS_N_CONDITIONS = "<h3>Terms</h3>\n" +
            "<p>By accessing this mobile application, you are agreeing to be bound by these Mobile Application Terms and Conditions of Use, all applicable laws and regulations, and agree that you are responsible for compliance with any applicable local laws. If you do not agree with any of these terms, you are prohibited from using or accessing this site. The materials contained in this Mobile Application are protected by applicable copyright and trademark law.</p>\n" +
            "<h3>Use License</h3>\n" +
            "<p>a. Permission is granted to temporarily download one copy of the materials (information or software) on Evolve GT’s Mobile Application for personal, non-commercial transitory viewing only. This is the grant of a license, not a transfer of title, and under this license you may not:</p>\n" +
            "<ol>\n" +
            "<li>Modify or copy the materials;</li>\n" +
            "<li>Use the materials for any commercial purpose, or for any public display (commercial or non-commercial);</li>\n" +
            "<li>Attempt to recompile or reverse engineer any software contained on Evolve GT’s Mobile Application;</li>\n" +
            "<li>Remove any copyright or other proprietary notations from the materials;</li>\n" +
            "<li>Transfer the materials to another person or “mirror” the materials on any other server.</li>\n" +
            "</ol>\n" +
            "<p>b. This license shall automatically terminate if you violate any of these restrictions and may be terminated by Evolve GT at any time. Upon terminating your viewing of these materials or upon the termination of this license, you must destroy any downloaded materials in your possession whether in electronic or printed format.</p>\n" +
            "<h3>Disclaimer</h3>\n" +
            "<p>a. The materials on Evolve GT’s Mobile Application are provided “as is”. Evolve GT makes no warranties, expressed or implied, and hereby disclaims and negates all other warranties, including without limitation, implied warranties or conditions of merchantability, fitness for a particular purpose, or non-infringement of intellectual property or other violation of rights. Further, Evolve GT does not warrant or make any representations concerning the accuracy, likely results, or reliability of the use of the materials on its Internet Mobile Application or otherwise relating to such materials or on any sites linked to this site.</p>\n" +
            "<h3>Limitations</h3>\n" +
            "<p>In no event shall Evolve GT or its suppliers be liable for any damages (including, without limitation, damages for loss of data or profit, or due to business interruption,) arising out of the use or inability to use the materials on Moto Gladiator’s Internet site, even if Evolve GT or a Evolve GT authorized representative has been notified orally or in writing of the possibility of such damage. Because some jurisdictions do not allow limitations on implied warranties, or limitations of liability for consequential or incidental damages, these limitations may not apply to you.</p>\n" +
            "<h3>Revisions and Errata</h3>\n" +
            "<p>The materials appearing on Evolve GT Mobile Application could include technical, typographical, or photographic errors. Evolve GT does not warrant that any of the materials on its Mobile Application are accurate, complete, or current. Evolve GT may make changes to the materials contained on its Mobile Application at any time without notice. Evolve GT does not, however, make any commitment to update the materials.</p>\n" +
            "<h3>Links</h3>\n" +
            "<p>Evolve GT has not reviewed all of the sites linked to its Internet Mobile Application and is not responsible for the contents of any such linked site. The inclusion of any link does not imply endorsement by Evolve GT of the site. Use of any such linked Mobile Application is at the user’s own risk.</p>\n" +
            "<h3>Application Terms of Use Modifications</h3>\n" +
            "<p>Evolve GT may revise these terms of use for its Mobile Application at any time without notice. By using this Mobile Application you are agreeing to be bound by the then current version of these Terms and Conditions of Use.</p>\n" +
            "<h3>Governing Law</h3>\n" +
            "<p>Any claim relating to Evolve GT’s Mobile Application shall be governed by the laws of the State of Virginia without regard to its conflict of law provisions. General Terms and Conditions applicable to Use of a Mobile Application.</p>";


    @Override
    protected Class<TermsViewModel> getViewModel() {
        return TermsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_help;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_terms_n_condition);
    }

    @Override
    public void initializeViews() {
        String text = getConfigString(MessageProvider.terms_n_conditions);
        text = TextUtils.isEmpty(text) ? TXT_TERMS_N_CONDITIONS : text;
        dataBinding.tvHelpText.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
    }

    @Override
    public void observeEventsFromViewModel() {

    }


}
