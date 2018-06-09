package com.example.hhj73.fix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by skrud on 2018-06-10.
 */

public class ContractAdapter extends ArrayAdapter<ContractData> implements View.OnClickListener {

    public interface ListBtnClickListener{
        void onListBtnClick(int position);
    }

    //생성자로부터 전달된 listBtnClickListener를 저장한다.
    private ListBtnClickListener listBtnClickListener;

    final ArrayList<ContractData> m_contractData;
    final Context context;
    public ContractAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ContractData> objects, ListBtnClickListener clickListener) {
        super(context, resource, objects);
        this.m_contractData = objects;
        this.context =context;
        this.listBtnClickListener = clickListener;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if(v==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.contract_row,null);
        }

        TextView seniorName = (TextView) v.findViewById(R.id.seniorName);
        TextView studentName = (TextView) v.findViewById(R.id.studentName);
        TextView effectiveDate = (TextView) v.findViewById(R.id.effectiveDate);
        TextView periodMonth = (TextView) v.findViewById(R.id.periodMonth);
        TextView expirationDate = (TextView) v.findViewById(R.id.expirationDate);
        TextView smoking = (TextView) v.findViewById(R.id.smoking);
        TextView smoking_consent = (TextView) v.findViewById(R.id.smoking_consent);;
        TextView smoking_detail = (TextView) v.findViewById(R.id.smoking_detail);
        TextView pet = (TextView) v.findViewById(R.id.pet);
        TextView pet_consent = (TextView) v.findViewById(R.id.pet_consent);
        TextView pet_detail = (TextView) v.findViewById(R.id.pet_detail);
        TextView cerfew = (TextView) v.findViewById(R.id.cerfew);
        TextView cerfew_consent = (TextView) v.findViewById(R.id.cerfew_consent);
        TextView cerfew_detail = (TextView) v.findViewById(R.id.cerfew_detail);
        TextView help = (TextView) v.findViewById(R.id.help);
        TextView help_consent = (TextView) v.findViewById(R.id.help_consent);
        TextView help_detail = (TextView) v.findViewById(R.id.help_detail);
        TextView extraspecial = (TextView) v.findViewById(R.id.extraspecial);
        TextView extraspecial_consent = (TextView) v.findViewById(R.id.extraspecial_consent);
        TextView writedate = (TextView) v.findViewById(R.id.writedate);
        TextView monthlyfee = (TextView) v.findViewById(R.id.monthlyFee);
        TextView address = (TextView)v.findViewById(R.id.address_contract);

        Button effectiveDateBtn = (Button)v.findViewById(R.id.effectiveDateBtn);
        Button monthPeroidBtn = (Button)v.findViewById(R.id.periodMonthBtn);
        Button monthlyFeeBtn = (Button)v.findViewById(R.id.monthlyFeeBtn);
        Button conditionBtn = (Button)v.findViewById(R.id.conditionBtn);
        Button extraspecialBtn = (Button)v.findViewById(R.id.extraspecialBtn);

        final int color_unConsent=v.getResources().getColor(R.color.colorLightRed);
        final int color_Consent=v.getResources().getColor(R.color.colorLightGreen);
        ContractData contractData = m_contractData.get(position);
        if(contractData != null){

            seniorName.setText(contractData.getSeniorName()+" 어르신");
            studentName.setText(contractData.getStudentName()+" 학    생");
            effectiveDate.setText(contractData.getStartdate());
            periodMonth.setText(contractData.getMonthperiod()+"개월");
            expirationDate.setText(contractData.getExpirationdate());
            if (contractData.isSmokingConsent()) {
                smoking.setBackgroundColor(color_Consent);
                smoking_consent.setText("합의");
            }
            else {
                smoking.setBackgroundColor(color_unConsent);
                smoking_consent.setText("미합의");
            }
            if (contractData.isPetConsent()) {
                pet.setBackgroundColor(color_Consent);
                pet_consent.setText("합의");
            }
            else {
                pet.setBackgroundColor(color_unConsent);
                pet_consent.setText("미합의");
            }
            if (contractData.isCerfewConsent()) {
                cerfew.setBackgroundColor(color_Consent);
                cerfew_consent.setText("합의");
            }
            else {
                cerfew.setBackgroundColor(color_unConsent);
                cerfew_consent.setText("미합의");
            }
            if (contractData.isHelpConsent()) {
                help.setBackgroundColor(color_Consent);
                help_consent.setText("합의");
            }
            else {
                help.setBackgroundColor(color_unConsent);
                help_consent.setText("미합의");
            }
            if (contractData.isExtraConsent()) {
                extraspecial.setBackgroundColor(color_Consent);
                extraspecial_consent.setText("합의");
            }
            else {
                extraspecial.setBackgroundColor(color_unConsent);
                extraspecial_consent.setText("미합의");
            }

            writedate.setText(contractData.getContractwritedate());
            int tempFee = Integer.parseInt(contractData.getMonthlyfee());
            DecimalFormat df = new DecimalFormat("###,###,###,###");
            String formalFee = df.format(tempFee);
            monthlyfee.setText(formalFee+"원");
            address.setText(contractData.getAddress());
            extraspecial.setText(contractData.getExtraspecial());

        }

        conditionBtn.setTag("Condition");
        conditionBtn.setOnClickListener(this);
        monthlyFeeBtn.setTag("Fee");
        monthlyFeeBtn.setOnClickListener(this);
        monthPeroidBtn.setTag("Peroid");
        monthPeroidBtn.setOnClickListener(this);
        effectiveDateBtn.setTag("eDate");
        effectiveDateBtn.setOnClickListener(this);
        extraspecialBtn.setTag("Special");
        extraspecialBtn.setOnClickListener(this);


        return v;
    }
    @Override
    public void onClick(View v) {
        if(this.listBtnClickListener!=null){
            this.listBtnClickListener.onListBtnClick((int)v.getTag());
        }
    }
}
