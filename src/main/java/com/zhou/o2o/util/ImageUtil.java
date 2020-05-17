package com.zhou.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * ImageUtil：用来封装thumbnailator的方法
 */
public class ImageUtil {
    /**  classPath绝对值路径:
     *    因为该方法是通过线程执行的，通过线程去逆推到他的类加载器
     *    从而通过类加载器得到这个classPath的绝对值路径了
     */
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    //设置时间的格式
    private static  final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    //设置随机数对象
    private static final Random r = new Random();
    //设置日志对象
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 将CommonsMultipartFile转换成File类
     * 处理用户传递过来的文件流，因此这里的文件流是Spring 自带的文件处理对象CommonsMultipartFile
     * 为了后面更好的处理，我们要转成java.io.File对象
     * @param cFile
     * @return
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
        File newFile = new File(cFile.getOriginalFilename());
        try {
            //通过CommonsMultipartFile中的transferTo方法将文件流转换成java.io.File的文件流对象
            cFile.transferTo(newFile);
        } catch (IOException e) {
            //将错误信息写入到error级别的错误日志中去
            logger.error(e.toString());
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     * 工具类1：处理缩略图，生成新的图片对象,并返回新生成图片的相对值路径
     * @param thumbnailInputStream  经过处理的文件流
     * @param targetAddr 图片在Tomcat服务器下保存的相对路径地址
     * @return
     */
    public static String generateThumbnail(InputStream thumbnailInputStream,String fileName, String targetAddr) throws IOException{
        //1.获取随机生成的文件名，防止传递过来的文件重名
        String realFileName = getRandomFileName();
        //2.获取用户传递过来的文件扩展名
        String extension = getFileExtension(fileName);
        //3.targetAddr这个路径的目录有时是不存在的，需要将这个目录创建出来
        makeDirPath(targetAddr);
        //4.缩略图的相对路径：   图片在Tomcat服务器下保存的相对路径地址 + 随机文件名 + 扩展名
        String relativeAddr = targetAddr+realFileName+extension;
        //设置日志信息:当前路径
        logger.debug("current relativeAddr is:" + relativeAddr);
        //5.新生成文件的路径：根路径 + 缩略图相对路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        //设置日志信息:全路径
        logger.debug("current complete addr is:" + PathUtil.getImgBasePath()+relativeAddr);

        //对图片进行处理并生成新的图片对象
        try{
            /**
             * 处理图片
             *  参数1：需要处理的图片路径  size 输出图片的大小
             *  watermark:给图片添加水印  参数1：水印的位置 参数2：指定水印图片的路径  参数3:定义图片透明度
             *  outputQuality:压缩比例   toFile:添加水印后的图片输出的地址
             */
            Thumbnails.of(thumbnailInputStream).size(1080,1080 )
                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg"))
                            ,0.25f).outputQuality(0.8f).toFile(dest);
        }catch (IOException e){
            //设置日志信息:生成失败添加日志
            logger.error(e.toString());
            e.printStackTrace();
        }

        /**
         * 返回缩略图的相对路径,数据库中有个字段需要这个图片的地址，所以我们需要返回
         * 为什么这里传的是相对路径呢？
         * 因为我们希望程序在别系统之后，也是能照常读出图片，而不是该改变数据库中存储图片地址的字段值
         */
        return relativeAddr;
    }

    /**
     * 工具类服务方法1：生成随机的文件名，当前年月日小时分钟秒+五位随机数
     * @return
     */
    public static String getRandomFileName() {
        //获取随机的五位数 10000 - 99999
        int rannum = r.nextInt(89999) + 10000;
        //获取当前时间格式
        String nowTimeStr = sDateFormat.format(new Date());
        //返回组合的文件名
        return  nowTimeStr + rannum;
    }


    /**
     * 工具类服务方法2：
     * 创建目标路径上所涉及到的目录, 即 /home/aaa/bbb/xx.jpg,那么home aaa bbb这三个文件夹都得创建
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        //因为targetAddr是所属文件夹的相对路径因此需要获取他的绝对路径
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        //根据文件路径创建File文件对象
        File dirPath = new File(realFileParentPath);
        //判断路径是否存在，如果不存在就递归的创建出来
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }


    /**
     * 工具类服务方法3：获取输入文件流的扩展名
     * @param fileName
     * @return
     */
    private static String getFileExtension(String  fileName) {
        //获取文件的扩展名并返回
        return fileName.substring(fileName.lastIndexOf("."));
    }




    /**
     * 理解thumbnailator的案例： 使用thumbnailator相关的方法去处理预先定义好的图片
     * @param args
     * @throws IOException
     */
    public static void main(String[] args)throws IOException {
        //通过线程逆推到类加载器，然后从类加载中得到classPath的绝对值路径了
        //String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

        /**
         * 处理图片
         *  参数1：需要处理的图片路径  size 输出图片的大小
         *  watermark:给图片添加水印  参数1：水印的位置 参数2：指定水印图片的路径  参数3:定义图片透明度
         *  outputQuality:压缩比例   toFile:添加水印后的图片输出的地址
         */
        Thumbnails.of(new File("B:/aaa.jpg")).size(1080,1080)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath +"/watermark.jpg"))
                        ,0.25f).outputQuality(0.8f).toFile("B:/aaa111.jpg");
    }

}
