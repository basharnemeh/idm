import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

public class VideoGenTestConcat {

	@Test
	public void testInJava1() throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File("output.mp4");
		file.delete();
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		assertNotNull(videoGen);
		// creating the txt file for the ffmpeg
		PrintWriter writer = new PrintWriter("mylist.txt", "UTF-8");
		boolean optional = (new Random()).nextBoolean();

		// we need to visit the video sequences and extract every location and add it to
		// the file
		// pour chaque media de videoGen
		for (Media media : videoGen.getMedias()) {
			// si c'est "mandatory" il faut absoulement jouer la video
			if (media instanceof MandatoryMedia) {
				String path = ((MandatoryMedia) media).getDescription().getLocation();
				writer.println("file " + "'" + path + "'");
			}
			if (media instanceof OptionalMedia) {
				// 50% of optionals will be showed
				optional = !optional;
				System.out.println("The optional video will be showed : " + optional);
				if (optional) {
					String path = ((OptionalMedia) media).getDescription().getLocation();
					writer.println("file " + "'" + path + "'");
				}
			}
			if (media instanceof AlternativesMedia) {
				AlternativesMedia alternative = ((AlternativesMedia) media);
				EList<MediaDescription> list = alternative.getMedias();
				int max = list.size() - 1;
				int choice = (new Random()).nextInt(max) + 1;
				MediaDescription desc = list.get(choice);
				String path = desc.getLocation();
				writer.println("file " + "'" + path + "'");
			}
		}
		execVLC("mylist.txt");
		writer.close();

	}

	public void execVLC(String filePath) {
		String command1 = "ffmpeg -f concat -i " + filePath + " -c copy output.mp4";
		String command2 = "vlc output.mp4";
		Process p;
		try {
			p = Runtime.getRuntime().exec(command1);
			p = Runtime.getRuntime().exec(command2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("VLC Started");
		

	}
}
