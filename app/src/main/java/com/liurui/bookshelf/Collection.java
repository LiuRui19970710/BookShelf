package com.liurui.bookshelf;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Collection {
    private String file;
    ArrayList<?> list_null = new ArrayList<>();

    public Collection(String filename ) {
        this.file = filename+".dat";
    }

    public ArrayList read(Context context) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (ArrayList<?>) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof InvalidClassException) {
                File data = context.getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return list_null;
    }

        public boolean save (Context context, Serializable ser){
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                fos = context.openFileOutput(file, Context.MODE_PRIVATE);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(ser);
                oos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    oos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
}
