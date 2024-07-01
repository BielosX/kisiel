import org.gradle.api.DefaultTask
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes.Node
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.Represent
import org.yaml.snakeyaml.representer.Representer

import static org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME
import static org.yaml.snakeyaml.DumperOptions.FlowStyle.BLOCK

class ResourcesMetadataTask extends DefaultTask {
    private final Yaml yaml

    ResourcesMetadataTask() {
        def options = new DumperOptions()
        options.setDefaultFlowStyle(BLOCK)
        yaml = new Yaml(new NullRepresenter(options))
    }

    @TaskAction
    void generateMetadata() {
        def main = project.extensions.getByType(SourceSetContainer).named(MAIN_SOURCE_SET_NAME).get()
        def resources = main.resources.srcDirs.stream().findFirst().orElseThrow()
        def structure = visit(resources)
        def result = yaml.dump(structure)
        def file = new File(main.output.resourcesDir, "metadata.yaml")
        file.text = result
    }

    private static Map<String, Object> visit(File directory) {
        def result = [:] as Map<String, Object>
        directory.listFiles().each { it ->
            if (it.isDirectory()) {
                result.put(it.name, visit(it))
            } else {
                result.put(it.name, null)
            }
        }
        return result
    }

    private class NullRepresenter extends Representer {
        NullRepresenter(DumperOptions options) {
            super(options)
            this.nullRepresenter = new RepresentNull()
        }

        private class RepresentNull implements Represent {
            Node representData(Object data) {
                return representScalar(Tag.NULL, "")
            }
        }
    }
}