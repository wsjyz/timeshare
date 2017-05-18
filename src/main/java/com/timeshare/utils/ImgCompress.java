package com.timeshare.utils;

import java.io.*;
import java.util.Date;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.*;


/**
 * Created by user on 2016/7/11.
 */
public class ImgCompress {
    private Image img;
    private int width;
    private int height;

    /**
     * 构造函数
     */
    public ImgCompress(String fileName) throws IOException {
        File file = new File(fileName);// 读入文件
        img = ImageIO.read(file);      // 构造Image对象
        width = img.getWidth(null);    // 得到源图宽
        height = img.getHeight(null);  // 得到源图长
    }

    /**
     * 按照宽度还是高度进行压缩
     *
     * @param w int 最大宽度
     *          35.     * @param h int 最大高度
     *          36.
     */
    public void resizeFix(int w, int h,String desPath) throws IOException {
        if (width / height > w / h) {
            resizeByWidth(w,desPath);
        } else {
            resizeByHeight(h,desPath);
        }
    }

    /**
     * 45.     * 以宽度为基准，等比例放缩图片
     * 46.     * @param w int 新宽度
     * 47.
     */
    public void resizeByWidth(int w,String desPath) throws IOException {
        int h = (int) (height * w / width);
        resize(w, h,desPath);
    }

    /**
     * .     * 以高度为基准，等比例缩放图片
     * .     * @param h int 新高度
     * .
     */
    public void resizeByHeight(int h,String desPath) throws IOException {
        int w = (int) (width * h / height);
        resize(w, h,desPath);
    }

    /**
     * .     * 强制压缩/放大图片到固定的大小
     * .     * @param w int 新宽度
     * .     * @param h int 新高度
     */
    public void resize(int w, int h,String desPath) throws IOException {
        // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
        File destFile = new File(desPath);
        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
        // 可以正常实现bmp、png、gif转jpg
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(image); // JPEG编码
        out.close();
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {

        System.out.println("开始：" + new Date().toLocaleString());
        try {
            ImgCompress imgCom = new ImgCompress("D:\\1\\timg.jpg");
            imgCom.resizeFix(600, 600,"D:\\1\\timg1.jpg");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
