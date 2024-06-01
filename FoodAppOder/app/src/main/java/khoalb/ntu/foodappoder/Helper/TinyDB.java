/*
 * Copyright 2014 KC Ochibili
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 *  The "‚‗‚" character is not a comma, it is the SINGLE LOW-9 QUOTATION MARK unicode 201A
 *  and unicode 2017 that are used for separating the items in a list.
 */

package khoalb.ntu.foodappoder.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import khoalb.ntu.foodappoder.Domain.Foods;


public class TinyDB {

    private SharedPreferences preferences;
    private String DEFAULT_APP_IMAGEDATA_DIRECTORY;
    private String lastImagePath = "";

    public TinyDB(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    /**
     * Giải mã Bitmap từ 'đường dẫn' và trả về nó
     * @param path đường dẫn hình ảnh
     * @return Bitmap từ 'đường dẫn'
     */

    public Bitmap getImage(String path) {
        Bitmap bitmapFromPath = null;
        try {
            bitmapFromPath = BitmapFactory.decodeFile(path);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return bitmapFromPath;
    }

    /**
     * Trả về đường dẫn String của hình ảnh đã lưu gần đây nhất
     * @return đường dẫn string của hình ảnh đã lưu gần đây nhất
     */

    public String getSavedImagePath() {
        return lastImagePath;
    }

    /**
     * Lưu 'theBitmap' vào thư mục 'theFolder' với tên 'theImageName'
     * @param theFolder đường dẫn thư mục bạn muốn lưu, ví dụ: "DropBox/WorkImages"
     * @param theImageName tên bạn muốn đặt cho tệp hình ảnh, ví dụ: "MeAtLunch.png"
     * @param theBitmap hình ảnh bạn muốn lưu dưới dạng Bitmap
     * @return trả về đường dẫn đầy đủ (địa chỉ hệ thống tệp) của hình ảnh đã lưu
     */

    public String putImage(String theFolder, String theImageName, Bitmap theBitmap) {
        if (theFolder == null || theImageName == null || theBitmap == null)
            return null;

        this.DEFAULT_APP_IMAGEDATA_DIRECTORY = theFolder;
        String mFullPath = setupFullPath(theImageName);

        if (!mFullPath.equals("")) {
            lastImagePath = mFullPath;
            saveBitmap(mFullPath, theBitmap);
        }

        return mFullPath;
    }

    /**
     * Lưu 'theBitmap' vào 'fullPath'
     * @param fullPath đường dẫn đầy đủ của tệp hình ảnh, ví dụ: "Images/MeAtLunch.png"
     * @param theBitmap hình ảnh bạn muốn lưu dưới dạng Bitmap
     * @return true nếu hình ảnh đã được lưu, ngược lại trả về false
     */

    public boolean putImageWithFullPath(String fullPath, Bitmap theBitmap) {
        return !(fullPath == null || theBitmap == null) && saveBitmap(fullPath, theBitmap);
    }

    /**
     * Tạo đường dẫn cho hình ảnh với tên 'imageName' trong thư mục DEFAULT_APP..
     * @param imageName tên của hình ảnh
     * @return đường dẫn đầy đủ của hình ảnh. Nếu không tạo được thư mục, trả về chuỗi rỗng
     */

    private String setupFullPath(String imageName) {
        File mFolder = new File(Environment.getExternalStorageDirectory(), DEFAULT_APP_IMAGEDATA_DIRECTORY);

        if (isExternalStorageReadable() && isExternalStorageWritable() && !mFolder.exists()) {
            if (!mFolder.mkdirs()) {
                Log.e("ERROR", "Failed to setup folder");
                return "";
            }
        }

        return mFolder.getPath() + '/' + imageName;
    }

    /**
     * Lưu Bitmap dưới dạng tệp PNG tại đường dẫn 'fullPath'
     * @param fullPath đường dẫn của tệp hình ảnh
     * @param bitmap hình ảnh dưới dạng Bitmap
     * @return true nếu lưu thành công, ngược lại trả về false
     */

    private boolean saveBitmap(String fullPath, Bitmap bitmap) {
        if (fullPath == null || bitmap == null)
            return false;

        boolean fileCreated = false;
        boolean bitmapCompressed = false;
        boolean streamClosed = false;

        File imageFile = new File(fullPath);

        if (imageFile.exists())
            if (!imageFile.delete())
                return false;

        try {
            fileCreated = imageFile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imageFile);
            bitmapCompressed = bitmap.compress(CompressFormat.PNG, 100, out);

        } catch (Exception e) {
            e.printStackTrace();
            bitmapCompressed = false;

        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                    streamClosed = true;

                } catch (IOException e) {
                    e.printStackTrace();
                    streamClosed = false;
                }
            }
        }

