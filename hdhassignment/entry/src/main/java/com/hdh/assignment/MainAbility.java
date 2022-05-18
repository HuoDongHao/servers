package com.hdh.assignment;

import com.hdh.assignment.bean.Server;
import com.hdh.assignment.slice.ServerSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

import java.util.ArrayList;

public class MainAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ServerSlice.class.getName());
    }
}
