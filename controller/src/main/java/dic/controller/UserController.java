package dic.controller;


import dic.entity.Ssmp;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class UserController {

    //国际化
    @RequestMapping(value = "i18n")
    public String i18n(Locale locale, Map<String, String> map) {
        map.put("lang", locale.toString());
        return "forward:/list";
    }

    @Autowired
    private ISsmpDicService userService;
    private List<Ssmp> deparList;
    //添加
    @RequestMapping(value = "users", method = RequestMethod.POST)
    public String add(Ssmp user, BindingResult result, Map<String, Object> map, @RequestParam(value = "picture", required = false) MultipartFile files) throws Exception {
        if (result.getErrorCount() > 0) {
            for (FieldError error : result.getFieldErrors()) {
                System.out.println(error.getField() + "  :  " + error.getDefaultMessage());
            }
            map.put("user", user);
            return "add";
        }
        String pathUrl = "C:\\Users\\aiyk\\Desktop\\git\\" + VerifyCodeUtil.randomCode() + System.currentTimeMillis() + files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf("."));
        files.transferTo(new File(pathUrl));
        user.setHead(pathUrl);
//        if (user.getId() == null){
//            user.setId(0);
//        }
//        System.out.println(user);
        userService.add(user);
        return "redirect:/list";
    }

    //from标签需要
    @RequestMapping(value = "users", method = RequestMethod.GET)
    public String input(Map<String, Object> map) {
        map.put("user", new Ssmp());
        return "add";
    }

    //删除
    @DeleteMapping(value = "users/{id}")
    public String delete(@PathVariable("id") Integer id) {
        userService.remove(id);
        return "redirect:/list";
    }

    //修改

    @PutMapping(value = "users")
    public String update(Ssmp user, BindingResult result, Map<String, Object> map, @RequestParam(value = "picture", required = false) MultipartFile files) {
        if (result.getErrorCount() > 0) {
            for (FieldError error : result.getFieldErrors()) {
                System.out.println(error.getField() + "  :  " + error.getDefaultMessage());
            }
            map.put("user", user);
            return "add";
        }
     try {
        String pathUrl = "C:\\Users\\aiyk\\Desktop\\git\\" + VerifyCodeUtil.randomCode() + System.currentTimeMillis() + files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf("."));
        files.transferTo(new File(pathUrl));
        user.setHead(pathUrl);
        userService.update(user);

     }catch (Exception e){
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

    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
        if (id != null) {
            map.put("user", userService.getData(id));
        }
    }

    //查询
    @RequestMapping(value = "list")
    public String select(Map<String, List<Ssmp>> map) {
        List<Ssmp> list = userService.select();
        map.put("list", list);
        return "list";
    }

    // 图片预览
    @RequestMapping(path = "picturePreview")
    public void picturePreview(Integer id, HttpServletResponse response) throws Exception {
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

    // 上传
    @RequestMapping(path = "uploadfile", method = RequestMethod.POST)
    public String upoadFile(@RequestParam("headFile") CommonsMultipartFile[] commonsmultipartfile) throws IOException {
        for (CommonsMultipartFile cmf : commonsmultipartfile) {
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
}
