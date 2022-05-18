package com.hdh.assignment.slice;

import com.hdh.assignment.ResourceTable;
import com.hdh.assignment.bean.Server;
import com.hdh.assignment.provider.ServerProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.agp.window.service.Window;

import java.util.ArrayList;
import java.util.Hashtable;

public class ServerSlice extends AbilitySlice {
    private Hashtable<String,String> server = new Hashtable<String,String>();
    ArrayList<Server> serverList = new ArrayList<>();
    private ListContainer listContainer;
    ServerProvider serverProvider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_serverlist);
        //服务器
        Component enterServer = findComponentById(ResourceTable.Id_server);
        enterServer.setClickedListener(listener -> present(new ServerSlice() , new Intent()));
        //图形
        Component enterShape = findComponentById(ResourceTable.Id_shape);
        enterShape.setClickedListener(listener -> present(new ShapesSlice() , new Intent()));
        //进程
        Component enterProcess = findComponentById(ResourceTable.Id_process);
        enterProcess.setClickedListener(listener -> present(new ProcessSlice() , new Intent()));
        //主题
        Component enterSetting = findComponentById(ResourceTable.Id_setting);
        enterSetting.setClickedListener(listener -> present(new SettingSlice() , new Intent()));

        init();
        Button btnAdd = (Button) findComponentById(ResourceTable.Id_add);
        // 为按钮设置点击事件回调
        btnAdd.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                showDialog();
            }
        });
        listContainer.setItemLongClickedListener(new ListContainer.ItemLongClickedListener() {
            @Override
            public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long l) {
                showdeleteDialog(i);
                return true;
            }
        });
        Button btnoutline = (Button) findComponentById(ResourceTable.Id_outline);
        btnoutline.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                showhdh();
            }
        });
    }

    @Override
    public void init() {
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_listContainer);
        serverList.add(new Server("1","localhost"));
        serverList.add(new Server("2","47.104.104.116"));
        serverProvider = new ServerProvider(serverList,this);
        listContainer.setItemProvider(serverProvider);
    }
    //删除服务器
    public void showdeleteDialog(int i){
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitleText("    警告");
        dialog.setContentText("    确定要删除吗？");
        dialog.setAutoClosable(true);
        dialog.setButton(1, "确认", (idialog,id) -> {
            serverList.remove(i);
            listContainer.setItemProvider(new ServerProvider(serverList,ServerSlice.this));
            dialog.remove();
        });

        dialog.show();
    }

    public void showhdh(){
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitleText("    曹孟德");
        dialog.setContentText("    服务器资源管理");
        dialog.setAutoClosable(true);
        dialog.show();
    }
    //添加服务器
    public void showDialog()
    {
        CommonDialog dialog = new CommonDialog(this);
        DirectionalLayout dl = (DirectionalLayout) LayoutScatter.getInstance(this).parse(ResourceTable.Layout_item_dialog, null, false);

        Text title = (Text) dl.findComponentById(ResourceTable.Id_title);
        TextField  name = (TextField ) dl.findComponentById(ResourceTable.Id_name);
        TextField  ip = (TextField ) dl.findComponentById(ResourceTable.Id_ip);

        Button btnconfirm = (Button) dl.findComponentById(ResourceTable.Id_confirm);
        dialog.setContentCustomComponent(dl);
        dialog.setAutoClosable(true);
        btnconfirm.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                String nam = name.getText();
                String IP = ip.getText();
                server.put(nam,IP);
                serverList.add(new Server(nam,IP));
                listContainer.setItemProvider(new ServerProvider(serverList,ServerSlice.this));
                dialog.remove();
            }
        });
        //让弹框展示出来
        dialog.show();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
