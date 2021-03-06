/* BowsersBigBeanBurrito
 * Desc: 2D platformer + shooter
 * Author: Preston Tom-Ying ICS3U
 * Jan. 2022  Version 1.0
 */

// ORIGINAL

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.Graphics;
import javax.imageio.*;
import javax.swing.*;
import java.io.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.sound.sampled.*;

public class BowsersBigBeanBurrito{ 
  // Game Window properties
  static JFrame gameWindow;
  static GraphicsPanel canvas;
  static final int WIDTH = 1500;
  static final int HEIGHT = 1000;
  // key listener
  static MyKeyListener keyListener = new MyKeyListener();
  // mouse listeners
  static MyMouseListener mouseListener = new MyMouseListener();
  static MyMouseMotionListener mouseMotionListener = new MyMouseMotionListener(); 
  
  //--------------------------------------------------------------------------
  // declare the properties of all game objects here
  //--------------------------------------------------------------------------
  
  // level variables
  static final int CUT_SCENE00 = -4;
  static final int CUT_SCENE01 = -5;
  static final int CUT_SCENE02 = -6;
  static final int CUT_SCENE10 = -7;
  static final int CUT_SCENE11 = -8;
  static final int CUT_SCENE20 = -9;
  static final int CUT_SCENE21 = -10;
  static final int CUT_SCENE22 = -11;
  static final int CUT_SCENE30 = -12;
  static final int CUT_SCENE40 = -13;
  static final int CUT_SCENE41 = -14;
  static final int SETTINGS = -3;
  static final int TITLE_SCREEN = -2;
  static final int GAME_OVER = -1;
  static final int MAIN_MENU = 0;
  static final int LEVEL_ONE = 1;
  static final int LEVEL_TWO = 2;
  static final int LEVEL_THREE = 3;
  static final int LEVEL_FOUR = 4;
  static final int LEVEL_FIVE = 5;
  // Make level = LEVEL_ONE for testing purposes (switch to title screen later)
  static int level = TITLE_SCREEN;
  
  // cutscene variables
  static BufferedImage[] cutScene0 = new BufferedImage[3];
  static BufferedImage[] cutScene1 = new BufferedImage[2];
  static BufferedImage[] cutScene2 = new BufferedImage[3];
  static BufferedImage[] cutScene3 = new BufferedImage[1];
  static BufferedImage[] cutScene4 = new BufferedImage[2];
  static long lastSceneSwap = System.currentTimeMillis();
  
  // Title, Menu, and Game Over Screen
  static BufferedImage titleScreen = null;
  static BufferedImage menuScreen = null;
  static BufferedImage menuImage = null;
  static BufferedImage gameOverScreen = null;
  // font for main menu
  static Font largeFont = new Font("Calibri", Font.BOLD, 100);
  static Font smallFont = new Font("Calibri", Font.BOLD, 40);
  
  // BOWSER PROPERTIES ---------------------------------------------
  
  // Bowser animation variables
  static BufferedImage[] bowserImage = new BufferedImage[8];
  static int[] bowserNextRightPic  = {1,2,1,2,1,2,1,2};
  static int[] bowserNextLeftPic = {5,6,5,6,5,6,5,6};
  static int bowserAnimationIndex = 0;
  static String bowserAnimation = "idle right";
  
  // jumping variables
  static final int GROUND = 800;
  static final int B_RUN_SPEED = 18;
  static final int B_JUMP_SPEED = -53;
  static final int GRAVITY = 4;
  
  // bowser hitbox
  static int bowserWidth = 100;
  static int bowserHeight = 134;
  static int bowserXVelocity = 0;
  static int bowserYVelocity = 0;
  static int bowserX = 750;
  static int bowserY = GROUND-bowserHeight;
  static Rectangle bowserHitBox = new Rectangle(bowserX, bowserY, bowserWidth, bowserHeight);
  
  // bowser healthbar
  static final int bowserMaxHealth = 4;
  static int bowserCurrentHealth = 4;
  static final int bowserHealthBarWidth = 300;
  static final int bowserHealthBarHeight = 40;
  
  // Weapon properties
  static BufferedImage[] tommyGun = new BufferedImage[4];
  static String tommyWeaponPosition = "weapon right";
  static int tommyWeaponIndex;
  
  static BufferedImage[] toadGun = new BufferedImage[2];
  static String toadWeaponPosition;
  static int toadWeaponIndex;
  
  static final int DIRECTION_RIGHT = 1;
  static final int DIRECTION_LEFT = -1;
  static int bowserDirection = DIRECTION_RIGHT;
  static final int UP_NO = 0;
  static final int UP_RIGHT = 1;
  static final int UP_LEFT = 2;
  static int shootUp = UP_NO;
  
  // bullet properties
  static int totalBullets = 50;
  static int[] bulletXR = new int[totalBullets];
  static int[] bulletYR = new int[totalBullets];
  static int[] bulletXL = new int[totalBullets];
  static int[] bulletYL = new int[totalBullets];
  static int[] bulletXUR = new int[totalBullets];
  static int[] bulletYUR = new int[totalBullets];
  static int[] bulletXUL = new int[totalBullets];
  static int[] bulletYUL = new int[totalBullets];
  static boolean[] bulletVisibleR = new boolean[totalBullets];
  static boolean[] bulletVisibleL = new boolean[totalBullets];
  static boolean[] bulletVisibleUR = new boolean[totalBullets];
  static boolean[] bulletVisibleUL = new boolean[totalBullets];
  static int bulletWidth = 15;
  static int bulletHeight = 15;
  static int bulletSpeedR = 20;
  static int bulletSpeedL = -20;
  static int bulletSpeedU = -20;
  static int currentBullet = 0;
  static long shootTime = System.currentTimeMillis();
  
  // BACKGROUND PROPERTIES
  static int background1X = 0;
  static int background1Y = 0;
  static int background1W = 1500;
  static int background2X = background1W*travelDirection(level);
  static int background2Y = 0;
  static int background2W = 1500; 
  static int backgroundStep;
  static final int NO_STEP = 0;
  static final int STEP_LEFT = 4;
  static final int STEP_RIGHT = -4;
  static BufferedImage grass = null;
  static boolean onPlatform;
  
  // Platforming properties
  // level one Jungle platforms
  static BufferedImage junglePlatform = null;
  static int junglePlatformX1 = 200;
  static int junglePlatformY1 = GROUND - 240;
  static int junglePlatformX2 = 800;
  static int junglePlatformY2 = GROUND - 450;
  static int junglePlatformW = 200;
  static int junglePlatformH = 60;
  
  // level three platforms
  static BufferedImage pipePlatform = null;
  static BufferedImage blockFloor = null;
  static int pipePlatformW = 151;
  static int pipePlatformH = 230;
  static int pipePlatformX1 = 200;
  static int pipePlatformY1 = GROUND - pipePlatformH;
  static int pipePlatformX2 = 700;
  static int pipePlatformY2 = GROUND - pipePlatformH;
  static int pipePlatformX3 = 1200;
  static int pipePlatformY3 = GROUND - pipePlatformH;
  
  // level five platforms
  static BufferedImage mushroomPlatform0 = null;
  static BufferedImage mushroomPlatform1 = null;
  // Short mushroom platform
  static int mushroomPlatformW0 = 313;
  static int mushroomPlatformH0 = 50;
  static int mushroomPlatformX01 = 100;
  static int mushroomPlatformY01 = GROUND - mushroomPlatformH0-190;
  static int mushroomPlatformX02 = 1100;
  static int mushroomPlatformY02 = GROUND - mushroomPlatformH0-190;
  // Tall mushroom platform
  static int mushroomPlatformW1 = 300;
  static int mushroomPlatformH1 = 50;
  static int mushroomPlatformX11 = 600;
  static int mushroomPlatformY11 = GROUND - mushroomPlatformH1-350;
  
  // ENEMIES -----------------------------------------------------
  
  // Yoshi enemy
  static BufferedImage[] yoshiImage = new BufferedImage[4];
  static int[] yoshiNextRightPic = {0,1,0,1};
  static int[] yoshiNextLeftPic = {2,3,2,3};
  static int yoshiAnimationIndex = 0;
  static String yoshiAnimation;
  static int yoshiDirection;
  
  static int yoshiWidth = 98;
  static int yoshiHeight = 200;
  static int yoshiX;
  static int yoshiY = GROUND-yoshiHeight;
  static final int YOSHI_RUN_SPEEDR = 9;
  static final int YOSHI_RUN_SPEEDL = -9;
  
  // Multiple yoshi will have similar properties as bullets
  static int totalYoshi = 20;
  static int[] yoshiXR = new int[totalYoshi];
  static int[] yoshiYR = new int[totalYoshi];
  static int[] yoshiXL = new int[totalYoshi];
  static int[] yoshiYL = new int[totalYoshi];
  static boolean[] yoshiVisibleR = new boolean[totalYoshi];
  static boolean[] yoshiVisibleL = new boolean[totalYoshi];
  static int currentYoshi = 0;
  
  // Toad enemy
  static BufferedImage[] toadImage = new BufferedImage[2];
  static int[] toadAnimationPic = {0,1};
  static int toadAnimationIndex = 0;
  static String toadAnimation;
  static int toadDirection;
  
  static int toadWidth = 62;
  static int toadHeight = 80;
  static int toadX;
  static int toadY = GROUND-toadHeight;
  // Toad is a stationary shooter; moves with background
  
  static int totalToad = 20;
  static int[] toadXR = new int[totalToad];
  static int[] toadYR = new int[totalToad];
  static int[] toadXL = new int[totalToad];
  static int[] toadYL = new int[totalToad];
  static boolean[] toadVisibleR = new boolean[totalToad];
  static boolean[] toadVisibleL = new boolean[totalToad];
  static int currentToad = 0;
  
  // Toad's gun properties
  static int toadTotalBullets = 60;
  static int[] toadBulletXR = new int[toadTotalBullets];
  static int[] toadBulletYR = new int[toadTotalBullets];
  static int[] toadBulletXL = new int[toadTotalBullets];
  static int[] toadBulletYL = new int[toadTotalBullets];
  static boolean[] toadBulletVisibleR = new boolean[toadTotalBullets];
  static boolean[] toadBulletVisibleL = new boolean[toadTotalBullets];
  static int toadBulletWidth = 10;
  static int toadBulletHeight = 10;
  static int toadBulletSpeedR = 1;
  static int toadBulletSpeedL = -1;
  static int toadCurrentBullet = 0;
  static long toadShootTime = System.currentTimeMillis();
  
  // Spawning enemies
  // Generate random number of microseconds
  // level one spawns an enemy after 1000 ms to 2000 ms
  static int randomEasy = (int) (10+Math.random()*(20-10+1));
  // level two spawns an enemy after 800 ms to 1500 ms
  static int randomMedium = (int) (8+Math.random()*(15-8+1));
  // level three spawns an enemy after 600 ms to 1000 ms
  static int randomHard = (int) (6+Math.random()*(15-6+1));
  static final int msInterval = 100;
  static int randomMode;
  static int spawnRate = msInterval*randomMode;
  static long lastSpawnTime;
  static int randomEnemy = (int) (1+Math.random()*(2-1+1));
  
  // Spawning flowers
  // Spawns a flower in intervals of 25 seconds
  static int flowerSpawnRate = msInterval*250;
  static long lastFlowerSpawnTime = System.currentTimeMillis();
  static int flowerCount = 0;
  
  static BufferedImage flowerImage = null;
  static int flowerWidth = 60;
  static int flowerHeight = 120;
  static int totalFlower = 20;
  static int[] flowerX = new int[totalFlower];
  static int[] flowerY = new int[totalFlower];
  static boolean[] flowerVisible = new boolean[totalFlower];
  static int currentFlower = 0;
  
  // DONKEY KONG BOSS
  static BufferedImage kongImage = null;
  static BufferedImage kongPlaneR = null;
  static BufferedImage kongPlaneL = null;
  static BufferedImage kongBarrel = null;
  
