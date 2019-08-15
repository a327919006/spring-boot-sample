package com.cn.boot.sample.wechat;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;

import com.cn.boot.sample.wechat.model.CreateMenuDTO;
import com.cn.boot.sample.wechat.model.CreateMenuDTO.ButtonBean;
import com.cn.boot.sample.wechat.model.CreateMenuDTO.ButtonBean.SubButtonBean;
import com.cn.boot.sample.wechat.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/6/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class WechatControllerTest {

    @Autowired
    private WechatService wechatService;

    @Test
    public void getAccessToken() {
        log.info(wechatService.getAccessToken());
        log.info(wechatService.getAccessToken());
        log.info(wechatService.getAccessToken());
    }

    @Test
    public void createMenu() {
        CreateMenuDTO createMenuDTO = new CreateMenuDTO();
        List<ButtonBean> buttonBeans = new ArrayList<>();
        ButtonBean buttonBean1 = new ButtonBean();
        ButtonBean buttonBean2 = new ButtonBean();
        ButtonBean buttonBean3 = new ButtonBean();

        buttonBean1.setType("click");
        buttonBean1.setName("点击按钮1");
        buttonBean1.setKey("1");

        List<SubButtonBean> subButtonBeans = new ArrayList<>();
        SubButtonBean subButtonBean1 = new SubButtonBean();
        SubButtonBean subButtonBean2 = new SubButtonBean();
        SubButtonBean subButtonBean3 = new SubButtonBean();
        SubButtonBean subButtonBean4 = new SubButtonBean();
        SubButtonBean subButtonBean5 = new SubButtonBean();

        subButtonBean1.setType("view");
        subButtonBean1.setName("百度");
        subButtonBean1.setUrl("http://www.baidu.com");

        subButtonBean2.setType("scancode_waitmsg");
        subButtonBean2.setName("扫码带提示");
        subButtonBean2.setKey("22");

        subButtonBean3.setType("pic_sysphoto");
        subButtonBean3.setName("系统拍照发图");
        subButtonBean3.setKey("23");

        subButtonBean4.setType("pic_photo_or_album");
        subButtonBean4.setName("拍照或者相册发图");
        subButtonBean4.setKey("24");

        subButtonBean5.setType("location_select");
        subButtonBean5.setName("发送位置");
        subButtonBean5.setKey("25");

        subButtonBeans.add(subButtonBean1);
        subButtonBeans.add(subButtonBean2);
        subButtonBeans.add(subButtonBean3);
        subButtonBeans.add(subButtonBean4);
        subButtonBeans.add(subButtonBean5);

        buttonBean2.setName("菜单2");
        buttonBean2.setSub_button(subButtonBeans);

        buttonBean3.setType("click");
        buttonBean3.setName("点击按钮2");
        buttonBean3.setKey("3");

        buttonBeans.add(buttonBean1);
        buttonBeans.add(buttonBean2);
        buttonBeans.add(buttonBean3);
        createMenuDTO.setButton(buttonBeans);

        wechatService.createMenu(createMenuDTO);
    }
}
