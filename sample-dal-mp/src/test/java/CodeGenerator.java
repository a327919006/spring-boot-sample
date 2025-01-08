import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.cn.boot.sample.dal.mp.model.po.BaseEntity;

import java.sql.Types;

/**
 * 官方文档： https://baomidou.com/reference/new-code-generator-configuration/
 *
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
                            // .disableOpenDir() // 生成后打开目录
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
                        builder.parent("com.cn.boot.sample") // 设置父包名
                                // .moduleName("dal.mp") // 设置父包模块名
                                .controller("server.mp.controller")
                                .service("server.mp.service")
                                .serviceImpl("server.mp.service.impl")
                                .entity("dal.mp.entity")
                                .mapper("dal.mp.mapper")
                                .xml("dal.mp.mapper.xml") // 设置mapper.xml路径，默认mapper/xml/下
                )
                .strategyConfig(builder ->
                        // 默认所有表都生成
                        builder
                                .addInclude("t_people") // 设置需要生成的表名，支持正则
                                // .addExclude("t_order_.*") // 设置需要跳过的表名，支持正则
                                .addTablePrefix("t_") // 设置过滤表前缀
                                .entityBuilder()
                                .superClass(BaseEntity.class) // 父类
                                .addSuperEntityColumns("is_deleted", "create_time", "update_time") // 父类字段
                                .logicDeleteColumnName("is_deleted") // 逻辑删除字段
                                .enableLombok() // 启用lombok
                                // .enableChainModel() // 启用lombok 链式编程
                                .enableTableFieldAnnotation() // 启用字段注解
                                .controllerBuilder()
                                .enableRestStyle() // REST接口
                                .mapperBuilder()
                                .enableBaseColumnList() // mapper BaseColumnList
                                .enableBaseResultMap() // mapper BaseResultMap
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
