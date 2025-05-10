package com.example.w_safe;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class WomenLawActivity extends AppCompatActivity {

    private final String[] laws = {
            "https://en.wikipedia.org/wiki/Hindu_Succession_Act,_1956", // Law 1
            "https://www.indiacode.nic.in/bitstream/123456789/15100/1/immoral_traffic_prevention_act_%28itpa%29_1956.pdf", // Law 2
            "https://www.indiacode.nic.in/bitstream/123456789/5556/1/dowry_prohibition.pdf", // Law 3
            "https://doj.gov.in/access-to-justice-for-the-marginalized/", // Law 4
            "https://ncst.nic.in/", // Law 5
            "http://ncw.nic.in/", // Law 6
            "https://www.education.gov.in/sites/upload_files/mhrd/files/upload_document/RTE_Section_wise_rationale_rev_0.pdf", // Law 7
            "https://dfpd.gov.in/WriteReadData/Other/nfsa_1.pdf", // Law 8
            "https://rti.gov.in/rti%20act,%202005%20(amended)-english%20version.pdf ", // Law 9
            "https://www.indiacode.nic.in/bitstream/123456789/8865/1/200756senior_citizenact.pdf ", // Law 10
            "https://www.epfindia.gov.in/site_docs/PDFs/Downloads_PDFs/EPFAct1952.pdf ", // Law 11
            "https://www.indiatoday.in/education-today/gk-&-current-affairs/story/all-about-national-food-security-act-2013-1871060-2021-10-29", // Law 12
            "https://timesofindia.indiatimes.com/city/goa/hc-exempts-comunidades-from-purview-of-rti-act/articleshow/112298570.cms", // Law 13
            "https://en.wikipedia.org/wiki/Maintenance_and_Welfare_of_Parents_and_Senior_Citizens_Act,_2007", // Law 14
            "https://www.epfindia.gov.in/site_docs/PDFs/Downloads_PDFs/EPFAct1952.pdf" // Law 15
    };

    private final int[] lawTextViewIds = {
            R.id.law1, R.id.law2, R.id.law3, R.id.law4, R.id.law5,
            R.id.law6, R.id.law7, R.id.law8, R.id.law9, R.id.law10,
            R.id.law11, R.id.law12, R.id.law13, R.id.law14, R.id.law15
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_women_laws);
        setupLawClickListeners();
    }

    private void setupLawClickListeners() {
        for (int i = 0; i < lawTextViewIds.length; i++) {
            setupClickListener(lawTextViewIds[i], laws[i]);
        }
    }

    private void setupClickListener(int textViewId, String url) {
        TextView lawTextView = findViewById(textViewId);
        lawTextView.setOnClickListener(v -> openWebsiteUsingIntent(url));
    }

    private void openWebsiteUsingIntent(String url) {
        if (url != null && !url.isEmpty()) {
            Uri webpage = Uri.parse(url);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
            try {
                startActivity(webIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request. Please install a web browser.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
        }
    }
}
