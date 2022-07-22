package com.cn.boot.sample.kafka.avro.config;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * avro序列化
 *
 * @author Chen Nan
 */
public class AvroSerializer<T extends SpecificRecordBase> implements Serializer<T> {
    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }
        DatumWriter<T> writer = new SpecificDatumWriter<>(data.getSchema());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryEncoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(byteArrayOutputStream, null);
        try {
            writer.write(data, binaryEncoder);
            binaryEncoder.flush();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new SerializationException(e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }
}
