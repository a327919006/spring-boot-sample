package com.cn.boot.sample.kafka.avro;

import cn.hutool.core.io.FileUtil;
import com.cn.boot.sample.kafka.avro.model.UserAvro;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
