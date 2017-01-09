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

public class Adapter_KhoanThu extends BaseAdapter {
    Activity context;
    ArrayList<KhoanThu> list = null;

    public Adapter_KhoanThu(Activity context, ArrayList<KhoanThu> list) {
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
        View row = inflater.inflate(R.layout.layout_dong_khoanthu,null);

        TextView txtTenKhoanThu = (TextView) row.findViewById(R.id.textViewDongTen);
        TextView txtSoTienThu   = (TextView) row.findViewById(R.id.textViewDongSoTien);
        TextView txtNgayThu     = (TextView) row.findViewById(R.id.textViewDongNgay);
        TextView txtGhiChuThu   = (TextView) row.findViewById(R.id.textViewDongGhiChu);

        Button   btnSua         = (Button)   row.findViewById(R.id.buttonSua);
        Button   btnXoa         = (Button)   row.findViewById(R.id.buttonXoa);

        final KhoanThu khoanThu = list.get(position);
        DecimalFormat dinhdanggia = new DecimalFormat("###,###,###");

        txtTenKhoanThu.setText(" " + khoanThu.tenKhoanThu);
        txtSoTienThu.setText(" " + dinhdanggia.format(khoanThu.soTienThu) + "");
        txtNgayThu.setText(" " + khoanThu.ngayThu);
        txtGhiChuThu.setText(" " + khoanThu.ghiChu);
//        txtIDThu.setText(khoanThu.IDThu);

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateActivity.class);
                intent.putExtra("IDThu",khoanThu.IDThu);
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
                        delete(khoanThu.IDThu);
                        Toast.makeText(context, "Delete success" + " " + khoanThu.tenKhoanThu, Toast.LENGTH_SHORT).show();
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

    private void delete(int iDKhoanThu) {
        SQLiteDatabase database = Database.initDatabase(context,"QLChiTieus.sqlite");
        database.delete("KhoanThu","IDThu = ?", new String[]{iDKhoanThu + ""});
        list.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM KhoanThu", null);
        while (cursor.moveToNext()){
            int iDThu = cursor.getInt(0);
            String tenKhoanThu = cursor.getString(1);
            int soTienThu = cursor.getInt(2);
            String ngayThu = cursor.getString(3);
            String ghiChuThu = cursor.getString(4);
            list.add(new KhoanThu(iDThu, tenKhoanThu, soTienThu, ngayThu, ghiChuThu));
        }
        notifyDataSetChanged();
    }
}
