/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.calculator2;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.content.Context;

/**
 * 存储历史记录
 * 使用文件calculator.data记录。
 * 使用版本号控制
 * @author Administrator
 *
 */
class Persist {
    private static final int LAST_VERSION = 1;
    private static final String FILE_NAME = "calculator.data";
    private Context mContext;

    History history = new History();
    /**
     * 在构造的时候进行读取文件加载历史记录
     * 
     * @param context
     */
    Persist(Context context) {
        this.mContext = context;
        load();
    }

    /**
     * 加载文件中的历史记录(dataInputStream)，放去History中。
     */
    private void load() {
        try {
            InputStream is = new BufferedInputStream(mContext.openFileInput(FILE_NAME), 8192);
            DataInputStream in = new DataInputStream(is);
            int version = in.readInt();
            if (version > LAST_VERSION) {
                throw new IOException("data version " + version + "; expected " + LAST_VERSION);
            }
            history = new History(version, in);
            in.close();
        } catch (FileNotFoundException e) {
            Calculator.log("" + e);
        } catch (IOException e) {
            Calculator.log("" + e);
        }
    }
    /**
     * 把历史记录存储到文件中 通过History的write方法，把记录写入out中。
     */
    void save() {
        try {
            OutputStream os = new BufferedOutputStream(mContext.openFileOutput(FILE_NAME, 0), 8192);
            DataOutputStream out = new DataOutputStream(os);
            out.writeInt(LAST_VERSION);
            history.write(out);
            out.close();
        } catch (IOException e) {
            Calculator.log("" + e);
        } 
    }
}
