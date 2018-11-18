import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
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

public class VideoGenTestDureeMax {

	Map<String, String> videoLength = new HashMap<String, String>();

	@Test
	public void testInJava1() throws IOException, InterruptedException {
		File file = new File("output.mp4");
		file.delete();
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));

		assertNotNull(videoGen);
		// creating the txt file for the ffmpeg
		PrintWriter writer = new PrintWriter("mylist.txt", "UTF-8");
		for (Media media : videoGen.getMedias()) {
			// si c'est "mandatory" il faut absoulement jouer la video
			if (media instanceof MandatoryMedia) {
				String path = ((MandatoryMedia) media).getDescription().getLocation();
				writer.println("file " + "'" + path + "'");
			}
			if (media instanceof OptionalMedia) {
				// Take all the optional videos
				String path = ((OptionalMedia) media).getDescription().getLocation();
				writer.println("file " + "'" + path + "'");

			}

			if (media instanceof AlternativesMedia) {
				AlternativesMedia alternative = ((AlternativesMedia) media);
				EList<MediaDescription> list = alternative.getMedias();
				String path = getTheLongestAlternative(list);
				writer.println("file " + "'" + path + "'");
			}
		}
		execVLC("mylist.txt");
		writer.close();
	}

	// Get the longest video in alternatives
	public String getTheLongestAlternative(EList<MediaDescription> list) throws IOException, InterruptedException {
		String line = "";
		String result = "";
		for (MediaDescription md : list) {
			String filePath = md.getLocation();
			System.out.println(filePath);
			String command = "ffmpeg -i " + filePath;
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			double maxDuration = 0;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("Duration")) {
					String string1 = line.substring(12);
					String string2 = string1.substring(0, 11);
					System.out.println(string2);
					String[] splitedDuration = string2.split(":");
					double hours = Double.parseDouble(splitedDuration[0]) * 3600;
					double minutes = Double.parseDouble(splitedDuration[1]) * 60;
					double seconds = Double.parseDouble(splitedDuration[2]);
					double duration = hours + minutes + seconds;
					System.out.println(duration);
					if ((Double.compare(duration, maxDuration)) > 0) {
						maxDuration = duration;
						result = filePath;
					}

				}
			}
		}
		System.out.println("the result is " + result);
		return result;
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
