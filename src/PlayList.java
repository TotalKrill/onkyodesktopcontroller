
public class PlayList {
	String title;
	String[] SongTitles;
	int length;
	int menuDepth;
    boolean looped = false;
	int loopStartFlag;
	int currentTenStartsAt;
    public PlayList(String title, int length, int menuDepth,int startFlag){
    	this.title = title;
    	this.length =length;
    	if(length ==0)
    		length =1;
    	this.menuDepth = menuDepth;
    	this.looped =false;
    	this.loopStartFlag = startFlag;
    	this.SongTitles = new String[length];    	
    }
}
