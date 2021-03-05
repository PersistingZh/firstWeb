package com.pqkj.common;

import com.alibaba.fastjson.JSONObject;
import com.pq.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileUtil extends FileUtilBase {
    private static final Logger logger = LoggerFactory.getLogger(FileUtilBase.class);


    private final static String FILE_FILTER_FILENAME = FileProperties.getValue("file.filter.filename");
    private final static String FILE_FILTER_FILENAME223 = FileProperties.getValue("file.filter.filename223");
    private final static String FILE_FILTER_FILE_ZSNAME = FileProperties.getValue("file.filter.fileZsname");
    private final static String ATTACHMENT_CREATE_PATH = FileProperties.getValue("attachment.create.hlw.path");
    private final static String CREATE_PATH = FileProperties.getValue("json.create.hlw.path");
    private final static String CREATE_TEMP_PATH = FileProperties.getValue("json.create.hlw.temp");

    public static String imgToBase64(File file) {

        BASE64Encoder encoder = new BASE64Encoder();
        BufferedImage bi;
        try {
            bi = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "JPEG", baos);
            byte[] bytes = baos.toByteArray();

            return encoder.encodeBuffer(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 本地图片转换Base64的方法
     * 
     */
    public static String imageToBase64ByLocal(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理


        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
//        "data:image/jpeg;base64,"+
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }
    // 生成文件路径
    private static String path = FileProperties.getRootPath();


    /**
     * 创建文件
     *
     * @param fileName    文件名称
     * @param filecontent 文件内容
     * @return 是否创建成功，成功则返回true
     */
    public static boolean createFile(String fileName, String filecontent) {
        Boolean bool = false;
        String filenameTemp = path + fileName;// 文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            // 如果文件不存在，则创建新的文件
            if (!file.exists()) {
                file.createNewFile();
                bool = true;
                // 创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 向文件中写入内容
     *
     * @param filepath 文件路径与名称
     * @param newstr   写入的内容
     * @return
     * @throws IOException
     */
    private static boolean writeFileContent(String filepath, String newstr)
            throws IOException {
        Boolean bool = false;
        String filein = newstr + "\r\n";// 新写入的行，换行
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);// 文件路径(包括文件名称)
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            // 文件原有内容
            for (int i = 0; (temp = br.readLine()) != null; i++) {
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名称
     * @return
     */
    public static boolean delFile(String fileName) {
        Boolean bool = false;
        String filenameTemp = path + fileName;
        File file = new File(filenameTemp);
        try {
            if (file.exists()) {
                file.delete();
                bool = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bool;
    }

    /**
     * @param fileName    【文件名称】.后缀
     * @param filecontent 文件内容
     * @param filePrefix  【文件前缀】
     * @param filePath    文件路径
     * @return
     * @title 创建文件
     */
    public static String createFile(String filePath, String fileName, String filecontent, String filePrefix) {
        String name = isNull(filePath, filePrefix, fileName);
        createFile(name, filecontent);
        return name;
    }

    /**
     * @param objs
     * @return
     * @title 判断是否为null
     */
    private static String isNull(Object... objs) {
        String s = "";
        if (objs == null) {
            return s;
        }
        for (Object obj : objs) {
            s += obj == null ? "" : String.valueOf(obj);
        }
        return String.valueOf(s);
    }

    /**
     * @param obj
     * @return
     * @title 实体转换JSON
     * @author LZW
     */
    public static String toEntityString(Object obj) {
        String data = "";
        try {
            data = JSONObject.toJSONString(obj);
        } catch (Exception e) {
           logger.info("转换异常");
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @param obj
     * @return
     * @title 实体转换 HashMap
     * @author LZW
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> toEntityHashMap(Object obj) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        try {
            hashMap = JSONObject.parseObject(toEntityString(obj), HashMap.class);
        } catch (Exception e) {
           logger.info("转换异常");
            e.printStackTrace();
        }
        return hashMap;
    }


    public static void contentToTxt(String filePath, String content) {
        try {
            File f = new File(filePath);
            FileOutputStream fos = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(content);
            osw.flush();
            osw.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String content, String tempPath, String filePath) throws Exception {
        String fileName = FILE_FILTER_FILENAME + RandomGUID.getDatetUUID() + ".txt";
        //生成文件
        contentToTxt(tempPath + fileName, content);
        //文件进行复制
        FileUtilBase.deleteAndCopyFile(tempPath + fileName, filePath + fileName);
    }

    public static void writeToFile223(String content, String tempPath, String filePath) throws Exception {
        String fileName = FILE_FILTER_FILENAME223 + RandomGUID.getDatetUUID() + ".txt";
        //生成文件
        contentToTxt(tempPath + fileName, content);
        //文件进行复制
        FileUtilBase.deleteAndCopyFile(tempPath + fileName, filePath + fileName);


    }

    public static void writeToFileZs(String content, String tempPath, String filePath) throws Exception {
        String fileName = FILE_FILTER_FILE_ZSNAME + RandomGUID.getDatetUUID() + ".txt";
        //生成文件
        contentToTxt(tempPath + fileName, content);
        //文件进行复制
        FileUtilBase.deleteAndCopyFile(tempPath + fileName, filePath + fileName);


    }

    /*添加同步文件附件标志*/
    public static String addAttachmentTag(String fileType, String fileName) {
        String fileNameNew = FileProperties.getValue("attachment.fix") + fileName + "_" + fileType;
        return fileNameNew;
    }

    /*添加同步文件附件缩略图标志*/
    public static String addThumAttachmentTag(String fileType, String fileName) {
        String fileNameNew = FileProperties.getValue("attachment.thum.fix") + fileName + "_" + fileType;
        return fileNameNew;
    }

    /*添加同步文件附件缩略图标志*/
    public static String addThumAttachmentTag2(String fileType, String fileName) {
        String fileNameNew = fileType;
        return fileNameNew;
    }

    /*生成同步附件*/
    public static void createSyncAttachment(String filePath) throws Exception {
        File file = new File(filePath);
        FileUtilBase.copyfile(filePath, ATTACHMENT_CREATE_PATH + file.getName());
    }

    /*生成缩略图并同步文件*/
    public static String createAndSyncThumbnail(String filePath, String fileName, String fileThumName) throws Exception {
        String imgFormat = FileProperties.getValue("img_format");
        String imgThumbnailPath = FileProperties.getValue("imgThumbnail");
        String rootPath = FileProperties.getRootPath();

        if (imgFormat != null) {
            String splitFormat[] = imgFormat.split("_");
            boolean b = false;
            for (String format : splitFormat) {
                if (fileName.indexOf("." + format) > -1) {
                    b = true;
                    break;
                }
            }
            if (b) {
                String img = rootPath + imgThumbnailPath + filePath + fileThumName;
                FileUtilBase.copyfile(rootPath + filePath + fileName, img);
                File baseFile = new File(rootPath + filePath + fileName);
                File desFile = new File(img);

                ImgCompress imgThumThum = new ImgCompress(baseFile, desFile);
                imgThumThum.resizeFix(80, 80);
                FileUtil.createSyncAttachment(img);
                return imgThumbnailPath + filePath + fileThumName;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /*生成缩略图*/
    public static String createThumbnail(String filePath, String fileName, String fileThumName) throws Exception {
        String imgFormat = FileProperties.getValue("img_format");
        String imgThumbnailPath = FileProperties.getValue("imgThumbnail");
        String rootPath = FileProperties.getRootPath();

        if (imgFormat != null) {
            String splitFormat[] = imgFormat.split("_");
            boolean b = false;
            for (String format : splitFormat) {
                if (fileName.indexOf("." + format) > -1) {
                    b = true;
                    break;
                }
            }
            if (b) {
                String img = rootPath + imgThumbnailPath + filePath + fileThumName;
                FileUtilBase.copyfile(rootPath + filePath + fileName, img);
                File baseFile = new File(rootPath + filePath + fileName);
                File desFile = new File(img);

                ImgCompress imgThumThum = new ImgCompress(baseFile, desFile);
                imgThumThum.resizeFix(80, 80);
                return imgThumbnailPath + filePath + fileThumName;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 创建同步文件
     *
     * @param reqAction   请求的action
     * @param param       请求的json参数
     * @param description 描述
     */
    public static void createSyncFile(String reqAction, String param, String description) {
        JSONObject parentJon = new JSONObject();
        parentJon.put("requestAction", reqAction);
        parentJon.put("description", description);
        parentJon.put("createTime", DateTimeUI.getCurrentDateTime());
        parentJon.put("param", param);
        try {
            FileUtil.writeToFile(parentJon.toJSONString(), CREATE_TEMP_PATH, CREATE_PATH);
        } catch (Exception e) {
           logger.info("活动同步失败！");
            e.printStackTrace();
        }
    }

    /**
     * 创建同步文件給223 gawservice
     *
     * @param reqAction
     * @param param
     * @param description
     */
    public static void createSyncFileForZsjl(String reqAction, String param, String description) {
        JSONObject parentJon = new JSONObject();
        parentJon.put("requestAction", reqAction);
        parentJon.put("description", description);
        parentJon.put("createTime", DateTimeUI.getCurrentDateTime());
        parentJon.put("param", param);
        try {
            FileUtil.writeToFileZs(parentJon.toJSONString(), CREATE_TEMP_PATH, CREATE_PATH);
        } catch (Exception e) {
           logger.info("活动同步失败！");
            e.printStackTrace();
        }
    }

    /**
     * 同步文件
     *
     * @param tempPath
     * @title
     * @author LZW
     */
    public static void webCopyfile(String tempPath, String fileName) {
        try {
            FileUtilBase.copyfile(tempPath, CREATE_PATH + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param tempPath    图片原本路径
     * @param imgPath     保存路径
     * @param imgThumPath 略缩图保存路径
     * @return 【0】 大图 【1】缩略图
     * @throws Exception
     * @title 同步图片
     * @author ZYB
     */
    public static String[] savePicAndThum(String tempPath, String fix, String imgPath, String imgThumPath) throws Exception {
        String[] imgs = new String[3];
        if (tempPath != null && tempPath.indexOf("temp/") > -1) {
            tempPath = FileProperties.getRootPath(tempPath);
            //FileUtilBase.deleteAndCopyFile(tempPath, FileProperties.getRootPath(copyFilePath));//把临时文件夹的图片复制到其它路径下并删除
            //生成原图文件命名
            String saveFileDir = FileProperties.getRootPath() + FileProperties.getValue(fix);
            //生成缩略图文件命名
            String saveFileNameThumDir = FileProperties.getRootPath() + FileProperties.getValue("imgThumPath") + FileProperties.getValue(fix);
            ;
            String fileName = saveFileDir + imgPath;
            FileUtilBase.deleteAndCopyFile(tempPath, fileName);//把临时文件夹的图片复制到其它路径下并删除
            FileUtil.createThumbnail(FileProperties.getValue(fix), imgPath, imgThumPath);
            imgs[0] = FileProperties.getValue(fix) + imgPath;
            imgs[1] = imgThumPath + FileProperties.getValue(fix) + imgThumPath;
        }
        return imgs;

    }

    /**
     * 递归查找文件
     *
     * @param baseDirName    查找的文件夹路径
     * @param targetFileName 需要查找的文件名
     * @param fileList       查找到的文件集合
     */
    public static void findFiles(String baseDirName, String targetFileName, List fileList) {
        File baseDir = new File(baseDirName);        // 创建一个File对象
        if (!baseDir.exists() || !baseDir.isDirectory()) {    // 判断目录是否存在
           logger.info("文件查找失败：" + baseDirName + "不是一个目录！");
        }
        String tempName = null;
        //判断目录是否存在
        File tempFile;
        File[] files = baseDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            tempFile = files[i];
            if (tempFile.isDirectory()) {
                findFiles(tempFile.getAbsolutePath(), targetFileName, fileList);
            } else if (tempFile.isFile()) {
                tempName = tempFile.getName();
                if (tempName.indexOf(targetFileName) != -1) {
                    // 匹配成功，将文件名添加到结果集
                    fileList.add(tempName);
                }
            }
        }
    }

    public static String copyLocalFileToSys(String imgPath, String imgPrefix) throws Exception {
        return copyLocalFileToSys(imgPath, imgPrefix, null);
    }

    /**
     * 拷贝本地图片
     *
     * @param imgPath   本地图片路径
     * @param imgPrefix 系统路径
     * @param imgName   图片名称
     * @return
     * @throws Exception
     */
    public static String copyLocalFileToSys(String imgPath, String imgPrefix, String imgName) throws Exception {
        String sysImgPath = "";
        String tempPath = FileProperties.getRootPath(imgPath);//获取临时文件的绝对路径
        String copyFilePath = "";
        if (!StringUtil.isNullorEmpty(imgName)) {
            copyFilePath = FileProperties.getValue(imgPrefix) + imgName;
        } else {
            copyFilePath = FileProperties.getValue(imgPrefix) + FileUtilBase.getFileNameSuff(tempPath);
        }

        //获取文件绝对路径
        String rootCopyFilePath = FileProperties.getRootPath(copyFilePath);
        FileUtilBase.deleteAndCopyFile(tempPath, rootCopyFilePath);//把临时文件夹的图片复制到其它路径下并删除
        sysImgPath = copyFilePath;
        return sysImgPath;

    }

  /*  private static void getFile() {

    }*/


    public static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();
    }
    public static String sizeFormatNum2String(long size) {
        String s = "";
        if(size>1024*1024) {
            s = String.format("%.2f", (double) size / (1024*1024)) + "M";
        }else {
            s = String.format("%.2f", (double) size / (1024)) + "KB";
        }
        return s;
    }
    /**
     * 获取文件夹中的所有文件
     * @param path
     * @return
     * @throws Exception
     */
    public static ArrayList<File> getFiles(String path) throws Exception {
        //目标集合fileList
        ArrayList<File> fileList = new ArrayList<File>();
        File file = new File(path);
        if(file.isDirectory()){
            File []files = file.listFiles();
            for(File fileIndex:files){
                //如果这个文件是目录，则进行递归搜索
                if(fileIndex.isDirectory()){
                    getFiles(fileIndex.getPath());
                }else {
                    //如果文件是普通文件，则将文件句柄放入集合中
                    fileList.add(fileIndex);
                }
            }
        }
        return fileList;
    }

    public static void judeDirExists(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
               logger.info("dir exists");
            } else {
               logger.info("the same name file exists, can not create dir");
            }
        } else {
           logger.info("dir not exists, create it ...");
            file.mkdir();
        }
    }
    // 判断文件是否存在
    public static void judeFileExists(File file) {

        if (file.exists()) {
           logger.info("file exists");
        } else {
           logger.info("file not exists, create it ...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
