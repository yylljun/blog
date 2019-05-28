package com.yl.blog.controller;


import com.yl.blog.entity.File;
import com.yl.blog.service.FileService;
import com.yl.blog.util.MD5Util;
import com.yl.blog.vo.Response;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;



    @GetMapping
    public ModelAndView list(@RequestParam(value = "async", required = false) boolean async,
                             @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                             @RequestParam(value = "name", required = false, defaultValue = "") String name, Model model) {
        Sort sort = new Sort(Sort.Direction.DESC,"uploadDate");
//
        Pageable pageable = PageRequest.of(pageIndex, pageSize,sort);
        Page<File> page = fileService.listFileLikeName(name, pageable);
        List<File> list = page.getContent(); // 当前所在页面数据列表
        System.out.println("fileSearch有请求");
        model.addAttribute("page", page);
        model.addAttribute("fileList", list);
        return new ModelAndView(async == true ? "files/list :: #mainContainerRepleace" : "files/list", "fileModel",
                model);
    }

    /**
     * 获取文件片信息
     *
     * @param id
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFile(@PathVariable String id) throws UnsupportedEncodingException {

        Optional<File> file = fileService.getFileById(id);

        if (file.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + new String(file.get().getName().getBytes("utf-8"), "ISO-8859-1"))
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "").header("Connection", "close")
                    .body(file.get().getContent().getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }

    }

    /**
     * 在线显示文件
     * @param id
     * @return
     */
    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFileOnline(@PathVariable String id) {

        Optional<File> file = fileService.getFileById(id);

        if (file.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + file.get().getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, file.get().getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "").header("Connection", "close")
                    .body(file.get().getContent().getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }

    }

    /**
     * 上传
     *
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        try {
            if (file.getName() == null)
                return ResponseEntity.ok().body(new Response(true, "上传失败,文件为空"));
            File f = new File(file.getOriginalFilename(), file.getContentType(), file.getSize(),
                    new Binary(file.getBytes()));
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            fileService.saveFile(f);
        } catch (IOException | NoSuchAlgorithmException ex) {
            return ResponseEntity.ok().body(new Response(true, "上传失败：" + ex.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "上传成功"));
    }


    /**
     * 上传接口
     *
     * @param file
     * @return
     */
    @PostMapping("/edupload")
    @ResponseBody
    public Map<String, Object> editorFileUpload(@RequestParam(value = "editormd-image-file") MultipartFile file) {
        System.out.println("有请阿牛");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        File returnFile = null;
        try {
            File f = new File(file.getOriginalFilename(), file.getContentType(), file.getSize(),
                    new Binary(file.getBytes()));
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            returnFile = fileService.saveFile(f);
            String path = "//" + serverAddress + ":" + serverPort + "/view/" + returnFile.getId();
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功！");
            resultMap.put("url", path);
            return resultMap;
        } catch (IOException | NoSuchAlgorithmException ex) {
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败！");
            return resultMap;
        }

    }



    /**
     * 删除文件
     *
     * @param id
     * @return
     */

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Response> deleteFile(@PathVariable String id) {

        try {
            fileService.removeFile(id);
            return ResponseEntity.ok().body(new Response(true, "处理成功"));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
    }
}