  // Kong variables
  static int kongWidth = 375;
  static int kongHeight = 300;
  static int kongX = 0;
  static int kongY = 50;
  static final int kongMaxHealth = 40;
  static int kongCurrentHealth = kongMaxHealth;
  static int kongDirection;
  static final int KONG_RUN_SPEED = 10;
  
  // Plane variables
  static int planeWidth = 200;
  static int planeHeight = 160;
  static int planeX = 0;
  static int planeY = 300;
  
  // Barrel variables
  static int barrelWidth = 62;
  static int barrelHeight = 80;
  static int totalBarrel = 20;
  static final int BARREL_SPEED = 12;
  static int[] barrelX = new int[totalBarrel];
  static int[] barrelY = new int[totalBarrel];
  static boolean[] barrelVisible = new boolean[totalBarrel];
  static int currentBarrel = 0;
  static long barrelShootTime = 1800;
  static long lastBarrelShootTime = System.currentTimeMillis();
  static long barrelImmuneTime = System.currentTimeMillis();
  
  // Flag hitbox collision
  // if bowser collides with object and is dangerous, lose health. If collides with item, gets benefits
  static final int DANGER = 0;
  static final int ITEM = 1;
  static final int NO_COLLISION = 2;
  static final int FLOWER = 3;
  static int collisionType = NO_COLLISION;
  static long immuneTime = System.currentTimeMillis();
  
