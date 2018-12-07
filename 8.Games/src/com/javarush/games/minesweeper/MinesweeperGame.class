package com.javarush.games.minesweeper;
import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MinesweeperGame extends Game{

    private static final int SIDE = 9;
    private GameObject [][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField = 0;


    @Override
    public void initialize(){
        setScreenSize(SIDE,SIDE);
        createGame();

    }

    private void createGame(){
        for (int i = 0; i < SIDE ; i++) {//создание обьектов ячеек с минами
            for (int j = 0; j < SIDE; j++) {
                if (getRandomNumber(10) == 2) {

                    gameField[j][i] = new GameObject(j, i,true );
                    setCellColor(j, i, Color.RED);
                    countMinesOnField++;
                }
                else{
                    gameField[j][i] = new GameObject(j, i,false);
                    setCellColor(j, i, Color.WHITE);
                }

            }
        }
        countMineNeighbors();
        
        /* Кусок кода для проверки корректного подсчета соседей-минеров
       for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
               
                String buffer = String.valueOf(gameField[j][i].countMineNeighbors);
                setCellValue(j,i,buffer);
            }
        }
        */
        
    }

    private List<GameObject> getNeighbors(GameObject object){


        int x = gameObject.x;
        int y = gameObject.y;

        List<GameObject> listNeighbors = new ArrayList<>();//создание листа соседей

        //Левый верхний сосед
        if((x - 1) >= 0 && (y - 1) >= 0) listNeighbors.add(gameField[x - 1][y - 1]); 
        
        //Верхний сосед
        if((y - 1) >= 0) listNeighbors.add(gameField[x][y - 1]);
        
        
        //Правый верхний сосед
        if((y - 1) >= 0 && (x + 1) < SIDE) listNeighbors.add(gameField[x + 1][y - 1]);
        //------------------------------------------------
        
        //Правый сосед
        if((x + 1) < SIDE) listNeighbors.add(gameField[x + 1][y]);
        
        //Правый нижний сосед
        if( (y + 1) < SIDE && (x + 1) < SIDE) listNeighbors.add(gameField[x + 1][y + 1]);

        //Нижний сосед
        if( (y + 1) < SIDE) listNeighbors.add(gameField[x ][y + 1]);

        //------------------------------------------------
        
        //Левый нижний сосед
        if((x - 1) >= 0  && (y + 1) < SIDE) listNeighbors.add(gameField[x - 1][y + 1]);

        //Левый сосед))
        if((x - 1) >= 0 ) listNeighbors.add(gameField[x - 1][y]);

        return  listNeighbors;
        
    }

    private void countMineNeighbors(){

        List<GameObject> listNeighbors;
        for (int i = 0; i < SIDE ; i++) {//цикл для парсинга по столбцам
            for (int j = 0; j < SIDE; j++) {//цикл для парсинг по строкам
                if (!gameField [j][i].isMine){//проверка текущая ячейка не является миной
                    listNeighbors = getNeighbors(gameField[j][i]);//получение соседей текущей ячейки
                    for (GameObject buf :listNeighbors ) { // парсинг по соседям и....
                        if (buf.isMine)gameField[j][i].countMineNeighbors++;//...кто из из них является террористом подсчитываем

                    }                    
                }
            }
        }

    }

}


