package com.kiettc.nomtudien;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class list extends BaseAdapter {

    private ArrayList<HanNom> arr;

    list(ArrayList<HanNom> arr){
        this.arr=arr;
    }

    @Override
    public int getCount() { //Trả về tổng số phần tử, nó được gọi bởi ListView
        return arr.size();
    }

    @Override
    public HanNom getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return arr.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewProduct;
        if (convertView == null) {
            viewProduct = View.inflate(parent.getContext(), R.layout.jj, null);
        } else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        HanNom hanNom = getItem(position);
        ((TextView) viewProduct.findViewById(R.id.hanNom)).setText(hanNom.getHanNom());
        ((TextView) viewProduct.findViewById(R.id.amV)).setText("Âm: "+hanNom.getAmViet());
        ((TextView) viewProduct.findViewById(R.id.mean)).setText("Cách đọc khác: "+hanNom.getAmVKhac());
        ((TextView) viewProduct.findViewById(R.id.cachVietK)).setText(hanNom.getCachVietK());

        return viewProduct;
    }
}