  // Music variables
  static AudioInputStream audioStream0;
  static Clip music0;
  static AudioInputStream audioStream1;
  static Clip music1;
  static AudioInputStream audioStream2;
  static Clip music2;
  static AudioInputStream audioStream3;
  static Clip music3;
  static AudioInputStream audioStream4;
  static Clip music4;
  static int musicMode = -1;
  
  
//------------------------------------------------------------------------------
  public static void main(String[] args){
    gameWindow = new JFrame("Bowser's Big Bean Burrito");
    gameWindow.setSize(WIDTH,HEIGHT);
    gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Load all the images only once (This prevented all the lag problems I was having
    for(int i = 0; i <= 7; i++) {
      try {
        bowserImage[i] = ImageIO.read(new File("bowser" + Integer.toString(i) + ".png"));
      } catch (IOException ex) {}   
    }
    for(int i = 0; i < 4; i++) {
      try {
        tommyGun[i] = ImageIO.read(new File("tommyGun" + Integer.toString(i) + ".png"));
      } catch (IOException ex) {}
    }
    for(int i = 0; i < 3; i++) {
      try {
        cutScene0[i] = ImageIO.read(new File("cutScene0" + Integer.toString(i) + ".png"));
      } catch (IOException ex) {}
    }
    for(int i = 0; i < 2; i++) {
      try {
        cutScene1[i] = ImageIO.read(new File("cutScene1" + Integer.toString(i) + ".png"));
      } catch (IOException ex) {}
    }
    for(int i = 0; i < 3; i++) {
      try {
        cutScene2[i] = ImageIO.read(new File("cutScene2" + Integer.toString(i) + ".png"));
      } catch (IOException ex) {}
    }
    for(int i = 0; i < 1; i++) {
      try {
        cutScene3[i] = ImageIO.read(new File("cutScene3" + Integer.toString(i) + ".png"));
      } catch (IOException ex) {}
    }
    for(int i = 0; i < 2; i++) {
      try {
        cutScene4[i] = ImageIO.read(new File("cutScene4" + Integer.toString(i) + ".png"));
      } catch (IOException ex) {}
    }
    try {
      titleScreen = ImageIO.read(new File("titleScreenOriginal.png"));
    } catch (IOException ex) {}
    try {
      menuImage = ImageIO.read(new File("menuBackground.png"));
    } catch (IOException ex) {}
    try {
      junglePlatform = ImageIO.read(new File("logPlatform.png"));
    } catch (IOException ex) {}
    try {
      grass = ImageIO.read(new File("tipOfGrass.png"));
    } catch (IOException ex) {}
    try {
      kongImage = ImageIO.read(new File("DKHaveBarrel.png"));
    } catch (IOException ex) {}
    try {
      kongBarrel = ImageIO.read(new File("barrel.png"));
    } catch (IOException ex) {}
    try {
      kongPlaneR = ImageIO.read(new File("DKPlaneR.png"));
    } catch (IOException ex) {}
    try {
      kongPlaneL = ImageIO.read(new File("DKPlaneL.png"));
    } catch (IOException ex) {}
    try {
      pipePlatform = ImageIO.read(new File("pipePlatform.png"));
    } catch (IOException ex) {}
    try {
      blockFloor = ImageIO.read(new File("blockFloor.png"));
    } catch (IOException ex) {}
    try {
      mushroomPlatform0 = ImageIO.read(new File("mushroomPlatform0.png"));
    } catch (IOException ex) {}
    try {
      mushroomPlatform1 = ImageIO.read(new File("mushroomPlatform1.png"));
    } catch (IOException ex) {}
    try {
      blockFloor = ImageIO.read(new File("blockFloor.png"));
    } catch (IOException ex) {}
    for(int i = 0; i <= 1; i++) {
      try {
        toadImage[i] = ImageIO.read(new File("toad" + Integer.toString(i) + ".png"));
      } catch (IOException ex) {}
      try {
        toadGun[i] = ImageIO.read(new File("toadGun" + Integer.toString(i) + ".png"));
      } catch (IOException ex) {}
    }
    for(int i = 0; i <= 3; i++) {
      try {
        yoshiImage[i] = ImageIO.read(new File("yoshi" + Integer.toString(i) + ".png"));
      } catch (IOException ex) {}
    }
    try {
      gameOverScreen = ImageIO.read(new File("deathScreen.png"));
    } catch (IOException ex) {}
    try {
      flowerImage = ImageIO.read(new File("flower.png"));
    } catch (IOException ex) {}
    
    canvas = new GraphicsPanel();
    canvas.addMouseListener(mouseListener);
    canvas.addMouseMotionListener(mouseMotionListener);
    canvas.addKeyListener(keyListener);
    gameWindow.add(canvas); 
    gameWindow.setVisible(true);
    runGameLoop();
    
  } // main method end
  
//------------------------------------------------------------------------------
  public static void runGameLoop(){
    while (true) {
      gameWindow.repaint();
      // Decrease FPS when sequencing through a cut scene, else increase FPS
      if(level == MAIN_MENU || level == CUT_SCENE00 || level == CUT_SCENE01 || level == CUT_SCENE02
           || level == CUT_SCENE10 || level == CUT_SCENE11 || level == CUT_SCENE20 || level == CUT_SCENE21
           || level == CUT_SCENE22 || level == CUT_SCENE30 || level == CUT_SCENE40 || level == CUT_SCENE41
           || level == GAME_OVER || level == TITLE_SCREEN)
        try  {Thread.sleep(200);} catch(Exception e){}
      else
        try  {Thread.sleep(40);} catch(Exception e){}
      //------------------------------------------------------------------
      // implement the game functionality here
      //------------------------------------------------------------------
      
      // BACKGROUND SCROLLING ---------------------------------------------------------------------------
      
      // Change scrolling speed depending on level of game and cut scene
      changeScrollingSpeed();
      
      // Determine background scrolling
      moveBackground();
      
      if(level == TITLE_SCREEN) {
        // Music for title page
        if(musicMode != 0) {
          try {
            File audioFile0 = new File("Music0.wav");
            audioStream0 = AudioSystem.getAudioInputStream(audioFile0);
            music0 = AudioSystem.getClip();
            music0.open(audioStream0);
            FloatControl gainControl0 = (FloatControl) music0.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl0.setValue(-20.f);
          } catch (Exception ex){}
          music0.start();
          music0.loop(Clip.LOOP_CONTINUOUSLY);
          musicMode = 0;
        }
      }
      if(level == MAIN_MENU) {
        // Music for main menu
        if(musicMode == 1) {
          music1.close();
        } else if(musicMode == 2) {
          music2.close();
        } else if(musicMode == 3) {
          music3.close();
        } else if(musicMode == 4) {
          music4.close();
        }
        if(musicMode != 0) {
          try {
            File audioFile0 = new File("Music0.wav");
            audioStream0 = AudioSystem.getAudioInputStream(audioFile0);
            music0 = AudioSystem.getClip();
            music0.open(audioStream0);
            FloatControl gainControl0 = (FloatControl) music0.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl0.setValue(-20.f);
          } catch (Exception ex){}
          music0.start();
          music0.loop(Clip.LOOP_CONTINUOUSLY);
          musicMode = 0;
        }
        // Reset variables for next level
        resetNextLevel();
      }else if(level == LEVEL_ONE || level == LEVEL_TWO || level == LEVEL_THREE || level == LEVEL_FOUR || level == LEVEL_FIVE) {
        
        // BOWSER MOVEMENT AND ANIMATION -----------------------------------------------------------
        // Determine bowser image direction
        bowserImageDirection();
        // Bowser jumping
        // Must be after bowser animation code so bowser walks on platforms
        bowserMovement();
        
        // SHOOTING APPEARANCE ----------------------------------------------------------------------
        // Choose which index in tommyGun's image array to use based on its position
        shootingTommyGun();
        
        // ENEMY MOVEMENT ------------------------------------------------------------------------------------
        // Yoshi movement and animation
        yoshiMovement();
        // Toad "movement" and animation
        toadMovement();
        
        // TOMMY GUN BULLET COLLISION ---------------------------------------------------------------
        // Literally sending enemies down to hell when it gets shot (also with bullet)
        // One bullet cannot pierce through multiple enemies
        enemyBulletCollision();
        
        // SPAWNING ENEMIES -----------------------------------------------------------------------------------
        spawnEnemies();
        
        
        // Enemy collision
        enemyCollision();
        
        // TOAD SHOOTING ------------------------------
        toadShooting();
        
        // SPAWNING FLOWERS -------------------------------------------------------------------------------
        spawningFlowers();
        
        // LEVEL ONE ------------------------------------------------------------------------------------------
        if(level == LEVEL_ONE) {
          // Music for level one
          if(musicMode == 0) {
            music0.close();
          } else if(musicMode == 2) {
            music2.close();
          } else if(musicMode == 3) {
            music3.close();
          } else if(musicMode == 4) {
            music4.close();
          }
          if(musicMode != 1) {
            try {
              File audioFile1 = new File("Music1.wav");
              audioStream1 = AudioSystem.getAudioInputStream(audioFile1);
              music1 = AudioSystem.getClip();
              music1.open(audioStream1);
              FloatControl gainControl1 = (FloatControl) music1.getControl(FloatControl.Type.MASTER_GAIN);
              gainControl1.setValue(-20.f);
            } catch (Exception ex){}
            music1.start();
            music1.loop(Clip.LOOP_CONTINUOUSLY);
            musicMode = 1;
          }
          // PLATFORMING LOGIC LEVEL ONE --------------------------------------------------------
          lvlOnePlatformingLogic();
        } else if(level == LEVEL_TWO) {
          // Music for level two
          if(musicMode == 0) {
            music0.close();
          } else if(musicMode == 1) {
            music1.close();
          } else if(musicMode == 3) {
            music3.close();
          } else if(musicMode == 4) {
            music4.close();
          }
          if(musicMode != 2) {
            try {
              File audioFile2 = new File("Music2.wav");
              audioStream2 = AudioSystem.getAudioInputStream(audioFile2);
              music2 = AudioSystem.getClip();
              music2.open(audioStream2);
              FloatControl gainControl2 = (FloatControl) music2.getControl(FloatControl.Type.MASTER_GAIN);
              gainControl2.setValue(-20.f);
            } catch (Exception ex){}
            music2.start();
            music2.loop(Clip.LOOP_CONTINUOUSLY);
            musicMode = 2;
          }
          // KONG MOVEMENT --------------------------------------------------------------------------
          kongMovement();
          kongBulletCollision();
          kongBarrels();
        } else if(level == LEVEL_THREE) {
          // Music for level three
          if(musicMode == 0) {
            music0.close();
          } else if(musicMode == 2) {
            music2.close();
          } else if(musicMode == 3) {
            music3.close();
          } else if(musicMode == 4) {
            music4.close();
          }
          if(musicMode != 1) {
            try {
              File audioFile1 = new File("Music1.wav");
              audioStream1 = AudioSystem.getAudioInputStream(audioFile1);
              music1 = AudioSystem.getClip();
              music1.open(audioStream1);
              FloatControl gainControl1 = (FloatControl) music1.getControl(FloatControl.Type.MASTER_GAIN);
              gainControl1.setValue(-20.f);
            } catch (Exception ex){}
            music1.start();
            music1.loop(Clip.LOOP_CONTINUOUSLY);
            musicMode = 1;
          }
          // PLATFORMING LOGIC LEVEL THREE ------------------------------------------------------
          lvlThreePlatformingLogic();
        } else if(level == LEVEL_FOUR) {
          // Will finish in the future (mario boss fight)
//          try {
//            File audioFile3 = new File("Music3.wav");
//            audioStream3 = AudioSystem.getAudioInputStream(audioFile3);
//            music3 = AudioSystem.getClip();
//            music3.open(audioStream3);
//            FloatControl gainControl3 = (FloatControl) music3.getControl(FloatControl.Type.MASTER_GAIN);
//            gainControl3.setValue(-20.f);
//          } catch (Exception ex){}
        } else if(level == LEVEL_FIVE) {
          // Music for level five
          if(musicMode == 0) {
            music0.close();
          } else if(musicMode == 2) {
            music2.close();
          } else if(musicMode == 3) {
            music3.close();
          } else if(musicMode == 4) {
            music4.close();
          }
          if(musicMode != 1) {
            try {
              File audioFile1 = new File("Music1.wav");
              audioStream1 = AudioSystem.getAudioInputStream(audioFile1);
              music1 = AudioSystem.getClip();
              music1.open(audioStream1);
              FloatControl gainControl1 = (FloatControl) music1.getControl(FloatControl.Type.MASTER_GAIN);
              gainControl1.setValue(-20.f);
            } catch (Exception ex){}
            music1.start();
            music1.loop(Clip.LOOP_CONTINUOUSLY);
            musicMode = 1;
          }
          lvlFivePlatformingLogic();
        } // end of level 5 (DO NOT DELETE THIS BRACKET!)
        
      } else if(level == GAME_OVER) {
        // Music for level one
        if(musicMode == 0) {
          music0.close();
        } else if(musicMode == 1) {
          music1.close();
        } else if(musicMode == 2) {
          music2.close();
        } else if(musicMode == 3) {
          music3.close();
        }
        if(musicMode != 4) {
          try {
            File audioFile4 = new File("Music4.wav");
            audioStream4 = AudioSystem.getAudioInputStream(audioFile4);
            music4 = AudioSystem.getClip();
            music4.open(audioStream4);
            FloatControl gainControl4 = (FloatControl) music4.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl4.setValue(-10.f);
          } catch (Exception ex){}
          music4.start();
          music4.loop(Clip.LOOP_CONTINUOUSLY);
          musicMode = 4;
        }
      }
      
      
    } // end of while(true) loop
  } // runGameLoop method end
  
  // BACKGROUND -------------------------------------------------------------------------------
  // Determine scrolling background direction
  public static int travelDirection(int level) {
    // bowser travels from left to right on level 1 and 3
    int plusMinus;
    if(level == LEVEL_ONE || level == LEVEL_THREE)
      plusMinus = 1;
    // bowser travels from right to left on level 5
    else if(level == LEVEL_FIVE)
      plusMinus = -1;
    else
      plusMinus = 1;
    return plusMinus;
  }
  
  public static void changeScrollingSpeed() {
    if(level == LEVEL_ONE) {
      backgroundStep = STEP_LEFT;
      randomMode = randomEasy;
    } else if(level == LEVEL_THREE){
      backgroundStep = STEP_LEFT;
      randomMode = randomMedium;
    } else if(level == LEVEL_FIVE) {
      backgroundStep = STEP_RIGHT;
      randomMode = randomHard;
    } else {
      backgroundStep = NO_STEP;
    }
  }
  
  public static void moveBackground() {
    // Move background images and bowser
    if(level == LEVEL_ONE || level == LEVEL_THREE) {
      // level one and three scrolls the background from right to left (bowser journeys out)
      background1X -= backgroundStep;
      if (background1X - backgroundStep < -background1W)
        background1X = background2W;
      background2X -= backgroundStep;
      if (background2X - backgroundStep < -background1W)
        background2X = background1W;
      // If bowser stays skill he moves with background
      if((bowserX-backgroundStep) >= 0 && bowserXVelocity == 0) {
        bowserX -= backgroundStep;
      }
      // Direction enemies move to
      yoshiDirection = DIRECTION_LEFT;
      toadDirection = DIRECTION_LEFT;
    } else if(level == LEVEL_FIVE) {
      // level five scrolls the background from left to right (bowser returns back)
      background1X -= backgroundStep;
      if (background1X - backgroundStep > background1W)
        background1X = -background2W;
      background2X -= backgroundStep;
      if (background2X - backgroundStep > background2W)
        background2X = -background1W;
      // If bowser stays skill he moves with background
      if((bowserX+backgroundStep) <= 1500 && bowserXVelocity == 0) {
        bowserX -= backgroundStep;
      }
      // Direction enemies move to
      yoshiDirection = DIRECTION_RIGHT;
      toadDirection = DIRECTION_RIGHT;
    }
  }
  
  public static void enemyBulletCollision() {
    for(int i = 0; i < totalBullets; i++) {
      // Bullet collision on yoshi
      for(int j = 0; j < totalYoshi; j++) {
        if(bulletXR[i]+bulletWidth >= yoshiXL[j] && bulletXR[i] <= yoshiXL[j]+yoshiWidth
             && bulletYR[i]+bulletHeight >= yoshiYL[j] && bulletYR[i]+bulletHeight <= yoshiYL[j]+yoshiHeight
             && yoshiXL[j] > 0 && yoshiXL[j] <1500) {
          // yoshiL -- bulletR
          yoshiYL[j] = 1200;
          bulletYR[i] = 1450;
          // Both yoshi and bullet vanish
          yoshiVisibleL[j] = false;
          bulletVisibleR[i] = false;
        } else if(bulletXL[i]+bulletWidth >= yoshiXL[j] && bulletXL[i] <= yoshiXL[j]+yoshiWidth
                    && bulletYL[i]+bulletHeight >= yoshiYL[j] && bulletYL[i]+bulletHeight <= yoshiYL[j]+yoshiHeight
                    && yoshiXL[j] > 0 && yoshiXL[j] <1500) {
          // yoshiL -- bulletL
          yoshiYL[j] = 1200;
          bulletYL[i] = 1450;
          // Both yoshi and bullet vanish
          yoshiVisibleL[j] = false;
          bulletVisibleL[i] = false;
        } else if(bulletXR[i]+bulletWidth >= yoshiXR[j] && bulletXR[i] <= yoshiXR[j]+yoshiWidth
                    && bulletYR[i] >= yoshiYR[j] && bulletYR[i]+bulletHeight <= yoshiYR[j]+yoshiHeight
                    && yoshiXR[j] > 0 && yoshiXR[j] <1500) {
          // yoshiR -- bulletR
          yoshiYR[j] = 1200;
          bulletYR[i] = 1450;
          // Both yoshi and bullet vanish
          yoshiVisibleR[j] = false;
          bulletVisibleR[i] = false;
        } else if(bulletXL[i]+bulletWidth >= yoshiXR[j] && bulletXL[i] <= yoshiXR[j]+yoshiWidth
                    && bulletYL[i] >= yoshiYR[j] && bulletYL[i]+bulletHeight <= yoshiYR[j]+yoshiHeight
                    && yoshiXR[j] > 0 && yoshiXR[j] <1500) {
          // yoshiR -- bulletL
          yoshiYR[j] = 1200;
          bulletYL[i] = 1450;
          // Both yoshi and bullet vanish
          yoshiVisibleR[j] = false;
          bulletVisibleL[i] = false;
        }
      } // end of yoshi getting shot for loop
      // Bullet collision on toad
      for(int j = 0; j < totalToad; j++) {
        if(bulletXR[i]+bulletWidth >= toadXL[j] && bulletXR[i] <= toadXL[j]+toadWidth
             && bulletYR[i]+bulletHeight >= toadYL[j] && bulletYR[i]+bulletHeight <= toadYL[j]+toadHeight
             && toadXL[j] > 0 && toadXL[j] <1500) {
          // toadL -- bulletR
          toadYL[j] = 1450;
          bulletYR[i] = 1200;
          // Both toad and bullet vanish
          toadVisibleL[j] = false;
          bulletVisibleR[i] = false;
        } else if(bulletXL[i] >= toadXL[j] && bulletXL[i] <= toadXL[j]+toadWidth
                    && bulletYL[i]+bulletHeight >= toadYL[j] && bulletYL[i]+bulletHeight <= toadYL[j]+toadHeight
                    && toadXL[j] > 0 && toadXL[j] <1500) {
          // toadL -- bulletL
          toadYL[j] = 1450;
          bulletYL[i] = 1200;
          // Both toad and bullet vanish
          toadVisibleL[j] = false;
          bulletVisibleL[i] = false;
        } else if(bulletXR[i]+bulletWidth >= toadXR[j] && bulletXR[i] <= toadXR[j]+toadWidth
                    && bulletYR[i]+bulletHeight >= toadYR[j] && bulletYR[i]+bulletHeight <= toadYR[j]+toadHeight
                    && toadXR[j] > 0 && toadXR[j] <1500) {
          // toadR -- bulletR
          toadYR[j] = 1450;
          bulletYR[i] = 1200;
          // Both toad and bullet vanish
          toadVisibleR[j] = false;
          bulletVisibleR[i] = false;
        } else if(bulletXL[i]+bulletWidth >= toadXR[j] && bulletXL[i] <= toadXR[j]+toadWidth
                    && bulletYL[i]+bulletHeight >= toadYR[j] && bulletYL[i]+bulletHeight <= toadYR[j]+toadHeight
                    && toadXR[j] > 0 && toadXR[j] <1500) {
          // toadR -- bulletL
          toadYR[j] = 1450;
          bulletYL[i] = 1200;
          // Both toad and bullet vanish
          toadVisibleR[j] = false;
          bulletVisibleL[i] = false;
        }
      } // end of toad getting shot for loop
    } // end of bullet collision check for loop
  }
  
  public static void spawnEnemies() {
    if(System.currentTimeMillis() - spawnRate > lastSpawnTime) {
      lastSpawnTime = System.currentTimeMillis();
      // Generate random time intervals between spawn times varying by level
      if(level == LEVEL_ONE) {
        randomEasy = (int) (10+Math.random()*(20-10+1));
      } else if(level == LEVEL_THREE) {
        randomMedium = (int) (8+Math.random()*(15-8+1));
      } else if(level == LEVEL_FIVE) {
        randomHard = (int) (6+Math.random()*(15-6+1));
      }
      // Calculate spawn rate
      spawnRate = msInterval*randomMode;
      // 50% chance to spawn yoshi or toad
      randomEnemy = (int) (1+Math.random()*(2-1+1));
      if(randomEnemy == 1) {
        // Spawn yoshi
        if(level == LEVEL_ONE || level == LEVEL_THREE) {
          yoshiXL[currentYoshi] = 1500;
          yoshiYL[currentYoshi] = GROUND-yoshiHeight;
          yoshiVisibleL[currentYoshi] = true;
        } else if(level == LEVEL_FIVE) {
          yoshiXR[currentYoshi] = 0;
          yoshiYR[currentYoshi] = GROUND-yoshiHeight;
          yoshiVisibleR[currentYoshi] = true;
        }
        currentYoshi = (currentYoshi + 1)%totalYoshi;
      } else if(randomEnemy == 2) {
        // Spawn toad
        if(level == LEVEL_ONE || level == LEVEL_THREE) {
          toadXL[currentToad] = 1450;
          toadYL[currentToad] = GROUND-toadHeight;
          toadVisibleL[currentToad] = true;
        } else if(level == LEVEL_FIVE) {
          toadXR[currentToad] = 0;
          toadYR[currentToad] = GROUND-toadHeight;
          toadVisibleR[currentToad] = true;
        }
        currentToad = (currentToad + 1)%totalToad;
      }
    }
  }
  
  public static void spawningFlowers() {
    if(level == LEVEL_ONE || level == LEVEL_THREE || level == LEVEL_FIVE) {
      if(System.currentTimeMillis() - flowerSpawnRate > lastFlowerSpawnTime) {
        lastFlowerSpawnTime = System.currentTimeMillis();
        // Spawn flower
        if(level == LEVEL_ONE || level == LEVEL_THREE) {
          flowerX[currentFlower] = 1500;
        } else if(level == LEVEL_FIVE) {
          flowerX[currentFlower] = 1;
        }
        flowerY[currentFlower] = GROUND-flowerHeight;
        flowerVisible[currentFlower] = true;
        currentFlower = (currentFlower + 1)%totalFlower;
      }
      
      for(int i = 0; i < totalFlower; i++) {
        // Move flower from one side of screen to other
        if(flowerVisible[i] == true) {
          flowerX[i] -= backgroundStep;
          flowerY[i] = GROUND-flowerHeight;
          if(flowerX[i] >= WIDTH || flowerX[i] <= 0) {
            flowerVisible[i] = false;
          }
        }
        // Flower collision
        if(flowerX[i]+flowerWidth >= bowserX && flowerX[i] <= bowserX+bowserWidth
             && flowerY[i]+flowerHeight >= bowserY && flowerY[i]+flowerHeight <= bowserY+bowserHeight
             && flowerX[i] > 0 && flowerX[i] < 1500) {
          collisionType = FLOWER;
        } else {
          collisionType = NO_COLLISION;
        }
        // Check collision type
        if(collisionType == FLOWER) {
          flowerCount +=1;
          // take flower out of screen so bowser cannot collect multiple flowers from one
          flowerY[i] = 1200;
          flowerVisible[i] = false;
        }
        // Exits out of level when bowser collects five or more flowers
        if(flowerCount >= 5) {
          if(level == LEVEL_ONE) {
            level = CUT_SCENE10;
          } else if(level == LEVEL_THREE) {
            // will add to later
            level = CUT_SCENE30;
          } else if(level == LEVEL_FIVE) {
            // will add to later
            level = CUT_SCENE40;
          }
        }
      }
    }
  }
  
  public static void enemyCollision() {
    for(int i = 0; i < totalYoshi; i++) {
      // Bowser is granted immunity for 400 ms after collision with an enemy
      // if bowser is inside yoshi, bowser takes damage
      if(bowserX+bowserWidth >= yoshiXL[i]&& bowserX <= yoshiXL[i]+yoshiWidth
           && bowserY+bowserHeight >= yoshiYL[i] && bowserY+bowserHeight <= yoshiYL[i]+yoshiHeight
           && System.currentTimeMillis() - immuneTime > 400 && yoshiXL[i] > 0 && yoshiXL[i] < 1500) {
        collisionType = DANGER;
        immuneTime = System.currentTimeMillis();
      } else if(bowserX+bowserWidth >= yoshiXR[i]&& bowserX <= yoshiXR[i]+yoshiWidth
                  && bowserY+bowserHeight >= yoshiYR[i] && bowserY+bowserHeight <= yoshiYR[i]+yoshiHeight
                  && System.currentTimeMillis() - immuneTime > 400 && yoshiXR[i] > 0 && yoshiXR[i] < 1500) {
        collisionType = DANGER;
        immuneTime = System.currentTimeMillis();
      } else {
        collisionType = NO_COLLISION;
      }
      // Calculate bowser health
      if(collisionType == DANGER) {
        // Take away one health point from bowser
        bowserCurrentHealth -=1;
      }
      if(bowserCurrentHealth <= 0) {
        // When bowser runs out of health, game over
        level = GAME_OVER;
      }
    }
    for(int i = 0; i < totalToad; i++) {
      // Bowser is granted immunity for 400 ms after collision with an enemy
      // If toad is inside bowser, bowser takes damage
      if(toadXL[i]+toadWidth >= bowserX && toadXL[i] <= bowserX+bowserWidth
           && toadYL[i]+toadHeight >= bowserY && toadYL[i]+toadHeight <= bowserY+bowserHeight
           && System.currentTimeMillis() - immuneTime > 400 && toadXL[i] > 0 && toadXL[i] < 1500) {
        collisionType = DANGER;
        immuneTime = System.currentTimeMillis();
      } else if(toadXR[i]+toadWidth >= bowserX && toadXR[i] <= bowserX+bowserWidth
                  && toadYR[i]+toadHeight >= bowserY && toadYR[i]+toadHeight <= bowserY+bowserHeight
                  && System.currentTimeMillis() - immuneTime > 400 && toadXR[i] > 0 && toadXR[i] < 1500) {
        collisionType = DANGER;
        immuneTime = System.currentTimeMillis();
      } else {
        collisionType = NO_COLLISION;
      }
      // Calculate bowser health
      if(collisionType == DANGER) {
        bowserCurrentHealth -=1;
      }
      if(bowserCurrentHealth <= 0) {
        level = GAME_OVER;
      }
    }
  }
  
  public static void resetNextLevel() {
    // Reset all the variables for next level (recreating the array)
    // reset yoshi enemy    
    yoshiXR = new int[totalYoshi];
    yoshiYR = new int[totalYoshi];
    yoshiXL = new int[totalYoshi];
    yoshiYL = new int[totalYoshi];
    yoshiVisibleR = new boolean[totalYoshi];
    yoshiVisibleL = new boolean[totalYoshi];
    // Must have this statement to print all yoshi off screen so they aren't frozen in corner
    for(int i = 0; i < totalYoshi; i++) {
      yoshiXL[i] = -300;
      yoshiXR[i] = 1800;
    }
    // reset toad enemy
    toadXR = new int[totalToad];
    toadYR = new int[totalToad];
    toadXL = new int[totalToad];
    toadYL = new int[totalToad];
    toadVisibleR = new boolean[totalToad];
    toadVisibleL = new boolean[totalToad];
    // Reset toad bullets
    toadBulletXR = new int[toadTotalBullets];
    toadBulletYR = new int[toadTotalBullets];
    toadBulletXL = new int[toadTotalBullets];
    toadBulletYL = new int[toadTotalBullets];
    toadBulletVisibleR = new boolean[toadTotalBullets];
    toadBulletVisibleL = new boolean[toadTotalBullets];
    toadShootTime = System.currentTimeMillis();
    // Must have this statement to print all toad off screen so they aren't frozen in corner
    for(int i = 0; i < totalYoshi; i++) {
      toadXL[i] = -300;
      toadXR[i] = 1800;
    }
    for(int i = 0; i < toadTotalBullets; i++) {
      toadBulletXR[i] = 1800;
      toadBulletXL[i] = -300;
    }
    // Reset bullets
    bulletXR = new int[totalBullets];
    bulletYR = new int[totalBullets];
    bulletXL = new int[totalBullets];
    bulletYL = new int[totalBullets];
    bulletXUR = new int[totalBullets];
    bulletYUR = new int[totalBullets];
    bulletXUL = new int[totalBullets];
    bulletYUL = new int[totalBullets];
    bulletVisibleR = new boolean[totalBullets];
    bulletVisibleL = new boolean[totalBullets];
    bulletVisibleUR = new boolean[totalBullets];
    bulletVisibleUL = new boolean[totalBullets];
    // Must have this statement to print all bullets 300 units off screen so they aren't frozen in corner
    for(int i = 0; i < totalBullets; i++) {
      bulletXR[i] = 1800;
      bulletXL[i] = -300;
      bulletYR[i] = 1000;
      bulletYL[i] = 1000;
      bulletXUR[i] = 1800;
      bulletXUL[i] = 1800;
      bulletYUR[i] = -300;
      bulletYUL[i] = -300;
    }
    // Reset bowser variables
    bowserCurrentHealth = 4;
    bowserX = 750;
    bowserY = GROUND-bowserHeight;
    // Reset flower variables
    flowerCount = 0;
    flowerX = new int[totalFlower];
    
    // Must include this so flowers don't spawn on screen at beginning
    for(int i = 0; i < totalFlower; i++) {
      flowerX[i] = -300;
    }
    flowerY = new int[totalFlower];
    flowerVisible = new boolean[totalFlower];
    currentFlower = 0;
    lastFlowerSpawnTime = System.currentTimeMillis();
    // Reset background scrolling variables
    background1X = 0;
    background1Y = 0;
    background1W = 1500;
    background2X = background1W*travelDirection(level);
    background2Y = 0;
    background2W = 1500;
    
    // reset kong
    kongX = 0;
    kongY = 50;  
    kongCurrentHealth = kongMaxHealth;
    kongDirection = DIRECTION_RIGHT;
    // Plane variables
    planeX = 0;
    planeY = 300;
    // Barrel variables
    barrelX = new int[totalBarrel];
    barrelY = new int[totalBarrel];
    // must include so barrels do not appear until spawned
    for(int i = 0; i < totalBarrel; i++) {
      barrelX[i] = -300;
    }
    barrelVisible = new boolean[totalBarrel];
    currentBarrel = 0;
    barrelShootTime = 1800;
    lastBarrelShootTime = System.currentTimeMillis();
    barrelImmuneTime = System.currentTimeMillis();
    // cut scene variables
    lastSceneSwap = System.currentTimeMillis();
  }
  
// BOWSER MOVEMENT AND ANIMATION ---------------------------------------------------------------------
  public static void bowserImageDirection() {
    // Determine bowser's action through his x & y velocity and direction he is facing
    if(bowserDirection == DIRECTION_RIGHT) {
      // Actions facing right
      if(bowserXVelocity == 0 && bowserYVelocity == 0) {
        bowserAnimation = "idle right";
      } else if(bowserXVelocity != 0 && bowserYVelocity == 0) {
        bowserAnimation = "move right";
      } else if(bowserYVelocity < 0) {
        bowserAnimation = "jump right";
      } else {
        bowserAnimation = "idle right";
      }
    } else if(bowserDirection == DIRECTION_LEFT) {
      // Actions facing left
      if(bowserXVelocity == 0 && bowserYVelocity == 0) {
        bowserAnimation = "idle left";
      } else if(bowserXVelocity != 0 && bowserYVelocity == 0) {
        bowserAnimation = "move left";
      } else if(bowserYVelocity < 0) {
        bowserAnimation = "jump left";
      } else {
        bowserAnimation = "idle left";
      }
    }
    // Choose which index in bowser's image array to use based on his action
    if (bowserAnimation == "idle right"){
      bowserAnimationIndex = 0;
    } else if (bowserAnimation == "move right"){
      bowserAnimationIndex = bowserNextRightPic[bowserAnimationIndex];
    } else if (bowserAnimation == "jump right"){
      bowserAnimationIndex = 3;
    } else if (bowserAnimation == "idle left"){
      bowserAnimationIndex = 4;
    } else if (bowserAnimation == "move left"){
      bowserAnimationIndex = bowserNextLeftPic[bowserAnimationIndex];
    } else if (bowserAnimation == "jump left"){
      bowserAnimationIndex = 7;
    }
  }
  public static void bowserMovement() {
    bowserX += bowserXVelocity;
    // bowserYVelocity must never be greater than platform's height or else bowser will go through
    if(bowserYVelocity < 59) {
      bowserYVelocity += GRAVITY;
    }
    bowserY += bowserYVelocity;
    if(bowserY+bowserHeight >= GROUND) {
      bowserY = GROUND - bowserHeight;
      bowserYVelocity = 0;
    }
    /* Bowser cannot leave the screen
     * Java window is very weird as it does not display 1500 by 1000 by default but around 1340 by 1000. If you
     * expand the window, you will see 1500 by 1000 (Since it is a hassle, to expand each time, I restricted more
     * of the area */
    if(bowserX < 0) {
      bowserX = 0;
    } else if(bowserX > WIDTH-160) {
      bowserX = 1500-160;
    }
  }
  // SHOOTING ------------------------------------------------------------------------------------------
  public static void shootingTommyGun() {
    if(tommyWeaponPosition == "weapon up right"){
      tommyWeaponIndex = 2;
    } else if(tommyWeaponPosition == "weapon up left"){
      tommyWeaponIndex = 3;
    } else if(bowserDirection == DIRECTION_RIGHT) {
      tommyWeaponIndex = 0;
    } else if(bowserDirection == DIRECTION_LEFT) {
      tommyWeaponIndex = 1;
    }
    
    // Bullet movement
    for(int i = 0; i < totalBullets; i++) {
      // Must include so enemies don't die from out of screen bullets
      if(bulletXR[i] >= 1500) {
        bulletYR[i] = 1200;
      }
      if(bulletXL[i] <= 0) {
        bulletYL[i] = 1200;
      }
      
      // Bullets travelling right
      if(bulletVisibleR[i] == true) {
        bulletXR[i] = bulletXR[i] + bulletSpeedR;
        if(bulletXR[i] >= WIDTH || bulletXR[i] <= 0) {
          bulletVisibleR[i] = false;
        }
      }
      // Bullets travelling left
      if(bulletVisibleL[i] == true) {
        bulletXL[i] = bulletXL[i] + bulletSpeedL;
        if(bulletXL[i] >= WIDTH || bulletXL[i] <= 0) {
          bulletVisibleL[i] = false;
        }
      }
      // Bullets travelling up right
      if(bulletVisibleUR[i] == true) {
        bulletYUR[i] = bulletYUR[i] + bulletSpeedU;
        if(bulletYUR[i] <= 0) {
          bulletVisibleUR[i] = false;
        }
      }
      // Bullets travelling up left
      if(bulletVisibleUL[i] == true) {
        bulletYUL[i] = bulletYUL[i] + bulletSpeedU;
        if(bulletYUL[i] <= 0) {
          bulletVisibleUL[i] = false;
        }
      }
    }
  }
  public static void toadShooting() {
    // spawn a bullet from each toad
    
    for(int j = 0; j < totalToad; j++) {
      if(level == LEVEL_ONE || level == LEVEL_THREE) {
        // if toad is still alive, it shoots
        if(toadXL[j] > 0 && toadXL[j]+toadWidth < 1500 && System.currentTimeMillis() - toadShootTime > 2000) {
          toadShootTime = System.currentTimeMillis();
          toadBulletXL[toadCurrentBullet] = toadXL[j]-5;
          toadBulletYL[toadCurrentBullet] = toadYL[j]+25;
          toadBulletVisibleL[toadCurrentBullet] = true;
          toadCurrentBullet = (toadCurrentBullet + 1)%toadTotalBullets;
        }
        for(int i = 0; i < toadTotalBullets; i++) {
          // Bullets travelling left
          if(toadBulletVisibleL[i] == true) {
            toadBulletXL[i] += toadBulletSpeedL;
            if(toadBulletXL[i] >= WIDTH || toadBulletXL[i] <= 0) {
              toadBulletVisibleL[i] = false;
            }
          }
        }
      } else if(level == LEVEL_FIVE) {
        if(toadXR[j] > 0 && toadXR[j]+toadWidth < 1500 && System.currentTimeMillis() - toadShootTime > 2000) {
          toadShootTime = System.currentTimeMillis();
          toadBulletXR[toadCurrentBullet] = toadXR[j]+10;
          toadBulletYR[toadCurrentBullet] = toadYR[j]+25;
          toadBulletVisibleR[toadCurrentBullet] = true;
          toadCurrentBullet = (toadCurrentBullet + 1)%toadTotalBullets;
        }
      }
      for(int i = 0; i < toadTotalBullets; i++) {
        // Bullets travelling right
        if(toadBulletVisibleR[i] == true) {
          toadBulletXR[i] += toadBulletSpeedR;
          if(toadBulletXR[i] >= WIDTH || toadBulletXR[i] <= 0) {
            toadBulletVisibleR[i] = false;
          }
        }
      }
    }
  }
  // ENEMY MOVEMENT AND ANIMATION --------------------------------------------------------------
  public static void yoshiMovement() {
    // Yoshi movement
    for(int i = 0; i < totalYoshi; i++) {
      // yoshi travelling right
      // yoshi travelling left
      if(level == LEVEL_ONE || level == LEVEL_THREE) {
        if(yoshiVisibleL[i] == true) {
          yoshiXL[i] = yoshiXL[i] + YOSHI_RUN_SPEEDL;
          yoshiYL[i] = GROUND-yoshiHeight;
          if(yoshiXL[i] >= WIDTH || yoshiXL[i] <= 0) {
            yoshiVisibleL[i] = false;
          }
        }
      } else if(level == LEVEL_FIVE) {
        if(yoshiVisibleR[i] == true) {
          yoshiXR[i] = yoshiXR[i] + YOSHI_RUN_SPEEDR;
          yoshiYR[i] = GROUND-yoshiHeight;
          if(yoshiXR[i] >= WIDTH || yoshiXR[i] <= 0) {
            yoshiVisibleR[i] = false;
          }
        }
      }
    }
    // Yoshi animation position
    // Actions facing left
    if(yoshiDirection == DIRECTION_LEFT) {
      yoshiAnimation = "move left";
    }else if(yoshiDirection == DIRECTION_RIGHT) {
      yoshiAnimation = "move right";
    }
    if (yoshiAnimation == "move right"){
      yoshiAnimationIndex = yoshiNextRightPic[yoshiAnimationIndex];
    } else if (yoshiAnimation == "move left"){
      yoshiAnimationIndex = yoshiNextLeftPic[yoshiAnimationIndex];
    }
  }
  public static void toadMovement() {
    // Toad appearance
    for(int i = 0; i < totalToad; i++) {
      // Move toad from one side of screen to other
      if(level == LEVEL_FIVE) {
        // toad facing right
        if(toadVisibleR[i] == true) {
          toadXR[i] -= backgroundStep;
          toadYR[i] = GROUND-toadHeight;
          if(toadXR[i] >= WIDTH || toadXR[i] <= 0) {
            toadVisibleR[i] = false;
          }
        }
      } else if(level == LEVEL_ONE || level == LEVEL_THREE) {
        // toad facing left
        if(toadVisibleL[i] == true) {
          // Move toad from one side of screen to other
          toadXL[i] -= backgroundStep;
          toadYL[i] = GROUND-toadHeight;
          if(toadXL[i] >= WIDTH || toadXL[i] <= 0) {
            toadVisibleL[i] = false;
          }
        }
      }
    }
    // Toad's position based on direction
    if(toadDirection == DIRECTION_LEFT) {
      toadAnimation = "idle left";
    }else if(toadDirection == DIRECTION_RIGHT) {
      toadAnimation = "idle right";
    }
    if (toadAnimation == "idle right"){
      toadAnimationIndex = 0;
    } else if (toadAnimation == "idle left"){
      toadAnimationIndex = 1;
    }
    // Toad's weapon position based on direction
    if(toadDirection == DIRECTION_RIGHT) {
      toadWeaponIndex = 0;
    } else if(toadDirection == DIRECTION_LEFT) {
      toadWeaponIndex = 1;
    }
  }
  
  public static void kongMovement() {
    // kong moves from one side of screen to another
    if(kongX+kongWidth >= 1500) {
      kongDirection = DIRECTION_LEFT;
    } else if(kongX <= 0) {
      kongDirection = DIRECTION_RIGHT;
    }
    if(kongDirection == DIRECTION_RIGHT) {
      kongX += KONG_RUN_SPEED;
      planeX += KONG_RUN_SPEED;
    } else if(kongDirection == DIRECTION_LEFT) {
      kongX -= KONG_RUN_SPEED;
      planeX -= KONG_RUN_SPEED;
    }
  }
  public static void kongBulletCollision() {
    // KONG BULLET COLLISION --------------------------------------------------------------------------
    // kong can only get shot by bullets travelling up. Only check hitbox for bullet arrays XUR and XUL
    for(int i = 0; i < totalBullets; i++) {
      // Bullet collision on yoshi
      if(bulletXUR[i]+bulletWidth >= kongX && bulletXUR[i] <= kongX+kongWidth
           && bulletYUR[i]+bulletHeight >= kongY && bulletYUR[i]+bulletHeight <= kongY+kongHeight-30) {
        // bulletR moves out of screen so kong cannot get hit again
        kongCurrentHealth -=1;
        bulletYUR[i] = -20;
        // bullet vanishes
        bulletVisibleR[i] = false;
      }else if(bulletXUL[i]+bulletWidth >= kongX && bulletXUL[i] <= kongX+kongWidth
                 && bulletYUL[i]+bulletHeight >= kongY && bulletYUL[i]+bulletHeight <= kongY+kongHeight-30) {
        // bulletL moves out of screen so kong cannot get hit again
        kongCurrentHealth -=1;
        bulletYUL[i] = -20;
        // bullet vanish
        bulletVisibleL[i] = false;
      }
    } // end of kong getting shot for loop
    
    if(kongCurrentHealth <= 0) {
      level = CUT_SCENE20;
      // Add this in so swapping to first scene works
      lastSceneSwap = System.currentTimeMillis() - 10000;
    }
  }
  
  public static void kongBarrels() {
    // SPAWNING BARREL -------------------------------------------------------------------------------
    if(System.currentTimeMillis() - barrelShootTime > lastBarrelShootTime) {
      lastBarrelShootTime = System.currentTimeMillis();
      // Spawn barrel
      barrelX[currentBarrel] = 1500;
      barrelY[currentFlower] = GROUND-barrelHeight;
      barrelVisible[currentBarrel] = true;
      currentBarrel = (currentBarrel + 1)%totalBarrel;
    }
    // Move barrels
    for(int i = 0; i < totalBarrel; i++) {
      // Move barrel from one side of screen to other
      barrelX[i] -= BARREL_SPEED;
      barrelY[i] = GROUND-barrelHeight;
      if(barrelVisible[i] == true) {
        if(barrelX[i] >= WIDTH || barrelX[i] <= 0) {
          barrelVisible[i] = false;
        }
      }
      // Barrel collision
      if(bowserX+bowserWidth >= barrelX[i] && bowserX <= barrelX[i]+barrelWidth
           && bowserY+bowserHeight>= barrelY[i] && bowserY <= barrelY[i]+barrelHeight
           && System.currentTimeMillis() - barrelImmuneTime > 400) {
        // Must give bowser 13 sec of barrel so bowser doesn't die instantly
        barrelImmuneTime = System.currentTimeMillis();
        collisionType = DANGER;
      } else {
        collisionType = NO_COLLISION;
      }
      // Check collision type
      if(collisionType == DANGER) {
        bowserCurrentHealth -=1;
      }
    }
  }
  
  public static void lvlOnePlatformingLogic() {
    // when bowser's feet are within platform, bowser Y coordinate is at the top of the platform
    if(bowserX+bowserWidth > junglePlatformX1+background1X
         && bowserX < junglePlatformX1+background1X+junglePlatformW
         && bowserY+bowserHeight > junglePlatformY1
         && bowserY+bowserHeight < junglePlatformY1 + junglePlatformH && bowserYVelocity >= 0) {
      // lower platform
      bowserY = junglePlatformY1 - bowserHeight;
      bowserYVelocity = 0;
    }else if(bowserX+bowserWidth > junglePlatformX1+background2X
               && bowserX < junglePlatformX1+background2X+junglePlatformW
               && bowserY+bowserHeight > junglePlatformY1
               && bowserY+bowserHeight < junglePlatformY1 + junglePlatformH && bowserYVelocity >= 0) {
      // lower platform
      bowserY = junglePlatformY1 - bowserHeight;
      bowserYVelocity = 0;
    }else if(bowserX+bowserWidth > junglePlatformX2+background1X
               && bowserX < junglePlatformX2+background1X+junglePlatformW
               && bowserY+bowserHeight > junglePlatformY2
               && bowserY+bowserHeight < junglePlatformY2 + junglePlatformH && bowserYVelocity >= 0) {
      // higher platform
      bowserY = junglePlatformY2 - bowserHeight;
      bowserYVelocity = 0;
    }else if(bowserX+bowserWidth > junglePlatformX2+background2X
               && bowserX < junglePlatformX2+background2X+junglePlatformW
               && bowserY+bowserHeight > junglePlatformY2
               && bowserY+bowserHeight < junglePlatformY2 + junglePlatformH && bowserYVelocity >= 0) {
      // higher platform
      bowserY = junglePlatformY2 - bowserHeight;
      bowserYVelocity = 0;
    }
  }
  
  public static void lvlThreePlatformingLogic() {
    if(bowserX+bowserWidth > pipePlatformX1+background1X
         && bowserX < pipePlatformX1+background1X+pipePlatformW
         && bowserY+bowserHeight > pipePlatformY1
         && bowserY+bowserHeight < pipePlatformY1 + 30 && bowserYVelocity >= 0) {
      // lower platform
      bowserY = pipePlatformY1 - bowserHeight;
      bowserYVelocity = 0;
    } else if(bowserX+bowserWidth > pipePlatformX1+background2X
                && bowserX < pipePlatformX1+background2X+pipePlatformW
                && bowserY+bowserHeight > pipePlatformY1
                && bowserY+bowserHeight < pipePlatformY1 + 30 && bowserYVelocity >= 0) {
      // lower platform
      bowserY = pipePlatformY1 - bowserHeight;
      bowserYVelocity = 0;
    } else if(bowserX+bowserWidth > pipePlatformX2+background1X
                && bowserX < pipePlatformX2+background1X+pipePlatformW
                && bowserY+bowserHeight > pipePlatformY2
                && bowserY+bowserHeight < pipePlatformY2 + 30 && bowserYVelocity >= 0) {
      // higher platform
      bowserY = pipePlatformY2 - bowserHeight;
      bowserYVelocity = 0;
    } else if(bowserX+bowserWidth > pipePlatformX2+background2X
                && bowserX < pipePlatformX2+background2X+pipePlatformW
                && bowserY+bowserHeight > pipePlatformY2
                && bowserY+bowserHeight < pipePlatformY2 + 30 && bowserYVelocity >= 0) {
      // higher platform
      bowserY = pipePlatformY2 - bowserHeight;
      bowserYVelocity = 0;
    } else if(bowserX+bowserWidth > pipePlatformX3+background1X
                && bowserX < pipePlatformX3+background1X+pipePlatformW
                && bowserY+bowserHeight > pipePlatformY3
                && bowserY+bowserHeight < pipePlatformY3 + 30 && bowserYVelocity >= 0) {
      // higher platform
      bowserY = pipePlatformY3 - bowserHeight;
      bowserYVelocity = 0;
    } else if(bowserX+bowserWidth > pipePlatformX3+background2X
                && bowserX < pipePlatformX3+background2X+pipePlatformW
                && bowserY+bowserHeight > pipePlatformY3
                && bowserY+bowserHeight < pipePlatformY3 + 30 && bowserYVelocity >= 0) {
      // higher platform
      bowserY = pipePlatformY2 - bowserHeight;
      bowserYVelocity = 0;
    }
  }
  
  public static void lvlFivePlatformingLogic() {
    // Mushroom platforms
    if(bowserX+bowserWidth > mushroomPlatformX01+background1X
         && bowserX < mushroomPlatformX01+background1X+mushroomPlatformW0
         && bowserY+bowserHeight > mushroomPlatformY01
         && bowserY+bowserHeight < mushroomPlatformY01 + mushroomPlatformH0 && bowserYVelocity >= 0) {
      // lower platform
      bowserY = mushroomPlatformY01 - bowserHeight;
      bowserYVelocity = 0;
    }else if(bowserX+bowserWidth > mushroomPlatformX11+background2X
               && bowserX < mushroomPlatformX11+background2X+mushroomPlatformW1
               && bowserY+bowserHeight > mushroomPlatformY11
               && bowserY+bowserHeight < mushroomPlatformY11 + mushroomPlatformH1 && bowserYVelocity >= 0) {
      // higher platform
      bowserY = mushroomPlatformY11 - bowserHeight;
      bowserYVelocity = 0;
    }else if(bowserX+bowserWidth > mushroomPlatformX02+background1X
               && bowserX < mushroomPlatformX02+background1X+mushroomPlatformW0
               && bowserY+bowserHeight > mushroomPlatformY02
               && bowserY+bowserHeight < mushroomPlatformY02 + mushroomPlatformH0 && bowserYVelocity >= 0) {
      // lower platform
      bowserY = mushroomPlatformY02 - bowserHeight;
      bowserYVelocity = 0;
    }else if(bowserX+bowserWidth > mushroomPlatformX01+background2X
               && bowserX < mushroomPlatformX01+background2X+mushroomPlatformW0
               && bowserY+bowserHeight > mushroomPlatformY01
               && bowserY+bowserHeight < mushroomPlatformY01 + mushroomPlatformH0 && bowserYVelocity >= 0) {
      // lower platform
      bowserY = mushroomPlatformY01 - bowserHeight;
      bowserYVelocity = 0;
    } else if(bowserX+bowserWidth > mushroomPlatformX11+background1X
                && bowserX < mushroomPlatformX11+background1X+mushroomPlatformW1
                && bowserY+bowserHeight > mushroomPlatformY11
                && bowserY+bowserHeight < mushroomPlatformY11 + mushroomPlatformH1 && bowserYVelocity >= 0) {
      // higher platform
      bowserY = mushroomPlatformY11 - bowserHeight;
      bowserYVelocity = 0;
    }else if(bowserX+bowserWidth > mushroomPlatformX02+background2X
               && bowserX < mushroomPlatformX02+background2X+mushroomPlatformW0
               && bowserY+bowserHeight > mushroomPlatformY02
               && bowserY+bowserHeight < mushroomPlatformY02 + mushroomPlatformH0 && bowserYVelocity >= 0) {
      // lower platform
      bowserY = mushroomPlatformY02 - bowserHeight;
      bowserYVelocity = 0;
    }
  }
  
//------------------------------------------------------------------------------  
  static class GraphicsPanel extends JPanel{
    public GraphicsPanel(){
      setFocusable(true);
      requestFocusInWindow();
    }
    public void paintComponent(Graphics g){
      super.paintComponent(g); //required
      //------------------------------------------------------------------
      // do all your drawings here 
      //------------------------------------------------------------------
      
      if(level == TITLE_SCREEN) {
        g.drawImage(titleScreen, 0, 0, this);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 1000, 100);
      } else if(level == MAIN_MENU) {
        // draw main menu
        Color buttonColour = new Color(186, 20, 82);
        // Display menu background
        g.drawImage(menuImage, 0, 0, this);
        g.setColor(Color.white);
        g.setFont(largeFont);
        g.drawString("MAIN MENU", 475, 175);
        // Display the level buttons and setting button
        for(int j = 1; j <= 2; j++) {
          for(int i = 1; i <= 3; i++) {
            g.setColor(buttonColour);
            g.fillRect(i*250-150, j*300, 200, 200);
            g.setColor(Color.white);
            g.setFont(largeFont);
            if(j == 1) {
              g.drawString(Integer.toString(i+j-1), i*250-40, j*300+180);
            } else if(j == 2 && i < 3) {
              g.drawString(Integer.toString(i+j+1), i*250-40, j*300+180);
            } else if(j == 2 && i == 3) {
              g.setFont(smallFont);
              g.drawString("Settings", i*250-130, j*300+120);
              g.drawString("& Rules", i*250-130, j*300+150);
            }
          }
        }
        
      }else if(level == LEVEL_ONE) {
        // Jungle Background Colours
        Color jungleBrown = new Color(84,67,39);
        Color jungleLightBrown = new Color(132,108,72);
        Color jungleBlue = new Color(183,207,219);
        Color jungleGreen = new Color(85,140,85);
        Color jungleLightGreen = new Color(103,180,105);
        
        // Jungle Background 1
        g.setColor(jungleBlue);
        g.fillRect(background1X,background1Y,1500,800);
        // log platform
        g.drawImage(junglePlatform, junglePlatformX1+background1X-50,junglePlatformY1+background1Y-40, this);
        g.drawImage(junglePlatform, junglePlatformX2+background1X-50,junglePlatformY2+background1Y-40, this);
        for(int i = 0; i < 5; i++) {
          g.setColor(jungleLightBrown);
          g.fillRect(300*i+100+background1X,background1Y+200,100,600);
          // Draw tree leaves
          g.setColor(jungleGreen);
          g.fillOval(300*i-50+background1X,background1Y-250,400,500);
        }
        for(int i = 1; i <= 3; i+=2) {
          g.fillOval(300*i-50+background1X,background1Y-200,400,500);
        }
        // Draw ground
        g.fillRect(background1X,800+background1Y,1500,200);
        g.drawImage(grass, background1X,760+background1Y, this);
        
        // Jungle Background 2
        // sky
        g.setColor(jungleBlue);
        g.fillRect(background2X,background2Y,1500,800);
        // log platform
        g.drawImage(junglePlatform, junglePlatformX1+background2X-50,junglePlatformY1+background2Y-40, this);
        g.drawImage(junglePlatform, junglePlatformX2+background2X-50,junglePlatformY2+background2Y-40, this);
        for(int i = 0; i < 5; i++) {
          g.setColor(jungleLightBrown);
          g.fillRect(300*i+100+background2X,background2Y+200,100,600);
          // Draw tree leaves
          g.setColor(jungleGreen);
          g.fillOval(300*i-50+background2X,background2Y-250,400,500);
        }
        for(int i = 0; i <= 5; i+=2) {
          g.fillOval(300*i-50+background2X,background2Y-200,400,500);
        }
        // Draw ground
        g.fillRect(background2X,800+background2Y,1500,200);
        g.drawImage(grass, background2X,760+background2Y, this);
        
        
        //log platform hitbox
        g.setColor(Color.red);
        g.drawRect(junglePlatformX1+background1X,junglePlatformY1+background1Y,junglePlatformW, junglePlatformH);
        g.drawRect(junglePlatformX1+background2X,junglePlatformY1+background2Y,junglePlatformW, junglePlatformH);
        
        g.drawRect(junglePlatformX2+background1X,junglePlatformY2+background1Y,junglePlatformW, junglePlatformH);
        g.drawRect(junglePlatformX2+background2X,junglePlatformY2+background2Y,junglePlatformW, junglePlatformH);
      } else if(level == LEVEL_TWO) {
        // DRAW JUNGLE BACKGROUND (DOES NOT MOVE)
        // Jungle Background Colours
        Color jungleBrown = new Color(84,67,39);
        Color jungleLightBrown = new Color(132,108,72);
        Color jungleBlue = new Color(183,207,219);
        Color jungleGreen = new Color(85,140,85);
        Color jungleLightGreen = new Color(103,180,105);
        
        // Draw sky
        g.setColor(jungleBlue);
        g.fillRect(background1X,background1Y,1500,800);
        // Draw trees
        for(int i = 0; i < 5; i++) {
          g.setColor(jungleLightBrown);
          g.fillRect(300*i+100+background1X,background1Y+200,100,600);
          g.setColor(jungleGreen);
          g.fillOval(300*i-50+background1X,background1Y-250,400,500);
        }
        for(int i = 1; i <= 3; i+=2) {
          g.fillOval(300*i-50+background1X,background1Y-200,400,500);
        }
        // Draw ground
        g.fillRect(background1X,800+background1Y,1500,200);
        g.drawImage(grass, background1X,760+background1Y, this);
        
        // Draw donkey kong        
        g.drawImage(kongImage, kongX, kongY, this);
        if(kongDirection == DIRECTION_RIGHT) {
          g.drawImage(kongPlaneR, kongX-50, kongY+80, this);
        } else if(kongDirection == DIRECTION_LEFT) {
          g.drawImage(kongPlaneL, kongX-50, kongY+80, this);
        }
        
        // draw barrels
        for(int i = 0; i < totalBarrel; i++) {
          g.drawImage(kongBarrel,barrelX[i],barrelY[i]-60, this);
        }
        
        // Draw kong health bar
        
        g.setColor(Color.red);
        g.fillRect(1500/2-kongMaxHealth*20/2,5,kongMaxHealth*20,40);
        g.setColor(Color.green);
        g.fillRect(1500/2-kongMaxHealth*20/2,5,kongCurrentHealth*20,40);
        
      } else if(level == LEVEL_THREE) { // --------------------------------------------------------------------
        // Plain Background Colours
        Color plainBlue = new Color(96,150,255);
        
        // Plain Background 1
        // sky
        g.setColor(plainBlue);
        g.fillRect(background1X,background1Y,1500,800);
        // pipe platform
        g.drawImage(pipePlatform, pipePlatformX1+background1X,pipePlatformY1+background1Y, this);
        g.drawImage(pipePlatform, pipePlatformX2+background1X,pipePlatformY2+background1Y, this);
        g.drawImage(pipePlatform, pipePlatformX3+background1X,pipePlatformY3+background1Y, this);
        for(int i = 0; i < 5; i++) {
          // Draw clouds in sky
          g.setColor(Color.white);
          g.fillOval(300*i-50+background1X,background1Y-250,400,500);
        }
        for(int i = 1; i <= 3; i+=2) {
          g.fillOval(300*i-50+background1X,background1Y-200,400,500);
        }
        // Draw ground
        g.drawImage(blockFloor, background1X,GROUND+background1Y, this);
        
        // Jungle Background 2
        // sky
        g.setColor(plainBlue);
        g.fillRect(background2X,background2Y,1500,800);
        // pipe platform
        g.drawImage(pipePlatform, pipePlatformX1+background2X,pipePlatformY1+background2Y, this);
        g.drawImage(pipePlatform, pipePlatformX2+background2X,pipePlatformY2+background2Y, this);
        g.drawImage(pipePlatform, pipePlatformX3+background2X,pipePlatformY3+background2Y, this);
        for(int i = 0; i < 5; i++) {
          // Draw clouds in sky
          g.setColor(Color.white);
          g.fillOval(300*i-50+background2X,background2Y-250,400,500);
        }
        for(int i = 1; i <= 3; i+=2) {
          g.fillOval(300*i-50+background2X,background2Y-200,400,500);
        }
        // Draw ground
        g.drawImage(blockFloor, background2X,GROUND+background2Y, this);
        
        
        // PIPE PLATFORM HITBOX ------------------------------------------------------
        g.setColor(Color.red);
        g.drawRect(pipePlatformX1+background1X,pipePlatformY1+background1Y,pipePlatformW, 30);
        g.drawRect(pipePlatformX1+background2X,pipePlatformY1+background2Y,pipePlatformW, 30);
        
        g.drawRect(pipePlatformX2+background1X,pipePlatformY2+background1Y,pipePlatformW, 30);
        g.drawRect(pipePlatformX2+background2X,pipePlatformY2+background2Y,pipePlatformW, 30);
        
        g.drawRect(pipePlatformX3+background1X,pipePlatformY3+background1Y,pipePlatformW, 30);
        g.drawRect(pipePlatformX3+background2X,pipePlatformY3+background2Y,pipePlatformW, 30);
        
      } else if(level == LEVEL_FOUR) {
        // Finish in future (mario boss and saving peach cutscene)
      } else if(level == LEVEL_FIVE) {
        // Plain Background 1
        // sky
        Color plainBlue = new Color(96,150,255);
        g.setColor(plainBlue);
        g.fillRect(background1X,background1Y,1510,800);
        // mushroom platform
        g.drawImage(mushroomPlatform0, mushroomPlatformX01+background1X,mushroomPlatformY01+background1Y, this);
        g.drawImage(mushroomPlatform1, mushroomPlatformX11+background1X,mushroomPlatformY11+background1Y, this);
        g.drawImage(mushroomPlatform0, mushroomPlatformX02+background1X,mushroomPlatformY02+background1Y, this);
        for(int i = 0; i < 5; i++) {
          // Draw clouds in sky
          g.setColor(Color.white);
          g.fillOval(300*i-50+background1X,background1Y-250,400,500);
        }
        for(int i = 1; i <= 3; i+=2) {
          g.fillOval(300*i-50+background1X,background1Y-200,400,500);
        }
        // Draw ground
        g.drawImage(blockFloor, background1X,GROUND+background1Y, this);
        
        // Jungle Background 2
        // sky
        g.setColor(plainBlue);
        g.fillRect(background2X,background2Y,1510,800);
        // mushroom platform
        g.drawImage(mushroomPlatform0, mushroomPlatformX01+background2X,mushroomPlatformY01+background2Y, this);
        g.drawImage(mushroomPlatform1, mushroomPlatformX11+background2X,mushroomPlatformY11+background2Y, this);
        g.drawImage(mushroomPlatform0, mushroomPlatformX02+background2X,mushroomPlatformY02+background2Y, this);
        for(int i = 0; i < 5; i++) {
          // Draw clouds in sky
          g.setColor(Color.white);
          g.fillOval(300*i-50+background2X,background2Y-250,400,500);
        }
        for(int i = 1; i <= 3; i+=2) {
          g.fillOval(300*i-50+background2X,background2Y-200,400,500);
        }
        // Draw ground
        g.drawImage(blockFloor, background2X,GROUND+background2Y, this);
        
        
        // MUSHROOM PLATFORM HITBOX ------------------------------------------------------
        g.setColor(Color.red);
        // low platform
        g.drawRect(mushroomPlatformX01+background1X,mushroomPlatformY01+background1Y,mushroomPlatformW0, mushroomPlatformH0);
        g.drawRect(mushroomPlatformX01+background2X,mushroomPlatformY01+background2Y,mushroomPlatformW0, mushroomPlatformH0);
        // high platform
        g.drawRect(mushroomPlatformX11+background1X,mushroomPlatformY11+background1Y,mushroomPlatformW1, mushroomPlatformH1);
        g.drawRect(mushroomPlatformX11+background2X,mushroomPlatformY11+background2Y,mushroomPlatformW1, mushroomPlatformH1);
        // low platform
        g.drawRect(mushroomPlatformX02+background1X,mushroomPlatformY02+background1Y,mushroomPlatformW0, mushroomPlatformH0);
        g.drawRect(mushroomPlatformX02+background2X,mushroomPlatformY02+background2Y,mushroomPlatformW0, mushroomPlatformH0);
      } else if(level == CUT_SCENE00) {
        g.drawImage(cutScene0[0],0,0,this);
        g.setColor(Color.black);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 910);
      } else if(level == CUT_SCENE01) {
        g.drawImage(cutScene0[1],0,0,this);
        g.setColor(Color.black);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 910);
      } else if(level == CUT_SCENE02) {
        g.drawImage(cutScene0[2],0,0,this);
        g.setColor(Color.black);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 910);
      } else if(level == CUT_SCENE10) {
        g.drawImage(cutScene1[0],0,0,this);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 100);
      } else if(level == CUT_SCENE11) {
        g.drawImage(cutScene1[1],0,0,this);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 100);
      } else if(level == CUT_SCENE20) {
        g.drawImage(cutScene2[0],0,0,this);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 100);
      } else if(level == CUT_SCENE21) {
        g.drawImage(cutScene2[1],0,0,this);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 100);
      } else if(level == CUT_SCENE22) {
        g.drawImage(cutScene2[2],0,0,this);
        g.setColor(Color.black);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 910);
      } else if(level == CUT_SCENE30) {
        g.drawImage(cutScene3[0],0,0,this);
        g.setColor(Color.black);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 910);
      }else if(level == CUT_SCENE40) {
        g.drawImage(cutScene4[0],0,0,this);
        g.setColor(Color.black);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 100);
      } else if(level == CUT_SCENE41) {
        g.drawImage(cutScene4[1],0,0,this);
        g.setColor(Color.black);
        g.setFont(smallFont);
        g.drawString("Left click to continue", 100, 100);
      } else if(level == SETTINGS) {
        Color jungleBlue = new Color(183,207,219);
        g.setColor(jungleBlue);
        g.fillRect(0,0,1500, 1000);
        g.setColor(Color.black);
        g.setFont(smallFont);
        g.drawString("Objective of level 1,3,5: Collect 5 flowers in each level in order to earn Peach's favour.",20,50);
        g.drawString("You may have to 'take out' a few people on your journey to Peach",20,100);
        g.drawString("(Also bowser doesn't 'take out' enemies when he jumps on them; that's op!)",20,150);
        g.drawString("Boss fights: level 2,4 (lvl 4 coming soon)",20,200);
        g.drawString("Controls:",20,300);
        g.drawString("- Hold down LEFT and RIGHT arrow keys to move",20,350);
        g.drawString("- SPACE to jump",20,400);
        g.drawString("- Q to shoot a constant rate of fire (I suggest tapping Q when moving)",20,450);
        g.drawString("- UP arrow to point gun up (Will need for level 2)",20,500);
        g.drawString("Nintendo please don't copyright claim me", 20, 600);
        g.drawString("Left click to return to main menu", 20, 800);
      }
      
      // when in a level, draw bowser, gun, bullets, enemies, bosses, flowers, platforms, etc.
      if(level == LEVEL_ONE || level == LEVEL_TWO || level == LEVEL_THREE || level == LEVEL_FOUR || level == LEVEL_FIVE) {
        // GUN SHOOTING ---------------------------------------------------------------------------------------------
        for(int i = 0; i < totalBullets; i++) {
          // Shooting right
          if(bulletVisibleR[i] == true) {
            g.setColor(Color.red);
            g.fillOval(bulletXR[i], bulletYR[i], bulletWidth, bulletHeight);
          }
          // Shooting left
          if(bulletVisibleL[i] == true) {
            g.setColor(Color.red);
            g.fillOval(bulletXL[i], bulletYL[i], bulletWidth, bulletHeight);
          }
          // Shooting up right
          if(bulletVisibleUR[i] == true) {
            g.setColor(Color.red);
            g.fillOval(bulletXUR[i], bulletYUR[i], bulletWidth, bulletHeight);
          }
          // Shooting up left
          if(bulletVisibleUL[i] == true) {
            g.setColor(Color.red);
            g.fillOval(bulletXUL[i], bulletYUL[i], bulletWidth, bulletHeight);
          }
        }
        
        // DRAW BOWSER AND GUN -------------------------------------------------------------------------
        // Draws bowser, hitbox, and gun depending either left or right
        if(bowserDirection == DIRECTION_RIGHT) {
          if(tommyWeaponIndex == 0) {
            // Draw weapon right
            g.drawImage(tommyGun[tommyWeaponIndex], bowserX-5, bowserY+5, this);
          } else if(tommyWeaponIndex == 2) {
            // Draws gun pointing up
            g.drawImage(tommyGun[tommyWeaponIndex], bowserX+20, bowserY-80, this);
          }
          // Draw bowser
          g.drawImage(bowserImage[bowserAnimationIndex], bowserX, bowserY, this);
          g.setColor(Color.red);
          // main hitbox
          g.drawRect(bowserX,bowserY,bowserWidth, bowserHeight);
        } else if(bowserDirection == DIRECTION_LEFT) {
          if(tommyWeaponIndex == 1) {
            // Draw weapon left
            g.drawImage(tommyGun[tommyWeaponIndex], bowserX-105, bowserY+5, this);
          } else if(tommyWeaponIndex == 3) {
            // Draws gun pointing up
            g.drawImage(tommyGun[tommyWeaponIndex], bowserX-105, bowserY-80, this);
          }
          // Draw bowser
          g.drawImage(bowserImage[bowserAnimationIndex], bowserX, bowserY, this);
          g.setColor(Color.red);
          // main hitbox
          g.drawRect(bowserX,bowserY,bowserWidth, bowserHeight);
        }
        
        // Draw bowser's healthbar
        // If bowser loses health, missing health is red
        g.setColor(Color.red);
        g.fillRect(50, 850, bowserHealthBarWidth,bowserHealthBarHeight);
        for(int i = 0; i < bowserCurrentHealth; i++) {
          g.setColor(Color.green);
          g.fillRect(50+i*bowserHealthBarWidth/bowserMaxHealth, 850, bowserHealthBarWidth/bowserMaxHealth, bowserHealthBarHeight);
        }
        
        // Draw enemies
        // Draw yoshi
        for(int i = 0; i < totalYoshi; i++) {
          // yoshi move right
          if(yoshiVisibleR[i] == true) {
            g.drawImage(yoshiImage[yoshiAnimationIndex], yoshiXR[i], yoshiYR[i], this);
            g.setColor(Color.red);
            g.drawRect(yoshiXR[i], yoshiYR[i], yoshiWidth, yoshiHeight);
          }
          // yoshi move left
          if(yoshiVisibleL[i] == true) {
            g.drawImage(yoshiImage[yoshiAnimationIndex], yoshiXL[i], yoshiYL[i], this);
            g.setColor(Color.red);
            g.drawRect(yoshiXL[i], yoshiYL[i], yoshiWidth, yoshiHeight);
          }
        }
        
        // Draw toad and gun
        for(int i = 0; i < totalToad; i++) {
          // toad draw right
          if(toadVisibleR[i] == true) {
            g.drawImage(toadImage[toadAnimationIndex], toadXR[i], toadYR[i], this);
            g.drawImage(toadGun[toadWeaponIndex], toadXR[i]+20, toadYR[i]+40, this);
            g.setColor(Color.red);
            g.drawRect(toadXR[i], toadYR[i], toadWidth, toadHeight);
          }
          // toad draw left
          if(toadVisibleL[i] == true) {
            g.drawImage(toadImage[toadAnimationIndex], toadXL[i], toadYL[i], this);
            g.drawImage(toadGun[toadWeaponIndex], toadXL[i]-5, toadYL[i]+40, this);
            g.setColor(Color.red);
            g.drawRect(toadXL[i], toadYL[i], toadWidth, toadHeight);
          }
        }
        // Draw toad bullets
        for(int i = 0; i < toadTotalBullets; i++) {
          // toad draw right
          if(toadBulletVisibleR[i] == true) {
            g.setColor(Color.blue);
            g.fillRect(toadBulletXR[i], toadBulletYR[i], toadBulletWidth, toadBulletHeight);
          }
          // toad draw left
          if(toadBulletVisibleL[i] == true) {
            g.setColor(Color.blue);
            g.fillRect(toadBulletXL[i], toadBulletYL[i], toadBulletWidth, toadBulletHeight);
          }
        }
        
        // Draw flower
        for(int i = 0; i < totalFlower; i++) {
          if(flowerVisible[i] == true) {
            g.drawImage(flowerImage, flowerX[i], flowerY[i], this);
            g.setColor(Color.red);
            g.drawRect(flowerX[i], flowerY[i], flowerWidth, flowerHeight);
          }
        }
        
        if(level == LEVEL_ONE || level == LEVEL_THREE || level == LEVEL_FIVE) {
          // Draw flower score
          g.setColor(Color.white);
          g.setFont(smallFont);
          g.drawString("Flowers Collected: " + Integer.toString(flowerCount), 700, GROUND+75);
        }
        
      } else if(level == GAME_OVER) {
        // display the game over screen
        g.drawImage(gameOverScreen, 0, 0, this);
        g.setColor(Color.white);
        g.setFont(smallFont);
        g.drawString("Left click to return to main menu", 100, 830);
      }
    } // paintComponent method end
  } // GraphicsPanel class end   
  