        return (fileCreated && bitmapCompressed && streamClosed);
    }

    // Getters

    /**
     * Lấy giá trị int từ SharedPreferences tại 'key'. Nếu không tìm thấy key, trả về 0
     * @param key key của SharedPreferences
     * @return giá trị int tại 'key' hoặc 0 nếu không tìm thấy key
     */

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * Lấy danh sách ArrayList của các số nguyên đã được phân tích từ SharedPreferences tại 'key'
     * @param key khóa của SharedPreferences
     * @return ArrayList của các số nguyên
     */

    public ArrayList<Integer> getListInt(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Integer> newList = new ArrayList<Integer>();

        for (String item : arrayToList)
            newList.add(Integer.parseInt(item));

        return newList;
    }

    /**
     * Lấy giá trị long từ SharedPreferences tại 'key'. Nếu không tìm thấy key, trả về 0
     * @param key khóa của SharedPreferences
     * @return giá trị long tại 'key' hoặc 0 nếu không tìm thấy key
     */

    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

    /**
     * Lấy giá trị float từ SharedPreferences tại 'key'. Nếu không tìm thấy key, trả về 0
     * @param key khóa của SharedPreferences
     * @return giá trị float tại 'key' hoặc 0 nếu không tìm thấy key
     */

    public float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    /**
     * Lấy giá trị double từ SharedPreferences tại 'key'. Nếu có ngoại lệ xảy ra, trả về 0
     * @param key khóa của SharedPreferences
     * @return giá trị double tại 'key' hoặc 0 nếu có ngoại lệ xảy ra
     */

    public double getDouble(String key) {
        String number = getString(key);

        try {
            return Double.parseDouble(number);

        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Lấy danh sách ArrayList đã phân tích của các số Double từ SharedPreferences tại 'key'
     * @param key khóa của SharedPreferences
     * @return ArrayList của các số Double
     */

    public ArrayList<Double> getListDouble(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Double> newList = new ArrayList<Double>();

        for (String item : arrayToList)
            newList.add(Double.parseDouble(item));

        return newList;
    }

    /**
     * Lấy danh sách ArrayList đã phân tích của các số nguyên từ SharedPreferences tại 'key'
     * @param key khóa của SharedPreferences
     * @return ArrayList của các số Long
     */

    public ArrayList<Long> getListLong(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<String>(Arrays.asList(myList));
        ArrayList<Long> newList = new ArrayList<Long>();

        for (String item : arrayToList)
            newList.add(Long.parseLong(item));

        return newList;
    }

    /**
     * Lấy giá trị String từ SharedPreferences tại 'key'. Nếu không tìm thấy key, trả về "" (chuỗi rỗng)
     * @param key khóa của SharedPreferences
     * @return giá trị String tại 'key' hoặc "" (chuỗi rỗng) nếu không tìm thấy key
     */

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    /**
     * Lấy danh sách ArrayList đã phân tích của các chuỗi từ SharedPreferences tại 'key'
     * @param key khóa của SharedPreferences
     * @return ArrayList của các chuỗi
     */

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    /**
     * Lấy giá trị boolean từ SharedPreferences tại 'key'. Nếu không tìm thấy key, trả về false
     * @param key khóa của SharedPreferences
     * @return giá trị boolean tại 'key' hoặc false nếu không tìm thấy key
     */

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    /**
     * Lấy danh sách ArrayList đã phân tích của các giá trị boolean từ SharedPreferences tại 'key'
     * @param key khóa của SharedPreferences
     * @return ArrayList của các giá trị boolean
     */

    public ArrayList<Boolean> getListBoolean(String key) {
        ArrayList<String> myList = getListString(key);
        ArrayList<Boolean> newList = new ArrayList<Boolean>();

        for (String item : myList) {
            if (item.equals("true")) {
                newList.add(true);
            } else {
                newList.add(false);
            }
        }

        return newList;
    }

    public ArrayList<Foods> getListObject(String key){
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<Foods> playerList =  new ArrayList<Foods>();

        for(String jObjString : objStrings){
            Foods player  = gson.fromJson(jObjString,  Foods.class);
            playerList.add(player);
        }
        return playerList;
    }
    public <T> T getObject(String key, Class<T> classOfT){

        String json = getString(key);
        Object value = new Gson().fromJson(json, classOfT);
        if (value == null)
            throw new NullPointerException();
        return (T)value;
    }
    // Các phương thức Put

    /**
     * Đặt giá trị int vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param value giá trị int để thêm vào
     */

    public void putInt(String key, int value) {
        checkForNullKey(key);
        preferences.edit().putInt(key, value).apply();
    }

    /**
     * Đặt danh sách ArrayList của Integer vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param intList ArrayList của Integer để thêm vào
     */

    public void putListInt(String key, ArrayList<Integer> intList) {
        checkForNullKey(key);
        Integer[] myIntList = intList.toArray(new Integer[intList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply();
    }

    /**
     * Đặt giá trị long vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param value giá trị long để thêm vào
     */

    public void putLong(String key, long value) {
        checkForNullKey(key);
        preferences.edit().putLong(key, value).apply();
    }

    /**
     * Đặt danh sách ArrayList của Long vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param longList ArrayList của Long để thêm vào
     */

    public void putListLong(String key, ArrayList<Long> longList) {
        checkForNullKey(key);
        Long[] myLongList = longList.toArray(new Long[longList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myLongList)).apply();
    }

    /**
     * Đặt giá trị float vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param value giá trị float để thêm vào
     */

    public void putFloat(String key, float value) {
        checkForNullKey(key);
        preferences.edit().putFloat(key, value).apply();
    }

    /**
     * Đặt giá trị double vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param value giá trị double để thêm vào
     */

    public void putDouble(String key, double value) {
        checkForNullKey(key);
        putString(key, String.valueOf(value));
    }

    /**
     * Đặt danh sách ArrayList của Double vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param doubleList ArrayList của Double để thêm vào
     */

    public void putListDouble(String key, ArrayList<Double> doubleList) {
        checkForNullKey(key);
        Double[] myDoubleList = doubleList.toArray(new Double[doubleList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myDoubleList)).apply();
    }

    /**
     * Đặt giá trị String vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param value giá trị String để thêm vào
     */

    public void putString(String key, String value) {
        checkForNullKey(key); checkForNullValue(value);
        preferences.edit().putString(key, value).apply();
    }

    /**
     * Đặt danh sách ArrayList của String vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param stringList danh sách ArrayList của String để thêm vào
     */

    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    /**
     * Đặt giá trị boolean vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param value giá trị boolean để thêm vào
     */

    public void putBoolean(String key, boolean value) {
        checkForNullKey(key);
        preferences.edit().putBoolean(key, value).apply();
    }

    /**
     * Đặt danh sách ArrayList của Boolean vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param boolList danh sách ArrayList của Boolean để thêm vào
     */

    public void putListBoolean(String key, ArrayList<Boolean> boolList) {
        checkForNullKey(key);
        ArrayList<String> newList = new ArrayList<String>();

        for (Boolean item : boolList) {
            if (item) {
                newList.add("true");
            } else {
                newList.add("false");
            }
        }

        putListString(key, newList);
    }

    /**
     * Đặt đối tượng bất kỳ vào SharedPreferences với 'key' và lưu
     * @param key khóa của SharedPreferences
     * @param obj là đối tượng bạn muốn đặt
     */

    public void putObject(String key, Object obj){
    	checkForNullKey(key);
    	Gson gson = new Gson();
    	putString(key, gson.toJson(obj));
    }

    public void putListObject(String key, ArrayList<Foods> playerList){
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(Foods player: playerList){
            objStrings.add(gson.toJson(player));
        }
        putListString(key, objStrings);
    }

    /**
     * Xóa mục SharedPreferences với 'key'
     * @param key khóa của SharedPreferences
     */

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    /**
     * Xóa tệp hình ảnh tại 'đường dẫn'
     * @param path đường dẫn của tệp hình ảnh
     * @return true nếu nó đã được xóa thành công, ngược lại trả về false
     */

    public boolean deleteImage(String path) {
        return new File(path).delete();
    }

    /**
     * Xóa sạch SharedPreferences (loại bỏ tất cả mọi thứ)
     */

    public void clear() {
        preferences.edit().clear().apply();
    }

    /**
     * Lấy tất cả các giá trị từ SharedPreferences. Không chỉnh sửa bộ sưu tập được trả về bởi phương thức
     * @return Một Map biểu diễn một danh sách các cặp key/value từ SharedPreferences
     */

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    /**
     * Đăng ký trình nghe thay đổi SharedPreferences
     * @param listener đối tượng trình nghe của OnSharedPreferenceChangeListener
     */

    public void registerOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Hủy đăng ký trình nghe thay đổi SharedPreferences
     * @param listener đối tượng trình nghe của OnSharedPreferenceChangeListener sẽ được hủy đăng ký
     */

    public void unregisterOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Kiểm tra xem bộ nhớ ngoài có thể ghi được hay không
     * @return true nếu có thể ghi được, false nếu ngược lại
     */

    public static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Kiểm tra xem bộ nhớ ngoài có thể đọc được hay không
     * @return true nếu có thể đọc được, false nếu ngược lại
     */

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
    /**
     * Các khóa null sẽ làm hỏng tệp shared pref và làm cho chúng không đọc được, đây là biện pháp phòng ngừa
     * @param key khóa pref cần kiểm tra
     */

    private void checkForNullKey(String key){
        if (key == null){
            throw new NullPointerException();
        }
    }
    /**
     * Các giá trị null sẽ làm hỏng tệp shared pref và làm cho chúng không đọc được, đây là biện pháp phòng ngừa
     * @param value giá trị pref cần kiểm tra
     */

    private void checkForNullValue(String value){
        if (value == null){
            throw new NullPointerException();
        }
    }
}