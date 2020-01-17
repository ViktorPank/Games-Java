package com.javarush.games.minesweeper;
import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MinesweeperGame extends Game{

    private static final int SIDE = 12;
    private GameObject [][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField = 0;
    private  int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles = SIDE * SIDE;
    private int score = 0;

    private static final String FLAG = "\uD83D\uDEA9"; //изображение флага
    private static final String MINE = "\uD83D\uDCA3"; //изображение мины


    @Override
    public void initialize(){
        setScreenSize(SIDE,SIDE);
        createGame();

    }

    private void createGame(){
        for (int i = 0; i < SIDE ; i++) {//создание обьектов ячеек с минами  и без
            for (int j = 0; j < SIDE; j++) {

                if (getRandomNumber(8) == 2) {

                    gameField[i][j] = new GameObject(j, i,true );
                    setCellColor(j, i, Color.GRAY);
                    setCellValue(j,i,"");
                    countMinesOnField++;
                }
                else{
                    gameField[i][j] = new GameObject(j, i,false);
                    setCellColor(j, i, Color.GRAY);
                    setCellValue(j,i,"");
                }
                gameField[i][j].isFlag = false;
                gameField[i][j].isOpen = false;

            }
        }
        countMineNeighbors();//считаем количество мин на поле
        countFlags = countMinesOnField; //присваиваем кол-во мин количеству флагов




    }

    private List<GameObject> getNeighbors(GameObject object){


        int x = object.x;
        int y = object.y;
        List neighbors = new LinkedList<>();

        if((x - 1) >= 0 && (y - 1) >= 0) neighbors.add(gameField[y - 1][x - 1]);

        if((y - 1) >= 0) neighbors.add(gameField[y - 1][x]);

        if((y - 1) >= 0 && (x + 1) < SIDE) neighbors.add(gameField[y - 1][x + 1]);
        //------------------------------------------------

        if((x + 1) < SIDE) neighbors.add(gameField[y][x + 1]);
        if( (y + 1) < SIDE && (x + 1) < SIDE) neighbors.add(gameField[y + 1][x + 1]);

        if( (y + 1) < SIDE) neighbors.add(gameField[y + 1 ][x]);

        //------------------------------------------------

        if((x - 1) >= 0  && (y + 1) < SIDE) neighbors.add(gameField[y + 1][x - 1]);


        if((x - 1) >= 0 ) neighbors.add(gameField[y][x - 1]);

        return  neighbors;

    }


    private void countMineNeighbors(){

        List<GameObject> listNeighbors;

        for (int i = 0; i < SIDE ; i++) {//цикл для парсинга по столбцам
            for (int j = 0; j < SIDE; j++) {//цикл для парсинг по строкам
                if (!gameField [j][i].isMine){//проверка текущая ячейка не является миной
                    listNeighbors = getNeighbors(gameField[j][i]);//получение соседей текущей ячейки
                    for (GameObject buf :listNeighbors ) { // парсинг по соседям и....
                        if (buf.isMine) gameField[j][i].countMineNeighbors++;//...кто из из них является террористом подсчитываем

                    }
                }
            }
        }

    }

    @Override

    public void onMouseLeftClick(int x, int y) {  //обработчик события левой кнопки мыши
        if(!isGameStopped)  openTile(x, y);
        else restart();
    }

    private void openTile(int x, int y){//открытие ячейки


        if (gameField[y][x].isOpen && !gameField[y][x].isFlag){ //метод открывания соседей при нажатии на открытую ячейку с цифрой

            for (GameObject gameObject : getNeighbors(gameField[y][x])) {//проходим по его соседям , а....
                if (!gameObject.isOpen && !gameObject.isFlag) {//...если не открыта, то открываем ячейки  по рекурсии
                    openTile(gameObject.x, gameObject.y);
                }
            }

        }


        if (!gameField[y][x].isOpen && !gameField[y][x].isFlag) {

            gameField[y][x].isOpen = true;
            countClosedTiles--;

            if (gameField[y][x].isMine && !gameField[y][x].isFlag) {//если является бомбой...

                setCellValueEx(x, y, Color.RED, MINE);

                gameOver();
               /*     while(true){
                        for (int i = 0; i < SIDE; i++) {
                            for (int j = 0; j < SIDE; j++) {

                                String buffer = String.valueOf(gameField[j][i].countMineNeighbors);

                                setCellValue(j,i,buffer);//тест проверки количества минеров

                            }
                        }
                    }*/

            } else {//...или не является бомбой
                score +=5;//увеличиваем счет при правильном открытии
                setScore(score);//обновляем счет на экране
                setCellColor(x, y, Color.GREEN); //окрашиваем в зеленый открытую ячейку

                if (countClosedTiles == countMinesOnField)win();//проверка на полностью открытые ячейки без бомбы

                if (gameField[y][x].countMineNeighbors == 0) { //если у обычной ячейки нет соседей террористов то..

                    setCellValue(x, y, "");//...открываем текущую ячейку и .....

                    for (GameObject gameObject : getNeighbors(gameField[y][x])) {//....проходим по его соседям , а....
                        if (!gameObject.isOpen) {//...если не открыта, то открываем ячейки  по рекурсии
                            openTile(gameObject.x, gameObject.y);
                        }
                    }
                } else setCellNumber(x, y, gameField[y][x].countMineNeighbors);

            }

        }


    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x,y);
    }//обработчик события правой кнопки мыши

    private void markTile(int x,int y){
        if (!isGameStopped) {
            if (gameField[y][x].isFlag) {//если флаг ячейки активен то убираем его...
                if (!gameField[y][x].isOpen && countFlags < countMinesOnField) {//уборка флага

                    gameField[y][x].isFlag = false;
                    countFlags++;
                    setCellValue(x, y, "");
                    setCellColor(x, y, Color.GRAY);

                }
            } else {//...если нет то ставим его

                if (!gameField[y][x].isOpen && countFlags > 0) {//установка флага

                    gameField[y][x].isFlag = true;
                    countFlags--;
                    setCellValue(x, y, FLAG);
                    setCellColor(x, y, Color.YELLOW);

                }
            }
        }



    }


    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.NONE, "Сегодня тебе повезло, завтра нет.МУАХАХХА!!!",Color.WHITE,25);
    }

    private void gameOver(){

        isGameStopped = true;
        showMessageDialog(Color.NONE, "Ты проиграл, кожаное существо!!!",Color.WHITE,25);

    }

    private void restart(){ //сброс параметров и рестарт игры
        isGameStopped = false;
        score = 0;
        countClosedTiles = SIDE * SIDE;
        setScore(score);
        countMinesOnField = 0;
        countFlags = 0;
        createGame();



    }
}


