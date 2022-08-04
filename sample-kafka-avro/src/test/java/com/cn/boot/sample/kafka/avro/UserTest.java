package com.cn.boot.sample.kafka.avro;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.kafka.avro.model.UserAvro;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
public class UserTest {

    /**
     * 构造User对象的三种方式
     */
    @Test
    public void initUser() {
        UserAvro user1 = new UserAvro();
        user1.setId("user1");
        user1.setUsername("username1");

        UserAvro user2 = new UserAvro("user2", "username2");

        UserAvro user3 = UserAvro.newBuilder()
                .setId("user3")
                .setUsername("username3")
                .build();

        log.info("user1={}", user1);
        log.info("user2={}", user2);
        log.info("user3={}", user3);
    }

    @Test
    public void serializing() throws IOException {
        UserAvro user1 = new UserAvro("user1", "username1");
        UserAvro user2 = new UserAvro("user2", "username2");
        UserAvro user3 = new UserAvro("user3", "username3");
        Schema schema = user1.getSchema();
        log.info("schema={}", schema);

        DatumWriter<UserAvro> userDatumWriter = new SpecificDatumWriter<>(UserAvro.class);
        DataFileWriter<UserAvro> dataFileWriter = new DataFileWriter<>(userDatumWriter);

        dataFileWriter.create(user1.getSchema(), new File("users.avro"));
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.append(user3);
        dataFileWriter.close();
    }

    @Test
    public void deserializing() throws IOException {
        File file = new File("users.avro");
        DatumReader<UserAvro> userDatumReader = new SpecificDatumReader<>(UserAvro.class);
        DataFileReader<UserAvro> dataFileReader = new DataFileReader<>(file, userDatumReader);
        UserAvro user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            log.info("user={}", user);
        }
    }

    @Test
    public void withoutUserObject() throws IOException {
        Schema schema = new Schema.Parser().parse(new File(FileUtil.getAbsolutePath("user.avsc")));

        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("id", "user1");
        user1.put("username", "username1");

        GenericRecord user2 = new GenericData.Record(schema);
        user2.put("id", "user2");

        log.info("user1={}", user1);
        log.info("user2={}", user2);

        File file = new File("users2.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(schema, file);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.close();

        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader);
        GenericRecord user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            log.info("user={}", user);
        }
    }

    @Test
    public void schema() {
        List<Schema.Field> fields = new ArrayList<>();
        fields.add(new Schema.Field("id", Schema.create(Schema.Type.STRING), null, null));
        fields.add(new Schema.Field("username", Schema.create(Schema.Type.STRING), null, null));
        Schema schema = Schema.createRecord("user", null, "", false, fields);

        GenericRecord user = new GenericData.Record(schema);
        user.put("id", "user1");
        user.put("username", "username1");

        log.info("schema={}", schema);
        log.info("user={}", user);
    }

    @Test
    public void testEncodeJson() throws IOException {
        //直接将字符串转为二进制流
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "user1");
        hashMap.put("username", "username1");
        String json = JSONUtil.toJsonStr(hashMap);
        log.info("json={}", json);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(out, null);
        binaryEncoder.writeString(json);
        binaryEncoder.flush();

        byte[] bytes = out.toByteArray();
        log.info("bytes={}", bytes);
        log.info("data={}", new String(bytes));
    }

    @Test
    public void testSerializable() {
        UserAvro user = new UserAvro();
        user.setId("user1");
        user.setUsername("username1");
        byte[] bytes = binarySerializable(user);
        log.info("bytes={}", bytes);
        UserAvro user1 = binaryDeserialize(bytes, UserAvro.class);
        UserAvro user2 = binaryDeserialize(bytes, UserAvro.getClassSchema());
        log.info("user1={}", user1);
        log.info("user2={}", user2);

        byte[] bytes1 = jsonSerializable(user1, UserAvro.getClassSchema());
        UserAvro user3 = jsonDeserialize(UserAvro.getClassSchema(), new ByteArrayInputStream(bytes1));
        log.info("user3={}", user3);
    }

    /**
     * 二进制序列化
     *
     * @param t   AVRO生成的对象
     * @param <T> AVRO类型
     * @return
     */
    public static <T> byte[] binarySerializable(T t) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(out, null);
        DatumWriter<T> writer = new SpecificDatumWriter<T>((Class<T>) t.getClass());
        try {
            writer.write(t, binaryEncoder);
            binaryEncoder.flush();
            out.flush();
        } catch (IOException e) {
            log.error("binarySerializable error");
            e.printStackTrace();
        }
        log.debug("ByteArrayOutputStream = {}", new String(out.toByteArray()));
        return out.toByteArray();
    }

    /**
     * 二进制反序列化
     *
     * @param bytes
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T binaryDeserialize(byte[] bytes, Class<T> tClass) {
        try {
            BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(bytes, null);
            DatumReader<T> datumReader = new SpecificDatumReader<T>(tClass);
            T read = datumReader.read(null, binaryDecoder);
            return read;
        } catch (IOException e) {
            log.error("binaryDeserialize error");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 二进制反序列化
     *
     * @param bytes
     * @param schema
     * @param <T>
     * @return
     */
    public static <T> T binaryDeserialize(byte[] bytes, Schema schema) {
        try {
            BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(bytes, null);
            DatumReader<T> datumReader = new SpecificDatumReader<T>(schema);
            T read = datumReader.read(null, binaryDecoder);
            return read;
        } catch (IOException e) {
            log.error("binaryDeserialize error");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json序列化
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> byte[] jsonSerializable(T t, Schema schema) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Encoder jsonEncoder = EncoderFactory.get().jsonEncoder(schema, out);
            DatumWriter<T> writer = new SpecificDatumWriter<T>(schema);
            writer.write(t, jsonEncoder);
            jsonEncoder.flush();
            out.flush();
        } catch (IOException e) {
            log.error("jsonSerializable error");
            e.printStackTrace();
        }
        log.info("json序列化的String为 = {}", new String(out.toByteArray()));
        return out.toByteArray();
    }

    /**
     * json反序列化
     *
     * @param schema
     * @param byteArrayInputStream
     * @param <T>
     * @return
     */
    public static <T> T jsonDeserialize(Schema schema, ByteArrayInputStream byteArrayInputStream) {
        try {
            Decoder jsonDecoder = DecoderFactory.get().jsonDecoder(schema, byteArrayInputStream);
            DatumReader<T> datumReader = new SpecificDatumReader<T>(schema);
            T read = datumReader.read(null, jsonDecoder);
            return read;
        } catch (Exception e) {
            log.error("jsonDeserialize error");
            e.printStackTrace();
        }
        return null;
    }

}
