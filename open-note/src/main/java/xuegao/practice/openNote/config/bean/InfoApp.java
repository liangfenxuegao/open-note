package xuegao.practice.openNote.config.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "info.app")
public class InfoApp {
    private String moduleName;//当前模块名
    private Region region;//关于地区的配置

    @Override
    public String toString() {
        return "InfoApp{" +
                "moduleName='" + moduleName + '\'' +
                ", region=" + region +
                '}';
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
