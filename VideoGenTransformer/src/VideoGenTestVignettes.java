import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoGeneratorModel;

public class VideoGenTestVignettes {

	@Test
	public void testInJava1() {
		String vignetteDirectoryPath = "/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/vignettes";

		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		assertNotNull(videoGen);
		System.out.println(videoGen.getInformation().getAuthorName());

		// pour chaque media de videoGen
		for (Media media : videoGen.getMedias()) {
			// si c'est "mandatory" il faut absoulement jouer la video
			if (media instanceof MandatoryMedia) {
				String path = ((MandatoryMedia) media).getDescription().getLocation();
				genererVignette(path, vignetteDirectoryPath);

			}
			if (media instanceof OptionalMedia) {

				String path = ((OptionalMedia) media).getDescription().getLocation();
				genererVignette(path, vignetteDirectoryPath);

			}
			if (media instanceof AlternativesMedia) {
				AlternativesMedia alternative = ((AlternativesMedia) media);
				EList<MediaDescription> list = alternative.getMedias();
				String alternativeDirectoryPath = vignetteDirectoryPath + "/alternatives";
				for (MediaDescription m : list) {
					genererVignette(m.getLocation(), alternativeDirectoryPath);
				}

			}
		}

	}

	public void genererVignette(String path, String vignetteDirectoryPath) {
		String imageName = path.substring(7);
		String command = "ffmpeg -y -i " + path + " -r 1 -t 00:00:01 -ss 00:00:05 -f image2 " + vignetteDirectoryPath
				+ "/" + imageName + ".png";
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
