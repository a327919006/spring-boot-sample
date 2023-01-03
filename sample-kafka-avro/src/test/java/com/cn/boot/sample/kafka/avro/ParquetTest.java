package com.cn.boot.sample.kafka.avro;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.ParquetProperties;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.format.converter.ParquetMetadataConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.ExampleParquetWriter;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.hadoop.metadata.ParquetMetadata;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

import java.io.IOException;

/**
 * @author Chen Nan
 */
@Slf4j
public class ParquetTest {

    private static String schemaStr = "message TestDetail {\n" +
            "optional binary did;\n" +
            "optional binary mid;\n" +
            "optional binary id;\n" +
            "optional int64 CaptureTime;\n" +
            "optional int32 StepIndex;\n" +
            "optional double TestVol;\n" +
            "optional double TestCur;\n" +
            "optional double TestChaEne;\n" +
            "optional double TestChaCap;\n" +
            "optional double TestDisEne;\n" +
            "optional double TestDisCap;\n" +
            "optional double BatVol;\n" +
            "optional double BatCur;\n" +
            "optional double BatEne;\n" +
            "optional double BatCap;\n" +
            "optional double BatDisEne;\n" +
            "optional double BatDisCap;\n" +
            "optional double CellMinVol;\n" +
            "optional binary UserOrder;\n" +
            "optional binary ChPileID;\n" +
            "optional double BatSOC;\n" +
            "optional double CellMaxVol;\n" +
            "optional double CellMaxTemp;\n" +
            "optional double CellMinTemp;\n" +
            "optional binary ChGunID;\n" +
            "}";
    static MessageType schema = MessageTypeParser.parseMessageType(schemaStr);

    public static void testParseSchema() {
        log.info(schema.toString());
    }

    public static void testGetSchema() throws Exception {
        Configuration configuration = new Configuration();
        ParquetMetadata readFooter = null;
        Path parquetFilePath = new Path("test.parquet");
        readFooter = ParquetFileReader.readFooter(configuration,
                parquetFilePath, ParquetMetadataConverter.NO_FILTER);
        MessageType schema = readFooter.getFileMetaData().getSchema();
        log.info(schema.toString());
    }

    private static void testParquetWriter() throws IOException {
        Path file = new Path("test.parquet");
        ExampleParquetWriter.Builder builder = ExampleParquetWriter
                .builder(file).withWriteMode(ParquetFileWriter.Mode.CREATE)
                .withWriterVersion(ParquetProperties.WriterVersion.PARQUET_1_0)
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                //.withConf(configuration)
                .withType(schema);

        ParquetWriter<Group> writer = builder.build();
        SimpleGroupFactory groupFactory = new SimpleGroupFactory(schema);
        String[] access_log = {"111111", "22222", "33333", "44444", "55555",
                "666666", "777777", "888888", "999999", "101010"};
        for (int i = 0; i < 1000; i++) {
            writer.write(groupFactory.newGroup()
                    .append("did", access_log[0])
                    .append("mid", access_log[1])
                    .append("id", access_log[2])
                    .append("CaptureTime", Long.parseLong(access_log[3]))
                    .append("StepIndex", Integer.parseInt(access_log[4]))
                    .append("TestVol", Double.parseDouble(access_log[5]))
                    .append("TestCur", Double.parseDouble(access_log[6]))
                    .append("TestChaEne", Double.parseDouble(access_log[7]))
                    .append("TestChaCap", Double.parseDouble(access_log[8]))
                    .append("TestDisEne", Double.parseDouble(access_log[9])));
        }
        writer.close();
    }

    private static void testParquetReader() throws IOException {
        Path file = new Path("test.parquet");
        ParquetReader.Builder<Group> builder = ParquetReader.builder(new GroupReadSupport(), file);

        ParquetReader<Group> reader = builder.build();
        SimpleGroup group = (SimpleGroup) reader.read();
        log.info("schema:" + group.getType().toString());
        log.info("did:" + group.getString(1, 0));
    }

    private static void testParquetReader2() throws IOException {
        Path file = new Path("123.parquet");
        ParquetReader.Builder<Group> builder = ParquetReader.builder(new GroupReadSupport(), file);

        ParquetReader<Group> reader = builder.build();
        SimpleGroup group = (SimpleGroup) reader.read();
        log.info("schema:" + group.getType().toString());
        while (group != null) {
            log.info("did={} mid={}", group.getString(0, 0), group.getString(1, 0));
            group = (SimpleGroup) reader.read();
            break;
        }
    }

    public static void main(String[] args) throws Exception {
//        testGetSchema();
//        testParseSchema();
//        testParquetWriter();
//        testParquetReader();
        testParquetReader2();
    }
}
