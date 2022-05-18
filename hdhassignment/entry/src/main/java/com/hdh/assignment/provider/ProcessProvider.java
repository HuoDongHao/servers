package com.hdh.assignment.provider;

import com.hdh.assignment.ResourceTable;
import com.hdh.assignment.bean.Process;
import com.hdh.assignment.bean.Server;
import com.hdh.assignment.slice.ShapesSlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

import java.util.List;

public class ProcessProvider extends BaseItemProvider {
    private List<Process> list;
    private AbilitySlice slice;

    public ProcessProvider(List<Process> list, AbilitySlice slice) {
        this.list = list;
        this.slice = slice;
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position >= 0 && position < list.size()){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        final Component dl;
        if(component  == null){
            dl  = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_item_process,null,false);
        } else{
            dl = component;
        }
        Process process = list.get(position);
        Text item = (Text) dl.findComponentById(ResourceTable.Id_content);
        item.setText(process.getItem());
        return dl;
    }
}
