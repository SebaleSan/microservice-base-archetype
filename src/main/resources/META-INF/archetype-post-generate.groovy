import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

def projectDir = Paths.get(request.outputDirectory, request.artifactId)
def source = projectDir.resolve("gitignore")

if (Files.exists(source)) {
    Files.move(source, projectDir.resolve(".gitignore"), StandardCopyOption.REPLACE_EXISTING)
}
