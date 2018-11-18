import static org.junit.Assert.*;

import java.io.IOException;
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
import fr.istic.videoGen.impl.OptionalMediaImpl;

public class VideoGenTest1 {

	@Test
	public void testInJava1(){
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		assertNotNull(videoGen);
		System.out.println(videoGen.getInformation().getAuthorName());

		//pour chaque media de videoGen
		for (Media media : videoGen.getMedias()) {
			//si c'est "mandatory" il faut absoulement jouer la video
			if (media instanceof MandatoryMedia) {
				String path = 
						((MandatoryMedia) media).getDescription().getLocation();
				execVLC(path);
			}
			if(media instanceof OptionalMedia) {
				boolean bool = (new Random()).nextBoolean();
				System.out.println("The optional video will be showed : "+bool);
				if(bool) {
					String path = 
							((OptionalMedia) media).getDescription().getLocation();
					execVLC(path);
				}
			}
			if(media instanceof AlternativesMedia) {
				AlternativesMedia alternative = ((AlternativesMedia) media);
				EList<MediaDescription> list = alternative.getMedias();
				int max = list.size() - 1;
				int choice = (new Random()).nextInt(max)+1;
				MediaDescription desc = list.get(choice);
				String path = desc.getLocation();
				execVLC(path);
			}
		}

	}
	
	public void execVLC(String fileName)  {
		String command = "vlc "+fileName;
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("VLC Started");
		

	}

}
