package com.wei.mobileoffice.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wei.mobileoffice.BaseActivity;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.business.FollowupAddActivity;
import com.wei.mobileoffice.contacts.ContactsAddActivity;
import com.wei.mobileoffice.contract.ContractAddActivity;
import com.wei.mobileoffice.opportunity.OpportunityAddActivity;

/**
 * Created by weiyilin on 16/6/27.
 */
public class CustomerChooseActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_choose);

        ((TextView) findViewById(R.id.title_bar_title)).setText("添加");

        findViewById(R.id.followup).setOnClickListener(this);
        findViewById(R.id.contacts).setOnClickListener(this);
        findViewById(R.id.opportunity).setOnClickListener(this);
        findViewById(R.id.contract).setOnClickListener(this);
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
                intent = new Intent(CustomerChooseActivity.this, FollowupAddActivity.class);
                intent.putExtra("sourceid", getIntent().getStringExtra("customerid"));
                intent.putExtra("sourcetype", "1");
                startActivity(intent);
                break;
            case R.id.contacts:
                intent = new Intent(CustomerChooseActivity.this, ContactsAddActivity.class);
                intent.putExtra("customerid", getIntent().getStringExtra("customerid"));
                startActivity(intent);
                break;
            case R.id.opportunity:
                intent = new Intent(CustomerChooseActivity.this, OpportunityAddActivity.class);
                intent.putExtra("customerid", getIntent().getStringExtra("customerid"));
                startActivity(intent);
                break;
            case R.id.contract:
                intent = new Intent(CustomerChooseActivity.this, ContractAddActivity.class);
                intent.putExtra("customerid", getIntent().getStringExtra("customerid"));
                startActivity(intent);
                break;
        }
    }
}
