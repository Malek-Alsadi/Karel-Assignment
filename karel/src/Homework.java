import apple.laf.JRSUIConstants;
import com.sun.org.apache.xpath.internal.operations.Plus;
import stanford.karel.SuperKarel;

import java.util.Collections;

public class Homework extends SuperKarel {
    public void run(){

        int high = 1;
        int width = 1;
        beepers = 0;
        while(frontIsClear()){move();width++;}
        turnLeft();
        while(frontIsClear()){move();high++;}
        turnLeft();
        System.out.println("the messuring steps = " + (width + high - 2));
        int steps = 0;
        if( high == 1 || width == 1){
            System.out.println("number of steps = " + onOne(high, width) + '\n' + "number of beepers = " + beepers);
        }else if(high == 2 || width == 2){
            System.out.println("number of steps = " + onTwo(high,width) + '\n' + "number of beepers = " + beepers);
        }
        else if( high%2!=0 && width%2!=0){
            System.out.println("number of steps = " + onOdds(high,width) + '\n' + "number of beepers = " + beepers);
        }
        else if( high%2 + width%2 == 0)  {
            System.out.println("number of steps = " + onEvens(high,width) + '\n' + "number of beepers = " + beepers);
        }
        else
            System.out.println("number of steps = " + EvenOdd(high,width) + '\n' + "number of beepers = " + beepers);
    }
    public static int beepers = 0;
    int plusCount(int high , int width){high--;width--; return width*2+high+high/2;}
    int sCount(int high , int width){
        if((width-3)%4!=0)
            return 10000;
        return (high-1)*3 + width - 1 - (width-3)/4;
    }
    int onOne(int high, int width){
        int ans = 0;
        boolean swap = false;
        if(high == 1){
            int tmp = width ;
            width = high;
            high = tmp;
            swap = true;
        }
        if(!swap)    turnLeft();
        int chmprSize = high>8?high / 4-1:1;
        int chmprs =0;
        while(frontIsClear() && high!=2) {
            ans += countedSteps(chmprSize-(chmprs==0?1:0),false);
            chmprs++;
            if(frontIsClear()){
                ans += 1;
                move(); put();
            }
            if(chmprs==4)
                break;
        }
        while(frontIsClear() && high!=2)
            ans += countedSteps(1,true);
        return ans;
    }
    int onTwo(int high, int width){
        int ans = 0;
        boolean swap = false;
        if(high == 2){
            int tmp = width ;
            width = high;
            high = tmp;
            swap = true;
        }
        if(swap)    turnLeft();
        if(high<7){
            int pepr = 0;
            for(int i=0;i<7 && frontIsClear();i++) {
                ans += countedSteps(1, pepr%2==0);
                if(pepr<2 || pepr>3)
                    turnBySwap(swap,1);
                else
                turnBySwap(swap,2);
                pepr++;
            }
            if(pepr!=7)
                return ans;
            turnAround();
            ans += fillRest(swap);
        }else{
            int chmprSz = (high-3)/4;
            turnBySwap(swap,1);
            ans += countedSteps(chmprSz, false);
            ans += sWay(swap,1,chmprSz+1);
            turnBySwap(swap,1);
            if((high-3)%4==0)   return ans;
            ans += countedSteps(chmprSz+1,false);
            put();
            turnBySwap(swap,1);
            ans += countedSteps(1,true);
            turnBySwap(swap,2);
            fillRest(swap);
        }
        return ans;
    }
    int fillRest(boolean swap){
        int ans = 0;
        int cnt = 0;
        if(frontIsBlocked())    return 0;
        while(frontIsClear()){
            ans += countedSteps(1, true);
            cnt++;
        }
        turnBySwap(swap,2);
        ans += countedSteps(1, true);
        turnBySwap(swap,2);
        ans += countedSteps(cnt-1,true);
        return ans;
    }
    void turnBySwap(boolean swap,int dir){
        if(!swap){
            if(dir == 1)
                  turnLeft();
            else turnRight();
        }else{
            if(dir == 1)
                turnRight();
            else turnLeft();
        }
    }
    int onOdds(int high , int width){
        int ans = 0, plus1 = plusCount(high,width), plus2 = plusCount(width,high), plus = Math.min(plus1,plus2), s1 = sCount(high,width), s2 = sCount(width,high);
        if(high>=7 && width >=7 && (high-3)%4==0 && (width-3)%4==0){
            if(s1<=s2 && s1<plus){
                int w = (width-3)/4;
                ans += countedSteps(w,false);
                turnLeft();
                ans += sWay(true,high-1,w+1);
            }else if(s2<=s1 && s2<plus){
                int h = (high-3)/4;
                turnLeft();
                ans += countedSteps(h,false);
                ans += sWay(false,width-1,h+1);
            }else {
                if(plus1 == plus)
                    ans += PlusWay(high, width, false);
                else
                    ans += PlusWay(width, high,true);
            }
        }else if(high>=7 && (high-3)%4==0 && s2<plus){
            int h = (high-3)/4;
            turnLeft();
            ans += countedSteps(h,false);
            ans += sWay(false,width-1,h+1);
        }else if(width>=7 && (width-3)%4==0 && s1<plus){
            int w = (width-3)/4;
            ans += countedSteps(w,false);
            turnLeft();
            ans += sWay(true,high-1,w+1);
        }else{
            if(plus == plus1)
                PlusWay(high, width,false);
            else
                PlusWay(width,high,true);
        }
        return ans;
    }
    int onEvens(int high, int width){
        boolean swap = false;
        if(width > high)    {int tmp = high; high = width; width = tmp; swap = true;}
        int x1 = width/2,x2 = x1-1, y1 = high/2, y2 = y1-1;
        int area1 = y1*x2 , area2 = y2*x1;
        int diff = Math.max(area1,area2) - Math.min(area1, area2);

        int ans = 0;
        if(swap)
            turnLeft();
        if(diff>1){
            ans += countedSteps(x1,false);
            turnBySwap(swap,1);
            int m = diff%2;
            put();
            ans += countedSteps((diff-m)/2-1,true);
            ans += countedSteps(1, false);
            turnBySwap(swap,1);
            ans += countedSteps(1, false);
            turnBySwap(swap,2);
        }else{
            ans += countedSteps(x2, false);
            turnBySwap(swap,1);
        }
        put();
        ans += countedSteps(y1-(diff/2)-2,true);
        ans += countedSteps(1, diff%2==0);
        ans += countedSteps(1,true);
        turnBySwap(swap,2);
        ans += countedSteps(1, false);
        turnBySwap(swap,1);
        if(diff%2==0)   put();
        ans += countedSteps(y1-(diff/2)-1,true);
        if(diff/2 > 0) {
            ans += countedSteps(1, false);
            turnBySwap(swap,1);
            ans += countedSteps(1, false);
            turnBySwap(swap,2);
            put();
            ans += countedSteps((diff / 2) - 1, true);
        }
        turnBySwap(swap,1);
        int m = (diff/2>0)?1:0;
        ans += countedSteps(x1-m,false);
        turnBySwap(swap,1);
        ans += countedSteps(y2,false);
        turnBySwap(swap,1);
        put();
        ans += countedSteps(x1-1, true);
        ans += countedSteps(1, diff%2==0);
        turnBySwap(swap,2);
        ans += countedSteps(1,true);
        turnBySwap(swap,1);
        put();
        ans += countedSteps(x2,true);
        return ans;
    }
    int EvenOdd(int high , int width){
        int ans = 0;
        boolean swap = false;
        if(high%2!=0){
            int tmp = high;
            high = width;
            width = tmp;
            swap = true;
        }
        int corner = high/2 * width/2 - high/2 * (width/2-1);
        corner = corner%2;
        if(swap)
            turnLeft();
        ans += countedSteps(width/2,false);
        turnBySwap(swap,1);
        put();
        ans += countedSteps(high-1,true);
        turnAround();
        ans += countedSteps(high/2-1,false);
        turnBySwap(swap,2);
        for(int i=0;i<2;i++) {
            ans += countedSteps(width / 4, true);
            ans += countedSteps(1, (width / 2) % 2 == 1);
            turnBySwap(swap, 1);
            ans += countedSteps(1, true);
            turnBySwap(swap, 2);
            ans += countedSteps(width / 4 - ((width / 2) % 2 == 1 ? 0 : 1), true);
            if(i==0){
                turnAround();
                ans += countedSteps(width/2,false);
            }

        }
        return ans;
    }
    int countedSteps(int s,boolean put){
        int cnt = 0;
        while(s>0){
            move();
            if(put) put();
            s--;
            cnt++;
        }
        return cnt;
    }
    void put(){
        if(!beepersPresent()){
            putBeeper(); beepers++;
        }
    }
    int sWay(boolean swap , int len , int len2){
        int steps = 0;
        while((!swap && notFacingWest())||(swap && notFacingSouth())){
            turnLeft();
        }
        put();
        steps += countedSteps(len, true);
        turnBySwap(!swap,2);
        steps += countedSteps(len2,false);
        turnBySwap(!swap,2);
        put();
        steps += countedSteps(len,true);
        turnBySwap(!swap,1);
        steps += countedSteps(len2,false);
        turnBySwap(!swap,1);
        put();
        steps +=countedSteps(len,true);
        return steps;
    }
    int PlusWay(int high , int width,boolean swap){
        if(swap)    turnLeft();
        int ans = countedSteps(width/2,false);
        turnBySwap(swap,1);
        put();
        ans += countedSteps(high/2,true);
        for(int i=0;i<3;i++) {
            turnLeft();
            int len = (i%2==0)?width:high;
            put();
            ans += countedSteps(len / 2, true);
            turnAround();
            if(i!=2)    ans += countedSteps(len / 2, false);
        }
        return ans;
    }
}
