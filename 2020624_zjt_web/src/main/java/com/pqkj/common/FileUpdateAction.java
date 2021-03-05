package com.pqkj.common;

import com.alibaba.fastjson.JSONObject;
import com.pq.framework.common.FilePropertiesBase;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.GZipUtil;
import com.pq.framework.util.RandomGUID;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.service.IAppVersionService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by L on 2018/3/30.
 */
@RestController
@RequestMapping("/mFileUpdate")
public class FileUpdateAction {
    private static final Logger logger = LoggerFactory.getLogger(FileUpdateAction.class);

    private static final String UNTITLED = "\\image\\untitled.png";

    @Autowired
    private IAppVersionService appVersionService;
    @ResponseBody
    @ApiOperation(value = "文件上传")
    @RequestMapping(value = "/file_upload", method = RequestMethod.POST)
    public DataResult fileUpload(@RequestParam("path") String path, HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("UTF-8");
            Map<String, Object> json = new HashMap<String, Object>();
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            /** 页面控件的文件流* */
            MultipartFile multipartFile = null;
            Map map = multipartRequest.getFileMap();
            for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                multipartFile = (MultipartFile) map.get(obj);
            }
            /** 获取文件的后缀* */
            String filename = multipartFile.getOriginalFilename();
            InputStream inputStream;
            String subPath = "", verCode = "", verName = "", vpackage = "", fileName = "", fileMd5 = "", oldFileName = "",filePath;
            try {
                inputStream = multipartFile.getInputStream();
                File tmpFile = File.createTempFile(filename,
                        filename.substring(filename.lastIndexOf(".")));
                // fileMd5 = Files.hash(tmpFile, Hashing.md5()).toString();
                FileUtils.copyInputStreamToFile(inputStream, tmpFile);
                String saveAsFileName = RandomGUID.getFileUUID() + FileUtil.getExtention(filename);
                String newFilePath = FileProperties.getValue("file.root")+path+"/";
                FileUtils.copyFile(tmpFile, new File(newFilePath, saveAsFileName));
                subPath = path;
                oldFileName = filename.substring(0, filename.lastIndexOf("."));
                fileName = saveAsFileName;
                filePath = newFilePath+fileName;
                tmpFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
                return DataResult.fail("上传失败");
            }
            json.put("subPath", subPath);
            json.put("fileName", fileName);
            json.put("oldFileName", filename);
            json.put("filePath", filePath);

            return DataResult.success(json);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.fail("上传失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/fileUploadVideo", method = RequestMethod.POST)
    public Map<String, Object> fileUploadVideo(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        File file2 = new File(FilePropertiesBase.getRootPath() + "sbyssp/");
        if (!file2.exists()) {
            file2.mkdir();
        }
        request.setCharacterEncoding("UTF-8");

        Map<String, Object> json = new HashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        /** 页面控件的文件流* */
        MultipartFile multipartFile = null;
        Map map = multipartRequest.getFileMap();
        for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            multipartFile = (MultipartFile) map.get(obj);

        }
        /** 获取文件的后缀* */
        String filename = multipartFile.getOriginalFilename();
        InputStream inputStream;
        String subPath = "", verCode = "", verName = "", vpackage = "", fileName = "", fileMd5 = "", oldFileName = "";
        try {
            inputStream = multipartFile.getInputStream();
            File tmpFile = File.createTempFile(filename,
                    filename.substring(filename.lastIndexOf(".")));
            // fileMd5 = Files.hash(tmpFile, Hashing.md5()).toString();
            FileUtils.copyInputStreamToFile(inputStream, tmpFile);

            String saveAsFileName = RandomGUID.getFileUUID() + FileUtil.getExtention(filename);
            FileUtils.copyFile(tmpFile, new File(FilePropertiesBase.getRootPath() + "sbyssp/", filename));
            subPath = "sbyssp";
            oldFileName = filename.substring(0, filename.lastIndexOf("."));
            fileName = saveAsFileName;

            tmpFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("subPath", subPath);
        json.put("fileName", fileName);
        json.put("oldFileName", oldFileName);
        return json;
    }


