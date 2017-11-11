/**
 * 
 */
package bots;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

/**
 * @author Winnie Trandinh
 *
 */
public class PrototypeLXI extends Bot {
	
	String name = "PrototypeLXI";
	
	Image current;
	
	private int move = BattleBotArena.UP;

	private int counter = 0;
	
	BotHelper botHelper = new BotHelper();
	
	private double timeNeeded = -1;
	
	private int prevMove;
		
	/**
	 * 
	 */
	public PrototypeLXI() {
		
	}

	/* (non-Javadoc)
	 * @see bots.Bot#newRound()
	 */
	@Override
	public void newRound() {
		
	}

	/* (non-Javadoc)
	 * @see bots.Bot#getMove(arena.BotInfo, boolean, arena.BotInfo[], arena.BotInfo[], arena.Bullet[])
	 */
	@Override
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
		ArrayList<Integer> noMoves = new ArrayList<Integer>();
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		
		if (timeNeeded == -1) {
			double distanceToMove = RADIUS*3;
			timeNeeded = Math.ceil(distanceToMove/BattleBotArena.BOT_SPEED);
		}
		
		if (counter == 0) {
			double temp = Math.random();
			if (temp <= 0.25) {
				move = BattleBotArena.FIREUP;
			} else if (temp > 0.25 && temp <= 0.5) {
				move = BattleBotArena.FIRERIGHT;
			} else if (temp > 0.5 && temp <= 0.75) {
				move = BattleBotArena.FIREDOWN;
			} else if (temp > 0.75) {
				move = BattleBotArena.FIRELEFT;
			}
			prevMove = move;
			counter++;
			return move;
		}
		
		if (!shotOK || (prevMove > 4 && prevMove < 9) ) {
			noMoves = noFire(noMoves);
		}
		
		//edge cases
		if (me.getX() < BattleBotArena.LEFT_EDGE + BattleBotArena.BOT_SPEED*2) {
			noMoves = noMoveL(noMoves);
		}
		if (me.getX() > BattleBotArena.RIGHT_EDGE - BattleBotArena.BOT_SPEED*2) {
			noMoves = noMoveR(noMoves);
		}
		if (me.getY() < BattleBotArena.TOP_EDGE + BattleBotArena.BOT_SPEED*2) {
			noMoves = noMoveU(noMoves);
		}
		if (me.getX() > BattleBotArena.BOTTOM_EDGE - BattleBotArena.BOT_SPEED*2) {
			noMoves = noMoveD(noMoves);
		}

		
		//assuming coordinates start at top left corner**
		//ArrayList<Integer> bulletDir = new ArrayList<Integer>();
		for (Bullet bullet : bullets) {
			if (BotHelper.manhattanDist(me.getX(), me.getY(), bullet.getX(), bullet.getY() ) > BattleBotArena.BULLET_SPEED*10 ) {
				//break;
			}
			int bulletDir = -1;
			if(bullet.getXSpeed() < 0) {
				//left
				//bulletDir.add(3);
				bulletDir = 3;
			} else if(bullet.getXSpeed() > 0) {
				//right
				//bulletDir.add(1);
				bulletDir = 1;
			} else if(bullet.getYSpeed() < 0) {
				//up
				//bulletDir.add(0);
				bulletDir = 0;
			} else if(bullet.getYSpeed() > 0) {
				//down
				//bulletDir.add(2);
				bulletDir = 2;
			}
			//bullet from right side
			if (bulletDir == 3 && 
				bullet.getY() > me.getY()-RADIUS && 
				bullet.getY() < me.getY()+RADIUS*3 && 
				bullet.getX() > me.getX() ) {
				System.out.println("on right " + (bullet.getX() - me.getX() ) + " units away");
				//System.out.println("distance compared: " + ((BattleBotArena.BULLET_SPEED*timeNeeded) + RADIUS ));
				if (bullet.getX() - me.getX() <= ( (BattleBotArena.BULLET_SPEED*timeNeeded) + RADIUS) ) {
					//1 unit away from contact
					noMoves = noFire(noMoves);
					noMoves = noMoveR(noMoves);
					noMoves = noMoveL(noMoves);
					noMoves = noStay(noMoves);
				}
			}
			//bullet from left side
			if (bulletDir == 1 && 
				bullet.getY() > me.getY()-RADIUS && 
				bullet.getY() < me.getY()+RADIUS*3 && 
				bullet.getX() < me.getX() ) {
				System.out.println("on left " + (bullet.getX() - me.getX() ) + " units away");
				if (me.getX() - bullet.getX() <= ( (BattleBotArena.BULLET_SPEED*timeNeeded) + RADIUS) ) {
					//1 unit away from contact
					noMoves = noFire(noMoves);
					noMoves = noMoveR(noMoves);
					noMoves = noMoveL(noMoves);
					noMoves = noStay(noMoves);
				}
			}
			//bullet from below
			if (bulletDir == 0 && 
				bullet.getX() > me.getX()-RADIUS && 
				bullet.getX() < me.getX()+RADIUS*3 && 
				bullet.getY() > me.getY() ) {
				System.out.println("on below " + (bullet.getX() - me.getX() ) + " units away");
				if (bullet.getY() - me.getY() <= ( (BattleBotArena.BULLET_SPEED*timeNeeded) + RADIUS) ) {
					//1 unit away from contact
					noMoves = noFire(noMoves);
					noMoves = noMoveU(noMoves);
					noMoves = noMoveD(noMoves);
					noMoves = noStay(noMoves);
				}
			}
			//bullet from above
			if (bulletDir == 2 && 
				bullet.getX() > me.getX()-RADIUS && 
				bullet.getX() < me.getX()+RADIUS*3 && 
				bullet.getY() < me.getY() ) {
				System.out.println("on above " + (bullet.getX() - me.getX() ) + " units away");
				if (me.getY() - bullet.getY() <= ( (BattleBotArena.BULLET_SPEED*timeNeeded) + RADIUS) ) {
					//1 unit away from contact
					noMoves = noFire(noMoves);
					noMoves = noMoveU(noMoves);
					noMoves = noMoveD(noMoves);
					noMoves = noStay(noMoves);
				}
			}
		}
		
