package apollo.modules;

import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import apollo.command_line.OptionalArgs;
import apollo.maven.MavenDeployOption;
import apollo.upload.MavenUploader;
import apollo.upload.Uploader;

public class MavenModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Uploader.class).to(MavenUploader.class);
        bind(new TypeLiteral<List<MavenDeployOption>>(){}).toProvider(MavenCommandsProvider.class);
        bind(String.class).annotatedWith(Names.named("deploy-start-command")).toInstance("deploy:deploy-file -q ");
        bind(OptionalArgs.class).asEagerSingleton();
    }
}
