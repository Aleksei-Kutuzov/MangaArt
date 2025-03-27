package com.killerficha.mangaart.ProjectInstruments;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ProjectSerializerOrDeserializer {
    // Сериализация проекта в байты
    public static byte[] serializeProject(Project project) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(project);
            return bos.toByteArray();
        }
        catch (Exception e){
            Log.e("EROR_FOR_DB_SERIALISE", e.toString());
            return String.valueOf(769).getBytes();
        }
    }

    // Десериализация проекта из байтов
    public static Project deserializeProject(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Project) ois.readObject();
        }
    }
}