//------------------------------------------------------------------------------     
  static class MyKeyListener implements KeyListener{   
    public void keyPressed(KeyEvent e){
      int key = e.getKeyCode();
      if(key == KeyEvent.VK_LEFT){
        bowserXVelocity = -B_RUN_SPEED;
        bowserDirection = DIRECTION_LEFT;
        if(shootUp == UP_RIGHT) {
          shootUp = UP_LEFT;
          tommyWeaponPosition = "weapon up left";
        }
      } else if(key == KeyEvent.VK_RIGHT) {
        bowserXVelocity = B_RUN_SPEED;
        bowserDirection = DIRECTION_RIGHT;
        if(shootUp == UP_LEFT) {
          shootUp = UP_RIGHT;
          tommyWeaponPosition = "weapon up right";
        }
      } else if(key == KeyEvent.VK_SPACE && bowserYVelocity == 0) {
        bowserYVelocity = B_JUMP_SPEED;
        if(bowserDirection == DIRECTION_LEFT) {
          bowserAnimation = "jump left";
        } else if(bowserDirection == DIRECTION_RIGHT) {
          bowserAnimation = "jump right";
        }
        
      } else if(key == KeyEvent.VK_SPACE) {
        // need this condition so bowserXVelocity continues when jumping     
      } else if(key == KeyEvent.VK_UP) {
        // if up arrow is pressed, gun points up with direction bower is facing
        if(bowserDirection == DIRECTION_LEFT) {
          tommyWeaponPosition = "weapon up left";
          shootUp = UP_LEFT;
        } else if(bowserDirection == DIRECTION_RIGHT) {
          tommyWeaponPosition = "weapon up right";
          shootUp = UP_RIGHT;
        }
      } else if(key == KeyEvent.VK_Q && shootUp == UP_RIGHT && System.currentTimeMillis() - shootTime > 400) {
        // Must wait 150 ms to fire another shot right. Fire rate is around 6.7 bullets/s
        shootTime = System.currentTimeMillis();
        bulletXUR[currentBullet] = bowserX+85;
        bulletYUR[currentBullet] = bowserY-40;
        bulletVisibleUR[currentBullet] = true;
        currentBullet = (currentBullet + 1)%totalBullets;
      } else if(key == KeyEvent.VK_Q && shootUp == UP_LEFT && System.currentTimeMillis() - shootTime > 400) {
        // Must wait 150 ms to fire another shot left. Fire rate is around 6.7 bullets/s
        shootTime = System.currentTimeMillis();
        bulletXUL[currentBullet] = bowserX+5;
        bulletYUL[currentBullet] = bowserY-40;
        bulletVisibleUL[currentBullet] = true;
        currentBullet = (currentBullet + 1)%totalBullets;
      } else if(key == KeyEvent.VK_Q && bowserDirection == DIRECTION_RIGHT && System.currentTimeMillis() - shootTime > 400) {
        // Must wait 150 ms to fire another shot up right. Fire rate is around 6.7 bullets/s
        shootTime = System.currentTimeMillis();
        bulletXR[currentBullet] = bowserX+bowserWidth+80;
        bulletYR[currentBullet] = bowserY+75;
        bulletVisibleR[currentBullet] = true;
        currentBullet = (currentBullet + 1)%totalBullets;
      } else if(key == KeyEvent.VK_Q && bowserDirection == DIRECTION_LEFT && System.currentTimeMillis() - shootTime > 400) {
        // Must wait 150 ms to fire another shot up left. Fire rate is around 6.7 bullets/s
        shootTime = System.currentTimeMillis();
        bulletXL[currentBullet] = bowserX+bowserWidth-160;
        bulletYL[currentBullet] = bowserY+75;
        bulletVisibleL[currentBullet] = true;
        currentBullet = (currentBullet + 1)%totalBullets;
      } else if(key == KeyEvent.VK_Q) {
        // must include this if statement so bowser doesn't lose momentum when in the air and q is pressed
      } else {
        bowserXVelocity = 0;
      }
    }
    public void keyReleased(KeyEvent e){ 
      int key = e.getKeyCode();
      if (key == KeyEvent.VK_ESCAPE){
        gameWindow.dispose();
      }
      // Stops bowser when not pressing movement keys
      if(key == KeyEvent.VK_LEFT && bowserXVelocity == -B_RUN_SPEED) {
        bowserXVelocity = 0;
      }
      if(key == KeyEvent.VK_RIGHT && bowserXVelocity == B_RUN_SPEED) {
        bowserXVelocity = 0;
      }
      // Bowser lowers gun when up arrow is released
      if(key == KeyEvent.VK_UP) {
        if(bowserDirection == DIRECTION_LEFT) {
          tommyWeaponPosition = "weapon left";
        } else if(bowserDirection == DIRECTION_RIGHT) {
          tommyWeaponPosition = "weapon right";
        }
        shootUp = UP_NO;
      }
    }
    public void keyTyped(KeyEvent e){
      char keyChar = e.getKeyChar();
    }         
  } // MyKeyListener class end
  
