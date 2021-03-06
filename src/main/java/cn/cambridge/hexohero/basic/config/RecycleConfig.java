package cn.cambridge.hexohero.basic.config;

import cn.cambridge.hexohero.basic.util.ArticleRecycleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RecycleConfig {
    private Logger logger = LoggerFactory.getLogger(ArticleRecycleUtil.class);

    private Map<String, Object> recycleMap;  // ConcurrentHashMap线程安全且锁粒度较小
    private String configFileName = ".recycleConfig";
    private String separator = java.io.File.separator;

    private DirectoryConfig directoryConfig;

    @Autowired
    @SuppressWarnings("unchecked")
    public RecycleConfig(DirectoryConfig directoryConfig) {
        this.directoryConfig = directoryConfig;
        // 在此处对回收站配置相关的recycleId和recycleMap进行初始化
        File configFile = new File(directoryConfig.getRecycle() + separator + configFileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(configFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.recycleMap = (ConcurrentHashMap<String, Object>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            logger.info("Recycle bin configuration file loaded successfully.");
        } catch (FileNotFoundException e) {
            logger.warn("Profile '" + configFile.getAbsolutePath() + "' not found.");
            this.recycleMap = new ConcurrentHashMap<>();
        } catch (Exception e) {
            logger.error(e.toString(), e);
            this.recycleMap = new ConcurrentHashMap<>();
        }
    }

    public void setRecycleMap(Map<String, Object> recycleMap) {
        this.recycleMap = recycleMap;
    }
    public  Map<String, Object> getRecycleMap() { return this.recycleMap; }

    /**
     * 获取唯一UUID标识
     * @return 唯一UUID标识
     */
    public String getUUId() { return UUID.randomUUID().toString(); }

    /**
     * 将回收站配置写入配置文件
     * @throws IOException 文件写入出现问题
     */
    public void updateConfig() throws IOException {
        File configFile = new File(directoryConfig.getRecycle() + separator + configFileName);
        FileOutputStream fileOutputStream = new FileOutputStream(configFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(recycleMap);
        objectOutputStream.close();
        fileOutputStream.close();
        logger.info("Recycle bin configuration file saved successfully.");
    }
}
