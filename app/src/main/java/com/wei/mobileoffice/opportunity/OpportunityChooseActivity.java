package com.wei.mobileoffice.opportunity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.business.FollowupAddActivity;

/**
 * Created by weiyilin on 16/6/29.
 */
public class OpportunityChooseActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opportunity_choose);
        ((TextView) findViewById(R.id.title_bar_title)).setText("添加");

        findViewById(R.id.followup).setOnClickListener(this);
        findViewById(R.id.product).setOnClickListener(this);
        findViewById(R.id.title_bar_back).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.followup:
                intent = new Intent(OpportunityChooseActivity.this, FollowupAddActivity.class);
                intent.putExtra("sourceid", getIntent().getStringExtra("opportunityid"));
                intent.putExtra("sourcetype", "2");
                startActivity(intent);
                break;
            case R.id.product:
                break;
        }
    }
}
