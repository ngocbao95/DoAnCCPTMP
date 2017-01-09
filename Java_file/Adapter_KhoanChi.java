package com.dtt.dangtantien.quanlichitieu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter_KhoanChi extends BaseAdapter {
    Activity context;
    ArrayList<KhoanChi> list = null;

    public Adapter_KhoanChi(Activity context, ArrayList<KhoanChi> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.layout_dong_khoanchi,null);

        TextView txtTenKhoanChi = (TextView) row.findViewById(R.id.textViewDongTen);
        TextView txtSoTienChi   = (TextView) row.findViewById(R.id.textViewDongSoTien);
        TextView txtNgayChi     = (TextView) row.findViewById(R.id.textViewDongNgay);
        TextView txtGhiChuChi   = (TextView) row.findViewById(R.id.textViewDongGhiChu);

        Button   btnSua         = (Button)   row.findViewById(R.id.buttonSua);
        Button   btnXoa         = (Button)   row.findViewById(R.id.buttonXoa);

        final KhoanChi khoanChi = list.get(position);

        DecimalFormat dinhdanggia = new DecimalFormat("###,###,###");

        txtTenKhoanChi.setText(" " + khoanChi.tenKhoanChi);
        txtSoTienChi.setText(" " + dinhdanggia.format(khoanChi.soTienChi) + "");
        txtNgayChi.setText(" " + khoanChi.ngayChi);
        txtGhiChuChi.setText(" " + khoanChi.ghiChuChi);

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateChiActivity.class);
                intent.putExtra("IDChi",khoanChi.IDChi);
                context.startActivity(intent);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                builder.setMessage("Do you want to delete this ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(khoanChi.IDChi);
                        Toast.makeText(context, "Delete success" + " " + khoanChi.tenKhoanChi, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDilog = builder.create();
                alertDilog.show();

            }
        });
        final Animation animLisView = AnimationUtils.loadAnimation(context, R.anim.anim_listview);
        row.startAnimation(animLisView);
        return row;
    }

    private void delete(int iDKhoanChi) {
        SQLiteDatabase database = Database.initDatabase(context,"QLChiTieus.sqlite");
        database.delete("KhoanChi","IDChi = ?", new String[]{iDKhoanChi + ""});
        list.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM KhoanChi", null);
        while (cursor.moveToNext()){
            int iDChi = cursor.getInt(0);
            String tenKhoanChi = cursor.getString(1);
            int soTienChi = cursor.getInt(2);
            String ngayChi = cursor.getString(3);
            String ghiChuChi = cursor.getString(4);
            list.add(new KhoanChi(iDChi, tenKhoanChi, soTienChi, ngayChi, ghiChuChi));
        }
        notifyDataSetChanged();
    }
}
