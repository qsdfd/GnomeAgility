import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(name="Gnome course", author="Dokato", version=1.3D, info="", logo="")
public class mainGnome extends Script{
  private long timeBegan;
  private long timeRan;
  private long timeBotted;
  private long timeOffline;
  private String status;
  
  public void onStart()
  {
	this.timeBegan = System.currentTimeMillis();
	this.timeBotted = 0;
	this.timeOffline = 0;
  }
  
  public int onLoop() throws InterruptedException{
	status="loop started";
	procedures();
	if(myPlayer().getPosition().getZ()==0){
		if(myPlayer().getPosition().getY()>3434){
			status="Log balance";
			interact("Log balance","Walk-across",null);
		}else if(myPlayer().getPosition().getX()<2479 && myPlayer().getPosition().getY()<3434){
			status="Net 1";
			interact("Obstacle net","Climb-over",new Area(2474,3425,2473,3425));
		}else if(myPlayer().getPosition().getX()>2479 && myPlayer().getPosition().getY()<3427){
			status="Net 2";
			interact("Obstacle net","Climb-over",new Area(2485,3426,2486,3426));
		}else if(myPlayer().getPosition().getX()>2479 && myPlayer().getPosition().getY()<3434){
			status="Pipe";
			interact("Obstacle pipe","Squeeze-through",null);
		}
	}else if(myPlayer().getPosition().getZ()==1){
		status="Tree branch 1";
		interact("Tree branch","Climb",null);
	}else if(myPlayer().getPosition().getZ()==2){
		status="Z==2";
		if(myPlayer().getPosition().getX()<2480){
			status="Balancing rope";
			interact("Balancing rope","Walk-on",null);
		}else{
			status="Tree branch 2";
			interact("Tree branch","Climb-down",null);
		}
	}
	status="loop ended";
    return random(0,29);
  }
  
  private void interact(String name,String action,Area area) throws InterruptedException{
	  if(!myPlayer().isAnimating()&&!myPlayer().isMoving()){
		  if(area==null){
			  getObjects().closest(name).interact(action);
		  }else{
			  getObjects().closest(area,name).interact(action);
		  }
		  sleep(random(1100,1600));
	  }
  }
  
  private void procedures(){
	  getCamera().toTop();
	  if(getSettings().getRunEnergy()>random(12,16)){
		  getSettings().setRunning(true);
	  }
  }
  
  public void onPaint(Graphics2D g1)
  {
    this.timeRan = (System.currentTimeMillis() - this.timeBegan);
    Graphics2D g = g1;
    if (getClient().isLoggedIn()) {
      this.timeBotted = (this.timeRan - this.timeOffline);
    } else {
      this.timeOffline = (this.timeRan - this.timeBotted);
    }
    g.setFont(new Font("Arial", 0, 13));
    g.setColor(new Color(255, 255, 255));
    g.drawString("Version: "+getVersion(), 20, 170);
    g.drawString("Total runtime: " + ft(this.timeRan), 20, 185);
    g.drawString("Time botted: " + ft(this.timeBotted), 20, 200);
    
    g.drawString("Status: "+status, 20, 230);
    g.drawString("Current lvl: " + this.skills.getStatic(Skill.AGILITY), 20, 245);
    g.drawString(""+getSkills().experienceToLevel(Skill.AGILITY), 20, 260);
  }
  
  private String ft(long duration)
  {
    String res = "";
    long days = TimeUnit.MILLISECONDS.toDays(duration);
    long hours = TimeUnit.MILLISECONDS.toHours(duration) - 
      TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
    long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - 
      TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
      .toHours(duration));
    long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - 
      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
      .toMinutes(duration));
    if (days == 0L) {
      res = hours + ":" + minutes + ":" + seconds;
    } else {
      res = days + ":" + hours + ":" + minutes + ":" + seconds;
    }
    return res;
  }
}
