package xuegao.practice.openNote.config.bean;

public class Region {
    private String exportPath;//地区文件的导出路径

    @Override
    public String toString() {
        return "Region{" +
                "exportPath='" + exportPath + '\'' +
                '}';
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }
}
