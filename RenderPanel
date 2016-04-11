import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RenderPanel extends JPanel{

	double iterator = 0;//start at alpha zero
	public BufferedImage transformKauseImage(BufferedImage img){
		iterator += Math.PI / 14;//increase the iterator by pi/14 every time
		double alpha = 100 * Math.sin(iterator) + 100;//multiply the amplitude by 100 and
													  //shift up by the same amount so we
													  //get no negative alpha values
		byte a = (byte)((int)alpha);//cast the sin result to an int and make sure the int is < 255 and > 0 by casting to a byte
		alpha %= 0xff;//if the int is above 255, get the remainder
	    for (int cx=0;cx<img.getWidth();cx++) {//iterate over width pixels
	        for (int cy=0;cy<img.getHeight();cy++) {//iterate over height pixels
	            int color = img.getRGB(cx, cy);//get the RGB of the current pixel
	            int mc = (a << 24) | 0x00ffffff;//bitshift alpha and bitwise add it to a white hex color code
	            int newcolor = color & mc;//take the first 4 bits of color and the last 4 bits of the modified color
	            img.setRGB(cx, cy, newcolor);//set the new RGB at (cx, cy) to newcolor
	        }
	    }
		return img; //returns pic
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Snake snake = Snake.snake;
		try {
			BufferedImage i = ImageIO.read(new File(System.getProperty("user.dir") + "/test.png"));
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(i, 0, 0, this);
			
			BufferedImage kause = ImageIO.read(new File(System.getProperty("user.dir") + "/kause.png"));
			transformKauseImage(kause);
			g2d.drawImage(kause, 150, 115, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.setColor(Color.GREEN);

		for (Point point : snake.snakeParts){
			g.fillRect(point.x * Snake.SCALE, point.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
		}
		g.fillRect(snake.head.x * Snake.SCALE, snake.head.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
		g.setColor(Color.RED);
		g.fillRect(snake.cherry.x * Snake.SCALE, snake.cherry.y * Snake.SCALE, Snake.SCALE, Snake.SCALE);
		String string = "Score: " + snake.score + ", Length: " + snake.tailLength;
		g.setColor(Color.white);
		g.drawString(string, (int) (getWidth() / 2 - string.length() * 2.5f), 10);
		string = "Game Over!";
		
		if (snake.over){
			g.drawString(string, (int) (getWidth() / 2 - string.length() * 2.5f), (int) snake.dim.getHeight() / 4);
		}

		string = "Paused!";

		if (snake.paused && !snake.over){
			g.drawString(string, (int) (getWidth() / 2 - string.length() * 2.5f), (int) snake.dim.getHeight() / 4);
		}
		
		Toolkit.getDefaultToolkit().sync();
        g.dispose();
	}
}
