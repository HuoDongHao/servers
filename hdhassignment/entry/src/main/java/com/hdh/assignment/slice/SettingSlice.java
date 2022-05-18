package com.hdh.assignment.slice;

import com.hdh.assignment.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.window.dialog.CommonDialog;

import java.util.Random;

public class SettingSlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_setting);
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

        Button btnSet = (Button) findComponentById(ResourceTable.Id_set);
        btnSet.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Text back = (Text) findComponentById(ResourceTable.Id_back);
                ShapeElement background = new ShapeElement();
                Random random = new Random();
                int r = random.nextInt(255);
                int g = random.nextInt(255);
                int b = random.nextInt(255);
                background.setRgbColor(new RgbColor(r,g,b));
                back.setBackground(background);
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

    public void showhdh(){
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitleText("    曹孟德");
        dialog.setContentText("    服务器资源管理");
        dialog.setAutoClosable(true);
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
