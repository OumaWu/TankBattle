package tank;

public class Explosion {
	
	//定义爆炸效果的坐标
	private int x, y;
	//炸弹的生命
	private int life = 31;
	private boolean isLive = true;
	
	public Explosion(int x, int y){
		this.setX(x);
		this.setY(y);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isLive() {
		return isLive;
	}

	public int getLife() {
		return life;
	}
	
	//减少生命值
	public void lifeDown(){
		if(life>0){
			life--;
		}
		else{
			this.isLive = false;
		}
	}
	
}












