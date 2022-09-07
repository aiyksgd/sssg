package dic.controller;


import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import dic.entity.User;
import dic.service.ISsmpDicService;
import dic.util.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;


    //国际化
    @RequestMapping(value = "i18n")
    public String i18n(Locale locale, Map<String, String> map) {
        map.put("lang", locale.toString());
        return "forward:/list";
    }

    @Autowired
    private ISsmpDicService userService;
    private List<User> deparList;

    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
        if (id != null) {
            map.put("user", userService.getData(id));

        }
    }

    //添加
    @RequestMapping(value = "users", method = RequestMethod.POST)
    public String add(User user, BindingResult result, Map<String, Object> map, @RequestParam(value = "picture", required = false) MultipartFile[] files) throws Exception {
        if (result.getErrorCount() > 0) {
            for (FieldError error : result.getFieldErrors()) {
                System.out.println(error.getField() + "  :  " + error.getDefaultMessage());
            }
            map.put("user", user);

            return "add";
        }
        for (MultipartFile file : files) {
            Set<MetaData> mataData = new HashSet<>();
            mataData.add(new MetaData("时间", String.valueOf(new Date())));
            mataData.add(new MetaData("上传用户", "aiyk"));
            String fun = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fun, null);
            System.out.println(storePath + "----------------------------------");
            user.setHead(storePath.getGroup());
            user.setFilename(storePath.getPath());
            user.setNamef(storePath.getPath().substring(storePath.getPath().lastIndexOf("/")).replace("/", ""));
            userService.add(user);
        }
//       String pathUrl = "C:\\Users\\aiyk\\Desktop\\git\\" + VerifyCodeUtil.randomCode() + System.currentTimeMillis() + files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf("."));
//        user.setHead(pathUrl);
//        userService.add(user);
        return "redirect:/list";
    }

    //from标签需要
    @RequestMapping(value = "users", method = RequestMethod.GET)
    public String input(Map<String, Object> map) {
        map.put("user", new User());
        return "add";

    }

    //删除
    @RequestMapping(value = "users/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Integer id) {
        User user = userService.getData(id);
        fastFileStorageClient.deleteFile(user.getHead(), user.getFilename());
        userService.remove(id);
        System.out.println(id);
        return "redirect:/list";
    }

    //修改
    @RequestMapping(value = "users", method = RequestMethod.PUT)
    public String update(User user, BindingResult result, Map<String, Object> map, @RequestParam(value = "picture", required = false) MultipartFile[] files) {
        if (result.getErrorCount() > 0) {
            for (FieldError error : result.getFieldErrors()) {
                System.out.println(error.getField() + "  :  " + error.getDefaultMessage());
            }
            map.put("user", user);
            return "add";
        }
        try {
            for (MultipartFile file : files) {
                Set<MetaData> mataData = new HashSet<>();
                mataData.add(new MetaData("修改时间", String.valueOf(new Date())));
                mataData.add(new MetaData("修改用户", "aiyk"));
                String fun = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fun, mataData);
                System.out.println(storePath + "----------------------------------");
                user.setHead(storePath.getGroup());
                user.setFilename(storePath.getPath());
                user.setNamef(storePath.getPath().substring(storePath.getPath().lastIndexOf("/")).replace("/", ""));
                // userService.add(user);
                userService.update(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/list";
    }

    //修改回显
    @RequestMapping(value = "users/{id}", method = RequestMethod.GET)
    public String getUpdate(@PathVariable("id") Integer id, Map<String, Object> map) {
        map.put("user", userService.getData(id));
        return "add";
    }


    //查询
    @RequestMapping(value = "list")
    public String select(Map<String, List<User>> map) {
        List<User> list = userService.select();
        map.put("list", list);
        System.out.println(list);
        return "list";
    }

    // 图片预览
    @RequestMapping(path = "picturePreview1")
    public void picturePreview(Integer id, HttpServletResponse response) throws Exception {
//        System.out.println(222);

        FileInputStream fis = new FileInputStream(userService.getData(id).getHead());
        ServletOutputStream out = response.getOutputStream();
        byte[] bt = new byte[1024];
        int length = 0;
        while ((length = fis.read(bt)) != -1) {
            out.write(bt, 0, length);
        }
        out.close();
        fis.close();

    }

    //查看元数据 图片预览
    @RequestMapping(value = "picturePreview/{id}")
    public void yuan(@PathVariable("id") Integer id) {
        User user = userService.getData(id);
        Set<MetaData> metaDataSet = fastFileStorageClient.getMetadata(user.getHead(), user.getFilename());
        for (MetaData metaData : metaDataSet) {
//            System.out.println(metaData.toString() + "--------------------------");
        }
    }

    // 上传
    @RequestMapping(path = "uploadfile1", method = RequestMethod.POST)
    public String upoadFile(@RequestParam("headFile") CommonsMultipartFile[] commonsmultipartfile) throws IOException {
        for (CommonsMultipartFile cmf : commonsmultipartfile) {
            System.out.println(111);
            InputStream is = cmf.getInputStream();
            String path = "C:\\Users\\aiyk\\Desktop\\git\\";
            File file = new File(path + cmf.getOriginalFilename());
            OutputStream os = new FileOutputStream(file);
            byte b[] = new byte[1024 * 1024 * 3];
            int length = 0;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
                os.flush();
            }
            os.close();
            is.close();
        }
        return "list";
    }

    // 上传
    @RequestMapping(value = "uploadfile")
    public String uploadfile1(User user, @RequestParam(value = "fill") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            String fun = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fun, null);
            System.out.println(storePath + "----------------------------------");
            user.setHead(storePath.getFullPath());
            userService.add(user);
        }
        return "redirect:/list";
    }

    // 下载
    @RequestMapping(path = "downloadfile")
    public void downloadFile(String path, String id, HttpServletResponse resp) throws IOException {
        if (path != "" || path != null) {
            String head = userService.getData(Integer.parseInt(id)).getHead();
            File file = new File(head);
            resp.setHeader("Content-Disposition", "attachment;Filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            resp.setContentType("application/octet-stream; charset=UTF-8");
            InputStream is = new FileInputStream(file);
            OutputStream os = resp.getOutputStream();
            byte[] b = new byte[1024 * 1024 * 5];

            int length = 0;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
                os.flush();
            }
            os.close();
            is.close();
        } else {
            String name = path.substring(path.lastIndexOf("\\") + 1);
            System.out.println(name + "--------" + path);
            resp.setHeader("Content-Disposition", "attachment;Filename=" + URLEncoder.encode(name, "UTF-8"));
            resp.setContentType("application/octet-stream; charset=UTF-8");

            File file = new File(path);
            InputStream is = new FileInputStream(file);
            OutputStream os = resp.getOutputStream();
            byte[] b = new byte[1024 * 1024 * 5];

            int length = 0;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
                os.flush();
            }
            os.close();
            is.close();
        }
    }

    @RequestMapping("aaaa/{id}")
    public void download(@PathVariable("id") Integer id, HttpServletResponse resp) throws Exception {
        User user = userService.getData(id);

        byte[] bytes = fastFileStorageClient.downloadFile(user.getHead(), user.getFilename(), new DownloadByteArray());

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);

        String a = dateString + "" + user.getNamef();
        resp.setHeader("Content-Disposition", "attachment;Filename=" + URLEncoder.encode(a, "UTF-8"));
        resp.setContentType("application/octet-stream; charset=UTF-8");
        OutputStream os = resp.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();
    }
}
