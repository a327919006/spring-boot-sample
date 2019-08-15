package com.cn.boot.sample.wechat;

import com.cn.boot.sample.wechat.model.CreateMenuDTO;
import com.cn.boot.sample.wechat.model.CreateMenuDTO.ButtonBean;
import com.cn.boot.sample.wechat.model.CreateMenuDTO.ButtonBean.SubButtonBean;
import com.cn.boot.sample.wechat.model.UserInfoRsp;
import com.cn.boot.sample.wechat.model.UpdateIndustryDTO;
import com.cn.boot.sample.wechat.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    /**
     * 获取token
     */
    @Test
    public void getAccessToken() {
        log.info(wechatService.getAccessToken());
        log.info(wechatService.getAccessToken());
        log.info(wechatService.getAccessToken());
    }

    /**
     * 创建菜单
     */
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

    /**
     * 设置行业
     */
    @Test
    public void updateIndustry() {
        UpdateIndustryDTO updateIndustryDTO = new UpdateIndustryDTO();
        updateIndustryDTO.setIndustry_id1("1");
        updateIndustryDTO.setIndustry_id2("2");

        wechatService.updateIndustry(updateIndustryDTO);
    }

    /**
     * 下载素材到本地
     */
    @Test
    public void downloadMedia() throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
        String token = "24_qWArqrO_WkbPUoatcXdKQK4TbJyc7rYccgM8kiUGw3MQZnUuLNJMihYhrzlhOA050jeHGHS-0v1s8Q8dp_Wbg_4p3ZRYRluM9RUe4qX3g9N_yeTdb9F0YfvvHZNle9qw8yWIdVFhszBMu2aDJHHiAIAECC";
        String mediaId = "HuMuZhsbvDWLU1hIjXz1hwXbt4gEIeNAz0Oar1vFjLr2G1xFjUOqEkCUeRAk_lUC";
        url = url.replace("ACCESS_TOKEN", token);
        url = url.replace("MEDIA_ID", mediaId);
        byte[] bytes = IOUtils.toByteArray(new URL(url));
        log.info("length={}", bytes.length);
        FileUtils.writeByteArrayToFile(new File("E://aa.png"), bytes);
    }

    /**
     * 使用ticket生成二维码，下载到本地
     */
    @Test
    public void getQrCode() throws IOException {
        String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
        String ticket = "gQHp7jwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyQklkaVlqTXdkUjExbl94dWh0YzUAAgR-J1VdAwSAOgkA";
        url = url.replace("TICKET", ticket);
        byte[] bytes = IOUtils.toByteArray(new URL(url));
        log.info("length={}", bytes.length);
        FileUtils.writeByteArrayToFile(new File("E://aa.png"), bytes);
    }

    /**
     * 使用ticket生成二维码，下载到本地
     */
    @Test
    public void getUserInfo() throws IOException {
        String openId = "o8OB1wRCFbJKaSyB9vMfFeq7rcGk";
        UserInfoRsp userInfo = wechatService.getUserInfo(openId);
        log.info("userInfo={}", userInfo);
    }
}