		possibleMoves = calcPossibleMoves(noMoves);
		
		//making choices
		double [] choices = new double [possibleMoves.size() ];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = 0;
		}
		if (possibleMoves.get(possibleMoves.size()-1) == 9) {
			//makes stay unfavourable
			choices[choices.length-1] = 5;
		}
		
		choices = calcDangers(choices, possibleMoves, bullets, me, deadBots);
		
		BotInfo target = BotHelper.findClosest(me, liveBots);
		choices = calcDesire(choices, possibleMoves, me, target);
		
		System.out.println(possibleMoves);
		for (int i = 0; i < choices.length; i++) {
			System.out.println("choice at " + i + " is " + choices[i]);
		}
		
		int idealMove = possibleMoves.size()-1;
		double lowestThreat = 100.;
		for (int i = 0; i < choices.length; i++) {
			if (choices[i] < lowestThreat) {
				lowestThreat = choices[i];
				idealMove = i;
			}
		}
		
		/*
		//does not work...
		if (possibleMoves.contains(prevMove) && prevMove < 5) {
			if (choices[possibleMoves.indexOf(prevMove) ] == lowestThreat) {
				idealMove = prevMove;
			}
		}*/
		move = possibleMoves.get(idealMove);
		prevMove = move;
		//move = possibleMoves.get(possibleMoves.size()-1);
		
		//System.out.println("no: " + noMoves);
		//System.out.println("yes: " + possibleMoves);
		//System.out.println("move: " + move);
		
		//ArrayList<Bullet> dangerousBullets = new ArrayList<Bullet>();
		
		
		/*
		if (bullets.length != bulletAmount) {
			for ()
		}
		for (int i = 0; i < bullets.length; i++) {
			bulletPos.get(i)[0] = bullets[i].getX();
			bulletPos.get(i)[2] = bullets[i].getY();
		}*/
		
		counter++;
		return move;
	}

	private ArrayList<Integer> noMoveL(ArrayList<Integer> noMoves) {
		if (!noMoves.contains(BattleBotArena.LEFT) ) {
			noMoves.add(BattleBotArena.LEFT);
		}
		return noMoves;
	}
	private ArrayList<Integer> noMoveR(ArrayList<Integer> noMoves) {
		if (!noMoves.contains(BattleBotArena.RIGHT) ) {
			noMoves.add(BattleBotArena.RIGHT);
		}
		return noMoves;
	}
	private ArrayList<Integer> noMoveU(ArrayList<Integer> noMoves) {
		if (!noMoves.contains(BattleBotArena.UP) ) {
			noMoves.add(BattleBotArena.UP);
		}
		return noMoves;
	}
	private ArrayList<Integer> noMoveD(ArrayList<Integer> noMoves) {
		if (!noMoves.contains(BattleBotArena.DOWN) ) {
			noMoves.add(BattleBotArena.DOWN);
		}
		return noMoves;
	}
	private ArrayList<Integer> noStay(ArrayList<Integer> noMoves) {
		if (!noMoves.contains(BattleBotArena.STAY) ) {
			noMoves.add(BattleBotArena.STAY);
		}
		return noMoves;
	}
	private ArrayList<Integer> noFireL(ArrayList<Integer> noMoves) {
		if (!noMoves.contains(BattleBotArena.FIRELEFT) ) {
			noMoves.add(BattleBotArena.FIRELEFT);
		}
		return noMoves;
	}
	private ArrayList<Integer> noFireR(ArrayList<Integer> noMoves) {
		if (!noMoves.contains(BattleBotArena.FIRERIGHT) ) {
			noMoves.add(BattleBotArena.FIRERIGHT);
		}
		return noMoves;
	}
	private ArrayList<Integer> noFireU(ArrayList<Integer> noMoves) {
		if (!noMoves.contains(BattleBotArena.FIREUP) ) {
			noMoves.add(BattleBotArena.FIREUP);
		}
		return noMoves;
	}
	private ArrayList<Integer> noFireD(ArrayList<Integer> noMoves) {
		if (!noMoves.contains(BattleBotArena.FIREDOWN) ) {
			noMoves.add(BattleBotArena.FIREDOWN);
		}
		return noMoves;
	}
	
	private ArrayList<Integer> noFire(ArrayList<Integer> noMoves) {
		noFireU(noMoves);
		noFireR(noMoves);
		noFireD(noMoves);
		noFireL(noMoves);
		return noMoves;
	}
	
	private ArrayList<Integer> calcPossibleMoves(ArrayList<Integer> noMoves) {
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		for (int i = 1; i < 10; i++) {
			possibleMoves.add(i);
		}
		for (int i = 0; i < noMoves.size(); i++) {
			int noMove = noMoves.get(i);
			if (possibleMoves.contains(noMove) ) {
				possibleMoves.remove(possibleMoves.indexOf(noMove) );
			}
		}
		return possibleMoves;
	}
	
	private double [] calcDangers(double [] choices, ArrayList<Integer> possibleMoves, Bullet[] bullets, BotInfo me, BotInfo[] deadBots) {
		if (possibleMoves.get(0) >= 5) {
			return choices;
		}
		//1 = above
		//2 = down
		//3 = left
		//4 = right
		double [] dangers = new double [4];
		//bullet checks
		for (Bullet bullet : bullets) {
			//vertical checks
			if (bullet.getX() > me.getX()-50 && bullet.getX() < me.getX()+50) {
				if (bullet.getY() < me.getY() && bullet.getY() > me.getY()-50) {
					//above
					double distance = BotHelper.calcDistance(me.getX(), me.getY(), bullet.getX(), bullet.getY() );
					dangers[0] = 8 - distance/10;
				}
				if (bullet.getY() > me.getY() && bullet.getY() < me.getY()+50) {
					//below
					double distance = BotHelper.calcDistance(me.getX(), me.getY(), bullet.getX(), bullet.getY() );
					dangers[1] = 8 - distance/10;
				}
			}
			//horizontal checks
			if (bullet.getY() > me.getY()-50 && bullet.getY() < me.getY()+50) {
				if (bullet.getX() < me.getX() && bullet.getX() > me.getX()-50) {
					//left
					double distance = BotHelper.calcDistance(me.getX(), me.getY(), bullet.getX(), bullet.getY() );
					dangers[2] = 8 - distance/10;
				}
				if (bullet.getX() > me.getX() && bullet.getX() < me.getX()+50) {
					//right
					double distance = BotHelper.calcDistance(me.getX(), me.getY(), bullet.getX(), bullet.getY() );
					dangers[3] = 8 - distance/10;
				}
			}
		}
		
		//edge checks
		if (me.getY() < (BattleBotArena.TOP_EDGE + 100) ) {
			//top edge
			dangers[0] += ( ( (BattleBotArena.TOP_EDGE + 100) - me.getY() )/20);
		}
		if (me.getY() > (BattleBotArena.BOTTOM_EDGE-100) ) {
			//bottom edge
			dangers[1] += ( (me.getY()-(BattleBotArena.BOTTOM_EDGE-100) )/20);
		}
		if (me.getX() < (BattleBotArena.LEFT_EDGE + 100)) {
			//left edge
			dangers[2] += ( ( (BattleBotArena.LEFT_EDGE + 100) - me.getX() )/20);
		}
		if (me.getX() > (BattleBotArena.RIGHT_EDGE-100) ) {
			//right edge
			dangers[3] += ( (me.getX()-(BattleBotArena.RIGHT_EDGE-100) )/20);
		}
		
		//grave checks
		for (BotInfo bot : deadBots) {
			//vertical checks
			if (bot.getX() > me.getX()-100 && bot.getX() < me.getX()+100) {
				if (bot.getY() < me.getY() && bot.getY() > me.getY()-100) {
					//above
					double distance = BotHelper.calcDistance(me.getX(), me.getY(), bot.getX(), bot.getY() );
					dangers[0] = 7.1 - distance/20;
				}
				if (bot.getY() > me.getY() && bot.getY() < me.getY()+100) {
					//below
					double distance = BotHelper.calcDistance(me.getX(), me.getY(), bot.getX(), bot.getY() );
					dangers[1] = 8 - distance/10;
				}
			}
			//horizontal checks
			if (bot.getY() > me.getY()-100 && bot.getY() < me.getY()+100) {
				if (bot.getX() < me.getX() && bot.getX() > me.getX()-100) {
					//left
					double distance = BotHelper.calcDistance(me.getX(), me.getY(), bot.getX(), bot.getY() );
					dangers[2] = 7.1 - distance/20;
				}
				if (bot.getX() > me.getX() && bot.getX() < me.getX()+100) {
					//right
					double distance = BotHelper.calcDistance(me.getX(), me.getY(), bot.getX(), bot.getY() );
					dangers[3] = 7.1 - distance/20;
				}
			}
		}
		
		for (int i = 0; i < possibleMoves.size(); i++) {
			if (possibleMoves.get(i) < 5) {
				choices[i] += dangers[possibleMoves.get(i)-1];
			} else {
				break;
			}
		}
		return choices;
	}
	
	//desire
	private double [] calcDesire(double[] choices, ArrayList<Integer> possibleMoves, BotInfo me, BotInfo target) {
		double [] desires = new double [8];
		double xDif = me.getX()-target.getX();
		double yDif = me.getY()-target.getY();
		//positive x = target on left
		//negative x = target on right
		//positive y = target on top
		//negative y = target on bottom
		double framesToTarget = Math.ceil(xDif/BattleBotArena.BULLET_SPEED);
		double idealDistanceY = framesToTarget * BattleBotArena.BOT_SPEED;
		
		//if (target.getLastMove() == )
		if (yDif > idealDistanceY) {
			desires[0] = -3;
		}
		if (yDif < -idealDistanceY) {
			desires[1] = -3;
		}
		if (xDif > 100) {
			desires[2] = -3.01;
		}
		if (xDif < -100) {
			desires[3] = -3.01;
		}
		//cannot shoot up or down...
		//System.out.println("xDif = " + xDif + " yDif = " + yDif + " ideal = " + idealDistance);
		if (yDif > -idealDistanceY && yDif < idealDistanceY && xDif > -100 && xDif < 100) {
			//fire left
			desires[6] = -2;
		}
		if (yDif < -idealDistanceY && yDif > idealDistanceY && xDif > -100 && xDif < 100) {
			//fire right
			desires[7] = -2;
		}
		/*
		if (xDif < 100 && xDif > 0) {
			desires[3] = -1.01;
		}
		if (xDif > -100 && xDif < 0) {
			desires[2] = -1.01;
		}*/
		
		
		for (int i = 0; i < possibleMoves.size(); i++) {
			if (possibleMoves.get(i) < 9) {
				choices[i] += desires[possibleMoves.get(i)-1];
			} else {
				break;
			}
		}
		
		//not in no fire state
		if (possibleMoves.get(possibleMoves.size()-1) >= 5 && possibleMoves.get(possibleMoves.size()-1) < 9) {
			
		}

		return choices;
	}
	
	/* (non-Javadoc)
	 * @see bots.Bot#draw(java.awt.Graphics, int, int)
	 */
	@Override
	public void draw(Graphics g, int x, int y) {
		g.drawImage(current, x, y, Bot.RADIUS*2, Bot.RADIUS*2, null);
	}

	/* (non-Javadoc)
	 * @see bots.Bot#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see bots.Bot#getTeamName()
	 */
	@Override
	public String getTeamName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bots.Bot#outgoingMessage()
	 */
	@Override
	public String outgoingMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bots.Bot#incomingMessage(int, java.lang.String)
	 */
	@Override
	public void incomingMessage(int botNum, String msg) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bots.Bot#imageNames()
	 */
	@Override
	public String [] imageNames() {
		//String[] images = {"roomba_up.png","roomba_down.png","roomba_left.png","roomba_right.png"};
		String [] images = {"Spider.png"};
		return images;
	}

	/* (non-Javadoc)
	 * @see bots.Bot#loadedImages(java.awt.Image[])
	 */
	@Override
	public void loadedImages(Image[] images) {
		if (images != null)
		{
			//current = up = images[0];
			//down = images[1];
			//left = images[2];
			//right = images[3];
			current = images[0];
		}
	}

}
