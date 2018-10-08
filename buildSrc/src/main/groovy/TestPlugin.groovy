import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println 'hello test plugin ' + project.name
        //创建扩展属性
        project.extensions.create('extentions', Extentions)
        //创建自定义task
        project.tasks.create('RecordVersionTask', RecordVersionTask)

        //自定义transform
        project.android.registerTransform(new MyClassTransform(project))
    }
}