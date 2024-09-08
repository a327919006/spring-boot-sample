import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;

/**
 * @author Chen Nan
 */
public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/boot_sample?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "chennan";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("Chen Nan") // 设置作者
                            // .enableSpringdoc() // 开启 SpringDoc模式
                            .enableSwagger() // 开启 Swagger 模式
                            .outputDir("E://code"); // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("com.cn.boot.sample.dal.mp") // 设置父包名
                                // .moduleName("dal.mp") // 设置父包模块名
                                .controller("controller")
                                .entity("entity")
                                .mapper("mapper")
                                .service("service")
                                .serviceImpl("service.impl")
                                .xml("mapper/xml") // 设置mapper.xml路径，默认mapper/xml/下
                )
                .strategyConfig(builder ->
                        // 默认所有表都生成
                        builder
                                .addInclude("t_people") // 设置需要生成的表名
                                // .addExclude("t_order_0", "t_order_1") // 设置需要跳过的表名
                                .addTablePrefix("t_") // 设置过滤表前缀
                                .entityBuilder()
                                .logicDeleteColumnName("is_deleted")
                                // .superClass()
                                // .addSuperEntityColumns()
                                .enableLombok()
                                .enableTableFieldAnnotation() // 启用字段注解
                                .controllerBuilder()
                                .enableRestStyle()
                                .mapperBuilder()
                                .enableBaseColumnList()
                                .enableBaseResultMap()

                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
