package tank;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 播放声音的类
 */
public class AudioPlayer extends Thread {
	
	private String fileName;

	public AudioPlayer(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	public void run(){
		
		File soundFile = new File(fileName);
		AudioInputStream ais = null;
		
		
		try {
			ais = AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AudioFormat format = ais.getFormat();
		SourceDataLine auline = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		
		try {
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		auline.start();
		int nBytesRead = 0;
		//这是缓冲
		byte[] abData = new byte[1024];
		
		try {
			while(nBytesRead != -1){
				nBytesRead = ais.read(abData, 0, abData.length);
				if(nBytesRead >= 0)
					auline.write(abData, 0, nBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			auline.drain();
			auline.close();
		}
	}
}