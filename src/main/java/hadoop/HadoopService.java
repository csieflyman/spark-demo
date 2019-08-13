package hadoop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author csieflyman
 */
@Slf4j
@Service
public class HadoopService {

    @Autowired
    private FsShell fsShell;

    public void saveFile(File file) {
        String dir = "demo";
        if(!fsShell.test(dir)) {
            fsShell.mkdir(dir);
            fsShell.chmodr(744, dir);
        }
        String dest = dir + "/" + file.getName();
        log.info("Hadoop copyFromLocal: from " + file.getAbsolutePath() + " to " + dest);
        fsShell.copyFromLocal(file.getAbsolutePath(), dest);
    }
}