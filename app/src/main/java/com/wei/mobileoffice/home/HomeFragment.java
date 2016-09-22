package com.wei.mobileoffice.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wei.mobileoffice.BaseFragment;
import com.wei.mobileoffice.R;
import com.wei.mobileoffice.business.BusinessActivity;
import com.wei.mobileoffice.contacts.ContactsActivity;
import com.wei.mobileoffice.contract.ContractActivity;
import com.wei.mobileoffice.customer.CustomerActivity;
import com.wei.mobileoffice.opportunity.OpportunityActivity;
import com.wei.mobileoffice.product.ProductActivity;

import java.util.Locale;

/**
 * Created by weiyilin on 16/5/9.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Override
    protected View doCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.home_fragment, container, false);
        initView();
        return fragmentView;
    }

    @Override
    protected void doActivityCreated(Bundle savedInstanceState) {
        this.createPage(this.getClass().getSimpleName().toLowerCase(Locale.getDefault()));
    }

    private void initView(){
        (fragmentView.findViewById(R.id.tab_business_img)).setOnClickListener(this);
        (fragmentView.findViewById(R.id.tab_customer_img)).setOnClickListener(this);
        (fragmentView.findViewById(R.id.tab_contacts_img)).setOnClickListener(this);
        (fragmentView.findViewById(R.id.tab_opportunity_img)).setOnClickListener(this);
        (fragmentView.findViewById(R.id.tab_contract_img)).setOnClickListener(this);
        (fragmentView.findViewById(R.id.tab_product_img)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tab_business_img:
                intent = new Intent(this.getActivity(), BusinessActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_contacts_img:
                intent = new Intent(this.getActivity(), ContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_customer_img:
                intent = new Intent(this.getActivity(), CustomerActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_contract_img:
                intent = new Intent(this.getActivity(), ContractActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_opportunity_img:
                intent = new Intent(this.getActivity(), OpportunityActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_product_img:
                intent = new Intent(this.getActivity(), ProductActivity.class);
                startActivity(intent);
                break;
        }
    }
}
