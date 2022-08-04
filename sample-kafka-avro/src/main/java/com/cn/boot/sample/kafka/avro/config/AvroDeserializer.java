package com.cn.boot.sample.kafka.avro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
public class AvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

    protected final Class<T> targetType;

    public AvroDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            T result = null;
            if (data == null) {
                return null;
            }
            log.debug("data='{}'", DatatypeConverter.printHexBinary(data));
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            DatumReader<GenericRecord> userDatumReader = new SpecificDatumReader<>(targetType.newInstance().getSchema());
            BinaryDecoder decoder = DecoderFactory.get().directBinaryDecoder(in, null);
            result = (T) userDatumReader.read(null, decoder);
            log.debug("deserialized data='{}'", result);
            return result;
        } catch (Exception ex) {
            throw new SerializationException(
                    "Can't deserialize data '" + Arrays.toString(data) + "' from topic '" + topic + "'", ex);
        } finally {

        }
    }
}
