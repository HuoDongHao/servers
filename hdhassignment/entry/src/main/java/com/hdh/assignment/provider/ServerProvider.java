package com.hdh.assignment.provider;

import com.hdh.assignment.ResourceTable;
import com.hdh.assignment.bean.Server;
import com.hdh.assignment.slice.SettingSlice;
import com.hdh.assignment.slice.Shapes2Slice;
import com.hdh.assignment.slice.ShapesSlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

import java.util.ArrayList;
import java.util.List;

public class ServerProvider extends BaseItemProvider {
    private List<Server> list;
    private AbilitySlice slice;
    public ServerProvider(List<Server> list,AbilitySlice slice) {
        this.list  = list;
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
            dl  = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_item_server,null,false);
        } else{
           dl = component;
        }
        Server server = list.get(position);
        Text name = (Text) dl.findComponentById(ResourceTable.Id_name);
        Text ip  = (Text) dl.findComponentById(ResourceTable.Id_ip);
        Button btnSpacify  = (Button) dl.findComponentById(ResourceTable.Id_spacify);
        name.setText(server.getName());
        ip.setText(server.getIp());
        if(position == 1)
            btnSpacify.setClickedListener(listener -> slice.present(new Shapes2Slice(),new Intent()));
        else
            btnSpacify.setClickedListener(listener -> slice.present(new ShapesSlice(),new Intent()));
        return dl;
    }
}