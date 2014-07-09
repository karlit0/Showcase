uses graph, crt;

procedure lvl1;
begin
	cleardevice;
	settextstyle(1,2,2);
	outtextxy(getmaxx div 2-50,getmaxy div 2,'LEVEL 1');
	delay(1500);
end;


procedure lvl2;
begin
	cleardevice;
	settextstyle(1,2,2);
	outtextxy(getmaxx div 2-50,getmaxy div 2,'LEVEL 2');
	delay(1500);
end;


var gd, gm, brojac, x0, y0, yp, lives : integer;
x,b: array[1..10] of integer;
finish, dead, newgame, mbexit, defexit : boolean;
rtmm, resetlives, level1, level2 : boolean;
defdead, lvl1death, lvl2death, skipmm, oke : boolean;
input, L : char;
livesstring : char;
kodgreske : integer;
begin
	gm:=m800x600;
	gd:=d8bit;
	initgraph(gd,gm,'');
	randomize;
	rtmm:=true;
	
	repeat
		yp:=getmaxy div 2 + 50;
		
		if dead 
		then begin
			skipmm:=true;
			newgame:=true;
			resetlives:=false;
		end
		else 
			skipmm:=false;

		if rtmm=true then begin
			repeat
				skipmm:=false;
				newgame:=false;
				cleardevice;

				moveto(getmaxx div 2-120, getmaxy div 2-100);
				settextstyle(1,2,5);
				outtext('FROGGER');

				moveto(getmaxx div 2 - 90,getmaxy div 2 + 50);
				settextstyle(1,2,3);
				outtext('NEW GAME');

				moveto(getmaxx div 2 - 90,getmaxy div 2 + 90);
				outtext('CONTROLS');

				moveto(getmaxx div 2 - 90,getmaxy div 2 + 130);
				outtext('EXIT GAME');

				settextstyle(1,2,2);
				moveto(getmaxx div 2 - 110,yp+5);
				outtext('*');

				//kontrola izbornika
				input:=readkey;

				//dolje
				if (ord(input)=80) and (yp+10<getmaxy div 2 + 130)
				then 
					inc(yp,40);
				//gore
				if (ord(input)=72) and (yp-10>getmaxy div 2 + 50)
				then 
					dec(yp,40);

				//new game
				if (yp=getmaxy div 2 + 50) and (ord(input)=13) 
				then begin
					skipmm:=true;
					resetlives:=true;
					newgame:=true; 
				end;

				//exit game
				if (yp=getmaxy div 2 + 130) and (ord(input)=13) 
				then begin 
					skipmm:=true; 
					defexit:=true; 
				end;

				//Controls izbor
				if (yp=getmaxy div 2 + 90) and (ord(input)=13)
				then begin
					repeat
						cleardevice;

						moveto(getmaxx div 2-120, getmaxy div 2-100);
						settextstyle(1,2,5);
						outtext('FROGGER');

						moveto(getmaxx div 2 - 90,getmaxy div 2 + 50);
						settextstyle(1,2,3);
						outtext('Change Controls');

						moveto(getmaxx div 2 - 90, getmaxy div 2 + 90);
						outtext('Back');

						settextstyle(1,2,2);
						moveto(getmaxx div 2 - 110,yp);
						outtext('*');

						input:=readkey;

						//dolje
						if (ord(input)=80) and (yp+10<getmaxy div 2 + 90)
						then 
							inc(yp,40);
						//gore
						if (ord(input)=72) and (yp-10>getmaxy div 2 + 50)
						then 
							dec(yp,40);

					// back
					until (yp= getmaxy div 2 + 90) and (ord(input)=13)
				end;
			until skipmm;

			x0:=35;
		end;

		if newgame 
		then begin
			finish:=false;
			defdead:=false;
			newgame:=false;
			if resetlives 
			then begin 
				level1:=true;
				lives:=2; 
			end;
			
			mbexit:=false;
			cleardevice;

			// then lvl1;
			//if lvl2death then lvl2;

			//pocetne koordinate zabe
			y0:=50;

			//pocetne koordinate auta
			x[1]:=getmaxx;
			x[2]:=x[1]+250;
			x[3]:=0;
			x[4]:=0;
			x[5]:=getmaxx+100;
			x[6]:=getmaxx;
			x[7]:=-50;
			x[8]:=-25;

			//boje auta
			b[1]:=random(6)+1;
			b[2]:=random(6)+1;
			b[3]:=random(6)+1;
			b[4]:=random(6)+1;
			b[5]:=random(6)+1;
			b[6]:=random(6)+1;
			b[7]:=random(6)+1;
			b[8]:=random(6)+1;

			repeat
				//ciscenje ekrana
				delay(50);
				cleardevice;

				//cesta
				moveto(0,70);
				lineto(getmaxx,70);
				moveto(0,75);
				lineto(getmaxx,75);

				moveto(0,175);
				lineto(getmaxx,175);

				moveto(0,270);
				lineto(getmaxx,270);
				moveto(0,275);
				lineto(getmaxx,275);

				moveto(0,320);
				lineto(getmaxx,320);
				moveto(0,325);
				lineto(getmaxx,325);

				moveto(0,425);
				lineto(getmaxx,425);

				moveto(0,520);
				lineto(getmaxx,520);
				moveto(0,525);
				lineto(getmaxx,525);

				//kontrole zaba

				if keypressed
				then begin 
					input:=readkey;


					//manual kontrole

					if (ord(input)=75) and (x0>35)
					then 
						dec(x0,25);
					if (ord(input)=80) and (y0<getmaxy-25) 
					then 
						inc(y0,25);
					if (ord(input)=77) and (x0<getmaxx-40) 
					then 
						inc(x0,25);
					if (ord(input)=72) and (y0>50) 
					then 
						dec(y0,25);

					if ord(input)=27 
					then 
						mbexit:=true;

					skipmm:=false;
					oke:=false;
					yp:=getmaxy div 2 + 40;

					if mbexit=true
					then begin
						repeat
							cleardevice;
							settextstyle(1,2,2);

							moveto(getmaxx div 2 -100, getmaxy div 2 - 20);
							outtext('Return to main menu');
							moveto(getmaxx div 2 -100, getmaxy div 2 + 10);
							outtext('Exit Game');
							moveto(getmaxx div 2 -100, getmaxy div 2 + 40);
							outtext('Continue');


							settextstyle(1,2,1);
							moveto(getmaxx div 2 - 110,yp+5);
							outtext('*');


							input:=readkey;

							//dolje
							if (ord(input)=80) and (yp+10<getmaxy div 2 + 40)
							then 
								inc(yp,30);
							//gore
							if (ord(input)=72) and (yp-10>getmaxy div 2 - 20)
							then 
								dec(yp,30);

							//return to main menu
							if (yp=getmaxy div 2 - 20) and (ord(input)=13)
							then begin
								level1:=false; 
								level2:=false; 
								rtmm:=true;
								finish:=true; 
								skipmm:=false; 
								oke:=true; 
							end;


							//continue
							if (yp=getmaxy div 2 + 40) and (ord(input)=13) 
							then begin 
								skipmm:=true; 
								oke:=true; 
							end;

							//exit game
							if (yp=getmaxy div 2 + 10) and (ord(input)=13) 
							then begin
								skipmm:=true; 
								finish:=true; 
								defexit:=true; 
								oke:=true;
							end;
						until oke=true;

						mbexit:=false;
					end;

					writeln(x0);
					writeln(y0);
				end;

				//zaba
				circle(x0,y0,25);
				settextstyle(1,2,1);
				outtextxy(x0+20,y0+19,'M');
				outtextxy(x0-25,y0+19,'M');
				circle(x0-10,y0+10,5);
				circle(x0+10,y0+10,5);

				outtextxy(x0+20,y0-20,'-');
				outtextxy(x0+25,y0-17,'/');
				outtextxy(x0+20,y0-15,'-');
				outtextxy(x0+27,y0-25,'W');

				outtextxy(x0-26,y0-20,'-');
				outtextxy(x0-31,y0-17,'\');
				outtextxy(x0-26,y0-15,'-');
				outtextxy(x0-33,y0-25,'W');

				if level1 
				then begin
					if dead or rtmm 
					then 
						lvl1;

					dead:=false;
					rtmm:=false;
					
					//1. auto
					setfillstyle(1,b[1]);
					x[1]:=x[1]-20;
					bar(x[1],100,x[1]+100,150);
					circle(x[1]+25,150,10);
					circle(x[1]+25,150,5);
					circle(x[1]+75,150,10);
					circle(x[1]+75,150,5);

					if x[1]+100<0 
					then begin
						x[1]:=getmaxx+250;
						b[1]:=random(6)+1;
						setfillstyle(1,b[1]);
					end;

					if (x0>x[1]) and (x0<x[1]+100) and (y0>=100) and (y0<=150) 
					then begin
						dec(lives); 
						dead:=true; 
						lvl1death:=true; 
					end;

					//2. auto
					setfillstyle(1,b[2]);
					x[2]:=x[2]-15;
					bar(x[2], 200, x[2]+100, 250);
					circle(x[2]+25,250,10);
					circle(x[2]+25,250,5);
					circle(x[2]+75,250,10);
					circle(x[2]+75,250,5);

					if x[2]+100<0 
					then begin
						b[2]:=random(6)+1;
						x[2]:=getmaxx+450;
					end;

					if (x0>x[2]) and (x0<x[2]+100) and (y0>=200) and (y0<=250) 
					then begin 
						dec(lives); 
						dead:=true; 
						lvl1death:=true; 
					end;

					//3. auto
					setfillstyle(1,b[3]);
					inc(x[3],25);
					bar(x[3], 350, x[3]+100, 400);
					circle(x[3]+25,400,10);
					circle(x[3]+25,400,5);
					circle(x[3]+75,400,10);
					circle(x[3]+75,400,5);

					if x[3]>getmaxx 
					then begin
						b[3]:=random(6)+1;
						x[3]:=-100;
					end;

					if (x0>x[3]) and (x0<x[3]+100) and (y0>=350) and (y0<=400) 
					then begin 
						dec(lives); 
						dead:=true; 
						lvl1death:=true; 
					end;

					//4. auto
					setfillstyle(1,b[4]);
					inc(x[4],15);
					bar(x[4], 450, x[4]+100, 500);
					circle(x[4]+25,500,10);
					circle(x[4]+25,500,5);
					circle(x[4]+75,500,10);
					circle(x[4]+75,500,5);

					if x[4]>getmaxx 
					then begin
						b[4]:=random(6)+1;
						x[4]:=-100;
					end;

					if (x0>x[4]) and (x0<x[4]+100) and (y0>=450) and (y0<=500) 
					then begin 
						dec(lives); 
						dead:=true; 
						lvl1death:=true; 
					end;

					//exit
					circle(735,575,25);
					if (x0=735) and (y0=575) 
					then begin
						level1:=false;
						level2:=true; 
						dead:=true; 
						y0:=50; 
					end;

					if not finish 
					then 
						outtextxy(735-14,575-4,'EXIT');

				end;

				if level2 
				then begin

					if dead and not level1 
					then 
						lvl2;
					
					dead:=false;
					rtmm:=false;

					//1. auto
					setfillstyle(1,b[5]);
					x[5]:=x[5]-20;
					bar(x[5],100,x[5]+100,150);
					circle(x[5]+25,150,10);
					circle(x[5]+25,150,5);
					circle(x[5]+75,150,10);
					circle(x[5]+75,150,5);

					if x[5]+100<0 
					then begin
						x[5]:=getmaxx+250;
						b[5]:=random(6)+1;
						setfillstyle(1,b[5]);
					end;

					if (x0>x[5]) and (x0<x[5]+100) and (y0>=100) and (y0<=150) 
					then begin 
						dec(lives); 
						dead:=true; 
						lvl2death:=true; 
					end;

					//2. auto
					setfillstyle(1,b[6]);
					x[6]:=x[6]-15;
					bar(x[6], 200, x[6]+100, 250);
					circle(x[6]+25,250,10);
					circle(x[6]+25,250,5);
					circle(x[6]+75,250,10);
					circle(x[6]+75,250,5);

					if x[6]+100<0 
					then begin
						b[6]:=random(6)+1;
						x[6]:=getmaxx+450;
					end;

					if (x0>x[6]) and (x0<x[6]+100) and (y0>=200) and (y0<=250) 
					then begin 
						dec(lives); 
						dead:=true; 
						lvl2death:=true; 
					end;

					//3. auto
					setfillstyle(1,b[7]);
					inc(x[7],25);
					bar(x[7], 350, x[7]+100, 400);
					circle(x[7]+25,400,10);
					circle(x[7]+25,400,5);
					circle(x[7]+75,400,10);
					circle(x[7]+75,400,5);

					if x[7]>getmaxx 
					then begin
						b[7]:=random(6)+1;
						x[7]:=-100;
					end;

					if (x0>x[7]) and (x0<x[7]+100) and (y0>=350) and (y0<=400) 
					then begin 
						dec(lives); 
						dead:=true; 
						lvl2death:=true; 				
					end;

					//4. auto
					setfillstyle(1,b[8]);
					inc(x[8],15);
					bar(x[8], 450, x[8]+100, 500);
					circle(x[8]+25,500,10);
					circle(x[8]+25,500,5);
					circle(x[8]+75,500,10);
					circle(x[8]+75,500,5);

					if x[8]>getmaxx 
					then begin
						b[8]:=random(6)+1;
						x[8]:=-100;
					end;

					if (x0>x[8]) and (x0<x[8]+100) and (y0>=450) and (y0<=500) 
					then begin 
						dec(lives); 
						dead:=true; 
						lvl2death:=true; 
					end;

					//exit
					circle(getmaxx-39-500,getmaxy-24,25);
					if (x0=getmaxx-39-500) and (y0=getmaxy-24) 
					then begin 
						finish:=true; 
						delay(1500); 
						level2:=false; 
						rtmm:=true; 
					end;
					if not finish 
					then 
						outtextxy(getmaxx-53-500,getmaxy-28,'EXIT');

				end;

				moveto(getmaxx - 100, 10);
				settextstyle(1,2,1);
				outtext('LIVES : ');
				case lives of 2 : outtext('2');
							  1 : outtext('1');
							  0 : outtext('0');
							  -1: outtext('-1');
				end;

				if lives=-1 
				then 
					defdead:=true;

			until finish or dead;

			//smrt
			if dead 
			then begin
				outtextxy(x0-13,y0+7,'X');
				outtextxy(x0+7,y0+7,'X');
				delay(1500);
				if defdead 
				then begin
					delay(500);
					level2:=false;
					rtmm:=true;
				end;
			end;
		end;
	until defexit=true;
end.
