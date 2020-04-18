/*
 * Copyright (C) 2016 AriaLyy(https://github.com/AriaLyy/Aria)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arialyy.aria.core;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.PopupWindow;
import com.arialyy.aria.core.command.CommandManager;
import com.arialyy.aria.core.common.QueueMod;
import com.arialyy.aria.core.config.AppConfig;
import com.arialyy.aria.core.config.DGroupConfig;
import com.arialyy.aria.core.config.DownloadConfig;
import com.arialyy.aria.core.config.UploadConfig;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.download.DownloadGroupEntity;
import com.arialyy.aria.core.download.DownloadReceiver;
import com.arialyy.aria.core.inf.AbsReceiver;
import com.arialyy.aria.core.inf.IReceiver;
import com.arialyy.aria.core.loader.IRecordHandler;
import com.arialyy.aria.core.inf.ReceiverType;
import com.arialyy.aria.core.upload.UploadEntity;
import com.arialyy.aria.core.upload.UploadReceiver;
import com.arialyy.aria.orm.DbEntity;
import com.arialyy.aria.orm.DelegateWrapper;
import com.arialyy.aria.util.ALog;
import com.arialyy.aria.util.AriaCrashHandler;
import com.arialyy.aria.util.DeleteDGRecord;
import com.arialyy.aria.util.DeleteURecord;
import com.arialyy.aria.util.RecordUtil;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lyy on 2016/12/1. https://github.com/AriaLyy/Aria
 * Aria管理器，任务操作在这里执行
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) public class AriaManager {
  private static final String TAG = "AriaManager";
  private static final Object LOCK = new Object();

  /**
   * android、androidx、support的fragment、dialogFragment类名
   */
  private static List<String> mFragmentClassName = new ArrayList<>();
  private static List<String> mDialogFragmentClassName = new ArrayList<>();

  @SuppressLint("StaticFieldLeak") private static volatile AriaManager INSTANCE = null;
  private Map<String, AbsReceiver> mReceivers = new ConcurrentHashMap<>();
  /**
   * activity 和其Dialog、Fragment的映射表
   */
  private Map<String, List<String>> mSubClass = new ConcurrentHashMap<>();
  private static Context APP;
  private DelegateWrapper mDbWrapper;
  private AriaConfig mConfig;

  static {
    mFragmentClassName.add("androidx.fragment.app.Fragment");
    mFragmentClassName.add("androidx.fragment.app.DialogFragment");
    mFragmentClassName.add("android.app.Fragment");
    mFragmentClassName.add("android.app.DialogFragment");
    mFragmentClassName.add("android.support.v4.app.Fragment");
    mFragmentClassName.add("android.support.v4.app.DialogFragment");

    mDialogFragmentClassName.add("androidx.fragment.app.DialogFragment");
    mDialogFragmentClassName.add("android.app.DialogFragment");
    mDialogFragmentClassName.add("android.support.v4.app.DialogFragment");
  }

  private AriaManager(Context context) {
    APP = context.getApplicationContext();
  }

  public static AriaManager getInstance() {
    return INSTANCE;
  }

  static AriaManager init(Context context) {
    if (INSTANCE == null) {
      synchronized (LOCK) {
        if (INSTANCE == null) {
          INSTANCE = new AriaManager(context);
          INSTANCE.initData();
        }
      }
    }
    return INSTANCE;
  }

  private void initData() {
    mConfig = AriaConfig.init(APP);
    initDb(APP);
    regAppLifeCallback(APP);
    initAria();
  }

  public Context getAPP() {
    return APP;
  }

  /**
   * 初始化数据库
   */
  private void initDb(Context context) {
    String oldDbName = "AriaLyyDb";
    File oldDbFile = context.getDatabasePath(oldDbName);
    if (oldDbFile != null && oldDbFile.exists()) {
      File dbConfig = new File(String.format("%s/%s", oldDbFile.getParent(), "AriaLyyDb-journal"));
      oldDbFile.renameTo(new File(String.format("%s/%s", oldDbFile.getParent(), "AndroidAria.db")));
      // 如果数据库是在/data/data/{packagename}/databases/下面，journal文件因权限问题将无法删除和重命名
      if (dbConfig.exists()) {
        dbConfig.delete();
      }
    }
    mDbWrapper = DelegateWrapper.init(context.getApplicationContext());
    amendTaskState();
  }

  private void initAria() {
    AppConfig appConfig = mConfig.getAConfig();
    if (appConfig.getUseAriaCrashHandler()) {
      Thread.setDefaultUncaughtExceptionHandler(new AriaCrashHandler());
    }
    appConfig.setLogLevel(appConfig.getLogLevel());
    CommandManager.init();
  }

  /**
   * 修正任务状态
   */
  private void amendTaskState() {
    Class[] clazzs = new Class[] {
        DownloadEntity.class, UploadEntity.class, DownloadGroupEntity.class
    };
    String sql = "UPDATE %s SET state=2 WHERE state IN (4,5,6)";
    for (Class clazz : clazzs) {
      if (!mDbWrapper.tableExists(clazz)) {
        continue;
      }
      String temp = String.format(sql, clazz.getSimpleName());
      DbEntity.exeSql(temp);
    }
  }

  public Map<String, AbsReceiver> getReceiver() {
    return mReceivers;
  }

  /**
   * 设置上传任务的执行队列类型，后续版本会删除该api，请使用：
   * <pre>
   *   <code>
   *     Aria.get(this).getUploadConfig().setQueueMod(mod.tag)
   *   </code>
   * </pre>
   *
   * @param mod {@link QueueMod}
   * @deprecated 后续版本会删除该api
   */
  @Deprecated public AriaManager setUploadQueueMod(QueueMod mod) {
    mConfig.getUConfig().setQueueMod(mod.tag);
    return this;
  }

  /**
   * 设置下载任务的执行队列类型，后续版本会删除该api，请使用：
   * <pre>
   *   <code>
   *     Aria.get(this).getDownloadConfig().setQueueMod(mod.tag)
   *   </code>
   * </pre>
   *
   * @param mod {@link QueueMod}
   * @deprecated 后续版本会删除该api
   */
  @Deprecated public AriaManager setDownloadQueueMod(QueueMod mod) {
    mConfig.getDConfig().setQueueMod(mod.tag);
    return this;
  }

  /**
   * 如果需要在代码中修改下载配置，请使用以下方法
   * <pre>
   *   <code>
   *     //修改最大任务队列数
   *     Aria.get(this).getDownloadConfig().setMaxTaskNum(3);
   *   </code>
   * </pre>
   */
  public DownloadConfig getDownloadConfig() {
    return mConfig.getDConfig();
  }

  /**
   * 如果需要在代码中修改下载配置，请使用以下方法
   * <pre>
   *   <code>
   *     //修改最大任务队列数
   *     Aria.get(this).getUploadConfig().setMaxTaskNum(3);
   *   </code>
   * </pre>
   */
  public UploadConfig getUploadConfig() {
    return mConfig.getUConfig();
  }

  /**
   * 获取APP配置
   */
  public AppConfig getAppConfig() {
    return mConfig.getAConfig();
  }

  /**
   * 如果需要在代码中修改下载类型的组合任务的配置，请使用以下方法
   * <pre>
   *   <code>
   *     //修改最大任务队列数
   *     Aria.get(this).getDownloadConfig().setMaxTaskNum(3);
   *   </code>
   * </pre>
   */
  public DGroupConfig getDGroupConfig() {
    return mConfig.getDGConfig();
  }

  /**
   * 处理下载操作
   */
  DownloadReceiver download(Object obj) {
    IReceiver receiver = mReceivers.get(getKey(ReceiverType.DOWNLOAD, obj));
    if (receiver == null) {
      receiver = putReceiver(ReceiverType.DOWNLOAD, obj);
    }
    return (receiver instanceof DownloadReceiver) ? (DownloadReceiver) receiver : null;
  }

  /**
   * 处理上传操作
   */
  UploadReceiver upload(Object obj) {
    IReceiver receiver = mReceivers.get(getKey(ReceiverType.UPLOAD, obj));
    if (receiver == null) {
      receiver = putReceiver(ReceiverType.UPLOAD, obj);
    }
    return (receiver instanceof UploadReceiver) ? (UploadReceiver) receiver : null;
  }

  /**
   * 删除任务记录
   *
   * @param type 需要删除的任务类型，1、普通下载任务；2、组合任务；3、普通上传任务。
   * @param key type为1时，key为保存路径；type为2时，key为组合任务hash；type为3时，key为文件上传路径。
   * @param removeFile {@code true} 不仅删除任务数据库记录，还会删除已经删除完成的文件；{@code false}如果任务已经完成，只删除任务数据库记录。
   */
  public void delRecord(int type, String key, boolean removeFile) {
    switch (type) {
      case 1: // 删除普通任务记录
        RecordUtil.delTaskRecord(key, IRecordHandler.TYPE_DOWNLOAD, removeFile, true);
        break;
      case 2:
        RecordUtil.delGroupTaskRecordByHash(key, removeFile);
        break;
      case 3:
        DeleteURecord.getInstance().deleteRecord(key, removeFile, true);
        break;
    }
  }

  private IReceiver putReceiver(String type, Object obj) {
    final String key = getKey(type, obj);
    IReceiver receiver = mReceivers.get(key);
    boolean needRmReceiver = false;
    // 监控Dialog、fragment、popupWindow的生命周期
    final WidgetLiftManager widgetLiftManager = new WidgetLiftManager();
    Context subParenActivity = null;

    if (obj instanceof Dialog) {
      needRmReceiver = widgetLiftManager.handleDialogLift((Dialog) obj);
      subParenActivity = ((Dialog) obj).getOwnerActivity();
    } else if (obj instanceof PopupWindow) {
      needRmReceiver = widgetLiftManager.handlePopupWindowLift((PopupWindow) obj);
      subParenActivity = ((PopupWindow) obj).getContentView().getContext();
    } else if (isDialogFragment(obj.getClass())) {
      needRmReceiver = widgetLiftManager.handleDialogFragmentLift(getDialog(obj));
      subParenActivity = getFragmentActivity(obj);
    } else if (isFragment(obj.getClass())) {
      subParenActivity = getFragmentActivity(obj);
    }

    if (subParenActivity instanceof Activity) {
      relateSubClass(type, obj, (Activity) subParenActivity);
    }

    if (receiver == null) {
      AbsReceiver absReceiver =
          type.equals(ReceiverType.DOWNLOAD) ? new DownloadReceiver() : new UploadReceiver();
      absReceiver.targetName = obj.getClass().getName();
      AbsReceiver.OBJ_MAP.put(absReceiver.getKey(), obj);
      absReceiver.needRmListener = needRmReceiver;
      mReceivers.put(key, absReceiver);
      receiver = absReceiver;
    }
    return receiver;
  }

  /**
   * 获取fragment的activity
   *
   * @return 获取失败，返回null
   */
  static Activity getFragmentActivity(Object obj) {
    try {
      Method method = obj.getClass().getMethod("getActivity");
      return (Activity) method.invoke(obj);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 判断注解对象是否是fragment
   *
   * @return true 对象是fragment
   */
  static boolean isFragment(Class subClazz) {
    Class parentClass = subClazz.getSuperclass();
    if (parentClass == null) {
      return false;
    } else {
      String parentName = parentClass.getName();
      if (mFragmentClassName.contains(parentName)) {
        return true;
      } else {
        return isFragment(parentClass);
      }
    }
  }

  /**
   * 判断对象是否是DialogFragment
   *
   * @return true 对象是DialogFragment
   */
  private boolean isDialogFragment(Class subClazz) {
    Class parentClass = subClazz.getSuperclass();
    if (parentClass == null) {
      return false;
    } else {
      String parentName = parentClass.getName();
      if (mDialogFragmentClassName.contains(parentName)) {
        return true;
      } else {
        return isDialogFragment(parentClass);
      }
    }
  }

  /**
   * 获取DialogFragment的dialog
   *
   * @return 获取失败，返回null
   */
  private Dialog getDialog(Object obj) {
    try {
      Method method = obj.getClass().getMethod("getDialog");
      return (Dialog) method.invoke(obj);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 关联Activity类和Fragment间的关系
   *
   * @param sub Fragment或dialog类
   * @param activity activity寄主类
   */
  private void relateSubClass(String type, Object sub, Activity activity) {
    String key = getKey(type, activity);
    List<String> subClass = mSubClass.get(key);
    if (subClass == null) {
      subClass = new ArrayList<>();
      mSubClass.put(key, subClass);
    }
    subClass.add(getKey(type, sub));
    if (mReceivers.get(key) == null) { // 将activity填充进去
      mReceivers.put(key, new DownloadReceiver());
    }
  }

  /**
   * 根据功能类型和控件类型获取对应的key
   *
   * @param type {@link ReceiverType}
   * @param obj 观察者对象
   * @return key的格式为：{@code String.format("%s_%s_%s", obj.getClass().getName(), type,
   * obj.hashCode());}
   */
  private String getKey(String type, Object obj) {
    return String.format("%s_%s_%s", obj.getClass().getName(), type, obj.hashCode());
  }

  /**
   * 注册APP生命周期回调
   */
  private void regAppLifeCallback(Context context) {
    Context app = context.getApplicationContext();
    if (app instanceof Application) {
      LifeCallback lifeCallback = new LifeCallback();
      ((Application) app).registerActivityLifecycleCallbacks(lifeCallback);
    }
  }

  /**
   * 移除指定对象的receiver
   */
  public void removeReceiver(Object obj) {
    if (obj == null) {
      ALog.e(TAG, "target obj is null");
      return;
    }
    List<String> temp = new ArrayList<>();
    // 移除寄主的receiver
    for (Iterator<Map.Entry<String, AbsReceiver>> iter = mReceivers.entrySet().iterator();
        iter.hasNext(); ) {
      Map.Entry<String, AbsReceiver> entry = iter.next();
      String key = entry.getKey();
      if (key.equals(getKey(ReceiverType.DOWNLOAD, obj)) || key.equals(
          getKey(ReceiverType.UPLOAD, obj))) {
        AbsReceiver receiver = mReceivers.get(key);
        List<String> subNames = mSubClass.get(key);
        if (subNames != null && !subNames.isEmpty()) {
          temp.addAll(subNames);
        }
        if (receiver != null) {
          receiver.destroy();
        }
        iter.remove();
      }
    }

    // 移除寄生的receiver
    if (!temp.isEmpty()) {
      for (Iterator<Map.Entry<String, AbsReceiver>> iter = mReceivers.entrySet().iterator();
          iter.hasNext(); ) {
        Map.Entry<String, AbsReceiver> entry = iter.next();
        if (temp.contains(entry.getKey())) {
          AbsReceiver receiver = mReceivers.get(entry.getKey());
          if (receiver != null) {
            receiver.destroy();
          }
          iter.remove();
        }
      }
    }
  }

  /**
   * Activity生命周期
   */
  private class LifeCallback implements Application.ActivityLifecycleCallbacks {

    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override public void onActivityStarted(Activity activity) {

    }

    @Override public void onActivityResumed(Activity activity) {

    }

    @Override public void onActivityPaused(Activity activity) {

    }

    @Override public void onActivityStopped(Activity activity) {

    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override public void onActivityDestroyed(Activity activity) {
      removeReceiver(activity);
    }
  }
}
