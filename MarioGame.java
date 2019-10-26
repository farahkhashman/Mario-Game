import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MarioGame extends JPanel implements KeyListener{
	
	private final int width = 800, height = 600;
	public final int floorHeight = height-250;
	private float JumpStrength;
	
	private int x = 100;
	private int y = floorHeight;
	private int imgwidth = 50, imgheight = 50;
	private int speed = 10;
	
	private double speedy = 0, speedx = 0;
	
	private int landSpeedY = 0, landSpeedX = 0; 
	
	private final int rectwidth = 60, rectheight = 25;
	private int rectX = 400, rectY = floorHeight+25;
	
	private int shroomspeedX = 4, count = 0, counter = 0;
	private int shroomX = 1450, shroomY = floorHeight-5;
	
	Rectangle mario = new Rectangle(x,y, 50,50);
	Rectangle shroom = new Rectangle(shroomX,shroomY, 70,70);
	ArrayList<Shape> shapes = new ArrayList<>();
	private boolean collide = false, over = false, shows = false, fall = false;;
	
	
	Image img, smallerImg;
	Image block, smallblock;
	Image bg, smallbg;
	Image shrooms, smallshroom; 
	
	
	public MarioGame() {
		JFrame frame = new JFrame();
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		this.setFocusable(true);
		this.addKeyListener(this);
		
		img = Toolkit.getDefaultToolkit().createImage("mario.png");
		smallerImg = img.getScaledInstance(imgwidth, imgheight, Image.SCALE_SMOOTH);
		
		shrooms = Toolkit.getDefaultToolkit().getImage("shroom.png");
		smallshroom = shrooms.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		
		bg= Toolkit.getDefaultToolkit().getImage("bck.png");
		smallbg = bg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		
		
		Rectangle2 l = new Rectangle2(0, floorHeight+50, 1200, 2, "rectangle");
		shapes.add(l);
		Rectangle2 k = new Rectangle2(1240, floorHeight+50, 400, 2, "rectangle");
		shapes.add(k);
		Rectangle2 t = new Rectangle2(1700, floorHeight+50, 600, 2, "rectangle");
		shapes.add(t);
		Rectangle2 r = new Rectangle2(rectX, rectY, rectwidth, rectheight, "rectangle");
		shapes.add(r);
		Rectangle2 m = new Rectangle2(rectX*2, rectY, rectwidth, rectheight, "rectangle");
		shapes.add(m);
		Rectangle2 n = new Rectangle2(m.x+m.width, rectY-rectheight, rectwidth, rectheight*2, "rectangle");
		shapes.add(n);
		
		
		

		//add mushroom, moves back and forth in a specific area, if intersects with mario, he gets 4px bigger
		//add mario enemy as a block
		
		run();
	}
	
	public void run() {
		while(true) {
			repaint();
			

			mario.x += speedx;
			
			gravity();
			
			collide = false;
			for(int i = 3; i<shapes.size(); i++) {
				if(mario.x +imgwidth == (shapes.get(i)).x)  {
					collide = true;
				}
			}
			 if (!collide)
				for (Shape s : shapes) {
					s.x += landSpeedX;
					s.y += landSpeedY;
				}
			 
			 if(mario.intersects(shroom) && mario.y == y) {
				 shroomspeedX = 0;
				 over = true;
			 }
			 
			 if(shroom.x > 0 && shroom.x < 800) {
				 shows = true;
			 }
			 
			 if(shows) {
				 if (count%100 < 50)
					 shroom.x += shroomspeedX;
				 
			 	else {
					 shroom.x -=shroomspeedX;
				 }	 
			 	count ++;
			 }
			 
			 if(mario.intersects(shroom) && mario.y <= shroom.y+3) 
				 fall = true;
			
			if(fall) {
				 if(counter<1000) {
					 shroomspeedX = 0;
					 shroom.y += 10;
				 }
				 counter++;
			 }
			 
			 shroom.x +=landSpeedX;
			 
			try {
				Thread.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 }
			
	}

	private void gravity() {
		
		//speed y gets more negative
		if(mario.y<floorHeight+30)
			speedy-=1;

		
		//if mario is intersecting any things then speed y = 0
		for(Shape s : shapes) {
			if(mario.intersects(s))
				speedy = 0;
		}
		
		// marios y increases by the speed
		mario.y-=speedy;
		
	}
	
	public void paint(Graphics g) {
		g.drawImage(smallbg, 0, 0, null);
		g.drawImage(smallerImg, (mario.x), mario.y, this);
		g.drawImage(smallshroom, shroom.x, shroom.y, this);
		
		for(int i = 0; i<shapes.size(); i++) {
			shapes.get(i).draw(g);
		}
		
		if(over) {
			g.setColor(Color.RED);
			g.drawString("Game Over!", width/2, height/2);
		}
	
	}

	public static void main(String[] args) {
		MarioGame test = new MarioGame(); 
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {			
			JumpStrength = 40;
			mario.y-=JumpStrength;
			mario.x+=10;
			
			if (mario.y > floorHeight) {
				over=true;
			}
			
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			if(!collide)
				landSpeedX=-speed;
			
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			landSpeedX= speed;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			landSpeedX = 0;
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			landSpeedX = 0;

		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}

}

//rejected code
/*for(int j = 0; j<shapes.size(); j++) {
if(shapes.get(j).type == "line") {
	shapes.get(j).x -= 30;
	shapes.get(j).width -= 30;
	shapes.get(j).y += JumpStrength;
	shapes.get(j).height += JumpStrength;
	
}
else {
	shapes.get(j).y += JumpStrength;
	shapes.get(j).x -= 30;
}


}*/

/*if(x>=c.x+c.width && y<=c.y && c.y >= floorHeight+50) {
if(c.type.equals("line")) {
	c.y-=JumpStrength;
	c.height-=JumpStrength;
	c.x+=30;
	c.width+=30;
}
else {
c.y-=JumpStrength;
}
}
*/

/*for(Shape g: shapes) {
if(mario.x+imgwidth==g.x) {
	collide = true;
}
else {
	collide=false;
}
}
*/

/*
if(mario.x==c.x+c.width && mario.y<=c.y && mario.y >= floorHeight+50) {
	collide = false;
	y+=30;
	x+=30;
}
}
*/

/*

				for(int i = 1; i<shapes.size(); i++) {
				if(x>=(shapes.get(i).x+shapes.get(i).width) && y<=floorHeight) {
					landSpeedX-=10;
					speedy+=JumpStrength;
*/

/*
for(Shape c : shapes) {
	if((x+imgwidth)==(c.x) && y==(c.y-c.height)) {
		collide = true;
		landSpeedX = 0;	
	}
}

for(Shape k : shapes) {
	if(!((x+imgwidth)>=k.x) && y!=(k.y-k.height)) {
		collide = false;	
	}
}

for(int i = 3; i<shapes.size(); i++) {
	if((x+imgwidth) < ((shapes.get(i).x) + (shapes.get(i).width)) && (x+imgwidth)>shapes.get(i).x && y+imgwidth<=shapes.get(i).y) {
		ontop=true;
		collide = false;
	}
}



if(ontop) {
	for(int i = 3; i<shapes.size(); i++) {
		if((x+(imgwidth/2)) == (shapes.get(i).x+shapes.get(i).width) && y+imgwidth<=shapes.get(i).y) {
			x += (shapes.get(i).width - 10);
			y = floorHeight;
			ontop = false;
			collide = false;
		}
	}
}
	
if(jump) {
	JumpStrength -= weight;
	x+=1;
}
*/

//if(mario.y+imgwidth==floorHeight+50)
//	speedy=0;