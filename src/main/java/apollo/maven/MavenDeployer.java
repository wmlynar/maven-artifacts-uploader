package apollo.maven;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.wmlynar.execlibs.ConsoleProcessExecutor;
import com.google.inject.Inject;

public class MavenDeployer {

    private final MavenCommandFactory mavenCommandFactory;
    private static final Logger logger = LogManager.getLogger(MavenDeployer.class);

    @Inject
    public MavenDeployer(MavenCommandFactory mavenCommandFactory) {
        this.mavenCommandFactory = mavenCommandFactory;
    }

    public void deployArtifact(Path pathToPom) {
        String commandToExecute = mavenCommandFactory.getMavenDeployCommand(pathToPom);
        
        String command = "mvn " + commandToExecute;
        System.out.println("Executing command: " + command);
        
        ConsoleProcessExecutor processExecutor = new ConsoleProcessExecutor();
        processExecutor.start(command);
        try {
            processExecutor.waitFor();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        
//        CommandLine cmdLine = CommandLine.parse(command);
//        DefaultExecutor executor = new DefaultExecutor();
//        try {
//			int exitValue = executor.execute(cmdLine);
//		} catch (IOException e) {
//			logger.error(e.getMessage(), e);
//		}

    }
}