    /**
     * 上传文件
     *
     * @param request
     * @param response
     * @param file     上传的文件，支持多文件
     * @throws Exception
     */
    @RequestMapping("/files_upload")
    public void filesUpload(HttpServletRequest request, HttpServletResponse response
            , @RequestParam("file") MultipartFile[] file) throws Exception {
        if (file != null && file.length > 0) {
            //组合image名称，“;隔开”
            List<String> fileName = new ArrayList<String>();
            try {
                for (int i = 0; i < file.length; i++) {
                    if (!file[i].isEmpty()) {
                        //上传文件，随机名称，";"分号隔开
                        fileName.add(FileUtil.uploadImage(request, "/upload/" + RandomGUID.getFileUUID() + "/", file[i], true));
                    }
                }
                //上传成功
                if (fileName != null && fileName.size() > 0) {
                    logger.info("上传成功！");
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    @RequestMapping(value = "/fileDownload")
    public void filesDownload(HttpServletRequest request, HttpServletResponse response) {
        String fileName = request.getParameter("filename").trim();
        String realPath = request.getParameter("filepath").trim();
        logger.info(realPath);
        Boolean result = false;
        if (realPath.lastIndexOf("?") != -1) {
            realPath = realPath.substring(0, realPath.lastIndexOf("?"));
        }
        if (realPath != null && !StringUtil.isNullorEmpty(realPath) && !"undefined".equals(realPath)) {
            //当前是从该工程的WEB-INF//File//下获取文件(该目录可以在下面一行代码配置)然后下载到C:\\users\\downloads即本机的默认下载的目录
           /* String realPath = request.getServletContext().getRealPath(
                    "//WEB-INF//");*/
            File file = new File(FileProperties.getRootPath(realPath));
            if (StringUtil.isNullorEmpty(fileName)) {
                fileName = file.getName();
            }
            if (fileName.indexOf(".") == -1) {
                fileName += realPath.substring(realPath.lastIndexOf("."));
            }
            if (file.exists()) {
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    // 配置文件下载
                    response.setHeader("content-type", "application/octet-stream");
                    response.setContentType("application/octet-stream");
                    // 下载文件能正常显示中文
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

                    // 实现文件下载
                    byte[] buffer = new byte[1024];
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    //System.out.println("Download the song successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                    //System.out.println("Download the song failed!");
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        /*if (realPath != null&& !StringUtil.isNullorEmpty(realPath)) {
            //当前是从该工程的WEB-INF//File//下获取文件(该目录可以在下面一行代码配置)然后下载到C:\\users\\downloads即本机的默认下载的目录
           *//* String realPath = request.getServletContext().getRealPath(
                    "//WEB-INF//");*//*
            File file = new File(FileProperties.getRootPath(realPath));
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                String downloadFileName= null;
                try {
                    downloadFileName = new String((fileName+ FileUtil.getExtention(realPath)).getBytes(), "ISO-8859-1");
                    response.addHeader("Content-Disposition", "attachment; filename=\""
                            + downloadFileName + "\"");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                   logger.info("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }*/
    }

    @RequestMapping(value = "/fileDownloadBase64", method = RequestMethod.POST)
    public String fileDownloadBase64(HttpServletRequest request, HttpServletResponse response) {
        String strFileName = request.getParameter("filename");
        String strFilePath = request.getParameter("filepath").replace("ppsattachment_thum", "ppsattachment#thum");
        String imgFile = FileProperties.getRootPath(strFilePath);//待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }

    @RequestMapping(value = "/filesDownloadAndDelFile")
    public void filesDownloadAndDelFile(HttpServletRequest request, HttpServletResponse response) {
        logger.info("正在下载");
        String fileName = request.getParameter("filename");
        String realPath = request.getParameter("filepath");
        Boolean result = false;
        if (realPath.lastIndexOf("?") != -1) {
            realPath = realPath.substring(0, realPath.lastIndexOf("?"));
        }
        if (realPath != null && !StringUtil.isNullorEmpty(realPath)) {
            //当前是从该工程的WEB-INF//File//下获取文件(该目录可以在下面一行代码配置)然后下载到C:\\users\\downloads即本机的默认下载的目录
           /* String realPath = request.getServletContext().getRealPath(
                    "//WEB-INF//");*/
            File file = new File(FileProperties.getRootPath(realPath));
            if (file.exists()) {
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    // 配置文件下载
                    response.setHeader("content-type", "application/octet-stream");
                    response.setContentType("application/octet-stream");
                    // 下载文件能正常显示中文
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                    // 实现文件下载
                    byte[] buffer = new byte[1024];
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    result = true;
                    logger.info("Download the song successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("Download the song failed!");
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                try {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json; charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.print("fail");
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (result) {
                file.delete();
            }
        }
    }
    @PostMapping(value = "/DownloadApk")
    public JSONObject getDefaultApkVersion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject appVersion= null;
        try {
            appVersion = appVersionService.findVersionsTopOne("zjtAPK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appVersion;
    }


    @RequestMapping(value = "/apk_upload", method= RequestMethod.POST)
    public Map<String, Object> uploadApkFile(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setCharacterEncoding("UTF-8");

        Map<String, Object> json = new HashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        /** 页面控件的文件流* */
        MultipartFile multipartFile = null;
        Map map =multipartRequest.getFileMap();
        for (Iterator i = map.keySet().iterator(); i.hasNext();) {
            Object obj = i.next();
            multipartFile=(MultipartFile) map.get(obj);

        }
        /** 获取文件的后缀* */
        String filename = multipartFile.getOriginalFilename();
        InputStream inputStream;
        String subPath="",verCode="",verName="",vpackage="",fileName="",fileMd5="",oldFileName="";
        try {
            inputStream = multipartFile.getInputStream();
            File tmpFile = File.createTempFile(filename,
                    filename.substring(filename.lastIndexOf(".")));
            // fileMd5 = Files.hash(tmpFile, Hashing.md5()).toString();
            FileUtils.copyInputStreamToFile(inputStream, tmpFile);

            String saveAsFileName= "zjt_"+DateTimeUI.getCurrentDateTimeUUID() + FileUtil.getExtention(filename);
            FileUtils.copyFile(tmpFile, new File("/home/java/zjtWeb/files/apk/",saveAsFileName));
            //ApkInfo info = GetApkInfo.getApkInfoByFilePath(FileProperties.DIR_TEMP()+ saveAsFileName);
            // 上传UpYun后返回的path
            /*String versionGbk = AnalysisApk.unZip(tmpFile.getPath(), "")[0];
            byte[] versionNam = versionGbk.getBytes("iso8859-1");// 这里写转换前的编码方式
            newVersionName = new String(versionNam, "utf-8");// 这里写转换后的编码方式
            path = UpyunUtils.uploadApk(tmpFile, multipartFile.getOriginalFilename(), true, newVersionName);
            System.err.println(path);*/
            //verCode = info.getVersionCode();
            //verName =  info.getVersionName();
            //vpackage =  info.getPackageName();
            subPath = "apk";
            oldFileName = filename.substring(0,filename.lastIndexOf("."));
            fileName = saveAsFileName;
            tmpFile.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        json.put("verCode", verCode);
//        json.put("verName", verName);
//        json.put("vpackage",vpackage);
        json.put("subPath", subPath);
        json.put("fileName", fileName);
        json.put("appName", oldFileName);
        return json;
    }
    /**
     * 获取请求体中内容，转换为json
     * @return JSONObject
     */
    public JSONObject getStrResponseGzip(HttpServletRequest request){
        JSONObject json = new JSONObject();
        InputStream inputStream;
        String strResponse = "";
        try {
            inputStream = request.getInputStream();
            String strMessage = "";
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            while ((strMessage = reader.readLine()) != null) {
                strResponse += strMessage;
            }
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //System.out.println(GZipUtil.gunzip(strResponse));
            if(StringUtil.isNullorEmpty(strResponse) || strResponse.equals("{}")){
                json.put("isnew",true);
            }else{
                json = JSONObject.parseObject (GZipUtil.gunzip(strResponse));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//		try {
//			logger.log(SysLogLevel.REMIND , SysLogLevel.getApkLog( "压缩之前",json,strResponse));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        return json;
    }
}
