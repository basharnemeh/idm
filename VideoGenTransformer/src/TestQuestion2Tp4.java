

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import fr.istic.videoGen.VideoGeneratorModel;

public class TestQuestion2Tp4 {
	
	@Test
	public void test1() throws FileNotFoundException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test1.videogen"));
		assertNotNull(videoGen);
		VideoGenTp3 vg = new VideoGenTp3();
		vg.getVideosInformations(videoGen);
		assertEquals(vg.variantes.size(),VideoGenTp4.getNumberOfVariants(videoGen));
		
	}
	
	@Test
	public void test2() throws FileNotFoundException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test2.videogen"));
		assertNotNull(videoGen);
		VideoGenTp3 vg = new VideoGenTp3();
		vg.getVideosInformations(videoGen);
		assertEquals(vg.variantes.size(),VideoGenTp4.getNumberOfVariants(videoGen));
		
	}
	
	@Test
	public void test3() throws FileNotFoundException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test3.videogen"));
		assertNotNull(videoGen);
		VideoGenTp3 vg = new VideoGenTp3();
		vg.getVideosInformations(videoGen);
		assertEquals(vg.variantes.size(),VideoGenTp4.getNumberOfVariants(videoGen));
		
	}
	
	@Test
	public void test4() throws FileNotFoundException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test4.videogen"));
		assertNotNull(videoGen);
		VideoGenTp3 vg = new VideoGenTp3();
		vg.getVideosInformations(videoGen);
		assertEquals(vg.variantes.size(),VideoGenTp4.getNumberOfVariants(videoGen));
		
	}
	@Test
	public void test6() throws FileNotFoundException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test6.videogen"));
		assertNotNull(videoGen);
		VideoGenTp3 vg = new VideoGenTp3();
		vg.getVideosInformations(videoGen);
		assertEquals(vg.variantes.size(),VideoGenTp4.getNumberOfVariants(videoGen));
		
	}
	@Test
	public void test7() throws FileNotFoundException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test7.videogen"));
		assertNotNull(videoGen);
		VideoGenTp3 vg = new VideoGenTp3();
		vg.getVideosInformations(videoGen);
		assertEquals(vg.variantes.size(),VideoGenTp4.getNumberOfVariants(videoGen));
		
	}
	

}
