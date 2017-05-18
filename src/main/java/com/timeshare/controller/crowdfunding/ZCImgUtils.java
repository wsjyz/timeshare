package com.timeshare.controller.crowdfunding;

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
public class ZCImgUtils {

    /**
     * 压缩
     *
     * @param srcPath 源图路径
     * @param desPath 目标图保存路径
     * @throws IM4JavaException
     * @throws InterruptedException
     * @throws IOException
     */
    public static void cropImageCenter(String srcPath, String desPath) {
        IMOperation op = new IMOperation();
        try {
            op.addImage();
            op.extent(0, 0);
            //图片质量
            op.addRawArgs("-quality", "10");
            op.addImage();
            ConvertCmd convert = new ConvertCmd(true);
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
        cropImageCenter("G:\\Image/2.jpg", "G:\\Image/2_quality.jpg");
        System.out.println("ok...");
    }
}
