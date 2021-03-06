package com.jiangyy.action;

import com.alibaba.fastjson.JSON;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jiangyy.entity.Repository;
import com.jiangyy.ui.HomeDialog;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.jiangyy.constant.Constant.KEY;
import static com.jiangyy.entity.DataKt.JSON_STR;

public class AutoGradleAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        String localData =  PropertiesComponent.getInstance().getValue(KEY);

        List<Repository> data,data0;

        if(localData == null || localData.isEmpty()){
            data = JSON.parseArray(JSON_STR, Repository.class);
            data0 = JSON.parseArray(JSON_STR, Repository.class);
        }else{
            data = JSON.parseArray(localData, Repository.class);
            data0 = JSON.parseArray(localData, Repository.class);
        }
        if (data == null || data0 == null) {
            showNotification(e, "error", "提示", "项目列表解析失败");
        } else {
            HomeDialog dialog = new HomeDialog(e, data, data0);
            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private void showNotification(@NotNull AnActionEvent e, String displayId, String title, String message) {
        NotificationGroup notificationGroup = new NotificationGroup(displayId, NotificationDisplayType.BALLOON, true);
        notificationGroup.createNotification(
                title,
                message,
                NotificationType.INFORMATION,
                null
        ).notify(e.getProject());
    }

}
