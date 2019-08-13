package app;

import hadoop.HadoopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author csieflyman
 */
@Slf4j
@Controller
public class HomeController {

    @Autowired
    private Environment env;

    @GetMapping
    public String home() {
        return "index";
    }

    @Autowired
    private HadoopService hadoopService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile, Model model) {
        log.info("================= Upload File to Hadoop =========================");

        String fileName = multipartFile.getOriginalFilename() != null ? multipartFile.getOriginalFilename() : "test.json";
        String uploadDir = env.getProperty("app.upload.dir");

        File file;
        try {
            file = new File(uploadDir + "/" + fileName);
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
        } catch (IOException e) {
            log.error("Upload File Failure", e);
            model.addAttribute("message", "Upload File Failure");
            return "index";
        }

        try {
            hadoopService.saveFile(file);
        } catch (Exception e) {
            log.error("Save File to Hadoop Failure", e);
            model.addAttribute("message", "Save File to Hadoop Failure");
            return "index";
        }

        model.addAttribute("message", "Upload File Success");
        return "index";
    }
}
