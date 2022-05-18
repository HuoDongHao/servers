package com.hdh.assignment.slice;

import com.hdh.assignment.ResourceTable;
import com.hdh.assignment.untils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.CommonDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shapes2Slice extends AbilitySlice {
    private int total = 828;
    private static final String TAG = Shapes2Slice.class.getSimpleName();

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, TAG);

    private long rx;
    private long tx;
    private NetManager netManager;

    Text c1,c2,c3,c12,c22,c32;
    private double sh = 0,uh = 0, av = 0, us = 0;


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_shape);
        //服务器
        Component enterServer = findComponentById(ResourceTable.Id_server);
        enterServer.setClickedListener(listener -> present(new ServerSlice() , new Intent()));
        //图形
        Component enterShape = findComponentById(ResourceTable.Id_shape);
        enterShape.setClickedListener(listener -> present(new Shapes2Slice() , new Intent()));
        //进程
        Component enterProcess = findComponentById(ResourceTable.Id_process);
        enterProcess.setClickedListener(listener -> present(new Process2Slice() , new Intent()));
        //主题
        Component enterSetting = findComponentById(ResourceTable.Id_setting);
        enterSetting.setClickedListener(listener -> present(new SettingSlice() , new Intent()));

        c1 = findComponentById(ResourceTable.Id_c1);
        c2 = findComponentById(ResourceTable.Id_c2);
        c3 = findComponentById(ResourceTable.Id_c3);
        c12 = findComponentById(ResourceTable.Id_c12);
        c22 = findComponentById(ResourceTable.Id_c22);
        c32 = findComponentById(ResourceTable.Id_c32);

        Button btnFlush = (Button) findComponentById(ResourceTable.Id_flush);
        btnFlush.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                synchronized (btnFlush){
                    flush(component);
                }
                while(uh == 0);
                c1.setText(String.valueOf(uh+sh));
                c1.setHeight((int)(total*0.01*(sh+uh)));
                c2.setText(String.valueOf(sh));
                c2.setHeight((int)(total*0.01*sh));
                c3.setText(String.valueOf(uh));
                c3.setHeight((int)(total*0.01*uh));
                uh = 0;
                while(av == 0);
                c22.setText(String.valueOf(us/(av+us)*100));
                HiLog.warn(LABEL_LOG,String.valueOf(total));
                c22.setWidth((int)(total*(us/(av+us))));
                c32.setText(String.valueOf(av/(us+av)*100));
                c32.setWidth((int)(total*(av/(us+av))));
                c12.setText("100");
                c12.setWidth(total);
                av = 0;
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

    public void flush(Component component){
        netManager = NetManager.getInstance(null);
        if (!netManager.hasDefaultNet()) {
            return;
        }
        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            netManager.addDefaultNetStatusCallback(callback);
            HttpURLConnection connection = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                String urlString = "http://47.104.104.116:61208/api/3/cpu";//IP地址
                URL url = new URL(urlString);
                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                if (urlConnection instanceof HttpURLConnection) {
                    connection = (HttpURLConnection) urlConnection;
                }
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setDoOutput(false);
                HiLog.warn(LABEL_LOG,"1");
                connection.connect();
                HiLog.warn(LABEL_LOG,"2");
                trafficDataStatistics(false);
                HiLog.warn(LABEL_LOG,"3");
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    HiLog.warn(LABEL_LOG,"4");
                    byte[] cache = new byte[2 * 1024];
                    int len = inputStream.read(cache);
                    HiLog.warn(LABEL_LOG,"5");
                    while (len != -1) {
                        HiLog.warn(LABEL_LOG,"6");
                        outputStream.write(cache, 0, len);
                        len = inputStream.read(cache);
                    }
                    HiLog.warn(LABEL_LOG,"7");
                } catch (IOException e) {
                    HiLog.error(LABEL_LOG, "%{public}s", "netRequest inner IOException");
                }
                String result = new String(outputStream.toByteArray());
                HiLog.warn(LABEL_LOG,"8");
                String line = result;
                HiLog.warn(LABEL_LOG,"9");

                //cpu使用
                Matcher sys = Pattern.compile(".*system\": ([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])").matcher(line);
                Matcher user = Pattern.compile(".*user\": ([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])").matcher(line);
                //内存使用
                Matcher used = Pattern.compile(".*used\": ([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])").matcher(line);
                Matcher available = Pattern.compile(".*available\": ([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])").matcher(line);

                HiLog.warn(LABEL_LOG,"10");
                //改变d1
                HiLog.warn(LABEL_LOG,line);
                if (sys.find()) {
                    sh = Double.parseDouble(sys.group(1));
                }
                if (user.find()) {
                    HiLog.warn(LABEL_LOG,"11.2");
                    uh = Double.parseDouble(user.group(1));
                }
                //改变d2
                HiLog.warn(LABEL_LOG,"12");
                if (used.find() && available.find()) {
                    us = Double.parseDouble(used.group(1));
                    av = Double.parseDouble(available.group(1));
                }
                trafficDataStatistics(true);
                connection.disconnect();
            } catch (IOException e) {
                HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
            }
        });
        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            netManager.addDefaultNetStatusCallback(callback);
            HttpURLConnection connection = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                String urlString = "http://47.104.104.116:61208/api/3/mem";//IP地址
                URL url = new URL(urlString);
                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                if (urlConnection instanceof HttpURLConnection) {
                    connection = (HttpURLConnection) urlConnection;
                }
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setDoOutput(false);
                HiLog.warn(LABEL_LOG,"1");
                connection.connect();
                HiLog.warn(LABEL_LOG,"2");
                trafficDataStatistics(false);
                HiLog.warn(LABEL_LOG,"3");
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    HiLog.warn(LABEL_LOG,"4");
                    byte[] cache = new byte[2 * 1024];
                    int len = inputStream.read(cache);
                    HiLog.warn(LABEL_LOG,"5");
                    while (len != -1) {
                        HiLog.warn(LABEL_LOG,"6");
                        outputStream.write(cache, 0, len);
                        len = inputStream.read(cache);
                    }
                    HiLog.warn(LABEL_LOG,"7");
                } catch (IOException e) {
                    HiLog.error(LABEL_LOG, "%{public}s", "netRequest inner IOException");
                }
                String result = new String(outputStream.toByteArray());
                HiLog.warn(LABEL_LOG,"8");
                String line = result;
                HiLog.warn(LABEL_LOG,"9");

                //cpu使用
                Matcher sys = Pattern.compile(".*system\": ([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])").matcher(line);
                Matcher user = Pattern.compile(".*user\": ([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])").matcher(line);
                //内存使用
                Matcher used = Pattern.compile(".*used\": ([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])").matcher(line);
                Matcher available = Pattern.compile(".*available\": ([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])").matcher(line);

                HiLog.warn(LABEL_LOG,"10");
                //改变d1
                HiLog.warn(LABEL_LOG,line);
                if (sys.find()) {
                    sh = Double.parseDouble(sys.group(1));
                }
                if (user.find()) {
                    HiLog.warn(LABEL_LOG,"11.2");
                    uh = Double.parseDouble(user.group(1));
                }
                //改变d2

                if (used.find() && available.find()) {
                    HiLog.warn(LABEL_LOG,used.group(1));
                    us = Double.parseDouble(used.group(1));
                    HiLog.warn(LABEL_LOG,available.group(1));
                    av = Double.parseDouble(available.group(1));
                }
                trafficDataStatistics(true);
                HttpResponseCache.getInstalled().flush();
                connection.disconnect();
            } catch (IOException e) {
                HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
            }
        });
    }

    private final NetStatusCallback callback = new NetStatusCallback() {
        @Override
        public void onAvailable(NetHandle handle) {
            HiLog.info(LABEL_LOG, "%{public}s", "NetStatusCallback onAvailable");
        }

        @Override
        public void onBlockedStatusChanged(NetHandle handle, boolean blocked) {
            HiLog.info(LABEL_LOG, "%{public}s", "NetStatusCallback onBlockedStatusChanged");
        }
    };

    private void trafficDataStatistics(boolean isStart) {
        int uid = 0;
        if (isStart) {
            rx = DataFlowStatistics.getUidRxBytes(uid);
            tx = DataFlowStatistics.getUidTxBytes(uid);
        } else {
            rx = DataFlowStatistics.getUidRxBytes(uid) - rx;
            tx = DataFlowStatistics.getUidTxBytes(uid) - tx;
        }
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