//------------------------------------------------------------------------------
  static class MyMouseListener implements MouseListener{
    public void mouseClicked(MouseEvent e){
      int mouseX = e.getX();
      int mouseY = e.getY();
      int clickButton = e.getButton();
      if (clickButton == 1) {
        // Transitioning through cutscenes, levels, main menu
        if(level == TITLE_SCREEN) {
          level = CUT_SCENE00;
        } else if(level == CUT_SCENE00) {
          level = CUT_SCENE01;
        } else if(level == CUT_SCENE01) {
          level = CUT_SCENE02;
        } else if(level == CUT_SCENE02) {
          level = MAIN_MENU;
        } else if(level == CUT_SCENE10) {
          level = CUT_SCENE11;
        } else if(level == CUT_SCENE11) {
          level = MAIN_MENU;
        } else if(level == CUT_SCENE20) {
          level = CUT_SCENE21;
        } else if(level == CUT_SCENE21) {
          level = CUT_SCENE22;
        } else if(level == CUT_SCENE22) {
          level = MAIN_MENU;
        } else if(level == CUT_SCENE30) {
          level = MAIN_MENU;
        } else if(level == CUT_SCENE40) {
          level = CUT_SCENE41;
        } else if(level == CUT_SCENE41) {
          level = MAIN_MENU;
        } else if(level == SETTINGS) {
          level = MAIN_MENU;
        } else if(level == MAIN_MENU) {
          // Detect if menu buttons are pressed by mouse click
          if(mouseX >= 100 && mouseX <= 300 && mouseY >= 300 && mouseY <= 500) {
            level = LEVEL_ONE;
          } else if(mouseX >= 350 && mouseX <= 650 && mouseY >= 300 && mouseY <= 500) {
            level = LEVEL_TWO;
          } else if(mouseX >= 600 && mouseX <= 900 && mouseY >= 300 && mouseY <= 500) {
            level = LEVEL_THREE;
          } else if(mouseX >= 100 && mouseX <= 300 && mouseY >= 600 && mouseY <= 800) {
//            level = LEVEL_FOUR;
          } else if(mouseX >= 350 && mouseX <= 650 && mouseY >= 600 && mouseY <= 800) {
            level = LEVEL_FIVE;
          } else if(mouseX >= 600 && mouseX <= 900 && mouseY >= 600 && mouseY <= 800) {
            level = SETTINGS;
          }
        } else if(level == GAME_OVER) {
          level = MAIN_MENU;
        }
      } else if (clickButton == 3) {
        // right mousebutton is clicked
        
      }
    }
    
    // DO NOT COMMENT SECTION BELOW OUT, IS NEEDED
    public void mousePressed(MouseEvent e){
    }
    public void mouseReleased(MouseEvent e){
    }
    public void mouseEntered(MouseEvent e){
    }
    public void mouseExited(MouseEvent e){
    }
  } // MyMouseListener class end
  
//------------------------------------------------------------------------------     
  static class MyMouseMotionListener implements MouseMotionListener{
    public void mouseMoved(MouseEvent e){
    }
    public void mouseDragged(MouseEvent e){
    }         
  } // MyMouseMotionListener class end
  
} // Bowser Game class end