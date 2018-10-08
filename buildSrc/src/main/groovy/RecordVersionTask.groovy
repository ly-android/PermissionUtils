import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class RecordVersionTask extends DefaultTask {

    RecordVersionTask() {
        group = 'allenTask'
        description = 'update verison info'
    }
    //执行阶段
    @TaskAction
    void doAction() {
        println 'the verison code is ' + project.extensions.extentions.versionCode
    }

}