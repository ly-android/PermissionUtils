//与插件传递参数
class Extentions {
    String versionCode
    String versionName


    @Override
    public String toString() {
        return "{" +
                "versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                '}'
    }
}