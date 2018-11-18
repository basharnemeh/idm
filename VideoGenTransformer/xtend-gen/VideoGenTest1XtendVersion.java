import fr.istic.videoGen.VideoGeneratorModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class VideoGenTest1XtendVersion {
  @Test
  public void testLoadModel() {
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
    Assert.assertNotNull(videoGen);
    InputOutput.<String>println(videoGen.getInformation().getAuthorName());
  }
}
