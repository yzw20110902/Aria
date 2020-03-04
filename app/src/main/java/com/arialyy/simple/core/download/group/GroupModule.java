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
package com.arialyy.simple.core.download.group;

import android.content.Context;
import com.arialyy.simple.R;
import com.arialyy.simple.base.BaseModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lyy on 2017/7/6.
 */
public class GroupModule extends BaseModule {
  public GroupModule(Context context) {
    super(context);
  }

  public List<String> getUrls() {
    List<String> urls = new ArrayList<>();
    String[] str = getContext().getResources().getStringArray(R.array.group_urls_1);
    Collections.addAll(urls, str);
    //urls.add("https://scontent-hkt1-1.xx.fbcdn.net/v/t1.0-9/fr/cp0/e15/q65/85148967_168821394550033_6164348723502514176_n.jpg?_nc_cat=105&_nc_sid=110474&efg=eyJpIjoidCJ9&_nc_oc=AQni9wIDOcxkQGfqH7jFwLuRWV3cIi0We11MXp1V0_vfrQ3-SBV4Ue034aJXzzIzdNI&_nc_ht=scontent-hkt1-1.xx&_nc_tp=14&oh=37829bbfd4f7179fcc3527a7a7971f53&oe=5F00FC35");
    //urls.add("https://scontent-hkt1-1.xx.fbcdn.net/v/t1.0-0/cp0/e15/q65/p480x480/84206444_168821471216692_7979166053252988928_n.jpg?_nc_cat=106&_nc_sid=110474&efg=eyJpIjoidCJ9&_nc_oc=AQny1ZTkxw-sq4mSH2TThaQNRJB89uKG2cW9e3DgcI78qXE7b8Jlyox749lXkcngYBs&_nc_ht=scontent-hkt1-1.xx&_nc_tp=3&oh=6714e2c9b3d76e12eff960b6aa22cdfb&oe=5EEE640F");
    //urls.add("https://scontent-hkt1-1.xx.fbcdn.net/v/t1.0-0/cp0/e15/q65/p480x480/86702402_168821534550019_6064732312896012288_n.jpg?_nc_cat=101&_nc_sid=110474&efg=eyJpIjoidCJ9&_nc_oc=AQnfJfW55cqVfs0lzZOjEa8kGoxvrA--\\brjaelZgcf8MzqQUhjH3moqGoekMQlATXRM&_nc_ht=scontent-hkt1-1.xx&_nc_tp=3&oh=96f4f776c05b2c200cfb720fa36f938b&oe=5EFA310D");
    //urls.add("https://scontent-hkt1-1.xx.fbcdn.net/v/t1.0-0/cp0/e15/q65/p480x480/85236032_168821577883348_2800973234302877696_n.jpg?_nc_cat=106&_nc_sid=110474&efg=eyJpIjoidCJ9&_nc_oc=AQlNvaTlYUspquMWBLnkgVOHdzG4pX2XW3NSHYxvVYsaJF8Ehy9tGn6R6Ned5UwcwAM&_nc_ht=scontent-hkt1-1.xx&_nc_tp=3&oh=6fd5e6442712e4da111d09e8902c68c5&oe=5EFE1528");
    //urls.add("https://scontent-hkt1-1.xx.fbcdn.net/v/t1.0-1/cp0/e15/q65/p120x120/80398195_141232180642288_3692820603451998208_n.jpg?_nc_cat=109&_nc_sid=dbb9e7&efg=eyJpIjoidCJ9&_nc_oc=AQmsZxgvLgoObkqHbcKS8qIZ3w_QhssRwLSF5o-8P6q_kf962w32kmjLqcg6lH9qxWI&_nc_ht=scontent-hkt1-1.xx&_nc_tp=3&oh=269e655e7b901e8d07d9362142a0fdda&oe=5EF43734");

    return urls;
  }

  List<String> getUrls1() {
    List<String> urls = new ArrayList<>();
    //String[] str = getContext().getResources().getStringArray(R.array.group_urls);
    //Collections.addAll(urls, str);
    urls.add("https://d.pcs.baidu.com/file/130335545f3f4d9cc38afe709c19af5a?fid=1411168371-250528-1010657263806840&dstime=1531134607&rt=sh&sign=FDtAERVY-DCb740ccc5511e5e8fedcff06b081203-sNCujT7lC42aMcfiHcgqAzYHuw4%3D&expires=8h&chkv=1&chkbd=0&chkpc=et&dp-logid=4401967667009194668&dp-callid=0&r=540192514");
    return urls;
  }

  List<String> getSubName() {
    List<String> names = new ArrayList<>();
    String[] str = getContext().getResources().getStringArray(R.array.group_names);
    Collections.addAll(names, str);
    //names.add("1.png");
    //names.add("2.png");
    //names.add("3.png");
    //names.add("4.png");
    //names.add("5.png");
    return names;
  }

  List<String> getSubName1() {
    List<String> names = new ArrayList<>();
    String[] str = getContext().getResources().getStringArray(R.array.group_names);
    Collections.addAll(names, str);
    return names;
  }

  List<String> getSubName2() {
    List<String> taskSubFile;
    taskSubFile = new ArrayList<>();
    //taskSubFile.add("2156.mp4");
    //        taskSubFile.add("2115.mp4");
    //taskSubFile.add("2009.mp4");
    //taskSubFile.add("1893.mp4");
    taskSubFile.add("1952.mp4");
    taskSubFile.add("1958.mp4");
    taskSubFile.add("1994.mp4");
    //taskSubFile.add("2083.mp4");
    taskSubFile.add("2305.JPG");
    taskSubFile.add("2183.JPG");
    taskSubFile.add("2154.JPG");
    taskSubFile.add("2153.JPG");
    taskSubFile.add("2152.JPG");
    taskSubFile.add("2151.JPG");
    taskSubFile.add("2149.JPG");
    taskSubFile.add("2148.JPG");
    taskSubFile.add("2147.JPG");
    taskSubFile.add("2146.JPG");
    taskSubFile.add("1949.JPG");
    taskSubFile.add("1887.JPG");
    taskSubFile.add("1996.txt");
    return taskSubFile;
  }

  public List<String> getUrls2() {
    List<String> downLoadUrls;
    downLoadUrls = new ArrayList<>();
    //downLoadUrls.add(
    //    "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2156&clientId=A000011106034058176");
    //        downLoadUrls.add("http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2115&clientId=A000011106034058176");
    //downLoadUrls.add(
    //    "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2009&clientId=A000011106034058176");
    //downLoadUrls.add(
    //    "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=1893&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=1952&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=1958&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=1994&clientId=A000011106034058176");
    //downLoadUrls.add(
    //    "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2083&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2305&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2183&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2154&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2153&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2152&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2151&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2149&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2148&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2147&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=2146&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=1949&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=1887&clientId=A000011106034058176");
    downLoadUrls.add(
        "http://d.quanscreen.com/k/down/resourceDownLoad?resourceId=1996&clientId=A000011106034058176");
    return downLoadUrls;
  }
}
