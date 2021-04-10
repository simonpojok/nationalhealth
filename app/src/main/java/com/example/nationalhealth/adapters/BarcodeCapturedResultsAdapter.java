package com.example.nationalhealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nationalhealth.R;
import com.example.nationalhealth.models.BarcodeCapturedResultsModel;

import java.util.ArrayList;
import java.util.List;

public class BarcodeCapturedResultsAdapter extends RecyclerView.Adapter<BarcodeCapturedResultsAdapter.MyViewHolder> {
    private Context mContext;
    int pause = 0;
    List<BarcodeCapturedResultsModel> mData;
    private BarcodeCapturedResultsModel barcodeCapturedResultsModel;

    public BarcodeCapturedResultsAdapter(List<BarcodeCapturedResultsModel> mData) {
        this.mData=mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.barcode_result_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        barcodeCapturedResultsModel = mData.get(position);

        holder.surname.setText(barcodeCapturedResultsModel.getSurname());
        holder.givenName.setText(barcodeCapturedResultsModel.getGivenName());
        holder.dateOfBirth.setText(barcodeCapturedResultsModel.getDateOfBirth());
        holder.sexx.setText(String.valueOf(barcodeCapturedResultsModel.getSex()));
        holder.ninn.setText(barcodeCapturedResultsModel.getNin());
        holder.documentNumber.setText(barcodeCapturedResultsModel.getDocumentNumber());
        holder.expirationDate.setText(barcodeCapturedResultsModel.getExpirationDate());
        holder.phoneNumber.setText(barcodeCapturedResultsModel.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView surname, givenName,sexx,ninn, dateOfBirth, documentNumber, expirationDate, phoneNumber;
        private List<BarcodeCapturedResultsModel> list;
        private BarcodeCapturedResultsAdapter adapter;
        MyViewHolder(View view) {
            super(view);

            surname = view.findViewById(R.id.listed_results_value_surname);
            givenName = view.findViewById(R.id.listed_results_value_givenName);
            dateOfBirth = view.findViewById(R.id.listed_results_value_dob);
            sexx = view.findViewById(R.id.listed_results_value_gender);
            ninn = view.findViewById(R.id.listed_results_value_nin);
            documentNumber = view.findViewById(R.id.listed_results_value_documentNumber);
            expirationDate = view.findViewById(R.id.listed_results_value_expiryDate);
            phoneNumber=view.findViewById(R.id.listed_results_value_nationality);
            list = new ArrayList<BarcodeCapturedResultsModel>();
            adapter = new BarcodeCapturedResultsAdapter(list);
        }

    }

}
