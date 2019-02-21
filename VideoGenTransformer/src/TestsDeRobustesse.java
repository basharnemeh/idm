import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;

import org.eclipse.emf.common.util.URI;
import org.junit.Before;
import org.junit.Test;

import fr.istic.videoGen.VideoGeneratorModel;

public class TestsDeRobustesse {
	
	VideoGenTest vgt ;
	
	@Before
	public void init() {
		vgt= new VideoGenTest();
	}
	
	
	@Test
	public void testGetNumberOfVariants1() throws FileNotFoundException {
		// Test1
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test1.videogen"));
		assertNotNull(videoGen);
		vgt.getVariants_CSV_GIF(videoGen);
		assertEquals(vgt.variantes.size(), vgt.getNumberOfVariants(videoGen));

	}

	@Test
	public void testGetNumberOfVariants2() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test2.videogen"));
		assertNotNull(videoGen);
		vgt.getVariants_CSV_GIF(videoGen);
		assertEquals(vgt.variantes.size(), vgt.getNumberOfVariants(videoGen));
	}

	@Test
	public void testGetNumberOfVariants3() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test3.videogen"));
		assertNotNull(videoGen);
		vgt.getVariants_CSV_GIF(videoGen);
		assertEquals(vgt.variantes.size(), vgt.getNumberOfVariants(videoGen));
	}

	@Test
	public void testGetNumberOfVariants4() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test4.videogen"));
		assertNotNull(videoGen);
		vgt.getVariants_CSV_GIF(videoGen);
		assertEquals(vgt.variantes.size(), vgt.getNumberOfVariants(videoGen));
	}

	@Test
	public void testGetNumberOfVariants5() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test5.videogen"));
		assertNotNull(videoGen);
		vgt.getVariants_CSV_GIF(videoGen);
		assertEquals(vgt.variantes.size(), vgt.getNumberOfVariants(videoGen));
	}

	@Test
	public void testGetNumberOfVariants6() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test6.videogen"));
		assertNotNull(videoGen);
		vgt.getVariants_CSV_GIF(videoGen);
		assertEquals(vgt.variantes.size(), vgt.getNumberOfVariants(videoGen));
	}
	
	@Test
	public void testGetNumberOfVariants7() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test7.videogen"));
		assertNotNull(videoGen);
		vgt.getVariants_CSV_GIF(videoGen);
		assertEquals(vgt.variantes.size(), vgt.getNumberOfVariants(videoGen));
	}
	
	@Test
	public void testErreur1() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test_erreur1.videogen"));
		assertNotNull(videoGen);
		vgt.getVariants_CSV_GIF(videoGen);
		assertNotEquals(vgt.variantes.size(), vgt.getNumberOfVariants(videoGen));
	}
	



}
