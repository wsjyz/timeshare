package com.timeshare.utils;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by user on 2016/7/11.
 */
public class ImgUtils {

    /**
     * 先缩放，后居中切割图片
     *
     * @param srcPath 源图路径
     * @param desPath 目标图保存路径
     * @param rectw   待切割在宽度
     * @param recth   待切割在高度
     * @throws IM4JavaException
     * @throws InterruptedException
     * @throws IOException
     */
    public static void cropImageCenter(String srcPath, String desPath, int rectw, int recth) {
        IMOperation op = new IMOperation();
        try {
            File srcfile = new File(srcPath);
            BufferedImage im = ImageIO.read(srcfile);
                   /* 原始图像的宽度和高度 */
            int width = im.getWidth();
            int height = im.getHeight();
            op.addImage();
            //既缩小（resize），居中（gravity），又压缩（extent）
          //  op.resize(rectw, recth, '^').gravity("center").extent(width, height);
            op.extent(0, 0);
            op.addImage();

            ConvertCmd convert = new ConvertCmd(true);
//        //convert.createScript("e:\\test\\myscript.sh",op);

            convert.run(op, srcPath, desPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        cropImageCenter("D:\\1\\timg.jpg", "D:\\2\\1.jpg", 320, 240);
    }
}
